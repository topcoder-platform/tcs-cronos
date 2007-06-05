/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.registration.validation;

import com.topcoder.util.datavalidator.BundleInfo;
import com.topcoder.util.log.Log;
import com.topcoder.util.log.Level;
import com.cronos.onlinereview.external.ExternalUser;

/**
 * <p>
 * This class extends the <code>AbstractConfigurableValidator</code> class and
 * is used for testing.
 * </p>
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
public class MockAbstractConfigurableValidator extends
        AbstractConfigurableValidator {
    /**
     * <p>
     * Represents the class name used to log.
     * </p>
     *
     */
    private final String className = "MockAbstractConfigurableValidator";

    /**
     * Initializes the resource bundle based on the input parameters.
     *
     *
     * @param bundleInfo
     *            The resource bundle info
     * @throws IllegalArgumentException
     *             if any of the parameters in bundleInfo is null or an empty
     *             string (for string parameters)
     */
    public MockAbstractConfigurableValidator(BundleInfo bundleInfo) {
        super(bundleInfo);
    }

    /**
     * Initializes the defaultMessage to the input string. This can be null or
     * empty and it is up to the user to make this decision.
     * <p>
     *
     * @param validationMessage
     *            String This is the validation message to use for the
     *            underlying validator.
     * @throws IllegalArgumentException
     *             if the input parameter is null or empty
     */
    public MockAbstractConfigurableValidator(String validationMessage) {
        super(validationMessage);
    }

    /**
     * <p>
     * Checks if the development rating of the user is not available.
     * </p>
     * <p>
     *
     * @return null if obj is valid. Otherwise, an error message is returned.
     * @param obj
     *            Object to validate.
     * @throws IllegalArgumentException
     *             If obj is null and not a ValidationInfo object
     */
    public String getMessage(Object obj) {
        String methodName = className + ".getMessage(Object obj)";
        Log logger = getRegistrationValidator().getLog();

        // log entrance
        RegistrationValidationHelper.log(logger, Level.INFO, "enter " + methodName);

        try {
            RegistrationValidationHelper.validateIsValidationInfo(obj,
                    "Object to validate");
            String message = null;

            ValidationInfo validationInfo = (ValidationInfo) obj;
            ExternalUser user = validationInfo.getUser();
            if (user.getDevRating().compareTo("N/A") == 0) {
                message = getValidationMessage();
            }

            // Log return value
            RegistrationValidationHelper.log(logger, Level.DEBUG,
                "return value is [" + message + "]");
            // Log exit
            RegistrationValidationHelper.log(logger, Level.INFO, "exit " + methodName);

            return message;
        } catch (IllegalArgumentException e) {
            RegistrationValidationHelper.log(logger, Level.ERROR, "Error[" + e.getClass().getName()
                + "] occurs in " + methodName + ", cause:" + e.getMessage());
            throw e;
        }

    }
}