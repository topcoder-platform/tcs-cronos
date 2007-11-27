/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.timetracker.user.j2ee;

import javax.ejb.SessionBean;
import javax.ejb.SessionContext;

import com.topcoder.search.builder.filter.Filter;
import com.topcoder.timetracker.user.ConfigurationException;
import com.topcoder.timetracker.user.DataAccessException;
import com.topcoder.timetracker.user.UserManagerFactory;
import com.topcoder.timetracker.user.UserStatus;
import com.topcoder.timetracker.user.UserStatusManager;

/**
 * <p>
 * This is a Stateless SessionBean that is used to provide business services to manage UserStatuses within the Time
 * Tracker Application. It contains the same methods as UserStatusManager, and delegates to an instance of
 * UserStatusManager.
 * </p>
 * <p>
 * The instance of <code>UserStatusManager</code> to use is retrieved from the <code>UserManagerFactory</code>.
 * </p>
 * <p>
 * Thread Safety: The UserStatusManager interface implementations are required to be thread-safe, and so this
 * stateless session bean is thread-safe also.
 * </p>
 *
 * @author George1, enefem21
 * @version 3.2.1
 * @since 3.2.1
 */
public class UserStatusManagerSessionBean implements UserStatusManager, SessionBean {

    /** Serial version UID. */
    private static final long serialVersionUID = -5002765819867164361L;

    /**
     * <p>
     * This is the instance of SessionContext that was provided by the EJB container. It is stored and made
     * available to subclasses. It is also used when performing a rollback in the case that an exception occurred.
     * </p>
     * <p>
     * Initial Value: Null
     * </p>
     * <p>
     * Accessed In: getSessionContext();
     * </p>
     * <p>
     * Modified In: setSessionContext
     * </p>
     * <p>
     * Utilized In: All business methods of this class
     * </p>
     * <p>
     * Valid Values: sessionContext objects (possibly null)
     * </p>
     */
    private SessionContext sessionContext;

    /**
     * <p>
     * Default constructor.
     * </p>
     */
    public UserStatusManagerSessionBean() {
        // nothing to do
    }

    /**
     * <p>
     * Defines a user status to be recognized within the persistent store managed by this utility. A unique user
     * status id will automatically be generated and assigned to the user status.
     * </p>
     *
     * @param userStatus
     *            The status for which the operation should be performed.
     * @throws IllegalArgumentException
     *             if status is null or has id != 0.
     * @throws DataAccessException
     *             if a problem occurs while accessing the persistent store.
     */
    public void createUserStatus(UserStatus userStatus) throws DataAccessException {
        try {
            getUserStatusManager().createUserStatus(userStatus);
        } catch (DataAccessException e) {
            sessionContext.setRollbackOnly();
            throw e;
        } catch (IllegalArgumentException e) {
            sessionContext.setRollbackOnly();
            throw e;
        }
    }

    /**
     * <p>
     * Modifies the persistent store so that it now reflects the data in the provided UserStatus parameter.
     * </p>
     * <p>
     * The implementation will set the UserStatus' modification details to the current date. These modification
     * details will also reflect in the persistent store. The modification user is the responsibility of the
     * calling application.
     * </p>
     * <p>
     * The given entity should have an id specified (id &gt; 0), or else IllegalArgumentException will be thrown.
     * </p>
     *
     * @param userStatus
     *            The status for which the operation should be performed.
     * @throws IllegalArgumentException
     *             if status is null or has id <=0
     * @throws com.topcoder.timetracker.user.UnrecognizedEntityException
     *             if a status with the provided id was not found in the data store.
     * @throws DataAccessException
     *             if a problem occurs while accessing the persistent store.
     */
    public void updateUserStatus(UserStatus userStatus) throws DataAccessException {
        try {
            getUserStatusManager().updateUserStatus(userStatus);
        } catch (DataAccessException e) {
            sessionContext.setRollbackOnly();
            throw e;
        } catch (IllegalArgumentException e) {
            sessionContext.setRollbackOnly();
            throw e;
        }
    }

    /**
     * <p>
     * Modifies the persistent store so that it no longer contains data on the user status with the specified id.
     * </p>
     * <p>
     * The given entity should have an id specified (id &gt; 0), or else IllegalArgumentException will be thrown.
     * </p>
     *
     * @param userStatusId
     *            The id of the status for which the operation should be performed.
     * @throws com.topcoder.timetracker.user.UnrecognizedEntityException
     *             if a status with the provided id was not found in the data store.
     * @throws IllegalArgumentException
     *             if statusId <= 0.
     * @throws DataAccessException
     *             if a problem occurs while accessing the persistent store.
     */
    public void removeUserStatus(long userStatusId) throws DataAccessException {
        try {
            getUserStatusManager().removeUserStatus(userStatusId);
        } catch (DataAccessException e) {
            sessionContext.setRollbackOnly();
            throw e;
        } catch (IllegalArgumentException e) {
            sessionContext.setRollbackOnly();
            throw e;
        }
    }

    /**
     * <p>
     * Retrieves a UserStatus object that reflects the data in the persistent store on the Time Tracker UserStatus
     * with the specified userStatusId.
     * </p>
     *
     * @param userStatusId
     *            The id of the status to retrieve.
     * @return The status with specified id.
     * @throws IllegalArgumentException
     *             if userStatusId <= 0
     * @throws com.topcoder.timetracker.user.UnrecognizedEntityException
     *             if a status with the provided id was not found in the data store.
     * @throws DataAccessException
     *             if a problem occurs while accessing the persistent store.
     */
    public UserStatus getUserStatus(long userStatusId) throws DataAccessException {
        try {
            return getUserStatusManager().getUserStatus(userStatusId);
        } catch (DataAccessException e) {
            sessionContext.setRollbackOnly();
            throw e;
        } catch (IllegalArgumentException e) {
            sessionContext.setRollbackOnly();
            throw e;
        }
    }

    /**
     * <p>
     * Searches the persistent store for any user statuses that satisfy the criteria that was specified in the
     * provided search filter. The provided filter should be created using either the filters that are created
     * using the UserStatusFilterFactory or a composite Search Filters (such as AndFilter, OrFilter and NotFilter
     * from Search Builder component) that combines the filters created using UserStatusFilterFactory.
     * </p>
     *
     * @param filter
     *            The filter used to search for statuses.
     * @return The statuses satisfying the conditions in the search filter.
     * @throws IllegalArgumentException
     *             if filter is null.
     * @throws DataAccessException
     *             if a problem occurs while accessing the persistent store.
     */
    public UserStatus[] searchUserStatuses(Filter filter) throws DataAccessException {
        try {
            return getUserStatusManager().searchUserStatuses(filter);
        } catch (DataAccessException e) {
            sessionContext.setRollbackOnly();
            throw e;
        } catch (IllegalArgumentException e) {
            sessionContext.setRollbackOnly();
            throw e;
        }
    }

    /**
     * <p>
     * This is a batch version of the createUserStatus method.
     * </p>
     * <p>
     * The creation and modification user is the responsibility of the calling application.
     * </p>
     *
     * @param userStatuses
     *            An array of user statuses for which the operation should be performed.
     * @throws IllegalArgumentException
     *             if userStatuses is null or contains null values or has ids != 0.
     * @throws DataAccessException
     *             if a problem occurs while accessing the persistent store.
     * @throws com.topcoder.timetracker.user.BatchOperationException
     *             if a problem occurs while processing the batch.
     */
    public void addUserStatuses(UserStatus[] userStatuses) throws DataAccessException {
        try {
            getUserStatusManager().addUserStatuses(userStatuses);
        } catch (DataAccessException e) {
            sessionContext.setRollbackOnly();
            throw e;
        } catch (IllegalArgumentException e) {
            sessionContext.setRollbackOnly();
            throw e;
        }
    }

    /**
     * <p>
     * This is a batch version of the updateUserStatus method.
     * </p>
     * <p>
     * The creation and modification user is the responsibility of the calling application.
     * </p>
     *
     * @param userStatuses
     *            An array of user statuses for which the operation should be performed.
     * @throws IllegalArgumentException
     *             if userStatuses is null or contains null values or has id <= 0.
     * @throws DataAccessException
     *             if a problem occurs while accessing the persistent store.
     * @throws com.topcoder.timetracker.user.BatchOperationException
     *             if a problem occurs while processing the batch.
     */
    public void updateUserStatuses(UserStatus[] userStatuses) throws DataAccessException {
        try {
            getUserStatusManager().updateUserStatuses(userStatuses);
        } catch (DataAccessException e) {
            sessionContext.setRollbackOnly();
            throw e;
        } catch (IllegalArgumentException e) {
            sessionContext.setRollbackOnly();
            throw e;
        }
    }

    /**
     * <p>
     * This is a batch version of the removeUserStatus method.
     * </p>
     *
     * @param userStatusIds
     *            An array of ids for which the operation should be performed.
     * @throws IllegalArgumentException
     *             if userStatusIds is null or contains values <= 0.
     * @throws DataAccessException
     *             if a problem occurs while accessing the persistent store.
     * @throws com.topcoder.timetracker.user.BatchOperationException
     *             if a problem occurs while processing the batch.
     */
    public void removeUserStatuses(long[] userStatusIds) throws DataAccessException {
        try {
            getUserStatusManager().removeUserStatuses(userStatusIds);
        } catch (DataAccessException e) {
            sessionContext.setRollbackOnly();
            throw e;
        } catch (IllegalArgumentException e) {
            sessionContext.setRollbackOnly();
            throw e;
        }
    }

    /**
     * <p>
     * This is a batch version of the getUserStatus method.
     * </p>
     *
     * @param userStatusIds
     *            An array of userStatusIds for which user status should be retrieved.
     * @return The UserStatuses corresponding to the provided ids.
     * @throws IllegalArgumentException
     *             if userStatusIds is null or contains values <= 0.
     * @throws DataAccessException
     *             if a problem occurs while accessing the persistent store.
     * @throws com.topcoder.timetracker.user.BatchOperationException
     *             if a problem occurs while processing the batch.
     */
    public UserStatus[] getUserStatuses(long[] userStatusIds) throws DataAccessException {
        try {
            return getUserStatusManager().getUserStatuses(userStatusIds);
        } catch (DataAccessException e) {
            sessionContext.setRollbackOnly();
            throw e;
        } catch (IllegalArgumentException e) {
            sessionContext.setRollbackOnly();
            throw e;
        }
    }

    /**
     * <p>
     * Retrieves all the UserStatuses that are currently in the persistent store.
     * </p>
     *
     * @return An array of user statuses retrieved from the persistent store.
     * @throws DataAccessException
     *             if a problem occurs while accessing the persistent store.
     */
    public UserStatus[] getAllUserStatuses() throws DataAccessException {
        try {
            return getUserStatusManager().getAllUserStatuses();
        } catch (DataAccessException e) {
            sessionContext.setRollbackOnly();
            throw e;
        }
    }

    /**
     * <p>
     * ejbCreate method. Empty implementation.
     * </p>
     */
    public void ejbCreate() {
        // nothing to do
    }

    /**
     * <p>
     * This method has simply been included to complete the SessionBean interface. It has an empty method body.
     * </p>
     */
    public void ejbActivate() {
        // nothing to do
    }

    /**
     * <p>
     * This method has simply been included to complete the SessionBean interface. It has an empty method body.
     * </p>
     */
    public void ejbPassivate() {
        // nothing to do
    }

    /**
     * <p>
     * This method has simply been included to complete the SessionBean interface. It has an empty method body.
     * </p>
     */
    public void ejbRemove() {
        // nothing to do
    }

    /**
     * <p>
     * Sets the SessionContext to use for this session. This method is included to comply with the SessionBean
     * interface.
     * </p>
     *
     * @param ctx
     *            The SessionContext to use .
     */
    public void setSessionContext(SessionContext ctx) {
        this.sessionContext = ctx;
    }

    /**
     * <p>
     * Protected method that allows subclasses to access the session context if necessary.
     * </p>
     *
     * @return The session context provided by the EJB container.
     */
    protected SessionContext getSessionContext() {
        return sessionContext;
    }

    /**
     * <p>
     * This method gets the current user status manager in the factory.
     * </p>
     *
     * @return the current user status manager in the factory
     *
     * @throws DataAccessException
     *             to wrap the <code>ConfigurationException</code> thrown by <code>UserManagerFactory</code>
     */
    private UserStatusManager getUserStatusManager() throws DataAccessException {
        try {
            return UserManagerFactory.getUserStatusManager();
        } catch (ConfigurationException e) {
            throw new DataAccessException(
                "ConfigurationException occurs when getting the user status manager.", e);
        }
    }
}
