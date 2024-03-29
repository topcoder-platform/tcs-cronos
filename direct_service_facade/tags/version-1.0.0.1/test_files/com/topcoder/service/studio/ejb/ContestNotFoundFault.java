/*
 * Copyright (C) 2009 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.service.studio.ejb;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * This class represents the "contest_not_found_fault" element in WSDL. It's
 * contained in the related exception-message class, it's the fault bean. It was
 * generated by JBoss tools and I changed the name and other little changes.
 * </p>
 *
 * <p>
 * This class is not thread safe because it's highly mutable
 * </p>
 *
 * @author fabrizyo, TCSDEVELOPER
 * @version 1.0
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "contestIdNotFound" })
@XmlRootElement(name = "contest_not_found_fault")
public class ContestNotFoundFault {
    /**
     * <p>
     * Represents the contest Not Found in the exception. I updated the WSDL,
     * now it's long.
     * </p>
     */
    @XmlElement(name = "contest_not_found", required = true)
    private long contestIdNotFound;

    /**
     * <p>
     * This is the default constructor. It does nothing.
     * </p>
     */
    public ContestNotFoundFault() {
    }

    /**
     * <p>
     * Gets the value of the contestIdNotFound property.
     * </p>
     *
     * @return the value of the contestIdNotFound property.
     */
    public long getContestIdNotFound() {
        return contestIdNotFound;
    }

    /**
     * <p>
     * Sets the value of the contestIdNotFound property.
     * </p>
     *
     * @param contestIdNotFound the value to set
     */
    public void setContestIdNotFound(long contestIdNotFound) {
        this.contestIdNotFound = contestIdNotFound;
    }
}
