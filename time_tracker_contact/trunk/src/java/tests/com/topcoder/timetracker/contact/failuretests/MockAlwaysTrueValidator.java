/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.timetracker.contact.failuretests;

import com.topcoder.util.datavalidator.ObjectValidator;

/**
 * <p>
 * Mock validator always return true.
 * </p>
 *
 * @author TCSDEVELOPER
 * @version 3.2
 */
public class MockAlwaysTrueValidator implements ObjectValidator {

    /**
     * <p>
     * Default Cosntructor.
     * </p>
     *
     */
    public MockAlwaysTrueValidator() {
    }

    /**
     * <p>
     * Always returns null, since this validator considers any object to be
     * valid.
     * </p>
     *
     * @return null always.
     * @param obj The object to validate (this parameter is ignored)
     */
    public String getMessage(Object obj) {
        return null;
    }

    /**
     * <p>
     * Always returns true, since this validator considers any object to be
     * valid.
     * </p>
     *
     * @return True always.
     * @param obj The object to validate (this parameter is ignored)
     */
    public boolean valid(Object obj) {
        return true;
    }
}
