/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.apps.screening.applications.specification.failuretests;

import junit.framework.TestCase;

import com.topcoder.apps.screening.applications.specification.Submission;
import com.topcoder.apps.screening.applications.specification.ValidationOutput;
import com.topcoder.apps.screening.applications.specification.impl.validators.DefaultUseCaseDiagramValidator;
import com.topcoder.util.xmi.parser.data.UMLActivityDiagram;
import com.topcoder.util.xmi.parser.data.UMLUseCaseDiagram;


/**
 * <p>
 * Failure tests for <code>DefaultUseCaseDiagramValidator</code>.
 * </p>
 *
 * @author GavinWang
 * @version 1.0
 */
public class DefaultUseCaseDiagramValidatorFailureTests extends TestCase {
    /**
     * Failure tests for <code>validateUseCaseDiagram(UMLUseCaseDiagram, Submission)</code>, with null useCaseDiagram,
     * IAE expected.
     */
    public void testValidateUseCaseDiagram_null_useCaseDiagram() {
        SimpleDefaultUseCaseDiagramValidator validator = new SimpleDefaultUseCaseDiagramValidator();

        try {
            validator.validateUseCaseDiagram(null, new Submission(new UMLUseCaseDiagram[0], new UMLActivityDiagram[0]));
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * Failure tests for <code>validateUseCaseDiagram(UMLUseCaseDiagram, Submission)</code>, with null submission, IAE
     * expected.
     */
    public void testValidateUseCaseDiagram_null_submission() {
        SimpleDefaultUseCaseDiagramValidator validator = new SimpleDefaultUseCaseDiagramValidator();

        try {
            validator.validateUseCaseDiagram(new UMLUseCaseDiagram(), null);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }
}


/**
 * Subclass of DefaultUseCaseDiagramValidator used for failure tests.
 */
class SimpleDefaultUseCaseDiagramValidator extends DefaultUseCaseDiagramValidator {
    /**
     * Inheritent super validateUseCaseDiagram.
     *
     * @param useCaseDiagram The diagram to validate
     * @param submission The submission containing this diagram
     *
     * @return the array of validation outputs
     */
    protected ValidationOutput[] validateUseCaseDiagram(UMLUseCaseDiagram useCaseDiagram, Submission submission) {
        return super.validateUseCaseDiagram(useCaseDiagram, submission);
    }
}
