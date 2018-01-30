package com.fr.swift.adaptor.executor;

import com.finebi.conf.structure.bean.field.FineBusinessField;
import com.finebi.conf.structure.bean.table.FineBusinessTable;
import com.finebi.conf.structure.result.BIDetailCell;
import com.finebi.conf.structure.result.BIDetailTableResult;
import com.fr.general.ComparatorUtils;
import com.fr.swift.adaptor.struct.SwiftCombineDetailResult;
import com.fr.swift.adaptor.struct.SwiftDetailTableResult;
import com.fr.swift.adaptor.struct.SwiftEmptyResult;
import com.fr.swift.adaptor.transformer.IndexingDataSourceFactory;
import com.fr.swift.source.DataSource;
import com.fr.swift.source.SwiftResultSet;
import com.fr.swift.source.SwiftSourceTransfer;
import com.fr.swift.source.SwiftSourceTransferFactory;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * This class created on 2018-1-29 12:02:53
 *
 * @author Lucifer
 * @description
 * @since Advanced FineBI Analysis 1.0
 */
public class SwiftFieldsDataPreview {

    private SwiftTableEngineExecutor swiftTableEngineExecutor;

    public SwiftFieldsDataPreview() {
        swiftTableEngineExecutor = new SwiftTableEngineExecutor();
    }

    public BIDetailTableResult getDetailPreviewByFields(DataSource dataSource, int rowCount) throws Exception {

        try {
            if (dataSource != null) {
                SwiftSourceTransfer transfer = SwiftSourceTransferFactory.createSourcePreviewTransfer(dataSource, rowCount);
                SwiftResultSet swiftResultSet = transfer.createResultSet();
                BIDetailTableResult detailTableResult = new SwiftDetailTableResult(swiftResultSet);
                return detailTableResult;
            }
            return new SwiftDetailTableResult(new SwiftEmptyResult());
        } catch (Exception e) {
            e.printStackTrace();
            return new SwiftDetailTableResult(new SwiftEmptyResult());
        }
//        SwiftSourceTransfer transfer = SwiftSourceTransferFactory.createSourcePreviewTransfer(dataSource, rowCount);
//        SwiftResultSet swiftResultSet = transfer.createResultSet();
//        BIDetailTableResult detailTableResult = new SwiftDetailTableResult(swiftResultSet);
//        return detailTableResult;
//        List<List<BIDetailCell>> columnDataLists = new ArrayList<List<BIDetailCell>>();
//        int realRowCount = 0;
//
//        for (int i = 0; i < columnDataLists.size(); i++) {
//            ColumnKey[] columns = columnKeys.get(i);
//            DataSource dataSource = dataSources.get(i);
//
//            SwiftSourceTransfer transfer = SwiftSourceTransferFactory.createSourcePreviewTransfer(dataSource, rowCount);
//            SwiftResultSet swiftResultSet = transfer.createResultSet();
//            BIDetailTableResult detailTableResult = new SwiftDetailTableResult(swiftResultSet);
//
//            for (int j = 0; j < columns.length; j++) {
//                ColumnKey currentKey = columns[i];
//                int index = 0;
//                for (int k = 1; k <= dataSource.getMetadata().getColumnCount(); k++) {
//                    if (ComparatorUtils.equals(currentKey.getName(), dataSource.getMetadata().getColumnName(i))) {
//                        index = k;
//                        break;
//                    }
//                }
//                List<BIDetailCell> columnDataList = new ArrayList<BIDetailCell>();
//                if (index != 0) {
//                    while (detailTableResult.hasNext()) {
//                        columnDataList.add(detailTableResult.next().get(index - 1));
//                    }
//                    realRowCount = columnDataList.size();
//                }
//                columnDataLists.add(columnDataList);
//            }
//        }
//
//        BIDetailTableResult result = new SwiftCombineDetailResult(columnDataLists, realRowCount);
//        return result;
    }

    public BIDetailTableResult getDetailPreviewByFields(LinkedHashMap<FineBusinessField, FineBusinessTable> fieldTableMap, int rowCount) throws Exception {
        List<List<BIDetailCell>> columnDataLists = new ArrayList<List<BIDetailCell>>();
        int realRowCount = 0;
        for (Map.Entry<FineBusinessField, FineBusinessTable> entry : fieldTableMap.entrySet()) {
            DataSource dataSource = IndexingDataSourceFactory.transformDataSource(entry.getValue());
            BIDetailTableResult detailTableResult = swiftTableEngineExecutor.getPreviewData(entry.getValue(), rowCount);
            int index = 0;
            for (int i = 1; i <= dataSource.getMetadata().getColumnCount(); i++) {
                if (ComparatorUtils.equals(entry.getKey().getName(), dataSource.getMetadata().getColumnName(i))) {
                    index = i;
                    break;
                }
            }
            List<BIDetailCell> columnDataList = new ArrayList<BIDetailCell>();
            if (index != 0) {
                while (detailTableResult.hasNext()) {
                    columnDataList.add(detailTableResult.next().get(index - 1));
                }
                realRowCount = columnDataList.size();
            }
            columnDataLists.add(columnDataList);
        }
        BIDetailTableResult result = new SwiftCombineDetailResult(columnDataLists, realRowCount);
        return result;
    }
}

