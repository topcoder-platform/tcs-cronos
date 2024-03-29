/*
 * Copyright (C) 2008 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.jira.webservices;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import java.io.IOException;


/**
 * Some tests for JiraServiceException class.
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
public class JiraServiceExceptionTest extends TestCase {
    /**
     * Sample message to use in tests.
     */
    private static final String SAMPLE_MESSAGE = "sample message";

    /**
     * Sample cause to use in tests.
     */
    private static final Throwable SAMPLE_CAUSE = new IOException("some error");

    /**
     * <p>Returns the test suite of this class.</p>
     *
     * @return the test suite of this class.
     */
    public static Test suite() {
        return new TestSuite(JiraServiceExceptionTest.class);
    }

    /**
     * Tests first constructor.
     */
    public void testFirstConstructor() {
        try {
            throw new JiraServiceException();
        } catch (JiraServiceException ex) {
            assertNull("cause", ex.getCause());
            assertNull("message", ex.getMessage());
        }
    }

    /**
     * Tests second constructor.
     */
    public void testSecondConstructor() {
        try {
            throw new JiraServiceException(SAMPLE_MESSAGE);
        } catch (JiraServiceException ex) {
            assertEquals("message", SAMPLE_MESSAGE, ex.getMessage());
            assertNull("cause", ex.getCause());
        }
    }

    /**
     * Tests third constructor.
     */
    public void testThirdConstructor() {
        try {
            throw new JiraServiceException(SAMPLE_CAUSE);
        } catch (JiraServiceException ex) {
            assertSame("cause", SAMPLE_CAUSE, ex.getCause());
        }
    }

    /**
     * Tests forth constructor.
     */
    public void testForthConstructor() {
        try {
            throw new JiraServiceException(SAMPLE_MESSAGE, SAMPLE_CAUSE);
        } catch (JiraServiceException ex) {
            assertEquals("message", SAMPLE_MESSAGE, ex.getMessage());
            assertSame("cause", SAMPLE_CAUSE, ex.getCause());
        }
    }
}
