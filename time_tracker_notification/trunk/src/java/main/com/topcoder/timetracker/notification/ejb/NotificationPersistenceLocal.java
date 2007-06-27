/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.timetracker.notification.ejb;

import com.topcoder.timetracker.notification.NotificationPersistence;

import javax.ejb.EJBLocalObject;

/**
 * <p>
 * Local interface for NotificationPersistence.  It contains exactly the same methods as NotificationPersistence
 * interface.
 * </p>
 *
 * <p>
 * <b>Thread Safety:</b> Implementation will be generated by EJB container and thread-safety is dependent on the
 * container.
 * </p>
 *
 * @author ShindouHikaru, kzhu
 * @version 1.0
 */
public interface NotificationPersistenceLocal extends NotificationPersistence, EJBLocalObject {
}
