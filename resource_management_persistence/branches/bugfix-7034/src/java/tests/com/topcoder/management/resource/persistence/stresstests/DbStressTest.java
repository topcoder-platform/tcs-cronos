/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.resource.persistence.stresstests;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Iterator;

import junit.framework.TestCase;

import com.topcoder.db.connectionfactory.DBConnectionFactory;
import com.topcoder.db.connectionfactory.DBConnectionFactoryImpl;
import com.topcoder.util.config.ConfigManager;

/**
 * This is a base(helper) class for stress tests.
 *
 * @author kinfkong
 * @version 1.0
 */
public class DbStressTest extends TestCase {

    /**
     * The config file of DBConnectionFactory.
     */
    public static final String DBCONFIG =
        "test_files" + File.separator + "stresstests" + File.separator + "dbconfig.xml";

    /**
     * The namespace of the DBConnectionFactory.
     */
    public static final String DB_NAMESPACE = "com.topcoder.db.connectionfactory.DBConnectionFactoryImpl";

    /**
     * The times that the stress tests runs.
     */
    public static final int STRESS_TEST_NUM = 10;

    /**
     * The table names in this component.
     */
    public  String[] tableNames = {
        "notification",
        "notification_type_lu",
        "resource_submission",
        "resource_info",
        "resource_info_type_lu",
        "resource",
        "resource_role_lu",
        "submission",
        "project_phase",
        "phase_type_lu",
        "project"
    };

    /**
     * Sets up the environments.
     *
     * @throws Exception to JUnit
     */
    public void setUp() throws Exception {
        // load the configuration
        loadConfig();

        // clear the previous records first
        clearTables();

        // insert the fixed records
        insertFixedRecords();
    }

    /**
     * Clears all the test environments.
     *
     * @throws Exception to JUnit
     */
    public void tearDown() throws Exception {
        clearTables();
        clearNamespaces();
    }

    /**
     * Loads the configuration files to ConfigManager.
     *
     * @throws Exception to JUnit
     */
    private void loadConfig() throws Exception {
        // load the config file
        File file = new File(DBCONFIG);

        // load it to the ConfigManager
        ConfigManager cm = ConfigManager.getInstance();
        cm.add(file.getCanonicalPath());
    }

    /**
     * Clears all the namespaces in the ConfigManager.
     *
     * @throws Exception to JUnit
     */
    private void clearNamespaces() throws Exception {
        ConfigManager cm = ConfigManager.getInstance();

        // iterator through to clear the namespace
        for (Iterator it = cm.getAllNamespaces(); it.hasNext();) {

            String namespace = (String) it.next();

            if (cm.existsNamespace(namespace)) {

                // removes the namespace that exists
                cm.removeNamespace(namespace);
            }
        }
    }

    /**
     * Clears the tables in the database.
     *
     * @throws Exception to JUnit
     */
    private void clearTables() throws Exception {
        for (int i = 0; i < tableNames.length; i++) {
            String sql = "DELETE FROM " + tableNames[i];
            // update the database
            doSQLUpdate(sql, new Object[] {});
        }
    }

    /**
     * Does the update operation to the database.
     *
     * @param sql the SQL statement to be executed
     * @param values the values that to set in the statement
     *
     * @throws Exception to JUnit
     */
    protected void doSQLUpdate(String sql, Object[] values) throws Exception {
        // gets the connection
        Connection con = getConnection();

        // creates the prepared statement
        PreparedStatement ps = con.prepareStatement(sql);

        // set the values to the prepared statement
        for (int i = 0; i < values.length; i++) {
            setElement(ps, i + 1, values[i]);
        }

        // do the update
        ps.executeUpdate();


        // close the resources
        closeStatement(ps);
        closeConnection(con);

    }

    /**
     * Returns the default connection.
     *
     * @return the default connection.
     *
     * @throws Exception to JUnit
     */
    protected Connection getConnection() throws Exception {
        // gets the DBConnectionFactory.
        DBConnectionFactory factory = new DBConnectionFactoryImpl(DB_NAMESPACE);

        // returns the default connection (that is set to be non-auto commit).
        Connection con = factory.createConnection();
        return con;
    }

    /**
     * Returns the connection factory.
     *
     * @return returns the connection factory
     *
     * @throws Exception to JUnit
     */
    protected DBConnectionFactory getConnectionFactory() throws Exception {
        // gets the DBConnectionFactory.
        DBConnectionFactory factory = new DBConnectionFactoryImpl(DB_NAMESPACE);
        return factory;
    }

    /**
     * Checks if there are some specific records of a query.
     *
     * @param sql the sql to query
     * @param values  the values to set into the statement
     *
     * @return true if there is the record, false otherwise
     *
     * @throws Exception to JUnit
     */
    protected boolean existsRecord(String sql, Object[] values) throws Exception {
        // create the connection
        Connection con = getConnection();

        // create the prepared statement
        PreparedStatement ps = con.prepareStatement(sql);

        ResultSet rs = doSQLQuery(con, ps, values);

        boolean containsRecords = false;

        if (rs.next()) {
            containsRecords = true;
        }

        // closes the resources
        closeConnection(con);
        closeStatement(ps);
        closeResultSet(rs);

        return containsRecords;
    }

    /**
     * Closes a result set silently.
     *
     * @param rs the result set to close
     */
    protected void closeResultSet(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                // ignore
            }
        }

    }

    /**
     * Does the query with a specific sql statement.
     *
     * @param con the connection
     * @param ps the sql statement
     * @param values the values
     *
     * @return the result set
     *
     * @throws Exception to JUnit
     */
    protected ResultSet doSQLQuery(Connection con, PreparedStatement ps, Object[] values) throws Exception {
        // set the elements
        for (int i = 0; i < values.length; i++) {
            setElement(ps, i + 1, values[i]);
        }

        // do the query
        return ps.executeQuery();
    }

    /**
     * Sets the value of a specific index to the prepared statement.
     *
     * @param ps the prepared statement
     * @param index the index
     * @param value the value to be set
     *
     * @throws Exception to JUnit
     */
    private void setElement(PreparedStatement ps, int index, Object value) throws Exception {
        if (value instanceof Integer) {
            ps.setInt(index, ((Integer) value).intValue());
        } else if (value instanceof Long) {
            ps.setLong(index, ((Long) value).longValue());
        } else if (value instanceof Timestamp) {
            ps.setTimestamp(index, (Timestamp) value);
        } else if (value instanceof String) {
            ps.setString(index, (String) value);
        } else if (value instanceof Date) {
            ps.setDate(index, new java.sql.Date(((Date) value).getTime()));
        } else {
            throw new IllegalArgumentException("The type of the value is not supported");
        }
    }

    /**
     * Closes the connection silently.
     *
     * @param con the connection to close.
     */
    protected void closeConnection(Connection con) {
        if (con != null) {
            try {
                con.close();
            } catch (SQLException e) {
                // ignore
            }
        }
    }

    /**
     * Closes the connection silently.
     *
     * @param con the connection to close.
     */
    protected void closeStatement(PreparedStatement ps) {
        if (ps != null) {
            try {
                ps.close();
            } catch (SQLException e) {
                // ignore
            }
        }
    }

    /**
     * Outputs the running info of the stress tests.
     *
     * @param start the time that begins the stress test
     * @param finish the time that finishes the stress test
     * @param testCaseName the name of the stress test
     */
    protected void outputStressInfo(Date start, Date finish, String testCaseName) {
        System.out.println("Running " + testCaseName
                + " for " + STRESS_TEST_NUM + " times: "
                + (finish.getTime() - start.getTime()) + " ms.");
    }

    /**
     * Insert the fixed records to the tables.
     *
     * @throws Exception to JUnit
     */
    private void insertFixedRecords() throws Exception {
        Connection con = getConnection();

        String sql = null;

        // insert records to the table: project
        sql = "INSERT INTO project(project_id) VALUES(?)";

        doSQLUpdate(sql, new Object[] {new Long(1)});

        doSQLUpdate(sql, new Object[] {new Long(2)});

        doSQLUpdate(sql, new Object[] {new Long(3)});

        // insert records to the table: phase_type_lu
        sql = "INSERT INTO phase_type_lu(phase_type_id) VALUES(?)";

        doSQLUpdate(sql, new Object[] {new Long(1)});

        doSQLUpdate(sql, new Object[] {new Long(2)});

        doSQLUpdate(sql, new Object[] {new Long(3)});

        // insert records to the table: phase

        sql = "INSERT INTO project_phase(project_phase_id, project_id, phase_type_id) VALUES(?, ?, ?)";

        doSQLUpdate(sql, new Object[] {new Long(1), new Long(1), new Long(1)});

        doSQLUpdate(sql, new Object[] {new Long(2), new Long(1), new Long(2)});

        doSQLUpdate(sql, new Object[] {new Long(3), new Long(1), new Long(3)});

        doSQLUpdate(sql, new Object[] {new Long(4), new Long(2), new Long(1)});

        doSQLUpdate(sql, new Object[] {new Long(5), new Long(2), new Long(2)});

        doSQLUpdate(sql, new Object[] {new Long(6), new Long(2), new Long(3)});

        doSQLUpdate(sql, new Object[] {new Long(7), new Long(3), new Long(1)});

        doSQLUpdate(sql, new Object[] {new Long(8), new Long(3), new Long(2)});

        doSQLUpdate(sql, new Object[] {new Long(9), new Long(3), new Long(3)});

        // insert records to the table: submission

        sql = "INSERT INTO submission(submission_id) VALUES(?)";

        doSQLUpdate(sql, new Object[] {new Long(1)});

        doSQLUpdate(sql, new Object[] {new Long(2)});

        doSQLUpdate(sql, new Object[] {new Long(3)});

        // insert records to the table: resource_role_lu

        sql = "INSERT INTO resource_role_lu(resource_role_id, phase_type_id, name,"
            + " description, create_user, create_date, modify_user, modify_date)"
            + " VALUES(?, ?, ?, 'test', 'developer', CURRENT, 'developer', CURRENT)";

        doSQLUpdate(sql, new Object[] {new Long(1), new Long(1), "role 1"});

        doSQLUpdate(sql, new Object[] {new Long(2), new Long(2), "role 2"});

        doSQLUpdate(sql, new Object[] {new Long(3), new Long(3), "role 3"});

        // insert records to the table: resource.

        sql = "INSERT INTO resource(resource_id, resource_role_id, project_id, project_phase_id, "
            + "create_user, create_date, modify_user, modify_date)"
            + " VALUES (?, ?, ?, ?, 'developer', CURRENT, 'developer', CURRENT)";

        doSQLUpdate(sql, new Object[] {new Long(1),
            new Long(1), new Long(1), new Long(1)});

        doSQLUpdate(sql, new Object[] {new Long(2),
            new Long(1), new Long(2), new Long(1)});

        doSQLUpdate(sql, new Object[] {new Long(3),
            new Long(1), new Long(3), new Long(1)});

        doSQLUpdate(sql, new Object[] {new Long(4),
            new Long(2), new Long(1), new Long(1)});

        doSQLUpdate(sql, new Object[] {new Long(5),
            new Long(2), new Long(2), new Long(1)});

        doSQLUpdate(sql, new Object[] {new Long(6),
            new Long(2), new Long(3), new Long(1)});

        doSQLUpdate(sql, new Object[] {new Long(7),
            new Long(3), new Long(1), new Long(1)});

        doSQLUpdate(sql, new Object[] {new Long(8),
            new Long(3), new Long(2), new Long(1)});

        doSQLUpdate(sql, new Object[] {new Long(9),
            new Long(3), new Long(3), new Long(1)});

        // insert records to the table: resource_info_type_lu

        sql = "INSERT INTO resource_info_type_lu"
            + " (resource_info_type_id, name, description, create_user, create_date, modify_user, modify_date)"
            + " VALUES(?, ?, 'test', 'developer', CURRENT, 'developer', CURRENT)";

        doSQLUpdate(sql, new Object[] {new Long(1), "resource info type 1"});

        doSQLUpdate(sql, new Object[] {new Long(2), "resource info type 2"});

        doSQLUpdate(sql, new Object[] {new Long(3), "resource info type 3"});

        // insert records to the table: resource_info
        sql = "INSERT INTO resource_info"
            + " (resource_id, resource_info_type_id, value, create_user, create_date, modify_user, modify_date)"
            + " VALUES(?, ?, ?, 'developer', CURRENT, 'developer', CURRENT)";

        doSQLUpdate(sql, new Object[] {new Long(1), new Long(1), "value 1"});

        doSQLUpdate(sql, new Object[] {new Long(2), new Long(1), "value 2"});

        doSQLUpdate(sql, new Object[] {new Long(3), new Long(1), "value 3"});

        doSQLUpdate(sql, new Object[] {new Long(1), new Long(2), "value 4"});

        doSQLUpdate(sql, new Object[] {new Long(2), new Long(2), "value 5"});

        doSQLUpdate(sql, new Object[] {new Long(3), new Long(2), "value 6"});

        doSQLUpdate(sql, new Object[] {new Long(1), new Long(3), "value 7"});

        doSQLUpdate(sql, new Object[] {new Long(2), new Long(3), "value 8"});

        doSQLUpdate(sql, new Object[] {new Long(3), new Long(3), "value 9"});

        // insert records to the table: resource_submission
        sql = "INSERT INTO resource_submission"
            + " (resource_id, submission_id, create_user, create_date, modify_user, modify_date)"
            + " VALUES(?, ?, 'developer', CURRENT, 'developer', CURRENT)";

        doSQLUpdate(sql, new Object[] {new Long(1), new Long(1)});

        doSQLUpdate(sql, new Object[] {new Long(1), new Long(2)});

        doSQLUpdate(sql, new Object[] {new Long(1), new Long(3)});

        doSQLUpdate(sql, new Object[] {new Long(2), new Long(1)});

        doSQLUpdate(sql, new Object[] {new Long(2), new Long(2)});

        doSQLUpdate(sql, new Object[] {new Long(2), new Long(3)});

        doSQLUpdate(sql, new Object[] {new Long(3), new Long(1)});

        doSQLUpdate(sql, new Object[] {new Long(3), new Long(2)});

        doSQLUpdate(sql, new Object[] {new Long(3), new Long(3)});

        // insert records to the table: notification_type_lu
        sql = "INSERT INTO notification_type_lu"
            + " (notification_type_id, name, description, create_user, create_date, modify_user, modify_date)"
            + " VALUES(?, ?, 'test', 'developer', CURRENT, 'developer', CURRENT)";

        doSQLUpdate(sql, new Object[] {new Long(1), "notification type 1"});

        doSQLUpdate(sql, new Object[] {new Long(2), "notification type 2"});

        doSQLUpdate(sql, new Object[] {new Long(3), "notification type 3"});

        // insert records to the table: notification
        sql = "INSERT INTO notification ("
            + "project_id, external_ref_id, notification_type_id, create_user, create_date, modify_user, modify_date)"
            + " VALUES(?, ?, ?, 'developer', CURRENT, 'developer', CURRENT)";

        doSQLUpdate(sql, new Object[] {new Long(1), new Long(1), new Long(1)});

        doSQLUpdate(sql, new Object[] {new Long(1), new Long(1), new Long(2)});

        doSQLUpdate(sql, new Object[] {new Long(1), new Long(1), new Long(3)});

        doSQLUpdate(sql, new Object[] {new Long(1), new Long(2), new Long(1)});

        doSQLUpdate(sql, new Object[] {new Long(1), new Long(2), new Long(2)});

        doSQLUpdate(sql, new Object[] {new Long(1), new Long(2), new Long(3)});

        doSQLUpdate(sql, new Object[] {new Long(1), new Long(3), new Long(1)});

        doSQLUpdate(sql, new Object[] {new Long(1), new Long(3), new Long(2)});

        doSQLUpdate(sql, new Object[] {new Long(1), new Long(3), new Long(3)});

        doSQLUpdate(sql, new Object[] {new Long(2), new Long(1), new Long(1)});

        doSQLUpdate(sql, new Object[] {new Long(2), new Long(1), new Long(2)});

        doSQLUpdate(sql, new Object[] {new Long(2), new Long(1), new Long(3)});

        doSQLUpdate(sql, new Object[] {new Long(2), new Long(2), new Long(1)});

        doSQLUpdate(sql, new Object[] {new Long(2), new Long(2), new Long(2)});

        doSQLUpdate(sql, new Object[] {new Long(2), new Long(2), new Long(3)});

        doSQLUpdate(sql, new Object[] {new Long(2), new Long(3), new Long(1)});

        doSQLUpdate(sql, new Object[] {new Long(2), new Long(3), new Long(2)});

        doSQLUpdate(sql, new Object[] {new Long(2), new Long(3), new Long(3)});

        doSQLUpdate(sql, new Object[] {new Long(3), new Long(1), new Long(1)});

        doSQLUpdate(sql, new Object[] {new Long(3), new Long(1), new Long(2)});

        doSQLUpdate(sql, new Object[] {new Long(3), new Long(1), new Long(3)});

        doSQLUpdate(sql, new Object[] {new Long(3), new Long(2), new Long(1)});

        doSQLUpdate(sql, new Object[] {new Long(3), new Long(2), new Long(2)});

        doSQLUpdate(sql, new Object[] {new Long(3), new Long(2), new Long(3)});

        doSQLUpdate(sql, new Object[] {new Long(3), new Long(3), new Long(1)});

        doSQLUpdate(sql, new Object[] {new Long(3), new Long(3), new Long(2)});

        doSQLUpdate(sql, new Object[] {new Long(3), new Long(3), new Long(3)});

        closeConnection(con);

    }
}
