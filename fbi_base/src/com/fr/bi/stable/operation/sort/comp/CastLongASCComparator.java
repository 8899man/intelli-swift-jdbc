package com.fr.bi.stable.operation.sort.comp;

/**
 * Created by roy on 16/11/2.
 */
public class CastLongASCComparator extends ASCComparator {
    @Override
    public int compare(Object o1, Object o2) {
        if (o1 == null || o2 == null) {
            return super.compare(o1, o2);
        }
        Long o1Double = ((Number) o1).longValue();
        Long o2Double = ((Number) o2).longValue();
        return super.compare(o1Double, o2Double);
    }
}

