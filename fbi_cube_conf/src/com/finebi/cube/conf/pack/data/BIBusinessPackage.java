package com.finebi.cube.conf.pack.data;

import com.finebi.cube.conf.table.IBusinessTable;
import com.fr.bi.base.BIUser;
import com.fr.bi.common.container.BISetContainer;
import com.fr.bi.stable.data.BITableID;
import com.fr.bi.stable.exception.BITableAbsentException;
import com.fr.general.ComparatorUtils;
import com.fr.json.JSONArray;
import com.fr.json.JSONObject;
import com.fr.json.JSONTransform;
import com.fr.stable.FCloneable;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Created by Connery on 2016/1/20.
 */
public abstract class BIBusinessPackage<T extends IBusinessTable> extends BISetContainer<T> implements JSONTransform, FCloneable, IBusinessPackageGetterService<T> {

    protected BIUser owner;
    protected BIPackageName name;
    protected long position;

    /**
     * 唯一标准，包括equals和hashcode
     */
    protected BIPackageID ID;


    public BIBusinessPackage(BIPackageID ID, BIPackageName name, BIUser owner,long position) {
        this.ID = ID;
        this.name = name;
        this.owner = owner;
        this.position = position;
    }

    public long getPosition() {
        return position;
    }

    @Override
    public BIUser getOwner() {
        return owner;
    }

    @Override
    public BIPackageName getName() {
        return name;
    }

    public void setName(BIPackageName name) {
        this.name = name;
    }

    @Override
    public BIPackageID getID() {
        return ID;
    }

    public BIBusinessPackage(BIPackageID id) {
        this(id, BIPackageName.DEFAULT, BIUser.DEFALUT,System.currentTimeMillis());
    }

    protected abstract T createTable();

    protected Collection initCollection() {
        return new LinkedHashSet<T>();
    }
    @Override
    public boolean isNeed2BuildCube(BIBusinessPackage targetPackage) {
        if (size() == targetPackage.size()) {
            Iterator<T> currentIt = container.iterator();
            while (currentIt.hasNext()) {
                T currentTable = currentIt.next();
                Iterator<T> targetIt = targetPackage.container.iterator();
                while (targetIt.hasNext()) {
                    T targetTable = targetIt.next();
                    if (ComparatorUtils.equals(targetTable, currentTable)) {
                        continue;
                    } else {
                        return false;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public Set<T> getBusinessTables() {
        return getContainer();
    }

    public void addBusinessTable(T biBusinessTable) {
        add(biBusinessTable);
    }

    public void removeBusinessTable(T biBusinessTable) {
        remove(biBusinessTable);
    }

    public void removeBusinessTableByID(BITableID tableID) {
        try {
            T table = getSpecificTable(tableID);
            removeBusinessTable(table);
        } catch (BITableAbsentException ignore) {

        }
    }

    @Override
    public T getSpecificTable(BITableID tableID) throws BITableAbsentException {
        Iterator<T> it = getContainer().iterator();
        while (it.hasNext()) {
            T result = it.next();
            if (ComparatorUtils.equals(result.getID(), tableID)) {
                return result;
            }
        }
        throw new BITableAbsentException();
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("BIBusinessPackage{");
        sb.append("owner=").append(owner);
        sb.append(", name=").append(name);
        sb.append(", ID=").append(ID);
        sb.append('}');
        return sb.toString();
    }

    /**
     * 输出表名json
     *
     * @return json对象
     * @throws
     */

    @Override
    public JSONObject createJSON() throws Exception {
        JSONObject jo = new JSONObject();
        jo.put("name", name.getValue());
        jo.put("id", ID.getIdentityValue());

        JSONArray result = new JSONArray();
        Set<T> tables = getBusinessTables();
        Iterator<T> it = tables.iterator();
        while (it.hasNext()) {
            T table = it.next();
            result.put(table.createJSON());
        }
        jo.put("tables", result);
        jo.put("position", this.position);
        return jo;
    }

    @Override
    public void parseJSON(JSONObject jo) throws Exception {
        //this.setName(jo.optString("package_name"));
        JSONArray ja = jo.optJSONArray("data");
        clear();
        for (int i = 0; i < ja.length(); i++) {
            T table = createTable();
            table.parseJSON(ja.optJSONObject(i));
            add(table);
        }
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        BIBusinessPackage cloned = (BIBusinessPackage) super.clone();

        Iterator<T> it = getContainer().iterator();
        while (it.hasNext()) {
            cloned.addBusinessTable((T) it.next().clone());
        }

        return cloned;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BIBusinessPackage)) {
            return false;
        }

        BIBusinessPackage<?> that = (BIBusinessPackage<?>) o;

        return !(ID != null ? !ComparatorUtils.equals(ID, that.ID) : that.ID != null);


    }

    @Override
    public int hashCode() {
        return ID != null ? ID.hashCode() : 0;
    }
}