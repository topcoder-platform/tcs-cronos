/*
 * Copyright (C) 2008 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.clients.dao;

import java.util.List;

import com.topcoder.clients.model.Client;
import com.topcoder.clients.model.Project;

/**
 * <p>
 * This interface represents the ClientDAO business interface.
 * </p>
 * <p>
 * This interface defines the specific methods available for the ClientDAO
 * business interface: get the projects for the corresponding client.
 * </p>
 * <p>
 * See base interface for other available operations.
 * </p>
 * <p>
 * <strong>THREAD SAFETY:</strong> Implementations of this interface should be
 * thread safe.
 * </p>
 *
 * @author Mafy, TCSDEVELOPER
 * @version 1.0
 */
public interface ClientDAO extends GenericDAO<Client, Long> {
    /**
     * <p>
     * This static final String field represents the 'BEAN_NAME' property of the
     * ClientDAO business interface. Represents the EJB session bean name.
     * </p>
     * <p>
     * It is initialized to a default value: "ClientDAOBean" String during
     * runtime.
     * </p>
     */
    public static final String BEAN_NAME = "ClientDAOBean";

    /**
     * <p>
     * Defines the operation that performs the retrieval of the list with
     * projects with the given client from the persistence. If nothing is found,
     * return an empty list.
     * </p>
     *
     * @param client
     *                the given clients to retrieve it's projects. Should not be
     *                null.
     * @return the list of Projects for the given client found in the
     *         persistence. If nothing is found, return an empty list.
     * @throws IllegalArgumentException
     *                 if client is null.
     * @throws EntityNotFoundException
     *                 if client is not found in the persistence.
     * @throws DAOException
     *                 if any error occurs while performing this operation.
     */
    public List<Project> getProjectsForClient(Client client)
        throws DAOException;
    



}
