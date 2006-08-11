/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 *
 * User Project Data Store 1.0
 */
package com.cronos.onlinereview.external;

import junit.framework.TestCase;

/**
 * <p>Tests the ConfigException class.</p>
 *
 * @author oodinary
 * @version 1.0
 */
public class ConfigExceptionUnitTest extends TestCase {

    /**
     * <p>The Default Exception Message.</p>
     */
    private final String defaultExceptionMessage = "DefaultExceptionMessage";

    /**
     * <p>The Default Throwable Message.</p>
     */
    private final String defaultThrowableMessage = "DefaultThrowableMessage";

    /**
     * <p>An ConfigException instance for testing.</p>
     */
    private ConfigException defaultException = null;

    /**
     * <p>Initialization.</p>
     *
     * @throws Exception to JUnit.
     */
    protected void setUp() throws Exception {
        defaultException = new ConfigException(defaultExceptionMessage);
    }

    /**
     * <p>Set defaultException to null.</p>
     *
     * @throws Exception to JUnit.
     */
    protected void tearDown() throws Exception {
        defaultException = null;
    }

    /**
     * <p>Tests the ctor(String).</p>
     * <p>The ConfigException instance should be created successfully.</p>
     */
    public void testCtor_String() {

        assertNotNull("ConfigException should be accurately created.", defaultException);
        assertTrue("defaultException should be instance of ConfigException.",
                defaultException instanceof ConfigException);
        assertTrue("ConfigException should be accurately created with the same Exception message.",
                defaultException.getMessage().indexOf(defaultExceptionMessage) >= 0);
    }

    /**
     * <p>Tests the ctor(String, Throwable).</p>
     * <p>The ConfigException instance should be created successfully.</p>
     */
    public void testCtor_StringThrowable() {

        defaultException = new ConfigException(defaultExceptionMessage,
                new Throwable(defaultThrowableMessage));

        assertNotNull("ConfigException should be accurately created.", defaultException);
        assertTrue("defaultException should be instance of ConfigException.",
                defaultException instanceof ConfigException);
        assertTrue("ConfigException should be accurately created with the same Exception message.",
                defaultException.getMessage().indexOf(defaultExceptionMessage) >= 0);
        assertTrue("ConfigException should be accurately created with the same Throwable message.",
                defaultException.getMessage().indexOf(defaultThrowableMessage) >= 0);
    }
}
