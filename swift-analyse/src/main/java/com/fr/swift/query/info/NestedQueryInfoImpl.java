package com.fr.swift.query.info;

import com.fr.swift.query.QueryInfo;
import com.fr.swift.query.QueryType;
import com.fr.swift.query.info.group.post.PostQueryInfo;
import com.fr.swift.result.NodeResultSet;

import java.util.List;

/**
 * 只支持处理返回NodeResultSet的子查询
 * <p>
 * Created by Lyon on 2018/6/6.
 */
public class NestedQueryInfoImpl implements NestedQueryInfo<NodeResultSet> {

    private String queryId;
    private QueryInfo<NodeResultSet> queryInfo;
    private List<PostQueryInfo> postQueryInfoList;

    public NestedQueryInfoImpl(String queryId, QueryInfo<NodeResultSet> queryInfo, List<PostQueryInfo> postQueryInfoList) {
        this.queryId = queryId;
        this.queryInfo = queryInfo;
        this.postQueryInfoList = postQueryInfoList;
    }

    @Override
    public QueryInfo<NodeResultSet> getSubQueryInfo() {
        return queryInfo;
    }

    @Override
    public List<PostQueryInfo> getPostQueryInfoList() {
        return postQueryInfoList;
    }

    @Override
    public String getQueryId() {
        return queryId;
    }

    @Override
    public QueryType getType() {
        return QueryType.NEST;
    }
}
