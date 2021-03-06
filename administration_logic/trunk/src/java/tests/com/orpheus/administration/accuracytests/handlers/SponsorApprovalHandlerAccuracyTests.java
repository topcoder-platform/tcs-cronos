/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.orpheus.administration.accuracytests.handlers;

import junit.framework.TestCase;

import org.w3c.dom.Element;

import com.orpheus.administration.accuracytests.Helper;
import com.orpheus.administration.handlers.SponsorApprovalHandler;
import com.topcoder.web.frontcontroller.ActionContext;

/**
 * <p>
 * Accuracy test the <code>SponsorApprovalRejectionHandler</code> class. Note this
 * class is inherited from SponsorApprovalRejectionHandler, so these tests also test
 * the base class.
 * </p>
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
public class SponsorApprovalHandlerAccuracyTests extends TestCase {
    /**
     * The HttpServletRequest instance used in tests.
     */
    private MockHttpRequest request = new MockHttpRequest();

    /**
     * The HttpServletResponse instance used in tests.
     */
    private MockHttpResponse response = new MockHttpResponse();

    /**
     * The ActionContext used in tests.
     */
    private ActionContext context;

    /**
     * An instance of <code>SponsorApprovalHandler</code> which is tested.
     */
    private SponsorApprovalHandler target = null;

    /**
     * <p>
     * setUp() routine. Creates test SponsorApprovalHandler instance and other
     * instances used in tests.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    protected void setUp() throws Exception {
        Helper.prepareTest();

        context = new ActionContext(request, response);
        // the xml string used for test
        String xml = "<handler type=\"pendingSponsorDomain\">"
            + "<object-factory-ns>objFactoryNS</object-factory-ns>"
            + "<game-data-jndi-name>"
            + "GameDataHome"
            + "</game-data-jndi-name>"
            + "<sponsor-id-request-param>sponsorId</sponsor-id-request-param>"
            + "<fail-result>Failed</fail-result><fail-request-attribute>"
            + "fail</fail-request-attribute></handler>";
        Element element = Helper.loadXmlString(xml);
        target = new SponsorApprovalHandler(element);
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
        Helper.clearTestEnvironment();
    }

    /**
     * <p>
     * Accuracy test. Tests the
     * <code>SponsorApprovalHandler(Element)</code> Create for proper
     * behavior. Verify that all fields are set correct.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void testConstructorAccuracy() throws Exception {
        assertNotNull("Failed to initialize SponsorApprovalHandler.", target);
    }

    /**
     * <p>
     * Accuracy test. Tests the <code>execute(ActionContext)</code> for
     * proper behavior. Verifies if the return value is correct.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void testExecuteAccuracy() throws Exception {
        request.setParameter("sponsorId", "1001");
        assertEquals("null should be returned if success.", null, target
                .execute(context));
    }
}
