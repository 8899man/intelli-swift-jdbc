package com.fr.swift.query.session;

import com.fr.swift.query.query.QueryInfo;
import com.fr.swift.source.SwiftResultSet;

import java.io.Closeable;
import java.sql.SQLException;

/**
 * @author yee
 * @date 2018/6/19
 */
public interface Session extends Closeable {

    /**
     * 取的sessionId
     *
     * @return
     */
    String getSessionId();

    /**
     * 查询当前节点
     * @param queryInfo
     * @param <T>
     * @return
     * @throws SQLException
     */
    <T extends SwiftResultSet> T executeQuery(QueryInfo<T> queryInfo) throws SQLException;

    /**
     * 关闭并清理缓存
     */
    @Override
    void close();

    /**
     * session是否关闭
     * @return
     */
    boolean isClose();

    /**
     * 清理缓存
     * @param force true 无论怎样都清理 false 超时的清理
     */
    void cleanCache(boolean force);
}
