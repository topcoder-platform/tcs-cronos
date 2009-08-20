/*
 * Copyright (C) 2009 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.service.studio;

import com.topcoder.service.studio.ejb.StatusNotAllowedFault;
import com.topcoder.util.errorhandling.ExceptionData;

import javax.xml.ws.WebFault;

/**
 * <p>
 * This exception is thrown automatically by the web service interface when a
 * status is not allowed. It is related with "status_not_allowed_faultMsg" fault
 * in WSDL, but it contains only annotations about "status_not_allowed_fault"
 * because the messages are automatically constructed. It was generated by JBoss
 * tools and I changed the name, the inheritance and other changes.
 * </p>
 *
 * <p>
 * The constructors with StatusNotAllowedFault and the getter are necessary for
 * the correct webservices serialization/deserialization. The other constructors
 * are added for the future implementations of the service interface.
 * </p>
 *
 * @author fabrizyo, TCSDEVELOPER
 * @version 1.0
 */
@WebFault(name = "status_not_allowed_fault", faultBean = "com.topcoder.service.studio.ejb.StatusNotAllowedFault")
public class StatusNotAllowedException extends StudioServiceException {
    /**
     * <p>
     * Represents the faultInfo, Java type that goes as soapenv:Fault detail
     * element.
     * </p>
     */
    private final StatusNotAllowedFault faultInfo;

    /**
     * <p>
     * Represents the statusNotAllowed. I updated the WSDL, now it's an long. It
     * is retrieved from StatusNotAllowedFault.
     * </p>
     */
    private final long statusIdNotAllowed;

    /**
     * <p>
     * Construct the exception. Call the super constructor,set the fault info
     * and set the statusIdNotAllowed from the faultInfo.
     * </p>
     *
     * @param message the message
     * @param faultInfo the fault info which contains the statusIdNotAllowed
     */
    public StatusNotAllowedException(String message, StatusNotAllowedFault faultInfo) {
        super(message);
        this.faultInfo = faultInfo;
        this.statusIdNotAllowed = (faultInfo == null) ? -1 : faultInfo.getStatusIdNotAllowed();
    }

    /**
     * <p>
     * Construct the exception. Call the super constructor,set the fault info
     * and set the statusIdNotAllowed from the faultInfo.
     * </p>
     *
     * @param message the message
     * @param faultInfo the fault info which contains the statusIdNotAllowed
     * @param cause the cause
     */
    public StatusNotAllowedException(String message, StatusNotAllowedFault faultInfo, Throwable cause) {
        super(message, cause);
        this.faultInfo = faultInfo;
        this.statusIdNotAllowed = (faultInfo == null) ? -1 : faultInfo.getStatusIdNotAllowed();
    }

    /**
     * <p>
     * Constructor with error message.
     * </p>
     *
     * @param message the error message
     * @param statusIdNotAllowed the status's id not allowed
     */
    public StatusNotAllowedException(String message, long statusIdNotAllowed) {
        super(message);
        this.faultInfo = null;
        this.statusIdNotAllowed = statusIdNotAllowed;
    }

    /**
     * <p>
     * Constructor with error message and inner cause.
     * </p>
     *
     * @param message the error message
     * @param cause the cause of this exception
     * @param statusIdNotAllowed the status's id not allowed
     */
    public StatusNotAllowedException(String message, Throwable cause, long statusIdNotAllowed) {
        super(message, cause);
        this.faultInfo = null;
        this.statusIdNotAllowed = statusIdNotAllowed;
    }

    /**
     * <p>
     * Constructor with error message and exception data.
     * </p>
     *
     * @param message the error message
     * @param data the exception data
     * @param statusIdNotAllowed the status's id not allowed
     */
    public StatusNotAllowedException(String message, ExceptionData data, long statusIdNotAllowed) {
        super(message, data);
        this.faultInfo = null;
        this.statusIdNotAllowed = statusIdNotAllowed;
    }

    /**
     * <p>
     * Constructor with error message and inner cause and exception data.
     * </p>
     *
     * @param message the error message
     * @param cause the cause of this exception
     * @param data the exception data
     * @param statusIdNotAllowed the status's id not allowed
     */
    public StatusNotAllowedException(String message, Throwable cause, ExceptionData data,
            long statusIdNotAllowed) {
        super(message, cause, data);
        this.faultInfo = null;
        this.statusIdNotAllowed = statusIdNotAllowed;
    }

    /**
     * <p>
     * Return the statusId NotAllowed.
     * </p>
     *
     * @return the statusId NotAllowed
     */
    public long getStatusIdNotAllowed() {
        return statusIdNotAllowed;
    }

    /**
     * <p>
     * Return the fault bean. This method is necessary for the
     * serialization/deserialization. it returns null if the constructors
     * without fault bean are used.
     * </p>
     *
     * @return returns fault bean
     */
    public StatusNotAllowedFault getFaultInfo() {
        return faultInfo;
    }
}