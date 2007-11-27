/**
 * Copyright (c) 2006, TopCoder, Inc. All rights reserved
 */
package com.topcoder.timetracker.user;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * <p>
 * This test case aggregates all test cases.
 * </p>
 *
 * @author TopCoder
 * @version 3.2
 */
public class AllTests extends TestCase {

    public static Test suite() {
        final TestSuite suite = new TestSuite();

        // stress tests
        // suite.addTest(StressTests.suite());

        // unit tests
        suite.addTest(UnitTests.suite());

        // accuracy tests
        // suite.addTest(AccuracyTests.suite());

        // failure tests
        // suite.addTest(FailureTests.suite());

        return suite;
    }

}
