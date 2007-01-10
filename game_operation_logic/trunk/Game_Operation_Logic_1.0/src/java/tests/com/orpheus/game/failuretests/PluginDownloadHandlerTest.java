/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */

package com.orpheus.game.failuretests;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.w3c.dom.Element;

import servlet.MockHttpRequest;
import servlet.MockHttpResponse;
import servlet.MockHttpSession;
import servlet.MockServletContext;

import com.orpheus.game.GameOperationLogicUtility;
import com.orpheus.game.PluginDownloadHandler;
import com.topcoder.util.rssgenerator.MockDataStore;
import com.topcoder.web.frontcontroller.ActionContext;
import com.topcoder.web.frontcontroller.HandlerExecutionException;

import junit.framework.TestCase;

/**
 * Test case for <code>PluginDownloadHandlerTest</code>.
 *
 * @author assistant
 * @version 1.0
 */
public class PluginDownloadHandlerTest extends TestCase {

    private PluginDownloadHandler handler;

    /**
     * Set up the environment.
     */
    protected void setUp() throws Exception {
        super.setUp();
        handler = new PluginDownloadHandler("test");
    }

    /**
     * Clean up the environment.
     */
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Test method for PluginDownloadHandler(java.lang.String).
     * In this case, the name is null.
     */
    public void testPluginDownloadHandler_String_NullKey() {
        try {
            new PluginDownloadHandler((String) null);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // should land here
        }
    }

    /**
     * Test method for PluginDownloadHandler(java.lang.String).
     * In this case, the name is empty.
     */
    public void testPluginDownloadHandler_String_EmptyKey() {
        try {
            new PluginDownloadHandler(" ");
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // should land here
        }
    }

    /**
     * Test method for {@link com.orpheus.game.PluginDownloadHandler#PluginDownloadHandler(org.w3c.dom.Element)}.
     * In this caes, the element is null.
     */
    public void testPluginDownloadHandler_Element_NullElement() {
        try {
            new PluginDownloadHandler((Element) null);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // should land here
        }
    }

    /**
     * Test method for execute(ActionContext).
     * In this case, the context is null.
     * Expected : {@link IllegalArgumentException}.
     * @throws Exception to JUnit
     */
    public void testExecute_NullContext() throws Exception {
        try {
            handler.execute(null);
            fail("IllegalARgumentException expected.");
        } catch (IllegalArgumentException e) {
            // should land here
        }
    }

    /**
     * Test method for execute(ActionContext).
     * In this case, the context is null.
     * Expected : {@link HandlerExecutionException}.
     * @throws Exception to JUnit
     */
    public void testExecute_FailedToLoad() throws Exception {
        try {
        	ServletContext servletContext = new MockServletContext();
    		servletContext.setAttribute(GameOperationLogicUtility.getInstance()
    				.getDataStoreKey(), new MockDataStore());

    		HttpSession session = new MockHttpSession(servletContext);
    		MockHttpRequest mockRequest = new MockHttpRequest(session);
    		HttpServletRequest request = mockRequest;

    		HttpServletResponse response = new MockHttpResponse();
            ActionContext ac = new ActionContext(request, response);
            handler.execute(ac);
            fail("HandlerExecutionException expected.");
        } catch (HandlerExecutionException e) {
            // should land here
        }
    }


}
