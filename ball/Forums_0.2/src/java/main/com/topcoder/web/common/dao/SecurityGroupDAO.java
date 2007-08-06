package com.topcoder.web.common.dao;


import com.topcoder.web.common.model.User;

import java.util.List;
import java.util.Set;

/**
 * @author dok
 * @version $Revision: 1.3 $ Date: 2005/01/01 00:00:00
 *          Create Date: May 19, 2006
 */
public interface SecurityGroupDAO {
    List getSecurityGroups(Set registrationTypes);

    public boolean hasInactiveHSGroup(User u);
}
