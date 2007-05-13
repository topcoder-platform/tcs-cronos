/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.resource.persistence.stresstests;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;


/**
 * <p>
 * This test case aggregates all Stress test cases.
 * </p>
 *
 * @author kinfkong
 * @version 1.0
 */
public class StressTests extends TestCase {

    /**
     * Add the tests.
     *
     * @return the test suites.
     */
    public static Test suite() {
        final TestSuite suite = new TestSuite();

        suite.addTestSuite(SqlResourcePersistenceTest.class);

        return suite;
    }
}
