/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.registration.validation.validators.simple;

import com.topcoder.project.service.ProjectServices;
import com.topcoder.project.service.FullProjectData;
import com.topcoder.management.resource.Resource;
import com.topcoder.registration.validation.AbstractConfigurableValidator;
import com.topcoder.registration.validation.RegistrationValidationHelper;
import com.topcoder.registration.validation.ValidationInfo;
import com.topcoder.util.datavalidator.BundleInfo;
import com.topcoder.util.log.Log;
import com.topcoder.util.log.Level;

/**
 * A simple custom validator used for test. This validator checks if a member is
 * registered with a given role for a project. Project Services is used here.
 * <p>
 * This class extends AbstractConfigurableValidator that implements most of the
 * validation methods, so this class only needs to implement the getMessage
 * method.
 * </p>
 * <p>
 * The DataValidationRegistrationValidator manager, available via the
 * getRegistrationValidator method, provides all necessary managers and services
 * for this validator. It also provides the Log for logging.
 * </p>
 * <p>
 * Thread Safety: This class is mutable and thus is not thread-safe, but the
 * actual properties and managers used to validate are immutable, so this class
 * will be effectively thread-safe in the context of its usage in this
 * component.
 * </p>
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
public class MemberMustBeRegisteredValidator extends
        AbstractConfigurableValidator {
    /**
     * <p>
     * Represents the class name used to log.
     * </p>
     *
     */
    private final String className = "MemberMustBeRegisteredValidator";

    /**
     * Initializes the resource bundle based on the input parameters.
     *
     * @param bundleInfo
     *            The resource bundle info
     * @throws IllegalArgumentException
     *             if any of the parameters in bundleInfo is null or an empty
     *             string (for string parameters)
     * @throws IllegalArgumentException
     *             if maxRegistrationCount is negative
     */
    public MemberMustBeRegisteredValidator(BundleInfo bundleInfo) {
        super(bundleInfo);
    }

    /**
     * Initializes the resource bundle from configuration info from the Config
     * Manager.
     * <p>
     *
     * @param namespace
     *            The configuration namespace
     * @throws IllegalArgumentException
     *             If namespace is null/empty
     */
    public MemberMustBeRegisteredValidator(String namespace) {
        super("temporary");

        RegistrationValidationHelper.validateStringNotNullOrEmpty(namespace,
                "namespace");
        // Creates BundleInfo from base parameters
        BundleInfo bundleInfo = RegistrationValidationHelper
                .createBundleInfo(namespace);
        // Sets the BundleInfo
        setResourceBundleInfo(bundleInfo);

    }

    /**
     * <p>
     * Checks that a member is registered to the tournament.
     * </p>
     *
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

            ValidationInfo validationInfo = (ValidationInfo) obj;
            Resource resource = RegistrationValidationHelper.findResource(
                    validationInfo, logger);

            ProjectServices projectServices = getRegistrationValidator()
                    .getProjectServices();

            // Finds the active projects
            RegistrationValidationHelper.log(logger, Level.DEBUG,
                "Starts calling ProjectServices#findActiveProjects()");
            FullProjectData[] projects = projectServices.findActiveProjects();
            RegistrationValidationHelper.log(logger, Level.DEBUG,
                "Finishes calling ProjectServices#findActiveProjects()");

            // Finds the projects whose activeProject.resources contains this resource.id
            int registrationCount = 0;
            if (resource != null) {
                for (int i = 0; i < projects.length; i++) {
                    Resource[] projectResources = projects[i].getResources();
                    for (int j = 0; j < projectResources.length; j++) {
                        if (projectResources[j].getId() == resource.getId()) {
                            registrationCount++;
                            break;
                        }
                    }
                }
            }

            String message = null;

            // If there is not any project whose activeProject.resources contains this resource.id,
            // then fill the message.
            if (registrationCount == 0) {
                String data = RegistrationValidationHelper
                        .buildStandInfo(validationInfo);
                // Log variable value
                RegistrationValidationHelper.log(logger, Level.DEBUG,
                    "the value of data stirng is [" + data + "]");

                String messageTemplate = this.getValidationMessage();
                message = RegistrationValidationHelper.fillMessage(
                        messageTemplate, data, logger, methodName);
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
