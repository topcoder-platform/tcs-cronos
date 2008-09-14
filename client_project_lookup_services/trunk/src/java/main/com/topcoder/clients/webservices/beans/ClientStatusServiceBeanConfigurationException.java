/*
 * Copyright (C) 2008 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.clients.webservices.beans;

/**
 * <p>
 * This runtime exception signals an issue if the configured value is invalid in ClientStatusServiceBean (in
 * this design, when managers are null or required resources are null or empty String). Wraps the underlying
 * exceptions when using the configured values.
 * </p>
 * <p>
 * Thread safety: This exception is not thread safe because parent exception is not thread safe. The
 * application should handle this exception in a thread-safe manner.
 * </p>
 *
 * @author Mafy, TCSDEVELOPER
 * @version 1.0
 */
public class ClientStatusServiceBeanConfigurationException extends ServiceBeanConfigurationException {
    /**
     * <p>
     * Constructor. Constructs a new 'ClientStatusServiceBeanConfigurationException' instance with the given
     * message.
     * </p>
     *
     * @param message
     *            the exception message
     */
    public ClientStatusServiceBeanConfigurationException(String message) {
        super(message);
    }

    /**
     * <p>
     * Constructor. Constructs a new 'ClientStatusServiceBeanConfigurationException' exception with the given
     * message and cause.
     * </p>
     *
     * @param message
     *            the exception message
     * @param cause
     *            the exception cause
     */
    public ClientStatusServiceBeanConfigurationException(String message, Throwable cause) {
        super(message, cause);
    }
}
