package com.topcoder.web.common.dao.hibernate;

import com.topcoder.web.common.dao.NotificationDAO;
import com.topcoder.web.common.model.Notification;
import com.topcoder.web.common.model.RegistrationType;
import org.hibernate.Query;
import org.hibernate.Session;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * @author dok
 * @version $Revision: 1.4 $ Date: 2005/01/01 00:00:00
 *          Create Date: Apr 10, 2006
 */
public class NotificationDAOHibernate extends Base implements NotificationDAO {

    public NotificationDAOHibernate() {
        super();
    }

    public NotificationDAOHibernate(Session session) {
        super(session);
    }

    public List getNotifications() {
        return findAll(Notification.class, "status", "A");
    }

    public Notification find(Integer id) {
        return (Notification) find(Notification.class, id);
    }

    public List getNotifications(Set regTypes) {
        List ret;
        if (regTypes.isEmpty()) {
            ret = Collections.EMPTY_LIST;
        } else {
            StringBuffer query = new StringBuffer(100);
            query.append("SELECT distinct n FROM Notification n WHERE n.status = 'A' AND n.registrationTypes.id in (");
            for (Iterator it = regTypes.iterator(); it.hasNext();) {
                query.append(((RegistrationType) it.next()).getId()).append(",");
            }
            query.delete(query.length() - 1, query.length());
            query.append(")");
            query.append(" order by n.sort");
            Query q = session.createQuery(query.toString());
            ret = q.list();
        }
        return ret;

    }


}
