/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.timetracker.report.failuretests.impl;

import com.topcoder.timetracker.report.Report;
import com.topcoder.timetracker.report.ReportCategory;
import com.topcoder.timetracker.report.ReportException;
import com.topcoder.timetracker.report.ReportType;

import java.util.List;

/**
 * <p>An implementation of {@link com.topcoder.timetracker.report.Report} interface to be utilized
 * for testing purposes.</p>
 *
 * @author  isv
 * @version 2.0
 */
public class ReportDuplicateFormatCategory2 implements Report {

    public ReportDuplicateFormatCategory2() {
    }

    public String execute(String namespace, ReportType type, List filters, String order) throws ReportException {
        return null;
    }

    public ReportCategory getCategory() {
        return ReportCategory.EXPENSE;
    }

    public String getFormat() {
        return "HTML";
    }
}
