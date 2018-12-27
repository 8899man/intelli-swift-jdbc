package com.fr.swift.config.service.impl;

import com.fr.swift.SwiftContext;
import com.fr.swift.beans.annotation.SwiftBean;
import com.fr.swift.config.SwiftConfigConstants;
import com.fr.swift.config.bean.SwiftTablePathBean;
import com.fr.swift.config.dao.SwiftTablePathDao;
import com.fr.swift.config.oper.BaseTransactionWorker;
import com.fr.swift.config.oper.ConfigSession;
import com.fr.swift.config.oper.RestrictionFactory;
import com.fr.swift.config.oper.TransactionManager;
import com.fr.swift.config.oper.impl.RestrictionFactoryImpl;
import com.fr.swift.config.service.SwiftTablePathService;
import com.fr.swift.converter.FindList;
import com.fr.swift.event.ClusterEvent;
import com.fr.swift.event.ClusterEventListener;
import com.fr.swift.event.ClusterEventType;
import com.fr.swift.event.ClusterListenerHandler;
import com.fr.swift.log.SwiftLoggers;
import com.fr.swift.selector.ClusterSelector;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author yee
 * @date 2018/7/18
 */
@SwiftBean
public class SwiftTablePathServiceImpl implements SwiftTablePathService {
    private SwiftTablePathDao swiftTablePathDao = SwiftContext.get().getBean(SwiftTablePathDao.class);
    private TransactionManager tx = SwiftContext.get().getBean(TransactionManager.class);
    private RestrictionFactory factory = RestrictionFactoryImpl.INSTANCE;
    private String clusterId = SwiftConfigConstants.LOCALHOST;
    private ConcurrentHashMap<String, Integer> tablePath = new ConcurrentHashMap<String, Integer>();

    public SwiftTablePathServiceImpl() {
        ClusterListenerHandler.addInitialListener(new ClusterEventListener() {
            @Override
            public void handleEvent(ClusterEvent clusterEvent) {
                tablePath.clear();
                if (clusterEvent.getEventType() == ClusterEventType.JOIN_CLUSTER) {
                    clusterId = ClusterSelector.getInstance().getFactory().getCurrentId();
                } else if (clusterEvent.getEventType() == ClusterEventType.LEFT_CLUSTER) {
                    clusterId = SwiftConfigConstants.LOCALHOST;
                }
            }
        });
    }

    @Override
    public List<SwiftTablePathBean> find(final Object... criterion) {
        try {
            return tx.doTransactionIfNeed(new BaseTransactionWorker<List<SwiftTablePathBean>>(false) {
                @Override
                public List<SwiftTablePathBean> work(ConfigSession session) {
                    return swiftTablePathDao.find(session, criterion).list();
                }
            });
        } catch (SQLException e) {
            return Collections.emptyList();
        }
    }

    @Override
    public boolean saveOrUpdate(final SwiftTablePathBean entity) {
        try {
            return tx.doTransactionIfNeed(new BaseTransactionWorker<Boolean>() {
                @Override
                public Boolean work(ConfigSession session) throws SQLException {
                    entity.setClusterId(clusterId);
                    boolean success = swiftTablePathDao.saveOrUpdate(session, entity);
                    if (success) {
                        if (null != entity.getTablePath()) {
                            tablePath.put(entity.getTableKey(), entity.getTablePath());
                        }
                        return true;
                    }
                    return false;
                }
            });
        } catch (SQLException e) {
            SwiftLoggers.getLogger().warn("save or update table path error", e);
            return false;
        }
    }

    @Override
    public boolean removePath(final String table) {
        try {
            return tx.doTransactionIfNeed(new BaseTransactionWorker<Boolean>() {
                @Override
                public Boolean work(final ConfigSession session) throws SQLException {
                    try {
                        swiftTablePathDao.find(session, factory.eq("id.tableKey", table), factory.eq("id.clusterId", clusterId)).justForEach(new FindList.ConvertEach() {
                            @Override
                            public Object forEach(int idx, Object item) throws Exception {
                                try {
                                    session.delete(item);
                                    tablePath.remove(table);
                                } catch (Exception e) {
                                    SwiftLoggers.getLogger().warn(e);
                                }
                                return null;
                            }
                        });
                        return true;
                    } catch (Throwable e) {
                        SwiftLoggers.getLogger().warn(e);
                    }
                    return false;
                }
            });
        } catch (SQLException e) {
            SwiftLoggers.getLogger().warn("remove table path error", e);
            return false;
        }
    }

    @Override
    public Integer getTablePath(final String table) {
        try {
            Integer path = tablePath.get(table);
            if (null == path) {
                path = tx.doTransactionIfNeed(new BaseTransactionWorker<Integer>() {
                    @Override
                    public Integer work(ConfigSession session) throws SQLException {
                        try {
                            SwiftTablePathBean entity = swiftTablePathDao.find(session, factory.eq("id.tableKey", table), factory.eq("id.clusterId", clusterId)).get(0);
                            if (null != entity) {
                                Integer path = entity.getTablePath();
                                if (null != path && path.intValue() > -1) {
                                    tablePath.put(table, path);
                                    return path;
                                }
                                return 0;
                            }
                            return 0;
                        } catch (Exception e) {
                            SwiftLoggers.getLogger().warn(e);
                            return 0;
                        }
                    }
                });
            }
            return path;
        } catch (SQLException e) {
            return 0;
        }
    }

    @Override
    public Integer getLastPath(final String table) {
        try {
            return tx.doTransactionIfNeed(new BaseTransactionWorker<Integer>() {
                @Override
                public Integer work(ConfigSession session) throws SQLException {
                    try {
                        SwiftTablePathBean entity = swiftTablePathDao.find(session, factory.eq("id.tableKey", table), factory.eq("id.clusterId", clusterId)).get(0);
                        if (null != entity) {
                            return entity.getLastPath();
                        }
                        return 0;
                    } catch (Exception e) {
                        SwiftLoggers.getLogger().warn(e);
                        return 0;
                    }
                }
            });
        } catch (SQLException e) {
            return 0;
        }
    }

    @Override
    public SwiftTablePathBean get(final String table) {
        try {
            return tx.doTransactionIfNeed(new BaseTransactionWorker<SwiftTablePathBean>() {
                @Override
                public SwiftTablePathBean work(ConfigSession session) throws SQLException {
                    try {
                        return swiftTablePathDao.find(session, factory.eq("id.tableKey", table), factory.eq("id.clusterId", clusterId)).get(0);
                    } catch (Exception e) {
                        if (e instanceof SQLException) {
                            throw (SQLException) e;
                        }
                        throw new SQLException(e);
                    }
                }
            });
        } catch (SQLException e) {
            return null;
        }
    }
}
