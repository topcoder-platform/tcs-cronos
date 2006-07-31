/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.phase.accuracytests;

import junit.framework.TestCase;

import com.topcoder.management.phase.PhaseManagementException;
import com.topcoder.util.errorhandling.BaseException;

/**
 * <p>
 * Accuracy test fixture for PhaseManagementException class.
 * </p>
 * @author Thinfox
 * @version 1.0
 */
public class PhaseManagementExceptionTests extends TestCase {
    /**
     * <p>
     * The default error message used for tests.
     * </p>
     */
    private static final String ERROR_MESSAGE = "error";

    /**
     * <p>
     * Tests the constructor with message.
     * </p>
     */
    public void testConstructor_Message() {
        Exception exception = new PhaseManagementException(ERROR_MESSAGE);
        assertTrue("Incorrect inheritance.", exception instanceof BaseException);
        assertEquals("Incorrect error message.", ERROR_MESSAGE, exception.getMessage());
        assertNull("Incorrect cause.", exception.getCause());
    }

    /**
     * <p>
     * Tests the constructor with message and cause.
     * </p>
     */
    public void testConstructor_MessageCause() {
        Throwable cause = new Throwable();
        Exception exception = new PhaseManagementException(ERROR_MESSAGE, cause);
        assertTrue("Incorrect inheritance.", exception instanceof BaseException);
        assertEquals("Incorrect error message.", ERROR_MESSAGE + ", caused by null", exception.getMessage());
        assertEquals("Incorrect cause.", cause, exception.getCause());
    }
}