/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.orpheus.administration.entities;

import junit.framework.TestCase;

/**
 * <p>
 * Test the <code>PuzzleConfig</code> class.
 * </p>
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
public class PuzzleConfigUnitTests extends TestCase {
    /**
     * name of the puzzle type as configured in Puzzle framework component.
     *
     */
    private final String puzzleTypeName = "puzzleTypeName";

    /**
     * The width of the desired puzzle, in pieces.
     *
     */
    private final Integer width = new Integer(1);

    /**
     * The height of the desired puzzle, in pieces.
     *
     */
    private final Integer height = new Integer(2);

    /**
     * The size of the puzzle series to be generated.
     *
     */
    private final int puzzleSeriesSize = 3;

    /**
     * PuzzleConfig instance to test.
     */
    private PuzzleConfig target = null;

    /**
     * <p>
     * setUp() routine. Creates test PuzzleConfig instance.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    protected void setUp() throws Exception {
        target = new PuzzleConfig(puzzleTypeName, width, height,
                puzzleSeriesSize);
    }

    /**
     * <p>
     * Accuracy test. Tests the <code>PuzzleConfig()</code> for
     * proper behavior. Verify that all fields are set correctly.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void testConstructor_1_accuracy() throws Exception {
        assertNotNull("Failed to get PuzzleConfig instance.", target);
        assertEquals("height set incorrectly.", height, target.getHeight());
        assertEquals("puzzleTypeName set incorrectly.", puzzleTypeName, target
                .getPuzzleTypeName());
        assertEquals("width set incorrectly.", width, target.getWidth());
        assertEquals("puzzleSeriesSize set incorrectly.", puzzleSeriesSize,
                target.getPuzzleSeriesSize());
    }

    /**
     * <p>
     * Failure test. Tests the <code>PuzzleConfig()</code> for
     * proper behavior. Verify that IllegalArgumentException should be thrown if puzzleTypeName is null.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void testConstructor_1_failure() throws Exception {
        try {
            new PuzzleConfig(null, width, height, puzzleSeriesSize);
            fail("IllegalArgumentException should be thrown if puzzleTypeName is null.");
        } catch (IllegalArgumentException e) {
            // ok
        }
    }

    /**
     * <p>
     * Failure test. Tests the <code>PuzzleConfig()</code> for
     * proper behavior. Verify that IllegalArgumentException should be thrown if puzzleTypeName is empty.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void testConstructor_2_failure() throws Exception {
        try {
            new PuzzleConfig("  ", width, height, puzzleSeriesSize);
            fail("IllegalArgumentException should be thrown if puzzleTypeName is empty.");
        } catch (IllegalArgumentException e) {
            // ok
        }
    }

    /**
     * <p>
     * Accuracy test. Tests the <code>getPuzzleTypeName()</code> for
     * proper behavior. Verify that PuzzleTypeName got correctly.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void testGetPuzzleTypeName_1_accuracy() throws Exception {
        assertEquals("PuzzleTypeName got incorrectly.", puzzleTypeName, target
                .getPuzzleTypeName());
    }

    /**
     * <p>
     * Accuracy test. Tests the <code>getWidth()</code> for
     * proper behavior. Verify that Width got correctly.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void testGetWidth_1_accuracy() throws Exception {
        assertEquals("Width got incorrectly.", width, target.getWidth());
    }

    /**
     * <p>
     * Accuracy test. Tests the <code>getHeight()</code> for
     * proper behavior. Verify that Height got correctly.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void testGetHeight_1_accuracy() throws Exception {
        assertEquals("Height got incorrectly.", height, target.getHeight());
    }

    /**
     * <p>
     * Accuracy test. Tests the <code>getPuzzleSeriesSize()</code> for
     * proper behavior. Verify that PuzzleSeriesSize got correctly.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void testGetPuzzleSeriesSize_1_accuracy() throws Exception {
        assertEquals("PuzzleSeriesSize got incorrectly.", puzzleSeriesSize,
                target.getPuzzleSeriesSize());
    }
}
