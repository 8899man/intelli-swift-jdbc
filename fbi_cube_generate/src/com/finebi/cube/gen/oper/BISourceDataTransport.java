package com.finebi.cube.gen.oper;

import com.finebi.cube.adapter.BIUserCubeManager;
import com.finebi.cube.exception.BICubeColumnAbsentException;
import com.finebi.cube.impl.pubsub.BIProcessor;
import com.finebi.cube.message.IMessage;
import com.finebi.cube.structure.BITableKey;
import com.finebi.cube.structure.ICube;
import com.finebi.cube.structure.ICubeTableEntityService;
import com.finebi.cube.structure.ITableKey;
import com.finebi.cube.utils.BITableKeyUtils;
import com.fr.bi.common.inter.Traversal;
import com.fr.bi.stable.data.db.BIDataValue;
import com.fr.bi.stable.data.db.BICubeFieldSource;
import com.fr.bi.stable.data.source.ICubeTableSource;
import com.fr.fs.control.UserControl;
import com.fr.general.ComparatorUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * This class created on 2016/4/5.
 *
 * @author Connery
 * @since 4.0
 */
public class BISourceDataTransport extends BIProcessor {
    protected ICubeTableSource tableSource;
    protected Set<ICubeTableSource> allSources;
    protected ICubeTableEntityService tableEntityService;
    protected ICube cube;
    protected List<ITableKey> parents = new ArrayList<ITableKey>();

    public BISourceDataTransport(ICube cube, ICubeTableSource tableSource, Set<ICubeTableSource> allSources, Set<ICubeTableSource> parentTableSource) {
        this.tableSource = tableSource;
        this.allSources = allSources;
        this.cube = cube;
        tableEntityService = cube.getCubeTableWriter(BITableKeyUtils.convert(tableSource));
        initialParents(parentTableSource);
    }

    private void initialParents(Set<ICubeTableSource> parentTableSource) {
        if (parentTableSource != null) {
            for (ICubeTableSource tableSource : parentTableSource) {
                parents.add(new BITableKey(tableSource));
            }
        }
    }

    @Override
    public Object mainTask(IMessage lastReceiveMessage) {
        recordTableInfo();
        long count = transport();

        if (count >= 0) {
            tableEntityService.recordRowCount(count);
        }
        return null;
    }

    @Override
    public void release() {
        tableEntityService.clear();
    }

    private void recordTableInfo() {
        if (tableSource.getSourceID().equals("2d023b15")) {
            System.out.println("fine");
        }
        BICubeFieldSource[] columns = getFieldsArray();
        List<BICubeFieldSource> columnList = new ArrayList<BICubeFieldSource>();
        for (BICubeFieldSource col : columns) {
            columnList.add(convert(col));
        }
        tableEntityService.recordTableStructure(columnList);
        if (!tableSource.isIndependent()) {
            tableEntityService.recordParentsTable(parents);
            tableEntityService.recordFieldNamesFromParent(getParentFieldNames());
        }
    }

    private Set<String> getParentFieldNames() {
        Set<BICubeFieldSource> parentFields = tableSource.getParentFields(allSources);
        Set<BICubeFieldSource> facetFields = tableSource.getFacetFields(allSources);
        Set<BICubeFieldSource> selfFields = tableSource.getSelfFields(allSources);
        Set<String> fieldNames = new HashSet<String>();
        for (BICubeFieldSource field : parentFields) {
            if (!containSameName(selfFields, field.getFieldName()) && containSameName(facetFields, field.getFieldName())) {
                fieldNames.add(field.getFieldName());
            }
        }
        return fieldNames;
    }

    private boolean containSameName(Set<BICubeFieldSource> set, String fieldName) {
        for (BICubeFieldSource field : set) {
            if (ComparatorUtils.equals(field.getFieldName(), fieldName)) {
                return true;
            }
        }
        return false;
    }

    private long transport() {
        List<BICubeFieldSource> fieldList = tableEntityService.getFieldInfo();
        BICubeFieldSource[] BICubeFieldSources = new BICubeFieldSource[fieldList.size()];
        for (int i = 0; i < fieldList.size(); i++) {
            BICubeFieldSources[i] = fieldList.get(i);
        }
        return this.tableSource.read(new Traversal<BIDataValue>() {
            @Override
            public void actionPerformed(BIDataValue v) {
                try {
                    tableEntityService.addDataValue(v);
                } catch (BICubeColumnAbsentException e) {
                    e.printStackTrace();
                }
            }
        }, BICubeFieldSources, new BIUserCubeManager(UserControl.getInstance().getSuperManagerID(), cube));
    }

    private BICubeFieldSource convert(BICubeFieldSource column) {
        return new BICubeFieldSource(tableSource.getSourceID(), column.getFieldName(), column.getClassType(), column.getFieldSize());
    }

    private BICubeFieldSource[] getFieldsArray() {
        return tableSource.getFieldsArray(allSources);
    }

}
