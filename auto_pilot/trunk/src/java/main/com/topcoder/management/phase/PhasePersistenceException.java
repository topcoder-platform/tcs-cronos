/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */

package com.topcoder.management.phase;

import com.topcoder.util.errorhandling.BaseException;

/**
 * <p>
 * This exception should be thrown if there is error in phase persistence.
 * </p>
 * @author TCSDESIGNER, TCSDEVELOPER
 * @version 1.0
 */
public class PhasePersistenceException extends BaseException {

    /**
     * <p>
     * Constructs a new instance of ConfigurationException with the given error message.
     * </p>
     * @param message the error message
     */
    public PhasePersistenceException(String message) {
        super(message);
    }

    /**
     * <p>
     * Constructs a new instance of ConfigurationException with the given error message and inner
     * cause.
     * </p>
     * @param message the error message
     * @param cause the inner cause
     */
    public PhasePersistenceException(String message, Throwable cause) {
        super(message, cause);
    }
}
