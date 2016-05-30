/**
 *
 */
package com.fr.bi.web.conf.services.cubetask;

import com.finebi.cube.conf.table.BIBusinessTable;
import com.finebi.cube.conf.table.BusinessTable;
import com.fr.bi.web.conf.AbstractBIConfigureAction;
import com.fr.fs.web.service.ServiceUtils;
import com.fr.json.JSONObject;
import com.fr.web.utils.WebUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class BIGetSingleTableUpdateStatueAction extends
        AbstractBIConfigureAction {

    /* (non-Javadoc)
     * @see com.fr.web.core.AcceptCMD#getCMD()
     */
    @Override
    public String getCMD() {
        return "get_single_table_update_statue_action";
    }

    /* (non-Javadoc)
     * @see com.fr.bi.web.services.conf.AbstractBIConfigureAction#actionCMDPrivilegePassed(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected void actionCMDPrivilegePassed(HttpServletRequest req,
                                            HttpServletResponse res) throws Exception {
        String tableString = WebUtils.getHTTPRequestParameter(req, "table");
        long userId = ServiceUtils.getCurrentUserID(req);
        if (tableString != null) {
            BusinessTable table = new BIBusinessTable("", "");
            table.parseJSON(new JSONObject(tableString));

//            boolean hasTask = BICubeConfigureCenter.getCubeManager().hasTask(new SingleTableTask(table, userId), userId);
//            WebUtils.printAsJSON(res, new JSONObject().put("hasTask", hasTask));
        }
        WebUtils.printAsString(res, "table is not defined");
    }

}
