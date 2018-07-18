package com.fr.swift.segment.recover;

import com.fr.swift.config.TestConfDb;
import com.fr.swift.context.SwiftContext;
import com.fr.swift.cube.io.ResourceDiscovery;
import com.fr.swift.cube.io.Types.StoreType;
import com.fr.swift.cube.io.location.IResourceLocation;
import com.fr.swift.cube.io.location.ResourceLocation;
import com.fr.swift.segment.Incrementer;
import com.fr.swift.segment.RealTimeSegmentImpl;
import com.fr.swift.segment.Segment;
import com.fr.swift.segment.SwiftDataOperatorProvider;
import com.fr.swift.segment.column.ColumnKey;
import com.fr.swift.source.SwiftResultSet;
import com.fr.swift.source.db.ConnectionInfo;
import com.fr.swift.source.db.QueryDBSource;
import com.fr.swift.source.db.QuerySourceTransfer;
import com.fr.swift.source.db.TestConnectionProvider;
import com.fr.swift.test.Preparer;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

/**
 * @author anchore
 * @date 2018/5/25
 */
public class SegmentRecoveryTest {
    public SwiftDataOperatorProvider operators;

    public QueryDBSource dataSource;

    private ConnectionInfo connectionInfo;


    @BeforeClass
    public static void boot() throws Exception {
        Preparer.prepareCubeBuild();
    }


    @Before
    public void setUp() throws Exception {
        TestConfDb.setConfDb();
        connectionInfo = TestConnectionProvider.createConnection();
        dataSource = new QueryDBSource("select * from DEMO_CAPITAL_RETURN", SegmentRecoveryTest.class.getName());
        operators = SwiftContext.get().getBean(SwiftDataOperatorProvider.class);
    }

    @After
    public void tearDown() {
    }

    @Ignore
    @Test
    public void recover() throws Exception {
        QuerySourceTransfer transfer = new QuerySourceTransfer(connectionInfo, dataSource.getMetadata(), dataSource.getMetadata(), dataSource.getQuery());
        SwiftResultSet swiftResultSet = transfer.createResultSet();

        Incrementer incrementer = new Incrementer(dataSource);
        incrementer.increment(swiftResultSet);

        String tablePath = String.format("%s/%s",
                dataSource.getMetadata().getSwiftSchema().getDir(),
                dataSource.getSourceKey().getId());
        ResourceDiscovery.getInstance().removeCubeResource(tablePath);
        SegmentRecovery segmentRecovery = (SegmentRecovery) SwiftContext.get().getBean("segmentRecovery");
        segmentRecovery.recoverAll();

        String cubePath = tablePath + "/seg0";
        IResourceLocation location = new ResourceLocation(cubePath, StoreType.MEMORY);
        Segment segment = new RealTimeSegmentImpl(location, dataSource.getMetadata());
        for (int i = 0; i < segment.getRowCount(); i++) {
            segment.getColumn(new ColumnKey("付款金额")).getDetailColumn().get(i);
        }
    }

    private Segment newRealtimeSegment() {
        return new RealTimeSegmentImpl(new ResourceLocation(
                String.format("%s/%s/seg0",
                        dataSource.getMetadata().getSwiftSchema().getDir(),
                        dataSource.getSourceKey().getId()), StoreType.MEMORY), dataSource.getMetadata());
    }
}