/*
 * Copyright (C) 2008 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.service.studio.ejb;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;

/**
 * <p> This class represents the "persistence_fault" element in WSDL. It's contained in the related exception-message
 * class. It was generated by JBoss tools and I changed the name and other little changes.</p>
 *
 * <p> This class is not thread safe because it's highly mutable</p>
 *
 * @author fabrizyo, TCSDEVELOPER
 * @version 1.0
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {"persistenceMessage"})
@XmlRootElement(name = "persistence_fault")
public class PersistenceFault implements Serializable {
    /**
     * <p> Represents the persistence message</p>
     */
    @XmlElement(name = "persistence_message", required = true)
    private String persistenceMessage;

    /**
     * <p> This is the default constructor. It does nothing.</p>
     */
    public PersistenceFault() {
    }

    /**
     * <p> Gets the value of the persistenceMessage property.</p>
     *
     * @return the value of the persistenceMessage property.
     */
    public String getPersistenceMessage() {
        return persistenceMessage;
    }

    /**
     * <p> Sets the value of the persistenceMessage property.</p>
     *
     * @param persistenceMessage the value of the persistenceMessage property to set, can be null, can be empty
     */
    public void setPersistenceMessage(String persistenceMessage) {
        this.persistenceMessage = persistenceMessage;
    }
}
