package com.topcoder.web.common.dao;

import com.topcoder.web.common.model.comp.Submission;

/**
 * @author dok
 * @version $Revision: 1.1 $ Date: 2005/01/01 00:00:00
 *          Create Date: Jul 23, 2007
 */
public interface SubmissionDAO {
    public Submission find(Integer id);
}

