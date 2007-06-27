/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.timetracker.notification.ejb;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;

/**
 * <p>
 * LocalHome interface for the NotificationPersistence.  It contains only a single no-param create method that produces
 * an instance of the local interface.  it is used to obtain a handle to the Stateless SessionBean.
 * </p>
 *
 * <p>
 * Thread Safety: Implementation will be generated by EJB container and thread-safety is dependent on the container.
 * </p>
 *
 * @author ShindouHikaru, kzhu
 * @version 1.0
 */
public interface NotificationPersistenceLocalHome extends EJBLocalHome {

    /**
     * <p>
     * Creates an instance of NotificationPersistenceLocal, which is a handle to the SessionBean.
     * </p>
     *
     * @return an instance of NotificationPersistenceLocal.
     *
     * @throws CreateException if a problem occurs while creating the object.
     */
    public NotificationPersistenceLocal create() throws CreateException;
}
