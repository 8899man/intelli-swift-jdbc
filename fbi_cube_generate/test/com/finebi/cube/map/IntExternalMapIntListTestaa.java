package com.finebi.cube.map;

import com.fr.bi.stable.operation.sort.comp.ComparatorFacotry;
import com.fr.bi.stable.structure.collection.list.IntList;
import junit.framework.TestCase;

import java.util.Iterator;
import java.util.Map;

/**
 * Created by wang on 2016/8/30.
 */
public class IntExternalMapIntListTestaa extends TestCase {
    public void testIntExternalMapIntList() {
        IntegerIntListExternalMap map = new IntegerIntListExternalMap(ComparatorFacotry.INTEGER_ASC, "test");
        for (int c = 1; c < 4; c++) {
            IntList list = new IntList();
            for (int i = 10000; i > 0; i--) {
                list.add(i * c);
                list.add(i * c + 4);
            }
            map.put(c, list);
        }

        Iterator<ExternalMap.Entry<Integer, IntList>> it = map.getIterator();
        while (it.hasNext()) {
            Map.Entry<Integer, IntList> entry = it.next();
            System.out.println(entry.getKey());
        }
        System.err.println(map.size());
    }
}
