/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.project.service;

import junit.framework.TestCase;

/**
 * <p>
 * This is a test case for <code>Util</code> class.
 * </p>
 * @author TCSDEVELOPER
 * @version 1.0
 */
public class UtilTest extends TestCase {

    /**
     * <p>
     * Test for <code>checkObjNotNull</code> method.
     * </p>
     * <p>
     * Tests it against null object. Expects <code>IllegalArgumentException</code>.
     * </p>
     * @throws Exception
     *             to JUnit
     */
    public void testCheckObjNotNullWithNullObj() throws Exception {
        try {
            Util.checkObjNotNull(null, "null", null);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException ex) {
            // success
        }
    }

    /**
     * <p>
     * Test for <code>checkObjNotNull</code> method.
     * </p>
     * <p>
     * Tests it with non-null object, no exception should be thrown.
     * </p>
     * @throws Exception
     *             to JUnit
     */
    public void testCheckObjNotNullWithNonNullObj() throws Exception {
        Util.checkObjNotNull(new Object(), "non_null", null);
    }

    /**
     * <p>
     * Test for <code>checkStrNotNullOrEmpty</code> method.
     * </p>
     * <p>
     * Tests it against null string. Expects <code>IllegalArgumentException</code>.
     * </p>
     * @throws Exception
     *             to JUnit
     */
    public void testCheckStrNotNullOrEmptyWithNullStr() throws Exception {
        try {
            Util.checkStrNotNullOrEmpty(null, "null");
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException ex) {
            // success
        }
    }

    /**
     * <p>
     * Test for <code>checkStrNotNullOrEmpty</code> method.
     * </p>
     * <p>
     * Tests it against empty string. Expects <code>IllegalArgumentException</code>.
     * </p>
     * @throws Exception
     *             to JUnit
     */
    public void testCheckStrNotNullOrEmptyWithEmptyStr() throws Exception {
        try {
            Util.checkStrNotNullOrEmpty("    ", "empty");
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException ex) {
            // success
        }
    }

    /**
     * <p>
     * Test for <code>checkStrNotNullOrEmpty</code> method.
     * </p>
     * <p>
     * Tests it with non-null and non-empty string. no exception should be thrown.
     * </p>
     * @throws Exception
     *             to JUnit
     */
    public void testCheckStrNotNullOrEmpty() throws Exception {
        Util.checkStrNotNullOrEmpty("he", "non");
    }

    /**
     * <p>
     * Test for <code>checkArrayHasNull</code> method.
     * </p>
     * <p>
     * Tests it against array having null elements. Expects <code>IllegalArgumentException</code>.
     * </p>
     * @throws Exception
     *             to JUnit
     */
    public void testCheckArrayHasNullWithArrayHavingNull() throws Exception {
        try {
            Util.checkArrrayHasNull(new Object[] {null}, "hasNull");
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException ex) {
            // success
        }
    }

    /**
     * <p>
     * Test for <code>checkStringArrayHasNullOrEmptyEle</code> method.
     * </p>
     * <p>
     * Tests it with array having null string. Expects <code>IllegalArgumentException</code>.
     * </p>
     * @throws Exception
     *             to JUnit
     */
    public void testCheckStringArrayHasNullOrEmptyWithArrayHavingNull() throws Exception {
        try {
            Util.checkStringArrayHasNullOrEmptyEle(new String[] {null}, "hasNull");
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException ex) {
            // success
        }
    }

    /**
     * <p>
     * Test for <code>checkStringArrayHasNullOrEmptyEle</code> method.
     * </p>
     * <p>
     * Tests it with array having empty string. Expects <code>IllegalArgumentException</code>.
     * </p>
     * @throws Exception
     *             to JUnit
     */
    public void testCheckStringArrayHasNullOrEmptyWithArrayHavingEmpty() throws Exception {
        try {
            Util.checkStringArrayHasNullOrEmptyEle(new String[] {"   "}, "empty");
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException ex) {
            // success
        }
    }

    /**
     * <p>
     * Test for <code>checkIDNotNegative</code> method.
     * </p>
     * <p>
     * Tests it with negative ID. Expects <code>IllegalArgumentException</code>.
     * </p>
     * @throws Exception
     *             to JUnit
     */
    public void testCheckIDNotNegativeWithNegID() throws Exception {
        try {
            Util.checkIDNotNegative(-1, "negID", null);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException ex) {
            // success
        }
    }
}
