/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.timetracker.user.j2ee;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;

/**
 * <p>
 * LocalHome interface for the <code>UserStatusManager</code>. It contains only a single no-param create method
 * that produces an instance of the local interface. it is used to obtain a handle to the Stateless SessionBean.
 * </p>
 * <p>
 * Thread Safety: Implementation will be generated by EJB container and thread-safety is dependent on the
 * container.
 * </p>
 *
 * @author George1, enefem21
 * @version 3.2.1
 * @since 3.2.1
 */
public interface UserStatusManagerLocalHome extends EJBLocalHome {

    /**
     * <p>
     * Creates an instance of <code>UserStatusManagerLocal</code>, which is a handle to the SessionBean.
     * </p>
     *
     * @return an instance of UserStatusManagerLocal.
     * @throws CreateException
     *             if there is any exception when creating the local object
     */
    public UserStatusManagerLocal create() throws CreateException;
}
