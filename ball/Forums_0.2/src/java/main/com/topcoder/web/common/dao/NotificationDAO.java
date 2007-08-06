package com.topcoder.web.common.dao;

import com.topcoder.web.common.model.Notification;

import java.util.List;
import java.util.Set;

/**
 * @author dok
 * @version $Revision: 1.3 $ Date: 2005/01/01 00:00:00
 *          Create Date: May 11, 2006
 */
public interface NotificationDAO {
    List getNotifications();

    Notification find(Integer id);

    List getNotifications(Set regTypes);

}
