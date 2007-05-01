/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.im.logic.stresstests;

import junit.framework.TestCase;

/**
 * Base class for all stress test classes.
 *
 * @author kaqi072821
 * @version 1.0
 */
class BaseStressTest extends TestCase {
    /**
     * The times that a method will be called.
     */
    protected static final int RUN_TIMES = 100;

    /**
     * The start time of the stress test calling this helper.
     */
    private long start;

    /**
     * The end time of the stress test calling this helper.
     */
    private long end;

    /**
     * Protected constructor to prevent this class being instantiated externally.
     */
    protected BaseStressTest() {
        // do nothing
    }

    /**
     * Begins a stress test.
     */
    public void beginTest() {
        start = System.currentTimeMillis();
    }

    /**
     * Ends a stress test.
     *
     * @param exeMessage the execution message.
     * @param maxTime the maximum time allowed.
     */
    public void endTest(String exeMessage, long maxTime) {
        end = System.currentTimeMillis();
        long took = end - start;
        System.out.println("Executing " + exeMessage + " " + RUN_TIMES + " times takes " + (end - start)
            + "ms");
        assertTrue(took <= maxTime);
    }
}