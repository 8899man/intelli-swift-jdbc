package com.fr.fs.mapeditor;

import com.fr.data.dao.FieldColumnMapper;
import com.fr.data.dao.ObjectTableMapper;
import com.fr.data.dao.RelationFCMapper;
import com.fr.fs.AbstractFSPlate;
import com.fr.fs.base.entity.PlatformManageModule;
import com.fr.fs.control.dao.tabledata.TableDataDAOControl;
import com.fr.fs.dao.EntryDAO;
import com.fr.plugin.bi.chart.map.server.service.MapEditorEntryService;
import com.fr.plugin.bi.chart.map.server.service.MapEditorOpenEntryService;
import com.fr.plugin.bi.chart.map.server.service.MapGetJsonService;
import com.fr.stable.fun.Service;

import java.util.List;

public class FontEndMapEditorPlate extends AbstractFSPlate  {
    public FontEndMapEditorPlate() {
    }

    public Service[] service4Register() {
        return new Service[]{
                // 这里直接复用plugin-vanchart的MapEditorService
                new MapEditorOpenEntryService(),
                new MapEditorEntryService(),
                new MapGetJsonService()
        };
    }

    public ObjectTableMapper[] mappers4Register() {
        return new ObjectTableMapper[0];
    }

    public Class getRelationClass() {
        return null;
    }

    public TableDataDAOControl.ColumnColumn[] getTableDataColumns() {
        return new TableDataDAOControl.ColumnColumn[0];
    }

    public FieldColumnMapper[] columnMappers4Company() {
        return new FieldColumnMapper[0];
    }

    public FieldColumnMapper[] columnMappers4Custom() {
        return new FieldColumnMapper[0];
    }

    public RelationFCMapper getRelationFCMapper4Custom() {
        return null;
    }

    public RelationFCMapper getRelationFCMapper4Company() {
        return null;
    }

    public Object createPrivilegeObject(long id) {
        return null;
    }

    public List<String> getAllPrivilegesID() {
        return null;
    }

    public List<EntryDAO> getEntryDaoAccess() {
        return null;
    }

    public void refreshManager() {

    }

    public void release() {

    }

    public boolean isSupport() {
        return true;
    }

    public boolean needPrivilege() {
        return false;
    }

    @Override
    public String[] getLocaleFile() {
        return new String[]{
                "com/fr/plugin/chart/locale/vancharts"
        };
    }

    public String[] getPlateJavaScriptFiles4WebClient() {
        return new String[]{"/com/fr/fs/mapeditor/entry.js"};
    }

    @Override
    public PlatformManageModule[] supportPlatformManageModules() {
        return new PlatformManageModule[]{
                // todo 这个platformManageModuleId不知道写多少，先写一个20用着
                new PlatformManageModule("Plugin-Chart_Map_Editor", "", 20, 1, true)
        };
    }
}
