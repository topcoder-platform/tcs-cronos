/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.phases;

import com.topcoder.util.config.ConfigManager;


/**
 * Test cases for ManagerHelper class.
 *
 * @author bose_java
 * @version 1.0
 */
public class ManagerHelperTest extends BaseTest {

    /** instance used for testing purpose. */
    private ManagerHelper managerHelper;

    /**
     * sets up the environment required for test cases for this class.
     *
     * @throws Exception not under test.
     */
    protected void setUp() throws Exception {
        super.setUp();

        ConfigManager configManager = ConfigManager.getInstance();
        configManager.add(MANAGER_HELPER_CONFIG_FILE);

        //add the component configurations as well
        for (int i = 0; i < COMPONENT_FILE_NAMES.length; i++) {
            configManager.add(COMPONENT_FILE_NAMES[i]);
        }

        managerHelper = new ManagerHelper();
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
     * Tests the constructor ManagerHelper(String) with null namespace.
     *
     * @throws ConfigurationException not under test.
     */
    public void testManagerHelperWithNullNS() throws ConfigurationException {
        try {
            new ManagerHelper(null);
            fail("Should have thrown IllegalArgumentException for null namespace.");
        } catch (IllegalArgumentException ex) {
            //expected.
        }
    }

    /**
     * Tests the constructor ManagerHelper(String) with empty namespace.
     *
     * @throws ConfigurationException not under test.
     */
    public void testManagerHelperWithEmptyNS() throws ConfigurationException {
        try {
            new ManagerHelper("    ");
            fail("Should have thrown IllegalArgumentException for empty namespace.");
        } catch (IllegalArgumentException ex) {
            //expected.
        }
    }

    /**
     * Tests that ManagerHelper.getProjectManager() returns non-null value.
     */
    public void testGetProjectManager() {
        assertNotNull("getProjectManager() returned null.", managerHelper.getProjectManager());
    }

    /**
     * Tests that ManagerHelper.getPhaseManager() returns non-null value.
     */
    public void testGetPhaseManager() {
        assertNotNull("getPhaseManager() returned null.", managerHelper.getPhaseManager());
    }

    /**
     * Tests that ManagerHelper.getScorecardManager() returns non-null value.
     */
    public void testGetScorecardManager() {
        assertNotNull("getScorecardManager() returned null.", managerHelper.getScorecardManager());
    }

    /**
     * Tests that ManagerHelper.getScreeningManager() returns non-null value.
     */
    public void testGetScreeningManager() {
        assertNotNull("getScreeningManager() returned null.", managerHelper.getScreeningManager());
    }

    /**
     * Tests that ManagerHelper.getReviewManager() returns non-null value.
     */
    public void testGetReviewManager() {
        assertNotNull("getReviewManager() returned null.", managerHelper.getReviewManager());
    }

    /**
     * Tests that ManagerHelper.getResourceManager() returns non-null value.
     */
    public void testGetResourceManager() {
        assertNotNull("getResourceManager() returned null.", managerHelper.getResourceManager());
    }

    /**
     * Tests that ManagerHelper.getUploadManager() returns non-null value.
     */
    public void testGetUploadManager() {
        assertNotNull("getUploadManager() returned null.", managerHelper.getUploadManager());
    }

    /**
     * Tests that ManagerHelper.getUserRetrieval() returns non-null value.
     */
    public void testGetUserRetrieval() {
        assertNotNull("getUserRetrieval() returned null.", managerHelper.getUserRetrieval());
    }

    /**
     * Tests that ManagerHelper.getScorecardAggregator() returns non-null value.
     */
    public void testGetScorecardAggregator() {
        assertNotNull("getScorecardAggregator() returned null.", managerHelper.getScorecardAggregator());
    }

}
