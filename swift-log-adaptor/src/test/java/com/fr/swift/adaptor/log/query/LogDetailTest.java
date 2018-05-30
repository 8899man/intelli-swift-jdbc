package com.fr.swift.adaptor.log.query;

import com.fr.general.DataList;
import com.fr.stable.query.QueryFactory;
import com.fr.stable.query.condition.QueryCondition;
import com.fr.swift.adaptor.log.LogOperatorProxy;
import com.fr.swift.db.Database;
import com.fr.swift.db.Table;
import com.fr.swift.db.impl.SwiftDatabase;
import com.fr.swift.source.DataSource;
import com.fr.swift.source.SourceKey;
import com.fr.swift.source.db.QueryDBSource;

/**
 * This class created on 2018/4/27
 *
 * @author Lucifer
 * @description
 * @since Advanced FineBI 5.0
 */
public class LogDetailTest extends LogBaseTest {

    private final Database db = SwiftDatabase.getInstance();

    public void testFind() {
        try {
            DataSource dataSource = new QueryDBSource("select * from DEMO_CONTRACT", "DEMO_CONTRACT");
            if (!db.existsTable(new SourceKey("DEMO_CONTRACT"))) {
                db.createTable(new SourceKey("DEMO_CONTRACT"), dataSource.getMetadata());
            }
            Table table = db.getTable(new SourceKey("DEMO_CONTRACT"));
            transportAndIndex(dataSource, table);

            QueryCondition sortQueryCondition = QueryFactory.create();
            DataList dataList = LogOperatorProxy.getInstance().find(ContractBean.class, sortQueryCondition);
            assertTrue(!dataList.getList().isEmpty());
        } catch (Exception e) {
            LOGGER.error(e);
            assertTrue(false);
        }
    }
}
