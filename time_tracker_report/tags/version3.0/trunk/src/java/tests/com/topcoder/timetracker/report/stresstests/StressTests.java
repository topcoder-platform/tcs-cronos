/**
 *
 * Copyright (c) 2005, TopCoder, Inc. All rights reserved
 */
package com.topcoder.timetracker.report.stresstests;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * <p>This test case aggregates all Stress test cases.</p>
 *
 * @author TopCoder
 * @version 1.0
 */
public class StressTests extends TestCase {

    /**
     * Aggregate all stress tests cases.
     *
     * @return all stress tests cases
     */
    public static Test suite() {
        final TestSuite suite = new TestSuite();
        suite.addTestSuite(TimeTrackerReportStressTests.class);
        return suite;
    }
}
