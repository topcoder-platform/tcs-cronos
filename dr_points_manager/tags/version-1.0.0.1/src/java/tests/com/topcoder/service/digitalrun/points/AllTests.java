/**
 *
 * Copyright (c) 2008, TopCoder, Inc. All rights reserved
 */
package com.topcoder.service.digitalrun.points;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.topcoder.service.digitalrun.points.accuracytests.AccuracyTests;
import com.topcoder.service.digitalrun.points.failuretests.FailureTests;
import com.topcoder.service.digitalrun.points.stresstests.StressTests;

/**
 * <p>This test case aggregates all test cases.</p>
 *
 * @author TopCoder
 * @version 1.0
 */
public class AllTests extends TestCase {

    public static Test suite() {
        final TestSuite suite = new TestSuite();
        
        //accuracy tests
        suite.addTest(AccuracyTests.suite());

        //stress tests
        suite.addTest(StressTests.suite());

        //failure tests
        suite.addTest(FailureTests.suite());
        
        //unit tests
        suite.addTest(UnitTests.suite());
        
        return suite;
    }

}
