/**
 * 
 */
package com.fr.bi.conf.data.source.operator.add.rowcal.correspondperiod;

import com.finebi.cube.api.ICubeColumnDetailGetter;
import com.fr.bi.base.key.BIKey;
import com.fr.bi.common.inter.Traversal;
import com.fr.bi.conf.data.source.operator.add.rowcal.correspondperiodpercentage.CorrespondPeriodPercentResultDealer;
import com.fr.bi.stable.data.db.BIDataValue;
import com.fr.bi.stable.engine.cal.ResultDealer;
import com.finebi.cube.api.ICubeTableService;
import com.fr.bi.stable.gvi.GroupValueIndex;
import com.fr.bi.stable.gvi.traversal.SingleRowTraversalAction;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Daniel
 *
 */
public class CorrespondPeriodResultDealer implements ResultDealer {
	
	private BIKey key;
	private BIKey periodKey;
	private Traversal<BIDataValue> travel;
	
	CorrespondPeriodResultDealer(BIKey key, Traversal<BIDataValue> travel,  BIKey periodKey){
		this.key = key;
		this.travel = travel;
		this.periodKey = periodKey;
	}

	@Override
	public void dealWith(final ICubeTableService ti, GroupValueIndex gvi, final int startCol) {
		final Map<Double, Object> map = new HashMap<Double, Object>();
        final ICubeColumnDetailGetter getter = ti.getColumnDetailReader(periodKey);
        final ICubeColumnDetailGetter keyGetter = ti.getColumnDetailReader(CorrespondPeriodResultDealer.this.key);
		gvi.Traversal(new SingleRowTraversalAction() {
			@Override
			public void actionPerformed(int row) {
				Number v = (Number) getter.getValue(row);
				if(v == null){
					return;
				}
				double key = v.doubleValue();
				if(!map.containsKey(key)){
					Object value = keyGetter.getValue(row);
                    value = value == null ? value : ((Number)value).doubleValue();
					map.put(key, value);
				}
			}
		});
		gvi.Traversal(new SingleRowTraversalAction() {
			@Override
			public void actionPerformed(int row) {
				Number v = (Number) getter.getValue(row);
				Object value = null;
				if(v != null){
					double key = v.doubleValue() - 1;
					value = map.get(key);
				}
				travel.actionPerformed(new BIDataValue(row, startCol, value));
			}
		});
	}

}