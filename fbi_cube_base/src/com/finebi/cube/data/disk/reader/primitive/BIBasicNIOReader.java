package com.finebi.cube.data.disk.reader.primitive;

import com.finebi.cube.data.ICubeSourceReleaseManager;
import com.finebi.cube.data.disk.NIOHandlerManager;
import com.finebi.cube.data.input.primitive.ICubePrimitiveReader;
import com.finebi.cube.exception.BIResourceInvalidException;
import com.fr.bi.stable.io.newio.NIOConstant;
import com.finebi.cube.common.log.BILoggerFactory;
import com.fr.bi.stable.utils.mem.BIReleaseUtils;
import com.fr.bi.stable.utils.program.BINonValueUtils;

import java.io.*;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public abstract class BIBasicNIOReader implements ICubePrimitiveReader {

    private final static int INIT_INDEX_LENGTH = 128;
    protected Map<Integer, MappedByteBuffer> buffers = new ConcurrentHashMap<Integer, MappedByteBuffer>();
    protected transient MappedByteBuffer[] bufferArray = new MappedByteBuffer[1];
    protected final ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    protected Map<Integer, FileChannel> fcMap = new ConcurrentHashMap<Integer, FileChannel>();
    boolean[] initIndex = new boolean[INIT_INDEX_LENGTH];
    protected volatile boolean isValid = true;
    private ICubeSourceReleaseManager releaseManager;
    private NIOHandlerManager nioHandlerManager;

    protected File baseFile;
    private String readerHandler;

    public BIBasicNIOReader(File cacheFile) {
        this.baseFile = cacheFile;
        this.isValid = true;
        readerHandler = UUID.randomUUID().toString();
    }

    @Override
    public String getReaderHandler() {
        return readerHandler;
    }

    public BIBasicNIOReader(String cacheFilePath) {
        this(new File(cacheFilePath));
    }

    protected abstract long getPageModeValue();

    protected abstract long getPageStep();

    protected abstract void releaseChild();

    protected int getPage(long filePosition) throws BIResourceInvalidException {
        if (filePosition < 0) {
            throw BINonValueUtils.illegalArgument("The value of argument must be positive,but it's " + filePosition + " now");
        }
        if (isValid) {
            int index = (int) (filePosition >> getPageStep() >> NIOConstant.MAX_SINGLE_FILE_PART_SIZE);
            initBuffer(index);
            return index;
        } else {
            throw new BIResourceInvalidException();
        }

    }

    protected int getIndex(long filePosition) {
        return (int) (filePosition & getPageModeValue());
    }

    private boolean buffersContains(int index) {
        if (bufferArray.length <= index) {
            synchronized (this) {
                if (bufferArray.length <= index) {
                    MappedByteBuffer[] temp = new MappedByteBuffer[index + 1];
                    System.arraycopy(bufferArray, 0, temp, 0, bufferArray.length);
                    bufferArray = temp;
                }
            }
            return false;
        }
        return bufferArray[index] != null;
    }

    private void initBuffer(int index) {
        if (!buffersContains(index)) {
            /**
             * 资源不可用，需要初始化，释放读锁，加写锁。
             */
            readWriteLock.writeLock().lock();
            try {
                /**
                 * 重新检查是否有其它线程已经初始化了
                 */
                if (!buffersContains(index)) {
                    initialBuffer(index);
                }
                /**
                 * 添加读锁
                 */
            } catch (IOException e) {
                BILoggerFactory.getLogger().error(e.getMessage(), e);
            } finally {
                /**
                 * 释放写锁，保持读锁
                 */
                readWriteLock.writeLock().unlock();
            }
        }
    }

    @Override
    public void releaseHandler(String readerHandler) {
        if (useNioHandlerManager()) {
            nioHandlerManager.releaseHandler(readerHandler);
        } else {
            releaseSource();
        }
    }

    @Override
    public void forceRelease() {
        if (useNioHandlerManager()) {
            nioHandlerManager.destroyHandler();
        } else {
            destroySource();
        }
    }

    @Override
    public boolean isForceReleased() {
        return !isValid;
    }

    private void unMap() throws IOException {
        releaseChild();
        releaseBuffer();
        releaseChannel();
    }

    @Override
    public void destroySource() {
        try {
            readWriteLock.writeLock().lock();
            if (!isValid) {
                return;
            }
            //先改变isValid状态再判断canClear
            isValid = false;
            try {
                //但愿10ms能 执行完get方法否则可能导致jvm崩溃
                //锁太浪费资源了，10ms目前并没有遇到问题
                //daniel:改成1ms，最垃圾的磁盘也读完了
                Thread.currentThread().sleep(1);
            } catch (InterruptedException e) {
                BILoggerFactory.getLogger().error(e.getMessage(), e);
            }
            unMap();
        } catch (IOException e) {
            BILoggerFactory.getLogger().error(e.getMessage(), e);
        } finally {
            readWriteLock.writeLock().unlock();
        }
    }

    @Override
    public void reSetValid(boolean isValid) {
        this.isValid = isValid;
    }

    public void releaseSource() {
        try {
            readWriteLock.writeLock().lock();
            if (!isValid) {
                return;
            }
            //先改变isValid状态再判断canClear
            isValid = false;
            try {
                //但愿10ms能 执行完get方法否则可能导致jvm崩溃
                //锁太浪费资源了，10ms目前并没有遇到问题
                //daniel:改成1ms，最垃圾的磁盘也读完了
                Thread.currentThread().sleep(1);
            } catch (InterruptedException e) {
                BILoggerFactory.getLogger().error(e.getMessage(), e);
            }
            unMap();
        } catch (IOException e) {
            BILoggerFactory.getLogger().error(e.getMessage(), e);
        } finally {
            isValid = true;
            readWriteLock.writeLock().unlock();
        }
    }

    private boolean useReleaseManager() {
        return releaseManager != null;
    }

    private boolean useNioHandlerManager() {
        return nioHandlerManager != null;
    }
    public void releaseBuffer() {
        for (Entry<Integer, MappedByteBuffer> entry : buffers.entrySet()) {
            BIReleaseUtils.doClean(entry.getValue());
        }
        buffers.clear();
        bufferArray = new MappedByteBuffer[1];
    }

    private void releaseChannel() throws IOException {
        if (fcMap != null) {
            for (FileChannel fc : fcMap.values()) {
                if (fc != null) {
                    fc.close();
                }
            }
            fcMap.clear();
        }
    }

    private void initialBuffer(int index) throws IOException {
        FileChannel channel = initFile(index);
        MappedByteBuffer buffer = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());
        initChild(index, buffer);
        buffers.put(index, buffer);
        bufferArray[index] = buffer;
        fcMap.put(index, channel);
    }

    @Override
    public void setReleaseHelper(ICubeSourceReleaseManager releaseHelper) {
        this.releaseManager = releaseHelper;
    }

    @Override
    public void setHandlerReleaseHelper(NIOHandlerManager releaseHelper) {
        this.nioHandlerManager = releaseHelper;
    }
    @Override
    public NIOHandlerManager getHandlerReleaseHelper(){
        return this.nioHandlerManager;
    }
    protected abstract void initChild(int index, MappedByteBuffer buffer);

    private FileChannel initFile(long fileIndex) {
        //兼容之前的
        File cacheFile = null;
        if (fileIndex == 0) {
            cacheFile = baseFile;
        } else {
            cacheFile = new File(baseFile.getAbsolutePath() + "_" + fileIndex);
        }
        try {
            return new RandomAccessFile(cacheFile, "r").getChannel();
        } catch (FileNotFoundException e) {
            BILoggerFactory.getLogger().error(e.getMessage(), e);
        }
        return null;
    }

    @Override
    public boolean canReader() {
        return isValid && baseFile.exists() && baseFile.length() > 0;
    }
}