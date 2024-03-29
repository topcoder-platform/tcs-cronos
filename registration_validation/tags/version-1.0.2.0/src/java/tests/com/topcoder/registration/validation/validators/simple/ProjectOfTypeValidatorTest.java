/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.registration.validation.validators.simple;

import com.topcoder.registration.validation.TestHelper;
import com.topcoder.registration.validation.RegistrationValidationHelper;

import com.topcoder.registration.validation.ValidationInfo;
import com.topcoder.util.datavalidator.AbstractObjectValidator;
import com.topcoder.util.datavalidator.BundleInfo;
import com.topcoder.management.project.Project;
import com.topcoder.registration.validation.ValidationProcessingException;
import com.topcoder.registration.validation.RegistrationValidationConfigurationException;

import junit.framework.TestCase;

/**
 * <p>
 * Unit test cases for class ProjectOfTypeValidator.
 * </p>
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
public class ProjectOfTypeValidatorTest extends TestCase {

    /**
     * <p>
     * Represents a valid formed namespace for this class.
     * </p>
     */
    private static final String NAMESPACE = "simple.Good";

    /**
     * <p>
     * Represents the resource bundle information used by this validator.
     * </p>
     */
    private BundleInfo bundleInfo;

    /**
     * <p>
     * Represents the type ID the project should have. type.
     * </p>
     *
     */
    private long projectTypeId = 3;

    /**
     * <p>
     * The ProjectOfTypeValidator instance for testing purpose.
     * </p>
     */
    private ProjectOfTypeValidator validator;




    /**
     * <p>
     * Sets up test environment.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    protected void setUp() throws Exception {
        TestHelper.clearConfig();
        TestHelper.loadXMLConfig("DataValidationRegistrationValidator.xml");
        TestHelper.loadXMLConfig("Document_Generator.xml");
        TestHelper.loadXMLConfig("ProjectOfTypeValidator.xml");

        bundleInfo = RegistrationValidationHelper.createBundleInfo(NAMESPACE);

        validator = new ProjectOfTypeValidator(NAMESPACE);
        TestHelper.setDefaultRegistrationValidator(validator,
                "validatorLog.txt");

    }

    /**
     * Clears the testing environment.
     *
     * @throws Exception
     *             when error occurs
     * @throws Exception
     *             to JUnit
     */
    protected void tearDown() throws Exception {
        TestHelper.clearConfig();
    }

    /**
     * <p>
     * Inheritance test.
     * </p>
     *
     * <p>
     * Verifies ProjectOfTypeValidator subclasses AbstractObjectValidator.
     * </p>
     */
    public void testInheritance() {
        assertTrue(
                "ProjectOfTypeValidator does not subclass AbstractObjectValidator.",
                new ProjectOfTypeValidator(NAMESPACE) instanceof AbstractObjectValidator);
    }

    /**
     * <p>
     * Tests ctor ProjectOfTypeValidator(BundleInfo, long) for accuracy.
     * </p>
     *
     * <p>
     * It verifies the newly created ProjectOfTypeValidator instance should not
     * be null.
     * </p>
     *
     */
    public void testCtor1() {


        validator = new ProjectOfTypeValidator(bundleInfo, projectTypeId);
        assertNotNull(
                "Failed to create a new ProjectOfTypeValidator instance.",
                validator);
        BundleInfo bundleInfoField = validator.getBundleInfo();
        assertEquals("Failed to initialize the bundleInfo field.", null,
                TestHelper.compareBundleInfos(bundleInfoField, bundleInfo));


    }

    /**
     * <p>
     * Tests ctor ProjectOfTypeValidator(BundleInfo, long) for failure.
     * </p>
     *
     * <p>
     * It tests the case when the bundleInfo is invalid.
     * </p>
     *
     * <p>
     * Should have thrown IllegalArgumentException.
     * </p>
     *
     */
    public void testCtor1_InvalidBundleInfo() {

        try {
            bundleInfo = new BundleInfo();
            validator = new ProjectOfTypeValidator(bundleInfo, projectTypeId);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * <p>
     * Tests ctor ProjectOfTypeValidator(BundleInfo, long) for failure.
     * </p>
     *
     * <p>
     * It tests the case when the bundleInfo is null.
     * </p>
     *
     * <p>
     * Should have thrown IllegalArgumentException.
     * </p>
     *
     */
    public void testCtor1_NullBundleInfo() {


        try {
            validator = new ProjectOfTypeValidator(null, projectTypeId);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * <p>
     * Tests ctor ProjectOfTypeValidator(BundleInfo, long) for failure.
     * </p>
     *
     * <p>
     * It tests the case when the projectTypeId is negative.
     * </p>
     *
     * <p>
     * Should have thrown IllegalArgumentException.
     * </p>
     *
     */
    public void testCtor1_NegativeMinimumReliability() {


        try {
            validator = new ProjectOfTypeValidator(bundleInfo, -1);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * <p>
     * Tests ctor ProjectOfTypeValidator(String) for accuracy.
     * </p>
     *
     * <p>
     * It verifies the newly created ProjectOfTypeValidator instance should not
     * be null.
     * </p>
     *
     */
    public void testCtor2() {

        validator = new ProjectOfTypeValidator(NAMESPACE);
        BundleInfo bundleInfoField = validator.getBundleInfo();
        assertEquals("Failed to initialize the bundleInfo field.", null,
                TestHelper.compareBundleInfos(bundleInfoField, bundleInfo));

    }

    /**
     * <p>
     * Tests ctor ProjectOfTypeValidator(String) for accuracy.
     * </p>
     *
     * <p>
     * It verifies the newly created ProjectOfTypeValidator instance should not
     * be null.
     * </p>
     *
     */
    public void testCtor2_NoBundleName() {
        validator = new ProjectOfTypeValidator("simple.NoBundleName");
        assertNotNull("Failed to create ProjectOfTypeValidator instance.",
                validator);
    }

    /**
     * <p>
     * Tests ctor ProjectOfTypeValidator(String) for failure.
     * </p>
     *
     * <p>
     * It tests the case when the namespace is null.
     * </p>
     *
     * <p>
     * Should have thrown IllegalArgumentException.
     * </p>
     *
     */
    public void testCtor2_NullNamespace() {

        try {
            new ProjectOfTypeValidator(null);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * <p>
     * Test ctor ProjectOfTypeValidator(String) for failure.
     * </p>
     *
     * <p>
     * It tests the case when the namespace is empty.
     * </p>
     *
     * <p>
     * Should have thrown IllegalArgumentException.
     * </p>
     *
     */
    public void testCtor2_EmptyNamespace() {

        try {
            new ProjectOfTypeValidator("");
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * <p>
     * Tests ctor ProjectOfTypeValidator(String) for failure.
     * </p>
     *
     * <p>
     * It tests the case when the namespace is full of space.
     * </p>
     *
     * <p>
     * Should have thrown IllegalArgumentException.
     * </p>
     *
     */
    public void testCtor2_TrimmedEmptyNamespace() {

        try {
            new ProjectOfTypeValidator("  ");
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * <p>
     * Tests ctor ProjectOfTypeValidator(String) for failure.
     * </p>
     *
     * <p>
     * It tests the case when the namespace is unknown.
     * </p>
     *
     * <p>
     * Should have thrown RegistrationValidationConfigurationException.
     * </p>
     *
     */
    public void testCtor2_UnknownNamespace() {

        try {
            new ProjectOfTypeValidator("unknownNamespace");
            fail("RegistrationValidationConfigurationException expected.");
        } catch (RegistrationValidationConfigurationException e) {
            // good
        }
    }

    /**
     * <p>
     * Tests ctor ProjectOfTypeValidator(String) for failure.
     * </p>
     *
     * <p>
     * It tests the case when bundleInfo is not configured properly.
     * </p>
     *
     * <p>
     * Should have thrown RegistrationValidationConfigurationException.
     * </p>
     *
     */
    public void testCtor2_InvalidBundleInfo() {

        try {
            new ProjectOfTypeValidator("simple.InvalidBundleInfo");
            fail("RegistrationValidationConfigurationException expected.");
        } catch (RegistrationValidationConfigurationException e) {
            // good
        }
    }

    /**
     * <p>
     * Tests ctor ProjectOfTypeValidator(String) for failure.
     * </p>
     *
     * <p>
     * It tests the case when projectTypeId property is missing in the
     * namespace.
     * </p>
     *
     * <p>
     * Should have thrown RegistrationValidationConfigurationException.
     * </p>
     *
     */
    public void testCtor2_MissingValidatorInfo() {

        try {
            new ProjectOfTypeValidator("simple.MissingValidatorInfo");
            fail("RegistrationValidationConfigurationException expected.");
        } catch (RegistrationValidationConfigurationException e) {
            // good
        }
    }

    /**
     * <p>
     * Tests ctor ProjectOfTypeValidator(String) for failure.
     * </p>
     *
     * <p>
     * It tests the case when the projectTypeId property value is invalid.
     * </p>
     *
     * <p>
     * Should have thrown RegistrationValidationConfigurationException.
     * </p>
     *
     */
    public void testCtor2_InvalidValidatorInfo() {

        try {
            new ProjectOfTypeValidator("simple.InvalidValidatorInfo");
            fail("RegistrationValidationConfigurationException expected.");
        } catch (RegistrationValidationConfigurationException e) {
            // good
        }
    }

    /**
     * <p>
     * Tests ctor ProjectOfTypeValidator(String) for failure.
     * </p>
     *
     * <p>
     * It tests the case when the projectTypeId is negative.
     * </p>
     *
     * <p>
     * Should have thrown RegistrationValidationConfigurationException.
     * </p>
     *
     */
    public void testCtor2_NegativeValidatorInfo() {

        try {
            new ProjectOfTypeValidator("simple.NegativeValidatorInfo");
            fail("RegistrationValidationConfigurationException expected.");
        } catch (RegistrationValidationConfigurationException e) {
            // good
        }
    }

    /**
     * <p>
     * Tests ProjectOfTypeValidator#getMessage(Object) for accuracy.
     * </p>
     *
     * <p>
     * It tests the case when the obj is valid.
     * </p>
     *
     * <p>
     * Should have returned null if obj is valid.
     * </p>
     *
     * <p>
     * It verifies the method return null if obj is valid. Otherwise, an error
     * message is returned.
     * </p>
     *
     */
    public void testGetMessage_True() {

        Object obj = TestHelper.createValidationInfoForTest();
        // Sets validationInfo.Project.ProjectHeader
        // .ProjectCategory.ProjectType.Id
        Project project = ((ValidationInfo) obj).getProject()
                .getProjectHeader();
        project.getProjectCategory().getProjectType().setId(3);

        String message = validator.getMessage(obj);
        assertNull("Failed to get the error messages priority.", message);


    }

    /**
     * <p>
     * Tests ProjectOfTypeValidator#getMessage(Object) for accuracy.
     * </p>
     *
     * <p>
     * It tests the case when the obj is invalid.
     * </p>
     *
     * <p>
     * Should have returned an error message if obj is invalid.
     * </p>
     *
     */
    public void testGetMessage_False() {


        Object obj = TestHelper.createValidationInfoForTest();
        // Sets validationInfo.Project.ProjectHeader
        // .ProjectCategory.ProjectType.Id
        Project project = ((ValidationInfo) obj).getProject()
                .getProjectHeader();
        project.getProjectCategory().getProjectType().setId(1);

        String message = validator.getMessage(obj);
        assertNotNull("Failed to get the error messages correctly.", message);
        assertTrue("Failed to get the error messages correctly.", (message
                .trim().length() > 0));


    }

    /**
     * <p>
     * Tests ProjectOfTypeValidator#getMessage(Object) for failure.
     * </p>
     *
     * <p>
     * It tests the case when the obj is null.
     * </p>
     *
     * <p>
     * Should have thrown IllegalArgumentException.
     * </p>
     *
     */
    public void testGetMessage_Null() {

        try {
            validator.getMessage(null);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * <p>
     * Tests ProjectOfTypeValidator#getMessage(Object) for failure.
     * </p>
     *
     * <p>
     * It tests the case when the obj is not ValidationInfo.
     * </p>
     *
     * <p>
     * Should have thrown IllegalArgumentException.
     * </p>
     *
     */
    public void testGetMessage_NotValidationInfo() {

        try {
            validator.getMessage(new Object());
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * <p>
     * Tests
     * MemberNotExceededMaxProjectRegistrationLimitValidator#getMessage(Object)
     * for failure.
     * </p>
     *
     * <p>
     * It tests the case when the template is bad formed when generating
     * message.
     * </p>
     *
     * <p>
     * Should have thrown ValidationProcessingException.
     * </p>
     *
     */
    public void testGetMessage_BadTemplate() {


        bundleInfo.setDefaultMessage("badTemplate.txt");
        validator = new ProjectOfTypeValidator(bundleInfo, projectTypeId);
        TestHelper.setDefaultRegistrationValidator(validator,
                "validatorLog.txt");

        Object obj = TestHelper.createValidationInfoForTest();
        // Sets validationInfo.Project.ProjectHeader
        // .ProjectCategory.ProjectType.Id
        Project project = ((ValidationInfo) obj).getProject()
                .getProjectHeader();
        project.getProjectCategory().getProjectType().setId(1);
        try {
            validator.getMessage(obj);
            fail("ValidationProcessingException expected.");
        } catch (ValidationProcessingException e) {
            // good
        }
    }

}
