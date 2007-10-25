/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.registration.validation.accuracytests;

import junit.framework.TestCase;

import com.topcoder.registration.validation.ConfigurableValidator;
import com.topcoder.registration.validation.DataValidationRegistrationValidator;
import com.topcoder.registration.validation.ValidationInfo;
import com.topcoder.registration.validation.validators.conditional.RegisteringResourceRoleConditionalValidator;
import com.topcoder.registration.validation.validators.simple.MemberMinimumNumberOfRatingsForRatingTypeValidator;

/**
 * This class contains unit tests for
 * <code>RegisteringResourceRoleConditionalValidator</code> class.
 * 
 * @author TCSDEVELOPER
 * @version 1.0
 */
public class AccuracyTestRegisteringResourceRoleConditionalValidator extends TestCase {
	/**
	 * The RegisteringResourceRoleConditionalValidator instance used to test.
	 */
	private RegisteringResourceRoleConditionalValidator validator = null;

	/**
	 * The DataValidationRegistrationValidator instance used to test.
	 */
	private DataValidationRegistrationValidator drv = null;

	/**
	 * The ConfigurableValidator instance used to test.
	 */
	private ConfigurableValidator cv = null;

	/**
	 * Set Up the test environment before testing.
	 * 
	 * @throws Exception
	 *             to JUnit.
	 */
	protected void setUp() throws Exception {
		super.setUp();
		AccuracyTestHelper.loadNamespaces();
		validator = new RegisteringResourceRoleConditionalValidator(
				"RegisteringResourceRoleConditionalValidator.ns");
		drv = new DataValidationRegistrationValidator();

		cv = new MemberMinimumNumberOfRatingsForRatingTypeValidator(
				"MemberMinimumNumberOfRatingsForRatingTypeValidator.ns");
		AccuracyTestHelper.setLog(validator);
	}

	/**
	 * Clean up the test environment after testing.
	 * 
	 * @throws Exception
	 *             to JUnit.
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
		AccuracyTestHelper.clearNamespaces();
	}

	/**
	 * Function test : Tests
	 * <code>RegisteringResourceRoleConditionalValidator(BundleInfo bundleInfo,
	 * int minimumNumRatings, RatingType ratingType)</code>
	 * method for accuracy.
	 * 
	 * @throws Exception
	 *             to JUnit.
	 */
	public void testRegisteringResourceRoleConditionalValidator1Accuracy() throws Exception {
		assertNotNull("Null is allowed.", new RegisteringResourceRoleConditionalValidator(cv, 1));
	}

	/**
	 * Function test : Tests <code>getMessage(Object obj)</code> method for
	 * accuracy, numRatings >= this.minimumNumRatings
	 * 
	 * @throws Exception
	 *             to JUnit.
	 */
	public void testGetMessageAccuracy() throws Exception {
		ValidationInfo info = AccuracyTestHelper.createValidationInfo();
		String msg = validator.getMessage(info);
		assertNull("Null is expected.", msg);
	}

	/**
	 * Function test : Tests <code>getMessage(Object obj)</code> method for
	 * accuracy, numRatings < this.minimumNumRatings
	 * 
	 * @throws Exception
	 *             to JUnit.
	 */
	public void testGetMessageAccuracy2() throws Exception {
		validator = new RegisteringResourceRoleConditionalValidator(cv, 51);
		AccuracyTestHelper.setLog(validator);
		String msg = validator.getMessage(AccuracyTestHelper.createValidationInfo());
		assertNull("Null is allowed.", msg);
	}

	/**
	 * Function test : Tests
	 * <code>setRegistrationValidator(DataValidationRegistrationValidator dataValidationRegistrationValidator)</code>
	 * method for accuracy.
	 * 
	 * @throws Exception
	 *             to JUnit.
	 */
	public void testSetRegistrationValidatorAccuracy() throws Exception {
		validator.setRegistrationValidator(drv);
	}
}