package com.finebi.cube.relation;

import com.fr.bi.stable.data.BIField;
import com.fr.bi.stable.data.source.ICubeTableSource;
import com.fr.json.JSONCreator;
import com.fr.json.JSONObject;

/**
 * This class created on 2016/3/9.
 *
 * @author Connery
 * @since 4.0
 */
public class BITableSourceRelation extends BIBasicRelation<ICubeTableSource, BIField> implements JSONCreator {
    public BITableSourceRelation(BIField primaryField, BIField foreignField, ICubeTableSource primaryTable, ICubeTableSource foreignTable) {
        super(primaryField, foreignField, primaryTable, foreignTable);
    }


    @Override
    public JSONObject createJSON() throws Exception {
        JSONObject jo = new JSONObject();
        jo.put("primaryKey", this.primaryField.createJSON());
        jo.put("foreignKey", this.foreignField.createJSON());
        return jo;
    }
}
