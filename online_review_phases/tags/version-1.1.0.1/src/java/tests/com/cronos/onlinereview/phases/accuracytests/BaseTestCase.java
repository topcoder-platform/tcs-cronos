/*
 * Copyright (C) 2009 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.phases.accuracytests;

import com.topcoder.date.workdays.DefaultWorkdays;

import com.topcoder.db.connectionfactory.DBConnectionFactory;
import com.topcoder.db.connectionfactory.DBConnectionFactoryImpl;

import com.topcoder.management.deliverable.Submission;
import com.topcoder.management.deliverable.SubmissionStatus;
import com.topcoder.management.deliverable.Upload;
import com.topcoder.management.deliverable.UploadStatus;
import com.topcoder.management.deliverable.UploadType;
import com.topcoder.management.resource.Resource;
import com.topcoder.management.resource.ResourceRole;
import com.topcoder.management.review.data.Comment;
import com.topcoder.management.review.data.CommentType;
import com.topcoder.management.review.data.Item;
import com.topcoder.management.review.data.Review;
import com.topcoder.management.scorecard.data.Scorecard;
import com.topcoder.management.scorecard.data.ScorecardStatus;
import com.topcoder.management.scorecard.data.ScorecardType;

import com.topcoder.project.phases.Dependency;
import com.topcoder.project.phases.Phase;
import com.topcoder.project.phases.PhaseStatus;
import com.topcoder.project.phases.PhaseType;
import com.topcoder.project.phases.Project;

import com.topcoder.util.config.ConfigManager;

import junit.framework.TestCase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

import java.util.Date;
import java.util.Iterator;


/**
 * Defines methods to do usual operations.
 *
 * @author myxgyy
 * @version 1.0
 */
abstract class BaseTestCase extends TestCase {
    /** constant for db connection configuration file. */
    private static final String DB_FACTORY_CONFIG_FILE = "accuracy/ConnectionFactory.xml";

    /** constant for logging wrapper configuration file */
    private static final String LOGGING_WRAPPER_CONFIG_FILE = "accuracy/Logging_Wrapper.xml";

    /** constant for manager helper configuration file. */
    private static final String PHASE_HANDLER_CONFIG_FILE = "accuracy/Phase_Handler.xml";

    /** constant for manager helper configuration file. */
    private static final String MANAGER_HELPER_CONFIG_FILE = "accuracy/ManagerHelperConfig.xml";

    /** array of all the config file names for various dependency components. */
    private static final String[] COMPONENT_FILE_NAMES = new String[] {
            "accuracy/ProjectManagement.xml", "accuracy/PhaseManagement.xml",
            "accuracy/ReviewManagement.xml", "accuracy/ScorecardManagement.xml",
            "accuracy/ScreeningManagement.xml",
            "accuracy/ResourceUploadSearchBundleManager.xml",
            "accuracy/UserProjectDataStore.xml",
            "accuracy/ReviewScorecardAggregator.xml",
            "accuracy/SearchBuilderCommon.xml"
        };

    /**
     * An array of table names to be cleaned in setup() and tearDown()
     * methods.
     */
    private static final String[] ALL_TABLE_NAMES = new String[] {
        "project_user_audit", "screening_result", "screening_task", 
        "project_phase_audit", "project_info_audit",
        "notification", "project_audit", "review_item_comment",
        "review_comment", "review_item", "review",
        "resource_submission", "submission", "upload",
        "resource_info", "resource", "phase_criteria",
        "phase_dependency", "project_phase", "project_scorecard",
        "project_info", "project", "scorecard_question",
        "scorecard_section", "scorecard_group", "scorecard",
        "comp_forum_xref", "comp_versions", "categories",
        "comp_catalog", "user_reliability", "user_rating", "user",
        "email"};

    /** constant for namespace to test other phase handlers with. */
    static final String PHASE_HANDLER_NAMESPACE = "com.cronos.onlinereview.phases.Handler";

    /** Represents the configuration manager instance used in tests. */
    private ConfigManager configManager;

    /** Holds db connection factory instance. */
    private DBConnectionFactory dbFactory;

    /** holds connection. */
    private Connection connection;

    /**
     * <p>Sets up the test environment.</p>
     *
     * @throws Exception pass any unexpected exception to JUnit.
     */
    protected void setUp() throws Exception {
        configManager = ConfigManager.getInstance();

        clearAllNamespace();
        // init db factory
        configManager.add(DB_FACTORY_CONFIG_FILE);

        // load logging wrapper configuration
        configManager.add(LOGGING_WRAPPER_CONFIG_FILE);
        configManager.add(PHASE_HANDLER_CONFIG_FILE);

        configManager.add(MANAGER_HELPER_CONFIG_FILE);

        // add the component configurations as well
        for (int i = 0; i < COMPONENT_FILE_NAMES.length; i++) {
            configManager.add(COMPONENT_FILE_NAMES[i]);
        }

        dbFactory = new DBConnectionFactoryImpl(DBConnectionFactoryImpl.class
                .getName());
        cleanTables();
    }

    /**
     * <p>Cleans up the test environment.</p>
     *
     * @throws Exception pass any unexpected exception to JUnit.
     */
    protected void tearDown() throws Exception {
        clearAllNamespace();
        cleanTables();
        closeConnection();
        connection = null;
        dbFactory = null;
        configManager = null;
    }

    /**
     * Clear all cofiguration namepsaces.
     *
     * @throws Exception to JUnit.
     */
    protected void clearAllNamespace() throws Exception {
        ConfigManager configManager = ConfigManager.getInstance();

        Iterator iter = configManager.getAllNamespaces();

        while (iter.hasNext()) {
            configManager.removeNamespace((String) iter.next());
        }
    }

    /**
     * Helper method to create a phase instance.
     *
     * @param phaseId phase id.
     * @param phaseStatusId phase Status Id.
     * @param phaseStatusName phase Status Name.
     * @param phaseTypeId phase Type Id.
     * @param phaseTypeName phase Type Name.
     *
     * @return phase instance.
     */
    protected Phase createPhase(long phaseId, long phaseStatusId,
        String phaseStatusName, long phaseTypeId, String phaseTypeName) {
        Project project = new Project(new Date(), new DefaultWorkdays());
        project.setId(1);

        Phase phase = new Phase(project, 2000);
        phase.setId(phaseId);
        phase.setPhaseStatus(new PhaseStatus(phaseStatusId, phaseStatusName));
        phase.setPhaseType(new PhaseType(phaseTypeId, phaseTypeName));

        return phase;
    }

    /**
     * Helper method to create Resource instance.
     *
     * @param resourceId resource Id.
     * @param phaseId phase Id.
     * @param projectId project Id.
     * @param resourceRoleId resource Role Id.
     *
     * @return Resource instance.
     */
    protected Resource createResource(long resourceId, long phaseId,
        long projectId, long resourceRoleId) {
        Resource resource2 = new Resource();
        resource2.setId(resourceId);
        resource2.setPhase(new Long(phaseId));
        resource2.setProject(new Long(projectId));
        resource2.setResourceRole(new ResourceRole(resourceRoleId));

        return resource2;
    }

    /**
     * Helper method to create Upload instance.
     *
     * @param uploadId upload id.
     * @param projectId project id.
     * @param resourceId resource id.
     * @param uploadTypeId upload type id.
     * @param uploadStatusId upload status id.
     * @param parameter parameter.
     *
     * @return Upload instance.
     */
    protected Upload createUpload(long uploadId, long projectId,
        long resourceId, long uploadTypeId, long uploadStatusId,
        String parameter) {
        Upload upload = new Upload();
        upload.setId(uploadId);
        upload.setProject(projectId);
        upload.setOwner(resourceId);
        upload.setUploadType(new UploadType(uploadTypeId));
        upload.setUploadStatus(new UploadStatus(uploadStatusId));
        upload.setParameter(parameter);
        upload.setCreationTimestamp(new Date());

        return upload;
    }

    /**
     * Helper method to create Submission instance.
     *
     * @param submissionId submission id.
     * @param uploadId upload id.
     * @param submissionStatusId submission status id.
     *
     * @return Submission instance.
     */
    protected Submission createSubmission(long submissionId, long uploadId,
        long submissionStatusId) {
        Submission submission = new Submission(submissionId);
        submission.setUpload(new Upload(uploadId));
        submission.setSubmissionStatus(new SubmissionStatus(submissionStatusId));
        submission.setIntialScore(89.92d);
        submission.setFinalScore(93.92d);
        submission.setPlacement(new Long(2));
        submission.setScreeningScore(100.00d);

        return submission;
    }

    /**
     * Helper method to create Scorecard instance.
     *
     * @param scorecardId scorecard id.
     * @param scorecardStatusId scorecard status id.
     * @param scorecardTypeId scorecard type id.
     * @param projectCategoryId project category id.
     * @param name name.
     * @param version version.
     * @param minScore min score.
     * @param maxScore max score.
     *
     * @return Scorecard instance.
     */
    protected Scorecard createScorecard(long scorecardId,
        long scorecardStatusId, long scorecardTypeId, long projectCategoryId,
        String name, String version, float minScore, float maxScore) {
        Scorecard scorecard = new Scorecard(scorecardId);
        scorecard.setScorecardStatus(new ScorecardStatus(scorecardStatusId));
        scorecard.setScorecardType(new ScorecardType(scorecardTypeId));
        scorecard.setCategory(projectCategoryId);
        scorecard.setName(name);
        scorecard.setVersion(version);
        scorecard.setMinScore(minScore);
        scorecard.setMaxScore(maxScore);

        return scorecard;
    }

    /**
     * Helper method to create Review instance.
     *
     * @param reviewId review id.
     * @param resourceId resource id.
     * @param submissionId submission id.
     * @param scorecardId scorecard id.
     * @param committed committed.
     * @param score score.
     *
     * @return Review instance.
     */
    protected Review createReview(long reviewId, long resourceId,
        long submissionId, long scorecardId, boolean committed, float score) {
        Review review = new Review(reviewId);
        review.setAuthor(resourceId);
        review.setSubmission(submissionId);
        review.setScorecard(scorecardId);
        review.setCommitted(committed);
        review.setScore(new Float(score));

        return review;
    }

    /**
     * Helper method to create Comment instance.
     *
     * @param id comment id
     * @param author author of comment.
     * @param sComment comment text.
     * @param commentTypeId comment type id.
     * @param commentType comment type.
     *
     * @return Comment instance.
     */
    protected Comment createComment(long id, long author, String sComment,
        long commentTypeId, String commentType) {
        Comment comment = new Comment(id);
        comment.setAuthor(author);
        comment.setComment(sComment);
        comment.setCommentType(new CommentType(commentTypeId, commentType));

        return comment;
    }

    /**
     * Helper method to create a review item instance.
     *
     * @param id review item id.
     * @param answer answer.
     * @param reviewId review id.
     * @param questionId question id.
     *
     * @return review item instance.
     */
    protected Item createReviewItem(long id, String answer, long reviewId,
        long questionId) {
        Item item = new Item(id);
        item.setAnswer(answer);
        item.setDocument(new Long(reviewId));
        item.setQuestion(questionId);

        return item;
    }

    /**
     * Returns a connection instance.
     *
     * @return a connection instance.
     *
     * @throws Exception not for this test case.
     */
    protected Connection getConnection() throws Exception {
        if (connection == null) {
            connection = dbFactory.createConnection();
        }

        return connection;
    }

    /**
     * Closes the connection.
     */
    protected void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException ex) {
                // do nothing.
            }
        }

        connection = null;
    }

    /**
     * helper method to close a statement.
     *
     * @param stmt statement to close.
     */
    protected void closeStatement(Statement stmt) {
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException ex) {
                // do nothing
            }
        }
    }

    /**
     * inserts a project into the database. Inserts records into the
     * project, comp_catalog and comp_versions tables.
     *
     * @param conn connection to use.
     *
     * @throws Exception not under test.
     */
    protected void insertProject(Connection conn) throws Exception {
        PreparedStatement preparedStmt = null;

        try {
            // insert a project
            String insertProject = "insert into project(project_id, project_status_id, project_category_id,"
                + "create_user, create_date, modify_user, modify_date) values "
                + "(1, 1, 1, 'user', ?, 'user', ?)";
            preparedStmt = conn.prepareStatement(insertProject);
            preparedStmt.setTimestamp(1,
                new Timestamp(System.currentTimeMillis()));
            preparedStmt.setTimestamp(2,
                new Timestamp(System.currentTimeMillis()));
            preparedStmt.executeUpdate();
            closeStatement(preparedStmt);
            preparedStmt = null;

            // insert into comp_catalog
            String insertCatalog = "insert into comp_catalog(component_id, current_version, component_name,"
                + "description, create_time, status_id) values "
                + "(1, 1, 'Online Review Phases', 'Online Review Phases', ?, 1)";
            preparedStmt = conn.prepareStatement(insertCatalog);
            preparedStmt.setTimestamp(1,
                new Timestamp(System.currentTimeMillis()));
            preparedStmt.executeUpdate();
            closeStatement(preparedStmt);
            preparedStmt = null;

            // insert into comp_catalog
            String insertVersion = "insert into comp_versions(comp_vers_id, component_id, version,version_text,"
                + "create_time, phase_id, phase_time, price, comments) values "
                + "(1, 1, 1, '1.0', ?, 112, ?, 500, 'Comments')";
            preparedStmt = conn.prepareStatement(insertVersion);
            preparedStmt.setTimestamp(1,
                new Timestamp(System.currentTimeMillis()));
            preparedStmt.setTimestamp(2,
                new Timestamp(System.currentTimeMillis()));
            preparedStmt.executeUpdate();
            closeStatement(preparedStmt);
            preparedStmt = null;
        } finally {
            closeStatement(preparedStmt);
        }
    }

    /**
     * inserts project properties into the database. Inserts records
     * into the project_info table.
     *
     * @param conn connection to use.
     * @param projectId project id.
     * @param infoTypes array of project info type ids.
     * @param infoValues array of corresponding project info values.
     *
     * @throws Exception not under test.
     */
    protected void insertProjectInfo(Connection conn, long projectId,
        long[] infoTypes, String[] infoValues) throws Exception {
        PreparedStatement preparedStmt = null;

        try {
            // insert a project info
            String insertProjectInfo = "insert into project_info(project_id, project_info_type_id, value,"
                + "create_user, create_date, modify_user, modify_date) values "
                + "(?, ?, ?, 'user', ?, 'user', ?)";
            preparedStmt = conn.prepareStatement(insertProjectInfo);

            for (int i = 0; i < infoTypes.length; i++) {
                preparedStmt.setLong(1, projectId);
                preparedStmt.setLong(2, infoTypes[i]);
                preparedStmt.setString(3, infoValues[i]);
                preparedStmt.setTimestamp(4,
                    new Timestamp(System.currentTimeMillis()));
                preparedStmt.setTimestamp(5,
                    new Timestamp(System.currentTimeMillis()));
                preparedStmt.executeUpdate();
            }

            closeStatement(preparedStmt);
            preparedStmt = null;
        } finally {
            closeStatement(preparedStmt);
        }
    }

    /**
     * inserts a project and the standard phases into the database.
     *
     * @return project instance with phases populated.
     *
     * @throws Exception not under test.
     */
    protected Project setupPhases() throws Exception {
        Connection conn = getConnection();
        PreparedStatement preparedStmt = null;
        Project project = null;

        try {
            project = new Project(new Date(), new DefaultWorkdays());
            project.setId(1);

            // insert project first
            insertProject(conn);

            String insertPhase = "insert into project_phase(project_phase_id, project_id, phase_type_id, phase_status_id,"
                + "scheduled_start_time, scheduled_end_time, duration,"
                + " create_user, create_date, modify_user, modify_date)"
                + "values (?, 1, ?, 1, ?, ?, ?, 'user', ?, 'user', ?)";
            preparedStmt = conn.prepareStatement(insertPhase);

            // insert all standard phases
            long[] phaseIds = new long[] {
                    101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111
                };
            long[] phaseTypeIds = new long[] {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11};
            String[] phaseTypeNames = new String[] {
                    "Registration", "Submission", "Screening", "Review",
                    "Appeals", "Appeals Response", "Aggregation",
                    "Aggregation Review", "Final Fix", "Final Review",
                    "Approval"
                };
            long now = System.currentTimeMillis();
            Timestamp scheduledStart = new Timestamp(now);
            long duration = 24 * 60 * 60 * 1000; // one day
            Timestamp scheduledEnd = new Timestamp(now + duration);

            for (int i = 0; i < phaseIds.length; i++) {
                // insert into db
                preparedStmt.setLong(1, phaseIds[i]);
                preparedStmt.setLong(2, phaseTypeIds[i]);
                preparedStmt.setTimestamp(3, scheduledStart);
                preparedStmt.setTimestamp(4, scheduledEnd);
                preparedStmt.setLong(5, duration);
                preparedStmt.setTimestamp(6, new Timestamp(now));
                preparedStmt.setTimestamp(7, new Timestamp(now));
                preparedStmt.executeUpdate();

                // create phase intance
                Phase phase = new Phase(project, duration);
                phase.setId(phaseIds[i]);
                phase.setPhaseType(new PhaseType(phaseTypeIds[i],
                        phaseTypeNames[i]));
                phase.setPhaseStatus(PhaseStatus.SCHEDULED);
                phase.setActualStartDate(scheduledStart);
                phase.setActualEndDate(scheduledEnd);
                phase.setScheduledStartDate(scheduledStart);
                phase.setScheduledEndDate(scheduledEnd);

                project.addPhase(phase);

                // re-calculate scheduled start and end.
                scheduledStart = new Timestamp(scheduledEnd.getTime());
                scheduledEnd = new Timestamp(scheduledStart.getTime()
                        + duration);
            }

            closeStatement(preparedStmt);
            preparedStmt = null;

            // insert dependencies
            String insertDependency = "INSERT INTO phase_dependency "
                + "(dependency_phase_id, dependent_phase_id, dependency_start, dependent_start, lag_time,"
                + " create_user, create_date, modify_user, modify_date)"
                + "VALUES (?, ?, ?, ?, ?, 'user', ?, 'user', ?)";
            preparedStmt = conn.prepareStatement(insertDependency);

            long[] dependencyPhaseIds = new long[] {
                    101, 102, 103, 104, 105, 106, 107, 108, 109, 110
                };
            long[] dependentPhaseIds = new long[] {
                    102, 103, 104, 105, 106, 107, 108, 109, 110, 111
                };
            Phase[] phases = project.getAllPhases();

            for (int i = 0; i < dependencyPhaseIds.length; i++) {
                preparedStmt.setLong(1, dependencyPhaseIds[i]);
                preparedStmt.setLong(2, dependentPhaseIds[i]);
                preparedStmt.setBoolean(3, false);
                preparedStmt.setBoolean(4, true);
                preparedStmt.setLong(5, 0);
                preparedStmt.setTimestamp(6, new Timestamp(now));
                preparedStmt.setTimestamp(7, new Timestamp(now));
                preparedStmt.executeUpdate();

                Dependency dependency = new Dependency(phases[i],
                        phases[i + 1], false, true, 0);
                phases[i + 1].addDependency(dependency);
            }

            closeStatement(preparedStmt);
            preparedStmt = null;
        } finally {
            closeStatement(preparedStmt);
            closeConnection();
        }

        return project;
    }

    /**
     * Creates submission for the project at the screening phase.
     *
     * @throws Exception to JUnit.
     *
     * @since 1.1
     */
    protected void setupSubmissionForScreening() throws Exception {
        Connection con = getConnection();

        // Prepare submitter and insert into DB
        Resource submitter = new Resource();
        submitter.setId(1);
        submitter.setResourceRole(new ResourceRole(1));
        submitter.setProject(new Long(1));
        submitter.setPhase(new Long(102));

        insertResources(con, new Resource[] {submitter});

        // Prepare upload and insert into DB
        Upload upload = new Upload();
        upload.setId(1);
        upload.setProject(1);
        upload.setOwner(1);
        upload.setUploadType(new UploadType(1));
        upload.setUploadStatus(new UploadStatus(1));
        upload.setParameter("param");

        insertUploads(con, new Upload[] {upload});

        // Prepare submission and insert into DB
        Submission submission = new Submission();
        submission.setId(1);
        submission.setUpload(upload);
        submission.setIntialScore(89.92d);
        submission.setFinalScore(93.92d);
        submission.setPlacement(new Long(2));
        submission.setScreeningScore(100.00d);
        submission.setSubmissionStatus(new SubmissionStatus(1));

        insertSubmissions(con, new Submission[] {submission});
    }

    /**
     * Creates submission for the project at review phase.
     *
     * @throws Exception to JUnit.
     *
     * @since 1.1
     */
    protected void setupSubmissionForReview() throws Exception {
        Connection con = getConnection();

        // Prepare submitter and insert into DB
        Resource submitter = new Resource();
        submitter.setId(1);
        submitter.setResourceRole(new ResourceRole(1));
        submitter.setProject(new Long(1));
        submitter.setPhase(new Long(103));

        insertResources(con, new Resource[] {submitter});

        // Prepare upload and insert into DB
        Upload upload = new Upload();
        upload.setId(1);
        upload.setProject(1);
        upload.setOwner(1);
        upload.setUploadType(new UploadType(1));
        upload.setUploadStatus(new UploadStatus(1));
        upload.setParameter("param");

        insertUploads(con, new Upload[] {upload});

        // Prepare submission and insert into DB
        Submission submission = new Submission();
        submission.setId(1);
        submission.setUpload(upload);
        submission.setSubmissionStatus(new SubmissionStatus(1));

        insertSubmissions(con, new Submission[] {submission});
    }

    /**
     * inserts resources required by test cases into the db.
     *
     * @param conn connection to use.
     * @param resources resources to insert.
     *
     * @throws Exception not under test.
     */
    protected void insertResources(Connection conn, Resource[] resources)
        throws Exception {
        PreparedStatement preparedStmt = null;

        try {
            String insertResource = "INSERT INTO resource "
                + "(resource_id, resource_role_id, project_id, project_phase_id,"
                + "create_user, create_date, modify_user, modify_date) "
                + "VALUES (?, ?, ?, ?, 'user', ?, 'user', ?)";
            preparedStmt = conn.prepareStatement(insertResource);

            Timestamp now = new Timestamp(System.currentTimeMillis());

            for (int i = 0; i < resources.length; i++) {
                preparedStmt.setLong(1, resources[i].getId());
                preparedStmt.setLong(2, resources[i].getResourceRole().getId());
                preparedStmt.setLong(3, resources[i].getProject().longValue());
                preparedStmt.setLong(4, resources[i].getPhase().longValue());
                preparedStmt.setTimestamp(5, now);
                preparedStmt.setTimestamp(6, now);
                preparedStmt.executeUpdate();
            }

            closeStatement(preparedStmt);
            preparedStmt = null;
        } finally {
            closeStatement(preparedStmt);
        }
    }

    /**
     * inserts uploads required by test cases into the db.
     *
     * @param conn connection to use.
     * @param uploads uploads to insert.
     *
     * @throws Exception not under test.
     */
    protected void insertUploads(Connection conn, Upload[] uploads)
        throws Exception {
        PreparedStatement preparedStmt = null;

        try {
            String insertUpload = "INSERT INTO upload "
                + "(upload_id, project_id, resource_id, upload_type_id, upload_status_id, parameter,"
                + "create_user, create_date, modify_user, modify_date) "
                + "VALUES (?, ?, ?, ?, ?, ?, 'user', ?, 'user', ?)";
            preparedStmt = conn.prepareStatement(insertUpload);

            Timestamp now = new Timestamp(System.currentTimeMillis());

            for (int i = 0; i < uploads.length; i++) {
                preparedStmt.setLong(1, uploads[i].getId());
                preparedStmt.setLong(2, uploads[i].getProject());
                preparedStmt.setLong(3, uploads[i].getOwner());
                preparedStmt.setLong(4, uploads[i].getUploadType().getId());
                preparedStmt.setLong(5, uploads[i].getUploadStatus().getId());
                preparedStmt.setString(6, uploads[i].getParameter());
                preparedStmt.setTimestamp(7, now);
                preparedStmt.setTimestamp(8, now);
                preparedStmt.executeUpdate();
            }

            closeStatement(preparedStmt);
            preparedStmt = null;
        } finally {
            closeStatement(preparedStmt);
        }
    }

    /**
     * inserts uploads required by test cases into the db.
     *
     * @param conn connection to use.
     * @param submissions submissions to insert.
     *
     * @throws Exception not under test.
     */
    protected void insertSubmissions(Connection conn, Submission[] submissions)
        throws Exception {
        PreparedStatement preparedStmt = null;

        try {
            String insertSubmission = "INSERT INTO submission "
                + "(submission_id, upload_id, submission_status_id, "
                + "create_user, create_date, modify_user, modify_date,"
                + " placement, initial_score, final_score, screening_score) "
                + "VALUES (?, ?, ?, 'user', ?, 'user', ?, ?, ?, ?, ?)";
            preparedStmt = conn.prepareStatement(insertSubmission);

            Timestamp now = new Timestamp(System.currentTimeMillis());

            for (int i = 0; i < submissions.length; i++) {
                preparedStmt.setLong(1, submissions[i].getId());
                preparedStmt.setLong(2, submissions[i].getUpload().getId());
                preparedStmt.setLong(3,
                    submissions[i].getSubmissionStatus().getId());
                preparedStmt.setTimestamp(4, now);
                preparedStmt.setTimestamp(5, now);

                preparedStmt.setDouble(6,
                    (submissions[i].getPlacement() == null) ? new Long(0)
                                                            : submissions[i]
                    .getPlacement());
                preparedStmt.setDouble(7, submissions[i].getInitialScore());
                preparedStmt.setDouble(8, submissions[i].getFinalScore());
                preparedStmt.setDouble(9, submissions[i].getScreeningScore());
                preparedStmt.executeUpdate();
            }

            closeStatement(preparedStmt);
            preparedStmt = null;
        } finally {
            closeStatement(preparedStmt);
        }
    }

    /**
     * inserts scorecards required by test cases into the db.
     *
     * @param conn connection to use.
     * @param scorecards scorecards to insert.
     *
     * @throws Exception not under test.
     */
    protected void insertScorecards(Connection conn, Scorecard[] scorecards)
        throws Exception {
        PreparedStatement preparedStmt = null;

        try {
            String insertScorecard = "INSERT INTO scorecard "
                + "(scorecard_id, scorecard_status_id, scorecard_type_id, project_category_id,"
                + "name, version, min_score, max_score,"
                + "create_user, create_date, modify_user, modify_date) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, 'user', ?, 'user', ?)";
            preparedStmt = conn.prepareStatement(insertScorecard);

            Timestamp now = new Timestamp(System.currentTimeMillis());

            for (int i = 0; i < scorecards.length; i++) {
                preparedStmt.setLong(1, scorecards[i].getId());
                preparedStmt.setLong(2,
                    scorecards[i].getScorecardStatus().getId());
                preparedStmt.setLong(3, scorecards[i].getScorecardType().getId());
                preparedStmt.setLong(4, scorecards[i].getCategory());
                preparedStmt.setString(5, scorecards[i].getName());
                preparedStmt.setString(6, scorecards[i].getVersion());
                preparedStmt.setFloat(7, scorecards[i].getMinScore());
                preparedStmt.setFloat(8, scorecards[i].getMaxScore());
                preparedStmt.setTimestamp(9, now);
                preparedStmt.setTimestamp(10, now);
                preparedStmt.executeUpdate();
            }

            closeStatement(preparedStmt);
            preparedStmt = null;
        } finally {
            closeStatement(preparedStmt);
        }
    }

    /**
     * inserts reviews required by test cases into the db.
     *
     * @param conn connection to use.
     * @param reviews reviews to insert.
     *
     * @throws Exception not under test.
     */
    protected void insertReviews(Connection conn, Review[] reviews)
        throws Exception {
        PreparedStatement preparedStmt = null;

        try {
            String insertReview = "INSERT INTO review"
                + "(review_id, resource_id, submission_id, scorecard_id, committed, score,"
                + "create_user, create_date, modify_user, modify_date) "
                + "VALUES (?, ?, ?, ?, ?, ?, 'user', ?, 'user', ?)";
            preparedStmt = conn.prepareStatement(insertReview);

            Timestamp now = new Timestamp(System.currentTimeMillis());

            for (int i = 0; i < reviews.length; i++) {
                preparedStmt.setLong(1, reviews[i].getId());
                preparedStmt.setLong(2, reviews[i].getAuthor());
                preparedStmt.setLong(3, reviews[i].getSubmission());
                preparedStmt.setLong(4, reviews[i].getScorecard());
                preparedStmt.setBoolean(5, reviews[i].isCommitted());
                preparedStmt.setFloat(6, reviews[i].getScore().floatValue());
                preparedStmt.setTimestamp(7, now);
                preparedStmt.setTimestamp(8, now);
                preparedStmt.executeUpdate();
            }

            closeStatement(preparedStmt);
            preparedStmt = null;
        } finally {
            closeStatement(preparedStmt);
        }
    }

    /**
     * Helper method to insert Comment into the database.
     *
     * @param conn connection to use
     * @param ids comment id
     * @param authors author of comment.
     * @param reviewIds review id.
     * @param sComments comment text.
     * @param commentTypeIds comment type id.
     *
     * @throws Exception not under test.
     */
    protected void insertComments(Connection conn, long[] ids, long[] authors,
        long[] reviewIds, String[] sComments, long[] commentTypeIds)
        throws Exception {
        PreparedStatement preparedStmt = null;

        try {
            String insertReview = "INSERT INTO review_comment"
                + "(review_comment_id, resource_id, review_id, comment_type_id, content, sort,"
                + "create_user, create_date, modify_user, modify_date) "
                + "VALUES (?, ?, ?, ?, ?, 1, 'user', ?, 'user', ?)";
            preparedStmt = conn.prepareStatement(insertReview);

            Timestamp now = new Timestamp(System.currentTimeMillis());

            for (int i = 0; i < ids.length; i++) {
                preparedStmt.setLong(1, ids[i]);
                preparedStmt.setLong(2, authors[i]);
                preparedStmt.setLong(3, reviewIds[i]);
                preparedStmt.setLong(4, commentTypeIds[i]);
                preparedStmt.setString(5, sComments[i]);
                preparedStmt.setTimestamp(6, now);
                preparedStmt.setTimestamp(7, now);
                preparedStmt.executeUpdate();
            }

            closeStatement(preparedStmt);
            preparedStmt = null;
        } finally {
            closeStatement(preparedStmt);
        }
    }

    /**
     * Helper method to insert Comment with extra info into the
     * database.
     *
     * @param conn connection to use
     * @param ids comment id
     * @param authors author of comment.
     * @param reviewIds review id.
     * @param sComments comment text.
     * @param commentTypeIds comment type id.
     * @param extraInfos extra info
     *
     * @throws Exception not under test.
     */
    protected void insertCommentsWithExtraInfo(Connection conn, long[] ids,
        long[] authors, long[] reviewIds, String[] sComments,
        long[] commentTypeIds, String[] extraInfos) throws Exception {
        PreparedStatement preparedStmt = null;

        try {
            String insertReview = "INSERT INTO review_comment"
                + "(review_comment_id, resource_id, review_id, comment_type_id, content, sort, extra_info,"
                + "create_user, create_date, modify_user, modify_date) "
                + "VALUES (?, ?, ?, ?, ?, 1, ?, 'user', ?, 'user', ?)";
            preparedStmt = conn.prepareStatement(insertReview);

            Timestamp now = new Timestamp(System.currentTimeMillis());

            for (int i = 0; i < ids.length; i++) {
                preparedStmt.setLong(1, ids[i]);
                preparedStmt.setLong(2, authors[i]);
                preparedStmt.setLong(3, reviewIds[i]);
                preparedStmt.setLong(4, commentTypeIds[i]);
                preparedStmt.setString(5, sComments[i]);
                preparedStmt.setString(6, extraInfos[i]);
                preparedStmt.setTimestamp(7, now);
                preparedStmt.setTimestamp(8, now);
                preparedStmt.executeUpdate();
            }

            closeStatement(preparedStmt);
            preparedStmt = null;
        } finally {
            closeStatement(preparedStmt);
        }
    }

    /**
     * Helper method to insert a question. This inserts a record into
     * the scorecard_group, scorecard_section, scorecard_question tables.
     *
     * @param conn connection to use.
     * @param questionId scorecard question id.
     * @param scorecardId scorecard id.
     *
     * @throws Exception not under test.
     */
    protected void insertScorecardQuestion(Connection conn, long questionId,
        long scorecardId) throws Exception {
        PreparedStatement stmt1 = null;
        PreparedStatement stmt2 = null;
        PreparedStatement stmt3 = null;

        try {
            Timestamp now = new Timestamp(System.currentTimeMillis());
            String insertGroup = "INSERT INTO scorecard_group"
                + "(scorecard_group_id, scorecard_id, name, weight, sort, "
                + "create_user, create_date, modify_user, modify_date) "
                + "VALUES (1, ?, 'group name', 1, 1, 'user', ?, 'user', ?)";
            stmt1 = conn.prepareStatement(insertGroup);
            stmt1.setLong(1, scorecardId);
            stmt1.setTimestamp(2, now);
            stmt1.setTimestamp(3, now);
            stmt1.executeUpdate();
            closeStatement(stmt1);
            stmt1 = null;

            String insertSection = "INSERT INTO scorecard_section"
                + "(scorecard_section_id, scorecard_group_id, name, weight, sort, "
                + "create_user, create_date, modify_user, modify_date) "
                + "VALUES (1, 1, 'section name', 1, 1, 'user', ?, 'user', ?)";
            stmt2 = conn.prepareStatement(insertSection);
            stmt2.setTimestamp(1, now);
            stmt2.setTimestamp(2, now);
            stmt2.executeUpdate();
            closeStatement(stmt2);
            stmt2 = null;

            String insertQues = "INSERT INTO scorecard_question"
                + "(scorecard_question_id, scorecard_question_type_id, scorecard_section_id, description, weight,"
                + "sort, upload_document, upload_document_required,"
                + "create_user, create_date, modify_user, modify_date) "
                + "VALUES (?, 1, 1, 'question desc', 1, 1, 1, 1, 'user', ?, 'user', ?)";
            stmt3 = conn.prepareStatement(insertQues);

            stmt3.setLong(1, questionId);
            stmt3.setTimestamp(2, now);
            stmt3.setTimestamp(3, now);
            stmt3.executeUpdate();

            closeStatement(stmt3);
            stmt3 = null;
        } finally {
            closeStatement(stmt1);
            closeStatement(stmt2);
            closeStatement(stmt3);
        }
    }

    /**
     * Helper method to insert review item into the database.
     *
     * @param conn connection to use
     * @param items array of review items to insert.
     *
     * @throws Exception not under test.
     */
    protected void insertReviewItems(Connection conn, Item[] items)
        throws Exception {
        PreparedStatement preparedStmt = null;

        try {
            String insertReview = "INSERT INTO review_item"
                + "(review_item_id, review_id, scorecard_question_id, upload_id, answer, sort,"
                + "create_user, create_date, modify_user, modify_date) "
                + "VALUES (?, ?, ?, ?, ?, 1, 'user', ?, 'user', ?)";
            preparedStmt = conn.prepareStatement(insertReview);

            Timestamp now = new Timestamp(System.currentTimeMillis());

            for (int i = 0; i < items.length; i++) {
                preparedStmt.setLong(1, items[i].getId());
                preparedStmt.setLong(2, items[i].getDocument().longValue());
                preparedStmt.setLong(3, items[i].getQuestion());
                preparedStmt.setLong(4, 1);
                preparedStmt.setString(5, (String) items[i].getAnswer());
                preparedStmt.setTimestamp(6, now);
                preparedStmt.setTimestamp(7, now);
                preparedStmt.executeUpdate();
            }

            closeStatement(preparedStmt);
            preparedStmt = null;
        } finally {
            closeStatement(preparedStmt);
        }
    }

    /**
     * This will insert the review item comments into the database.
     *
     * @param conn connection to use.
     * @param itemComments item comments to insert.
     * @param reviewItemIds corresponding review item ids.
     *
     * @throws Exception not under test.
     */
    protected void insertReviewItemComments(Connection conn,
        Comment[] itemComments, long[] reviewItemIds) throws Exception {
        PreparedStatement preparedStmt = null;

        try {
            String insertReview = "INSERT INTO review_item_comment"
                + "(review_item_comment_id, resource_id, review_item_id, comment_type_id, content, extra_info, sort,"
                + "create_user, create_date, modify_user, modify_date) "
                + "VALUES (?, ?, ?, ?, ?, ?, 1, 'user', ?, 'user', ?)";
            preparedStmt = conn.prepareStatement(insertReview);

            Timestamp now = new Timestamp(System.currentTimeMillis());

            for (int i = 0; i < itemComments.length; i++) {
                preparedStmt.setLong(1, itemComments[i].getId());
                preparedStmt.setLong(2, itemComments[i].getAuthor());
                preparedStmt.setLong(3, reviewItemIds[i]);
                preparedStmt.setLong(4, itemComments[i].getCommentType().getId());
                preparedStmt.setString(5, itemComments[i].getComment());
                preparedStmt.setString(6,
                    (String) itemComments[i].getExtraInfo());
                preparedStmt.setTimestamp(7, now);
                preparedStmt.setTimestamp(8, now);
                preparedStmt.executeUpdate();
            }

            closeStatement(preparedStmt);
            preparedStmt = null;
        } finally {
            closeStatement(preparedStmt);
        }
    }

    /**
     * A helper method to insert a winning submitter for the given
     * project id with given resource id.
     *
     * @param conn connection to use.
     * @param resourceId resource id.
     * @param projectId project id.
     *
     * @throws Exception not under test.
     */
    protected void insertWinningSubmitter(Connection conn, long resourceId,
        long projectId) throws Exception {
        Resource winner = createResource(resourceId, 101, projectId, 1);
        insertResources(conn, new Resource[] {winner});

        // insert placement : value = 1
        insertResourceInfo(conn, resourceId, 12, "1");

        // insert external id
        insertResourceInfo(conn, resourceId, 1, "1");

        // insert project winner information
        insertProjectInfo(conn, projectId, new long[] {23}, new String[] {"1"});
    }

    /**
     * A helper method to insert a winning submitter for the given
     * project id with given resource id.
     *
     * @param conn connection to use.
     * @param resourceId resource id.
     * @param resourceInfoTypeId resource info type id.
     * @param resourceInfo resource info value.
     *
     * @throws Exception not under test.
     */
    protected void insertResourceInfo(Connection conn, long resourceId,
        long resourceInfoTypeId, String resourceInfo) throws Exception {
        PreparedStatement preparedStmt = null;

        try {
            String insertInfo = "insert into resource_info"
                + "(resource_id, resource_info_type_id, value,"
                + "create_user, create_date, modify_user, modify_date) "
                + "VALUES (?, ?, ?, 'user', ?, 'user', ?)";
            preparedStmt = conn.prepareStatement(insertInfo);

            Timestamp now = new Timestamp(System.currentTimeMillis());
            preparedStmt.setLong(1, resourceId);
            preparedStmt.setLong(2, resourceInfoTypeId);
            preparedStmt.setString(3, resourceInfo);
            preparedStmt.setTimestamp(4, now);
            preparedStmt.setTimestamp(5, now);
            preparedStmt.executeUpdate();

            closeStatement(preparedStmt);
            preparedStmt = null;
        } finally {
            closeStatement(preparedStmt);
        }
    }

    /**
     * Inserts resource-submission mapping into the resource_submission
     * table.
     *
     * @param conn connection to use.
     * @param resourceId resource id.
     * @param submissionId submission id.
     *
     * @throws Exception not under test.
     */
    protected void insertResourceSubmission(Connection conn, long resourceId,
        long submissionId) throws Exception {
        PreparedStatement preparedStmt = null;

        try {
            String insertInfo = "insert into resource_submission"
                + "(resource_id, submission_id,"
                + "create_user, create_date, modify_user, modify_date) "
                + "VALUES (?, ?, 'user', ?, 'user', ?)";
            preparedStmt = conn.prepareStatement(insertInfo);

            Timestamp now = new Timestamp(System.currentTimeMillis());
            preparedStmt.setLong(1, resourceId);
            preparedStmt.setLong(2, submissionId);
            preparedStmt.setTimestamp(3, now);
            preparedStmt.setTimestamp(4, now);
            preparedStmt.executeUpdate();

            closeStatement(preparedStmt);
            preparedStmt = null;
        } finally {
            closeStatement(preparedStmt);
        }
    }

    /**
     * Checks whether a post-mortem phase is inserted into database.
     *
     * @param con the database connection.
     *
     * @return true if there is Post-Mortem phase inserted, false otherwise.
     *
     * @throws Exception if any error occurs.
     */
    protected boolean havePostMortemPhase(Connection con)
        throws Exception {
        PreparedStatement preparedStmt = null;

        try {
            String selectPostPhase = "select * from project_phase Where phase_type_id = ?";

            preparedStmt = con.prepareStatement(selectPostPhase);

            preparedStmt.setLong(1, 12);

            ResultSet result = preparedStmt.executeQuery();

            int count = 0;

            while (result.next()) {
                count++;
            }

            return count > 0;
        } finally {
            closeStatement(preparedStmt);
        }
    }

    /**
     * Checks whether an approval phase is inserted into database.
     *
     * @param con the database connection.
     *
     * @return true if there is approval phase inserted, false otherwise.
     *
     * @throws Exception if any error occurs.
     */
    protected boolean haveApprovalPhase(Connection con)
        throws Exception {
        PreparedStatement preparedStmt = null;

        try {
            String selectPostPhase = "select * from project_phase Where phase_type_id = ?";

            preparedStmt = con.prepareStatement(selectPostPhase);

            preparedStmt.setLong(1, 11);

            ResultSet result = preparedStmt.executeQuery();

            int count = 0;

            while (result.next()) {
                count++;
            }

            return count > 0;
        } finally {
            closeStatement(preparedStmt);
        }
    }

    /**
     * Checks whether a new final review phase is inserted into
     * database.
     *
     * @param con the database connection.
     *
     * @return true if there is approval phase inserted, false otherwise.
     *
     * @throws Exception if any error occurs.
     */
    protected boolean haveNewFinalReviewPhase(Connection con)
        throws Exception {
        PreparedStatement preparedStmt = null;

        try {
            String selectPostPhase = "select * from project_phase Where phase_type_id = ?";

            preparedStmt = con.prepareStatement(selectPostPhase);

            preparedStmt.setLong(1, 10);

            ResultSet result = preparedStmt.executeQuery();

            int count = 0;

            while (result.next()) {
                count++;
            }

            // we should have more than 1
            return count > 1;
        } finally {
            closeStatement(preparedStmt);
        }
    }

    /**
     * Checks whether a new final fix phase is inserted into database.
     *
     * @param con the database connection.
     *
     * @return true if there is approval phase inserted, false otherwise.
     *
     * @throws Exception if any error occurs.
     */
    protected boolean haveNewFinalFixPhase(Connection con)
        throws Exception {
        PreparedStatement preparedStmt = null;

        try {
            String selectPostPhase = "select * from project_phase Where phase_type_id = ?";

            preparedStmt = con.prepareStatement(selectPostPhase);

            preparedStmt.setLong(1, 9);

            ResultSet result = preparedStmt.executeQuery();

            int count = 0;

            while (result.next()) {
                count++;
            }

            return count > 1;
        } finally {
            closeStatement(preparedStmt);
        }
    }

    /**
     * Cleans up records in the given table names.
     *
     * @throws Exception not under test.
     */
    protected void cleanTables() throws Exception {
        Connection conn = null;
        Statement stmt = null;

        try {
            conn = getConnection();
            stmt = conn.createStatement();

            for (int i = 0; i < ALL_TABLE_NAMES.length; i++) {
                stmt.addBatch("delete from " + ALL_TABLE_NAMES[i]);
            }

            stmt.executeBatch();
        } finally {
            closeStatement(stmt);
            closeConnection();
        }
    }

    /**
     * Set up phases include a new post-mortem phase.
     *
     * @return the created project
     *
     * @throws Exception to JUnit.
     */
    protected Project setupPhasesWithPostMortem() throws Exception {
        Connection conn = getConnection();
        PreparedStatement preparedStmt = null;
        Project project = null;

        try {
            project = new Project(new Date(), new DefaultWorkdays());
            project.setId(1);

            // insert project first
            insertProject(conn);

            String insertPhase = "insert into project_phase(project_phase_id, project_id, phase_type_id, phase_status_id,"
                + "scheduled_start_time, scheduled_end_time, duration,"
                + " create_user, create_date, modify_user, modify_date)"
                + "values (?, 1, ?, 1, ?, ?, ?, 'user', ?, 'user', ?)";
            preparedStmt = conn.prepareStatement(insertPhase);

            // insert all standard phases
            long[] phaseIds = new long[] {
                    101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112
                };
            long[] phaseTypeIds = new long[] {
                    1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12
                };
            String[] phaseTypeNames = new String[] {
                    "Registration", "Submission", "Screening", "Review",
                    "Appeals", "Appeals Response", "Aggregation",
                    "Aggregation Review", "Final Fix", "Final Review",
                    "Approval", "Post-Mortem"
                };
            long now = System.currentTimeMillis();
            Timestamp scheduledStart = new Timestamp(now);
            long duration = 24 * 60 * 60 * 1000; // one day
            Timestamp scheduledEnd = new Timestamp(now + duration);

            for (int i = 0; i < phaseIds.length; i++) {
                // insert into db
                preparedStmt.setLong(1, phaseIds[i]);
                preparedStmt.setLong(2, phaseTypeIds[i]);
                preparedStmt.setTimestamp(3, scheduledStart);
                preparedStmt.setTimestamp(4, scheduledEnd);
                preparedStmt.setLong(5, duration);
                preparedStmt.setTimestamp(6, new Timestamp(now));
                preparedStmt.setTimestamp(7, new Timestamp(now));
                preparedStmt.executeUpdate();

                // create phase intance
                Phase phase = new Phase(project, duration);
                phase.setId(phaseIds[i]);
                phase.setPhaseType(new PhaseType(phaseTypeIds[i],
                        phaseTypeNames[i]));
                phase.setPhaseStatus(PhaseStatus.SCHEDULED);
                phase.setActualStartDate(scheduledStart);
                phase.setActualEndDate(scheduledEnd);
                phase.setScheduledStartDate(scheduledStart);
                phase.setScheduledEndDate(scheduledEnd);

                project.addPhase(phase);

                // re-calculate scheduled start and end.
                scheduledStart = new Timestamp(scheduledEnd.getTime());
                scheduledEnd = new Timestamp(scheduledStart.getTime()
                        + duration);
            }

            closeStatement(preparedStmt);
            preparedStmt = null;

            // insert dependencies
            String insertDependency = "INSERT INTO phase_dependency "
                + "(dependency_phase_id, dependent_phase_id, dependency_start, dependent_start, lag_time,"
                + " create_user, create_date, modify_user, modify_date)"
                + "VALUES (?, ?, ?, ?, ?, 'user', ?, 'user', ?)";
            preparedStmt = conn.prepareStatement(insertDependency);

            long[] dependencyPhaseIds = new long[] {
                    101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 102
                };
            long[] dependentPhaseIds = new long[] {
                    102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112
                };
            Phase[] phases = project.getAllPhases();

            for (int i = 0; i < dependencyPhaseIds.length; i++) {
                preparedStmt.setLong(1, dependencyPhaseIds[i]);
                preparedStmt.setLong(2, dependentPhaseIds[i]);
                preparedStmt.setBoolean(3, false);
                preparedStmt.setBoolean(4, true);
                preparedStmt.setLong(5, 0);
                preparedStmt.setTimestamp(6, new Timestamp(now));
                preparedStmt.setTimestamp(7, new Timestamp(now));
                preparedStmt.executeUpdate();

                Dependency dependency = new Dependency(phases[i],
                        phases[i + 1], false, true, 0);
                phases[i + 1].addDependency(dependency);
            }

            closeStatement(preparedStmt);
            preparedStmt = null;
        } finally {
            closeStatement(preparedStmt);
            closeConnection();
        }

        return project;
    }
}
