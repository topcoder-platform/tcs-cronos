/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.phases;

import java.sql.Connection;
import java.util.Date;

import com.topcoder.management.deliverable.Submission;
import com.topcoder.management.deliverable.Upload;
import com.topcoder.management.phase.PhaseHandlingException;
import com.topcoder.management.resource.Resource;
import com.topcoder.management.review.data.Comment;
import com.topcoder.management.review.data.Item;
import com.topcoder.management.review.data.Review;
import com.topcoder.management.scorecard.data.Scorecard;
import com.topcoder.project.phases.Phase;
import com.topcoder.project.phases.PhaseStatus;
import com.topcoder.project.phases.Project;
import com.topcoder.util.config.ConfigManager;

/**
 * All tests for AggregationPhaseHandler class.
 *
 * @author bose_java
 * @version 1.0
 */
public class AggregationPhaseHandlerTest extends BaseTest {

    /**
     * sets up the environment required for test cases for this class.
     *
     * @throws Exception not under test.
     */
    protected void setUp() throws Exception {
        super.setUp();

        ConfigManager configManager = ConfigManager.getInstance();

        configManager.add(PHASE_HANDLER_CONFIG_FILE);

        configManager.add(MANAGER_HELPER_CONFIG_FILE);

        //add the component configurations as well
        for (int i = 0; i < COMPONENT_FILE_NAMES.length; i++) {
            configManager.add(COMPONENT_FILE_NAMES[i]);
        }

    }

    /**
     * cleans up the environment required for test cases for this class.
     *
     * @throws Exception not under test.
     */
    protected void tearDown() throws Exception {
        super.tearDown();
    }


    /**
     * Tests canPerform(Phase) with null phase.
     *
     * @throws Exception not under test.
     */
    public void testCanPerform() throws Exception {
        AggregationPhaseHandler handler = new AggregationPhaseHandler(PHASE_HANDLER_NAMESPACE);
        try {
            handler.canPerform(null);
            fail("canPerform() did not throw IllegalArgumentException for null argument.");
        } catch (IllegalArgumentException e) {
            //expected.
        }
    }

    /**
     * Tests canPerform(Phase) with invalid phase status.
     *
     * @throws Exception not under test.
     */
    public void testCanPerformWithInvalidStatus() throws Exception {
        AggregationPhaseHandler handler = new AggregationPhaseHandler(PHASE_HANDLER_NAMESPACE);
        try {
            Phase phase = createPhase(1, 1, "Invalid", 3, "Aggregation");
            handler.canPerform(phase);
            fail("canPerform() did not throw PhaseHandlingException for invalid phase status.");
        } catch (PhaseHandlingException e) {
            //expected.
        }
    }

    /**
     * Tests canPerform(Phase) with invalid phase type.
     *
     * @throws Exception not under test.
     */
    public void testCanPerformWithInvalidType() throws Exception {
        AggregationPhaseHandler handler = new AggregationPhaseHandler(PHASE_HANDLER_NAMESPACE);
        try {
            Phase phase = createPhase(1, 1, "Scheduled", 1, "INVALID");
            handler.canPerform(phase);
            fail("canPerform() did not throw PhaseHandlingException for invalid phase type.");
        } catch (PhaseHandlingException e) {
            //expected.
        }
    }

    /**
     * Tests perform(Phase) with null phase.
     *
     * @throws Exception not under test.
     */
    public void testPerformWithNullPhase() throws Exception {
        AggregationPhaseHandler handler = new AggregationPhaseHandler(PHASE_HANDLER_NAMESPACE);
        try {
            handler.perform(null, "operator");
            fail("perform() did not throw IllegalArgumentException for null argument.");
        } catch (IllegalArgumentException e) {
            //expected.
        }
    }

    /**
     * Tests perform(Phase) with invalid phase status.
     *
     * @throws Exception not under test.
     */
    public void testPerformWithInvalidStatus() throws Exception {
        AggregationPhaseHandler handler = new AggregationPhaseHandler(PHASE_HANDLER_NAMESPACE);
        try {
            Phase phase = createPhase(1, 1, "Invalid", 1, "Aggregation");
            handler.perform(phase, "operator");
            fail("perform() did not throw PhaseHandlingException for invalid phase status.");
        } catch (PhaseHandlingException e) {
            //expected.
        }
    }

    /**
     * Tests perform(Phase) with invalid phase type.
     *
     * @throws Exception not under test.
     */
    public void testPerformWithInvalidType() throws Exception {
        AggregationPhaseHandler handler = new AggregationPhaseHandler(PHASE_HANDLER_NAMESPACE);
        try {
            Phase phase = createPhase(1, 1, "Scheduled", 1, "INVALID");
            handler.perform(phase, "operator");
            fail("perform() did not throw PhaseHandlingException for invalid phase type.");
        } catch (PhaseHandlingException e) {
            //expected.
        }
    }

    /**
     * Tests perform(Phase) with null operator.
     *
     * @throws Exception not under test.
     */
    public void testPerformWithNullOperator() throws Exception {
        AggregationPhaseHandler handler = new AggregationPhaseHandler(PHASE_HANDLER_NAMESPACE);
        try {
            Phase phase = createPhase(1, 1, "Scheduled", 1, "Aggregation");
            handler.perform(phase, null);
            fail("perform() did not throw IllegalArgumentException for null operator.");
        } catch (IllegalArgumentException e) {
            //expected.
        }
    }

    /**
     * Tests perform(Phase) with empty operator.
     *
     * @throws Exception not under test.
     */
    public void testPerformWithEmptyOperator() throws Exception {
        AggregationPhaseHandler handler = new AggregationPhaseHandler(PHASE_HANDLER_NAMESPACE);
        try {
            Phase phase = createPhase(1, 1, "Scheduled", 1, "Aggregation");
            handler.perform(phase, "   ");
            fail("perform() did not throw IllegalArgumentException for empty operator.");
        } catch (IllegalArgumentException e) {
            //expected.
        }
    }

    /**
     * Tests the AggregationPhaseHandler() constructor and canPerform with Scheduled statuses.
     *
     * @throws Exception not under test.
     */
    public void testCanPerformWithScheduled() throws Exception {
        AggregationPhaseHandler handler = new AggregationPhaseHandler(PHASE_HANDLER_NAMESPACE);
        try {
        	cleanTables();
	
	        Project project = super.setupPhases();
	        Phase[] phases = project.getAllPhases();
	        Phase aggregationPhase = phases[6];
	
	        //test with scheduled status.
	        aggregationPhase.setPhaseStatus(PhaseStatus.SCHEDULED);
	
	        //time has not passed, nor dependencies met
	        assertFalse("canPerform should have returned false", handler.canPerform(aggregationPhase));
	
	        //time has passed, but dependency not met.
	        aggregationPhase.setActualStartDate(new Date());
	        assertFalse("canPerform should have returned false", handler.canPerform(aggregationPhase));
	
	        //time has passed and dependency met, but no winner
	        aggregationPhase.getAllDependencies()[0].getDependency().setPhaseStatus(PhaseStatus.CLOSED);
	        assertFalse("canPerform should have returned false", handler.canPerform(aggregationPhase));
	        
	        //insert winner.
	        Connection conn = getConnection();
	        insertWinningSubmitter(conn, 1, project.getId());
	        assertTrue("canPerform should have returned true", handler.canPerform(aggregationPhase));
        } finally {
        	cleanTables();
        	closeConnection();
        }
    }


    /**
     * Tests the AggregationPhaseHandler() constructor and canPerform with Open statuses.
     *
     * @throws Exception not under test.
     */
    public void testCanPerformHandlerWithOpen() throws Exception {
        AggregationPhaseHandler handler = new AggregationPhaseHandler(PHASE_HANDLER_NAMESPACE);

        try {
        	cleanTables();
	        Project project = super.setupPhases();
	        Phase[] phases = project.getAllPhases();
	        Phase aggregationPhase = phases[6];
	    	
	        //change dependency type to F2F
	        aggregationPhase.getAllDependencies()[0].setDependentStart(false);
	
	        //test with open status.
	        aggregationPhase.setPhaseStatus(PhaseStatus.OPEN);
	
	        //time has not passed, dependencies not met
	        assertFalse("canPerform should have returned false", handler.canPerform(aggregationPhase));
        } finally {
        	cleanTables();
        }
    }

    /**
     * Tests the perform with Open statuses.
     *
     * @throws Exception not under test.
     */
    public void testPerform() throws Exception {
        AggregationPhaseHandler handler = new AggregationPhaseHandler(PHASE_HANDLER_NAMESPACE);

        //test with open status.
        Phase aggregationPhase = createPhase(1, 1, "Open", 2, "Aggregation");
        String operator = "operator";
        handler.perform(aggregationPhase, operator);
    }

    /**
     * Tests the perform with Scheduled status.
     *
     * @throws Exception not under test.
     */
    public void testPerformWithScheduled() throws Exception {
        AggregationPhaseHandler handler = new AggregationPhaseHandler(PHASE_HANDLER_NAMESPACE);
        try {
	        cleanTables();
	        Project project = super.setupPhases();
	        Phase[] phases = project.getAllPhases();
	        Phase aggregationPhase = phases[6];
	        aggregationPhase.setPhaseStatus(PhaseStatus.SCHEDULED);
	        Phase reviewPhase = phases[3];
	        Phase submissionPhase = phases[1];
	        
	        //populate db with required data
	        //insert submitter, winner and submissions
	        Resource winner = createResource(11, submissionPhase.getId(), project.getId(), 1);
	        Upload winnerUpload = createUpload(1, project.getId(), winner.getId(), 1, 1, "parameter");
	        Submission winSubmission = createSubmission(1, winnerUpload.getId(), 1);
	        
	        Resource submitter = createResource(12, submissionPhase.getId(), project.getId(), 1);
	        Upload noWinUpload = createUpload(2, project.getId(), submitter.getId(), 1, 1, "parameter");
	        Submission noWinSubmission = createSubmission(2, noWinUpload.getId(), 1);
	        
	        //aggregator resource
	        Resource aggregator = createResource(1, aggregationPhase.getId(), project.getId(), 8);
	        
	        //reviewer resource and related review
	        Resource reviewer1 = createResource(2, reviewPhase.getId(), project.getId(), 5);
	        Scorecard scorecard1 = createScorecard(1, 1, 2, 1, "name", "1.0", 75.0f, 100.0f);
	        Review review1 = createReview(11, reviewer1.getId(), winSubmission.getId(), scorecard1.getId(), true, 90.0f);
	        review1.addComment(createComment(1, reviewer1.getId(), "Good Design", 1, "Comment"));
	        
	        Resource reviewer2 = createResource(3, reviewPhase.getId(), project.getId(), 5);
	        Scorecard scorecard2 = createScorecard(2, 1, 2, 1, "name", "1.0", 75.0f, 100.0f);
	        Review review2 = createReview(12, reviewer2.getId(), noWinSubmission.getId(), scorecard2.getId(), true, 80.0f);
	        
	        Item[] reviewItems = new Item[4];
	        reviewItems[0] = createReviewItem(11, "Answer 1", review1.getId(), 1);
	        reviewItems[1] = createReviewItem(12, "Answer 2", review1.getId(), 1);
	        reviewItems[2] = createReviewItem(13, "Answer 3", review2.getId(), 1);
	        reviewItems[3] = createReviewItem(14, "Answer 4", review2.getId(), 1);
	        
	        Comment[] reviewItemComments = new Comment[4];
	        reviewItemComments[0] = createComment(11, reviewer1.getId(), "Item 1", 1, "Comment");
	        reviewItemComments[1] = createComment(12, reviewer1.getId(), "Item 2", 1, "Comment");
	        reviewItemComments[2] = createComment(13, reviewer2.getId(), "Item 3", 1, "Comment");
	        reviewItemComments[3] = createComment(14, reviewer2.getId(), "Item 4", 1, "Comment");
        
        	Connection conn = getConnection();
            
        	//insert records
        	insertResources(conn, new Resource[] {winner, submitter, aggregator, reviewer1, reviewer2});
        	insertResourceInfo(conn, winner.getId(), 12, "1"); //winner info
            insertUploads(conn, new Upload[] {winnerUpload, noWinUpload});
            insertSubmissions(conn, new Submission[] {winSubmission, noWinSubmission});
            insertResourceSubmission(conn, winner.getId(), winSubmission.getId());
            insertScorecards(conn, new Scorecard[] {scorecard1, scorecard2});
            insertReviews(conn, new Review[] {review1, review2});
            insertComments(conn, new long[] {101,102}, new long[] {reviewer1.getId(), reviewer2.getId()}, 
            		new long[] {review1.getId(), review2.getId()}, new String[] {"comment 1", "comment 2"}, 
            		new long[] {1, 3});
            insertScorecardQuestion(conn, 1, 1);
            insertReviewItems(conn, reviewItems);
            insertReviewItemComments(conn, reviewItemComments, new long[] {11, 12, 13, 14});
            
            //check no agg worksheet exists before calling perform method
            Review aggWorksheet = PhasesHelper.getAggregationWorksheet(conn, handler.getManagerHelper(), 
            		aggregationPhase.getId());
            assertNull("No Aggregation worksheet should exist before this test", aggWorksheet);
            
            //call perform method
            String operator = "operator";
            handler.perform(aggregationPhase, operator);
            
            //agg worksheet should be created after the perform call.
            aggWorksheet = PhasesHelper.getAggregationWorksheet(conn, handler.getManagerHelper(), 
            		aggregationPhase.getId());
            assertNotNull("Aggregation worksheet should exist after perform()", aggWorksheet);
            
            //should only have 2 review items cause scorecard for winner has only 2 review items.
            assertEquals("review items not copied", aggWorksheet.getAllItems().length, 2);
        	
        } finally {
            closeConnection();
        	cleanTables();
        }
    }
}