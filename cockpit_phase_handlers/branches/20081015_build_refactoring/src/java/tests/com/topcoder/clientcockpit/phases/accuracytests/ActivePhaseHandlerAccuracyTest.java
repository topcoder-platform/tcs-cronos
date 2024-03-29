/*
 * Copyright (C) 2008 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.clientcockpit.phases.accuracytests;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.topcoder.clientcockpit.phases.ActivePhaseHandler;
import com.topcoder.clientcockpit.phases.accuracytests.TestHelper.CockpitContestStatus;
import com.topcoder.clientcockpit.phases.accuracytests.TestHelper.CockpitPhaseType;
import com.topcoder.project.phases.Phase;
import com.topcoder.project.phases.PhaseStatus;
import com.topcoder.service.studio.contest.Contest;
import com.topcoder.service.studio.submission.Submission;

/**
 * <p>
 * Accuracy tests for the <code>ActivePhaseHandler</code> class.
 * </p>
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
public class ActivePhaseHandlerAccuracyTest extends BaseTestCase {

    /**
     * <p>
     * Accuracy test for the constructor <code>ActivePhaseHandler()</code>, expects the instance is created
     * properly.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testCtor1Accuracy() throws Exception {
        assertNotNull("ActivePhaseHandler should be created.", new ActivePhaseHandler());
    }

    /**
     * <p>
     * Accuracy test for the constructor <code>ActivePhaseHandler(ContestManager)</code>, expects the instance is
     * created properly.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testCtor2Accuracy() throws Exception {
        assertNotNull("ActivePhaseHandler should be created.", new ActivePhaseHandler(TestHelper.CONTEST_MANAGER));
    }

    /**
     * <p>
     * Accuracy test for the constructor <code>ActivePhaseHandler(String)</code>, expects the instance is created
     * properly.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testCtor3Accuracy() throws Exception {
        assertNotNull("ActivePhaseHandler should be created.", new ActivePhaseHandler(TestHelper.ANOTHER_NAMESPACE));
    }

    /**
     * <p>
     * Accuracy test for the constructor <code>ActivePhaseHandler(String, ContestManager)</code>, expects the
     * instance is created properly.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testCtor4Accuracy() throws Exception {
        assertNotNull("ActivePhaseHandler should be created.", new ActivePhaseHandler(TestHelper.ANOTHER_NAMESPACE,
                TestHelper.CONTEST_MANAGER));
    }

    /**
     * <p>
     * Accuracy test for the <code>canPerform(Phase)</code> method with the phase type does not match, false should
     * be returned.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testCanPerformAccuracy1() throws Exception {
        // PhaseStatus.SCHEDULED means the phase is about to start
        Phase phase = this.createPhase(PhaseStatus.SCHEDULED, CockpitPhaseType.NO_SUCH_PHASE);

        // Set contest attribute
        phase.getProject().setAttribute(TestHelper.PROJECT_ATTR_CONTEST, this.createContest(null, null, null, null));

        assertFalse("The ActionRequired phase can not be performed.", this.getActivePhaseHandler().canPerform(phase));
    }

    /**
     * <p>
     * Accuracy test for the <code>canPerform(Phase)</code> method with the phase has already been closed, false
     * should be returned.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testCanPerformAccuracy2() throws Exception {
        Phase phase = this.createPhase(PhaseStatus.CLOSED, CockpitPhaseType.ACTIVE);

        // Set contest attribute
        phase.getProject().setAttribute(TestHelper.PROJECT_ATTR_CONTEST, this.createContest(null, null, null, null));

        assertFalse("The ActionRequired phase can not be performed.", this.getActivePhaseHandler().canPerform(phase));
    }

    /**
     * <p>
     * Accuracy test for the <code>canPerform(Phase)</code> method.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testCanPerformAccuracy3() throws Exception {
        // PhaseStatus.SCHEDULED means the phase is about to start
        Phase phase = this.createPhase(PhaseStatus.SCHEDULED, CockpitPhaseType.ACTIVE);

        // Set contest attribute
        phase.getProject().setAttribute(
                "contest",
                this.createContest(new Date(System.currentTimeMillis() - ONE_HOUR), new Date(), null,
                        CockpitContestStatus.SCHEDULED));

        assertTrue("The Active phase can start.", this.getActivePhaseHandler().canPerform(phase));
    }

    /**
     * <p>
     * Accuracy test for the <code>canPerform(Phase)</code> method.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testCanPerformAccuracy4() throws Exception {
        // PhaseStatus.SCHEDULED means the phase is about to start
        Phase phase = this.createPhase(PhaseStatus.SCHEDULED, CockpitPhaseType.ACTIVE);

        // Set contest attribute
        phase.getProject().setAttribute(
                "contest",
                this.createContest(new Date(System.currentTimeMillis() - ONE_HOUR), new Date(), null,
                        CockpitContestStatus.CANCELLED));

        assertFalse("The Active phase can not start.", this.getActivePhaseHandler().canPerform(phase));
    }

    /**
     * <p>
     * Accuracy test for the <code>canPerform(Phase)</code> method.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testCanPerformAccuracy5() throws Exception {
        // PhaseStatus.SCHEDULED means the phase is about to start
        Phase phase = this.createPhase(PhaseStatus.SCHEDULED, CockpitPhaseType.ACTIVE);

        Contest contest = this.createContest(new Date(System.currentTimeMillis() + ONE_HOUR), new Date(System
                .currentTimeMillis()
                + 2 * ONE_HOUR), null, null);
        // Set contest attribute
        phase.getProject().setAttribute(TestHelper.PROJECT_ATTR_CONTEST, contest);

        // Set minimum number of submissions to 1
        phase.getProject().setAttribute(TestHelper.PROJECT_ATTR_MINIMUM_SUBMISSIONS, 1);

        Set<Submission> submissions = new HashSet<Submission>();
        submissions.add(new Submission());
        contest.setSubmissions(submissions);

        assertFalse("The ActionRequired phase can not start.", this.getActivePhaseHandler().canPerform(phase));
    }

    /**
     * <p>
     * Accuracy test for the <code>canPerform(Phase)</code> method.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testCanPerformAccuracy6() throws Exception {
        // PhaseStatus.OPEN means the phase is about to end
        Phase phase = this.createPhase(PhaseStatus.OPEN, CockpitPhaseType.ACTIVE);

        Contest contest = this.createContest(new Date(System.currentTimeMillis() - 2 * ONE_HOUR), new Date(System
                .currentTimeMillis()
                - ONE_HOUR), new Date(System.currentTimeMillis() + ONE_HOUR), null);
        // Set contest attribute
        phase.getProject().setAttribute(TestHelper.PROJECT_ATTR_CONTEST, contest);

        assertTrue("The ActionRequired phase can end.", this.getActivePhaseHandler().canPerform(phase));
    }

    /**
     * <p>
     * Accuracy test for the <code>canPerform(Phase)</code> method.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testCanPerformAccuracy7() throws Exception {
        // PhaseStatus.OPEN means the phase is about to end
        Phase phase = this.createPhase(PhaseStatus.OPEN, CockpitPhaseType.ACTIVE);

        // Set contest attribute
        phase.getProject().setAttribute(
                "contest",
                this.createContest(new Date(System.currentTimeMillis() + ONE_HOUR), new Date(System
                        .currentTimeMillis()
                        + 2 * ONE_HOUR), null, CockpitContestStatus.ACTIVE));

        assertFalse("The Active phase can not end.", this.getActivePhaseHandler().canPerform(phase));
    }

    /**
     * <p>
     * Accuracy test for the <code>canPerform(Phase)</code> method with the Abandoned phase starts, start email
     * should be sent, contest status should be updated to "Abandoned".
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testPerformAccuracy1() throws Exception {
        Phase phase = this.createPhase(PhaseStatus.SCHEDULED, CockpitPhaseType.ACTIVE);
        Contest contest = this.createContest(null, null, null, null);
        this.populateAttributes(phase, contest);
        this.mockContestManager(false);

        this.startSMTPServer();
        try {
            this.getActivePhaseHandler().perform(phase, "operator");
            this.assertStartEmailSent();
            assertEquals("Contest status should be updated", contest.getStatus(), CockpitContestStatus.ACTIVE);
        } finally {
            this.stopSMTPServer();
        }
    }

    /**
     * <p>
     * Accuracy test for the <code>canPerform(Phase)</code> method with the Abandoned phase ends, end email should
     * be sent, contest status should NOT be updated.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testPerformAccuracy2() throws Exception {
        Phase phase = this.createPhase(PhaseStatus.OPEN, CockpitPhaseType.ACTIVE);
        Contest contest = this.createContest(null, null, null, CockpitContestStatus.ACTIVE);
        this.populateAttributes(phase, contest);
        this.mockContestManager(false);

        this.startSMTPServer();
        try {
            this.getActivePhaseHandler().perform(phase, "operator");
            this.assertEndEmailSent();
            assertEquals("Contest status should not be changed", contest.getStatus(), CockpitContestStatus.ACTIVE);
        } finally {
            this.stopSMTPServer();
        }
    }
}
