/*
 * Copyright (C) 2008 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.clients.webservices;

import javax.ejb.Remote;

/**
 * <p>
 *  This interface represents the CompanyService remote interface of the session bean.
 *  Defines a static String variable containing the JNDI name of the remote interface.
 * </p>
 *
 * <p>
 *  <strong>Thread-safety:</strong>
 *  Implementations of this interface should be thread safe.
 * </p>
 *
 * @author  Mafy, CaDenza
 * @version 1.0
 */
@Remote
public interface CompanyServiceRemote extends CompanyService {

    /**
     *  This static final String field represents the 'JNDI_NAME' identifier of
     *  the CompanyServiceRemote interface.
     *  Represents the JNDI name of this remote interface.
     */
    public static final String JNDI_NAME = CompanyService.BEAN_NAME + "/remote";
}

