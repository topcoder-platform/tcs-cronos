/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.registration.validation.failuretests;

import junit.framework.TestCase;

import com.topcoder.registration.validation.DataValidationRegistrationValidator;
import com.topcoder.registration.validation.RegistrationValidationConfigurationException;
import com.topcoder.registration.validation.ValidationInfo;
import com.topcoder.registration.validation.ValidationProcessingException;
import com.topcoder.registration.validation.validators.simple.MemberNotTeamMemberForProjectValidator;
import com.topcoder.util.datavalidator.BundleInfo;


/**
 * Failure Tests for MemberNotTeamMemberForProjectValidator class.
 *
 * @author slion
 * @version 1.0
 */
public class MemberNotTeamMemberForProjectValidatorFailureTests extends TestCase {
    /**
     * Represents the invalid configuration file path.
     */
    private static final String INVALID_CONFIG1 = "test_files/failure/MNTMFPV_invalidconfig1.xml";
    
    /**
     * Represents the MemberNotTeamMemberForProjectValidator instance.
     */
    private MemberNotTeamMemberForProjectValidator validator = null;
    
    /**
     * Represents the BundleInfo instance for testing.
     */
    private BundleInfo bundle = null;

    /**
     * Setup the test environment.
     *
     * @throws Exception to JUnit.
     */
    protected void setUp() throws Exception {
        bundle = new BundleInfo();
        bundle.setBundle("sun.security.util.Resources_zh_CN");
        bundle.setDefaultMessage("some message.");
        bundle.setMessageKey("key");
        validator = new MemberNotTeamMemberForProjectValidator(bundle);
        TestHelper.clearConfiguration();
        TestHelper.loadConfiguration("test_files/failure/validconfig.xml");
        validator.setRegistrationValidator(new DataValidationRegistrationValidator());
    }

    /**
     * Teardown the test environment.
     *
     * @throws Exception to JUnit.
     */
    protected void tearDown() throws Exception {
        validator = null;
        bundle = null;
        TestHelper.clearConfiguration();
    }

    /**
     * Tests MemberNotTeamMemberForProjectValidator(BundleInfo bundleInfo) method with null BundleInfo bundleInfo,
     * IllegalArgumentException should be thrown.
     */
    public void testMemberNotTeamMemberForProjectValidator_NullBundleInfo() {
        try {
            new MemberNotTeamMemberForProjectValidator((BundleInfo) null);
            fail("testMemberNotTeamMemberForProjectValidator_NullBundleInfo is failure.");
        } catch (IllegalArgumentException iae) {
            // pass
        } catch (Exception e) {
            fail("Unknown exception occurs in testMemberNotTeamMemberForProjectValidator_NullBundleInfo.");
        }
    }

    /**
     * Tests MemberNotTeamMemberForProjectValidator(BundleInfo bundleInfo) method with empty BundleInfo bundleInfo,
     * IllegalArgumentException should be thrown.
     */
    public void testMemberNotTeamMemberForProjectValidator_EmptyBundleInfo() {
        try {
            new MemberNotTeamMemberForProjectValidator(new BundleInfo());
            fail("testMemberNotTeamMemberForProjectValidator_EmptyBundleInfo is failure.");
        } catch (IllegalArgumentException iae) {
            // pass
        } catch (Exception e) {
            fail("Unknown exception occurs in testMemberNotTeamMemberForProjectValidator_EmptyBundleInfo.");
        }
    }

    /**
     * Tests MemberNotTeamMemberForProjectValidator(String namespace) method with null String
     * namespace, IllegalArgumentException should be thrown.
     */
    public void testMemberNotTeamMemberForProjectValidator_NullNamespace() {
        try {
            new MemberNotTeamMemberForProjectValidator((String) null);
            fail("testMemberNotTeamMemberForProjectValidator_NullNamespace is failure.");
        } catch (IllegalArgumentException iae) {
            // pass
        } catch (Exception e) {
            fail("Unknown exception occurs in testMemberNotTeamMemberForProjectValidator_NullNamespace.");
        }
    }

    /**
     * Tests MemberNotTeamMemberForProjectValidator(String namespace) method with empty String
     * namespace, IllegalArgumentException should be thrown.
     */
    public void testMemberNotTeamMemberForProjectValidator_EmptyNamespace() {
        try {
            new MemberNotTeamMemberForProjectValidator("  ");
            fail("testMemberNotTeamMemberForProjectValidator_EmptyNamespace is failure.");
        } catch (IllegalArgumentException iae) {
            // pass
        } catch (Exception e) {
            fail("Unknown exception occurs in testMemberNotTeamMemberForProjectValidator_EmptyNamespace.");
        }
    }

    /**
     * Tests MemberNotTeamMemberForProjectValidator(String namespace) method with unknown namespace,
     * RegistrationValidationConfigurationException should be thrown.
     */
    public void testMemberNotTeamMemberForProjectValidator_UnknownNamespace() {
        try {
            new MemberNotTeamMemberForProjectValidator("unknown");
            fail("testMemberNotTeamMemberForProjectValidator_UnknownNamespace is failure.");
        } catch (RegistrationValidationConfigurationException rvce) {
            // pass
        } catch (Exception e) {
            fail("Unknown exception occurs in testMemberNotTeamMemberForProjectValidator_UnknownNamespace.");
        }
    }

    /**
     * Tests MemberNotTeamMemberForProjectValidator(String namespace) method with invalid configuration,
     * RegistrationValidationConfigurationException should be thrown.
     */
    public void testMemberNotTeamMemberForProjectValidator_InvalidConfig1() {
        try {
            TestHelper.loadConfiguration(INVALID_CONFIG1);
            new MemberNotTeamMemberForProjectValidator("MemberNotTeamMemberForProjectValidator");
            fail("testMemberNotTeamMemberForProjectValidator_InvalidConfig1 is failure.");
        } catch (RegistrationValidationConfigurationException rvce) {
            // pass
        } catch (Exception e) {
            fail("Unknown exception occurs in testMemberNotTeamMemberForProjectValidator_InvalidConfig1.");
        }
    }

    /**
     * Tests getMessage(Object obj) method with null Object obj, IllegalArgumentException should be thrown.
     */
    public void testGetMessage_NullObj() {
        try {
            validator.getMessage(null);
            fail("testGetMessage_NullObj is failure.");
        } catch (IllegalArgumentException iae) {
            // pass
        } catch (Exception e) {
            fail("Unknown exception occurs in testGetMessage_NullObj.");
        }
    }

    /**
     * Tests getMessage(Object obj) method with invalid Object obj, IllegalArgumentException should be thrown.
     */
    public void testGetMessage_InvalidObj() {
        try {
            validator.getMessage(new Byte("1"));
            fail("testGetMessage_InvalidObj is failure.");
        } catch (IllegalArgumentException iae) {
            // pass
        } catch (Exception e) {
            fail("Unknown exception occurs in testGetMessage_InvalidObj.");
        }
    }
    
//    /**
//     * Tests getMessage(Object obj) method with ValidationProcessingException thrown,
//     * 
//     */
//    public void testGetMessage_VPE() {
//        try {
//            validator.getMessage(new ValidationInfo());
//            fail("testGetMessage_VPE is failure.");
//        } catch (ValidationProcessingException vpe) {
//            // pass
//        } catch (Exception e) {
//            fail("Unknown exception occurs in testGetMessage_VPE.");
//        }
//    }
}
