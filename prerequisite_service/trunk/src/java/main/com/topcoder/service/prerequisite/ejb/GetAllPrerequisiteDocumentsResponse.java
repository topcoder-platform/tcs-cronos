/*
 * Copyright (C) 2008 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.service.prerequisite.ejb;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.topcoder.service.prerequisite.PrerequisiteDocument;

/**
 * <p>
 * This class represents the "getAllPrerequisiteDocumentsResponse" element in WSDL. It is used to wrap the response of
 * the web services and it contains the result of getAllPrerequisiteDocuments operation.
 * </p>
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 * </p>
 *
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base=&quot;{http://www.w3.org/2001/XMLSchema}anyType&quot;&gt;
 *       &lt;sequence&gt;
 *         &lt;element name=&quot;prerequisiteDocuments&quot;
 *             type=&quot;{http://www.example.org/PrerequisiteService/}prerequisiteDocument&quot;
 *             maxOccurs=&quot;unbounded&quot; minOccurs=&quot;0&quot;/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 *
 * <p>
 * <b>Thread Safety</b>: This class is not thread safe because it's highly mutable.
 * </p>
 *
 * @author TCSDESIGNER, TCSDEVELOPER
 * @version 1.0
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {"prerequisiteDocuments"})
@XmlRootElement(name = "getAllPrerequisiteDocumentsResponse")
public class GetAllPrerequisiteDocumentsResponse implements Serializable {

    /**
     * Represents the serial version unique id.
     */
    private static final long serialVersionUID = -5762800040245385338L;
    /**
     * <p>
     * Represents the prerequisiteDocuments of the response.
     * </p>
     */
    private final List<PrerequisiteDocument> prerequisiteDocuments = new ArrayList<PrerequisiteDocument>();

    /**
     * <p>
     * This is the default constructor. It does nothing.
     * </p>
     */
    public GetAllPrerequisiteDocumentsResponse() {
    }

    /**
     * <p>
     * Gets the value of the prerequisiteDocuments property.
     * </p>
     * <p>
     * This accessor method returns a reference to the live list, not a snapshot. Therefore any modification you make to
     * the returned list will be present inside the JAXB object. This is why there is not a <CODE>set</CODE> method
     * for the prerequisiteDocuments property.
     * </p>
     * <p>
     * For example, to add a new item, do as follows:
     * </p>
     *
     * <pre>
     * getPrerequisiteDocuments().add(newItem);
     * </pre>
     *
     * @return the prerequisite documents list. never null, possibly empty.
     */
    public List<PrerequisiteDocument> getPrerequisiteDocuments() {
        return prerequisiteDocuments;
    }
}
