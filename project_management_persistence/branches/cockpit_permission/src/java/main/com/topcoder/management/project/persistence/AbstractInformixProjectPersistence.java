/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.project.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Arrays;
import java.util.Map.Entry;

import com.topcoder.db.connectionfactory.DBConnectionFactory;
import com.topcoder.db.connectionfactory.DBConnectionFactoryImpl;
import com.topcoder.management.project.ConfigurationException;
import com.topcoder.management.project.ContestSale;
import com.topcoder.management.project.PersistenceException;
import com.topcoder.management.project.Project;
import com.topcoder.management.project.ProjectCategory;
import com.topcoder.management.project.ProjectPersistence;
import com.topcoder.management.project.ProjectPropertyType;
import com.topcoder.management.project.ProjectSpec;
import com.topcoder.management.project.ProjectStatus;
import com.topcoder.management.project.ProjectType;
import com.topcoder.management.project.SaleStatus;
import com.topcoder.management.project.SaleType;
import com.topcoder.management.project.SimpleProjectContestData;
import com.topcoder.management.project.persistence.Helper.DataType;
import com.topcoder.management.project.persistence.logging.LogMessage;
import com.topcoder.management.project.SimpleProjectPermissionData;
import com.topcoder.util.config.ConfigManager;
import com.topcoder.util.idgenerator.IDGenerationException;
import com.topcoder.util.idgenerator.IDGenerator;
import com.topcoder.util.idgenerator.IDGeneratorFactory;
import com.topcoder.util.sql.databaseabstraction.CustomResultSet;
import com.topcoder.util.sql.databaseabstraction.InvalidCursorStateException;
import com.topcoder.util.log.Level;
import com.topcoder.util.log.Log;

/**
 * <p>
 * This class contains operations to create/update/retrieve project instances
 * from the Informix database. It also provides methods to retrieve database
 * look up table values. It implements the ProjectPersistence interface to
 * provide a plug-in persistence for Informix database. It is used by the
 * ProjectManagerImpl class. The constructor takes a namespace parameter to
 * initialize database connection.
 * </p>
 * <p>
 * Note that in this class, delete operation is not supported. To delete a
 * project, its status is set to 'Deleted'. Create and update operations here
 * work on the project and its related items as well. It means creating/updating
 * a project would involve creating/updating its properties.
 * </p>
 * <p>
 * This abstract class does not manage the connection itself. It contains three
 * abstract methods which should be implemented by concrete subclass to manage
 * the connection:
 * <ul>
 * <li><code>openConnection()</code> is used to acquire and open the
 * connection.</li>
 * <li><code>closeConnection()</code> is used to dispose the connection if
 * the queries are executed successfully.</li>
 * <li><code>closeConnectionOnError()</code> is used to handle the error if
 * the SQL operation fails.</li>
 * </ul>
 * The transaction handling logic should be implemented in subclasses while the
 * <code>Statement</code>s and <code>ResultSet</code>s are handled in this
 * abstract class.
 * </p>
 * 
 * <p>
 * Module Contest Service Software Contest Sales Assembly change: new methods added to support creating/updating/query contest
 * sale for software contest.
 * </p>
 *
 * <p>
 * Updated for Cockpit Launch Contest - Update for Spec Creation v1.0
 *      - added persist for project_spec.
 * </p>
 *
 * <p>
 * Thread Safety: This class is thread safe because it is immutable.
 * </p>
 * 
 * [BUGR-2038]: the new logic for returned contest 'status' will be
 * if status is 'active' in db, and there is a row in contest_sale, then returned/shown status will be 'Scheduled',
 * if status is 'active' in db, and there is no row in contest_sale, then returned/shwon status will be 'Draft",
 * otherwise, show 'status' from db.  
 *
 * @author tuenm, urtks, bendlund, fuyun, TCSASSEMBLER
 * @version 1.1
 */
public abstract class AbstractInformixProjectPersistence implements ProjectPersistence {
	private static final com.topcoder.util.log.Log log = com.topcoder.util.log.LogManager
			.getLog(AbstractInformixProjectPersistence.class.getName());

	/**
     * <p>
     * Represents the default value for Project Id sequence name. It is used to
     * create id generator for project. This value will be overridden by
     * 'ProjectIdSequenceName' configuration parameter if it exist.
     * </p>
     */
    public static final String PROJECT_ID_SEQUENCE_NAME = "project_id_seq";

	/**
     * <p>
     * Represents the default value for Contest Sale Id sequence name. It is used to
     * create id generator for contest_sale. This value will be overridden by
     * 'ContestSaleIdSequenceName' configuration parameter if it exist.
     * </p>
     *
     * @since Module Contest Service Software Contest Sales Assembly
     */
    public static final String CONTEST_SALE_ID_SEQUENCE_NAME = "contest_sale_id_seq";

    /**
     * <p>
     * Represents the default value for project audit id sequence name. It is
     * used to create id generator for project audit. This value will be
     * overridden by 'ProjectAuditIdSequenceName' configuration parameter if it
     * exist.
     * </p>
     */
    public static final String PROJECT_AUDIT_ID_SEQUENCE_NAME = "project_audit_id_seq";

    /**
     * <p>
     * Represents the default value for project spec id sequence name. It is
     * used to create id generator for project spec. This value will be
     * overridden by 'ProjectSpecIdSequenceName' configuration parameter if it
     * exist.
     * </p>
     * 
     * @since Cockpit Launch Contest - Update for Spec Creation v1.0
     */
    public static final String PROJECT_SPEC_ID_SEQUENCE_NAME = "PROJECT_SPEC_ID_SEQ";

    /**
     * Represents the name of connection name parameter in configuration.
     */
    private static final String CONNECTION_NAME_PARAMETER = "ConnectionName";

    /**
     * Represents the name of connection factory namespace parameter in
     * configuration.
     */
    private static final String CONNECTION_FACTORY_NAMESPACE_PARAMETER = "ConnectionFactoryNS";

    /**
     * Represents the name of project id sequence name parameter in
     * configuration.
     */
    private static final String PROJECT_ID_SEQUENCE_NAME_PARAMETER = "ProjectIdSequenceName";

    /**
     * Represents the name of contest sale id sequence name parameter in
     * configuration.
     *
     * @since Module Contest Service Software Contest Sales Assembly
     */
    private static final String CONTEST_SALE_ID_SEQUENCE_NAME_PARAMETER = "ContestSaleIdSequenceName";

    /**
     * Represents the name of project audit id sequence name parameter in
     * configuration.
     */
    private static final String PROJECT_AUDIT_ID_SEQUENCE_NAME_PARAMETER = "ProjectAuditIdSequenceName";

    /**
     * Represents the name of project audit id sequence name parameter in
     * configuration.
     */
    private static final String PROJECT_SPEC_ID_SEQUENCE_NAME_PARAMETER = "ProjectSpecIdSequenceName";

    /**
     * Represents the sql statement to query all project types.
     */
    private static final String QUERY_ALL_PROJECT_TYPES_SQL = "SELECT "
            + "project_type_id, name, description FROM project_type_lu";

    /**
     * Represents the column types for the result set which is returned by
     * executing the sql statement to query all project types.
     */
    private static final DataType[] QUERY_ALL_PROJECT_TYPES_COLUMN_TYPES = new DataType[] {
        Helper.LONG_TYPE, Helper.STRING_TYPE, Helper.STRING_TYPE };

    /**
     * Represents the sql statement to query the sale status via id.
     *
     * @since Module Contest Service Software Contest Sales Assembly
     */
    private static final String QUERY_SALE_STATUS_BY_ID_SQL = "SELECT "
            + "sale_status_id, sale_status_desc FROM sale_status_lu WHERE sale_status_id=?";

    /**
     * Represents the column types for the result set which is returned by
     * executing the sql statement to query the sale status via id.
     *
     * @since Module Contest Service Software Contest Sales Assembly
     */
    private static final DataType[] QUERY_SALE_STATUS_BY_ID_COLUMN_TYPES = new DataType[] {
        Helper.LONG_TYPE, Helper.STRING_TYPE };

    /**
     * Represents the sql statement to query the sale type via id.
     *
     * @since Module Contest Service Software Contest Sales Assembly
     */
    private static final String QUERY_SALE_TYPE_BY_ID_SQL = "SELECT "
            + "sale_type_id, sale_type_name FROM sale_type_lu WHERE sale_type_id=?";

    /**
     * Represents the column types for the result set which is returned by
     * executing the sql statement to query the sale type via id.
     *
     * @since Module Contest Service Software Contest Sales Assembly
     */
    private static final DataType[] QUERY_SALE_TYPE_BY_ID_COLUMN_TYPES = new DataType[] {
        Helper.LONG_TYPE, Helper.STRING_TYPE };

    /**
     * Represents the sql statement to query all project categories.
     */
    private static final String QUERY_ALL_PROJECT_CATEGORIES_SQL = "SELECT "
            + "category.project_category_id, category.name, category.description, "
            + "type.project_type_id, type.name, type.description "
            + "FROM project_category_lu AS category "
            + "JOIN project_type_lu AS type "
            + "ON category.project_type_id = type.project_type_id";

    /**
     * Represents the sql statement to query all project contest data.
     * 
     * <p>
     * Updated for Cockpit Release Assembly 3 [RS: 1.1.1 & 1.1.3]
     *      - fetch tc project and s/w project permission.
     *      - whenever join with s/w project and user_permission_grant, add is_studio=0 constraint.
     * </p>
     */

		private static final String QUERY_ALL_SIMPLE_PROJECT_CONTEST = " select p.project_id as contest_id, "
			+ " (select ptl.name from phase_type_lu ptl where phase_type_id = (select min(phase_type_id) from project_phase ph "
			+ " where ph.phase_status_id = 2 and ph.project_id=p.project_id)) as current_phase, "
			+ " (select value from project_info where project_id = p.project_id and project_info_type_id =6) as contest_name, "
			+ " (select min(nvl(actual_start_time, scheduled_start_time)) from project_phase ph where ph.project_id=p.project_id) as start_date, "
			+ " (select max(nvl(actual_end_time, scheduled_end_time)) from project_phase ph where ph.project_id=p.project_id) as end_date, "
			+ "  pcl.name as contest_type, psl.name as status, "
			+ " 0 as num_reg, "
			+ " 0 as num_sub, "
			+ " 0 as num_for, "
			+ " tc_direct_project_id as project_id, tcd.name, tcd.description, tcd.user_id, "
			+ "  (select value from project_info where project_id = p.project_id and project_info_type_id =4) as forum_id, "
			+ "  (select case when(count(*)>=1) then 'Scheduled' when(count(*)=0) then 'Draft' end "
			+ "   from contest_sale c where p.project_id = c.contest_id and upper(psl.name)='ACTIVE' ) as newstatus, "
			+ " (select name from permission_type where permission_type_id= NVL( (select max( permission_type_id)  "
            + " from user_permission_grant as upg  where resource_id=p.project_id and is_studio=0 "
            + " ),0)) as cperm, "

            + " (select name from permission_type where permission_type_id= NVL( (select max( permission_type_id)  "
            + " from user_permission_grant as upg  where resource_id=tcd.project_id  "
            + " ),0)) as pperm "
			+ " from project p, project_category_lu pcl, project_status_lu psl, tc_direct_project tcd "
			+ " where p.project_category_id = pcl.project_category_id and p.project_status_id = psl.project_status_id and p.tc_direct_project_id = tcd.project_id "
			+ "		and p.project_status_id != 3 ";
	
   
	
    /**
     * Represents the sql statement to query all project contest data for a tc project id.
     * 
     * <p>
     * Updated for Cockpit Release Assembly 3 [RS: 1.1.1 & 1.1.3]
     *      - fetch tc project and s/w project permission.
     *      - whenever join with s/w project and user_permission_grant, add is_studio=0 constraint.
     * </p>
     */
	private static final String QUERY_ALL_SIMPLE_PROJECT_CONTEST_BY_PID = " select p.project_id as contest_id, "
	+		" (select ptl.name from phase_type_lu ptl where phase_type_id = (select min(phase_type_id) from project_phase ph " 
	+ " where ph.phase_status_id = 2 and ph.project_id=p.project_id)) as current_phase, "
	+ "(select value from project_info where project_id = p.project_id and project_info_type_id =6) as contest_name, "
	+ "(select min(nvl(actual_start_time, scheduled_start_time)) from project_phase ph where ph.project_id=p.project_id) as start_date, "
	+ " (select max(nvl(actual_end_time, scheduled_end_time)) from project_phase ph where ph.project_id=p.project_id) as end_date, "
	+ "  pcl.name as contest_type, psl.name as status, "
	+ " 0 as num_reg, "
	+ " 0 as num_sub, "
	+ " 0 as num_for, "
	+ " tc_direct_project_id as project_id , tcd.name, tcd.description, tcd.user_id, "
	+ "  (select value from project_info where project_id = p.project_id and project_info_type_id =4) as forum_id, "
	+ "  (select case when(count(*)>=1) then 'Scheduled' when(count(*)=0) then 'Draft' end "
	+ "   from contest_sale c where p.project_id = c.contest_id and upper(psl.name)='ACTIVE' ) as newstatus, "
	
	
    + " (select name from permission_type where permission_type_id= NVL( (select max( permission_type_id)  "
    + " from user_permission_grant as upg  where resource_id=p.project_id and is_studio=0"
    + " ),0)) as cperm, "

    + " (select name from permission_type where permission_type_id= NVL( (select max( permission_type_id)  "
    + " from user_permission_grant as upg  where resource_id=tcd.project_id"
    + " ),0)) as pperm "
			
	+ " from project p, project_category_lu pcl, project_status_lu psl, tc_direct_project tcd "
	+ " where p.project_category_id = pcl.project_category_id and p.project_status_id = psl.project_status_id and p.tc_direct_project_id = tcd.project_id "
	+" and p.project_status_id != 3 and p.tc_direct_project_id= ";

    /**
     * Represents the column types for the result set which is returned by executing the sql statement to query all
     * project categories.
     */
    private static final DataType[] QUERY_ALL_PROJECT_CATEGORIES_COLUMN_TYPES = new DataType[] { Helper.LONG_TYPE,
        Helper.LONG_TYPE, Helper.STRING_TYPE, Helper.STRING_TYPE,
        Helper.LONG_TYPE, Helper.STRING_TYPE, Helper.STRING_TYPE };

	/**
     * Represents the column types for the result set which is returned by executing the sql statement to query all
     * project categories.
     * 
     * <p>
     * Updated for Cockpit Release Assembly 3 [RS: 1.1.1]
     *      - Added column type mapping for 2 new columns: tc project permission and contest permission.
     * </p>
     */
    private static final DataType[] QUERY_ALL_SIMPLE_PROJECT_CONTEST_COLUMN_TYPES = new DataType[] { Helper.LONG_TYPE,
			Helper.STRING_TYPE, Helper.STRING_TYPE,
			Helper.DATE_TYPE,Helper.DATE_TYPE,
			Helper.STRING_TYPE, Helper.STRING_TYPE,
			Helper.LONG_TYPE, Helper.LONG_TYPE, Helper.LONG_TYPE,
		   	Helper.LONG_TYPE, Helper.STRING_TYPE, Helper.STRING_TYPE,  
			Helper.STRING_TYPE, Helper.LONG_TYPE, Helper.STRING_TYPE,
			Helper.STRING_TYPE, Helper.STRING_TYPE};

    /**
     * Represents the sql statement to query all project statuses.
     */
    private static final String QUERY_ALL_PROJECT_STATUSES_SQL = "SELECT "
            + "project_status_id, name, description FROM project_status_lu";

    /**
     * Represents the column types for the result set which is returned by
     * executing the sql statement to query all project statuses.
     */
    private static final DataType[] QUERY_ALL_PROJECT_STATUSES_COLUMN_TYPES = new DataType[] {
        Helper.LONG_TYPE, Helper.STRING_TYPE, Helper.STRING_TYPE };

    /**
     * Represents the sql statement to query all project property types.
     */
    private static final String QUERY_ALL_PROJECT_PROPERTY_TYPES_SQL = "SELECT "
            + "project_info_type_id, name, description FROM project_info_type_lu";

    /**
     * Represents the column types for the result set which is returned by
     * executing the sql statement to query all project property types.
     */
    private static final DataType[] QUERY_ALL_PROJECT_PROPERTY_TYPES_COLUMN_TYPES = new DataType[] {
        Helper.LONG_TYPE, Helper.STRING_TYPE, Helper.STRING_TYPE };

    /**
     * Represents the sql statement to query projects.
     */
    private static final String QUERY_PROJECTS_SQL = "SELECT "
            + "project.project_id, status.project_status_id, status.name,"
            + "category.project_category_id, category.name, type.project_type_id, type.name,"
            + "project.create_user, project.create_date, project.modify_user, project.modify_date, category.description, "
            + "project.tc_direct_project_id "
            + "FROM project JOIN project_status_lu AS status "
            + "ON project.project_status_id=status.project_status_id "
            + "JOIN project_category_lu AS category "
            + "ON project.project_category_id=category.project_category_id "
            + "JOIN project_type_lu AS type "
            + "ON category.project_type_id=type.project_type_id "
            + "WHERE project.project_id IN ";

    /**
     * Represents the column types for the result set which is returned by
     * executing the sql statement to query projects.
     */
    private static final DataType[] QUERY_PROJECTS_COLUMN_TYPES = new DataType[] {
        Helper.LONG_TYPE, Helper.LONG_TYPE, Helper.STRING_TYPE,
        Helper.LONG_TYPE, Helper.STRING_TYPE, Helper.LONG_TYPE,
        Helper.STRING_TYPE, Helper.STRING_TYPE, Helper.DATE_TYPE,
        Helper.STRING_TYPE, Helper.DATE_TYPE, Helper.STRING_TYPE, Helper.LONG_TYPE};
    
    /**
     * Represents the sql statement to query tc direct projects.
     */
    private static final String QUERY_TC_DIRECT_PROJECTS_SQL = "SELECT "
            + "project.project_id, status.project_status_id, status.name,"
            + "category.project_category_id, category.name, type.project_type_id, type.name,"
            + "project.create_user, project.create_date, project.modify_user, project.modify_date, category.description, "
            + "project.tc_direct_project_id "
            + "FROM project JOIN project_status_lu AS status "
            + "ON project.project_status_id=status.project_status_id "
            + "JOIN project_category_lu AS category "
            + "ON project.project_category_id=category.project_category_id "
            + "JOIN project_type_lu AS type "
            + "ON category.project_type_id=type.project_type_id "
            + "WHERE project.tc_direct_project_id is not null ";

   
    /**
     * Represents the sql statement to query tc direct projects.
     */
    private static final String QUERY_USER_TC_DIRECT_PROJECTS_SQL = "SELECT "
            + "project.project_id, status.project_status_id, status.name, "
            + "category.project_category_id, category.name, type.project_type_id, type.name,"
            + "project.create_user, project.create_date, project.modify_user, project.modify_date, category.description, "
            + "project.tc_direct_project_id "
            + "FROM project JOIN project_status_lu AS status "
            + "ON project.project_status_id=status.project_status_id "
            + "JOIN project_category_lu AS category "
            + "ON project.project_category_id=category.project_category_id "
            + "JOIN project_type_lu AS type "
            + "ON category.project_type_id=type.project_type_id "
            + "WHERE project.tc_direct_project_id is not null AND project.create_user = ?";  
    
    /**
     * Represents the sql statement to query project properties.
     */
    private static final String QUERY_PROJECT_PROPERTIES_SQL = "SELECT "
            + "info.project_id, info_type.name, info.value "
            + "FROM project_info AS info "
            + "JOIN project_info_type_lu AS info_type "
            + "ON info.project_info_type_id=info_type.project_info_type_id "
            + "WHERE info.project_id IN ";

    /**
     * Represents the sql statement to query project properties.
     */
    private static final String QUERY_ONE_PROJECT_PROPERTIES_SQL = "SELECT "
            + "info.project_id, info_type.name, info.value "
            + "FROM project_info AS info "
            + "JOIN project_info_type_lu AS info_type "
            + "ON info.project_info_type_id=info_type.project_info_type_id "
            + "WHERE info.project_id = ?";
    
    /**
     * Represents the column types for the result set which is returned by
     * executing the sql statement to query project properties.
     */
    private static final DataType[] QUERY_PROJECT_PROPERTIES_COLUMN_TYPES = new DataType[] {
        Helper.LONG_TYPE, Helper.STRING_TYPE, Helper.STRING_TYPE };

    /**
     * Represents the sql statement to query project property ids.
     */
    private static final String QUERY_PROJECT_PROPERTY_IDS_SQL = "SELECT "
            + "project_info_type_id FROM project_info WHERE project_id=?";

    /**
     * Represents the column types for the result set which is returned by
     * executing the sql statement to query project property ids.
     */
    private static final DataType[] QUERY_PROJECT_PROPERTY_IDS_COLUMN_TYPES = new DataType[] {Helper.LONG_TYPE};

    /**
     * Represents the sql statement to create project.
     */
    private static final String CREATE_PROJECT_SQL = "INSERT INTO project "
            + "(project_id, project_status_id, project_category_id, "
            + "create_user, create_date, modify_user, modify_date, tc_direct_project_id) "
            + "VALUES (?, ?, ?, ?, CURRENT, ?, CURRENT, ?)";

    /**
     * Represents the sql statement to query the contest sale via id.
     *
     * @since Module Contest Service Software Contest Sales Assembly
     */
    private static final String QUERY_CONTEST_SALE_BY_ID_SQL = "SELECT "
            + "contest_sale_id, contest_id, sale_status_id, price, paypal_order_id, create_date, sale_reference_id, sale_type_id "
            + "FROM contest_sale WHERE contest_sale_id=?";

    /**
     * Represents the column types for the result set which is returned by
     * executing the sql statement to query the contest sale via id.
     *
     * @since Module Contest Service Software Contest Sales Assembly
     */
    private static final DataType[] QUERY_CONTEST_SALE_BY_ID_COLUMN_TYPES = new DataType[] {
        Helper.LONG_TYPE, Helper.LONG_TYPE, Helper.LONG_TYPE, Helper.DOUBLE_TYPE,
        Helper.STRING_TYPE, Helper.DATE_TYPE, Helper.STRING_TYPE, Helper.LONG_TYPE };

    /**
     * Represents the sql statement to query the contest sale via contest id.
     *
     * @since Module Contest Service Software Contest Sales Assembly
     */
    private static final String QUERY_CONTEST_SALE_BY_CONTEST_ID_SQL = "SELECT "
            + "contest_sale_id, contest_id, sale_status_id, price, paypal_order_id, create_date, sale_reference_id, sale_type_id "
            + "FROM contest_sale WHERE contest_id=?";

    /**
     * Represents the column types for the result set which is returned by
     * executing the sql statement to query the contest sale via contest id.
     *
     * @since Module Contest Service Software Contest Sales Assembly
     */
    private static final DataType[] QUERY_CONTEST_SALE_BY_CONTEST_ID_COLUMN_TYPES = new DataType[] {
        Helper.LONG_TYPE, Helper.LONG_TYPE, Helper.LONG_TYPE, Helper.DOUBLE_TYPE,
        Helper.STRING_TYPE, Helper.DATE_TYPE, Helper.STRING_TYPE, Helper.LONG_TYPE };

    /**
     * Represents the sql statement to create contest sale.
     *
     * @since Module Contest Service Software Contest Sales Assembly
     */
    private static final String CREATE_CONTEST_SALE_SQL = "INSERT INTO contest_sale "
            + "(contest_sale_id, contest_id, sale_status_id, "
            + "price, paypal_order_id, sale_reference_id, sale_type_id) "
            + "VALUES (?, ?, ?, ?, ?, ?, ?)";

    /**
     * Represents the sql statement to delete contest sale via id.
     *
     * @since Module Contest Service Software Contest Sales Assembly
     */
    private static final String DELETE_CONTEST_SALE_BY_ID_SQL = "DELETE FROM contest_sale "
            + "WHERE contest_sale_id=?";

    /**
     * Represents the sql statement to create project property.
     */
    private static final String CREATE_PROJECT_PROPERTY_SQL = "INSERT INTO project_info "
            + "(project_id, project_info_type_id, value, "
            + "create_user, create_date, modify_user, modify_date) "
            + "VALUES (?, ?, ?, ?, CURRENT, ?, CURRENT)";

    /**
     * Represents the sql statement to create project audit.
     */
    private static final String CREATE_PROJECT_AUDIT_SQL = "INSERT INTO project_audit "
            + "(project_audit_id, project_id, update_reason, "
            + "create_user, create_date, modify_user, modify_date) "
            + "VALUES (?, ?, ?, ?, CURRENT, ?, CURRENT)";

    /**
     * Represents the sql statement to update project.
     */
    private static final String UPDATE_PROJECT_SQL = "UPDATE project "
            + "SET project_status_id=?, project_category_id=?, modify_user=?, modify_date=CURRENT, tc_direct_project_id=? "
            + "WHERE project_id=?";

    /**
     * Represents the sql statement to update contest sale.
     *
     * @since Module Contest Service Software Contest Sales Assembly
     */
    private static final String UPDATE_CONTEST_SALE_SQL = "UPDATE contest_sale "
            + "SET contest_id=?, sale_status_id=?, price=?, paypal_order_id=?, sale_reference_id=?, sale_type_id=? "
            + "WHERE contest_sale_id=?";

    /**
     * Represents the sql statement to update project property.
     */
    private static final String UPDATE_PROJECT_PROPERTY_SQL = "UPDATE project_info "
            + "SET value=?, modify_user=?, modify_date=CURRENT "
            + "WHERE project_id=? AND project_info_type_id=?";

    /**
     * Represents the sql statement to delete project properties.
     */
    private static final String DELETE_PROJECT_PROPERTIES_SQL = "DELETE FROM project_info "
            + "WHERE project_id=? AND project_info_type_id IN ";


	    /**
     * Represents the sql statement to query project_spec for specified project_id.
     * 
     * @since Cockpit Launch Contest - Update for Spec Creation v1.0
     */
    private static final String QUERY_PROJECT_SPEC_SQL = "SELECT " 
        + " ps1.project_spec_id, " 
        + " ps1.version, "
        + " ps1.detailed_requirements, " 
        + " ps1.submission_deliverables, " 
        + " ps1.environment_setup_instruction,  "
        + " ps1.final_submission_guidelines, "
        + " ps1.create_user, " 
        + " ps1.create_date, "
        + " ps1.modify_user, "
        + " ps1.modify_date "
        + " FROM project_spec as ps1 " 
        + " WHERE ps1.project_id = ? "
        + " AND ps1.version = (SELECT max(ps2.version) FROM project_spec as ps2 WHERE ps2.project_id = ?)";

    /**
     * Represents the column types for the result set which is returned by
     * executing the sql statement to query all project categories.
     */
    private static final DataType[] QUERY_SIMPLE_PROJECT_PERMISSION_DATA_COLUMN_TYPES = new DataType[] {
            Helper.LONG_TYPE, Helper.STRING_TYPE, Helper.LONG_TYPE, Helper.STRING_TYPE,
            Helper.LONG_TYPE, Helper.LONG_TYPE, Helper.LONG_TYPE,
            Helper.LONG_TYPE, Helper.LONG_TYPE, Helper.LONG_TYPE};

    private static final DataType[] QUERY_PROJECT_SPEC_COLUMN_TYPES = new DataType[] {
        Helper.LONG_TYPE, Helper.LONG_TYPE, 
        Helper.STRING_TYPE, Helper.STRING_TYPE,
        Helper.STRING_TYPE, Helper.STRING_TYPE,
        Helper.STRING_TYPE, Helper.DATE_TYPE,
        Helper.STRING_TYPE, Helper.DATE_TYPE};
    
    /**
     * Represents the sql statement to create project spec.
     * 
     * @since Cockpit Launch Contest - Update for Spec Creation v1.0
     */
    private static final String CREATE_PROJECT_SPEC_SQL = "INSERT INTO project_spec "
            + "(project_spec_id, project_id, version, " 
            + "detailed_requirements, submission_deliverables, environment_setup_instruction, final_submission_guidelines, "
            + "create_user, create_date, modify_user, modify_date) "
            + "VALUES (?, ?, 1, " 
            + "?, ?, ?, ?, "
            + "?, CURRENT, ?, CURRENT)";
    
    /**
     * Represents the sql statement to update project spec.
     * 
     * Since the project_spec table maintains all the versions of the records, we just need to insert the entry with the next version.
     * 
     * @since Cockpit Launch Contest - Update for Spec Creation v1.0
     */
    private static final String UPDATE_PROJECT_SPEC_SQL = "INSERT INTO project_spec "
            + "(project_spec_id, project_id, version, " 
            + "detailed_requirements, submission_deliverables, environment_setup_instruction, final_submission_guidelines, "
            + "create_user, create_date, modify_user, modify_date) "
            + "VALUES (?, ?, (select NVL(max(ps.version), 0)  + 1 from project_spec as ps where ps.project_id = ?), " 
            + "?, ?, ?, ?, "
            + "?, CURRENT, ?, CURRENT)";

	/**
	 * 'Active' status name
	 */
	private static final String PROJECT_STATUS_ACTIVE = "Active";

    /**
     * <p>
     * The factory instance used to create connection to the database. It is
     * initialized in the constructor using DBConnectionFactory component and
     * never changed after that. It will be used in various persistence methods
     * of this project.
     * </p>
     */
    private final DBConnectionFactory factory;

    /**
     * <p>
     * Represents the database connection name that will be used by
     * DBConnectionFactory. This variable is initialized in the constructor and
     * never changed after that. It will be used in create/update/retrieve
     * method to create connection. This variable can be null, which mean
     * connection name is not defined in the configuration namespace and default
     * connection will be created.
     * </p>
     */
    private final String connectionName;

    /**
     * <p>
     * Represents the IDGenerator for project table. This variable is
     * initialized in the constructor and never change after that. It will be
     * used in createProject() method to generate new Id for project..
     * </p>
     */
    private final IDGenerator projectIdGenerator;

    /**
     * <p>
     * Represents the IDGenerator for contest_sale table. This variable is
     * initialized in the constructor and never change after that. It will be
     * used in createContestSale() method to generate new Id for contest_sale.
     * </p>
     *
     * @since Module Contest Service Software Contest Sales Assembly
     */
    private final IDGenerator contestSaleIdGenerator;
    
    /**
     * <p>
     * Represents the IDGenerator for project audit table. This variable is
     * initialized in the constructor and never change after that. It will be
     * used in updateProject() method to store update reason.
     * </p>
     */
    private final IDGenerator projectAuditIdGenerator;

    /**
     * <p>
     * Represents the IDGenerator for project spec table. This variable is
     * initialized in the constructor and never change after that.
     * </p>
     * 
     * @since Cockpit Launch Contest - Update for Spec Creation v1.0
     */
    private final IDGenerator projectSpecIdGenerator;

    /**
     * <p>
     * Creates a new instance of <code>AbstractProjectPersistence</code> from
     * the settings in configuration.
     * </p>
     * <p>
     * The following parameters are configured.
     * <ul>
     * <li>ConnectionFactoryNS: The namespace that contains settings for DB
     * Connection Factory. It is required.</li>
     * <li>ConnectionName: The name of the connection that will be used by
     * DBConnectionFactory to create connection. If missing, default connection
     * will be created. It is optional.</li>
     * <li>ProjectIdSequenceName: The sequence name used to create Id generator
     * for project Id. If missing default value (project_id_seq) is used. It is
     * optional.</li>
     * <li>ProjectAuditIdSequenceName: The sequence name used to create Id
     * generator for project audit Id. If missing, default value
     * (project_audit_id_seq) is used. It is optional.</li>
     * </ul>
     * </p>
     * <p>
     * Configuration sample:
     *
     * <pre>
     * &lt;Config name=&quot;AbstractInformixProjectPersistence&quot;&gt;
     * &lt;!-- The DBConnectionFactory class name used to create DB Connection Factory, required --&gt;
     * &lt;Property name=&quot;ConnectionFactoryNS&quot;&gt;
     * &lt;Value&gt;com.topcoder.db.connectionfactory.DBConnectionFactoryImpl&lt;/Value&gt;
     * &lt;/Property&gt;
     * &lt;Property name=&quot;ConnectionName&quot;&gt;
     * &lt;Value&gt;informix_connection&lt;/Value&gt;
     * &lt;/Property&gt;
     * &lt;Property name=&quot;ProjectIdSequenceName&quot;&gt;
     * &lt;Value&gt;project_id_seq&lt;/Value&gt;
     * &lt;/Property&gt;
     * &lt;Property name=&quot;ProjectAuditIdSequenceName&quot;&gt;
     * &lt;Value&gt;project_audit_id_seq&lt;/Value&gt;
     * &lt;/Property&gt;
     * &lt;/Config&gt;
     * </pre>
     *
     * </p>
     * @param namespace The namespace to load configuration setting.
     * @throws IllegalArgumentException if the input is null or empty string.
     * @throws ConfigurationException if error occurs while loading
     *             configuration settings, or required configuration parameter
     *             is missing.
     * @throws PersistenceException if cannot initialize the connection to the
     *             database.
     */
    protected AbstractInformixProjectPersistence(String namespace)
        throws ConfigurationException, PersistenceException {
        Helper.assertStringNotNullNorEmpty(namespace, "namespace");

        // get the instance of ConfigManager
        ConfigManager cm = ConfigManager.getInstance();

        // get db connection factory namespace
        String factoryNamespace = Helper.getConfigurationParameterValue(cm,
                namespace, CONNECTION_FACTORY_NAMESPACE_PARAMETER, true, getLogger());

        // try to create a DBConnectionFactoryImpl instance from
        // factoryNamespace
        try {
            factory = new DBConnectionFactoryImpl(factoryNamespace);
        } catch (Exception e) {
            throw new ConfigurationException(
                    "Unable to create a new instance of DBConnectionFactoryImpl class"
                            + " from namespace [" + factoryNamespace + "].", e);
        }

        // get the connection name
        connectionName = Helper.getConfigurationParameterValue(cm, namespace,
                CONNECTION_NAME_PARAMETER, false, getLogger());

        // try to get project id sequence name
        String projectIdSequenceName = Helper.getConfigurationParameterValue(
                cm, namespace, PROJECT_ID_SEQUENCE_NAME_PARAMETER, false, getLogger());
        // use default name if project id sequence name is not provided
        if (projectIdSequenceName == null) {
            projectIdSequenceName = PROJECT_ID_SEQUENCE_NAME;
        }

        // try to get contest sale id sequence name
        String contestSaleIdSequenceName = Helper.getConfigurationParameterValue(
                cm, namespace, CONTEST_SALE_ID_SEQUENCE_NAME_PARAMETER, false, getLogger());
        // use default name if contest sale id sequence name is not provided
        if (contestSaleIdSequenceName == null) {
        	contestSaleIdSequenceName = CONTEST_SALE_ID_SEQUENCE_NAME;
        }

        // try to get project audit id sequence name
        String projectAuditIdSequenceName = Helper
                .getConfigurationParameterValue(cm, namespace,
                        PROJECT_AUDIT_ID_SEQUENCE_NAME_PARAMETER, false, getLogger());
        // use default name if project audit id sequence name is not provided
        if (projectAuditIdSequenceName == null) {
            projectAuditIdSequenceName = PROJECT_AUDIT_ID_SEQUENCE_NAME;
        }
        
        //
        // try to get project spec id sequence name
        // since Cockpit Launch Contest - Update for Spec Creation v1.0
        //
        String projectSpecIdSequenceName = Helper
                .getConfigurationParameterValue(cm, namespace,
                        PROJECT_SPEC_ID_SEQUENCE_NAME_PARAMETER, false, getLogger());
        // use default name if project spec id sequence name is not provided
        if (projectSpecIdSequenceName == null) {
            projectSpecIdSequenceName = PROJECT_SPEC_ID_SEQUENCE_NAME;
        }

        // try to get the IDGenerators
        try {
            projectIdGenerator = IDGeneratorFactory
                    .getIDGenerator(projectIdSequenceName);
        } catch (IDGenerationException e) {
        	getLogger().log(Level.ERROR, "The projectIdSequence [" + projectIdSequenceName+"] is invalid.");
            throw new PersistenceException("Unable to create IDGenerator for '"
                    + projectIdSequenceName + "'.", e);
        }
        try {
            contestSaleIdGenerator = IDGeneratorFactory
                    .getIDGenerator(contestSaleIdSequenceName);
        } catch (IDGenerationException e) {
        	getLogger().log(Level.ERROR, "The contestSaleIdSequence [" + contestSaleIdSequenceName+"] is invalid.");
            throw new PersistenceException("Unable to create IDGenerator for '"
                    + contestSaleIdSequenceName + "'.", e);
        }
        try {
            projectAuditIdGenerator = IDGeneratorFactory
                    .getIDGenerator(projectAuditIdSequenceName);
        } catch (IDGenerationException e) {
        	getLogger().log(Level.ERROR, "The projectAuditIdSequence [" + projectAuditIdSequenceName +"] is invalid.");
            throw new PersistenceException("Unable to create IDGenerator for '"
                    + projectAuditIdSequenceName + "'.", e);
        }
        
        //
        // create the instance of project spec id generator.
        // since Cockpit Launch Contest - Update for Spec Creation v1.0
        //
        try {
            projectSpecIdGenerator = IDGeneratorFactory
                    .getIDGenerator(projectSpecIdSequenceName);
        } catch (IDGenerationException e) {
            getLogger().log(Level.ERROR, "The projectSpecIdSequence [" + projectSpecIdSequenceName +"] is invalid.");
            throw new PersistenceException("Unable to create IDGenerator for '"
                    + projectSpecIdSequenceName + "'.", e);
        }
    }

    /**
     * <p>
     * Creates the project in the database using the given project instance.
     * </p>
     * <p>
     * The project information is stored to 'project' table, while its
     * properties are stored in 'project_info' table. The project's associating
     * scorecards are stored in 'project_scorecard' table. For the project, its
     * properties and associating scorecards, the operator parameter is used as
     * the creation/modification user and the creation date and modification
     * date will be the current date time when the project is created.
     * </p>
     * @param project The project instance to be created in the database.
     * @param operator The creation user of this project.
     * @throws IllegalArgumentException if any input is null or the operator is
     *             empty string.
     * @throws PersistenceException if error occurred while accessing the
     *             database.
     */
    public void createProject(Project project, String operator)
        throws PersistenceException {
        Helper.assertObjectNotNull(project, "project");
        Helper.assertStringNotNullNorEmpty(operator, "operator");

        Connection conn = null;

        // newId will contain the new generated Id for the project
        Long newId;
        // createDate will contain the create_date value retrieved from
        // database.
        Date createDate;
        
        Date specCreateDate;

        getLogger().log(Level.INFO, new LogMessage(null, operator, 
        		"creating new project: " + project.getAllProperties()));

        
        try {
            // create the connection
            conn = openConnection();

            // check whether the project id is already in the database
            if (project.getId() > 0) {
                if (Helper.checkEntityExists("project", "project_id", project
                        .getId(), conn)) {
                	throw new PersistenceException(
                            "The project with the same id [" + project.getId()
                                    + "] already exists.");
                }
            }

            try {
                // generate id for the project
                newId = new Long(projectIdGenerator.getNextID());
                getLogger().log(Level.INFO, new LogMessage(newId, operator, "generate id for new project"));
            } catch (IDGenerationException e) {
                throw new PersistenceException(
                        "Unable to generate id for the project.", e);
            }

            // create the project
            createProject(newId, project, operator, conn);

            // get the creation date.
            createDate = (Date) Helper.doSingleValueQuery(conn,
                    "SELECT create_date FROM project WHERE project_id=?",
                    new Object[] {newId}, Helper.DATE_TYPE);
            
            closeConnection(conn);
        } catch (PersistenceException e) {
        	getLogger().log(Level.ERROR,
        			new LogMessage(null, operator, "Fails to create project " + project.getAllProperties(), e));
            if (conn != null) {
                closeConnectionOnError(conn);
            }
            throw e;
        }

        // set the newId when no exception occurred
        project.setId(newId.longValue());

        // set the creation/modification user and date when no exception
        // occurred
        project.setCreationUser(operator);
        project.setCreationTimestamp(createDate);
        project.setModificationUser(operator);
        project.setModificationTimestamp(createDate);
    }

    /**
     * <p>
     * Update the given project instance into the database.
     * </p>
     * <p>
     * The project information is stored to 'project' table, while its
     * properties are stored in 'project_info' table. The project's associating
     * scorecards are stored in 'project_scorecard' table. All related items in
     * these tables will be updated. If items are removed from the project, they
     * will be deleted from the persistence. Likewise, if new items are added,
     * they will be created in the persistence. For the project, its properties
     * and associating scorecards, the operator parameter is used as the
     * modification user and the modification date will be the current date time
     * when the project is updated. An update reason is provided with this
     * method. Update reason will be stored in the persistence for future
     * references.
     * </p>
     * @param project The project instance to be updated into the database.
     * @param reason The update reason. It will be stored in the persistence for
     *            future references.
     * @param operator The modification user of this project.
     * @throws IllegalArgumentException if any input is null or the operator is
     *             empty string. Or project id is zero.
     * @throws PersistenceException if error occurred while accessing the
     *             database.
     */
    public void updateProject(Project project, String reason, String operator)
        throws PersistenceException {
        Helper.assertObjectNotNull(project, "project");
        Helper.assertLongPositive(project.getId(), "project id");
        Helper.assertObjectNotNull(reason, "reason");
        Helper.assertStringNotNullNorEmpty(operator, "operator");

        // modifyDate will contain the modify_date retrieved from database.
        Date modifyDate;

        Connection conn = null;
        
        getLogger().log(Level.INFO, new LogMessage(new Long(project.getId()), operator, 
        		"updating project: " + project.getAllProperties()));
        try {
            // create the connection
            conn = openConnection();

            // check whether the project id is already in the database
            if (!Helper.checkEntityExists("project", "project_id", project
                    .getId(), conn)) {
                throw new PersistenceException("The project id ["
                        + project.getId() + "] does not exist in the database.");
            }

            // update the project
            updateProject(project, reason, operator, conn);

            getLogger().log(Level.INFO, new LogMessage(new Long(project.getId()), operator,
                    "execute sql:" + "SELECT modify_date " + "FROM project WHERE project_id=?"));
            // get the modification date.
            modifyDate = (Date) Helper.doSingleValueQuery(conn,
                    "SELECT modify_date " + "FROM project WHERE project_id=?",
                    new Object[] {new Long(project.getId())},
                    Helper.DATE_TYPE);

            closeConnection(conn);
        } catch (PersistenceException e) {
        	getLogger().log(Level.ERROR,
        			new LogMessage(null, operator, "Fails to update project " + project.getAllProperties(), e));
        	if (conn != null) {
                closeConnectionOnError(conn);
            }
            throw e;
        }

        // set the modification user and date when no exception
        // occurred.
        project.setModificationUser(operator);
        project.setModificationTimestamp(modifyDate);
    }

    /**
     * Retrieves the project instance from the persistence given its id. The
     * project instance is retrieved with its related items, such as properties
     * and scorecards.
     * @return The project instance.
     * @param id The id of the project to be retrieved.
     * @throws IllegalArgumentException if the input id is less than or equal to
     *             zero.
     * @throws PersistenceException if error occurred while accessing the
     *             database.
     */
    public Project getProject(long id) throws PersistenceException {
        Helper.assertLongPositive(id, "id");

        Project[] projects = getProjects(new long[] {id});
        return projects.length == 0 ? null : projects[0];
    }

    /**
     * <p>
     * Retrieves an array of project instance from the persistence given their
     * ids. The project instances are retrieved with their properties.
     * </p>
     * @param ids ids The ids of the projects to be retrieved.
     * @return An array of project instances.
     * @throws PersistenceException
     * @throws IllegalArgumentException if the input id is less than or equal to
     *             zero.
     * @throws PersistenceException if error occurred while accessing the
     *             database.
     */
    public Project[] getProjects(long[] ids) throws PersistenceException {
        Helper.assertObjectNotNull(ids, "ids");

        // check if ids is empty
        if (ids.length == 0) {
            throw new IllegalArgumentException(
                    "Array 'ids' should not be empty.");
        }

        String idstring = "";
        // check if ids contains an id that is <= 0
        for (int i = 0; i < ids.length; ++i) {
        	idstring += ids[i] + ",";
            if (ids[i] <= 0) {
                throw new IllegalArgumentException(
                        "Array 'ids' contains an id that is <= 0.");
            }
        }

        Connection conn = null;

        getLogger().log(Level.INFO, "get projects with the ids: " + idstring.substring(0, idstring.length() - 1));
        try {
            // create the connection
            conn = openConnection();

            // get the project objects
            Project[] projects = getProjects(ids, conn);
            closeConnection(conn);
            return projects;
        } catch (PersistenceException e) {
        	getLogger().log(Level.ERROR, new LogMessage(null, null,
                  "Fails to retrieving projects with ids: " + idstring.substring(0, idstring.length() - 1), e));
            if (conn != null) {
                closeConnectionOnError(conn);
            }
            throw e;
        } catch (ParseException e) {
            getLogger().log(Level.ERROR, new LogMessage(null, null,
                  "Fails to retrieving projects with ids: " + idstring.substring(0, idstring.length() - 1), e));
            if (conn != null) {
                closeConnectionOnError(conn);
            }
            
            throw new PersistenceException("Fails to retrieve projects", e);
        }
    }

    /**
     * Gets an array of all project types in the persistence. The project types
     * are stored in 'project_type_lu' table.
     * @return An array of all project types in the persistence.
     * @throws PersistenceException if error occurred while accessing the
     *             database.
     */
    public ProjectType[] getAllProjectTypes() throws PersistenceException {
        Connection conn = null;

        getLogger().log(Level.INFO, new LogMessage(null,null,"Enter getAllProjectTypes method."));
        try {
            // create the connection
            conn = openConnection();

            // get all the project types
            ProjectType[] projectTypes = getAllProjectTypes(conn);
            closeConnection(conn);
            return projectTypes;
        } catch (PersistenceException e) {
        	getLogger().log(Level.ERROR, new LogMessage(null, null,"Fail to getAllProjectTypes.", e));
            if (conn != null) {
                closeConnectionOnError(conn);
            }
            throw e;
        }
    }

    /**
     * Gets an array of all project categories in the persistence. The project
     * categories are stored in 'project_category_lu' table.
     * @return An array of all project categories in the persistence.
     * @throws PersistenceException if error occurred while accessing the
     *             database.
     */
    public ProjectCategory[] getAllProjectCategories()
        throws PersistenceException {
        Connection conn = null;
        getLogger().log(Level.INFO, new LogMessage(null,null,"Enter getAllProjectCategories method."));
        try {
            // create the connection
            conn = openConnection();

            // get all categories
            ProjectCategory[] projectCategories = getAllProjectCategories(conn);

            closeConnection(conn);
            return projectCategories;
        } catch (PersistenceException e) {
        	getLogger().log(Level.ERROR, new LogMessage(null, null,"Fail to getAllProjectCategories.", e));
            if (conn != null) {
                closeConnectionOnError(conn);
            }
            throw e;
        }
    }

    /**
     * Gets an array of all project statuses in the persistence. The project
     * statuses are stored in 'project_status_lu' table.
     * @return An array of all project statuses in the persistence.
     * @throws PersistenceException if error occurred while accessing the
     *             database.
     */
    public ProjectStatus[] getAllProjectStatuses() throws PersistenceException {
        Connection conn = null;
        getLogger().log(Level.INFO, new LogMessage(null,null,"Enter getAllProjectStatuses method."));
        try {
            // create the connection
            conn = openConnection();

            // get all the project statuses
            ProjectStatus[] projectStatuses = getAllProjectStatuses(conn);
            closeConnection(conn);
            return projectStatuses;
        } catch (PersistenceException e) {
        	getLogger().log(Level.ERROR, new LogMessage(null, null,"Fail to getAllProjectStatuses.", e));
            if (conn != null) {
                closeConnectionOnError(conn);
            }
            throw e;
        }
    }

    /**
     * Gets an array of all project property type in the persistence. The
     * project property types are stored in 'project_info_type_lu' table.
     * @return An array of all scorecard assignments in the persistence.
     * @throws PersistenceException if error occurred while accessing the
     *             database.
     */
    public ProjectPropertyType[] getAllProjectPropertyTypes()
        throws PersistenceException {
    	getLogger().log(Level.INFO, new LogMessage(null,null,"Enter getAllProjectPropertyTypes method."));
        Connection conn = null;

        try {
            // create the connection
            conn = openConnection();

            ProjectPropertyType[] propertyTypes = getAllProjectPropertyTypes(conn);
            closeConnection(conn);
            return propertyTypes;
        } catch (PersistenceException e) {
        	getLogger().log(Level.ERROR, new LogMessage(null, null,"Fail to getAllProjectPropertyTypes.", e));
            if (conn != null) {
                closeConnectionOnError(conn);
            }
            throw e;
        }
    }

	/**
     * Retrieve scorecard id with given project type and scorecard type.
	 *
     * @param projectTypeId   the project type id
     * @param scorecardTypeId the scorecard type id
     * @return the scorecard id
     */
    public long getScorecardId(long projectTypeId, int scorecardTypeId) throws PersistenceException
	{

		getLogger().log(Level.INFO, new LogMessage(null,null,"Enter getScorecardId method."));
        Connection conn = null;
		PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            // create the connection
            conn = openConnection();

            ps = conn.prepareStatement("SELECT scorecard_id FROM default_scorecard " +
                    "WHERE project_category_id = ? and scorecard_type_id = ? ");
            ps.setLong(1, projectTypeId);
            ps.setInt(2, scorecardTypeId);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("scorecard_id");
            } else {
                throw new RuntimeException("Cannot find default scorecard id for project type: " +
                        projectTypeId + ", scorecard type: " + scorecardTypeId);
            }

        } catch (Exception e) {
        	getLogger().log(Level.ERROR, new LogMessage(null, null,"Fail to getScorecardId.", e));
			if (rs != null)
			{
				try
				{
					rs.close();	
				}
				catch (Exception ee)
				{
				}
				
			}
			if (ps != null)
			{
				try
				{
					ps.close();
				}
				catch (Exception ee)
				{
				}
				
			}
            if (conn != null) {
                closeConnectionOnError(conn);
            }
            throw new PersistenceException(e.getMessage());
        }


	}

    /**
     * Returns the database connection name that will be used by DB Connection
     * Factory.
     * @return a possibly <code>null</code> string representing the connection name
     *         used in DB Connection Factory.
     */
    protected String getConnectionName() {
        return connectionName;
    }

    /**
     * returns the <code>DBConnectionFactory</code> instance used to create
     * connection to the database.
     * @return the <code>DBConnectionFactory</code> instance used to create
     *         connection to the database.
     */
    protected DBConnectionFactory getConnectionFactory() {
        return factory;
    }

    /**
     * <p>Return the getLogger().</p>
     * @return the <code>Log</code> instance used to take the log message
     */
    protected abstract Log getLogger();
    /**
     * <p>
     * Abstract method used to get an open connection from which to perform DB
     * operations.
     * </p>
     * <p>
     * The implementations in subclasses will get a connection and properly
     * prepare it for use, depending on the transaction management strategy used
     * in the subclass.
     * </p>
     * @return an open connection used for DB operation.
     * @throws PersistenceException if there's a problem getting the Connection
     */
    protected abstract Connection openConnection() throws PersistenceException;

    /**
     * <p>
     * Abstract method used to commit any transaction (if necessary in the
     * subclass) and close the given connection after an operation completes
     * successfully.
     * </p>
     * <p>
     * It is used by all public methods after the successful execution of DB
     * operation.
     * </p>
     * <p>
     * The implementations in subclasses will close the given connection.
     * Depending on the transaction management strategy used in the subclass, a
     * transaction on the connection may be committed.
     * </p>
     * @param connection connection to close
     * @throws PersistenceException if any problem occurs trying to close the
     *             connection
     * @throws IllegalArgumentException if the argument is <code>null</code>
     */
    protected abstract void closeConnection(Connection connection)
        throws PersistenceException;

    /**
     * <p>
     * Abstract method used to rollback any transaction (if necessary in the
     * subclass) and close the given connection when an error occurs.
     * </p>
     * <p>
     * It is used by all public methods just before any exception is thrown if
     * fails to do DB operation..
     * </p>
     * <p>
     * The implementations in subclasses will close the given connection.
     * Depending on the transaction management strategy used in the subclass, a
     * transaction on the connection may be rolled back.
     * </p>
     * @param connection connection to close
     * @throws PersistenceException if any problem occurs closing the Connection
     * @throws IllegalArgumentException if the argument is <code>null</code>
     */
    protected abstract void closeConnectionOnError(Connection connection)
        throws PersistenceException;

    /**
     * Creates the project in the database using the given project instance.
     * <p>
     * Updated for Cockpit Launch Contest - Update for Spec Creation v1.0
     *      - added creation of project spec.
     * </p>
     * 
     * @param projectId The new generated project id
     * @param project The project instance to be created in the database.
     * @param operator The creation user of this project.
     * @param conn The database connection
     * @throws PersistenceException if error occurred while accessing the
     *             database.
     */
    private void createProject(Long projectId, Project project,
            String operator, Connection conn) throws PersistenceException {

    	getLogger().log(Level.INFO, "insert record into project with id:" + projectId);
    	
    	//set tc_direct_project_id  
    	Long tcDirectProjectId;
        if(project.getTcDirectProjectId() > 0 ) {
        	tcDirectProjectId = new Long(project.getTcDirectProjectId());
        } else {
        	tcDirectProjectId = null;
        }
        
        // insert the project into database
        Object[] queryArgs = new Object[] {projectId,
            new Long(project.getProjectStatus().getId()),
            new Long(project.getProjectCategory().getId()), operator,
            operator, tcDirectProjectId};
        Helper.doDMLQuery(conn, CREATE_PROJECT_SQL, queryArgs);
        
        //
        // Added for Cockpit Launch Contest - Update for Spec Creation v1.0
        //
        createProjectSpec(projectId, project.getProjectSpec(), operator, conn);

        // get the property id - property value map from the project.
        Map idValueMap = makePropertyIdPropertyValueMap(project
                .getAllProperties(), conn);

        // create the project properties
        createProjectProperties(projectId, idValueMap, operator, conn);
    }

    /**
     * <p>
     * Persists the specified ProjectSpec instance for the specified projectId.
     * It basically creates a new row in table corresponding to ProjectSpec - version number for the row is choosen as 1.
     * </p>
     * 
     * @param projectId the project id for which project spec need to be persisted.
     * @param projectSpec the project spec
     * @param operator the operator/user who is creating this project spec.
     * @param conn the db connection to be used to create this project spec
     * @throws PersistenceException exception is thrown when there is some error in persisting 
     * @since Cockpit Launch Contest - Update for Spec Creation v1.0
     */
    private void createProjectSpec(Long projectId, ProjectSpec projectSpec, String operator, Connection conn) throws PersistenceException {
        // check whether the project spec id is already in the database
        if (projectSpec.getProjectSpecId() > 0) {
            if (Helper.checkEntityExists("project_spec", "project_spec_id", projectSpec
                    .getProjectSpecId(), conn)) {
                throw new PersistenceException(
                        "The projectSpec with the same id [" + projectSpec.getProjectSpecId()
                                + "] already exists.");
            }
        }
        
        Long newId = 0L;
        
        try {
            // generate id for the project spec
            newId = new Long(projectSpecIdGenerator.getNextID());
            getLogger().log(Level.INFO, new LogMessage(newId, operator, "generate id for new project spec"));
        } catch (IDGenerationException e) {
            throw new PersistenceException(
                    "Unable to generate id for the project spec.", e);
        }
        
        getLogger().log(Level.INFO, "insert record into project_spec with id:" + newId);
        
        // insert the project spec into database
        Object[] queryArgs = new Object[] {newId,
            projectId, 
			// TODO replace &lt; to <, should not need manually do this
            projectSpec.getDetailedRequirements().replace("&lt;", "<"),
            projectSpec.getSubmissionDeliverables().replace("&lt;", "<"),
            projectSpec.getEnvironmentSetupInstructions().replace("&lt;", "<"),
            projectSpec.getFinalSubmissionGuidelines().replace("&lt;", "<"), 
            operator,
            operator};
        Helper.doDMLQuery(conn, CREATE_PROJECT_SPEC_SQL, queryArgs);
        
        // get the creation date.
        Date specCreateDate = (Date) Helper.doSingleValueQuery(conn,
                "SELECT create_date FROM project_spec WHERE project_spec_id=?",
                new Object[] {newId}, Helper.DATE_TYPE);
        
        // set the newId when no exception occurred
        projectSpec.setProjectSpecId(newId.longValue());

        // set the creation/modification user and date when no exception occurred
        projectSpec.setCreationUser(operator);
        projectSpec.setCreationTimestamp(specCreateDate);
        projectSpec.setModificationUser(operator);
        projectSpec.setModificationTimestamp(specCreateDate);
    }

    /**
     * Update the given project instance into the database.
     * 
     * <p>
     * Updated for Cockpit Launch Contest - Update for Spec Creation v1.0
     *      -- added update for project spec.
     * </p>
     * 
     * @param project The project instance to be updated into the database.
     * @param reason The update reason. It will be stored in the persistence for
     *            future references.
     * @param operator The modification user of this project.
     * @param conn the database connection
     * @throws PersistenceException if error occurred while accessing the
     *             database.
     */
    private void updateProject(Project project, String reason, String operator,
            Connection conn) throws PersistenceException {
        Long projectId = new Long(project.getId());

        getLogger().log(Level.INFO, new LogMessage(projectId, operator,
        		"update project with projectId:" + projectId));
        // 
        Long tcDirectProjectId;
        if(project.getTcDirectProjectId() > 0 ) {
        	tcDirectProjectId = new Long(project.getTcDirectProjectId());
        } else {
        	tcDirectProjectId = null;
        }
        // update the project type and project category
        Object[] queryArgs = new Object[] {
            new Long(project.getProjectStatus().getId()),
            new Long(project.getProjectCategory().getId()), operator, tcDirectProjectId, 
            projectId };
        Helper.doDMLQuery(conn, UPDATE_PROJECT_SQL, queryArgs);
        
        //
        // Added for Cockpit Launch Contest - Update for Spec Creation v1.0
        //
        updateProjectSpec(project.getId(), project.getProjectSpec(), operator, conn);

        // get the property id - property value map from the new project object.
        Map idValueMap = makePropertyIdPropertyValueMap(project
                .getAllProperties(), conn);

        // update the project properties
        updateProjectProperties(projectId, idValueMap, operator, conn);

        // create project audit record into project_audit table
        createProjectAudit(projectId, reason, operator, conn);
    }

    /**
     * <p>
     * Persists the specified ProjectSpec instance for the specified projectId.
     * It basically creates a new row in table corresponding to ProjectSpec - version number for the row is earlier version number + 1.
     * </p>
     * 
     * @param projectId the project id for which project spec need to be persisted.
     * @param projectSpec the project spec
     * @param operator the operator/user who is creating this project spec.
     * @param conn the db connection to be used to create this project spec
     * @throws PersistenceException exception is thrown when there is some error in persisting 
     * @since Cockpit Launch Contest - Update for Spec Creation v1.0
     */
    private void updateProjectSpec(Long projectId, ProjectSpec projectSpec, String operator, Connection conn) throws PersistenceException {
        Long newId = 0L;
        
        try {
            // generate id for the project spec
            newId = new Long(projectSpecIdGenerator.getNextID());
            getLogger().log(Level.INFO, new LogMessage(newId, operator, "generate id for new project spec"));
        } catch (IDGenerationException e) {
            throw new PersistenceException(
                    "Unable to generate id for the project spec.", e);
        }
        
        getLogger().log(Level.INFO, "insert record into project_spec with id:" + newId);
        
        // insert the project spec into database
        Object[] queryArgs = new Object[] {newId,
            projectId, 
            projectId,
			// TODO replace &lt; to <, should not need manually do this
            projectSpec.getDetailedRequirements().replace("&lt;", "<"),
            projectSpec.getSubmissionDeliverables().replace("&lt;", "<"),
            projectSpec.getEnvironmentSetupInstructions().replace("&lt;", "<"),
            projectSpec.getFinalSubmissionGuidelines().replace("&lt;", "<"), 
            operator,
            operator};
        Helper.doDMLQuery(conn, UPDATE_PROJECT_SPEC_SQL, queryArgs);
        
        // get the creation date.
        Date specCreateDate = (Date) Helper.doSingleValueQuery(conn,
                "SELECT create_date FROM project_spec WHERE project_spec_id=?",
                new Object[] {newId}, Helper.DATE_TYPE);
        
        // set the newId when no exception occurred
        projectSpec.setProjectSpecId(newId.longValue());

        // set the creation/modification user and date when no exception occurred
        projectSpec.setCreationUser(operator);
        projectSpec.setCreationTimestamp(specCreateDate);
        projectSpec.setModificationUser(operator);
        projectSpec.setModificationTimestamp(specCreateDate);
    }

    /**
     * Makes a property id-property value(String) map from property
     * name-property value map.
     * @param nameValueMap the property name-property value map
     * @param conn the database connection
     * @return a property id-property value map
     * @throws PersistenceException if error occurred while accessing the
     *             database.
     */
    private Map makePropertyIdPropertyValueMap(Map nameValueMap, Connection conn)
        throws PersistenceException {
        Map idValueMap = new HashMap();

        // get the property name-property id map
        Map nameIdMap = makePropertyNamePropertyIdMap(getAllProjectPropertyTypes(conn));

        // enumerate each property
        for (Iterator it = nameValueMap.entrySet().iterator(); it.hasNext();) {
            Entry entry = (Entry) it.next();

            // find the property id from property name
            Object propertyId = nameIdMap.get(entry.getKey());
            // check if the property id can be found
            if (propertyId == null) {
                throw new PersistenceException(
                        "Unable to find ProjectPropertyType name ["
                                + entry.getKey()
                                + "] in project_info_type_lu table.");
            }

            // put the property id-property value(String) pair into the map
            idValueMap.put(propertyId, entry.getValue().toString());
        }
        return idValueMap;
    }

    /**
     * Gets an array of all project property type in the persistence. The
     * project property types are stored in 'project_info_type_lu' table.
     * @param conn the database connection
     * @return An array of all scorecard assignments in the persistence.
     * @throws PersistenceException if error occurred while accessing the
     *             database.
     */
    private ProjectPropertyType[] getAllProjectPropertyTypes(Connection conn)
        throws PersistenceException {
    	// find all project property types in the table.
        Object[][] rows = Helper.doQuery(conn,
                QUERY_ALL_PROJECT_PROPERTY_TYPES_SQL, new Object[] {},
                QUERY_ALL_PROJECT_PROPERTY_TYPES_COLUMN_TYPES);

        // create the ProjectPropertyType array.
        ProjectPropertyType[] propertyTypes = new ProjectPropertyType[rows.length];

        for (int i = 0; i < rows.length; ++i) {
            Object[] row = rows[i];

            // create a new instance of ProjectPropertyType class
            propertyTypes[i] = new ProjectPropertyType(((Long) row[0])
                    .longValue(), (String) row[1], (String) row[2]);
        }

        return propertyTypes;
    }
    
    /**
     * Build {@link Project} directly from the {@link CustomResultSet}
     * 
     * @param resultSet a {@link CustomResultSet} containing the data for build the {@link Project} instances. 
     * @return an array of {@link Project}
     * @throws PersistenceException if error occurred while accessing the database.
     */
    public Project[] getProjects(CustomResultSet result) throws PersistenceException {
    	Connection conn = null;
        try {
        	conn = openConnection();
        	PreparedStatement ps = conn.prepareStatement(QUERY_ONE_PROJECT_PROPERTIES_SQL);
			int size = result.getRecordCount();
			Project[] projects = new Project[size];
			for (int i = 0; i < size; i++) {
				result.absolute(i + 1);
				
				// create the ProjectStatus object
			    ProjectStatus status = new ProjectStatus(result.getLong(2), result.getString(3));

			    // create the ProjectType object
			    ProjectType type = new ProjectType(result.getLong(6), result.getString(7));

			    // create the ProjectCategory object
			    ProjectCategory category = new ProjectCategory(result.getLong(4), result.getString(5), type);

			    // create a new instance of ProjectType class
			    projects[i] = new Project(result.getLong(1), category, status);

			    // assign the audit information
			    projects[i].setCreationUser(result.getString(8));
			    projects[i].setCreationTimestamp(result.getDate(9));
			    projects[i].setModificationUser(result.getString(10));
			    projects[i].setModificationTimestamp(result.getDate(11));
			    projects[i].setTcDirectProjectId(result.getLong(12));
			    
			    //
	            // Added for Cockpit Launch Contest - Update for Spec Creation v1.0
	            //
	            ProjectSpec[] specs = getProjectSpecs(projects[i].getId(), conn);
	            if (specs != null && specs.length > 0) {
	                projects[i].setProjectSpec(specs[0]);
	            }
			    
			    ps.setLong(1, projects[i].getId());
			    ResultSet rs = ps.executeQuery();
			    while (rs.next()) {
			    	projects[i].setProperty(rs.getString(2), rs.getString(3));
			    }			    
			}
			return projects;
		} catch (InvalidCursorStateException icse) {
            throw new PersistenceException("cursor state is invalid.", icse);
		} catch (SQLException e) {
			throw new PersistenceException(e.getMessage(), e);
		} finally {
			if (conn != null) {
				closeConnection(conn);
			}
		}
	}
    
    /**
     * <p>
     * Retrieves an array of project instance from the persistence given their
     * ids. The project instances are retrieved with their properties.
     * </p>
     * @param ids The ids of the projects to be retrieved.
     * @param conn the database connection
     * @return An array of project instances.
     * @throws PersistenceException if error occurred while accessing the
     *             database.
     * @throws ParseException if error occurred while parsing the result
     */
    private Project[] getProjects(long ids[], Connection conn)
        throws PersistenceException, ParseException {

        // build the id list string
        StringBuffer idListBuffer = new StringBuffer();
        idListBuffer.append('(');
        for (int i = 0; i < ids.length; ++i) {
            if (i != 0) {
                idListBuffer.append(',');
            }
            idListBuffer.append(ids[i]);
        }
        idListBuffer.append(')');
        // get the id list string
        String idList = idListBuffer.toString();

        // find projects in the table.
        Object[][] rows = Helper.doQuery(conn, QUERY_PROJECTS_SQL + idList,
                new Object[] {}, QUERY_PROJECTS_COLUMN_TYPES);

        // create the Project array.
        Project[] projects = getProjects(rows, conn);
        return projects;
    }

    /**
     * Gets an array of all project types in the persistence. The project types
     * are stored in 'project_type_lu' table.
     * @param conn the database connection
     * @return An array of all project types in the persistence.
     * @throws PersistenceException if error occurred while accessing the
     *             database.
     */
    private ProjectType[] getAllProjectTypes(Connection conn)
        throws PersistenceException {
    	// find all project types in the table.
        Object[][] rows = Helper.doQuery(conn, QUERY_ALL_PROJECT_TYPES_SQL,
                new Object[] {}, QUERY_ALL_PROJECT_TYPES_COLUMN_TYPES);

        // create the ProjectType array.
        ProjectType[] projectTypes = new ProjectType[rows.length];

        for (int i = 0; i < rows.length; ++i) {
            Object[] row = rows[i];

            // create a new instance of ProjectType class
            projectTypes[i] = new ProjectType(((Long) row[0]).longValue(),
                    (String) row[1], (String) row[2]);
        }

        return projectTypes;
    }

    /**
     * Create the project properties in the database.
     * @param projectId The new generated project id
     * @param idValueMap The property id - property value map
     * @param operator The creation user of this project
     * @param conn The database connection
     * @throws PersistenceException if error occurred while accessing the
     *             database.
     */
    private void createProjectProperties(Long projectId, Map idValueMap,
            String operator, Connection conn) throws PersistenceException {

    	getLogger().log(Level.INFO, new LogMessage(projectId, operator,
    			"insert record into project_info with project id" + projectId));
        PreparedStatement preparedStatement = null;
        try {
            // prepare the statement.
            preparedStatement = conn
                    .prepareStatement(CREATE_PROJECT_PROPERTY_SQL);

            // enumerator each property in the idValueMap
            for (Iterator it = idValueMap.entrySet().iterator(); it.hasNext();) {
                Entry entry = (Entry) it.next();

                // insert the project property into database
                Object[] queryArgs = new Object[] {projectId, entry.getKey(),
                        entry.getValue(), operator, operator };
                Helper.doDMLQuery(preparedStatement, queryArgs);
            }

        } catch (SQLException e) {
            throw new PersistenceException(
                    "Unable to create prepared statement ["
                            + CREATE_PROJECT_PROPERTY_SQL + "].", e);
        } finally {
            Helper.closeStatement(preparedStatement);
        }
    }

    /**
     * Make an Id-Project map from Project[].
     * @param projects the Id-Project array
     * @return an Id-Project map
     */
    private Map makeIdProjectMap(Project[] projects) {
        Map map = new HashMap();

        for (int i = 0; i < projects.length; ++i) {
            map.put(new Long(projects[i].getId()), projects[i]);
        }
        return map;
    }

    /**
     * Update the project properties into the database.
     * @param projectId the id of the project
     * @param idValueMap the property id - property value map
     * @param operator the modification user of this project
     * @param conn the database connection
     * @throws PersistenceException if error occurred while accessing the
     *             database.
     */
    private void updateProjectProperties(Long projectId, Map idValueMap,
            String operator, Connection conn) throws PersistenceException {
        // get old property ids from database
        Set propertyIdSet = getProjectPropertyIds(projectId, conn);

        // create a property id-property value map that contains the properties
        // to insert
        Map createIdValueMap = new HashMap();

        PreparedStatement preparedStatement = null;
        try {
        	getLogger().log(Level.INFO, new LogMessage(projectId, operator,
             				"update project, update project_info with projectId:" + projectId));
        	 
            // prepare the statement.
            preparedStatement = conn
                    .prepareStatement(UPDATE_PROJECT_PROPERTY_SQL);

            // enumerator each property id in the project object
            for (Iterator it = idValueMap.entrySet().iterator(); it.hasNext();) {
                Entry entry = (Entry) it.next();

                Long propertyId = (Long) entry.getKey();

                // check if the property in the project object already exists in
                // the
                // database
                if (propertyIdSet.contains(propertyId)) {
                    propertyIdSet.remove(propertyId);

                    // update the project property
                    Object[] queryArgs = new Object[] {entry.getValue(),
                        operator, projectId, propertyId };
                    Helper.doDMLQuery(preparedStatement, queryArgs);
                } else {
                    // add the entry to the createIdValueMap
                    createIdValueMap.put(propertyId, entry.getValue());
                }
            }
        } catch (SQLException e) {
            throw new PersistenceException(
                    "Unable to create prepared statement ["
                            + UPDATE_PROJECT_PROPERTY_SQL + "].", e);
        } finally {
            Helper.closeStatement(preparedStatement);
        }

        // create the new properties
        createProjectProperties(projectId, createIdValueMap, operator, conn);

        // delete the remaining property ids that are not in the project object
        // any longer
        deleteProjectProperties(projectId, propertyIdSet, conn);
    }

    /**
     * Gets all the property ids associated to this project.
     * @param projectId The id of this project
     * @param conn The database connection
     * @return A set that contains the property ids
     * @throws PersistenceException if error occurred while accessing the
     *             database.
     */
    private Set getProjectPropertyIds(Long projectId, Connection conn)
        throws PersistenceException {
        Set idSet = new HashSet();

        // find projects in the table.
        Object[][] rows = Helper.doQuery(conn, QUERY_PROJECT_PROPERTY_IDS_SQL,
                new Object[] {projectId},
                QUERY_PROJECT_PROPERTY_IDS_COLUMN_TYPES);

        // enumerator each row
        for (int i = 0; i < rows.length; ++i) {
            Object[] row = rows[i];

            // add the id to the set
            idSet.add(row[0]);
        }
        return idSet;
    }

    /**
     * Delete the project properties from the database.
     * @param projectId the id of the project
     * @param propertyIdSet the Set that contains the property ids to delete
     * @param conn the database connection
     * @throws PersistenceException if error occurred while accessing the
     *             database.
     */
    private void deleteProjectProperties(Long projectId, Set propertyIdSet,
            Connection conn) throws PersistenceException {
        // check if the property id set is empty
        // do nothing if property id set is empty
        if (!propertyIdSet.isEmpty()) {

            // build the id list string
            StringBuffer idListBuffer = new StringBuffer();
            idListBuffer.append('(');
            int idx = 0;
            for (Iterator it = propertyIdSet.iterator(); it.hasNext();) {
                if (idx++ != 0) {
                    idListBuffer.append(',');
                }
                idListBuffer.append(it.next());
            }
            idListBuffer.append(')');

            getLogger().log(Level.INFO, new LogMessage(projectId, null,
            		"delete records from project_info with projectId:" + projectId));
            
            // delete the properties whose id is in the set
            Helper.doDMLQuery(conn, DELETE_PROJECT_PROPERTIES_SQL
                    + idListBuffer.toString(), new Object[] {projectId});
        }
    }

    /**
     * Create a project audit record into the database.
     * @param projectId The id of the project
     * @param reason The update reason
     * @param operator the modification user of this project
     * @param conn the database connection
     * @throws PersistenceException if error occurred while accessing the
     *             database.
     */
    private void createProjectAudit(Long projectId, String reason,
            String operator, Connection conn) throws PersistenceException {

        Long auditId;
        try {
            // generate a new audit id
            auditId = new Long(projectAuditIdGenerator.getNextID());
            getLogger().log(Level.INFO,
            		new LogMessage(projectId, operator, "generate new id for the project audit."));
        } catch (IDGenerationException e) {
            throw new PersistenceException(
                    "Unable to generate id for project.", e);
        }
        getLogger().log(Level.INFO, "insert record into project_audit with projectId:" + projectId);
        // insert the update reason information to project_audit table
        Object[] queryArgs = new Object[] {auditId, projectId, reason,
            operator, operator};
        Helper.doDMLQuery(conn, CREATE_PROJECT_AUDIT_SQL, queryArgs);
    }

    /**
     * Make a property name - property id map from ProjectPropertyType[].
     * @param propertyTypes the ProjectPropertyType array
     * @return a property name - property id map
     * @throws PersistenceException if duplicate property type names are found
     */
    private Map makePropertyNamePropertyIdMap(
            ProjectPropertyType[] propertyTypes) throws PersistenceException {
        Map map = new HashMap();

        // enumerate each property type
        for (int i = 0; i < propertyTypes.length; ++i) {

            // check if the property name already exists
            if (map.containsKey(propertyTypes[i].getName())) {
                throw new PersistenceException(
                        "Duplicate project property type names ["
                                + propertyTypes[i].getName()
                                + "] found in project_info_type_lu table.");
            }

            // put the name-id pair to the map
            map.put(propertyTypes[i].getName(), new Long(propertyTypes[i]
                    .getId()));
        }
        return map;
    }

    /**
     * Gets an array of all project categories in the persistence. The project
     * categories are stored in 'project_category_lu' table.
     * @param conn the database connection
     * @return An array of all project categories in the persistence.
     * @throws PersistenceException if error occurred while accessing the
     *             database.
     */
    private ProjectCategory[] getAllProjectCategories(Connection conn)
        throws PersistenceException {
    	// find all project categories in the table.
        Object[][] rows = Helper.doQuery(conn,
                QUERY_ALL_PROJECT_CATEGORIES_SQL, new Object[] {},
                QUERY_ALL_PROJECT_CATEGORIES_COLUMN_TYPES);

        // create the ProjectCategory array.
        ProjectCategory[] projectCategories = new ProjectCategory[rows.length];

        for (int i = 0; i < rows.length; ++i) {
            Object[] row = rows[i];

            // create the ProjectType object
            ProjectType type = new ProjectType(((Long) row[3]).longValue(),
                    (String) row[4], (String) row[5]);

            // create a new instance of ProjectCategory class
            projectCategories[i] = new ProjectCategory(((Long) row[0])
                    .longValue(), (String) row[1], (String) row[2], type);
        }

        return projectCategories;
    }

    /**
     * Gets an array of all project statuses in the persistence. The project
     * statuses are stored in 'project_status_lu' table.
     * @param conn the database connection
     * @return An array of all project statuses in the persistence.
     * @throws PersistenceException if error occurred while accessing the
     *             database.
     */
    private ProjectStatus[] getAllProjectStatuses(Connection conn)
        throws PersistenceException {
    	// find all project statuses in the table.
        Object[][] rows = Helper.doQuery(conn, QUERY_ALL_PROJECT_STATUSES_SQL,
                new Object[] {}, QUERY_ALL_PROJECT_STATUSES_COLUMN_TYPES);

        // create the ProjectStatus array.
        ProjectStatus[] projectStatuses = new ProjectStatus[rows.length];

        for (int i = 0; i < rows.length; ++i) {
            Object[] row = rows[i];

            // create a new instance of ProjectStatus class
            projectStatuses[i] = new ProjectStatus(((Long) row[0]).longValue(),
                    (String) row[1], (String) row[2]);
        }

        return projectStatuses;
    }
    
    /**
     * <p>
     * Retrieves an array of project instance from the persistence where tc direct 
     * project id is not null. The project instances are retrieved with their properties.
     * </p>
     * 
     * @return An array of project instances.
     * @throws PersistenceException if error occurred while accessing the
     *             database.
     */
	public Project[] getAllTcDirectProject() throws PersistenceException {		

        Connection conn = null;
        try {
            // create the connection
            conn = openConnection();

            // get the project objects
            // find projects in the table.
            Object[][] rows = Helper.doQuery(conn, QUERY_TC_DIRECT_PROJECTS_SQL,
                    new Object[] {}, QUERY_PROJECTS_COLUMN_TYPES);

              
            Project[] projects = getProjects(rows, conn); 
            closeConnection(conn);
            return projects;
        } catch (PersistenceException e) {
        	getLogger().log(Level.ERROR, new LogMessage(null, null,
                  "Fails to retrieving all tc direct projects " , e));
            if (conn != null) {
                closeConnectionOnError(conn);
            }
            throw e;
        }
		
	}
	
	/**
     * <p>
     * Retrieves an array of project instance from the persistence given user.
     * The project instances are retrieved with their properties.
     * </p>
     * @param operator The id of create user.     
     * @return An array of project instances.
     * @throws PersistenceException if error occurred while accessing the
     *             database.
     */
	public Project[] getAllTcDirectProject(String operator) throws PersistenceException {
//		 check if ids is empty
        if (operator == null  ||  operator.length() == 0) {
            throw new IllegalArgumentException("Create user should not be null.");
        }
		Connection conn = null;
        try {
            // create the connection
            conn = openConnection();

            // get the project objects
            // find projects in the table.
            Object[][] rows = Helper.doQuery(conn, QUERY_USER_TC_DIRECT_PROJECTS_SQL,
                    new Object[] {operator}, QUERY_PROJECTS_COLUMN_TYPES);
            
            Project[] projects = getProjects(rows, conn); 
            closeConnection(conn);
            return projects;
        } catch (PersistenceException e) {
        	getLogger().log(Level.ERROR, new LogMessage(null, null,
                  "Fails to retrieving all tc direct projects " , e));
            if (conn != null) {
                closeConnectionOnError(conn);
            }
            throw e;
        }
		
	}

    /**
     * <p>
     * Creates a new contest sale and returns the created contest sale.
     * </p>
     *
     * @param contestSale the contest sale to create
     *
     * @return the created contest sale.
     *
     * @throws IllegalArgumentException if the arg is null.
     * @throws PersistenceException if any other error occurs.
     *
     * @since Module Contest Service Software Contest Sales Assembly
     */
    public ContestSale createContestSale(ContestSale contestSale) throws PersistenceException {
        Helper.assertObjectNotNull(contestSale, "contestSale");

        Connection conn = null;

        // newId will contain the new generated Id for the contest sale
        Long newId;

        getLogger().log(Level.INFO, new LogMessage(null, null, "creating new contest sale"));
        
        try {
            // create the connection
            conn = openConnection();

            // check whether the contest_sale_id is already in the database
            if (contestSale.getContestSaleId() > 0) {
                if (Helper.checkEntityExists("contest_sale", "contest_sale_id", contestSale
                        .getContestSaleId(), conn)) {
                	throw new PersistenceException(
                            "The contest_sale with the same id [" + contestSale.getContestSaleId()
                                    + "] already exists.");
                }
            }

            try {
                // generate id for the contest sale
                newId = new Long(contestSaleIdGenerator.getNextID());
                getLogger().log(Level.INFO, new LogMessage(null, null, "generate id for new contest sale"));
            } catch (IDGenerationException e) {
                throw new PersistenceException(
                        "Unable to generate id for the contest sale.", e);
            }

            // create the contest sale
            Object[] queryArgs = new Object[] {newId, contestSale.getContestId(), contestSale.getStatus().getSaleStatusId(),
            		contestSale.getPrice(), contestSale.getPayPalOrderId(),
            		contestSale.getSaleReferenceId(), contestSale.getSaleType().getSaleTypeId()};
            Helper.doDMLQuery(conn, CREATE_CONTEST_SALE_SQL, queryArgs);
            
            closeConnection(conn);
        } catch (PersistenceException e) {
        	getLogger().log(Level.ERROR,
        			new LogMessage(null, null, "Fails to create contest sale", e));
            if (conn != null) {
                closeConnectionOnError(conn);
            }
            throw e;
        }

        // set the newId when no exception occurred
        contestSale.setContestSaleId(newId.longValue());

        return contestSale;
    }

    /**
     * <p>
     * Gets the sale status by given id.
     * </p>
     *
     * @param saleStatusId the given sale status id.
     *
     * @return the sale status by given id.
     *
     * @throws PersistenceException if any other error occurs.
     *
     * @since Module Contest Service Software Contest Sales Assembly
     */
    public SaleStatus getSaleStatus(long saleStatusId) throws PersistenceException {
        Connection conn = null;
        try {
            // create the connection
            conn = openConnection();

            // get the sale status
            Object[][] rows = Helper.doQuery(conn,
            		QUERY_SALE_STATUS_BY_ID_SQL, new Object[] {saleStatusId},
            		QUERY_SALE_STATUS_BY_ID_COLUMN_TYPES);

            if (rows.length == 0) {
                return null;
            }

            // create a new instance of SaleStatus class
            SaleStatus saleStatus = new SaleStatus();
            saleStatus.setSaleStatusId(((Long) rows[0][0]));
            saleStatus.setDescription((String) rows[0][1]);

            return saleStatus;
        } catch (PersistenceException e) {
        	getLogger().log(Level.ERROR, new LogMessage(null, null,
                  "Fails to retrieve the sale status" , e));
            if (conn != null) {
                closeConnectionOnError(conn);
            }
            throw e;
        }
    }

    /**
     * <p>
     * Gets the sale type by given id.
     * </p>
     *
     * @param saleTypeId the given sale type id.
     *
     * @return the sale type by given id.
     *
     * @throws PersistenceException if any other error occurs.
     *
     * @since Module Contest Service Software Contest Sales Assembly
     */
    public SaleType getSaleType(long saleTypeId) throws PersistenceException {
        Connection conn = null;
        try {
            // create the connection
            conn = openConnection();

            // get the sale type
            Object[][] rows = Helper.doQuery(conn,
            		QUERY_SALE_TYPE_BY_ID_SQL, new Object[] {saleTypeId},
            		QUERY_SALE_TYPE_BY_ID_COLUMN_TYPES);

            if (rows.length == 0) {
                return null;
            }

            // create a new instance of SaleType class
            SaleType saleType = new SaleType();
            saleType.setSaleTypeId(((Long) rows[0][0]));
            saleType.setDescription((String) rows[0][1]);

            return saleType;
        } catch (PersistenceException e) {
        	getLogger().log(Level.ERROR, new LogMessage(null, null,
                  "Fails to retrieve the sale type" , e));
            if (conn != null) {
                closeConnectionOnError(conn);
            }
            throw e;
        }
    }

    /**
     * <p>
     * Gets contest sale by id, and return the retrieved contest sale. If
     * the contest sale doesn't exist, null is returned.
     * </p>
     *
     * @param contestSaleId the contest sale id
     *
     * @return the retrieved contest sale, or null if id doesn't exist
     *
     * @throws PersistenceException if any other error occurs.
     *
     * @since Module Contest Service Software Contest Sales Assembly
     */
    public ContestSale getContestSale(long contestSaleId) throws PersistenceException {
        Connection conn = null;
        try {
            // create the connection
            conn = openConnection();

            // get the contest sale
            Object[][] rows = Helper.doQuery(conn,
            		QUERY_CONTEST_SALE_BY_ID_SQL, new Object[] {contestSaleId},
            		QUERY_CONTEST_SALE_BY_ID_COLUMN_TYPES);

            if (rows.length == 0) {
                return null;
            }

            // create a new instance of ContestSale class
            ContestSale contestSale = new ContestSale();
            contestSale.setContestSaleId(((Long) rows[0][0]));
            contestSale.setContestId((Long) rows[0][1]);
            contestSale.setStatus(this.getSaleStatus((Long) rows[0][2]));
            contestSale.setPrice((Double) rows[0][3]);
            contestSale.setPayPalOrderId((String) rows[0][4]);
            contestSale.setCreateDate((Date) rows[0][5]);
            contestSale.setSaleReferenceId((String) rows[0][6]);
            contestSale.setSaleType(this.getSaleType((Long) rows[0][7]));

            return contestSale;
        } catch (PersistenceException e) {
        	getLogger().log(Level.ERROR, new LogMessage(null, null,
                  "Fails to retrieve the contest sale" , e));
            if (conn != null) {
                closeConnectionOnError(conn);
            }
            throw e;
        }
    }

    /**
     * <p>
     * Gets contest sales by contest id, and return the retrieved contest sales.
     * </p>
     *
     * @param contestId the contest id of the contest sale
     *
     * @return the retrieved contest sales, or empty if none exists
     *
     * @throws PersistenceException if any other error occurs.
     *
     * @since Module Contest Service Software Contest Sales Assembly
     */
    public List<ContestSale> getContestSales(long contestId) throws PersistenceException {
        Connection conn = null;
        try {
            // create the connection
            conn = openConnection();

            // get the contest sale
            Object[][] rows = Helper.doQuery(conn,
            		QUERY_CONTEST_SALE_BY_CONTEST_ID_SQL, new Object[] {contestId},
            		QUERY_CONTEST_SALE_BY_CONTEST_ID_COLUMN_TYPES);

            List<ContestSale> ret = new ArrayList<ContestSale>();

            for (int i = 0; i < rows.length; i++) {
                // create a new instance of ContestSale class
                ContestSale contestSale = new ContestSale();
                contestSale.setContestSaleId(((Long) rows[i][0]));
                contestSale.setContestId((Long) rows[i][1]);
                contestSale.setStatus(this.getSaleStatus((Long) rows[i][2]));
                contestSale.setPrice((Double) rows[i][3]);
                contestSale.setPayPalOrderId((String) rows[i][4]);
                contestSale.setCreateDate((Date) rows[i][5]);
                contestSale.setSaleReferenceId((String) rows[i][6]);
                contestSale.setSaleType(this.getSaleType((Long) rows[i][7]));

                ret.add(contestSale);
            }

            return ret;
        } catch (PersistenceException e) {
        	getLogger().log(Level.ERROR, new LogMessage(null, null,
                  "Fails to retrieve the contest sales" , e));
            if (conn != null) {
                closeConnectionOnError(conn);
            }
            throw e;
        }
    }

    /**
     * <p>
     * Updates the contest sale data.
     * </p>
     *
     * @param contestSale the contest sale to update
     *
     * @throws IllegalArgumentException if the arg is null.
     * @throws PersistenceException if any other error occurs.
     *
     * @since Module Contest Service Software Contest Sales Assembly
     */
    public void editContestSale(ContestSale contestSale) throws PersistenceException {
        Helper.assertObjectNotNull(contestSale, "contestSale");

        Connection conn = null;

        getLogger().log(Level.INFO, new LogMessage(null, null, "updating contest sale"));
        try {
            // create the connection
            conn = openConnection();

            // check whether the contest_sale_id is already in the database
            if (!Helper.checkEntityExists("contest_sale", "contest_sale_id", contestSale
                    .getContestSaleId(), conn)) {
                throw new PersistenceException("The contest_sale id ["
                        + contestSale.getContestSaleId() + "] does not exist in the database.");
            }

            // update the contest sale
            Object[] queryArgs = new Object[] {
                contestSale.getContestId(), contestSale.getStatus().getSaleStatusId(), contestSale.getPrice(), contestSale.getPayPalOrderId(),
                contestSale.getSaleReferenceId(), contestSale.getSaleType().getSaleTypeId(), 
                contestSale.getContestSaleId()};
            Helper.doDMLQuery(conn, UPDATE_CONTEST_SALE_SQL, queryArgs);

            closeConnection(conn);
        } catch (PersistenceException e) {
        	getLogger().log(Level.ERROR,
        			new LogMessage(null, null, "Fails to update contest sale", e));
        	if (conn != null) {
                closeConnectionOnError(conn);
            }
            throw e;
        }
    }

    /**
     * <p>
     * Removes contest sale, return true if the contest sale exists and
     * removed successfully, return false if it doesn't exist.
     * </p>
     *
     * @param contestSaleId the contest sale id
     *
     * @return true if the contest sale exists and removed successfully,
     *         return false if it doesn't exist
     *
     * @throws PersistenceException if any other error occurs.
     *
     * @since Module Contest Service Software Contest Sales Assembly
     */
    public boolean removeContestSale(long contestSaleId) throws PersistenceException {
        Connection conn = null;

        getLogger().log(Level.INFO, new LogMessage(null, null, "removing contest sale"));
        try {
            // create the connection
            conn = openConnection();

            // check whether the contest_sale_id is already in the database
            if (!Helper.checkEntityExists("contest_sale", "contest_sale_id", contestSaleId, conn)) {
                return false;
            }

            // remove the contest sale
            Object[] queryArgs = new Object[] {contestSaleId};
            Helper.doDMLQuery(conn, DELETE_CONTEST_SALE_BY_ID_SQL, queryArgs);

            closeConnection(conn);

            return true;
        } catch (PersistenceException e) {
        	getLogger().log(Level.ERROR,
        			new LogMessage(null, null, "Fails to remove contest sale", e));
        	if (conn != null) {
                closeConnectionOnError(conn);
            }
            throw e;
        }
    }
	/**
	 * Retrieves an array of project
	 * The project instances are retrieved with their properties.
	 * @param rows
	 * @param conn the database connection
	 * @return An array of project instances.
	 * @throws PersistenceException
	 */
	
	private Project[] getProjects(Object [][]rows, Connection conn) throws PersistenceException {
		
		// 	create the Project array.
        Project[] projects = new Project[rows.length];
        
        // if no tc direct project found
        if(projects.length == 0 ) {        	
            return projects;
        }

        for (int i = 0; i < rows.length; ++i) {
            Object[] row = rows[i];

            // create the ProjectStatus object
            ProjectStatus status = new ProjectStatus(((Long) row[1])
                    .longValue(), (String) row[2]);

            // create the ProjectType object
            ProjectType type = new ProjectType(((Long) row[5]).longValue(),
                    (String) row[6]);

            // create the ProjectCategory object
            ProjectCategory category = new ProjectCategory(((Long) row[3])
                    .longValue(), (String) row[4], type);
            category.setDescription((String) row[11]);
            // create a new instance of ProjectType class
            projects[i] = new Project(((Long) row[0]).longValue(), category,
                    status);

            // assign the audit information
            projects[i].setCreationUser((String) row[7]);
            projects[i].setCreationTimestamp((Date) row[8]);
            projects[i].setModificationUser((String) row[9]);
            projects[i].setModificationTimestamp((Date) row[10]);
            projects[i].setTcDirectProjectId(((Long)row[12]).longValue());
            
            //
            // Added for Cockpit Launch Contest - Update for Spec Creation v1.0
            //
            ProjectSpec[] specs = getProjectSpecs(projects[i].getId(), conn);
            if (specs != null && specs.length > 0) {
                projects[i].setProjectSpec(specs[0]);
            }
        }

        // get the Id-Project map
        Map projectMap = makeIdProjectMap(projects);
        String ids = projectMap.keySet().toString();
        ids = ids.replace('[', '(');
        ids = ids.replace(']', ')');
        
        // find project properties in the table.
        rows = Helper.doQuery(conn, QUERY_PROJECT_PROPERTIES_SQL + ids ,
                new Object[] {}, QUERY_PROJECT_PROPERTIES_COLUMN_TYPES);

        // enumerate each row
        for (int i = 0; i < rows.length; ++i) {
            Object[] row = rows[i];

            // get the corresponding Project object
            Project project = (Project) projectMap.get(row[0]);

            // set the property to project
            project.setProperty((String) row[1], (String)row[2]);
        }         
        return projects;
        
	}
	
	/**
     * <p>
     * Gets the list of ProjectSpec for the specified projectId.
     * Though the method signature is to return list, but it essentially returns the ProjectSpec corresponding to the latest version id
     * for the specified projectId's spec.
     * </p>
     * 
     * @param projectId the project id for which project spec need to be persisted.
     * @param conn the db connection to be used to create this project spec
     * 
     * @return the list of ProjectSpec for the specified projectId.
     * @throws PersistenceException exception is thrown when there is some error in persisting 
     * @since Cockpit Launch Contest - Update for Spec Creation v1.0
     */
	private ProjectSpec[] getProjectSpecs(Long projectId, Connection conn) throws PersistenceException {
	     // get the project objects
        // find projects in the table.
        Object[][] rows = Helper.doQuery(conn,
                QUERY_PROJECT_SPEC_SQL, new Object[] {projectId, projectId},
                QUERY_PROJECT_SPEC_COLUMN_TYPES);
        
        if (rows == null || rows.length == 0) {
            return null;
        }
        
        ProjectSpec[] specs = new ProjectSpec[rows.length];
        for (int i = 0; i < rows.length; i++) {
            ProjectSpec spec = new ProjectSpec();
         
            spec.setProjectSpecId((Long) rows[i][0]);
            spec.setProjectId(projectId);
            spec.setVersion((Long) rows[i][1]);
            spec.setDetailedRequirements((String) rows[i][2]);
            spec.setSubmissionDeliverables((String) rows[i][3]);
            spec.setEnvironmentSetupInstructions((String) rows[i][4]);
            spec.setFinalSubmissionGuidelines((String) rows[i][5]);
            spec.setCreationUser((String) rows[i][6]);
            spec.setCreationTimestamp((Date) rows[i][7]);
            spec.setModificationUser((String) rows[i][8]);
            spec.setModificationTimestamp((Date) rows[i][9]);
            
            specs[i] = spec;
        }
        
        return specs;
	}
	
	/**
     * <p>
     * Gets the full list of contests.
     * </p>
     * 
     * <p>
     * Updated for Cockpit Release Assembly 3 [RS: 1.1.1]
     *     - project and contest permissions are also fetched now.
     * </p>
     * 
     * @return the full list of contests.
     * @throws PersistenceException exception is thrown when there is error retrieving the list from persistence.
     * @throws ParseException exception is thrown when there is error in parsing the results retrieved from persistence.
     */
	public List<SimpleProjectContestData> getSimpleProjectContestData() throws PersistenceException {

		Connection conn = null;
		try {
			// create the connection
			conn = openConnection();

			// get the project objects
			// find projects in the table.
			Object[][] rows = Helper.doQuery(conn,
					QUERY_ALL_SIMPLE_PROJECT_CONTEST, new Object[] {},
					this.QUERY_ALL_SIMPLE_PROJECT_CONTEST_COLUMN_TYPES);

			SimpleProjectContestData[] ret = new SimpleProjectContestData[rows.length];
			 SimpleDateFormat myFmt=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

			 List<SimpleProjectContestData> result = new ArrayList<SimpleProjectContestData>();

			for(int i=0;i<rows.length;i++)
			{
				ret[i]=new SimpleProjectContestData();
				ret[i].setContestId((Long)rows[i][0]);
				// if have phase, use phase as stutus, otherwise use project status
				/* [BUGR-2038]: See comments at the class level. Status is either 'Scheduled'
				  or 'Draft' or from the DB as done previously.*/

				// try to use phase if not null
				if (rows[i][1] != null)
				{
					ret[i].setSname((String)rows[i][1]);
				}
				// else for active, use 'newstatus'
				else if (rows[i][15] != null && ((String)rows[i][6]).equalsIgnoreCase(PROJECT_STATUS_ACTIVE))
				{
					ret[i].setSname((String)rows[i][15]);
				}
				// use status
				else
				{
					ret[i].setSname((String)rows[i][6]);
				}
				
				ret[i].setCname((String)rows[i][2]);
				if (rows[i][3] != null)
				{
					ret[i].setStartDate(myFmt.parse(rows[i][3].toString()));
				}
				if (rows[i][4] != null)
				{
					ret[i].setEndDate(myFmt.parse(rows[i][4].toString()));
				}
				
				ret[i].setType((String)rows[i][5]);
				ret[i].setNum_reg(new Integer(((Long)rows[i][7]).intValue()));
				ret[i].setNum_sub(new Integer(((Long)rows[i][8]).intValue()));
				ret[i].setNum_for(new Integer(((Long)rows[i][9]).intValue()));
				ret[i].setProjectId((Long)rows[i][10]);
				ret[i].setPname((String)rows[i][11]);
				ret[i].setDescription((String)rows[i][12]);
				ret[i].setCreateUser((String)rows[i][13]);
				if (rows[i][14] != null)
				{
					ret[i].setForumId(new Integer(((Long)rows[i][14]).intValue()));
				}
				
				if (rows[i][16] != null) {
				    ret[i].setCperm((String) rows[i][16]);
				}
				
				if (rows[i][17] != null) {
                    ret[i].setPperm((String) rows[i][17]);
                }

				if (ret[i].getCperm() != null || ret[i].getPperm() != null)
				{
					result.add(ret[i]);
				}
				
			}
			return Arrays.asList(ret);
		} catch (PersistenceException e) {
			getLogger().log(
					Level.ERROR,
					new LogMessage(null, null,
							"Fails to retrieving all tc direct projects ", e));
			if (conn != null) {
				closeConnectionOnError(conn);
			}
			throw e;
		} catch (ParseException e) {
			getLogger().log(
					Level.ERROR,
					new LogMessage(null, null,
							"Fails to retrieving all tc direct projects ", e));
			if (conn != null) {
				closeConnectionOnError(conn);
			}
			throw new PersistenceException("Fails to retrieve all tc direct projects", e);
		}

	}

	/**
     * <p>
     * Gets the list of contest for specified tc project id.
     * </p>
     * 
     * <p>
     * Updated for Cockpit Release Assembly 3 [RS: 1.1.1]
     *     - project and contest permissions are also fetched now.
     * </p>
     * 
     * @param pid the specified tc project id for which to get the list of contest.
     * @return the list of contest for specified tc project id.
     * @throws PersistenceException exception is thrown when there is error retrieving the list from persistence.
     * @throws ParseException exception is thrown when there is error in parsing the results retrieved from persistence.
     */
	public List<SimpleProjectContestData> getSimpleProjectContestData(long pid) throws PersistenceException {

		Connection conn = null;
		try {
			// create the connection
			conn = openConnection();

			// get the project objects
			// find projects in the table.
			Object[][] rows = Helper.doQuery(conn,
					QUERY_ALL_SIMPLE_PROJECT_CONTEST_BY_PID+pid, new Object[] {},
					this.QUERY_ALL_SIMPLE_PROJECT_CONTEST_COLUMN_TYPES);

			SimpleProjectContestData[] ret = new SimpleProjectContestData[rows.length];
			 SimpleDateFormat myFmt=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

			List<SimpleProjectContestData> result = new ArrayList<SimpleProjectContestData>();


			for(int i=0;i<rows.length;i++)
			{
				ret[i]=new SimpleProjectContestData();
				ret[i].setContestId((Long)rows[i][0]);
				/* [BUGR-2038]: See comments at the class level. Status is either 'Scheduled'
				  or 'Draft' or from the DB as done previously.*/

				// try to use phase if not null
				if (rows[i][1] != null)
				{
					ret[i].setSname((String)rows[i][1]);
				}
				// else for active, use 'newstatus'
				else if (rows[i][15] != null && ((String)rows[i][6]).equalsIgnoreCase(PROJECT_STATUS_ACTIVE))
				{
					ret[i].setSname((String)rows[i][15]);
				}
				// use status
				else
				{
					ret[i].setSname((String)rows[i][6]);
				}
				
				ret[i].setCname((String)rows[i][2]);
				if (rows[i][3] != null)
				{
					ret[i].setStartDate(myFmt.parse(rows[i][3].toString()));
				}
				if (rows[i][4] != null)
				{
					ret[i].setEndDate(myFmt.parse(rows[i][4].toString()));
				}
				
				ret[i].setType((String)rows[i][5]);
				ret[i].setNum_reg(new Integer(((Long)rows[i][7]).intValue()));
				ret[i].setNum_sub(new Integer(((Long)rows[i][8]).intValue()));
				ret[i].setNum_for(new Integer(((Long)rows[i][9]).intValue()));
				ret[i].setProjectId((Long)rows[i][10]);
				ret[i].setPname((String)rows[i][11]);
				ret[i].setDescription((String)rows[i][12]);
				ret[i].setCreateUser((String)rows[i][13]);
				if (rows[i][14] != null)
				{
					ret[i].setForumId(new Integer(((Long)rows[i][14]).intValue()));
				}
				
				if (rows[i][16] != null) {
				    ret[i].setCperm((String) rows[i][16]);
				}
				
				if (rows[i][17] != null) {
                    ret[i].setPperm((String) rows[i][17]);
                }

				if (ret[i].getCperm() != null || ret[i].getPperm() != null)
				{
					result.add(ret[i]);
				}
				
			}
			return Arrays.asList(ret);
		} catch (PersistenceException e) {
			getLogger().log(
					Level.ERROR,
					new LogMessage(null, null,
							"Fails to retrieving all tc direct projects ", e));
			if (conn != null) {
				closeConnectionOnError(conn);
			}
			throw e;
		} catch (ParseException e) {
			getLogger().log(
					Level.ERROR,
					new LogMessage(null, null,
							"Fails to retrieving all tc direct projects ", e));
			if (conn != null) {
				closeConnectionOnError(conn);
			}
			throw new PersistenceException("Fails to retrieve all tc direct projects", e);
		}

	}
	
	/**
	 * <p>
	 * Gets the list of contest for specified user.
	 * </p>
	 * 
	 * <p>
	 * Updated for Cockpit Release Assembly 3 [RS: 1.1.1]
	 *     - project and contest permissions are also fetched now.
	 * </p>
	 * 
	 * @param createdUser the specified user for which to get the list of contest.
	 * @return the list of contest for specified user.
	 * @throws PersistenceException exception is thrown when there is error retrieving the list from persistence.
	 * @throws ParseException exception is thrown when there is error in parsing the results retrieved from persistence.
	 */
	public List<SimpleProjectContestData> getSimpleProjectContestDataByUser(String createdUser) throws PersistenceException {

		Connection conn = null;
		try {
			// create the connection
			conn = openConnection();


			String qstr = 
			" select p.project_id as contest_id, "
			+		" (select ptl.name from phase_type_lu ptl where phase_type_id = (select min(phase_type_id) from project_phase ph " 
			+ " where ph.phase_status_id = 2 and ph.project_id=p.project_id)) as current_phase, "
			+ "(select value from project_info where project_id = p.project_id and project_info_type_id =6) as contest_name, "
			+ "(select min(nvl(actual_start_time, scheduled_start_time)) from project_phase ph where ph.project_id=p.project_id) as start_date, "
			+ " (select max(nvl(actual_end_time, scheduled_end_time)) from project_phase ph where ph.project_id=p.project_id) as end_date, "
			+ "  pcl.name as contest_type, psl.name as status, "
			+ " 0 as num_reg, "
			+ " 0 as num_sub, "
			+ " 0 as num_for , "
			+ " tc_direct_project_id as project_id, tcd.name, tcd.description, tcd.user_id, "
			+ "  (select value from project_info where project_id = p.project_id and project_info_type_id =4) as forum_id, "
			+ "  (select case when(count(*)>=1) then 'Scheduled' when(count(*)=0) then 'Draft' end "
			+ "   from contest_sale c where p.project_id = c.contest_id and upper(psl.name)='ACTIVE' ) as newstatus, "


			+ " (select name from permission_type where permission_type_id= NVL( (select max( permission_type_id)  "
			+ " from user_permission_grant as upg  where resource_id=p.project_id and is_studio=0 and user_id = " + createdUser
			+ " ),0)) as cperm, "

			+ " (select name from permission_type where permission_type_id= NVL( (select max( permission_type_id)  "
			+ " from user_permission_grant as upg  where resource_id=tcd.project_id and user_id = " + createdUser
			+ " ),0)) as pperm "
					
			+ " from project p, project_category_lu pcl, project_status_lu psl, tc_direct_project tcd "
			+ " where p.project_category_id = pcl.project_category_id and p.project_status_id = psl.project_status_id and p.tc_direct_project_id = tcd.project_id "
			+" and p.project_status_id != 3";

			// get the project objects
			// find projects in the table.
			Object[][] rows = Helper.doQuery(conn,
					qstr, new Object[] {},
					this.QUERY_ALL_SIMPLE_PROJECT_CONTEST_COLUMN_TYPES);

			SimpleProjectContestData[] ret = new SimpleProjectContestData[rows.length];
			 SimpleDateFormat myFmt=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

			 List<SimpleProjectContestData> result = new ArrayList<SimpleProjectContestData>();

			for(int i=0;i<rows.length;i++)
			{
				ret[i]=new SimpleProjectContestData();
				ret[i].setContestId((Long)rows[i][0]);
				/* [BUGR-2038]: See comments at the class level. Status is either 'Scheduled'
				  or 'Draft' or from the DB as done previously.*/

				// try to use phase if not null
				if (rows[i][1] != null)
				{
					ret[i].setSname((String)rows[i][1]);
				}
				// else for active, use 'newstatus'
				else if (rows[i][15] != null && ((String)rows[i][6]).equalsIgnoreCase(PROJECT_STATUS_ACTIVE))
				{
					ret[i].setSname((String)rows[i][15]);
				}
				// use status
				else
				{
					ret[i].setSname((String)rows[i][6]);
				}
				
				ret[i].setCname((String)rows[i][2]);
				if (rows[i][3] != null)
				{
					ret[i].setStartDate(myFmt.parse(rows[i][3].toString()));
				}
				if (rows[i][4] != null)
				{
					ret[i].setEndDate(myFmt.parse(rows[i][4].toString()));
				}
				
				ret[i].setType((String)rows[i][5]);
				ret[i].setNum_reg(new Integer(((Long)rows[i][7]).intValue()));
				ret[i].setNum_sub(new Integer(((Long)rows[i][8]).intValue()));
				ret[i].setNum_for(new Integer(((Long)rows[i][9]).intValue()));
				ret[i].setProjectId((Long)rows[i][10]);
				ret[i].setPname((String)rows[i][11]);
				ret[i].setDescription((String)rows[i][12]);
				ret[i].setCreateUser((String)rows[i][13]);
				if (rows[i][14] != null)
				{
					ret[i].setForumId(new Integer(((Long)rows[i][14]).intValue()));
				}
				
				if (rows[1][16] != null) {
                    ret[i].setCperm((String) rows[i][16]);
                }
                
                if (rows[1][17] != null) {
                    ret[i].setPperm((String) rows[i][17]);
                }

				if (ret[i].getCperm() != null || ret[i].getPperm() != null)
				{
					result.add(ret[i]);
				}

				
				
			}
			return result;
		} catch (PersistenceException e) {
			getLogger().log(
					Level.ERROR,
					new LogMessage(null, null,
							"Fails to retrieving all tc direct projects ", e));
			if (conn != null) {
				closeConnectionOnError(conn);
			}
			throw e;
		} catch (ParseException e) {
			getLogger().log(
					Level.ERROR,
					new LogMessage(null, null,
							"Fails to retrieving all tc direct projects ", e));
			if (conn != null) {
				closeConnectionOnError(conn);
			}
			throw new PersistenceException("Fails to retrieve all tc direct projects", e);
		}

	}
	
	/**
     * <p>
     * Gets the list of project their read/write/full permissions.
     * </p>
     * 
     * <p>
     * Updated for Cockpit Release Assembly 3 [RS: 1.1.3]
     *      - Added check for is_studio=0 whenever user_permission_grant is joined with project table.
     * </p>
     * 
     * @param createdUser
     *            the specified user for which to get the permission
     * @return the list of project their read/write/full permissions.
     * 
     * @throws PersistenceException exception if error during retrieval from database.
     * 
     * @since Cockpit Project Admin Release Assembly v1.0
     */
    public List<SimpleProjectPermissionData> getSimpleProjectPermissionDataForUser(long createdUser)
            throws PersistenceException {

        Connection conn = null;

        try {
            // create the connection
            conn = openConnection();

            String qstr = "select project_id as contest_id, " +
            		" (select pinfo.value from project_info as pinfo where pinfo.project_id = c.project_id and pinfo.project_info_type_id = 6) as name, "
                    + " tc_direct_project_id, "
                    + " ( select name from tc_direct_project p where c.tc_direct_project_id = p.project_id) as pname, "
                    + " (select count( *)  from user_permission_grant as upg  where resource_id=c.tc_direct_project_id  and user_id= "
                    + createdUser
                    + " and permission_type_id=1 ) as pread, "
                    + " (select count( *)  from user_permission_grant as upg  where resource_id=c.tc_direct_project_id  and user_id=  "
                    + createdUser
                    + " and permission_type_id=2 ) as pwrite, "
                    + " (select count( *)  from user_permission_grant as upg  where resource_id=c.tc_direct_project_id  and user_id=  "
                    + createdUser
                    + " and permission_type_id=3 ) as pfull, "
                    + " (select count( *)  from user_permission_grant as upg  where resource_id=c.project_id and is_studio=0 and user_id=  "
                    + createdUser
                    + " and permission_type_id=4 ) as cread, "
                    + " (select count( *)  from user_permission_grant as upg  where resource_id=c.project_id and is_studio=0 and user_id=  "
                    + createdUser
                    + " and permission_type_id=5 ) as cwrite, "
                    + " (select count( *)  from user_permission_grant as upg  where resource_id=c.project_id and is_studio=0 and user_id=  "
                    + createdUser
                    + " and permission_type_id=6 ) as cfull "
                    + " from project c  "
                    + " where not c.tc_direct_project_id is null ";

            Object[][] rows = Helper.doQuery(conn, qstr, new Object[] {},
                    this.QUERY_SIMPLE_PROJECT_PERMISSION_DATA_COLUMN_TYPES);

            List<SimpleProjectPermissionData> result = new ArrayList<SimpleProjectPermissionData>();

            for (int i = 0; i < rows.length; i++) {

                SimpleProjectPermissionData c = new SimpleProjectPermissionData();
                c.setStudio(false);
                Object[] os = rows[i];
                
                // skip records that doesn't have tc project names.
                if (os[3] == null || os[3].equals("")) {
                    continue;
                }

                if (os[0] != null)
                    c.setContestId((Long) os[0]);
                if (os[1] != null)
                    c.setCname(os[1].toString());
                if (os[2] != null)
                    c.setProjectId((Long) os[2]);
                if (os[3] != null)
                    c.setPname(os[3].toString());

                if (createdUser < 0) {
                    // admin
                    c.setPfull(1);
                    c.setCfull(1);
                    result.add(c);
                    continue;
                }

                int pp = 0;
                if (os[4] != null) {
                    c.setPread(((Long) os[4]).intValue());
                    pp++;
                }
                if (os[5] != null) {
                    c.setPwrite(((Long) os[5]).intValue());
                    pp++;
                }
                if (os[6] != null) {
                    c.setPfull(((Long) os[6]).intValue());
                    pp++;
                }
                int cp = 0;
                if (os[7] != null) {
                    c.setCread(((Long) os[7]).intValue());
                    cp++;
                }
                if (os[8] != null) {
                    c.setCwrite(((Long) os[8]).intValue());
                    cp++;
                }
                if (os[9] != null) {
                    c.setCfull(((Long) os[9]).intValue());
                    cp++;
                }
                if (pp > 0 || cp > 0) {
                    result.add(c);
                }
            }
            return result;
        } catch (PersistenceException e) {
            getLogger().log(Level.ERROR, new LogMessage(null, null, "Fails to retrieving all tc direct projects ", e));
            if (conn != null) {
                closeConnectionOnError(conn);
            }
            throw e;
        } 
    }
}
