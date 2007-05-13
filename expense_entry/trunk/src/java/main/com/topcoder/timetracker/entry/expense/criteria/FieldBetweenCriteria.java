/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.timetracker.entry.expense.criteria;

import com.topcoder.timetracker.entry.expense.ExpenseEntryHelper;

import java.math.BigDecimal;

import java.util.Date;

/**
 * <p>
 * This class represents a basic type of criteria for selecting records with a field within a given interval. The field
 * name and the value limits are given by the user. The interval can be open ended (one of the limits can be null,
 * interpreted as no limit). When both limits are not null, the criteria matches to an SQL between clause. When one
 * limit is missing, the criteria is a &lt;= or =&gt; comparison. The field must be fully qualified (table.field)
 * because the search query joins more tables. The class defines 7 constants and 7 static methods for the fields the
 * requirements specifically mention. This is done in an effort to provide the simplest possible API for the user.
 * </p>
 *
 * <p>
 * <b>Thread Safety: </b>Immutable class so there are no thread safety issues.
 * </p>
 *
 * @author AleaActaEst, argolite, TCSDEVELOPER
 * @version 3.2
 */
public class FieldBetweenCriteria implements Criteria {

	/**
	 * Automatically generated unique ID for use with serialization.
	 */
	private static final long serialVersionUID = 5344534418718392709L;

	/**
     * Represents the constant for the expense amount field in the expense entry table.
     */
    public static final String AMOUNT_FIELD = "expense_entry.amount";

    /**
     * Represents the constant for the creation date field in the expense entry table.
     */
    public static final String CREATION_DATE_FIELD = "expense_entry.creation_date";

    /**
     * Represents the constant for the modification date field in the expense entry table.
     */
    public static final String MODIFICATION_DATE_FIELD = "expense_entry.modification_date";

    /**
     * Represents the constant for the creation date field in the expense status table.
     */
    public static final String EXPENSE_STATUS_CREATION_DATE_FIELD = "expense_status.creation_date";

    /**
     * Represents the constant for the modification date field in the expense status table.
     */
    public static final String EXPENSE_STATUS_MODIFICATION_DATE_FIELD = "expense_status.modification_date";

    /**
     * Represents the constant for the creation date field in the expense type table.
     */
    public static final String EXPENSE_TYPE_CREATION_DATE_FIELD = "expense_type.creation_date";

    /**
     * Represents the constant for the modification date field in the expense type table.
     */
    public static final String EXPENSE_TYPE_MODIFICATION_DATE_FIELD = "expense_type.modification_date";

    /**
     * <p>
     * Represents the field the match should be applied on. The field must be fully qualified (table.field) because the
     * search query joins more tables. Cannot be null, empty or all spaces.
     * </p>
     */
    private final String field;

    /**
     * <p>
     * The lower value of the interval the field should be matched against (using between). Can be <code>null</code>
     * (for saying there is no lower limit) but only when toValue is not <code>null</code>.
     * </p>
     */
    private final Object fromValue;

    /**
     * <p>
     * The higher value of the interval the field should be matched against (using between). Can be <code>null</code>
     * (for saying there is no higher limit) but only when fromValue is not <code>null</code>.
     * </p>
     */
    private final Object toValue;

    /** Represents the where clause of this criteria. */
    private String whereClause;

    /** Represents the parameters of this criteria. */
    private Object[] parameters;

    /**
     * <p>
     * Creates a new instance of <code>FieldBetweenCriteria</code> class. It will create a field between two given
     * values criteria. The field must be fully qualified (table.field) because the search query joins more tables.
     * The between filter is open ended (one limit being <code>null</code> means to limit) and inclusive.
     * </p>
     *
     * @param field the field the match should be applied on.
     * @param fromValue the lower value of the interval (can be <code>null</code>)
     * @param toValue the higher value of the interval (can be <code>null</code>)
     *
     * @throws IllegalArgumentException if both value arguments are <code>null</code> at the same time; or if the field
     *         is <code>null</code>, empty or all spaces.
     */
    public FieldBetweenCriteria(String field, Object fromValue, Object toValue) {
        ExpenseEntryHelper.validateString(field, "field");

        if ((fromValue == null) && (toValue == null)) {
            throw new IllegalArgumentException("value arguments can not be null either.");
        }

        this.field = field;
        this.fromValue = fromValue;
        this.toValue = toValue;
    }

    /**
     * <p>
     * Gets the field the match should be applied on.
     * </p>
     *
     * @return the field the match should be applied on.
     */
    public String getField() {
        return field;
    }

    /**
     * <p>
     * Gets the lower limit value of the between match interval.
     * </p>
     *
     * @return the lower limit value of the between match interval.
     */
    public Object getFromValue() {
        return fromValue;
    }

    /**
     * <p>
     * Gets the higher limit value of the between match interval.
     * </p>
     *
     * @return the higher limit value of the between match interval.
     */
    public Object getToValue() {
        return toValue;
    }

    /**
     * <p>
     * Gets the where clause expression that can be used in an SQL query to perform the filtering. The expression is
     * expected to contain ? characters where the criteria parameters should be inserted. This is needed because
     * <code>PreparedStatement</code> can be used to execute the query.
     * </p>
     *
     * @return the where clause expression that can be used in an SQL query to perform the filtering.
     */
    public String getWhereClause() {
        if (whereClause == null) {
            if (fromValue == null) {
                this.whereClause = field + " <= ?";
            } else if (toValue == null) {
                this.whereClause = field + " >= ?";
            } else {
                this.whereClause = field + " between ? and ?";
            }
        }

        return whereClause;
    }

    /**
     * <p>
     * Gets the parameter values that should be used to fill the where clause expression returned by the
     * <code>getWhereClause</code> method. The parameter values should be inserted in place of the ? characters in the
     * where clause expression. The number of parameters should be equal to the number of ? characters in the where
     * clause expression.
     * </p>
     *
     * @return the parameters used in the expression.
     */
    public Object[] getParameters() {
        if (parameters == null) {
            if (fromValue == null) {
                this.parameters = new Object[] {toValue};
            } else if (toValue == null) {
                this.parameters = new Object[] {fromValue};
            } else {
                this.parameters = new Object[] {fromValue, toValue};
            }
        }

        return (Object[]) parameters.clone();
    }

    /**
     * <p>
     * Static shortcut method for creating an amount between two given values criteria. The between filter can be open
     * ended (one limit being <code>null</code> means no limit) and inclusive.
     * </p>
     *
     * @param fromValue the lower value of the interval (can be <code>null</code>)
     * @param toValue the higher value of the interval (can be <code>null</code>)
     *
     * @return the created amount between criteria instance.
     *
     * @throws IllegalArgumentException if both value arguments are <code>null</code> at the same time.
     */
    public static FieldBetweenCriteria getAmountBetweenCriteria(BigDecimal fromValue, BigDecimal toValue) {
        return new FieldBetweenCriteria(AMOUNT_FIELD, fromValue, toValue);
    }

    /**
     * <p>
     * Static shortcut method for creating a creation date between two given dates criteria. The between filter can be
     * open ended (one limit being <code>null</code> means to limit) and inclusive.
     * </p>
     *
     * @param fromDate the lower value of the interval (can be <code>null</code>)
     * @param toDate the higher value of the interval (can be <code>null</code>)
     *
     * @return the created creation date between criteria instance.
     *
     * @throws IllegalArgumentException if both value arguments are <code>null</code> at the same time.
     */
    public static FieldBetweenCriteria getCreationDateBetweenCriteria(Date fromDate, Date toDate) {
        return new FieldBetweenCriteria(CREATION_DATE_FIELD, fromDate, toDate);
    }

    /**
     * <p>
     * Static shortcut method for creating a modification date between two given dates criteria. The between filter can
     * be open ended (one limit being <code>null</code> means to limit) and inclusive.
     * </p>
     *
     * @param fromDate the lower value of the interval (can be <code>null</code>)
     * @param toDate the higher value of the interval (can be <code>null</code>)
     *
     * @return the created modification date between criteria instance.
     *
     * @throws IllegalArgumentException if both value arguments are <code>null</code> at the same time.
     */
    public static FieldBetweenCriteria getModificationDateBetweenCriteria(Date fromDate, Date toDate) {
        return new FieldBetweenCriteria(MODIFICATION_DATE_FIELD, fromDate, toDate);
    }

    /**
     * <p>
     * Static shortcut method for creating a creation date between two given dates criteria for expense status. The
     * between filter can be open ended (one limit being <code>null</code> means to limit) and inclusive.
     * </p>
     *
     * @param fromDate the lower value of the interval (can be <code>null</code>)
     * @param toDate the higher value of the interval (can be <code>null</code>)
     *
     * @return the created modification date between criteria instance.
     *
     * @throws IllegalArgumentException if both value arguments are <code>null</code> at the same time.
     */
    public static FieldBetweenCriteria getExpenseStatusCreationDateBetweenCriteria(Date fromDate, Date toDate) {
        return new FieldBetweenCriteria(EXPENSE_STATUS_CREATION_DATE_FIELD, fromDate, toDate);
    }

    /**
     * <p>
     * Static shortcut method for creating a modification date between two given dates criteria for expense status. The
     * between filter can be open ended (one limit being <code>null</code> means to limit) and inclusive.
     * </p>
     *
     * @param fromDate the lower value of the interval (can be <code>null</code>)
     * @param toDate the higher value of the interval (can be <code>null</code>)
     *
     * @return the created modification date between criteria instance.
     *
     * @throws IllegalArgumentException if both value arguments are <code>null</code> at the same time.
     */
    public static FieldBetweenCriteria getExpenseStatusModificationDateBetweenCriteria(Date fromDate, Date toDate) {
        return new FieldBetweenCriteria(EXPENSE_STATUS_MODIFICATION_DATE_FIELD, fromDate, toDate);
    }

    /**
     * <p>
     * Static shortcut method for creating a creation date between two given dates criteria for expense type. The
     * between filter can be open ended (one limit being <code>null</code> means to limit) and inclusive.
     * </p>
     *
     * @param fromDate the lower value of the interval (can be <code>null</code>)
     * @param toDate the higher value of the interval (can be <code>null</code>)
     *
     * @return the created modification date between criteria instance.
     *
     * @throws IllegalArgumentException if both value arguments are <code>null</code> at the same time.
     */
    public static FieldBetweenCriteria getExpenseTypeCreationDateBetweenCriteria(Date fromDate, Date toDate) {
        return new FieldBetweenCriteria(EXPENSE_TYPE_CREATION_DATE_FIELD, fromDate, toDate);
    }

    /**
     * <p>
     * Static shortcut method for creating a modification date between two given dates criteria for expense type. The
     * between filter can be open ended (one limit being <code>null</code> means to limit) and inclusive.
     * </p>
     *
     * @param fromDate the lower value of the interval (can be <code>null</code>)
     * @param toDate the higher value of the interval (can be <code>null</code>)
     *
     * @return the created modification date between criteria instance.
     *
     * @throws IllegalArgumentException if both value arguments are <code>null</code> at the same time.
     */
    public static FieldBetweenCriteria getExpenseTypeModificationDateBetweenCriteria(Date fromDate, Date toDate) {
        return new FieldBetweenCriteria(EXPENSE_TYPE_MODIFICATION_DATE_FIELD, fromDate, toDate);
    }
}
