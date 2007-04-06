/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.timetracker.user;

import com.topcoder.search.builder.filter.Filter;
import com.topcoder.security.authorization.SecurityRole;

/**
 * <p>
 * This interface represents the API that may be used in order to manipulate the various details
 * involving a Time Tracker User.
 * </p>
 *
 * <p>
 * <b>CRUDE</b> and search methods are provided to manage the Users inside a persistent store.
 * </p>
 *
 * <p>
 * There are also methods that for the manipulation of various roles for the Time Tracker user.
 * </p>
 *
 * <p>
 * It is also possible to search the persistent store for Users based on different search criteria.
 * </p>
 *
 * <p>
 * Thread safety: Implementations of this interface are required to be thread safety.
 * </p>
 *
 * @author ShindouHikaru, TCSDEVELOPER
 * @version 3.2
 */
public interface UserManager {
    /**
     * <p>
     * Defines a user to be recognized within the persistent store managed by this manage.
     * </p>
     *
     * <p>
     * A unique user id will automatically be generated and assigned to the user.
     * </p>
     *
     * <p>
     * There is also the option to perform an audit.
     * </p>
     *
     * <p>
     * The implementation will set the User's creation and modification date to the current
     * date. These creation/modification details will also reflect in the persistent store.
     * The creation and modification user is the responsibility of the calling application.
     * </p>
     *
     * @param user The user for which the operation should be performed.
     * @param audit Indicates whether an audit should be performed.
     *
     * @throws IllegalArgumentException if user is null or user contains null property which
     * is required when persisting.
     * @throws DataAccessException if a problem occurs while accessing the persistent store.
     */
    public void createUser(User user, boolean audit) throws DataAccessException;

    /**
     * <p>
     * Modifies the persistent store so that it now reflects the data in the provided User parameter.
     * </p>
     *
     * <p>
     * There is also the option to perform an audit.
     * </p>
     *
     * <p>
     * The implementation will set the User's creation and modification details to the current
     * date. These creation/modification details will also reflect in the persistent store.
     * The creation and modification user is the responsibility of the calling application.
     * </p>
     *
     * @param user The user for which the operation should be performed.
     * @param audit Indicates whether an audit should be performed.
     *
     * @throws IllegalArgumentException if user is null.
     * @throws UnrecognizedEntityException if a user with the provided id was not found in
     * the data store.
     * @throws DataAccessException if a problem occurs while accessing the persistent store.
     */
    public void updateUser(User user, boolean audit) throws UnrecognizedEntityException, DataAccessException;

    /**
     * <p>
     * Modifies the persistent store so that it no longer contains data on the user with the specified
     * userId.
     * </p>
     *
     * <p>
     * There is also the option to perform an audit.
     * </p>
     *
     * @param userId The userId for which the operation should be performed.
     * @param audit Indicates whether an audit should be performed.
     *
     * @throws IllegalArgumentException if userId &lt;= 0.
     * @throws UnrecognizedEntityException if a user with the provided id was not found in the data store.
     * @throws DataAccessException if a problem occurs while accessing the persistent store.
     */
    public void removeUser(long userId, boolean audit) throws UnrecognizedEntityException, DataAccessException;

    /**
     * <p>
     * Retrieves a <code>User</code> object that reflects the data in the persistent store on the
     * Time Tracker User with the specified <code>userId</code>.
     * </p>
     *
     * @param usertId The id of the user to retrieve.
     *
     * @return The user with specified id.
     * @throws UnrecognizedEntityException if a user with the provided id was not found in the data store.
     * @throws DataAccessException if a problem occurs while accessing the persistent store.
     */
    public User getUser(long usertId) throws UnrecognizedEntityException, DataAccessException;

    /**
     * <p>
     * Searches the persistent store for any users that satisfy the criteria that was specified in the
     * provided search filter.
     * </p>
     *
     * <p>
     * The provided filter should be created using either the filters that are created using the
     * <code>UserFilterFactory</code> returned by {@link UserManager#getUserFilterFactory()}, or
     * a composite Search Filters (such as <code>AndFilter</code>, <code>OrFilter</code> and
     * <code>NotFilter</code> from Search Builder component) that combines the filters created using
     * <code>UserFilterFactory</code>.
     * </p>
     *
     * @param filter The filter used to search for projects.
     * @return The projects satisfying the conditions in the search filter.
     *
     * @throws IllegalArgumentException if filter is null.
     * @throws DataAccessException if a problem occurs while accessing the persistent store.
     */
    public User[] searchUsers(Filter filter) throws DataAccessException;

    /**
     * <p>
     * This is a batch version of the {@link UserManager#createUser(User, boolean)} method.
     * </p>
     *
     * @param users An array of users for which the operation should be performed.
     * @param audit Indicates whether an audit should be performed.
     *
     * @throws IllegalArgumentException if users is null, empty or contains null values, or some user
     * contains null property which is required when persisting.
     * @throws DataAccessException if a problem occurs while accessing the persistent store.
     * @throws BatchOperationException if a problem occurs during the batch operation.
     */
    public void addUsers(User[] users, boolean audit) throws DataAccessException, BatchOperationException;

    /**
     * <p>
     * This is a batch version of the {@link UserManager#updateUser(User, boolean)} method.
     * </p>
     *
     * <p>
     * Note, <code>UnrecognizedEntityException</code> will be thrown as an cause of
     * <code>BatchOperationException</code> if a user with the provided id was not
     * found in the persistence store.
     * </p>
     *
     * @param users An array of users for which the operation should be performed.
     * @param audit Indicates whether an audit should be performed.
     *
     * @throws IllegalArgumentException if users is null, empty or contains null values, or some user
     * contains null property which is required when persisting.
     * @throws DataAccessException if a problem occurs while accessing the persistent store.
     * @throws BatchOperationException if a problem occurs during the batch operation.
     */
    public void updateUsers(User[] users, boolean audit) throws DataAccessException, BatchOperationException;

    /**
     * <p>
     * This is a batch version of the {@link UserManager#removeUser(long, boolean)} method.
     * </p>
     *
     * <p>
     * Note, <code>UnrecognizedEntityException</code> will be thrown as an cause of
     * <code>BatchOperationException</code> if a user with the provided id was not
     * found in the persistence store.
     * </p>
     *
     * @param userIds An array of userIds for which the operation should be performed.
     * @param audit Indicates whether an audit should be performed.
     *
     * @throws IllegalArgumentException if userIds is null, empty or contains values &lt;= 0.
     * @throws DataAccessException if a problem occurs while accessing the persistent store.
     * @throws BatchOperationException if a problem occurs during the batch operation.
     */
    public void removeUsers(long[] userIds, boolean audit) throws DataAccessException, BatchOperationException;

    /**
     * <p>
     * This is a batch version of the {@link UserManager#getUser(long)} method.
     * </p>
     *
     * <p>
     * Note, <code>UnrecognizedEntityException</code> will be thrown as an cause of
     * <code>BatchOperationException</code> if a user with the provided id was not
     * found in the persistence store.
     * </p>
     *
     * @param userIds An array of userIds for which users should be retrieved.
     * @return The users corresponding to the provided ids.
     *
     * @throws IllegalArgumentException if userIds is null, empty or contains values &lt;= 0.
     * @throws DataAccessException if a problem occurs while accessing the persistent store.
     * @throws BatchOperationException if a problem occurs during the batch operation.
     */
    public User[] getUsers(long[] userIds) throws DataAccessException, BatchOperationException;

    /**
     * <p>
     * This adds an Authorization role to the specified user.
     * </p>
     *
     * <p>
     * The User will now be authorized to perform any actions assigned to that <code>Role</code>.
     * </p>
     *
     * <p>
     * The roles to assign may be retrieved from the Authorization Component.
     * </p>
     *
     * @param user The user for which the operation should be performed.
     * @param role The authorization role to assign to the user.
     *
     * @throws IllegalArgumentException if any parameter is null.
     * @throws UnrecognizedEntityException if a user with the provided id was not found in the data store.
     * @throws DataAccessException if a problem occurs while accessing the persistent store.
     */
    public void addRoleToUser(User user, SecurityRole role) throws UnrecognizedEntityException, DataAccessException;

    /**
     * <p>
     * This removes an Authorization Role from the specified user.
     * </p>
     *
     * <p>
     * The User will no longer be authorized to perform any actions assigned to that <code>Role</code>.
     * </p>
     *
     * <p>
     * The roles to remove may be retrieved from the Authorization Component.
     * </p>
     *
     * @param user The user for which the operation should be performed.
     * @param role The authorization role to remove.
     *
     * @throws IllegalArgumentException if either argument is null.
     * @throws UnrecognizedEntityException if a role or user was not found in the data store.
     * @throws DataAccessException if a problem occurs while accessing the persistent store.
     */
    public void removeRoleFromUser(User user, SecurityRole role) throws UnrecognizedEntityException,
        DataAccessException;

    /**
     * <p>
     * Retrieves all the authorization roles for the given user.
     * </p>
     *
     * @param user The user for which the operation should be performed.
     * @return An array of SecurityRoles that were assigned to the user (may be empty array)
     *
     * @throws IllegalArgumentException if the user is null.
     * @throws UnrecognizedEntityException if the given user was not found in the data store.
     * @throws DataAccessException if a problem occurs while accessing the persistent store.
     */
    public SecurityRole[] retrieveRolesForUser(User user) throws UnrecognizedEntityException, DataAccessException;

    /**
     * <p>
     * Removes all the Authorization roles for the given user.
     * </p>
     *
     * <p>
     * The user will no longer be able to perform any actions that require authorization.
     * </p>
     *
     * @param user The user whose roles are to be cleared.
     *
     * @throws IllegalArgumentException if the user is null.
     * @throws DataAccessException if a problem occurs while accessing the persistent store.
     * @throws UnrecognizedEntityException if a user was not found in the data store.
     */
    public void clearRolesFromUser(User user) throws DataAccessException, UnrecognizedEntityException;

    /**
     * <p>
     * Authenticates a provided user name against the login information that is in the data store.
     * </p>
     *
     * <p>
     * The given password will be compared against the one that is stored in persistence, and the results
     * of the comparison will be returned.
     * </p>
     *
     * @param username The user name to authenticate.
     * @param password The password provided by the user.
     * @return true if the password matches; false otherwise.
     *
     * @throws IllegalArgumentException if user name or password is null or empty string
     * @throws DataAccessException if a problem occurs while accessing the persistent store.
     */
    public boolean authenticateUser(String username, String password) throws DataAccessException;

    /**
     * <p>
     * Retrieves all the users that are currently in the persistent store.
     * </p>
     *
     * @return An array of users retrieved from the persistent store.
     *
     * @throws DataAccessException if a problem occurs while accessing the persistent store.
     */
    public User[] getAllUsers() throws DataAccessException;

    /**
     * <p>
     * Retrieves the <code>UserFilterFactory</code> that is capable of creating SearchFilters to
     * use when searching for users.
     * </p>
     *
     * <p>
     * This is used to conveniently specify the search criteria that should be used.
     * The filters returned by the given factory should be used with {@link UserManager#searchUsers(Filter)}
     * method.
     * </p>
     *
     * @return the <code>UserFilterFactory</code> that is capable of creating SearchFilters to use
     * when searching for users.
     */
    public UserFilterFactory getUserFilterFactory();
}
