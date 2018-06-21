package com.fr.swift.segment.operator.insert;

import com.fr.swift.bitmap.BitMaps;
import com.fr.swift.config.bean.SegmentKeyBean;
import com.fr.swift.config.service.SwiftSegmentService;
import com.fr.swift.config.service.SwiftSegmentServiceProvider;
import com.fr.swift.context.SwiftContext;
import com.fr.swift.cube.io.Types;
import com.fr.swift.cube.io.location.IResourceLocation;
import com.fr.swift.cube.io.location.ResourceLocation;
import com.fr.swift.db.impl.SwiftDatabase;
import com.fr.swift.log.SwiftLogger;
import com.fr.swift.log.SwiftLoggers;
import com.fr.swift.segment.Segment;
import com.fr.swift.segment.SegmentIndexCache;
import com.fr.swift.segment.SegmentKey;
import com.fr.swift.segment.SwiftSegmentManager;
import com.fr.swift.segment.column.ColumnKey;
import com.fr.swift.segment.operator.Inserter;
import com.fr.swift.segment.operator.Recorder;
import com.fr.swift.segment.operator.utils.InserterUtils;
import com.fr.swift.source.ColumnTypeConstants;
import com.fr.swift.source.ColumnTypeUtils;
import com.fr.swift.source.Row;
import com.fr.swift.source.SourceKey;
import com.fr.swift.source.SwiftMetaData;
import com.fr.swift.source.SwiftMetaDataColumn;
import com.fr.swift.source.SwiftResultSet;
import com.fr.swift.source.alloter.SwiftSourceAlloter;
import com.fr.swift.source.alloter.SwiftSourceAlloterFactory;
import com.fr.swift.source.alloter.line.LineRowInfo;
import com.fr.swift.util.Crasher;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * This class created on 2018/3/27
 *
 * @author Lucifer
 * @description 指定数据，分块逻辑在内部计算
 * @since Advanced FineBI Analysis 1.0
 */
@Deprecated
public abstract class AbstractBlockInserter implements Inserter, Recorder {
    private static final SwiftLogger LOGGER = SwiftLoggers.getLogger(AbstractBlockInserter.class);

    protected SourceKey sourceKey;
    protected String cubeSourceKey;
    protected SwiftMetaData swiftMetaData;
    protected List<String> fields;
    protected List<Segment> segments;
    private List<SegmentKey> configSegment;
    private SwiftSourceAlloter alloter;
    private SegmentIndexCache segmentIndexCache;
    private int startSegIndex;
    private SwiftSegmentService segmentService = SwiftSegmentServiceProvider.getProvider();

    public AbstractBlockInserter(SourceKey sourceKey, String cubeSourceKey, SwiftMetaData swiftMetaData) {
        this(sourceKey, cubeSourceKey, swiftMetaData, swiftMetaData.getFieldNames());
    }

    public AbstractBlockInserter(SourceKey sourceKey, String cubeSourceKey, SwiftMetaData swiftMetaData, List<String> fields) {
        this(new ArrayList<Segment>(), sourceKey, cubeSourceKey, swiftMetaData, fields);
    }

    public AbstractBlockInserter(List<Segment> segments, SourceKey sourceKey, String cubeSourceKey, SwiftMetaData swiftMetaData) {
        this(segments, sourceKey, cubeSourceKey, swiftMetaData, swiftMetaData.getFieldNames());
    }

    public AbstractBlockInserter(List<Segment> segments, SourceKey sourceKey, String cubeSourceKey, SwiftMetaData swiftMetaData, List<String> fields) {
        this.sourceKey = sourceKey;
        this.cubeSourceKey = cubeSourceKey;
        this.swiftMetaData = swiftMetaData;
        this.fields = fields;
        this.alloter = SwiftSourceAlloterFactory.createLineSourceAlloter(sourceKey, cubeSourceKey);
        this.segments = new ArrayList<Segment>();
        this.configSegment = new ArrayList<SegmentKey>();
        this.segments = segments;
        this.segmentIndexCache = new SegmentIndexCache();
        this.startSegIndex = segments.size();
        for (int i = 0; i < segments.size(); i++) {
            if (segments.get(i).isHistory()) {
                createSegment(i, Types.StoreType.FINE_IO);
            } else {
                createSegment(i, Types.StoreType.MEMORY);
            }
        }
    }

    @Override
    public List<Segment> insertData(List<Row> rowList) {
        return Collections.emptyList();
    }

    @Override
    public List<Segment> insertData(SwiftResultSet swiftResultSet) throws SQLException {
        if (!fields.isEmpty()) {
            List<Segment> newSegments = new ArrayList<Segment>();
            try {
                long count = 0;
                String allotColumn = fields.get(0);
                while (swiftResultSet.next()) {
                    Row rowData = swiftResultSet.getRowData();
                    int size = segments.size();
                    int index = alloter.allot(new LineRowInfo(count)).getOrder() + startSegIndex;
                    if (index >= size) {
                        for (int i = size; i <= index; i++) {
                            segmentIndexCache.putSegRow(i, 0);
                            Segment newSegment = createSegment(i);
                            segments.add(newSegment);
                            newSegments.add(newSegment);
                        }
                    } else if (index == -1) {
                        index = segments.size() - 1;
                    }
                    recordData(rowData, index);
                    int segmentRow = segmentIndexCache.getSegRowByIndex(index);
                    Segment segment = segments.get(index);
                    segmentIndexCache.putSegment(index, segment);
                    for (int i = 0; i < fields.size(); i++) {
                        if (InserterUtils.isBusinessNullValue(rowData.getValue(i))) {
                            SwiftMetaDataColumn metaDataColumn = swiftMetaData.getColumn(fields.get(i));
                            ColumnTypeConstants.ClassType clazz = ColumnTypeUtils.getClassType(metaDataColumn);
                            segment.getColumn(new ColumnKey(fields.get(i))).getDetailColumn().put(segmentRow, InserterUtils.getNullValue(clazz));
                            segmentIndexCache.putSegFieldNull(index, fields.get(i), segmentRow);
                        } else {
                            segment.getColumn(new ColumnKey(fields.get(i))).getDetailColumn().put(segmentRow, rowData.getValue(i));
                        }
                    }
                    segmentIndexCache.putSegRow(index, ++segmentRow);
                    count++;
                }
            } catch (Exception e) {
                SwiftLoggers.getLogger().error(e);
            } finally {
                swiftResultSet.close();
            }
            release();
            end();
            return newSegments;
        } else {
            List<Segment> cubeSourceSegments = SwiftContext.getInstance().getBean(SwiftSegmentManager.class).getSegment(new SourceKey(cubeSourceKey));
            for (int i = 0; i < cubeSourceSegments.size(); i++) {
                Segment segment = cubeSourceSegments.get(i);
                createSegment(i, segment.isHistory() ? Types.StoreType.FINE_IO : Types.StoreType.MEMORY);
            }
            release();
            return Collections.emptyList();
        }
    }

    protected abstract Segment createSegment(int order);

    /**
     * 创建Segment
     * TODO 分块存放目录
     *
     * @param order 块号
     * @return
     * @throws Exception
     */
    protected Segment createSegment(int order, Types.StoreType storeType) {
        String cubePath = String.format("%s/%s/seg%d",
                swiftMetaData.getSwiftSchema().getDir(),
                cubeSourceKey, order);
        IResourceLocation location = new ResourceLocation(cubePath, storeType);
        configSegment.add(new SegmentKeyBean(sourceKey.getId(), location.getUri(), order, storeType));
        return createNewSegment(location, swiftMetaData);
    }

    protected abstract Segment createNewSegment(IResourceLocation location, SwiftMetaData swiftMetaData);

    public void release() {
        persistMeta();
        persistSegment();

        for (Map.Entry<Integer, Segment> entry : segmentIndexCache.getNewSegMap().entrySet()) {
            Segment segment = entry.getValue();
            segment.putAllShowIndex(BitMaps.newAllShowBitMap(segmentIndexCache.getSegRowByIndex(entry.getKey())));
            segment.putRowCount(segmentIndexCache.getSegRowByIndex(entry.getKey()));
            if (segment.isHistory()) {
                for (String field : fields) {
                    segment.getColumn(new ColumnKey(field)).getBitmapIndex().putNullIndex(segmentIndexCache.getNullBySegAndField(entry.getKey(), field));
                    segment.getColumn(new ColumnKey(field)).getBitmapIndex().release();
                    segment.getColumn(new ColumnKey(field)).getDetailColumn().release();
                }
                segment.release();
            } else {
                for (String field : fields) {
                    segment.getColumn(new ColumnKey(field)).getBitmapIndex().putNullIndex(segmentIndexCache.getNullBySegAndField(entry.getKey(), field));
                }
            }
        }
    }

    private void persistMeta() {
        try {
            if (!SwiftDatabase.getInstance().existsTable(sourceKey)) {
                SwiftDatabase.getInstance().createTable(sourceKey, swiftMetaData);
            }
        } catch (SQLException e) {
            LOGGER.error("save metadata failed! ", e);
            Crasher.crash(e);
        }
    }

    private void persistSegment() {
        segmentService.updateSegments(sourceKey.getId(), configSegment);
    }

    @Override
    public List<String> getFields() {
        return fields;
    }

    @Override
    public void recordData(Row row, int segIndex) {
    }

    @Override
    public void end() {
    }
}
