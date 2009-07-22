package com.cronos.onlinereview.phases.accuracytests;

import com.cronos.onlinereview.phases.AggregationPhaseHandler;
import com.topcoder.project.phases.Phase;
import com.topcoder.project.phases.PhaseStatus;
import com.topcoder.project.phases.Project;
import com.topcoder.util.idgenerator.IDGeneratorFactory;

import java.sql.Timestamp;

/**
 * Accuracy test for AggregationPhaseHandler class.
 *
 * @author tuenm
 * @version 1.0
 */
public class AggregationPhaseHandlerAccTest extends BaseAccuracyTest {
    /**
     * <p/>
     * Sets up the test environment.
     * </p>
     *
     * @throws Exception to JUnit.
     */
    protected void setUp() throws Exception {
        super.setUp();

        AccuracyTestHelper.releaseSingletonInstance(IDGeneratorFactory.class, "generators");

        AccuracyTestHelper.loadTestConfiguration();
    }

    /**
     * <p/>
     * Cleans up the test environment.
     * </p>
     *
     * @throws Exception pass any unexpected exception to JUnit.
     */
    protected void tearDown() throws Exception {
        super.tearDown();
        AccuracyTestHelper.clearAllConfigNS();
    }

    /**
     * Tests the constructor.
     *
     * @throws Exception pass any unexpected exception to JUnit.
     */
    public void testAggregationPhaseHandler() throws Exception {
        new AggregationPhaseHandler(PHASE_HANDLER_NS);

        // create success
    }

    /**
     * Tests the canPerform() method that check if the phase can start. The input phase has one dependency and
     * the dependency phase status is closed. The result is expected to be false since there is no winner.
     *
     * @throws Exception pass any unexpected exception to JUnit.
     */
    public void testCanPerformStartDepClosed() throws Exception {
        AggregationPhaseHandler handler = new AggregationPhaseHandler(PHASE_HANDLER_NS);

        try {
            AccuracyTestHelper.cleanTables();
            Project project = AccuracyTestHelper.setupPhases();

            Phase[] phases = project.getAllPhases();

            AccuracyTestHelper.addDependency("Aggregation", "Appeals Response", project, true);

            Phase phase = AccuracyTestHelper.getPhase("Aggregation", phases);

            boolean result = handler.canPerform(phase);

            assertFalse("Not the expected checking result", result);
        } finally {
            AccuracyTestHelper.closeConnection();
            AccuracyTestHelper.cleanTables();
        }
    }

    /**
     * Tests the canPerform() method that check if the phase can start. The input phase has one dependency and
     * the dependency phase status is open. The result is expected to be false.
     *
     * @throws Exception pass any unexpected exception to JUnit.
     */
    public void testCanPerformStartDepOpen() throws Exception {
        AggregationPhaseHandler handler = new AggregationPhaseHandler(PHASE_HANDLER_NS);

        try {
            AccuracyTestHelper.cleanTables();
            Project project = AccuracyTestHelper.setupPhases();

            Phase[] phases = project.getAllPhases();

            AccuracyTestHelper.addDependency("Aggregation", "Appeals Response", project, false);

            Phase phase = AccuracyTestHelper.getPhase("Aggregation", phases);

            boolean result = handler.canPerform(phase);

            assertFalse("Not the expected checking result", result);
        } finally {
            AccuracyTestHelper.closeConnection();
            AccuracyTestHelper.cleanTables();
        }
    }

    /**
     * Tests the canPerform() method that check if the phase can start. The input phase has no dependency.
     * The result is expected to be false since there is no winner.
     *
     * @throws Exception pass any unexpected exception to JUnit.
     */
    public void testCanPerformStartNoDep() throws Exception {
        AggregationPhaseHandler handler = new AggregationPhaseHandler(PHASE_HANDLER_NS);

        try {
            AccuracyTestHelper.cleanTables();
            Project project = AccuracyTestHelper.setupPhases();

            Phase[] phases = project.getAllPhases();

            Phase phase = AccuracyTestHelper.getPhase("Aggregation", phases);

            boolean result = handler.canPerform(phase);

            assertFalse("Not the expected checking result", result);
        } finally {
            AccuracyTestHelper.closeConnection();
            AccuracyTestHelper.cleanTables();
        }
    }

    /**
     * Tests the canPerform() method that check if the phase can start.
     * The input phase has no dependency, but the start date is not reached.
     * The result is expected to be false.
     *
     * @throws Exception pass any unexpected exception to JUnit.
     */
    public void testCanPerformStartDateNotReached() throws Exception {
        AggregationPhaseHandler handler = new AggregationPhaseHandler(PHASE_HANDLER_NS);

        try {
            AccuracyTestHelper.cleanTables();
            Project project = AccuracyTestHelper.setupPhases();

            Phase[] phases = project.getAllPhases();

            Phase phase = AccuracyTestHelper.getPhase("Aggregation", phases);

            long now = System.currentTimeMillis();
            long duration = 60 * 60 * 1000; //one hour
            Timestamp start = new Timestamp(now + duration);

            phase.setScheduledStartDate(start);
            phase.setActualStartDate(start);

            boolean result = handler.canPerform(phase);

            assertFalse("Not the expected checking result", result);
        } finally {
            AccuracyTestHelper.closeConnection();
            AccuracyTestHelper.cleanTables();
        }
    }

    /**
     * Tests the canPerform() method that check if the phase can end.
     * The input phase has one dependency and the dependency
     * phase status is open. The result is expected to be false.
     *
     * @throws Exception pass any unexpected exception to JUnit.
     */
    public void testCanPerformEndDepOpenEndDateReached() throws Exception {
        AggregationPhaseHandler handler = new AggregationPhaseHandler(PHASE_HANDLER_NS);

        try {
            AccuracyTestHelper.cleanTables();
            Project project = AccuracyTestHelper.setupPhases();

            Phase[] phases = project.getAllPhases();

            AccuracyTestHelper.addDependency("Aggregation", "Appeals Response", project, false);

            Phase phase = AccuracyTestHelper.getPhase("Aggregation", phases);
	        
            //change dependency type to F2F
	        phase.getAllDependencies()[0].setDependentStart(false);

            phase.setPhaseStatus(new PhaseStatus(2, "Open"));

            long now = System.currentTimeMillis();
            long duration = 60 * 60 * 1000; //one hour
            Timestamp start = new Timestamp(now - duration);
            Timestamp end = new Timestamp(now);

            phase.setScheduledStartDate(start);
            phase.setActualStartDate(start);
            phase.setScheduledEndDate(end);
            phase.setActualEndDate(end);

            boolean result = handler.canPerform(phase);

            assertFalse("Not the expected checking result", result);
        } finally {
            AccuracyTestHelper.closeConnection();
            AccuracyTestHelper.cleanTables();
        }
    }
}
