/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.login;

import com.topcoder.util.config.ConfigManager;
import com.topcoder.util.config.UnknownNamespaceException;

/**
 * Utility class for Online Review Login Component.
 * <p>
 * It defines some common routines used by this component. It includes methods to get
 * property value from <code>ConfigManager</code>, as well as methods to validate
 * parameters.
 * </p>
 *
 * @author woodjohn, maone
 * @version 1.0
 */
public final class Util {

    /**
     * Represents the key of user name value.
     * <p>
     * It can be used to retrieve user name value from <code>ActionForm</code> and <code>Principal</code>.
     * </p>
     */
    public static final String USERNAME = "userName";

    /**
     * Represents the key of password value.
     * <p>
     * It can be used to retrieve password value from <code>ActionForm</code> and <code>Principal</code>.
     * </p>
     */
    public static final String PASSWORD = "password";

    /**
     * Represents the key of redirectToProjectID value.
     * <p>
     * It can be used to retrieve redirectToProjectID value from <code>ActionForm</code> and <code>Principal</code>.
     * </p>
     */
    public static final String REDIRECT_TO_PROJECT_ID = "redirectToProjectID";

    /**
     * Represents the key of forwardUrl value.
     * <p>
     * It can be used to retrieve forward url value from <code>ActionForm</code> and <code>Principal</code>.
     * </p>
     */
    public static final String FOWARD_URL = "forwardUrl";

    /**
     * Private constructor.
     */
    private Util() {
        // emtpy
    }

    /**
     * Get optional property value for given <code>key</code> from the <code>namespace</code>.
     *
     * @param namespace the namespace to get configuration
     * @param key the key of desired value
     * @return the desired property value, or null if the value doesn't exist
     * @throws ConfigurationException if failed to get the value
     */
    public static String getOptionalPropertyString(String namespace, String key)
        throws ConfigurationException {
        try {
            return ConfigManager.getInstance().getString(namespace, key);
        } catch (UnknownNamespaceException e) {
            throw new ConfigurationException("Failed to get configuration value.", e);
        }
    }

    /**
     * Get required property value for given <code>key</code> from the <code>namespace</code>.
     *
     * @param namespace the namespace to get configuration
     * @param key the key of desired value
     * @return the desired property value
     * @throws ConfigurationException if failed to get the value
     */
    public static String getRequiredPropertyString(String namespace, String key)
        throws ConfigurationException {
        String val = getOptionalPropertyString(namespace, key);
        if (val == null || val.trim().length() == 0) {
            throw new ConfigurationException("Property " + key + " is required in "
                    + namespace);
        }
        return val;
    }

    /**
     * Validate whether the given value is null.
     *
     * @param value
     *            the value to be validated
     * @param name
     *            the name associated with the value (should be non-null)
     * @return the validated value
     *
     * @throws IllegalArgumentException
     *             is the given value is null
     */
    public static Object validateNotNull(Object value, String name) {
        if (value == null) {
            throw new IllegalArgumentException(name + " is null.");
        }
        return value;
    }

    /**
     * Validate whether the given String value is null or empty.
     *
     * @param value
     *            the value to be validated
     * @param name
     *            the name associated with the value (should be non-null)
     * @return the validated value
     *
     * @throws IllegalArgumentException
     *             is the given value is null or empty
     */
    public static String validateNotNullOrEmpty(String value, String name) {
        validateNotNull(value, name);
        if (value.trim().length() == 0) {
            throw new IllegalArgumentException(name + " is empty.");
        }
        return value;
    }
}
