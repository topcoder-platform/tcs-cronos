/*
 * Copyright (C) 2008 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.service.studio;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

/**
 * It is the DTO class which is used to transfer contest payment data. The
 * information can be null or can be empty, therefore this check is not present
 * in the setters. It's the related to the equivalent ContestPayment entity.
 * </p>
 * 
 * <p>
 * This class is not thread safe because it's highly mutable
 * </p>
 * 
 * @author TCSDEVELOPER
 * @version 1.0
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "mediumData", propOrder = { "mediumId",
        "mediumDesc" })
public class MediumData implements Serializable {
    /**
     * Represents medium id.
     */
    private Long mediumId;

    /**
     * @return the mediumId
     */
    public Long getMediumId() {
        return mediumId;
    }

    /**
     * @param mediumId the mediumId to set
     */
    public void setMediumId(Long mediumId) {
        this.mediumId = mediumId;
    }

    /**
     * Represents description.
     */
    private String description;

    /**
     * Returns description.
     * 
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets description.
     * 
     * @param description
     *            the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }
}
