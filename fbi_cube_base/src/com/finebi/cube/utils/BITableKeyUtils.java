package com.finebi.cube.utils;

import com.finebi.cube.structure.BITableKey;
import com.finebi.cube.structure.ITableKey;
import com.fr.bi.stable.data.source.ICubeTableSource;

/**
 * This class created on 2016/4/6.
 *
 * @author Connery
 * @since 4.0
 */
public class BITableKeyUtils {
    public static ITableKey convert(ICubeTableSource tableSource) {
        return new BITableKey(tableSource);
    }

}
