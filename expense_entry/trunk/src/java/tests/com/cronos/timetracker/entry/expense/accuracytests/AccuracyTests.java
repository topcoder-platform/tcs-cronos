/*
 * Copyright (c) 2006, TopCoder, Inc. All rights reserved
 */
package com.cronos.timetracker.entry.expense.accuracytests;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;


/**
 * <p>
 * This test case aggregates all Accuracy test cases.
 * </p>
 *
 * @author -oo-
 * @version 1.1
 */
public class AccuracyTests extends TestCase {
    /**
     * Accuracy test cases suite.
     *
     * @return the suite
     */
    public static Test suite() {
        final TestSuite suite = new TestSuite();

        suite.addTestSuite(SearchTests.class);
        suite.addTestSuite(ExpenseEntryTest.class);
        suite.addTestSuite(ExpenseEntryDbPersistenceTest.class);
        suite.addTestSuite(ExpenseEntryManagerTest.class);

        return suite;
    }
}
