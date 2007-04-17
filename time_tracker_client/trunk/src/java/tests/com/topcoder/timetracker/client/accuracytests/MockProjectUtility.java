/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.timetracker.client.accuracytests;

import com.topcoder.timetracker.project.DataAccessException;
import com.topcoder.timetracker.project.Project;
import com.topcoder.timetracker.project.ProjectUtility;
import com.topcoder.timetracker.project.UnrecognizedEntityException;

/**
 * <p>
 * This interface represents the API that may be used in order to manipulate the various details involving a Time
 * Tracker Project.  CRUDE and search methods are provided to manage the Projects inside a persistent store.  There
 * are also methods that allow various Time, Expense, and Fixed Billing entries to be associated with a Project.  It
 * is also possible to search the persistent store for Projects based on different search criteria.
 * </p>
 *
 * <p>
 * Thread safety: Thread safety is required for implementations of this interface.
 * </p>
 *
 * @author victorsam
 * @version 3.2
 */
public class MockProjectUtility implements ProjectUtility {
    /**
     * <p>
     * Defines a project to be recognized within the persistent store managed by this utility.  A unique project id
     * will automatically be generated and assigned to the project.  There is also the option to perform an audit.  If
     * an audit is specified, then the audit must be rolled back in the case that the operation fails.
     * </p>
     *
     * <p>
     * The implementation will set the Project's creation and modification date to the current date.   These
     * creation/modification details will also reflect in the persistent store.
     * </p>
     *
     * @param project The project for which the operation should be performed.
     * @param audit Indicates whether an audit should be performed.
     *
     * @throws IllegalArgumentException if project is null.
     * @throws DataAccessException if a problem occurs while accessing the persistent store.
     */
    public void addProject(com.topcoder.timetracker.project.Project project, boolean audit) {
    }

    /**
     * <p>
     * Modifies the persistent store so that it now reflects the data in the provided Project parameter. There is also
     * the option to perform an audit.  If an audit is specified, then the audit must be  rolled back in the case that
     * the operation fails.
     * </p>
     *
     * <p>
     * The implementation will set the Project's modification date to the current date.   These modification details
     * will also reflect in the persistent store.
     * </p>
     *
     * @param project The project for which the operation should be performed.
     * @param audit Indicates whether an audit should be performed.
     *
     * @throws IllegalArgumentException if project is null.
     * @throws UnrecognizedEntityException if a project with the provided id was not found in the data store.
     * @throws DataAccessException if a problem occurs while accessing the persistent store.
     */
    public void updateProject(com.topcoder.timetracker.project.Project project, boolean audit) {
    }

    /**
     * <p>
     * Modifies the persistent store so that it  no longer contains data on the project with the specified projectId.
     * </p>
     *
     * <p>
     * There is also the option to perform an audit.  If an audit is specified, then the audit must be  rolled back in
     * the case that the operation fails.
     * </p>
     *
     * @param projectId The projectId for which the operation should be performed.
     * @param audit Indicates whether an audit should be performed.
     *
     * @throws UnrecognizedEntityException if a project with the provided id was not found in the data store.
     * @throws IllegalArgumentException if projectId not positive.
     * @throws DataAccessException if a problem occurs while accessing the persistent store.
     */
    public void removeProject(long projectId, boolean audit) {
    }

    /**
     * <p>
     * Retrieves a Project object that reflects the data in the persistent store on the Time Tracker Project with the
     * specified projectId.
     * </p>
     *
     * @param projectId The id of the project to retrieve.
     *
     * @return The project with specified id.
     *
     * @throws UnrecognizedEntityException if a project with the provided id was not found in the data store.
     * @throws DataAccessException if a problem occurs while accessing the persistent store.
     */
    public com.topcoder.timetracker.project.Project getProject(long projectId) {
        Project project = new Project();
        project.setId(projectId);
        return project;
    }

    /**
     * <p>
     * Searches the persistent store for any projects that satisfy the criteria that was specified in the provided
     * search filter.  The provided filter should be created using either the filters that are created using the
     * ProjectFilterFactory returned by getProjectFilterFactory of this instance, or a composite Search Filters (such
     * as AndFilter, OrFilter and NotFilter from Search Builder component) that combines the filters created using
     * ProjectFilterFactory.
     * </p>
     *
     * <p>
     * An empty array is returned if no entities are found.
     * </p>
     *
     * @param filter The filter used to search for projects.
     *
     * @return The projects satisfying the conditions in the search filter.
     *
     * @throws IllegalArgumentException if filter is null.
     * @throws DataAccessException if a problem occurs while accessing the persistent store.
     */
    public com.topcoder.timetracker.project.Project[] searchProjects(com.topcoder.search.builder.filter.Filter filter) {
        return null;
    }

    /**
     * <p>
     * This is a batch version of the addProject method.  If any of the projects fail to be added, then the entire
     * batch operation will be rolled back.
     * </p>
     *
     * @param projects An array of projects for which the operation should be performed.
     * @param audit Indicates whether an audit should be performed.
     *
     * @throws DuplicateEntityException if a project with the same id has already been found.
     * @throws IllegalArgumentExceptionif projects is null or contains null values.
     * @throws DataAccessException if a problem occurs while accessing the persistent store.
     */
    public void addProjects(com.topcoder.timetracker.project.Project[] projects, boolean audit) {
    }

    /**
     * <p>
     * This is a batch version of the updateProject method.  If any of the projects fail to be update, then the entire
     * batch operation will be rolled back.
     * </p>
     *
     * @param projects An array of projects for which the operation should be performed.
     * @param audit Indicates whether an audit should be performed.
     *
     * @throws UnrecognizedEntityException if a project with the provided id was not found in the data store.
     * @throws IllegalArgumentExceptionif projects is null or contains null values.
     * @throws DataAccessException if a problem occurs while accessing the persistent store.
     */
    public void updateProjects(com.topcoder.timetracker.project.Project[] projects, boolean audit) {
    }

    /**
     * <p>
     * This is a batch version of the removeProject method.  If any of the projects fail to be update, then the entire
     * batch operation will be rolled back.
     * </p>
     *
     * @param projectIds An array of projectIds for which the operation should be performed.
     * @param audit Indicates whether an audit should be performed.
     *
     * @throws UnrecognizedEntityException if a project with the provided id was not found in the data store.
     * @throws IllegalArgumentException if projectIds is null or contains values not positive.
     * @throws DataAccessException if a problem occurs while accessing the persistent store.
     */
    public void removeProjects(long[] projectIds, boolean audit) {
    }

    /**
     * <p>
     * This is a batch version of the getProject method.  If any of the projects fail to be update, then the entire
     * batch operation will be rolled back.
     * </p>
     *
     * <p>
     * An empty array is returned if no entities are found.
     * </p>
     *
     * @param projectsIds An array of projectIds for which projects should be retrieved.
     *
     * @return The projects corresponding to the provided ids.
     *
     * @throws IllegalArgumentException if projectIds is null or contains values not positive.
     * @throws UnrecognizedEntityException if a project with the provided id was not found in the data store.
     * @throws DataAccessException if a problem occurs while accessing the persistent store.
     */
    public com.topcoder.timetracker.project.Project[] getProjects(long[] projectsIds) {
        return null;
    }

    /**
     * <p>
     * This associates an Entry with the project.  It could be either an Expense, Time, or Fixed Billing entry, as
     * specified by the entry type provided.   There is also the option to perform an audit.  If an audit is
     * specified, then the audit must be  rolled back in the case that the operation fails.
     * </p>
     *
     * @param projectId The project for which the operation should be performed.
     * @param entryId The id of the entry for which the operation should be performed.
     * @param type The type of the entry.
     * @param audit Indicates whether an audit should be performed.
     *
     * @throws InvalidCompanyException if the entry and project company ids do not match.
     * @throws IllegalArgumentException if any id has a value not positive.
     * @throws UnrecognizedEntityException if a project or entry with the provided id was not found in the data store.
     * @throws DataAccessException if a problem occurs while accessing the persistent store.
     * @throws DuplicateEntityException if the entry has already been associated with the project.
     */
    public void addEntryToProject(long projectId, long entryId, com.topcoder.timetracker.project.EntryType type,
        boolean audit) {
    }

    /**
     * <p>
     * This disassociates an Entry with the project.  It could be either an Expense, Time, or Fixed Billing entry, as
     * specified by the entry type provided.   There is also the option to perform an audit.  If an audit is
     * specified, then the audit must be  rolled back in the case that the operation fails.
     * </p>
     *
     * @param projectId The project for which the operation should be performed.
     * @param entryId The id of the entry for which the operation should be performed.
     * @param type The type of the entry.
     * @param audit Indicates whether an audit should be performed.
     *
     * @throws UnrecognizedEntityException if a project or entry with the provided id was not found in the data store.
     * @throws DataAccessException if a problem occurs while accessing the persistent store.
     * @throws IllegalArgumentException if either id is not positive.
     */
    public void removeEntryFromProject(long projectId, long entryId, com.topcoder.timetracker.project.EntryType type,
        boolean audit) {
    }

    /**
     * <p>
     * Retrieves all the Entries of the specified type that are associated with the specified project.
     * </p>
     *
     * <p>
     * An empty array is returned if no entities are found.
     * </p>
     *
     * @param projectId The id of the project for which the operation should be performed.
     * @param type The type of Entry to be retrieved.
     *
     * @return An array of long identifiers of the Entries corresponding to the given project id.
     *
     * @throws IllegalArgumentException if the projectId is not positive or if type is null.
     * @throws UnrecognizedEntityException if a project with the provided id was not found in the data store.
     * @throws DataAccessException if a problem occurs while accessing the persistent store.
     */
    public long[] retrieveEntriesForProject(long projectId, com.topcoder.timetracker.project.EntryType type) {
        return null;
    }

    /**
     * <p>
     * Retrieves all the Entries of the specified type that are associated with projects being performed for a specific
     * client..
     * </p>
     *
     * <p>
     * An empty array is returned if no entities are found.
     * </p>
     *
     * @param clientId The id of the client for which this operation should be performed.
     * @param type The type of Entry to be retrieved.
     *
     * @return An array of long identifiers of the Entries corresponding to the given client id.
     *
     * @throws IllegalArgumentException if the clientId is not positive or if type is null.
     * @throws DataAccessException if a problem occurs while accessing the persistent store.
     */
    public long[] retrieveEntriesForClient(long clientId, com.topcoder.timetracker.project.EntryType type) {
        return null;
    }

    /**
     * <p>
     * Retrieves the ProjectFilterFactory that is capable of creating SearchFilters to use when searching for projects.
     * This is used to conveniently specify the search criteria that should be used.  The filters returned by the
     * given factory should be used with this instance's searchProject method.
     * </p>
     *
     * @return the ProjectFilterFactory that is capable of creating SearchFilters to use when searching for projects.
     */
    public com.topcoder.timetracker.project.ProjectFilterFactory getProjectFilterFactory() {
        return null;
    }

    /**
     * <p>
     * Retrieves all the projects  that are currently in the persistent store.
     * </p>
     *
     * <p>
     * An empty array is returned if no entities are found.
     * </p>
     *
     * @return An array of projects retrieved from the persistent store.
     *
     * @throws DataAccessException if a problem occurs while accessing the persistent store.
     */
    public com.topcoder.timetracker.project.Project[] enumerateProjects() {
        return null;
    }
}
