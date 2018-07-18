package com.fr.swift.collate;

import com.fr.stable.query.condition.QueryCondition;
import com.fr.stable.query.restriction.RestrictionFactory;
import com.fr.swift.config.service.SwiftSegmentServiceProvider;
import com.fr.swift.context.SwiftContext;
import com.fr.swift.cube.io.Types;
import com.fr.swift.db.Where;
import com.fr.swift.db.impl.SwiftWhere;
import com.fr.swift.generate.BaseTest;
import com.fr.swift.generate.history.index.ColumnIndexer;
import com.fr.swift.generate.segment.operator.inserter.BlockInserter;
import com.fr.swift.query.condition.SwiftQueryFactory;
import com.fr.swift.redis.RedisClient;
import com.fr.swift.segment.Decrementer;
import com.fr.swift.segment.Segment;
import com.fr.swift.segment.SwiftSegmentManager;
import com.fr.swift.segment.column.Column;
import com.fr.swift.segment.column.ColumnKey;
import com.fr.swift.segment.operator.Inserter;
import com.fr.swift.service.SwiftCollateService;
import com.fr.swift.source.DataSource;
import com.fr.swift.source.SwiftResultSet;
import com.fr.swift.source.SwiftSourceTransfer;
import com.fr.swift.source.SwiftSourceTransferFactory;
import com.fr.swift.source.db.QueryDBSource;
import com.fr.swift.task.service.SwiftServiceTaskExecutor;
import junit.framework.TestCase;
import org.junit.Test;

import java.util.List;

import static junit.framework.TestCase.assertTrue;

/**
 * This class created on 2018/7/10
 *
 * @author Lucifer
 * @description 历史块合并成历史块。合并时剔除增量删除掉的数据
 * @since Advanced FineBI 5.0
 */
public class HistoryCollateTest extends BaseTest {

    private RedisClient redisClient;

    private SwiftSegmentManager swiftSegmentManager;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        SwiftContext.init();
        redisClient = (RedisClient) SwiftContext.get().getBean("redisClient");
        swiftSegmentManager = SwiftContext.get().getBean("localSegmentProvider", SwiftSegmentManager.class);
    }

    @Test
    public void testAutoHistoryCollate() {
        try {
            DataSource dataSource = new QueryDBSource("select * from DEMO_CONTRACT", "testHistoryCollate");
            SwiftSegmentServiceProvider.getProvider().removeSegments(dataSource.getSourceKey().getId());
            SwiftSourceTransfer transfer = SwiftSourceTransferFactory.createSourceTransfer(dataSource);
            SwiftResultSet resultSet = transfer.createResultSet();
            Inserter inserter = new BlockInserter(dataSource.getSourceKey(), dataSource.getSourceKey().getId(), dataSource.getMetadata());
            inserter.insertData(resultSet);

            List<Segment> segments = swiftSegmentManager.getSegment(dataSource.getSourceKey());
            for (String fieldName : dataSource.getMetadata().getFieldNames()) {
                ColumnIndexer columnIndexer = new ColumnIndexer(dataSource, new ColumnKey(fieldName), segments);
                columnIndexer.work();
            }

            QueryCondition eqQueryCondition = SwiftQueryFactory.create().addRestriction(RestrictionFactory.eq("合同类型", "购买合同"));
            Where where = new SwiftWhere(eqQueryCondition);
            //合并前1块历史块，且只要allshow是购买合同
            assertTrue(segments.size() == 1);
            for (Segment segment : segments) {
                Decrementer decrementer = new Decrementer(segment);
                decrementer.delete(dataSource.getSourceKey(), where);
                assertTrue(segment.getLocation().getStoreType() == Types.StoreType.FINE_IO);
                Column column = segment.getColumn(new ColumnKey("合同类型"));
                int neqCount = 0;
                for (int i = 0; i < segment.getRowCount(); i++) {
                    if (segment.getAllShowIndex().contains(i)) {
                        TestCase.assertEquals(column.getDetailColumn().get(i), "购买合同");
                    } else {
                        neqCount++;
                    }
                }
                assertTrue(neqCount != 0);
            }
            //合并历史块，直接写history
            SwiftCollateService collaterService = new SwiftCollateService();
            collaterService.setTaskExecutor(new SwiftServiceTaskExecutor("testAutoHistoryCollate", 1));
            collaterService.autoCollateHistory(dataSource.getSourceKey());
            Thread.sleep(5000l);
            segments = swiftSegmentManager.getSegment(dataSource.getSourceKey());
            assertTrue(segments.size() == 1);
            //合并后1块历史块，所有数据都是购买合同
            for (Segment segment : segments) {
                assertTrue(segment.getLocation().getStoreType() == Types.StoreType.FINE_IO);
                Column column = segment.getColumn(new ColumnKey("合同类型"));
                for (int i = 0; i < segment.getRowCount(); i++) {
                    TestCase.assertEquals(column.getDetailColumn().get(i), "购买合同");
                }
            }
        } catch (Exception e) {
            assertTrue(false);
        }
    }

}
