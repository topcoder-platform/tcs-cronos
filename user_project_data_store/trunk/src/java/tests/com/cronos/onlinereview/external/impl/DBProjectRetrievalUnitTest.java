/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 *
 * User Project Data Store 1.0
 */
package com.cronos.onlinereview.external.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.cronos.onlinereview.external.ConfigException;
import com.cronos.onlinereview.external.ExternalProject;
import com.cronos.onlinereview.external.RetrievalException;
import com.cronos.onlinereview.external.UnitTestHelper;
import com.topcoder.db.connectionfactory.DBConnectionFactory;
import com.topcoder.db.connectionfactory.DBConnectionFactoryImpl;

import junit.framework.TestCase;

/**
 * <p>Tests the DBProjectRetrieval class.</p>
 *
 * @author oodinary
 * @version 1.0
 */
public class DBProjectRetrievalUnitTest extends TestCase {

    /**
     * <p>Represents the configuration file.</p>
     */
    private static final String CONFIG_FILE = "SampleConfig.xml";

    /**
     * <p>The name of the namespace that the calling program can populate which contains
     * DBConnectionFactory and other configuration values.</p>
     */
    private static final String NAMESPACE = "com.cronos.onlinereview.external";

    /**
     * <p>The default ConnName which is defined in the configure file.</p>
     */
    private static final String defaultConnName = "UserProjectDataStoreConnection";

    /**
     * <p>The default forum id.</p>
     */
    private static final int defaultForumId = 10;

    /**
     * <p>The default DB connection factory.</p>
     */
    private DBConnectionFactory defaultConnFactory = null;

    /**
     * <p>The default connection used for db operations.</p>
     */
    private Connection defaultConnection = null;

    /**
     * <p>An DBProjectRetrieval instance for testing.</p>
     */
    private MockDBProjectRetrieval defaultDBProjectRetrieval = null;

    /**
     * <p>Initialization.</p>
     *
     * @throws Exception to JUnit.
     */
    protected void setUp() throws Exception {
        UnitTestHelper.addConfig(CONFIG_FILE);

        defaultDBProjectRetrieval = new MockDBProjectRetrieval(NAMESPACE);

        // Retrieves Connection and DBConnectionFactory.
        defaultConnection = defaultDBProjectRetrieval.getConnection();
        defaultConnFactory = new DBConnectionFactoryImpl(NAMESPACE);

        // Inserts.
        UnitTestHelper.insertIntoComponentCataLog(defaultConnection);
        UnitTestHelper.insertIntoComponentVersions(defaultConnection);
        UnitTestHelper.insertIntoCompForumXref(defaultConnection);
    }

    /**
     * <p>Set defaultDBProjectRetrieval to null.</p>
     *
     * @throws Exception to JUnit.
     */
    protected void tearDown() throws Exception {

        // Cleans up.
        UnitTestHelper.cleanupTable(defaultConnection, "comp_forum_xref");
        UnitTestHelper.cleanupTable(defaultConnection, "comp_versions");
        UnitTestHelper.cleanupTable(defaultConnection, "comp_catalog");

        // Sets connection and defaultDBProjectRetrieval to initial values.
        defaultConnection.close();
        defaultDBProjectRetrieval = null;

        UnitTestHelper.clearConfig();
    }

    /**
     * <p>Tests the accuracy of the ctor(String).</p>
     * <p>The defaultDBProjectRetrieval instance should be created successfully.</p>
     */
    public void testCtor_String() {

        assertNotNull("DBProjectRetrieval should be accurately created.", defaultDBProjectRetrieval);
        assertTrue("defaultDBProjectRetrieval should be instance of DBProjectRetrieval.",
                defaultDBProjectRetrieval instanceof DBProjectRetrieval);

        // Uses the reflection to test the field setting.
        Object connFactory = UnitTestHelper.getPrivateField(BaseDBRetrieval.class,
                defaultDBProjectRetrieval, "connFactory");
        Object connName = UnitTestHelper.getPrivateField(BaseDBRetrieval.class,
                defaultDBProjectRetrieval, "connName");
        Object forumType = UnitTestHelper.getPrivateField(DBProjectRetrieval.class,
                defaultDBProjectRetrieval, "forumType");

        // Asserts the set.
        assertNotNull("connFactory should be set correctly.", connFactory);
        assertEquals("connName should be set correctly.", defaultConnName, connName);
        assertEquals("forumType should be set correctly.", new Integer(5), forumType);
    }

    /**
     * <p>Tests the failure of the ctor(String).</p>
     * <p>If the parameter is null. Then IllegalArgumentException should be thrown.</p>
     *
     * @throws ConfigException this exception would never be thrown in this test case.
     */
    public void testCtor_String_NullNamespace()
        throws ConfigException {

        try {
            new MockDBProjectRetrieval(null);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // Success.
        }
    }

    /**
     * <p>Tests the failure of the ctor(String).</p>
     * <p>If the parameter is empty after trimed. Then IllegalArgumentException should be thrown.</p>
     *
     * @throws ConfigException this exception would never be thrown in this test case.
     */
    public void testCtor_String_EmptyNamespace()
        throws ConfigException {

        try {
            new MockDBProjectRetrieval("  ");
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // Success.
        }
    }

    /**
     * <p>Tests the failure of the ctor(String).</p>
     * <p>The connName which is got from the configure file, is not contained in the connFactoryImpl. Then
     * ConfigException should be thrown.</p>
     */
    public void testCtor_String_ConnNameNotInclude() {

        try {
            UnitTestHelper.addConfig("FailureConfig.xml");
        } catch (Exception e) {
            // Will never happen.
        }

        try {
            new MockDBProjectRetrieval("com.cronos.onlinereview.external.connNameNotInclude");
            fail("ConfigException should be thrown.");
        } catch (ConfigException e) {
            // Success.
        }
    }

    /**
     * <p>Tests the failure of the ctor(String).</p>
     * <p>The connName which is got from the configure file, is defined empty. Then ConfigException should be
     * thrown.</p>
     */
    public void testCtor_String_ConnNameEmpty() {

        try {
            UnitTestHelper.addConfig("FailureConfig.xml");
        } catch (Exception e) {
            // Will never happen.
        }

        try {
            new MockDBProjectRetrieval("com.cronos.onlinereview.external.connNameEmpty");
            fail("ConfigException should be thrown.");
        } catch (ConfigException e) {
            // Success.
        }
    }

    /**
     * <p>Tests the failure of the ctor(String).</p>
     * <p>If gives an unknow namespace. Then ConfigException should be thrown.</p>
     */
    public void testCtor_String_UnknowNamespace() {

        try {
            new MockDBProjectRetrieval("Unknow");
            fail("ConfigException should be thrown.");
        } catch (ConfigException e) {
            // Success.
        }
    }

    /**
     * <p>Tests the failure of the ctor(String).</p>
     * <p>If the forumType is defined not as a integer, ConfigException should be thrown.</p>
     */
    public void testCtor_String_ForumTypeParseError() {

        try {
            UnitTestHelper.addConfig("FailureConfig.xml");
        } catch (Exception e) {
            // Will never happen.
        }

        try {
            new MockDBProjectRetrieval("com.cronos.onlinereview.external.ForumTypeParseError");
            fail("ConfigException should be thrown.");
        } catch (ConfigException e) {
            // Success.
        }
    }

    /**
     * <p>Tests the failure of the ctor(String).</p>
     * <p>If the forumType is defined negative, ConfigException should be thrown.</p>
     */
    public void testCtor_String_ForumTypeNegative() {

        try {
            UnitTestHelper.addConfig("FailureConfig.xml");
        } catch (Exception e) {
            // Will never happen.
        }

        try {
            new MockDBProjectRetrieval("com.cronos.onlinereview.external.ForumTypeNegative");
            fail("ConfigException should be thrown.");
        } catch (ConfigException e) {
            // Success.
        }
    }

    /**
     * <p>Tests the accuracy of the ctor(DBConnectionFactory, String, int).</p>
     * <p>The instance should be created successfully.</p>
     *
     * @throws ConfigException this exception would never be thrown in this test case.
     */
    public void testCtor_DBConnectionFactoryStringInt()
        throws ConfigException {

        defaultDBProjectRetrieval = new MockDBProjectRetrieval(defaultConnFactory, defaultConnName,
                defaultForumId);

        assertNotNull("DBProjectRetrieval should be accurately created.", defaultDBProjectRetrieval);
        assertTrue("defaultDBProjectRetrieval should be instance of DBProjectRetrieval.",
                defaultDBProjectRetrieval instanceof DBProjectRetrieval);

        // Uses the reflection to test the field setting.
        Object connFactory = UnitTestHelper.getPrivateField(BaseDBRetrieval.class,
                defaultDBProjectRetrieval, "connFactory");
        Object connName = UnitTestHelper.getPrivateField(BaseDBRetrieval.class,
                defaultDBProjectRetrieval, "connName");
        Object forumType = UnitTestHelper.getPrivateField(DBProjectRetrieval.class,
                defaultDBProjectRetrieval, "forumType");

        // Asserts the set.
        assertEquals("connFactory should be set correctly.", defaultConnFactory, connFactory);
        assertEquals("connName should be set correctly.", defaultConnName, connName);
        assertEquals("forumType should be set correctly.", new Integer(defaultForumId), forumType);
    }

    /**
     * <p>Tests the failure of the ctor(DBConnectionFactory, String, int).</p>
     * <p>If DBConnectionFactory given is null, IllegalArgumentException should be thrown.</p>
     *
     * @throws ConfigException this exception would never be thrown in this test case.
     */
    public void testCtor_DBConnectionFactoryStringInt_NullDBConnectionFactory()
        throws ConfigException {

        try {
            new MockDBProjectRetrieval(null, defaultConnName, defaultForumId);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // Success.
        }
    }

    /**
     * <p>Tests the failure of the ctor(DBConnectionFactory, String, int).</p>
     * <p>If connName given is null, IllegalArgumentException should be thrown.</p>
     *
     * @throws ConfigException this exception would never be thrown in this test case.
     */
    public void testCtor_DBConnectionFactoryStringInt_NullConnName()
        throws ConfigException {

        try {
            new MockDBProjectRetrieval(defaultConnFactory, null, defaultForumId);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // Success.
        }
    }

    /**
     * <p>Tests the failure of the ctor(DBConnectionFactory, String, int).</p>
     * <p>If connName doesn't correspond to a connection the factory knows about, ConfigException
     * should be thrown.</p>
     */
    public void testCtor_DBConnectionFactoryStringInt_UnknownConnName() {

        try {
            new MockDBProjectRetrieval(defaultConnFactory, "UnknownConnName", defaultForumId);
            fail("ConfigException should be thrown.");
        } catch (ConfigException e) {
            // Success.
        }
    }

    /**
     * <p>Tests the failure of the ctor(DBConnectionFactory, String, int).</p>
     * <p>If the forumType is defined negative, IllegalArgumentException should be thrown.</p>
     *
     * @throws ConfigException this exception would never be thrown in this test case.
     */
    public void testCtor_DBConnectionFactoryStringInt_ForumTypeNegative()
        throws ConfigException {

        try {
            new MockDBProjectRetrieval(defaultConnFactory, defaultConnName, -1);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // Success.
        }
    }

    /**
     * <p>Tests the accuracy of the retrieveProjects(long[]).</p>
     *
     * @throws RetrievalException this exception would never be thrown in this test case.
     */
    public void testRetrieveProjects_LongArray()
        throws RetrievalException {

        ExternalProject[] projects = defaultDBProjectRetrieval.retrieveProjects(new long[] { 1, 2, 4 });

        // Asserts.
        assertEquals("Three records would be got.", 3, projects.length);
    }

    /**
     * <p>Tests the failure of the retrieveProjects(long[]).</p>
     * <p>If ids is null, IllegalArgumentException should be thrown.</p>
     *
     * @throws RetrievalException this exception would never be thrown in this test case.
     */
    public void testRetrieveProjects_LongArray_NullIds()
        throws RetrievalException {

        try {
            defaultDBProjectRetrieval.retrieveProjects(null);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // Success.
        }
    }

    /**
     * <p>Tests the failure of the retrieveProjects(long[]).</p>
     * <p>If any entry is not positive in ids, IllegalArgumentException should be thrown.</p>
     *
     * @throws RetrievalException this exception would never be thrown in this test case.
     */
    public void testRetrieveProjects_LongArray_NotPositiveEntry()
        throws RetrievalException {

        try {
            defaultDBProjectRetrieval.retrieveProjects(new long[] { 1, 0, 4 });
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // Success.
        }
    }

    /**
     * <p>Tests the failure of the retrieveProjects(long[]).</p>
     * <p>If there is no default Connection and connName, connection could not be created, RetrievalException
     * would be thrown.</p>
     */
    public void testRetrieveProjects_LongArray_NoDefaultConnectionAndConnName() {

        try {
            UnitTestHelper.addConfig("FailureConfig.xml");
        } catch (Exception e) {
            // Will never happen.
        }

        try {
            defaultDBProjectRetrieval = new MockDBProjectRetrieval
                ("com.cronos.onlinereview.external.NoDefaultConnAndConnName");
        } catch (ConfigException e) {
            // Will never happen.
        }

        try {
            defaultDBProjectRetrieval.retrieveProjects(new long[] { 1, 2, 4 });
            fail("RetrievalException should be thrown.");
        } catch (RetrievalException e1) {
            // Success.
        }
    }

    /**
     * <p>Tests the accuracy of the retrieveProjects(long).</p>
     *
     * @throws RetrievalException this exception would never be thrown in this test case.
     */
    public void testRetrieveProject_Long1()
        throws RetrievalException {

        ExternalProject project = defaultDBProjectRetrieval.retrieveProject(2);

        // Asserts.
        assertEquals("The catalog id should be the same.", 2, project.getCatalogId());
        assertEquals("The comment should be the same.", "Average", project.getComments());
        assertEquals("The component id should be the same.", 1002, project.getComponentId());
        assertEquals("The description should be the same.", "Second Project B", project.getDescription().trim());
        assertEquals("The forum id should be the same.", 100002, project.getForumId());
        assertEquals("The id should be the same.", 2, project.getId());
        assertEquals("The name should be the same.", "Project B", project.getName().trim());
        assertEquals("The version string should be the same.", "Version 2", project.getVersion().trim());
        assertEquals("The version id should be the same.", 2, project.getVersionId());
    }

    /**
     * <p>Tests the accuracy of the retrieveProjects(long).</p>
     * <p>Due to the outer join, some value will got null here, but the null value would be set into integer,
     * so it turns to zero.</p>
     *
     * @throws RetrievalException this exception would never be thrown in this test case.
     */
    public void testRetrieveProject_Long2()
        throws RetrievalException {

        ExternalProject project = defaultDBProjectRetrieval.retrieveProject(4);

        // Asserts.
        assertEquals("The catalog id should be the same.", 4, project.getCatalogId());
        assertEquals("The comment should be the same.", "Bad", project.getComments());
        assertEquals("The component id should be the same.", 1004, project.getComponentId());
        assertEquals("The description should be the same.", "Fourth Project D", project.getDescription().trim());
        assertEquals("The id should be the same.", 4, project.getId());
        assertEquals("The name should be the same.", "Project D", project.getName().trim());
        assertEquals("The version string should be the same.", "Version 3", project.getVersion().trim());
        assertEquals("The version id should be the same.", 3, project.getVersionId());

        // Notice here, as the outer join.
        assertEquals("The forum id should be the same.", -1, project.getForumId());
    }

    /**
     * <p>Tests the failure of the retrieveProject(long).</p>
     * <p>If id is not positive, IllegalArgumentException should be thrown.</p>
     *
     * @throws RetrievalException this exception would never be thrown in this test case.
     */
    public void testRetrieveProject_Long_NotPositive()
        throws RetrievalException {

        try {
            defaultDBProjectRetrieval.retrieveProject(0);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // Success.
        }
    }

    /**
     * <p>Tests the accuracy of the retrieveProjects(String[], String[]).</p>
     *
     * @throws RetrievalException this exception would never be thrown in this test case.
     */
    public void testRetrieveProjects_StringArrayStringArray()
        throws RetrievalException {

        ExternalProject[] projects = defaultDBProjectRetrieval.retrieveProjects(
                new String[] { "Project A", "Project C" },
                new String[] { "Version 1", "Version 2" });

        // Asserts.
        assertEquals("One records would be got.", 1, projects.length);
    }

    /**
     * <p>Tests the failure of the retrieveProjects(String[], String[]).</p>
     * <p>If either array is null, IllegalArgumentException should be thrown.</p>
     *
     * @throws RetrievalException this exception would never be thrown in this test case.
     */
    public void testRetrieveProjects_StringArrayStringArray_NullArray()
        throws RetrievalException {

        try {
            defaultDBProjectRetrieval.retrieveProjects(null, new String[] { "Version 2" });
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // Success.
        }
    }

    /**
     * <p>Tests the failure of the retrieveProjects(String[], String[]).</p>
     * <p>If any entry in either array is null, IllegalArgumentException should be thrown.</p>
     *
     * @throws RetrievalException this exception would never be thrown in this test case.
     */
    public void testRetrieveProjects_StringArrayStringArray_NullEntry()
        throws RetrievalException {

        try {
            defaultDBProjectRetrieval.retrieveProjects(
                    new String[] { "Project A", "Project C" },
                    new String[] { "Version 1", null });
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // Success.
        }
    }

    /**
     * <p>Tests the failure of the retrieveProjects(String[], String[]).</p>
     * <p>If any entry in either array is empty, IllegalArgumentException should be thrown.</p>
     *
     * @throws RetrievalException this exception would never be thrown in this test case.
     */
    public void testRetrieveProjects_StringArrayStringArray_EmptyEntry()
        throws RetrievalException {

        try {
            defaultDBProjectRetrieval.retrieveProjects(
                    new String[] { "Project A", "   " },
                    new String[] { "Version 1", "Version 2" });
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // Success.
        }
    }

    /**
     * <p>Tests the failure of the retrieveProjects(String[], String[]).</p>
     * <p>If the sizes of these two array are not the same, IllegalArgumentException should be thrown.</p>
     *
     * @throws RetrievalException this exception would never be thrown in this test case.
     */
    public void testRetrieveProjects_StringArrayStringArray_NotSameSize()
        throws RetrievalException {

        try {
            defaultDBProjectRetrieval.retrieveProjects(
                    new String[] { "Project A" },
                    new String[] { "Version 1", "Version 2" });
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // Success.
        }
    }

    /**
     * <p>Tests the failure of the retrieveProjects(String[], String[]).</p>
     * <p>If there is no default Connection and connName, connection could not be created, RetrievalException
     * would be thrown.</p>
     */
    public void testRetrieveProjects_StringArrayStringArray_NoDefaultConnectionAndConnName() {

        try {
            UnitTestHelper.addConfig("FailureConfig.xml");
        } catch (Exception e) {
            // Will never happen.
        }

        try {
            defaultDBProjectRetrieval = new MockDBProjectRetrieval
                ("com.cronos.onlinereview.external.NoDefaultConnAndConnName");
        } catch (ConfigException e) {
            // Will never happen.
        }

        try {
            defaultDBProjectRetrieval.retrieveProjects(
                    new String[] { "Project A", "Project C" },
                    new String[] { "Version 1", "Version 2" });
            fail("RetrievalException should be thrown.");
        } catch (RetrievalException e1) {
            // Success.
        }
    }

    /**
     * <p>Tests the accuracy of the retrieveProjects(String, String).</p>
     *
     * @throws RetrievalException this exception would never be thrown in this test case.
     */
    public void testRetrieveProjects_StringString()
        throws RetrievalException {

        ExternalProject[] projects = defaultDBProjectRetrieval.retrieveProject("Project A", "Version 1");

        // Asserts.
        assertEquals("One records would be got.", 1, projects.length);

        ExternalProject project = projects[0];
        assertEquals("The catalog id should be the same.", 1, project.getCatalogId());
        assertEquals("The comment should be the same.", "Good", project.getComments());
        assertEquals("The component id should be the same.", 1001, project.getComponentId());
        assertEquals("The description should be the same.", "First Project A", project.getDescription().trim());
        assertEquals("The forum id should be the same.", 100001, project.getForumId());
        assertEquals("The id should be the same.", 1, project.getId());
        assertEquals("The name should be the same.", "Project A", project.getName().trim());
        assertEquals("The version string should be the same.", "Version 1", project.getVersion().trim());
        assertEquals("The version id should be the same.", 1, project.getVersionId());
    }

    /**
     * <p>Tests the failure of the retrieveProject(String, String).</p>
     * <p>If either argument is null, IllegalArgumentException should be thrown.</p>
     *
     * @throws RetrievalException this exception would never be thrown in this test case.
     */
    public void testRetrieveProject_StringString_Null()
        throws RetrievalException {

        try {
            defaultDBProjectRetrieval.retrieveProject(null, "Version 1");
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // Success.
        }
    }

    /**
     * <p>Tests the failure of the retrieveProject(String, String).</p>
     * <p>If either argument is empty, IllegalArgumentException should be thrown.</p>
     *
     * @throws RetrievalException this exception would never be thrown in this test case.
     */
    public void testRetrieveProject_StringString_Empty()
        throws RetrievalException {

        try {
            defaultDBProjectRetrieval.retrieveProject("Project A", "   ");
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // Success.
        }
    }

    /**
     * <p>Tests the accuracy of the createObject(ResultSet).</p>
     *
     * @throws RetrievalException this exception would never be thrown in this test case.
     * @throws SQLException this exception would never be thrown in this test case.
     */
    public void testCreateObject_ResultSet()
        throws RetrievalException, SQLException {

        // Prepares the ps and rs.
        PreparedStatement ps = defaultConnection.prepareStatement("SELECT cv.comp_vers_id, "
                + "cv.component_id, "
                + "version, "
                + "version_text, "
                + "comments, "
                + "component_name, "
                + "description, "
                + "cc.root_category_id category_id, "
                + "forum_id "
                + "FROM comp_versions cv, comp_catalog cc, OUTER comp_forum_xref f "
                + "WHERE cv.component_id = cc.component_id "
                + "and cv.comp_vers_id = f.comp_vers_id "
                + "and f.forum_type = 5 and cv.comp_vers_id = 2");
        ResultSet rs = ps.executeQuery();

        // Creates object.
        ExternalProject project = null;
        while (rs.next()) {
            project = (ExternalProject) defaultDBProjectRetrieval.createObject(rs);
        }

        // Asserts.
        assertEquals("The catalog id should be the same.", 2, project.getCatalogId());
        assertEquals("The comment should be the same.", "Average", project.getComments());
        assertEquals("The component id should be the same.", 1002, project.getComponentId());
        assertEquals("The description should be the same.", "Second Project B", project.getDescription().trim());
        assertEquals("The forum id should be the same.", 100002, project.getForumId());
        assertEquals("The id should be the same.", 2, project.getId());
        assertEquals("The name should be the same.", "Project B", project.getName().trim());
        assertEquals("The version string should be the same.", "Version 2", project.getVersion().trim());
        assertEquals("The version id should be the same.", 2, project.getVersionId());
    }
}
