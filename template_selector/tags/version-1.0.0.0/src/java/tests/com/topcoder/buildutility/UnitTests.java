/**
 *
 * Copyright (c) 2005, TopCoder, Inc. All rights reserved
 */
package com.topcoder.buildutility;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * <p>This test case aggregates all Unit test cases.</p>
 *
 * @author TopCoder
 * @version 1.0
 */
public class UnitTests extends TestCase {

    public static Test suite() {
        final TestSuite suite = new TestSuite();

        suite.addTestSuite(TemplateSelectorTests.class);
        suite.addTestSuite(Demo.class);

        return suite;
    }

}
