/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.timetracker.entry.expense.search;

/**
 * <p>
 * This class represents a very specific type of expense entry match, matching those expense entries with a given
 * reject reason id. It is a very specific criteria because it doesn't act on the expense entry table but on the
 * exp_reject_reason table. Because of that, the where clause is very specific and it uses the "IN (SELECT ...)" SQL
 * nested queries.
 * </p>
 *
 * <p>
 * Thread safety: Immutable class so there are no thread safety issues.
 * </p>
 *
 * @author adic, TCSDEVELOPER
 * @version 1.1
 */
public class RejectReasonCriteria implements Criteria {

    /**
     * Represents the default where clause string of this criteria.
     */
    private static final String WHERE_CLAUSE = "? IN (SELECT reject_reason_id FROM exp_reject_reason "
        + "WHERE exp_reject_reason.ExpenseEntriesId = ExpenseEntries.ExpenseEntriesId)";

    /**
     * Represents the id of the reject reason. Can have any value (the id is not generated by us so we should not
     * enforce any constraints).
     */
    private final int rejectReasonId;

    /** Represents the where clause of this criteria. */
    private final String whereClause;

    /** Represents the parameters of this criteria. */
    private final Object[] parameters;

    /**
     * <p>
     * Creates a new instance of <code>FieldLikeCriteria</code> class with given reject reason id.
     * </p>
     *
     * @param rejectReasonId the id of the reject reason.
     */
    public RejectReasonCriteria(int rejectReasonId) {
        this.rejectReasonId = rejectReasonId;
        this.whereClause = WHERE_CLAUSE;
        this.parameters = new Integer[] {new Integer(rejectReasonId)};
    }

    /**
     * Gets the reject reason id.
     *
     * @return the reject reason id.
     */
    public int getRejectReasonId() {
        return rejectReasonId;
    }

    /**
     * Gets the where clause expression that can be used in an SQL query to perform the filtering. The expression is
     * expected to contain ? characters where the criteria parameters should be inserted. This is needed because
     * <code>PreparedStatement</code> can be used to execute the query.
     *
     * @return the where clause expression that can be used in an SQL query to perform the filtering.
     */
    public String getWhereClause() {
        return whereClause;
    }

    /**
     * Gets the parameter values that should be used to fill the where clause expression returned by the
     * <code>getWhereClause</code> method. The parameter values should be inserted in place of the ? characters in the
     * where clause expression. The number of parameters should be equal to the number of ? characters in the where
     * clause expression.
     *
     * @return the parameters used in the expression.
     */
    public Object[] getParameters() {
        return parameters;
    }
}
