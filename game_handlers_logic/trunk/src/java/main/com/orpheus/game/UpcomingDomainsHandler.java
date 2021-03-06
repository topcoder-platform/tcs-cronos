/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.orpheus.game;

import org.w3c.dom.Element;

import com.orpheus.game.persistence.Domain;
import com.topcoder.web.frontcontroller.ActionContext;
import com.topcoder.web.frontcontroller.Handler;
import com.topcoder.web.frontcontroller.HandlerExecutionException;


/**
 * <p>
 * <code>UpcomingDomainsHandler</code> class implements the Handler interface from the <code>FrontController</code>
 * component. It will load a game id value from the request parameter, and then use it to find the domains from the
 * current and next hosting blocks of the current game that have not yet been unlocked; it will assign an array
 * containing their corresponding <code>Domain</code> objects to a request attribute of configurable name.
 *
 * To follow the <code>Handler</code> implementation's contract in order to make it work properly with
 * <code>FrontController</code> component, two constructors are defined, one takes the configurable parameters, and
 * the other takes an <code>Element</code> object which contains all configurable parameters.
 *
 * Thread-safety: This class is immutable and thread-safe.
 * </p>
 *
 * @author Standlove, mittu
 * @version 1.0
 */
public class UpcomingDomainsHandler implements Handler {

    /**
     * <p>
     * Represents the key used to get the game id from the request parameter in execute method. It is initialized in
     * constructor and never changed afterwards. It must be non-null, non-empty string.
     * </p>
     */
    private final String gameIdParamKey;

    /**
     * <p>
     * Represents the key used to store an array of Domain objects into request's attribute Map in execute method. It is
     * initialized in constructor and never changed afterwards. It must be non-null, non-empty string.
     * </p>
     */
    private final String domainsKey;

    /**
     * <p>
     * Constructor with configurable arguments. Simply assign the arguments to corresponding variables.
     * </p>
     *
     * @param gameIdParamKey
     *            the key used to get the game id from the request parameter in execute method.
     * @param domainsKey
     *            the key used to store an array of Domain objects into request's attribute Map in execute method.
     * @throws IllegalArgumentException
     *             if any argument is null or empty string.
     */
    public UpcomingDomainsHandler(String gameIdParamKey, String domainsKey) {
        ImplementationHelper.checkStringValid(gameIdParamKey, "gameIdParamKey");
        ImplementationHelper.checkStringValid(domainsKey, "domainsKey");
        this.gameIdParamKey = gameIdParamKey;
        this.domainsKey = domainsKey;
    }

    /**
     * <p>
     * Constructor with an xml Element object. The constructor will extract necessary configurable values from this xml
     * element.
     *
     * Here is what the xml element likes:
     *
     * <pre>
     *  &lt;handler type=&quot;x&quot;&gt;
     *   &lt;game_id_param_key&gt;gameId&lt;/game_id_param_key&gt;
     *   &lt;domains_key&gt;domains&lt;/domains_key&gt;
     *  &lt;/handler&gt;
     * </pre>
     *
     * </p>
     *
     * @param element
     *            the xml Element to extract the configurable values.
     * @throws IllegalArgumentException
     *             if the argument is null or any value mentioned in implementation note is missing or empty string.
     */
    public UpcomingDomainsHandler(Element element) {
        ImplementationHelper.checkObjectValid(element, "element");
        gameIdParamKey = ImplementationHelper.getElement(element, "game_id_param_key");
        domainsKey = ImplementationHelper.getElement(element, "domains_key");
    }

    /**
     * <p>
     * It will load a game id value from the request parameter, and then use it to find the domains from the current and
     * next hosting blocks of the current game that have not yet been unlocked; it will assign an array containing their
     * corresponding <code>Domain</code> objects to a request attribute of configurable name.
     *
     * This method will return null always if the upcoming domains are loaded successfully (it's ok if loaded domains
     * array is of zero length). Otherwise <code>HandlerExecutionException</code> is thrown to wrap any underlying
     * exceptions.
     * </p>
     *
     * @param context
     *            the ActionContext object.
     * @return <code>null</code> always.
     * @throws IllegalArgumentException
     *             if the given argument is null.
     * @throws HandlerExecutionException
     *             if fail to load upcoming domains, or the game-id parameter is missing or invalid.
     */
    public String execute(ActionContext context) throws HandlerExecutionException {
        ImplementationHelper.checkObjectValid(context, "context");
        GameDataHelper helper = GameDataHelper.getInstance();
        try {
            // parses the game id from the request parameter.
            long gameId = Long.parseLong(context.getRequest().getParameter(gameIdParamKey));
            // gets the upcoming domains using helper.
            Domain[] domains = helper.getUpcomingDomains(gameId);
            // saves the domains to the request attribute.
            context.getRequest().setAttribute(domainsKey, domains);
        } catch (NumberFormatException formatException) {
            throw new HandlerExecutionException("Failed to execute unlocked domains handler.", formatException);
        }
        return null;
    }
}
