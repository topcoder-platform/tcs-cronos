/*
 * Copyright (C) 2010 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.reliability.accuracytests;

import junit.framework.TestCase;

import com.topcoder.reliability.ReliabilityCalculatorConfigurationException;
import com.topcoder.util.errorhandling.BaseRuntimeException;
import com.topcoder.util.errorhandling.ExceptionData;


/**
 * Accuracy test for <code>ReliabilityCalculatorConfigurationException</code>.
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
public class ReliabilityCalculatorConfigurationExceptionAccuracy
    extends TestCase {
    /**
     * The message for testing.
     */
    private static final String MESSAGE = "error message";

    /**
     * The inner cause exception for testing.
     */
    private static final Throwable CAUSE = new Exception("exception");

    /**
     * The inner cause exception for testing.
     */
    private static final ExceptionData DATA = new ExceptionData();

    /**
     * Accuracy test for <code>ReliabilityCalculatorConfigurationException(String)</code>.
     */
    public void testReliabilityCalculatorConfigurationException_Str() {
        ReliabilityCalculatorConfigurationException ex = new ReliabilityCalculatorConfigurationException(MESSAGE);
        assertNotNull("instance should be created.", ex);
        assertEquals("The message should match.", MESSAGE, ex.getMessage());
        assertTrue("The instance should extends correct exception.",
            ex instanceof BaseRuntimeException);
    }

    /**
     * Accuracy test for <code>ReliabilityCalculatorConfigurationException(String, Throwable)</code>.
     */
    public void testReliabilityCalculatorConfigurationException_Str_Throwable() {
        ReliabilityCalculatorConfigurationException ex = new ReliabilityCalculatorConfigurationException(MESSAGE,
                CAUSE);
        assertNotNull("instance should be created.", ex);
        assertEquals("The message should match.", MESSAGE, ex.getMessage());
        assertEquals("The cause should match.", CAUSE, ex.getCause());
        assertTrue("The instance should extends correct exception.",
            ex instanceof BaseRuntimeException);
    }

    /**
     * Accuracy test for <code>ReliabilityCalculatorConfigurationException(String, ExceptionData)</code>.
     */
    public void testReliabilityCalculatorConfigurationException_Str_Exp() {
        ReliabilityCalculatorConfigurationException ex = new ReliabilityCalculatorConfigurationException(MESSAGE,
                DATA);
        assertNotNull("instance should be created.", ex);
        assertEquals("The message should match.", MESSAGE, ex.getMessage());
        assertTrue("The instance should extends correct exception.",
            ex instanceof BaseRuntimeException);
    }

    /**
     * Accuracy test for <code>ReliabilityCalculatorConfigurationException(String, Throwable, ExceptionData)</code>.
     */
    public void testReliabilityCalculatorConfigurationException_Str_Throwable_Exp() {
        ReliabilityCalculatorConfigurationException ex = new ReliabilityCalculatorConfigurationException(MESSAGE,
                CAUSE, DATA);
        assertNotNull("instance should be created.", ex);
        assertEquals("The message should match.", MESSAGE, ex.getMessage());
        assertEquals("The cause should match.", CAUSE, ex.getCause());
        assertTrue("The instance should extends correct exception.",
            ex instanceof BaseRuntimeException);
    }
}
