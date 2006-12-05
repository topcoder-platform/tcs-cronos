/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.orpheus.administration.stresstests;

import com.mockrunner.mock.web.MockHttpServletRequest;
import com.orpheus.administration.handlers.PendingSponsorDomainHandler;
import com.orpheus.game.persistence.Domain;
import com.topcoder.user.profile.UserProfile;
import com.topcoder.web.frontcontroller.Handler;

/**
 * <p>
 * Stress test for <code>{@link com.orpheus.administration.handlers.PendingSponsorDomainHandler}</code> class.
 * </p>
 * @author FireIce
 * @version 1.0
 */
public class PendingSponsorDomainHandlerStressTest extends AbstractHandlerStressTest {

    protected void setUp() throws Exception {
        super.setUp();

        ((MockHttpServletRequest) request).setupAddParameter("DomainId", "1");
    }

    protected void assertAfterExecute() {
        super.assertAfterExecute();

        assertNull("should return null", result);
        assertTrue("attribute not found.", request.getAttribute("Domain") instanceof Domain);
        assertTrue("attribute not found.", request.getAttribute("Sponsor") instanceof UserProfile);
    }

    /**
     * <p>
     * Get the handler instance to be tested.
     * </p>
     * @return the handler to be test.
     * @throws Exception
     *             If fail to get.
     */
    protected Handler getHandler() throws Exception {
        // the xml string used for test
        String xml = "<handler type=\"pendingSponsorDomain\">" + "<object-factory-ns>objFactoryNS</object-factory-ns>"
            + "<game-data-jndi-name>" + StressTestHelper.GAME_DATA_JNDI_NAME + "</game-data-jndi-name>"
            + "<domain-id-request-param>DomainId</domain-id-request-param>"
            + "<domain-request-attribute>Domain</domain-request-attribute>"
            + "<sponsor-request-attribute>Sponsor</sponsor-request-attribute>"
            + "<fail-result>Failed</fail-result><fail-request-attribute>" + "fail</fail-request-attribute></handler>";
        return new PendingSponsorDomainHandler(StressTestHelper.loadXmlString(xml));
    }

    /**
     * <p>
     * Get the the handler class name to be tested.
     * </p>
     * @return the handler class name
     */
    protected String getHandlerName() {
        return "PendingSponsorDomainHandler";
    }

}
