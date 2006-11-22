/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */

package com.orpheus.game.failuretests;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;

import junit.framework.TestCase;

import org.easymock.MockControl;
import org.w3c.dom.Element;

import com.orpheus.game.RegisterGameHandler;
import com.topcoder.web.frontcontroller.ActionContext;
import com.topcoder.web.frontcontroller.HandlerExecutionException;

/**
 * Test case for <code>RegisterGameHandler</code>.
 *
 * @author assistant
 * @version 1.0
 */
public class RegisterGameHandlerTest extends TestCase {

    /**
     * Represents the handler to test.
     */
    private RegisterGameHandler handler;

    /**
     * Set up the environment.
     */
    protected void setUp() throws Exception {
        super.setUp();
        handler = new RegisterGameHandler("test");
        FailureTestHelper.loadConfig();
    }

    /**
     * Clean the envrionment.
     */
    protected void tearDown() throws Exception {
        super.tearDown();
        FailureTestHelper.unloadConfig();
    }

    /**
     * Test method for RegisterGameHandler(java.lang.String).
     * In this case, the name is null.
     */
    public void testRegisterGameHandler_String_NullName() {
        try {
            new RegisterGameHandler((String) null);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // should land here
        }
    }

    /**
     * Test method for RegisterGameHandler(java.lang.String).
     * In this case, the name is empty.
     */
    public void testRegisterGameHandler_String_EmptyName() {
        try {
            new RegisterGameHandler(" ");
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // should land here
        }
    }

    /**
     * Test method for RegisterGameHandler(org.w3c.dom.Element).
     * In this case, the element is null.
     */
    public void testRegisterGameHandler_Element_NullElement() {
        try {
            new RegisterGameHandler((Element) null);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // should land here
        }
    }

    /**
     * Test method for RegisterGameHandler(org.w3c.dom.Element).
     * In this case, the element has no param key.
     * @throws Exception to JUnit
     */
    public void testRegisterGameHandler_Element_NoParamKey() throws Exception, ParserConfigurationException, Exception {
        try {
            String xml = "<handler type=\"x\"> </handler>";
            new RegisterGameHandler(FailureTestHelper.parseElement(xml));
            fail("IllegalArgumentException expected.");
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
            MockControl reqControl = MockControl.createNiceControl(HttpServletRequest.class);
            MockControl resControl = MockControl.createControl(HttpServletResponse.class);
            reqControl.replay();
            ActionContext ac = new ActionContext(
                    (HttpServletRequest) reqControl.getMock(),
                    (HttpServletResponse) resControl.getMock());
            handler.execute(ac);
            fail("HandlerExecutionException expected.");
        } catch (HandlerExecutionException e) {
            // should land here
        }
    }

}
