/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.registration.validation.validators.simple;

import com.topcoder.registration.validation.TestHelper;
import com.topcoder.registration.validation.RegistrationValidationHelper;

import com.topcoder.registration.validation.ValidationInfo;
import com.topcoder.util.datavalidator.AbstractObjectValidator;
import com.topcoder.util.datavalidator.BundleInfo;
import com.cronos.onlinereview.external.RatingInfo;
import com.cronos.onlinereview.external.ExternalUser;
import com.cronos.onlinereview.external.impl.ExternalUserImpl;
import com.cronos.onlinereview.external.RatingType;
import com.topcoder.registration.validation.RegistrationValidationConfigurationException;
import com.topcoder.registration.validation.ValidationProcessingException;

import junit.framework.TestCase;

/**
 * <p>
 * Unit test cases for class MemberMinimumNumberOfRatingsForRatingTypeValidator.
 * </p>
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
public class MemberMinimumNumberOfRatingsForRatingTypeValidatorTest extends
        TestCase {

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
     * Represents the rating type that will be validated.
     * </p>
     *
     */
    private RatingType ratingType = RatingType.DESIGN;

    /**
     * <p>
     * Represents the minimum number of ratings the user must have for the given
     * rating type.
     * </p>
     *
     */
    private int minimumNumRatings = 3;

    /**
     * <p>
     * The MemberMinimumNumberOfRatingsForRatingTypeValidator instance for
     * testing purpose.
     * </p>
     */
    private MemberMinimumNumberOfRatingsForRatingTypeValidator validator;

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
        TestHelper
                .loadXMLConfig("MemberMinimumNumberOfRatingsForRatingTypeValidator.xml");

        bundleInfo = RegistrationValidationHelper.createBundleInfo(NAMESPACE);

        validator = new MemberMinimumNumberOfRatingsForRatingTypeValidator(
                NAMESPACE);
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
     * Verifies MemberMinimumNumberOfRatingsForRatingTypeValidator subclasses
     * AbstractObjectValidator.
     * </p>
     */
    public void testInheritance() {
        assertTrue(
                "MemberMinimumNumberOfRatingsForRatingTypeValidator does not subclass AbstractObjectValidator.",
                new MemberMinimumNumberOfRatingsForRatingTypeValidator(
                        NAMESPACE) instanceof AbstractObjectValidator);
    }

    /**
     * <p>
     * Tests ctor MemberMinimumNumberOfRatingsForRatingTypeValidator(BundleInfo,
     * int, RatingType) for accuracy.
     * </p>
     *
     * <p>
     * It verifies the newly created
     * MemberMinimumNumberOfRatingsForRatingTypeValidator instance should not be
     * null.
     * </p>
     *
     */
    public void testCtor1() {
        validator = new MemberMinimumNumberOfRatingsForRatingTypeValidator(
                bundleInfo, minimumNumRatings, ratingType);
        assertNotNull(
                "Failed to create a new MemberMinimumNumberOfRatingsForRatingTypeValidator instance.",
                validator);
        BundleInfo bundleInfoField = validator.getBundleInfo();
        assertEquals("Failed to initialize the bundleInfo field.", null,
                TestHelper.compareBundleInfos(bundleInfoField, bundleInfo));
    }

    /**
     * <p>
     * Tests ctor MemberMinimumNumberOfRatingsForRatingTypeValidator(BundleInfo,
     * int, RatingType) for failure.
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
            validator = new MemberMinimumNumberOfRatingsForRatingTypeValidator(
                    bundleInfo, minimumNumRatings, ratingType);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * <p>
     * Tests ctor MemberMinimumNumberOfRatingsForRatingTypeValidator(BundleInfo,
     * int, RatingType) for failure.
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
            validator = new MemberMinimumNumberOfRatingsForRatingTypeValidator(
                    null, minimumNumRatings, ratingType);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * <p>
     * Tests ctor MemberMinimumNumberOfRatingsForRatingTypeValidator(BundleInfo,
     * int, RatingType) for failure.
     * </p>
     *
     * <p>
     * It tests the case when the minimumNumRatings is negative.
     * </p>
     *
     * <p>
     * Should have thrown IllegalArgumentException.
     * </p>
     *
     */
    public void testCtor1_NegativeMinimumReliability() {
        try {
            validator = new MemberMinimumNumberOfRatingsForRatingTypeValidator(
                    bundleInfo, -1, ratingType);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * <p>
     * Tests ctor MemberMinimumNumberOfRatingsForRatingTypeValidator(BundleInfo,
     * int, RatingType) for failure.
     * </p>
     *
     * <p>
     * It tests the case when the ratingType is null.
     * </p>
     *
     * <p>
     * Should have thrown IllegalArgumentException.
     * </p>
     *
     */
    public void testCtor1_NullRatingType() {
        try {
            validator = new MemberMinimumNumberOfRatingsForRatingTypeValidator(
                    bundleInfo, minimumNumRatings, null);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * <p>
     * Tests ctor MemberMinimumNumberOfRatingsForRatingTypeValidator(String) for
     * accuracy.
     * </p>
     *
     * <p>
     * It verifies the newly created
     * MemberMinimumNumberOfRatingsForRatingTypeValidator instance should not be
     * null.
     * </p>
     *
     */
    public void testCtor2() {
        validator = new MemberMinimumNumberOfRatingsForRatingTypeValidator(
                NAMESPACE);
        BundleInfo bundleInfoField = validator.getBundleInfo();
        assertEquals("Failed to initialize the bundleInfo field.", null,
                TestHelper.compareBundleInfos(bundleInfoField, bundleInfo));
        assertNotNull(
                "Failed to create MemberMinimumNumberOfRatingsForRatingTypeValidator instance.",
                validator);
    }

    /**
     * <p>
     * Tests ctor MemberMinimumNumberOfRatingsForRatingTypeValidator(String) for
     * accuracy.
     * </p>
     *
     * <p>
     * It verifies the newly created
     * MemberMinimumNumberOfRatingsForRatingTypeValidator instance should not be
     * null.
     * </p>
     *
     */
    public void testCtor2_NoBundleName() {
        validator = new MemberMinimumNumberOfRatingsForRatingTypeValidator(
                "simple.NoBundleName");
        assertNotNull(
                "Failed to create MemberMinimumNumberOfRatingsForRatingTypeValidator instance.",
                validator);
    }

    /**
     * <p>
     * Tests ctor MemberMinimumNumberOfRatingsForRatingTypeValidator(String) for
     * failure.
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
            new MemberMinimumNumberOfRatingsForRatingTypeValidator(null);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * <p>
     * Test ctor MemberMinimumNumberOfRatingsForRatingTypeValidator(String) for
     * failure.
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
            new MemberMinimumNumberOfRatingsForRatingTypeValidator("");
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * <p>
     * Tests ctor MemberMinimumNumberOfRatingsForRatingTypeValidator(String) for
     * failure.
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
            new MemberMinimumNumberOfRatingsForRatingTypeValidator("  ");
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * <p>
     * Tests ctor MemberMinimumNumberOfRatingsForRatingTypeValidator(String) for
     * failure.
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
            new MemberMinimumNumberOfRatingsForRatingTypeValidator(
                    "unknownNamespace");
            fail("RegistrationValidationConfigurationException expected.");
        } catch (RegistrationValidationConfigurationException e) {
            // good
        }
    }

    /**
     * <p>
     * Tests ctor MemberMinimumNumberOfRatingsForRatingTypeValidator(String) for
     * failure.
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
            new MemberMinimumNumberOfRatingsForRatingTypeValidator(
                    "simple.InvalidBundleInfo");
            fail("RegistrationValidationConfigurationException expected.");
        } catch (RegistrationValidationConfigurationException e) {
            // good
        }
    }

    /**
     * <p>
     * Tests ctor MemberMinimumNumberOfRatingsForRatingTypeValidator(String) for
     * failure.
     * </p>
     *
     * <p>
     * It tests the case when ratingType property is missing in the namespace.
     * </p>
     *
     * <p>
     * Should have thrown RegistrationValidationConfigurationException.
     * </p>
     *
     */
    public void testCtor2_MissingRatingType() {
        try {
            new MemberMinimumNumberOfRatingsForRatingTypeValidator(
                    "simple.MissingRatingType");
            fail("RegistrationValidationConfigurationException expected.");
        } catch (RegistrationValidationConfigurationException e) {
            // good
        }
    }

    /**
     * <p>
     * Tests ctor MemberMinimumNumberOfRatingsForRatingTypeValidator(String) for
     * failure.
     * </p>
     *
     * <p>
     * It tests the case when minimumNumRatings property is missing in the
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
            new MemberMinimumNumberOfRatingsForRatingTypeValidator(
                    "simple.MissingValidatorInfo");
            fail("RegistrationValidationConfigurationException expected.");
        } catch (RegistrationValidationConfigurationException e) {
            // good
        }
    }

    /**
     * <p>
     * Tests ctor MemberMinimumNumberOfRatingsForRatingTypeValidator(String) for
     * failure.
     * </p>
     *
     * <p>
     * It tests the case when the minimumNumRatings property value is invalid.
     * </p>
     *
     * <p>
     * Should have thrown RegistrationValidationConfigurationException.
     * </p>
     *
     */
    public void testCtor2_InvalidValidatorInfo() {
        try {
            new MemberMinimumNumberOfRatingsForRatingTypeValidator(
                    "simple.InvalidValidatorInfo");
            fail("RegistrationValidationConfigurationException expected.");
        } catch (RegistrationValidationConfigurationException e) {
            // good
        }
    }

    /**
     * <p>
     * Tests ctor MemberMinimumNumberOfRatingsForRatingTypeValidator(String) for
     * failure.
     * </p>
     *
     * <p>
     * It tests the case when the minimumNumRatings is negative.
     * </p>
     *
     * <p>
     * Should have thrown RegistrationValidationConfigurationException.
     * </p>
     *
     */
    public void testCtor2_NegativeValidatorInfo() {
        try {
            new MemberMinimumNumberOfRatingsForRatingTypeValidator(
                    "simple.NegativeValidatorInfo");
            fail("RegistrationValidationConfigurationException expected.");
        } catch (RegistrationValidationConfigurationException e) {
            // good
        }
    }

    /**
     * <p>
     * Tests
     * MemberMinimumNumberOfRatingsForRatingTypeValidator#getMessage(Object) for
     * accuracy.
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
        ExternalUser user = ((ValidationInfo) obj).getUser();
        RatingInfo ratingInfo = new RatingInfo(RatingType.DESIGN, 2300, 5,
                20, 80.2);
        ratingInfo.getReliability();
        ((ExternalUserImpl) user).addRatingInfo(ratingInfo);

        String message = validator.getMessage(obj);
        assertNull("Failed to get the error messages priority.", message);
    }

    /**
     * <p>
     * Tests
     * MemberMinimumNumberOfRatingsForRatingTypeValidator#getMessage(Object) for
     * accuracy.
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

        ExternalUser user = ((ValidationInfo) obj).getUser();
        ratingType = RatingType.DESIGN;
        int rating = 900;
        int numRatings = 2;
        int volatility = 20;
        double reliability = 50.3;
        RatingInfo ratingInfo = new RatingInfo(ratingType, rating, numRatings,
                volatility, reliability);
        ((ExternalUserImpl) user).addRatingInfo(ratingInfo);

        String message = validator.getMessage(obj);
        assertNotNull("Failed to get the error messages correctly.", message);
        assertTrue("Failed to get the error messages correctly.", (message
                .trim().length() > 0));
    }

    /**
     * <p>
     * Tests
     * MemberMinimumNumberOfRatingsForRatingTypeValidator#getMessage(Object) for
     * failure.
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
     * Tests
     * MemberMinimumNumberOfRatingsForRatingTypeValidator#getMessage(Object) for
     * failure.
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
     * MemberMinimumNumberOfRatingsForRatingTypeValidator#getMessage(Object) for
     * failure.
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
        validator = new MemberMinimumNumberOfRatingsForRatingTypeValidator(
                bundleInfo, minimumNumRatings, ratingType);
        TestHelper.setDefaultRegistrationValidator(validator,
                "validatorLog.txt");

        Object obj = TestHelper.createValidationInfoForTest();

        ExternalUser user = ((ValidationInfo) obj).getUser();
        ratingType = RatingType.DESIGN;
        int rating = 900;
        int numRatings = 2;
        int volatility = 20;
        double reliability = 50.3;
        RatingInfo ratingInfo = new RatingInfo(ratingType, rating, numRatings,
                volatility, reliability);
        ((ExternalUserImpl) user).addRatingInfo(ratingInfo);

        try {
            validator.getMessage(obj);
            fail("ValidationProcessingException expected.");
        } catch (ValidationProcessingException e) {
            // good
        }
    }

}
