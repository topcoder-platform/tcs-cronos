/*
 * Copyright (C) 2008 TopCoder Inc., All Rights Reserved.
 */

package com.topcoder.jira.accuracytests;

import junit.framework.TestCase;

import com.topcoder.jira.JiraProjectRetrievalResult;
import com.topcoder.jira.JiraProjectRetrievalResultAction;
import com.topcoder.jira.managers.entities.JiraProject;
import com.topcoder.jira.managers.entities.JiraVersion;

/**
 * Accuracy tests for the <code>JiraProjectRetrievalResult</code> class.
 *
 * @author morehappiness
 * @version 1.0
 */
public class JiraProjectRetrievalResultAccuracyTests extends TestCase {

    /**
     * Tests <code>getRetrievalResult()</code>.
     * <p>
     * Checks that getter returns expected value.
     */
    public void testGetRetrievalResult() {
        JiraProjectRetrievalResultAction action = JiraProjectRetrievalResultAction.PROJECT_FOUND_NOT_VERSION;
        assertEquals("returned value is incorrect", action, new JiraProjectRetrievalResult(null,
            null, action).getRetrievalResult());
    }

    /**
     * Tests <code>getProject()</code>.
     * <p>
     * Checks that getter returns expected value.
     */
    public void testGetProject() {
        JiraProject project = new JiraProject();
        assertSame("returned value is incorrect", project, new JiraProjectRetrievalResult(project,
            null, null).getProject());
    }

    /**
     * Tests <code>getVersion()</code>.
     * <p>
     * Checks that getter returns expected value.
     */
    public void testGetVersion() {
        JiraVersion version = new JiraVersion();
        assertSame("returned value is incorrect", version, new JiraProjectRetrievalResult(null,
            version, null).getVersion());
    }

    /**
     * Tests
     * <code>JiraProjectRetrievalResult(JiraProject, JiraVersion, JiraProjectRetrievalResultAction)</code>.
     * <p>
     * Checks that constructor correctly creates new instance and propagates parameters. Passes
     * non-null values.
     */
    public void testJiraProjectRetrievalResult_2() {
        JiraProject project = new JiraProject();
        JiraVersion version = new JiraVersion();
        JiraProjectRetrievalResultAction action = JiraProjectRetrievalResultAction.PROJECT_FOUND_NOT_VERSION;
        JiraProjectRetrievalResult result = new JiraProjectRetrievalResult(project, version, action);
        assertSame("project was propagated incorrectly", project, result.getProject());
        assertSame("version was propagated incorrectly", version, result.getVersion());
        assertEquals("actioni was propagated incorrectly", action, result.getRetrievalResult());
    }
}
