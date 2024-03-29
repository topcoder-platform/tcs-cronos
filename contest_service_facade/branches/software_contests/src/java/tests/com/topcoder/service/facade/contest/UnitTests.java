/*
 * Copyright (C) 2008 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.service.facade.contest;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * <p>This test case aggregates all Unit test cases.</p>
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
public class UnitTests {

    /**
     * <p>Aggregates the unit tests.</p>
     *
     * @return a <code>Test</code> providing the test suite aggregating all unit tests.
     */
    public static Test suite() {
        final TestSuite suite = new TestSuite();

        suite.addTest(new TestSuite(ContestServiceFacadeTest.class));

        return suite;
    }
}
