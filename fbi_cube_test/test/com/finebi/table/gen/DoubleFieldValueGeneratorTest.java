package com.finebi.table.gen;
/**
 * This class created on 2017/5/17.
 *
 * @author Connery
 * @since Advanced FineBI Analysis 1.0
 */

import com.finebi.cube.common.log.BILogger;
import com.finebi.cube.common.log.BILoggerFactory;
import com.finebi.tool.BITestConstants;
import junit.framework.TestCase;

import java.util.HashSet;
import java.util.Set;

public class DoubleFieldValueGeneratorTest extends TestCase {
    private final static BILogger LOGGER = BILoggerFactory.getLogger(DoubleFieldValueGeneratorTest.class);

    /**
     * Detail:
     * Author:Connery
     * Date:2017/5/17
     */
    public void testStringRead() {
        try {
            Set<Double> valueSet = new HashSet<Double>();
            int group = BITestConstants.HUNDRED;
            int row = BITestConstants.THOUSAND;
            DoubleFieldValueGenerator generator = new DoubleFieldValueGenerator(group, row);
            for (int i = 0; i < row; i++) {
                valueSet.add(generator.getValue());
            }
            assertTrue(valueSet.size() > group - BITestConstants.TEN);

        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            fail();
        }
    }
}
