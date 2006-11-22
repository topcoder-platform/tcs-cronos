/**
 *
 * Copyright (c) 2006, TopCoder, Inc. All rights reserved
 */
package com.orpheus.game;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import com.orpheus.game.accuracytests.AccuracyTests;
import com.orpheus.game.failuretests.FailureTests;
import com.orpheus.game.stresstests.StressTests;

/**
 * <p>This test case aggregates all test cases.</p>
 *
 * @author TopCoder
 * @version 1.0
 */
public class AllTests extends TestCase {

    public static Test suite() {
        final TestSuite suite = new TestSuite();
        //failure tests
        suite.addTest(FailureTests.suite());
        
        //stress tests
        suite.addTest(StressTests.suite());
        
        //unit tests
        suite.addTest(UnitTests.suite());
        
        //accuracy tests
        suite.addTest(AccuracyTests.suite());

        return suite;
    }

}
