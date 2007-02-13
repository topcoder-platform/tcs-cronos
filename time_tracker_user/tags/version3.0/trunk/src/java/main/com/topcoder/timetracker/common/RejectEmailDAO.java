/*
 * Copyright (c) 2006, TopCoder, Inc. All rights reserved.
 */
package com.topcoder.timetracker.common;

import com.topcoder.search.builder.filter.Filter;

/**
 * <p>
 * This interface defines the necessary methods that a RejectEmail DAO should support. Create, Retrieve, Update,
 * Delete and Enumerate (CRUDE) methods are provided. There is also a search method that utilizes Filter classes
 * from the Search Builder 1.2 component.
 * </p>
 * <p>
 * Thread Safety: - Implementations need not necessarily be thread safe. Each implementation should specify whether
 * it is thread-safe or not. The application should pick the correct implementation for it's requirements.
 * </p>
 *
 * @author ShindouHikaru
 * @author kr00tki
 * @version 2.0
 */
public interface RejectEmailDAO {

    /**
     * <p>
     * This is a constant for a search filter field name for the Company Id which a RejectEmail belongs to.
     * </p>
     * <p>
     * Filters from the Search Builder component may use this constant when building their search parameters.
     * Implementations of the RejectEmailDAO interface should be able to recognize search filters bearing the
     * provided constant and adjust their searches according to the searchRejectEmails method.
     * </p>
     *
     */
    String SEARCH_COMPANY_ID = "search_company_id";

    /**
     * <p>
     * This is a constant for a search filter field name for the Description of the RejectEmail.
     * </p>
     * <p>
     * Filters from the Search Builder component may use this constant when building their search parameters.
     * Implementations of the RejectEmailDAO interface should be able to recognize search filters bearing the
     * provided constant and adjust their searches according to the searchRejectEmails method.
     * </p>
     *
     */
    String SEARCH_DESCRIPTION = "search_description";

    /**
     * <p>
     * This is a constant for a search filter field name for the Reject Email's Date of Creation.
     * </p>
     * <p>
     * Filters from the Search Builder component may use this constant when building their search parameters.
     * Implementations of the RejectEmailDAO interface should be able to recognize search filters bearing the
     * provided constant and adjust their searches according to the searchRejectEmails method.
     * </p>
     *
     */
    String SEARCH_CREATED_DATE = "search_created_date";

    /**
     * <p>
     * This is a constant for a search filter field name for the Reject Email's User Creator.
     * </p>
     * <p>
     * Filters from the Search Builder component may use this constant when building their search parameters.
     * Implementations of the RejectEmailDAO interface should be able to recognize search filters bearing the
     * provided constant and adjust their searches according to the searchRejectEmails method.
     * </p>
     *
     */
    String SEARCH_CREATED_USER = "search_created_user";

    /**
     * <p>
     * This is a constant for a search filter field name for the Reject Email's Last Date of Modification.
     * </p>
     * <p>
     * Filters from the Search Builder component may use this constant when building their search parameters.
     * Implementations of the RejectEmailDAO interface should be able to recognize search filters bearing the
     * provided constant and adjust their searches according to the searchRejectEmails method.
     * </p>
     *
     */
    String SEARCH_MODIFICATION_DATE = "search_modification_date";

    /**
     * <p>
     * This is a constant for a search filter field name for the Reject Email's Last User of Modification.
     * </p>
     * <p>
     * Filters from the Search Builder component may use this constant when building their search parameters.
     * Implementations of the RejectEmailDAO interface should be able to recognize search filters bearing the
     * provided constant and adjust their searches according to the searchRejectEmails method.
     * </p>
     *
     */
    String SEARCH_MODIFICATION_USER = "search_modification_user";

    /**
     * <p>
     * Creates a datastore entry for the given Reject Email. An id is automatically generated by the DAO and
     * assigned to the Email. The RejectEmail is also considered to have been created by the specified username.
     * </p>
     *
     *
     *
     * @return The same rejectEmail Object, with an assigned id, creationDate, modificationDate, creationUser and
     *         modificationUser assigned appropriately.
     * @param rejectEmail The rejectEmail to create within the datastore.
     * @param username The username of the user responsible for creating the RejectEmail entry within the
     *        datastore.
     * @throws IllegalArgumentException if the rejectEmail or username is null, or if username is an empty String.
     * @throws RejectEmailDAOException if a problem occurs while accessing the datastore.
     */
    RejectEmail createRejectEmail(RejectEmail rejectEmail, String username) throws RejectEmailDAOException;

    /**
     * <p>
     * Retrieves a RejectEmail from the datastore with the provided id. If no RejectEmail with that id exists, then
     * a null is returned.
     * </p>
     *
     *
     *
     * @return the Rejection email.
     * @param id The id of the RejectEmail to retrieve from the datastore.
     * @throws IllegalArgumentException if id is <=0
     * @throws RejectEmailDAOException if a problem occurs while accessing the datastore.
     */
    RejectEmail retrieveRejectEmail(long id) throws RejectEmailDAOException;

    /**
     * <p>
     * Updates the given RejectEmail in the data store. The RejectEmail is considered to have been modified by the
     * specified username.
     * </p>
     *
     *
     *
     * @param rejectEmail The RejectEmail entity to modify.
     * @param username The username of the user responsible for performing the update.
     * @throws IllegalArgumentException if the rejectEmail is null, or the username is null, or the username is an
     *         empty String.
     * @throws RejectEmailDAOException if a problem occurs while accessing the datastore.
     * @throws RejectEmailNotFoundException if the RejectEmail to update was not found in the data store.
     */
    void updateRejectEmail(RejectEmail rejectEmail, String username) throws RejectEmailDAOException,
            RejectEmailNotFoundException;

    /**
     * <p>
     * Removes the specified RejectEmail from the data store.
     * </p>
     *
     *
     *
     * @param rejectEmail The rejectEmail to delete.
     * @throws IllegalArgumentException if the rejectEmail is null.
     * @throws RejectEmailDAOException if a problem occurs while accessing the datastore.
     * @throws RejectEmailNotFoundException if the RejectEmail to delete was not found in the data store.
     */
    void deleteRejectEmail(RejectEmail rejectEmail) throws RejectEmailDAOException, RejectEmailNotFoundException;

    /**
     * <p>
     * Enumerates all the RejectEmails that are present within the data store.
     * </p>
     *
     *
     *
     * @return A list of all the RejectEmails within the data store.
     * @throws RejectEmailDAOException if a problem occurs while accessing the datastore.
     */
    RejectEmail[] listRejectEmails() throws RejectEmailDAOException;

    /**
     * <p>
     * Returns a list of all the RejectReasons within the datastore that satisfy the filters that are provided. The
     * filters are defined using classes from the Search Builder v1.2 component and com.topcoder.timetracker.
     * common.search package.
     * </p>
     *
     *
     *
     * @return A list of RejectReasons that satisfy the search criterion.
     * @param filter The filter that is used as criterion to facilitate the search..
     * @throws IllegalArgumentException if the filter is null.
     * @throws RejectEmailDAOException if a problem occurs while accessing the datastore.
     */
    RejectEmail[] searchRejectEmails(Filter filter) throws RejectEmailDAOException;
}
