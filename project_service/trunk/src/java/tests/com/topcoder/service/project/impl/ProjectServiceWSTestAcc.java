/*
 * Copyright (C) 2008 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.service.project.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.topcoder.service.project.BaseUnitTestCase;
import com.topcoder.service.project.Competition;
import com.topcoder.service.project.StudioCompetition;
import com.topcoder.service.project.Project;
import com.topcoder.service.project.ProjectData;

/**
 * <p>
 * Accuracy test for <code>{@link ProjectServiceBean}</code>'s WebService client.
 * </p>
 *
 * @author TCSDEVELOPER
 * @version 1.1
 */
public class ProjectServiceWSTestAcc extends BaseUnitTestCase {

    /**
     * <p>
     * Accuracy test for <code>{@link ProjectServiceBean#createProject(ProjectData)}</code> method.
     * </p>
     *
     * <p>
     * Create project by user.
     * </p>
     *
     * @throws Exception
     *             pass any unexpected exception to JUnit.
     */
    public void testCreateProject_ByUser() throws Exception {
        ProjectData projectData = new ProjectData();
        projectData.setName("Project Service");
        projectData.setDescription("Hello");

        // Create a project by user
        ProjectData newPrjData = this.lookupProjectServiceWSClientWithUserRole().createProject(projectData);

        assertNotNull("Never return null.", newPrjData);
        assertNotNull("The project id should be updated.", newPrjData.getProjectId());

        // Get the created project by user
        ProjectData created = this.lookupProjectServiceWSClientWithUserRole().getProject(newPrjData.getProjectId());

        // Verify the result
        this.assertProjectData(newPrjData, created);

        // Delete the created project by user
        this.lookupProjectServiceWSClientWithUserRole().deleteProject(created.getProjectId());
    }

    /**
     * <p>
     * Accuracy test for <code>{@link ProjectServiceBean#createProject(ProjectData)}</code> method.
     * </p>
     *
     * <p>
     * Create project by administrator.
     * </p>
     *
     * @throws Exception
     *             pass any unexpected exception to JUnit.
     */
    public void testCreateProject_ByAdmin() throws Exception {
        ProjectData projectData = new ProjectData();
        projectData.setName("Project Service");
        projectData.setDescription("Hello");

        // Create a project by administrator
        ProjectData newPrjData = this.lookupProjectServiceWSClientWithAdminRole().createProject(projectData);

        assertNotNull("Never return null.", newPrjData);
        assertNotNull("The project id should be updated.", newPrjData.getProjectId());

        // Get the created project by administrator
        ProjectData created = this.lookupProjectServiceWSClientWithAdminRole().getProject(newPrjData.getProjectId());

        // Verify the result
        this.assertProjectData(newPrjData, created);

        // Delete the created project by administrator
        this.lookupProjectServiceWSClientWithAdminRole().deleteProject(created.getProjectId());
    }

    /**
     * <p>
     * Accuracy test for <code>{@link ProjectServiceBean#createProject(ProjectData)}</code> method.
     * </p>
     *
     * <p>
     * The description can be null.
     * </p>
     *
     * @throws Exception
     *             pass any unexpected exception to JUnit.
     */
    public void testCreateProject_DescriptionCanBeNull() throws Exception {
        ProjectData projectData = new ProjectData();
        projectData.setName("Project Service");

        // The description can be null
        projectData.setDescription(null);

        // Create a project by administrator
        ProjectData newPrjData = this.lookupProjectServiceWSClientWithAdminRole().createProject(projectData);

        assertNotNull("Never return null.", newPrjData);
        assertNotNull("The project id should be updated.", newPrjData.getProjectId());
        assertNull("Description should be null.", newPrjData.getDescription());

        // Get the created project by administrator
        ProjectData created = this.lookupProjectServiceWSClientWithAdminRole().getProject(newPrjData.getProjectId());

        // Verify the result
        this.assertProjectData(newPrjData, created);

        // Delete the created project by administrator
        this.lookupProjectServiceWSClientWithAdminRole().deleteProject(created.getProjectId());
    }

    /**
     * <p>
     * Accuracy test for <code>{@link ProjectServiceBean#createProject(ProjectData)}</code> method.
     * </p>
     *
     * <p>
     * Create project has an id, it should be ignored.
     * </p>
     *
     * @throws Exception
     *             pass any unexpected exception to JUnit.
     */
    public void testCreateProject_IdShouldBeIgnored() throws Exception {
        ProjectData projectData = new ProjectData();
        projectData.setName("Project Service");
        projectData.setDescription("Hello");

        // The id should be ignored
        projectData.setProjectId(1L);

        // Create a project by administrator
        ProjectData newPrjData = this.lookupProjectServiceWSClientWithAdminRole().createProject(projectData);

        assertNotNull("Never return null.", newPrjData);
        assertNotNull("The project id should be updated.", newPrjData.getProjectId());
        assertTrue("The project id should be updated.", 1L != newPrjData.getProjectId().longValue());

        // Get the created project by administrator
        ProjectData created = this.lookupProjectServiceWSClientWithAdminRole().getProject(newPrjData.getProjectId());

        // Verify the result
        this.assertProjectData(newPrjData, created);

        // Delete the created project by administrator
        this.lookupProjectServiceWSClientWithAdminRole().deleteProject(created.getProjectId());
    }

    /**
     * <p>
     * Accuracy test for <code>{@link ProjectServiceBean#createProject(ProjectData)}</code> method.
     * </p>
     *
     * <p>
     * Create project has a set of <code>Competition</code>, they should be ignored.
     * </p>
     *
     * @throws Exception
     *             pass any unexpected exception to JUnit.
     */
    @SuppressWarnings("unchecked")
    public void testCreateProject_CompetitionsShouldBeIgnored() throws Exception {
        Project project = new Project();
        project.setName("Project Service");
        project.setDescription("Hello");

        // The competitions should be ignored
        Set < Competition > competitions = new HashSet();
        Competition competition = new StudioCompetition();
        competition.setProject(project);
        competitions.add(competition);
        project.setCompetitions(competitions);

        // Create a project by administrator
        ProjectData newPrjData = this.lookupProjectServiceWSClientWithAdminRole().createProject(project);

        assertNotNull("Never return null.", newPrjData);
        assertNotNull("The project id should be updated.", newPrjData.getProjectId());

        // Get the created project by administrator
        ProjectData created = this.lookupProjectServiceWSClientWithAdminRole().getProject(newPrjData.getProjectId());

        // Verify the result
        this.assertProjectData(newPrjData, created);

        // Since we can delete the project, so that means it has no competitions
        this.lookupProjectServiceWSClientWithAdminRole().deleteProject(created.getProjectId());
    }

    /**
     * <p>
     * Accuracy test for <code>{@link ProjectServiceBean#getProject(long)}</code> method.
     * </p>
     *
     * <p>
     * Get project by user.
     * </p>
     *
     * @throws Exception
     *             pass any unexpected exception to JUnit.
     */
    public void testGetProject_ByUser() throws Exception {
        ProjectData projectData = new ProjectData();
        projectData.setName("Project Service");
        projectData.setDescription("Hello");

        // Create a project by user
        ProjectData newPrjData = this.lookupProjectServiceWSClientWithUserRole().createProject(projectData);

        assertNotNull("Never return null.", newPrjData);
        assertNotNull("The project id should be updated.", newPrjData.getProjectId());

        // Get the created project by user
        ProjectData created = this.lookupProjectServiceWSClientWithUserRole().getProject(newPrjData.getProjectId());

        // Verify the result
        this.assertProjectData(newPrjData, created);

        // Delete the created project by user
        this.lookupProjectServiceWSClientWithUserRole().deleteProject(created.getProjectId());
    }

    /**
     * <p>
     * Accuracy test for <code>{@link ProjectServiceBean#getProject(long)}</code> method.
     * </p>
     *
     * <p>
     * Get project by administrator.
     * </p>
     *
     * @throws Exception
     *             pass any unexpected exception to JUnit.
     */
    public void testGetProject_ByAdmin() throws Exception {
        ProjectData projectData = new ProjectData();
        projectData.setName("Project Service");
        projectData.setDescription("Hello");

        // Create a project by administrator
        ProjectData newPrjData = this.lookupProjectServiceWSClientWithAdminRole().createProject(projectData);

        assertNotNull("Never return null.", newPrjData);
        assertNotNull("The project id should be updated.", newPrjData.getProjectId());

        // Get the created project by administrator
        ProjectData created = this.lookupProjectServiceWSClientWithAdminRole().getProject(newPrjData.getProjectId());

        // Verify the result
        this.assertProjectData(newPrjData, created);

        // Delete the created project by administrator
        this.lookupProjectServiceWSClientWithAdminRole().deleteProject(created.getProjectId());
    }

    /**
     * <p>
     * Accuracy test for <code>{@link ProjectServiceBean#getProject(long)}</code> method.
     * </p>
     *
     * <p>
     * Get project by administrator, the administrator can retrieve any project.
     * </p>
     *
     * @throws Exception
     *             pass any unexpected exception to JUnit.
     */
    public void testGetProject_AdminCanRetrieveAnyProject() throws Exception {
        ProjectData projectData = new ProjectData();
        projectData.setName("Project Service");
        projectData.setDescription("Hello");

        // Create a project by user
        ProjectData newPrjData = this.lookupProjectServiceWSClientWithUserRole().createProject(projectData);

        assertNotNull("Never return null.", newPrjData);
        assertNotNull("The project id should be updated.", newPrjData.getProjectId());

        // Get the created project by administrator
        ProjectData created = this.lookupProjectServiceWSClientWithAdminRole().getProject(newPrjData.getProjectId());

        // Verify the result
        this.assertProjectData(newPrjData, created);

        // Delete the created project by administrator
        this.lookupProjectServiceWSClientWithAdminRole().deleteProject(created.getProjectId());
    }

    /**
     * <p>
     * Accuracy test for <code>{@link ProjectServiceBean#getProjectsForUser(long)}</code> method.
     * </p>
     *
     * <p>
     * Only administrator can get projects for specific user.
     * </p>
     *
     * @throws Exception
     *             pass any unexpected exception to JUnit.
     */
    public void testGetProjectsForUser() throws Exception {
        ProjectData projectData1 = new ProjectData();
        projectData1.setName("Project Service1");
        projectData1.setDescription("Hello1");

        ProjectData projectData2 = new ProjectData();
        projectData2.setName("Project Service2");
        projectData2.setDescription("Hello2");

        // Create 2 projects by user
        projectData1 = this.lookupProjectServiceWSClientWithUserRole().createProject(projectData1);
        projectData2 = this.lookupProjectServiceWSClientWithUserRole().createProject(projectData2);

        // Only administrator can get projects for specific user.
        // The user id is 1. See /test_files/lib/mock.jar/MockUserGroupManager.
        List < ProjectData > projectDatas = this.lookupProjectServiceWSClientWithAdminRole().getProjectsForUser(1L);

        // Verify the result
        assertNotNull("Null is not expected.", projectDatas);
        assertEquals("The return list size is incorrect.", 2, projectDatas.size());
        assertTrue("There should be 2 distinct projects retrieved",
            projectDatas.get(0).getProjectId().longValue() != projectDatas.get(1).getProjectId().longValue());

        this.assertProjectData(projectData1, projectDatas.get(0));
        this.assertProjectData(projectData2, projectDatas.get(1));

        // Delete project by user
        this.lookupProjectServiceWSClientWithUserRole().deleteProject(projectDatas.get(0).getProjectId());

        // Delete project by administrator
        this.lookupProjectServiceWSClientWithAdminRole().deleteProject(projectDatas.get(1).getProjectId());
    }

    /**
     * <p>
     * Accuracy test for <code>{@link ProjectServiceBean#getAllProjects()}</code> method.
     * </p>
     *
     * <p>
     * If the login user is not administrator, should return its own projects.
     * </p>
     *
     * @throws Exception
     *             pass any unexpected exception to JUnit.
     */
    public void testGetAllProjects_ByUser() throws Exception {
        ProjectData projectData1 = new ProjectData();
        projectData1.setName("Project Service1");
        projectData1.setDescription("Hello1");

        ProjectData projectData2 = new ProjectData();
        projectData2.setName("Project Service2");
        projectData2.setDescription("Hello2");

        ProjectData projectData3 = new ProjectData();
        projectData3.setName("Project Service2");
        projectData3.setDescription("Hello2");

        // Create 2 projects by user
        projectData1 = this.lookupProjectServiceWSClientWithUserRole().createProject(projectData1);
        projectData2 = this.lookupProjectServiceWSClientWithUserRole().createProject(projectData2);

        // Create 1 project by administrator
        projectData3 = this.lookupProjectServiceWSClientWithAdminRole().createProject(projectData3);

        // Get all projects by user
        List < ProjectData > projectDatas = this.lookupProjectServiceWSClientWithUserRole().getAllProjects();

        // Verify the result
        assertNotNull("Null is not expected.", projectDatas);
        assertEquals("The return list size is incorrect.", 2, projectDatas.size());
        assertTrue("There should be 2 distinct projects retrieved",
            projectDatas.get(0).getProjectId().longValue() != projectDatas.get(1).getProjectId().longValue());

        this.assertProjectData(projectData1, projectDatas.get(0));
        this.assertProjectData(projectData2, projectDatas.get(1));

        // Delete project by user
        this.lookupProjectServiceWSClientWithUserRole().deleteProject(projectDatas.get(0).getProjectId());

        // Delete project by user
        this.lookupProjectServiceWSClientWithUserRole().deleteProject(projectDatas.get(1).getProjectId());

        // Delete project by administrator
        this.lookupProjectServiceWSClientWithAdminRole().deleteProject(projectData3.getProjectId());
    }

    /**
     * <p>
     * Accuracy test for <code>{@link ProjectServiceBean#getAllProjects()}</code> method.
     * </p>
     *
     * <p>
     * If the login user is not administrator, should return its own projects. If the user does not have
     * any project, an empty list should be returned.
     * </p>
     *
     * @throws Exception
     *             pass any unexpected exception to JUnit.
     */
    public void testGetAllProjects_ByUser_EmptyResult() throws Exception {

        ProjectData projectData3 = new ProjectData();
        projectData3.setName("Project Service2");
        projectData3.setDescription("Hello2");

        // Create 1 project by administrator
        projectData3 = this.lookupProjectServiceWSClientWithAdminRole().createProject(projectData3);

        // Get all projects by user
        List < ProjectData > projectDatas = this.lookupProjectServiceWSClientWithUserRole().getAllProjects();

        // Verify the result
        assertNotNull("Null is not expected.", projectDatas);
        assertEquals("The return list size is incorrect.", 0, projectDatas.size());

        // Delete project by administrator
        this.lookupProjectServiceWSClientWithAdminRole().deleteProject(projectData3.getProjectId());
    }

    /**
     * <p>
     * Accuracy test for <code>{@link ProjectServiceBean#getAllProjects()}</code> method.
     * </p>
     *
     * <p>
     * If the login user is administrator, should return all projects.
     * </p>
     *
     * @throws Exception
     *             pass any unexpected exception to JUnit.
     */
    public void testGetAllProjects_ByAdmin() throws Exception {
        ProjectData projectData1 = new ProjectData();
        projectData1.setName("Project Service1");
        projectData1.setDescription("Hello1");

        ProjectData projectData2 = new ProjectData();
        projectData2.setName("Project Service2");
        projectData2.setDescription("Hello2");

        ProjectData projectData3 = new ProjectData();
        projectData3.setName("Project Service2");
        projectData3.setDescription("Hello2");

        // Create 2 projects by user
        projectData1 = this.lookupProjectServiceWSClientWithUserRole().createProject(projectData1);
        projectData2 = this.lookupProjectServiceWSClientWithUserRole().createProject(projectData2);

        // Create 1 project by administrator
        projectData3 = this.lookupProjectServiceWSClientWithAdminRole().createProject(projectData3);

        // Get all projects by administrator
        List < ProjectData > projectDatas = this.lookupProjectServiceWSClientWithAdminRole().getAllProjects();

        // Verify the result
        assertNotNull("Null is not expected.", projectDatas);
        final int count = 3;
        assertEquals("The return list size is incorrect.", count, projectDatas.size());

        this.assertProjectData(projectData1, projectDatas.get(0));
        this.assertProjectData(projectData2, projectDatas.get(1));
        this.assertProjectData(projectData3, projectDatas.get(2));

        // Delete project by user
        this.lookupProjectServiceWSClientWithUserRole().deleteProject(projectDatas.get(0).getProjectId());

        // Delete project by user
        this.lookupProjectServiceWSClientWithUserRole().deleteProject(projectDatas.get(1).getProjectId());

        // Delete project by administrator
        this.lookupProjectServiceWSClientWithAdminRole().deleteProject(projectDatas.get(2).getProjectId());
    }

    /**
     * <p>
     * Accuracy test for <code>{@link ProjectServiceBean#getAllProjects()}</code> method.
     * </p>
     *
     * <p>
     * If the login user is administrator, should return all projects. If there is no project in persistence,
     * an empty ist should be returned.
     * </p>
     *
     * @throws Exception
     *             pass any unexpected exception to JUnit.
     */
    public void testGetAllProjects_ByAdmin_EmptyResult() throws Exception {
        // Get all projects by administrator
        List < ProjectData > projectDatas = this.lookupProjectServiceWSClientWithAdminRole().getAllProjects();

        // Verify the result
        assertNotNull("Null is not expected.", projectDatas);
        assertEquals("The return list size is incorrect.", 0, projectDatas.size());
    }

    /**
     * <p>
     * Accuracy test for <code>{@link ProjectServiceBean#updateProject(ProjectData)}</code> method.
     * </p>
     *
     * <p>
     * Update the project by user.
     * </p>
     *
     * @throws Exception
     *             pass any unexpected exception to JUnit.
     */
    public void testUpdateProject_ByUser() throws Exception {
        ProjectData projectData = new ProjectData();
        projectData.setName("Project Service");
        projectData.setDescription("Hello");

        // Create a project by user
        ProjectData newPrjData = this.lookupProjectServiceWSClientWithUserRole().createProject(projectData);

        // Modify the name and description
        newPrjData.setName("new name");
        newPrjData.setDescription("new description");

        // Update the created project by user
        this.lookupProjectServiceWSClientWithUserRole().updateProject(newPrjData);

        // Get the updated project by user
        ProjectData updated = this.lookupProjectServiceWSClientWithUserRole().getProject(newPrjData.getProjectId());

        // Verify the result
        this.assertProjectData(newPrjData, updated);

        // Delete the created project by user
        this.lookupProjectServiceWSClientWithUserRole().deleteProject(updated.getProjectId());
    }

    /**
     * <p>
     * Accuracy test for <code>{@link ProjectServiceBean#updateProject(ProjectData)}</code> method.
     * </p>
     *
     * <p>
     * Update the project by administrator.
     * </p>
     *
     * @throws Exception
     *             pass any unexpected exception to JUnit.
     */
    public void testUpdateProject_ByAdmin() throws Exception {
        ProjectData projectData = new ProjectData();
        projectData.setName("Project Service");
        projectData.setDescription("Hello");

        // Create a project by administrator
        ProjectData newPrjData = this.lookupProjectServiceWSClientWithAdminRole().createProject(projectData);

        // Modify the name and description
        newPrjData.setName("new name");
        newPrjData.setDescription("new description");

        // Update the created project by administrator
        this.lookupProjectServiceWSClientWithAdminRole().updateProject(newPrjData);

        // Get the updated project by administrator
        ProjectData updated = this.lookupProjectServiceWSClientWithAdminRole().getProject(newPrjData.getProjectId());

        // Verify the result
        this.assertProjectData(newPrjData, updated);

        // Delete the created project by administrator
        this.lookupProjectServiceWSClientWithAdminRole().deleteProject(updated.getProjectId());
    }

    /**
     * <p>
     * Accuracy test for <code>{@link ProjectServiceBean#updateProject(ProjectData)}</code> method.
     * </p>
     *
     * <p>
     * The administrator can update any project.
     * </p>
     *
     * @throws Exception
     *             pass any unexpected exception to JUnit.
     */
    public void testUpdateProject_AdminCanUpdateAnyProject() throws Exception {
        ProjectData projectData = new ProjectData();
        projectData.setName("Project Service");
        projectData.setDescription("Hello");

        // Create a project by user
        ProjectData newPrjData = this.lookupProjectServiceWSClientWithUserRole().createProject(projectData);

        // Modify the name and description
        newPrjData.setName("new name");
        newPrjData.setDescription("new description");

        // Update the created project by administrator
        this.lookupProjectServiceWSClientWithAdminRole().updateProject(newPrjData);

        // Get the updated project by administrator
        ProjectData updated = this.lookupProjectServiceWSClientWithAdminRole().getProject(newPrjData.getProjectId());

        // Verify the result
        this.assertProjectData(newPrjData, updated);

        // Delete the created project by administrator
        this.lookupProjectServiceWSClientWithAdminRole().deleteProject(updated.getProjectId());
    }

    /**
     * <p>
     * Accuracy test for <code>{@link ProjectServiceBean#deleteProject(long)}</code> method.
     * </p>
     *
     * <p>
     * Delete project by user.
     * </p>
     *
     * @throws Exception
     *             pass any unexpected exception to JUnit.
     */
    public void testDeleteProject_ByUser() throws Exception {
        ProjectData projectData = new ProjectData();
        projectData.setName("Project Service");
        projectData.setDescription("Hello");

        // Create a project by user
        ProjectData newPrjData = this.lookupProjectServiceWSClientWithUserRole().createProject(projectData);

        // Delete the created project by user
        assertTrue("Project should be deleted.",
            this.lookupProjectServiceWSClientWithUserRole().deleteProject(newPrjData.getProjectId()));
    }

    /**
     * <p>
     * Accuracy test for <code>{@link ProjectServiceBean#deleteProject(long)}</code> method.
     * </p>
     *
     * <p>
     * Delete project by administrator.
     * </p>
     *
     * @throws Exception
     *             pass any unexpected exception to JUnit.
     */
    public void testDeleteProject_ByAdmin() throws Exception {
        ProjectData projectData = new ProjectData();
        projectData.setName("Project Service");
        projectData.setDescription("Hello");

        // Create a project by administrator
        ProjectData newPrjData = this.lookupProjectServiceWSClientWithAdminRole().createProject(projectData);

        // Delete the created project by administrator
        assertTrue("Project should be deleted.",
            this.lookupProjectServiceWSClientWithAdminRole().deleteProject(newPrjData.getProjectId()));
    }

    /**
     * <p>
     * Accuracy test for <code>{@link ProjectServiceBean#deleteProject(long)}</code> method.
     * </p>
     *
     * <p>
     * Delete project by administrator.
     * </p>
     *
     * @throws Exception
     *             pass any unexpected exception to JUnit.
     */
    public void testDeleteProject_AdminCanDeleteAnyProject() throws Exception {
        ProjectData projectData = new ProjectData();
        projectData.setName("Project Service");
        projectData.setDescription("Hello");

        // Create a project by user
        ProjectData newPrjData = this.lookupProjectServiceWSClientWithUserRole().createProject(projectData);

        // Delete the created project by administrator
        assertTrue("Project should be deleted.",
            this.lookupProjectServiceWSClientWithAdminRole().deleteProject(newPrjData.getProjectId()));
    }

    /**
     * <p>
     * Accuracy test for <code>{@link ProjectServiceBean#deleteProject(long)}</code> method.
     * </p>
     *
     * <p>
     * If the project to delete does not exist, should return false.
     * </p>
     *
     * @throws Exception
     *             pass any unexpected exception to JUnit.
     */
    public void testDeleteProject_NotFound() throws Exception {
        assertFalse("Should return false.",
            this.lookupProjectServiceWSClientWithUserRole().deleteProject(Long.MAX_VALUE));
    }
}
