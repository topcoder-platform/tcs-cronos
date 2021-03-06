/*
 * Copyright (C) 2008 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.service.studio.submission.accuracytests;

import com.topcoder.service.studio.submission.SubmissionManagementException;

import com.topcoder.util.errorhandling.ExceptionData;

import junit.framework.TestCase;


/**
 * The accuracy test for the exception {@link SubmissionManagementException}.
 *
 * @author KLW
 * @version 1.0
 */
public class SubmissionManagementExceptionAccuracyTest extends TestCase {
    /**
     * The accuracy test for the constructor {@link SubmissionManagementException#SubmissionManagementException(String)}.
     */
    public void testCtor_1() {
        String message = "test";
        SubmissionManagementException ep = new SubmissionManagementException(message);
        assertNotNull("The instance should not be null.", ep);
        assertEquals("The message of the exception is incorrect.", message, ep.getMessage());
    }

    /**
     * The accuracy test for the constructor {@link SubmissionManagementException#SubmissionManagementException(String, Throwable)}.
     */
    public void testCtor_2() {
        Throwable cause = new Exception();
        String message = "test";
        SubmissionManagementException ep = new SubmissionManagementException(message, cause);
        assertNotNull("The instance should not be null.", ep);
        assertEquals("The message of the exception is incorrect.", message, ep.getMessage());
        assertEquals("The inner cause of this exception is incorrect.", cause, ep.getCause());
    }

    /**
     * The accuracy test for the constructor
     * {@link SubmissionManagementException#SubmissionManagementException(String, com.topcoder.util.errorhandling.ExceptionData)}.
     */
    public void testCtor_3() {
        ExceptionData data = new ExceptionData();
        data.setErrorCode("888888");

        String message = "test";
        SubmissionManagementException ep = new SubmissionManagementException(message, data);
        assertNotNull("The instance should not be null.", ep);
        assertEquals("The message of the exception is incorrect.", message, ep.getMessage());
        assertEquals("The exception data of this exception is incorrect.", "888888", ep.getErrorCode());
    }

    /**
     * The accuracy test for the constructor
     * {@link SubmissionManagementException#SubmissionManagementException(String, Throwable, ExceptionData)}.
     */
    public void testCtor_4() {
        Throwable cause = new Exception();
        ExceptionData data = new ExceptionData();
        data.setErrorCode("888888");

        String message = "test";
        SubmissionManagementException ep = new SubmissionManagementException(message, cause, data);
        assertNotNull("The instance should not be null.", ep);
        assertEquals("The message of the exception is incorrect.", message, ep.getMessage());
        assertEquals("The inner cause of this exception is incorrect.", cause, ep.getCause());
        assertEquals("The exception data of this exception is incorrect.", "888888", ep.getErrorCode());
    }
}
