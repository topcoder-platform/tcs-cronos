/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.timetracker.invoice.accuracytests;

import com.topcoder.timetracker.invoice.InvoiceDataAccessException;

import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.framework.Test;

/**
 * <p>
 * Accuracy test cases for InvoiceDataAccessException.
 * </p>
 *
 * <p>
 * This class is pretty simple. The test cases simply verifies the exception can be instantiated with the error message
 * and cause properly propagated, and that it comes with correct inheritance.
 * </p>
 *
 * @author victorsam
 * @version 1.0
 */
public class InvoiceDataAccessExceptionAccuracyTests extends TestCase {
    /**
     * <p>
     * The error message used for testing.
     * </p>
     */
    private static final String ERROR_MESSAGE = "test error message";

    /**
     * <p>
     * An exception instance used to create the InvoiceDataAccessException.
     * </p>
     */
    private static final Exception CAUSE_EXCEPTION = new NullPointerException();

    /**
     * <p>
     * Returns the test suite of this class.
     * </p>
     *
     * @return the test suite of this class.
     */
    public static Test suite() {
        return new TestSuite(InvoiceDataAccessExceptionAccuracyTests.class);
    }

    /**
     * <p>
     * Creation test.
     * </p>
     *
     * <p>
     * Verifies the error message is properly propagated.
     * </p>
     */
    public void testInvoiceDataAccessException1() {
        InvoiceDataAccessException exception = new InvoiceDataAccessException(ERROR_MESSAGE);
        assertNotNull("Unable to instantiate InvoiceDataAccessException.", exception);
        assertEquals("Error message is not properly propagated to super class.", ERROR_MESSAGE, exception.getMessage());
    }

    /**
     * <p>
     * Creation test.
     * </p>
     *
     * <p>
     * Verifies the error message and the cause are properly propagated.
     * </p>
     */
    public void testInvoiceDataAccessException2() {
        InvoiceDataAccessException exception = new InvoiceDataAccessException(ERROR_MESSAGE, CAUSE_EXCEPTION);

        assertNotNull("Unable to instantiate InvoiceDataAccessException.", exception);
        assertTrue("The error message should match", exception.getMessage().indexOf(ERROR_MESSAGE) >= 0);
        assertEquals("Cause is not properly propagated to super class.", CAUSE_EXCEPTION, exception.getCause());
    }

    /**
     * <p>
     * Inheritance test.
     * </p>
     *
     * <p>
     * Verifies InvoiceDataAccessException subclasses Exception.
     * </p>
     */
    public void testInvoiceDataAccessExceptionInheritance1() {
        InvoiceDataAccessException exception = new InvoiceDataAccessException(ERROR_MESSAGE);
        assertTrue("InvoiceDataAccessException does not subclass Exception.", exception instanceof Exception);
    }

    /**
     * <p>
     * Inheritance test.
     * </p>
     *
     * <p>
     * Verifies InvoiceDataAccessException subclasses Exception.
     * </p>
     */
    public void testInvoiceDataAccessExceptionInheritance2() {
        InvoiceDataAccessException exception = new InvoiceDataAccessException(ERROR_MESSAGE, CAUSE_EXCEPTION);
        assertTrue("InvoiceDataAccessException does not subclass Exception.", exception instanceof Exception);
    }
}