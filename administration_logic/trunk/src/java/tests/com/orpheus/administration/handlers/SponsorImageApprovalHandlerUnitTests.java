/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.orpheus.administration.handlers;

import junit.framework.TestCase;

import org.w3c.dom.Element;

import com.orpheus.administration.TestHelper;

/**
 * <p>
 * Test the <code>SponsorImageApprovalHandler</code> class.
 * </p>
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
public class SponsorImageApprovalHandlerUnitTests extends TestCase {
    /**
     * <p>
     * An instance of <code>SponsorImageApprovalHandler</code> which is tested.
     * </p>
     */
    private SponsorImageApprovalHandler target = null;

    /**
     * <p>
     * setUp() routine. Creates test SponsorImageApprovalHandler instance and other
     * instances used in tests.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    protected void setUp() throws Exception {
        TestHelper.prepareTest();
        //      the xml string used for test
        String xml = SponsorImageApprovalRejectionHandlerUnitTests.XMLSTRING;
        Element element = TestHelper.loadXmlString(xml);
        target = new SponsorImageApprovalHandler(element);
    }

    /**
     * <p>
     * Clean up all namespace of ConfigManager.
     * </p>
     *
     * @throws Exception
     *             the exception to JUnit.
     */
    protected void tearDown() throws Exception {
        TestHelper.clearTestEnvironment();
    }

    /**
     * <p>
     * Accuracy test. Tests the
     * <code>SponsorImageApprovalHandler(Element)</code> Create for proper
     * behavior. Verify that all fields are set correct.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void testConstructor_1_accuracy() throws Exception {
        assertNotNull("Failed to initialize SponsorImageApprovalHandler.",
                target);
    }

    /**
     * <p>
     * Failure test. Tests the <code>SponsorImageApprovalHandler(Element)</code> for
     * proper behavior. Verify that IllegalArgumentException is thrown if any
     * argument is null.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void testConstructorElement_1_failure() throws Exception {
        try {
            new SponsorImageApprovalHandler(null);
            fail("IllegalArgumentException expected if the 1st argument is null.");
        } catch (IllegalArgumentException e) {
            // ok
        }
    }

    /**
     * <p>
     * Accuracy test. Tests the
     * <code>getApprovedFlag()</code> for proper
     * behavior. Verify that the returned value is correctly.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void testGetApprovedFlag_1_accuracy() throws Exception {
        assertEquals("true should be returned.", true, target.getApprovedFlag());
    }
}
