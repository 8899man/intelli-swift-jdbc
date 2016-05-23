package com.finebi.cube.conf.relation.path;

/**
 * Created by Connery on 2016/1/14.
 */
public class BISeniorTablesManager extends BIIndirectlyRelativeTablesManager {
    @Override
    protected BIDirectlyRelativeTablesManager generateDirectlyRelativeManager() {
        return new BIFatherTablesManager(this);
    }

    @Override
    protected BIIndirectlyRelativeTableContainer generateIndirectlyRelativeContainer() {
        return new BISeniorTableContainer();
    }
}