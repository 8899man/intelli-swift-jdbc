package com.fr.swift.source.db;

import com.fr.base.FRContext;
import com.fr.data.core.db.DBUtils;
import com.fr.data.core.db.dialect.Dialect;
import com.fr.data.core.db.dialect.DialectFactory;
import com.fr.data.core.db.dialect.base.DialectKeyConstants;
import com.fr.data.core.db.dialect.base.key.create.statement.limit.DialectCreateLimitedFetchedStatementParameter;
import com.fr.general.DateUtils;
import com.fr.stable.StringUtils;
import com.fr.swift.exception.meta.SwiftMetaDataException;
import com.fr.swift.log.SwiftLogger;
import com.fr.swift.log.SwiftLoggers;
import com.fr.swift.retry.RetryLoop;
import com.fr.swift.retry.RetryNTimes;
import com.fr.swift.setting.PerformancePlugManager;
import com.fr.swift.source.ColumnTypeConstants.ColumnType;
import com.fr.swift.source.ColumnTypeUtils;
import com.fr.swift.source.SwiftMetaData;
import com.fr.swift.source.SwiftMetaDataColumn;
import com.fr.swift.source.SwiftResultSet;
import com.fr.swift.source.SwiftSourceTransfer;
import com.fr.swift.source.db.dbdealer.DBDealer;
import com.fr.swift.source.db.dbdealer.Date2StringConvertDealer;
import com.fr.swift.source.db.dbdealer.DateDealer;
import com.fr.swift.source.db.dbdealer.DoubleDealer;
import com.fr.swift.source.db.dbdealer.LongDealer;
import com.fr.swift.source.db.dbdealer.Number2DateConvertDealer;
import com.fr.swift.source.db.dbdealer.Number2StringConvertDealer;
import com.fr.swift.source.db.dbdealer.String2DateConvertDealer;
import com.fr.swift.source.db.dbdealer.String2NumberConvertDealer;
import com.fr.swift.source.db.dbdealer.StringDealer;
import com.fr.swift.source.db.dbdealer.StringDealerWithCharSet;
import com.fr.swift.source.db.dbdealer.TimeDealer;
import com.fr.swift.source.db.dbdealer.TimestampDealer;
import com.fr.swift.util.Util;

import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * Created by pony on 2017/12/5.
 */
public abstract class AbstractQueryTransfer implements SwiftSourceTransfer {
    private static final SwiftLogger LOGGER = SwiftLoggers.getLogger(AbstractQueryTransfer.class);
    protected ConnectionInfo connectionInfo;

    public AbstractQueryTransfer(ConnectionInfo connectionInfo) {
        Util.requireNonNull(connectionInfo);
        this.connectionInfo = connectionInfo;
    }

    @Override
    public SwiftResultSet createResultSet() {
        Callable<SwiftResultSet> task = new Callable<SwiftResultSet>() {
            @Override
            public SwiftResultSet call() throws Exception {
                SwiftResultSet iterator = null;
                String sql = null;
                com.fr.data.impl.Connection connection = connectionInfo.getFrConnection();
                Connection conn = null;
                Statement stmt = null;
                ResultSet rs = null;
                try {
                    long t = System.currentTimeMillis();
                    conn = connection.createConnection();
                    String originalCharSetName = connection.getOriginalCharsetName();
                    String newCharSetName = connection.getNewCharsetName();
                    boolean needCharSetConvert = StringUtils.isNotBlank(originalCharSetName)
                            && StringUtils.isNotBlank(newCharSetName);
                    Dialect dialect = DialectFactory.generateDialect(conn, connection.getDriver());
                    sql = getQuery(dialect);
                    LOGGER.info("runSQL " + sql);

                    if (StringUtils.isNotEmpty(sql)) {
                        sql = dealWithSqlCharSet(sql, connection);
                        stmt = createStatement(conn, dialect);
                        rs = stmt.executeQuery(sql);
                        LOGGER.info("sql: " + sql + " query cost:" + DateUtils.timeCostFrom(t));
                        iterator = createIterator(rs, dialect, sql, stmt, conn, needCharSetConvert, originalCharSetName, newCharSetName);
                        LOGGER.info("sql: " + sql + " execute cost:" + DateUtils.timeCostFrom(t));
                    } else {
                        iterator = EMPTY;
                    }
                } catch (Exception e) {
                    LOGGER.error("sql: " + sql + " execute failed!");
                    close(rs, stmt, conn);
                    throw e;
                }
                return iterator;
            }
        };

        RetryLoop retryLoop = new RetryLoop();
        retryLoop.initial(new RetryNTimes(PerformancePlugManager.getInstance().getRetryMaxTimes(), PerformancePlugManager.getInstance().getRetryMaxSleepTime()));
        try {
            return RetryLoop.retry(task, retryLoop);
        } catch (Exception e) {
            LOGGER.error(e);
            return EMPTY;
        }
    }

    protected void close(ResultSet rs, Statement stmt, Connection conn) {
        DBUtils.closeResultSet(rs);
        DBUtils.closeStatement(stmt);
        DBUtils.closeConnection(conn);
    }

    protected abstract String getQuery(Dialect dialect) throws SQLException;

    private String dealWithSqlCharSet(String sql, com.fr.data.impl.Connection database) {
        if (StringUtils.isNotBlank(database.getOriginalCharsetName()) && StringUtils.isNotBlank(database.getNewCharsetName())) {
            try {
                return new String(sql.getBytes(database.getNewCharsetName()), database.getOriginalCharsetName());
            } catch (UnsupportedEncodingException e) {
                FRContext.getLogger().error(e.getMessage(), e);
            }
        }
        return sql;
    }

    public Statement createStatement(Connection conn, Dialect dialect) throws SQLException {
        return dialect.execute(DialectKeyConstants.CREATE_LIMITED_FETCHED_STATEMENT_KEY, new DialectCreateLimitedFetchedStatementParameter(conn));
    }

    protected abstract SwiftResultSet createIterator(ResultSet rs,
                                                     Dialect dialect, String sql, Statement stmt, Connection conn, boolean needCharSetConvert,
                                                     String originalCharSetName,
                                                     String newCharSetName) throws SQLException;

    /**
     * @param needCharSetConvert
     * @param originalCharSetName
     * @param newCharSetName
     * @param metaData            ���ؽ����metadata
     * @param outerMeta           ʵ��ִ�е�sql��metadata��sql�����source��outermetadata����Ҫ�ر�ע�⣬���ݿ��ʹ�ò����ֶ�֮����Ҫ��ԭ����outermetadata����
     * @return
     * @throws SQLException
     */
    protected DBDealer[] createDBDealer(boolean needCharSetConvert, String originalCharSetName,
                                        String newCharSetName, SwiftMetaData metaData, SwiftMetaData outerMeta) throws SQLException {
        List<DBDealer> res = new ArrayList<DBDealer>();
        for (int i = 0; i < outerMeta.getColumnCount(); i++) {
            int rsColumn = i + 1;
            String name = outerMeta.getColumnName(rsColumn);
            SwiftMetaDataColumn column = null;
            try {
                column = metaData.getColumn(name);
            } catch (Exception e) {
                //do nothing
            }
            if (column != null) {
                DBDealer dealer = getDbDealer(needCharSetConvert, originalCharSetName, newCharSetName, metaData, outerMeta, rsColumn);
                res.add(dealer);
            }
        }
        return res.toArray(new DBDealer[res.size()]);
    }

    private DBDealer getDbDealer(boolean needCharSetConvert, String originalCharSetName, String newCharSetName, SwiftMetaData metaData, SwiftMetaData outerMeta, int rsColumn) throws SwiftMetaDataException {
        int outerSqlType = outerMeta.getColumnType(rsColumn);
        ColumnType columnType = ColumnTypeUtils.sqlTypeToColumnType(metaData.getColumnType(rsColumn), metaData.getPrecision(rsColumn), metaData.getScale(rsColumn));
        switch (outerSqlType) {
            case java.sql.Types.DECIMAL:
            case java.sql.Types.NUMERIC:
            case java.sql.Types.REAL:
            case java.sql.Types.DOUBLE:
            case java.sql.Types.FLOAT:
                return getDealerByColumn(outerMeta.getPrecision(rsColumn), outerMeta.getScale(rsColumn), rsColumn, columnType);
            case java.sql.Types.BIT:
            case java.sql.Types.TINYINT:
            case java.sql.Types.SMALLINT:
            case java.sql.Types.INTEGER:
            case java.sql.Types.BIGINT:
                return getNumberConvertDealer(new LongDealer(rsColumn), columnType);
            case java.sql.Types.DATE:
                return getDateConvertDealer(new DateDealer(rsColumn), columnType);
            case java.sql.Types.TIMESTAMP:
                return getDateConvertDealer(new TimestampDealer(rsColumn), columnType);
            case java.sql.Types.TIME:
                return getDateConvertDealer(new TimeDealer(rsColumn), columnType);
            default:
                if (needCharSetConvert) {
                    return getStringConvertDealer(new StringDealerWithCharSet(rsColumn, originalCharSetName, newCharSetName), columnType);
                } else {
                    return getStringConvertDealer(new StringDealer(rsColumn), columnType);
                }
        }
    }

    private DBDealer getDealerByColumn(int precision, int scale, int rsColumn, ColumnType columnType) {
        if (scale != 0) {
            //有小数点一定是double类型
            return getNumberConvertDealer(new DoubleDealer(rsColumn), columnType);
        } else if (ColumnTypeUtils.isLongType(precision) || columnType != ColumnType.STRING) {
            //没有小数点，并且判断为long类型的，只要不转文本，都是long类型
            return getNumberConvertDealer(new LongDealer(rsColumn), columnType);
        } else {
            return getStringConvertDealer(new StringDealer(rsColumn), columnType);
        }
    }


    private DBDealer getDateConvertDealer(DBDealer<Long> dateDealer, ColumnType columnType) {
        switch (columnType) {
            case STRING:
                return new Date2StringConvertDealer(dateDealer);
            default:
                return dateDealer;

        }
    }

    private DBDealer getStringConvertDealer(DBDealer<String> stringDealer, ColumnType columnType) {
        switch (columnType) {
            case NUMBER:
                return new String2NumberConvertDealer(stringDealer);
            case DATE:
                return new String2DateConvertDealer(stringDealer);
            default:
                return stringDealer;

        }
    }

    private DBDealer getNumberConvertDealer(DBDealer<? extends Number> numberDBDealer, ColumnType columnType) {
        switch (columnType) {
            case STRING:
                return new Number2StringConvertDealer(numberDBDealer);
            case DATE:
                return new Number2DateConvertDealer(numberDBDealer);
            default:
                return numberDBDealer;

        }
    }
}
