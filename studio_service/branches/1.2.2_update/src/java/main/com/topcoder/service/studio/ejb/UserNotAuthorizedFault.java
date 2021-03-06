/*
 * Copyright (C) 2008 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.service.studio.ejb;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p> This class represents the "user_not_authorized_fault" element in WSDL. It's contained in the related
 * exception-message class. It was generated by JBoss tools and I changed the name and other little changes.</p>
 *
 * <p> This class is not thread safe because it's highly mutable</p>
 *
 * @author fabrizyo, TCSDEVELOPER
 * @version 1.0
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {"userIdNotAuthorized"})
@XmlRootElement(name = "user_not_authorized_fault")
public class UserNotAuthorizedFault {
    /**
     * <p> Represents the user id Not Authorized.</p>
     */
    @XmlElement(name = "user_not_authorized", required = true)
    private long userIdNotAuthorized;

    /**
     * <p> This is the default constructor. It does nothing.</p>
     */
    public UserNotAuthorizedFault() {
    }

    /**
     * <p> Gets the value of the userIdNotAuthorized property.</p>
     *
     * @return the value of the userIdNotAuthorized property.
     */
    public long getUserIdNotAuthorized() {
        return userIdNotAuthorized;
    }

    /**
     * <p> Sets the value of the userIdNotAuthorized property.</p>
     *
     * @param userIdNotAuthorized the value of the userIdNotAuthorized property to set
     */
    public void setUserIdNotAuthorized(long userIdNotAuthorized) {
        this.userIdNotAuthorized = userIdNotAuthorized;
    }
}

