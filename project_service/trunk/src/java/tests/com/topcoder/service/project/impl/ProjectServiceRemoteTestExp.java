/*
 * Copyright (C) 2008 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.service.project.impl;

import javax.ejb.EJBAccessException;

import com.topcoder.service.BaseUnitTestCase;
import com.topcoder.service.project.AuthorizationFailedFault;
import com.topcoder.service.project.Competition;
import com.topcoder.service.project.ConfigurationException;
import com.topcoder.service.project.IllegalArgumentFault;
import com.topcoder.service.project.MockUserGroupManager;
import com.topcoder.service.project.Project;
import com.topcoder.service.project.ProjectData;
import com.topcoder.service.project.ProjectHasCompetitionsFault;
import com.topcoder.service.project.ProjectNotFoundFault;
import com.topcoder.service.project.ProjectService;
import com.topcoder.service.project.ProjectServiceRemote;
import com.topcoder.service.project.StudioCompetition;
import com.topcoder.service.project.UserNotFoundFault;

/**
 * <p>
 * Failure test for <code>{@link ProjectServiceBean}</code>'s Remote interface.
 * </p>
 *
 * @author TCSDEVELOPER
 * @version 1.1
 */
public class ProjectServiceRemoteTestExp extends BaseUnitTestCase {

    /**
     * <p>
     * Failure test for <code>{@link ProjectServiceBean#initialize()}</code> method.
     * </p>
     *
     * <p>
     * If the 'log_name' property is not <code>String</code>,
     * should thrown <code>ConfigurationException</code>.
     * </p>
     *
     * @throws Exception
     *             pass any unexpected exception to JUnit.
     */
    public void testInitialize_LogName_WrongType() throws Exception {
        try {
            ProjectService projectService = (ProjectService) getInitialContext("username")
                .lookup("project_service/ProjectServiceBeanLogNameInvalidEnvType/remote");

            // need to invoke one business method to make the ejb initialized.
            projectService.getAllProjects();
            fail("ConfigurationException expected");
        } catch (RuntimeException e) {
            assertTrue("Expect ConfigurationException cause.", e.getCause() instanceof ConfigurationException);
        }
    }

    /**
     * <p>
     * Failure test for <code>{@link ProjectServiceBean#initialize()}</code> method.
     * </p>
     *
     * <p>
     * If the required 'administrator_role' property is missing,
     * should thrown <code>ConfigurationException</code>.
     * </p>
     *
     * @throws Exception
     *             pass any unexpected exception to JUnit.
     */
    public void testInitialize_AdministratorRole_Missing() throws Exception {
        try {
            ProjectService projectService = (ProjectService) getInitialContext("username")
                .lookup("project_service/ProjectServiceBeanMissingAdministratorRole/remote");

            // need to invoke one business method to make the ejb initialized.
            projectService.getAllProjects();
            fail("ConfigurationException expected");
        } catch (RuntimeException e) {
            assertTrue("Expect ConfigurationException cause.", e.getCause() instanceof ConfigurationException);
        }
    }

    /**
     * <p>
     * Failure test for <code>{@link ProjectServiceBean#initialize()}</code> method.
     * </p>
     *
     * <p>
     * If the required 'administrator_role' property is empty,
     * should thrown <code>ConfigurationException</code>.
     * </p>
     *
     * @throws Exception
     *             pass any unexpected exception to JUnit.
     */
    public void testInitialize_AdministratorRole_Empty() throws Exception {
        try {
            ProjectService projectService = (ProjectService) getInitialContext("username")
                .lookup("project_service/ProjectServiceBeanEmptyAdministratorRole/remote");

            // need to invoke one business method to make the ejb initialized.
            projectService.getAllProjects();
            fail("ConfigurationException expected");
        } catch (RuntimeException e) {
            assertTrue("Expect ConfigurationException cause.", e.getCause() instanceof ConfigurationException);
        }
    }

    /**
     * <p>
     * Failure test for <code>{@link ProjectServiceBean#initialize()}</code> method.
     * </p>
     *
     * <p>
     * If the required 'administrator_role' property is empty(trimmed),
     * should thrown <code>ConfigurationException</code>.
     * </p>
     *
     * @throws Exception
     *             pass any unexpected exception to JUnit.
     */
    public void testInitialize_AdministratorRole_TrimmedEmpty() throws Exception {
        try {
            ProjectService projectService = (ProjectService) getInitialContext("username")
                .lookup("project_service/ProjectServiceBeanTrimmedEmptyAdministratorRole/remote");

            // need to invoke one business method to make the ejb initialized.
            projectService.getAllProjects();
            fail("ConfigurationException expected");
        } catch (RuntimeException e) {
            assertTrue("Expect ConfigurationException cause.", e.getCause() instanceof ConfigurationException);
        }
    }

    /**
     * <p>
     * Failure test for <code>{@link ProjectServiceBean#initialize()}</code> method.
     * </p>
     *
     * <p>
     * If the required 'administrator_role' property is not <code>String</code>,
     * should thrown <code>ConfigurationException</code>.
     * </p>
     *
     * @throws Exception
     *             pass any unexpected exception to JUnit.
     */
    public void testInitialize_AdministratorRole_WrongType() throws Exception {
        try {
            ProjectService projectService = (ProjectService) getInitialContext("username")
                .lookup("project_service/ProjectServiceBeanAdministratorRoleInvalidEnvType/remote");

            // need to invoke one business method to make the ejb initialized.
            projectService.getAllProjects();
            fail("ConfigurationException expected");
        } catch (RuntimeException e) {
            assertTrue("Expect ConfigurationException cause.", e.getCause() instanceof ConfigurationException);
        }
    }

    /**
     * <p>
     * Failure test for <code>{@link ProjectServiceBean#createProject(ProjectData)}</code> method.
     * </p>
     *
     * <p>
     * If the passed project data is null, should throw <code>IllegalArgumentFault</code>.
     * </p>
     *
     * @throws Exception
     *             pass any unexpected exception to JUnit.
     */
    public void testCreateProject_null() throws Exception {
        try {
            this.lookupProjectServiceRemoteWithUserRole().createProject(null);
            fail("Expect IllegalArgumentFault.");
        } catch (IllegalArgumentFault e) {
            // expected
        }
    }

    /**
     * <p>
     * Failure test for <code>{@link ProjectServiceBean#createProject(ProjectData)}</code> method.
     * </p>
     *
     * <p>
     * If the name is null, should throw <code>IllegalArgumentFault</code>.
     * </p>
     *
     * @throws Exception
     *             pass any unexpected exception to JUnit.
     */
    public void testCreateProject_nullName() throws Exception {
        try {
            this.lookupProjectServiceRemoteWithUserRole().createProject(new ProjectData());
            fail("Expect IllegalArgumentFault.");
        } catch (IllegalArgumentFault e) {
            // expected
        }
    }

    /**
     * <p>
     * Failure test for <code>{@link ProjectServiceBean#createProject(ProjectData)}</code> method.
     * </p>
     *
     * <p>
     * If the name is empty, should throw <code>IllegalArgumentFault</code>.
     * </p>
     *
     * @throws Exception
     *             pass any unexpected exception to JUnit.
     */
    public void testCreateProject_emptyName() throws Exception {
        ProjectData projectData = new ProjectData();
        projectData.setName("");

        try {
            this.lookupProjectServiceRemoteWithUserRole().createProject(projectData);
            fail("Expect IllegalArgumentFault.");
        } catch (IllegalArgumentFault e) {
            // expected
        }
    }

    /**
     * <p>
     * Failure test for <code>{@link ProjectServiceBean#createProject(ProjectData)}</code> method.
     * </p>
     *
     * <p>
     * The name is trimmed empty, should throw <code>IllegalArgumentFault</code>.
     * </p>
     *
     * @throws Exception
     *             pass any unexpected exception to JUnit.
     */
    public void testCreateProject_TrimmedEmptyName() throws Exception {
        ProjectData projectData = new ProjectData();
        projectData.setName(" ");

        try {
            this.lookupProjectServiceRemoteWithUserRole().createProject(projectData);
            fail("Expect IllegalArgumentFault.");
        } catch (IllegalArgumentFault e) {
            // expected
        }
    }

    /**
     * <p>
     * Failure test for <code>{@link ProjectServiceBean#getProject(long)}</code> method.
     * </p>
     *
     * <p>
     * If the project is not present in persistence, should throw <code>AuthorizationFailedFault</code>.
     * </p>
     *
     * @throws Exception
     *             pass any unexpected exception to JUnit.
     */
    public void testGetProject_NotFound() throws Exception {
        try {
            // No such project with id as Long.MAX_VALUE
            this.lookupProjectServiceRemoteWithUserRole().getProject(Long.MAX_VALUE);
            fail("Expect ProjectNotFoundFault.");
        } catch (ProjectNotFoundFault e) {
            // expected
        }
    }

    /**
     * <p>
     * Failure test for <code>{@link ProjectServiceBean#getProject(long)}</code> method.
     * </p>
     *
     * <p>
     * If the project is not associated with the current user, should throw <code>AuthorizationFailedFault</code>.
     * </p>
     *
     * @throws Exception
     *             pass any unexpected exception to JUnit.
     */
    public void testGetProject_NotOwned() throws Exception {
        ProjectData project = new ProjectData();
        project.setName("name");

        // Create a project by administrator
        project = this.lookupProjectServiceRemoteWithAdminRole().createProject(project);

        try {
            // Get the previous created project by user. AuthorizationFailedFault should be thrown
            this.lookupProjectServiceRemoteWithUserRole().getProject(project.getProjectId());
            fail("Expect AuthorizationFailedFault.");
        } catch (AuthorizationFailedFault e) {
            // expected
        }

        this.lookupProjectServiceRemoteWithAdminRole().deleteProject(project.getProjectId());
    }

    /**
     * <p>
     * Failure test for <code>{@link ProjectServiceBean#getProjectsForUser(long)}</code> method.
     * </p>
     *
     * <p>
     * If the login user is not administrator, ejb container will do authorization check.
     * </p>
     *
     * @throws Exception
     *             pass any unexpected exception to JUnit.
     */
    public void testGetProjectsForUser_NotAdministrator() throws Exception {
        // Only administrator can call getProjectsForUser()
        try {
            this.lookupProjectServiceRemoteWithUserRole().getProjectsForUser(1);
            fail("Expect EJBAccessException");
        } catch (EJBAccessException e) {
            // expected
        }
    }

    /**
     * <p>
     * Failure test for <code>{@link ProjectServiceBean#getProjectsForUser(long)}</code> method.
     * </p>
     *
     * <p>
     * If no project found for the specified user, should throw <code>UserNotFoundFault</code>.
     * </p>
     *
     * @throws Exception
     *             pass any unexpected exception to JUnit.
     */
    public void testGetProjectsForUser_UserNotFound() throws Exception {

        try {
            // No projects associated with user whose id is Long.MAX_VALUE
            this.lookupProjectServiceRemoteWithAdminRole().getProjectsForUser(Long.MAX_VALUE);
            fail("Expect UserNotFoundFault");
        } catch (UserNotFoundFault e) {
            // expected
        }
    }

    /**
     * <p>
     * Failure test for <code>{@link ProjectServiceBean#updateProject(ProjectData)}</code> method.
     * </p>
     *
     * <p>
     * If the passed project data is null, should throw <code>IllegalArgumentFault</code>.
     * </p>
     *
     * @throws Exception
     *             pass any unexpected exception to JUnit.
     */
    public void testUpdateProject_null() throws Exception {
        try {
            this.lookupProjectServiceRemoteWithAdminRole().updateProject(null);
            fail("Expect IllegalArgumentFault.");
        } catch (IllegalArgumentFault e) {
            // expected
        }
    }

    /**
     * <p>
     * Failure test for <code>{@link ProjectServiceBean#updateProject(ProjectData)}</code> method.
     * </p>
     *
     * <p>
     * If the passed project id is null, should throw <code>IllegalArgumentFault</code>.
     * </p>
     *
     * @throws Exception
     *             pass any unexpected exception to JUnit.
     */
    public void testUpdateProject_nullProjectId() throws Exception {
        ProjectData projectData = new ProjectData();
        projectData.setName("Hello");
        try {
            this.lookupProjectServiceRemoteWithAdminRole().updateProject(projectData);
            fail("Expect IllegalArgumentFault.");
        } catch (IllegalArgumentFault e) {
            // expected
        }
    }

    /**
     * <p>
     * Failure test for <code>{@link ProjectServiceBean#updateProject(ProjectData)}</code> method.
     * </p>
     *
     * <p>
     * If the name is null, should throw <code>IllegalArgumentFault</code>.
     * </p>
     *
     * @throws Exception
     *             pass any unexpected exception to JUnit.
     */
    public void testUpdateProject_nullName() throws Exception {
        ProjectData projectData = new ProjectData();
        projectData.setProjectId(1L);

        try {
            this.lookupProjectServiceRemoteWithAdminRole().updateProject(projectData);
            fail("Expect IllegalArgumentFault.");
        } catch (IllegalArgumentFault e) {
            // expected
        }
    }

    /**
     * <p>
     * Failure test for <code>{@link ProjectServiceBean#updateProject(ProjectData)}</code> method.
     * </p>
     *
     * <p>
     * If the name is empty, should throw <code>IllegalArgumentFault</code>.
     * </p>
     *
     * @throws Exception
     *             pass any unexpected exception to JUnit.
     */
    public void testUpdateProject_emptyName() throws Exception {
        ProjectData projectData = new ProjectData();
        projectData.setProjectId(1L);
        projectData.setName("");

        try {
            this.lookupProjectServiceRemoteWithAdminRole().updateProject(projectData);
            fail("Expect IllegalArgumentFault.");
        } catch (IllegalArgumentFault e) {
            // expected
        }
    }

    /**
     * <p>
     * Failure test for <code>{@link ProjectServiceBean#updateProject(ProjectData)}</code> method.
     * </p>
     *
     * <p>
     * If the name is trimmed empty, should throw <code>IllegalArgumentFault</code>.
     * </p>
     *
     * @throws Exception
     *             pass any unexpected exception to JUnit.
     */
    public void testUpdateProject_TrimmedEmptyName() throws Exception {
        ProjectData projectData = new ProjectData();
        projectData.setProjectId(1L);
        projectData.setName("  ");

        try {
            this.lookupProjectServiceRemoteWithAdminRole().updateProject(projectData);
            fail("Expect IllegalArgumentFault.");
        } catch (IllegalArgumentFault e) {
            // expected
        }
    }

    /**
     * <p>
     * Failure test for <code>{@link ProjectServiceBean#updateProject(ProjectData)}</code> method.
     * </p>
     *
     * <p>
     * If the project is not found, should throw <code>ProjectNotFoundFault</code>.
     * </p>
     *
     * @throws Exception
     *             pass any unexpected exception to JUnit.
     */
    public void testUpdateProject_NotFound() throws Exception {
        ProjectData projectData = new ProjectData();
        projectData.setName("Project Service");
        projectData.setDescription("Hello");

        // No such project with id as Long.MAX_VALUE
        projectData.setProjectId(Long.MAX_VALUE);

        try {
            this.lookupProjectServiceRemoteWithAdminRole().updateProject(projectData);
            fail("Expect ProjectNotFoundFault.");
        } catch (ProjectNotFoundFault e) {
            // expected
        }
    }

    /**
     * <p>
     * Failure test for <code>{@link ProjectServiceBean#updateProject(ProjectData)}</code> method.
     * </p>
     *
     * <p>
     * If the project is not owned by the user, should throw <code>AuthorizationFailedFault</code>.
     * </p>
     *
     * @throws Exception
     *             pass any unexpected exception to JUnit.
     */
    public void testUpdateProject_NotOwned() throws Exception {
        ProjectData projectData = new ProjectData();
        projectData.setName("Project Service");
        projectData.setDescription("Hello");

        // Create a project by administrator
        projectData = this.lookupProjectServiceRemoteWithAdminRole().createProject(projectData);

        try {
            // Update the previous created project by user. AuthorizationFailedFault should be thrown
            this.lookupProjectServiceRemoteWithUserRole().updateProject(projectData);
            fail("Expect AuthorizationFailedFault.");
        } catch (AuthorizationFailedFault e) {
            // expected
        }

        this.lookupProjectServiceRemoteWithAdminRole().deleteProject(projectData.getProjectId());
    }

    /**
     * <p>
     * Failure test for <code>{@link ProjectServiceBean#deleteProject(long)}</code> method.
     * </p>
     *
     * <p>
     * If user is not authorized to delete the project, should throw <code>AuthorizationFailedFault</code>.
     * </p>
     *
     * @throws Exception
     *             pass any unexpected exception to JUnit.
     */
    public void testDeleteProject_NotOwned() throws Exception {
        ProjectData projectData = new ProjectData();
        projectData.setName("Project Service");
        projectData.setDescription("Hello");

        // Create a project by administrator
        projectData = this.lookupProjectServiceRemoteWithAdminRole().createProject(projectData);

        try {
            // Delete the previous created project by user. AuthorizationFailedFault should be thrown
            this.lookupProjectServiceRemoteWithUserRole().deleteProject(projectData.getProjectId());
            fail("Expect AuthorizationFailedFault");
        } catch (AuthorizationFailedFault e) {
            // expected
        }

        this.lookupProjectServiceRemoteWithAdminRole().deleteProject(projectData.getProjectId());
    }

    /**
     * <p>
     * Failure test for <code>{@link ProjectServiceBean#deleteProject(long)}</code> method.
     * </p>
     *
     * <p>
     * The project has competitions associated, should throw <code>ProjectHasCompetitionsFault</code>.
     * </p>
     *
     * @throws Exception
     *             pass any unexpected exception to JUnit.
     */
    public void testDeleteProject_HasCompetitions() throws Exception {
        ProjectData projectData = new ProjectData();
        projectData.setName("Project Service");
        projectData.setDescription("Hello");

        projectData = this.lookupProjectServiceRemoteWithAdminRole().createProject(projectData);

        // Persist a competition within project
        Competition competition = new StudioCompetition();
        competition.setProject(getEntityManager().find(Project.class, projectData.getProjectId()));
        persist(competition);

        try {
            // The project can not be deleted since it has competition associated
            this.lookupProjectServiceRemoteWithAdminRole().deleteProject(projectData.getProjectId());
            fail("Expect ProjectHasCompetitionsFault");
        } catch (ProjectHasCompetitionsFault e) {
            // expected
        }

        executeScript("/clean.sql");
    }

    /**
     * <p>
     * Failure test for <code>{@link ProjectServiceBean#createProject(ProjectData)}</code> method.
     * </p>
     *
     * <p>
     * The user has no role, should throw <code>EJBAccessException</code>.
     * </p>
     *
     * @throws Exception
     *             pass any unexpected exception to JUnit.
     */
    public void testCreateProject_UserHasNoRole() throws Exception {
        ProjectData projectData = new ProjectData();
        try {
            ((ProjectServiceRemote) getInitialContext(MockUserGroupManager.NO_ROLES_USER)
                    .lookup("remote/ProjectServiceBean")).createProject(projectData);
            fail("Expect EJBAccessException.");
        } catch (EJBAccessException e) {
            // expected
        }
    }

    /**
     * <p>
     * Failure test for <code>{@link ProjectServiceBean#getProject(ProjectData)}</code> method.
     * </p>
     *
     * <p>
     * The user has no role, should throw <code>EJBAccessException</code>.
     * </p>
     *
     * @throws Exception
     *             pass any unexpected exception to JUnit.
     */
    public void testGetProject_UserHasNoRole() throws Exception {
        try {
            ((ProjectServiceRemote) getInitialContext(MockUserGroupManager.NO_ROLES_USER)
                    .lookup("remote/ProjectServiceBean")).getProject(1L);
            fail("Expect EJBAccessException.");
        } catch (EJBAccessException e) {
            // expected
        }
    }

    /**
     * <p>
     * Failure test for <code>{@link ProjectServiceBean#getProjectsForUser(long)}</code> method.
     * </p>
     *
     * <p>
     * The user has no role, should throw <code>EJBAccessException</code>.
     * </p>
     *
     * @throws Exception
     *             pass any unexpected exception to JUnit.
     */
    public void testGetProjectsForUser_UserHasNoRole() throws Exception {

        try {
            ((ProjectServiceRemote) getInitialContext(MockUserGroupManager.NO_ROLES_USER)
                    .lookup("remote/ProjectServiceBean")).getProjectsForUser(1L);
            fail("Expect EJBAccessException");
        } catch (EJBAccessException e) {
            // expected
        }
    }

    /**
     * <p>
     * Failure test for <code>{@link ProjectServiceBean#updateProject(ProjectData)}</code> method.
     * </p>
     *
     * <p>
     * The user has no role, should throw <code>EJBAccessException</code>.
     * </p>
     *
     * @throws Exception
     *             pass any unexpected exception to JUnit.
     */
    public void testUpdateProject_UserHasNoRole() throws Exception {
        ProjectData projectData = new ProjectData();
        try {
            ((ProjectServiceRemote) getInitialContext(MockUserGroupManager.NO_ROLES_USER)
                    .lookup("remote/ProjectServiceBean")).updateProject(projectData);
            fail("Expect EJBAccessException.");
        } catch (EJBAccessException e) {
            // expected
        }
    }

    /**
     * <p>
     * Failure test for <code>{@link ProjectServiceBean#deleteProject(long)}</code> method.
     * </p>
     *
     * <p>
     * The user has no role, should throw <code>EJBAccessException</code>.
     * </p>
     *
     * @throws Exception
     *             pass any unexpected exception to JUnit.
     */
    public void testDeleteProject_UserHasNoRole() throws Exception {
        try {
            ((ProjectServiceRemote) getInitialContext(MockUserGroupManager.NO_ROLES_USER)
                    .lookup("remote/ProjectServiceBean")).deleteProject(1L);
            fail("Expect EJBAccessException");
        } catch (EJBAccessException e) {
            // expected
        }
    }

    /**
     * <p>
     * Failure test for <code>{@link ProjectServiceBean#getAllProjects()}</code> method.
     * </p>
     *
     * <p>
     * The user has no role, should throw <code>EJBAccessException</code>.
     * </p>
     *
     * @throws Exception
     *             pass any unexpected exception to JUnit.
     */
    public void testGetAllProjects_UserHasNoRole() throws Exception {
        try {
            ((ProjectServiceRemote) getInitialContext(MockUserGroupManager.NO_ROLES_USER)
                    .lookup("remote/ProjectServiceBean")).getAllProjects();
            fail("Expect EJBAccessException");
        } catch (EJBAccessException e) {
            // expected
        }
    }
}
