/*
 * Copyright (C) 2008 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.clients.webservices.webserviceclients;

/**
 * <p>
 * This runtime exception signals an issue when the creation of the project status web service client failed
 * (wraps the MalformedURLException when creating the URL given a String wsdl document location).
 * </p>
 * <p>
 * Thread safety: This exception is not thread safe because parent exception is not thread safe. The
 * application should handle this exception in a thread-safe manner.
 * </p>
 *
 * @author Mafy, TCSDEVELOPER
 * @version 1.0
 */
public class ProjectStatusServiceClientCreationException extends ServiceClientCreationException {
    /**
     * <p>
     * Constructor. Constructs a new 'ProjectStatusServiceClientCreationException' instance with the given
     * message.
     * </p>
     *
     * @param message
     *            the exception message
     */
    public ProjectStatusServiceClientCreationException(String message) {
        super(message);
    }

    /**
     * <p>
     * Constructor. Constructs a new 'ProjectStatusServiceClientCreationException' exception with the given
     * message and cause.
     * </p>
     *
     * @param message
     *            the exception message
     * @param cause
     *            the exception cause
     */
    public ProjectStatusServiceClientCreationException(String message, Throwable cause) {
        super(message, cause);
    }
}
