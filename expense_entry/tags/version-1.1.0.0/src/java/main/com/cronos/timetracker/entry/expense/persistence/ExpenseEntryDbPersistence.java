/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.timetracker.entry.expense.persistence;

import com.cronos.timetracker.entry.expense.ConfigurationException;
import com.cronos.timetracker.entry.expense.ExpenseEntry;
import com.cronos.timetracker.entry.expense.ExpenseEntryRejectReason;
import com.cronos.timetracker.entry.expense.ExpenseEntryStatus;
import com.cronos.timetracker.entry.expense.ExpenseEntryType;
import com.cronos.timetracker.entry.expense.search.Criteria;

import com.topcoder.db.connectionfactory.DBConnectionException;
import com.topcoder.db.connectionfactory.DBConnectionFactory;
import com.topcoder.db.connectionfactory.DBConnectionFactoryImpl;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * <p>
 * Implements <code>ExpenseEntryPersistence</code> interface to provide functionality to manipulate
 * <code>ExpenseEntry</code> instance in database. The operations include adding, updating, deleting and retrieving.
 * The information in expense entry is used directly without any modifications. The database connection can be either
 * obtained from the DB connection factory using a connection producer name, or given directly. The expense entries
 * are stored in the database using the following DDL:
 * <pre>
 * CREATE TABLE ExpenseEntries (
 *        ExpenseEntriesID     integer NOT NULL,
 *        ExpenseTypesID       integer NOT NULL,
 *        ExpenseStatusesID    integer NOT NULL,
 *        Description          varchar(64) NOT NULL,
 *        EntryDate            datetime year to second NOT NULL,
 *        Amount               money NOT NULL,
 *        Billable             smallint NOT NULL,
 *        CreationDate         datetime year to second NOT NULL,
 *        CreationUser         varchar(64) NOT NULL,
 *        ModificationDate     datetime year to second NOT NULL,
 *        ModificationUser     varchar(64) NOT NULL,
 *        PRIMARY KEY (ExpenseEntriesID)
 * );
 * </pre>
 * </p>
 *
 * <p>
 * Changes in 1.1: Four new methods for doing bulk operations on sets of expense entries have been added. These method
 * can work in atomic mode (a failure on one entry causes the entire operation to fail) or non-atomic (a failure in
 * one entry doesn't affect the other and the user has a way to know which ones failed). There is also a search method
 * that provides the capability to search expense entries at the database level and return the ones that match.
 * </p>
 *
 * @author adic, TCSDEVELOPER
 * @author DanLazar, visualage
 * @version 1.1
 *
 * @since 1.0
 */
public class ExpenseEntryDbPersistence implements ExpenseEntryPersistence {
    /** Represents the prepared SQL statement to add an expense entry. */
    private static final String ADD_ENTRY_SQL = "INSERT INTO ExpenseEntries (ExpenseEntriesID, ExpenseTypesID, "
        + "ExpenseStatusesID, Description, EntryDate, Amount, Billable, CreationDate, CreationUser, ModificationDate, "
        + "ModificationUser) VALUES (?,?,?,?,?,?,?,?,?,?,?)";

    /**
     * Represents the prepared SQL statement to add into the exp_reject_reason table.
     *
     * @since 1.1
     */
    private static final String ADD_EXP_REJECT_REASON_SQL = "INSERT INTO exp_reject_reason (ExpenseEntriesID,"
        + "reject_reason_id, creation_date, creation_user, modification_date, modification_user) "
        + "VALUES (?,?,?,?,?,?)";

    /** Represents the prepared SQL statement to update an expense entry. */
    private static final String UPDATE_ENTRY_SQL = "UPDATE ExpenseEntries SET ExpenseTypesID=?, ExpenseStatusesID=?, "
        + "Description=?, EntryDate=?, Amount=?, Billable=?, ModificationDate=?, ModificationUser=? "
        + "WHERE ExpenseEntriesID=?";

    /** Represents the prepared SQL statement to delete an expense entry. */
    private static final String DELETE_ENTRY_SQL = "DELETE FROM ExpenseEntries WHERE ExpenseEntriesID=?";

    /**
     * Represents the prepared SQL statement to delete the exp_reject_reason table.
     *
     * @since 1.1
     */
    private static final String DELETE_EXP_REJECT_REASON_SQL = "DELETE FROM exp_reject_reason "
        + "WHERE ExpenseEntriesID=?";

    /** Represents the prepared SQL statement to delete all expense entries. */
    private static final String DELETE_ALL_ENTRY_SQL = "DELETE FROM ExpenseEntries";

    /**
     * Represents the prepared SQL statement to delete all the records in exp_reject_reason table.
     *
     * @since 1.1
     */
    private static final String DELETE_ALL_EXP_REJECT_REASON_SQL = "DELETE FROM exp_reject_reason";

    /** Represents the prepared SQL statement to get an expense entry, including its entry type and status. */
    private static final String RETRIEVE_ENTRY_SQL = "SELECT E.ExpenseEntriesID, E.ExpenseTypesID, "
        + "E.ExpenseStatusesID, E.Description, E.EntryDate, E.Amount, E.Billable, E.CreationDate, E.CreationUser, "
        + "E.ModificationDate, E.ModificationUser, T.Description AS TypeDescription, "
        + "T.CreationDate AS TypeCreationDate, T.CreationUser AS TypeCreationUser, "
        + "T.ModificationDate AS TypeModificationDate, T.ModificationUser AS TypeModificationUser, "
        + "S.Description AS StatusDescription, S.CreationDate AS StatusCreationDate, "
        + "S.CreationUser AS StatusCreationUser, S.ModificationDate AS StatusModificationDate, "
        + "S.ModificationUser AS StatusModificationUser FROM ExpenseEntries AS E "
        + "LEFT OUTER JOIN ExpenseTypes AS T ON E.ExpenseTypesID = T.ExpenseTypesID "
        + "LEFT OUTER JOIN ExpenseStatuses AS S ON E.ExpenseStatusesID = S.ExpenseStatusesID "
        + "WHERE E.ExpenseEntriesID=?";

    /**
     * Represents the prepared SQL statement to get the reject reasons with given ExpenseEntriesID.
     *
     * @since 1.1
     */
    private static final String RETRIEVE_REJECT_REASON_SQL = "SELECT R.reject_reason_id, "
        + "R.creation_date, R.creation_user, R.modification_date, R.modification_user FROM exp_reject_reason AS E "
        + "LEFT OUTER JOIN reject_reason AS R ON E.reject_reason_id = R.reject_reason_id "
        + "WHERE E.ExpenseEntriesID=?";

    /**
     * Represents the prepared SQL statement to get all expense entries, including their entry types and statuses.
     *
     * @since 1.0
     */
    private static final String RETRIEVE_ALL_ENTRY_SQL = "SELECT ExpenseEntries.ExpenseEntriesID, "
        + "ExpenseEntries.ExpenseTypesID, ExpenseEntries.ExpenseStatusesID, ExpenseEntries.Description, "
        + "ExpenseEntries.EntryDate, ExpenseEntries.Amount, ExpenseEntries.Billable, ExpenseEntries.CreationDate, "
        + "ExpenseEntries.CreationUser, ExpenseEntries.ModificationDate, ExpenseEntries.ModificationUser,"
        + "ExpenseTypes.Description AS TypeDescription, "
        + "ExpenseTypes.CreationDate AS TypeCreationDate, ExpenseTypes.CreationUser AS TypeCreationUser, "
        + "ExpenseTypes.ModificationDate AS TypeModificationDate, ExpenseTypes.ModificationUser AS "
        + "TypeModificationUser, ExpenseStatuses.Description AS StatusDescription, "
        + "ExpenseStatuses.CreationDate AS StatusCreationDate, ExpenseStatuses.CreationUser AS "
        + "StatusCreationUser, ExpenseStatuses.ModificationDate AS StatusModificationDate, "
        + "ExpenseStatuses.ModificationUser AS StatusModificationUser FROM ExpenseEntries "
        + "LEFT OUTER JOIN ExpenseTypes ON ExpenseEntries.ExpenseTypesID = ExpenseTypes.ExpenseTypesID "
        + "LEFT OUTER JOIN ExpenseStatuses ON ExpenseEntries.ExpenseStatusesID = ExpenseStatuses.ExpenseStatusesID";

    /** Represents the column name for expense entry ID. */
    private static final String ID_COLUMN = "ExpenseEntriesID";

    /** Represents the column name for the expense type ID of the expense entry. */
    private static final String TYPE_ID_COLUMN = "ExpenseTypesID";

    /** Represents the column name for the expense status ID of the expense entry. */
    private static final String STATUS_ID_COLUMN = "ExpenseStatusesID";

    /**
     * Represents the column name for the reject reason ID of the expense entry.
     *
     * @since 1.1
     */
    private static final String REASON_ID_COLUMN = "reject_reason_id";

    /** Represents the column name for expense entry description. */
    private static final String DESCRIPTION_COLUMN = "Description";

    /** Represents the column name for expense entry date. */
    private static final String DATE_COLUMN = "EntryDate";

    /** Represents the column name for expense entry amount. */
    private static final String AMOUNT_COLUMN = "Amount";

    /** Represents the column name for a flag indicating whether the expense entry is billable. */
    private static final String BILLABLE_COLUMN = "Billable";

    /** Represents the column name for expense entry creation date. */
    private static final String CREATION_DATE_COLUMN = "CreationDate";

    /** Represents the column name for expense entry creation user. */
    private static final String CREATION_USER_COLUMN = "CreationUser";

    /** Represents the column name for expense entry modification date. */
    private static final String MODIFICATION_DATE_COLUMN = "ModificationDate";

    /** Represents the column name for expense entry modification user. */
    private static final String MODIFICATION_USER_COLUMN = "ModificationUser";

    /** Represents the column name for expense entry type description. */
    private static final String TYPE_DESCRIPTION_COLUMN = "TypeDescription";

    /** Represents the column name for expense entry type creation date. */
    private static final String TYPE_CREATION_DATE_COLUMN = "TypeCreationDate";

    /** Represents the column name for expense entry type creation user. */
    private static final String TYPE_CREATION_USER_COLUMN = "TypeCreationUser";

    /** Represents the column name for expense entry type modification date. */
    private static final String TYPE_MODIFICATION_DATE_COLUMN = "TypeModificationDate";

    /** Represents the column name for expense entry type modification user. */
    private static final String TYPE_MODIFICATION_USER_COLUMN = "TypeModificationUser";

    /** Represents the column name for expense entry status description. */
    private static final String STATUS_DESCRIPTION_COLUMN = "StatusDescription";

    /** Represents the column name for expense entry status creation date. */
    private static final String STATUS_CREATION_DATE_COLUMN = "StatusCreationDate";

    /** Represents the column name for expense entry status creation user. */
    private static final String STATUS_CREATION_USER_COLUMN = "StatusCreationUser";

    /** Represents the column name for expense entry status modification date. */
    private static final String STATUS_MODIFICATION_DATE_COLUMN = "StatusModificationDate";

    /** Represents the column name for expense entry status modification user. */
    private static final String STATUS_MODIFICATION_USER_COLUMN = "StatusModificationUser";

    /**
     * Represents the column name for reject reason creation date.
     *
     * @since 1.1
     */
    private static final String REASON_CREATION_DATE_COLUMN = "creation_date";

    /**
     * Represents the column name for reject reason creation user.
     *
     * @since 1.1
     */
    private static final String REASON_CREATION_USER_COLUMN = "creation_user";

    /**
     * Represents the column name for reject reason modification date.
     *
     * @since 1.1
     */
    private static final String REASON_MODIFICATION_DATE_COLUMN = "modification_date";

    /**
     * Represents the column name for reject reason modification user.
     *
     * @since 1.1
     */
    private static final String REASON_MODIFICATION_USER_COLUMN = "modification_user";

    /** Represents the parameter index of ID in INSERT SQL statement. */
    private static final int INSERT_ID_INDEX = 1;

    /** Represents the parameter index of expense entry type ID in INSERT SQL statement. */
    private static final int INSERT_TYPEID_INDEX = 2;

    /** Represents the parameter index of expense entry status ID in INSERT SQL statement. */
    private static final int INSERT_STATUSID_INDEX = 3;

    /** Represents the parameter index of description in INSERT SQL statement. */
    private static final int INSERT_DESCRIPTION_INDEX = 4;

    /** Represents the parameter index of date in INSERT SQL statement. */
    private static final int INSERT_DATE_INDEX = 5;

    /** Represents the parameter index of amount of money in INSERT SQL statement. */
    private static final int INSERT_AMOUNT_INDEX = 6;

    /** Represents the parameter index of billable flag in INSERT SQL statement. */
    private static final int INSERT_BILLABLE_INDEX = 7;

    /** Represents the parameter index of creation date in INSERT SQL statement. */
    private static final int INSERT_CREATIONDATE_INDEX = 8;

    /** Represents the parameter index of creation user in INSERT SQL statement. */
    private static final int INSERT_CREATIONUSER_INDEX = 9;

    /** Represents the parameter index of modification date in INSERT SQL statement. */
    private static final int INSERT_MODIFICATIONDATE_INDEX = 10;

    /** Represents the parameter index of modification user in INSERT SQL statement. */
    private static final int INSERT_MODIFICATIONUSER_INDEX = 11;

    /** Represents the parameter index of task type ID in UPDATE SQL statement. */
    private static final int UPDATE_TYPEID_INDEX = 1;

    /** Represents the parameter index of time status ID in UPDATE SQL statement. */
    private static final int UPDATE_STATUSID_INDEX = 2;

    /** Represents the parameter index of description in UPDATE SQL statement. */
    private static final int UPDATE_DESCRIPTION_INDEX = 3;

    /** Represents the parameter index of date in UPDATE SQL statement. */
    private static final int UPDATE_DATE_INDEX = 4;

    /** Represents the parameter index of amount of money in UPDATE SQL statement. */
    private static final int UPDATE_AMOUNT_INDEX = 5;

    /** Represents the parameter index of billable flag in UPDATE SQL statement. */
    private static final int UPDATE_BILLABLE_INDEX = 6;

    /** Represents the parameter index of modification date in UPDATE SQL statement. */
    private static final int UPDATE_MODIFICATIONDATE_INDEX = 7;

    /** Represents the parameter index of modification user in UPDATE SQL statement. */
    private static final int UPDATE_MODIFICATIONUSER_INDEX = 8;

    /** Represents the parameter index of ID in UPDATE SQL statement. */
    private static final int UPDATE_ID_INDEX = 9;

    /**
     * Represents the current connection used to access database. If it is <code>null</code>, when needed, the
     * connection is obtained from the DB connection factory using the connection producer name.
     */
    private Connection connection = null;

    /** Represents the connection producer name used to obtain a new database connection from DB connection factory. */
    private String connectionProducerName = null;

    /** Represents the namespace used to create DB connection factory. */
    private String namespace = null;

    /** Represents the DB connection factory instance used to create connections. */
    private DBConnectionFactory factory = null;

    /**
     * <p>
     * Creates a new instance of <code>ExpenseEntryDbPersistence</code> class. The DB connection factory is created
     * from the given namespace.
     * </p>
     *
     * @param namespace the namespace to create DB connection factory.
     *
     * @throws NullPointerException if <code>namespace</code> is <code>null</code>.
     * @throws IllegalArgumentException if <code>namespace</code> is empty string.
     * @throws ConfigurationException if DB connection factory cannot be created.
     */
    public ExpenseEntryDbPersistence(String namespace) throws ConfigurationException {
        PersistenceHelper.validateNotNullOrEmpty(namespace, "namespace");

        this.namespace = namespace;

        try {
            factory = new DBConnectionFactoryImpl(this.namespace);
        } catch (DBConnectionException e) {
            throw new ConfigurationException("DB connection factory cannot be created.", e);
        } catch (com.topcoder.db.connectionfactory.ConfigurationException e) {
            throw new ConfigurationException("DB connection factory cannot be created.", e);
        }
    }

    /**
     * <p>
     * Creates a new instance of <code>ExpenseEntryDbPersistence</code> class. The connection producer name used to
     * obtain database connection is given. The namespace used to create DB connection factory is also given.
     * </p>
     *
     * @param connectionProducerName the connection producer name to obtain database connection from DB connection
     *        factory.
     * @param namespace the namespace to create DB connection factory.
     *
     * @throws NullPointerException if <code>connectionProducerName</code> or <code>namespace</code> is
     *         <code>null</code>.
     * @throws IllegalArgumentException <code>connectionProducerName</code> or <code>namespace</code> is empty string.
     * @throws PersistenceException if <code>connectionProducerName</code> does not exist in DB connection factory.
     */
    public ExpenseEntryDbPersistence(String connectionProducerName, String namespace)
        throws PersistenceException {
        PersistenceHelper.validateNotNullOrEmpty(connectionProducerName, "connectionProducerName");
        PersistenceHelper.validateNotNullOrEmpty(namespace, "namespace");

        this.namespace = namespace;
        this.connectionProducerName = connectionProducerName;

        try {
            factory = new DBConnectionFactoryImpl(namespace);
        } catch (DBConnectionException e) {
            throw new PersistenceException("DB connection factory cannot be created.", e);
        } catch (com.topcoder.db.connectionfactory.ConfigurationException e) {
            throw new PersistenceException("DB connection factory cannot be created.", e);
        }

        if (!((DBConnectionFactoryImpl) factory).contains(connectionProducerName)) {
            throw new PersistenceException("The connection producer is not available.");
        }
    }

    /**
     * <p>
     * Adds a new <code>ExpenseEntry</code> instance to the database.
     * </p>
     *
     * <p>
     * Changes in 1.1: The expense entry may contain now several reject reasons. For each contained reject reason, a
     * row needs to be inserted in the exp_reject_reason table.
     * </p>
     *
     * @param entry the expense entry to be added to the database.
     *
     * @return <code>true</code> if the ID does not exist in database and the expense entry is added;
     *         <code>false</code> otherwise.
     *
     * @throws NullPointerException if <code>entry</code> is <code>null</code>.
     * @throws PersistenceException if error occurs when adding the expense entry to the persistence.
     *
     * @since 1.0
     */
    public boolean addEntry(ExpenseEntry entry) throws PersistenceException {
        PersistenceHelper.validateNotNull(entry, "entry");

        // Check if the ID already exists in database
        if (retrieveEntry(entry.getId()) != null) {
            return false;
        }

        createConnection(connectionProducerName);

        Date now = new Date(new java.util.Date().getTime());
        PreparedStatement statement = null;

        try {
            statement = connection.prepareStatement(ADD_ENTRY_SQL);

            // Set parameters
            statement.setInt(INSERT_ID_INDEX, entry.getId());
            statement.setInt(INSERT_TYPEID_INDEX, entry.getExpenseType().getId());
            statement.setInt(INSERT_STATUSID_INDEX, entry.getStatus().getId());
            statement.setString(INSERT_DESCRIPTION_INDEX, entry.getDescription());
            statement.setDate(INSERT_DATE_INDEX, new Date(entry.getDate().getTime()));
            statement.setBigDecimal(INSERT_AMOUNT_INDEX, entry.getAmount());
            statement.setShort(INSERT_BILLABLE_INDEX, entry.isBillable() ? (short) 1 : (short) 0);
            statement.setDate(INSERT_CREATIONDATE_INDEX, now);
            statement.setString(INSERT_CREATIONUSER_INDEX, entry.getCreationUser());
            statement.setDate(INSERT_MODIFICATIONDATE_INDEX, now);
            statement.setString(INSERT_MODIFICATIONUSER_INDEX, entry.getModificationUser());

            // Execute
            statement.executeUpdate();

            // add into the exp_reject_reason table
            statement = connection.prepareStatement(ADD_EXP_REJECT_REASON_SQL);

            ExpenseEntryRejectReason[] rejectReasons = entry.getRejectReasons();

            for (int i = 0; i < rejectReasons.length; ++i) {
                int index = 0;
                statement.setInt(++index, entry.getId());
                statement.setInt(++index, rejectReasons[i].getRejectReasonId());
                statement.setDate(++index, now);
                statement.setString(++index, entry.getCreationUser());
                statement.setDate(++index, now);
                statement.setString(++index, entry.getModificationUser());
                statement.executeUpdate();
            }

            // Set property only after successful execution of the query
            entry.setCreationDate(now);
            entry.setModificationDate(now);
        } catch (SQLException e) {
            throw new PersistenceException("Accessing database fails.", e);
        } finally {
            PersistenceHelper.close(statement);
        }

        return true;
    }

    /**
     * <p>
     * Deletes an <code>ExpenseEntry</code> instance with the given ID from the database.
     * </p>
     *
     * <p>
     * Changes in 1.1: The expense entry may contain now several reject reasons. When deleting an entry, the linked
     * rows from exp_reject_reason must be deleted too.
     * </p>
     *
     * @param entryId the ID of the expense entry to be deleted.
     *
     * @return <code>true</code> if the expense entry exists in database and is deleted; <code>false</code> otherwise.
     *
     * @throws PersistenceException if error occurs when deleting the expense entry.
     *
     * @since 1.0
     */
    public boolean deleteEntry(int entryId) throws PersistenceException {
        createConnection(connectionProducerName);

        PreparedStatement statement = null;
        boolean success;

        try {
            // delete from the exp_reject_reason table
            statement = connection.prepareStatement(DELETE_EXP_REJECT_REASON_SQL);
            statement.setInt(1, entryId);
            statement.executeUpdate();

            // delete from the ExpenseEntries table
            statement = connection.prepareStatement(DELETE_ENTRY_SQL);

            // Set parameters
            statement.setInt(1, entryId);

            // Execute
            success = (statement.executeUpdate() > 0);
        } catch (SQLException e) {
            throw new PersistenceException("Accessing database fails.", e);
        } catch (NullPointerException e) {
            throw new PersistenceException("Status or type is null.", e);
        } finally {
            PersistenceHelper.close(statement);
        }

        return success;
    }

    /**
     * <p>
     * Deletes all <code>ExpenseEntry</code> instances in the database.
     * </p>
     *
     * <p>
     * Changes in 1.1: The expense entry may contain now several reject reasons. When deleting an entry, the linked
     * rows from exp_reject_reason must be deleted too.
     * </p>
     *
     * @throws PersistenceException if error occurs when deleting the expense entries.
     *
     * @since 1.0
     */
    public void deleteAllEntries() throws PersistenceException {
        createConnection(connectionProducerName);

        Statement statement = null;

        try {
            statement = connection.createStatement();
            statement.execute(DELETE_ALL_EXP_REJECT_REASON_SQL);
            statement.execute(DELETE_ALL_ENTRY_SQL);
        } catch (SQLException e) {
            throw new PersistenceException("Accessing database fails.", e);
        } finally {
            PersistenceHelper.close(statement);
        }
    }

    /**
     * <p>
     * Updates an <code>ExpenseEntry</code> instance to the database.
     * </p>
     *
     * <p>
     * Changes in 1.1: The expense entry may contain now several reject reasons. The exp_reject_reason stores in
     * persistence the reject reason ids for an expense entries. The updated entry may contain a totally different set
     * of reject reasons. In order to update the table we have to retrieve all reject records for the expense entry.
     * The common reject entries (all fields, including users and dates) from the retrieved reject entries and from
     * the update object must be eliminated (they don't need updating). All reject entries present in the update
     * object but not in the table, must be added. All reject entries present in the table but not in the update
     * object, must be deleted. The reject entries present in both but with different users/dates, must be updated.
     * </p>
     *
     * @param entry the expense entry to be updated in the database.
     *
     * @return <code>true</code> if the expense entry exists in database and is updated; <code>false</code> otherwise.
     *
     * @throws NullPointerException if <code>entry</code> is <code>null</code>.
     * @throws PersistenceException if error occurs when updating the expense entry.
     *
     * @since 1.0
     */
    public boolean updateEntry(ExpenseEntry entry) throws PersistenceException {
        PersistenceHelper.validateNotNull(entry, "entry");

        // Check if the ID already exists in database
        ExpenseEntry oldEntry = retrieveEntry(entry.getId());

        if (oldEntry == null) {
            return false;
        }

        createConnection(connectionProducerName);

        Date now = new Date(new java.util.Date().getTime());
        PreparedStatement statement = null;

        try {
            statement = connection.prepareStatement(UPDATE_ENTRY_SQL);

            // Set parameters
            statement.setInt(UPDATE_TYPEID_INDEX, entry.getExpenseType().getId());
            statement.setInt(UPDATE_STATUSID_INDEX, entry.getStatus().getId());
            statement.setString(UPDATE_DESCRIPTION_INDEX, entry.getDescription());
            statement.setDate(UPDATE_DATE_INDEX, new Date(entry.getDate().getTime()));
            statement.setBigDecimal(UPDATE_AMOUNT_INDEX, entry.getAmount());
            statement.setShort(UPDATE_BILLABLE_INDEX, entry.isBillable() ? (short) 1 : (short) 0);
            statement.setDate(UPDATE_MODIFICATIONDATE_INDEX, now);
            statement.setString(UPDATE_MODIFICATIONUSER_INDEX, entry.getModificationUser());
            statement.setInt(UPDATE_ID_INDEX, entry.getId());

            // Execute
            statement.executeUpdate();

            // update the exp_reject_reason table
            // first delete all the records by given ExpenseEntriesID
            statement = connection.prepareStatement(DELETE_EXP_REJECT_REASON_SQL);
            statement.setInt(1, entry.getId());
            statement.executeUpdate();

            // then add all the records into the exp_reject_reason table.
            statement = connection.prepareStatement(ADD_EXP_REJECT_REASON_SQL);

            ExpenseEntryRejectReason[] rejectReasons = entry.getRejectReasons();

            for (int i = 0; i < rejectReasons.length; ++i) {
                int index = 0;
                statement.setInt(++index, entry.getId());
                statement.setInt(++index, rejectReasons[i].getRejectReasonId());
                statement.setDate(++index, new Date(oldEntry.getCreationDate().getTime()));
                statement.setString(++index, oldEntry.getCreationUser());
                statement.setDate(++index, now);
                statement.setString(++index, entry.getModificationUser());
                statement.executeUpdate();
            }

            // Set property only after successful execution of the query
            entry.setModificationDate(now);
        } catch (SQLException e) {
            throw new PersistenceException("Accessing database fails.", e);
        } catch (NullPointerException e) {
            throw new PersistenceException("Status or type is null.", e);
        } finally {
            PersistenceHelper.close(statement);
        }

        return true;
    }

    /**
     * <p>
     * Retrieves an <code>ExpenseEntry</code> instance with the given ID from the database. The related
     * <code>ExpenseEntryType</code> and <code>ExpenseEntryStatus</code> instances are also retrieved.
     * </p>
     *
     * <p>
     * Changes in 1.1: The expense entry may contain now several reject reasons. The retrieval query must now be joined
     * with exp_reject_reason on the ExpenseEntriesID field (a separate query is acceptable too). The reject reason
     * fields will populate instances of the ExpenseEntryRejectReason class.
     * </p>
     *
     * @param entryId the ID of the expense entry to be retrieved.
     *
     * @return an <code>ExpenseEntry</code> instance with the given ID; or <code>null</code> if such instance cannot be
     *         found in the database.
     *
     * @throws PersistenceException if error occurs when retrieving the expense entry; or the value in database is
     *         invalid.
     *
     * @since 1.0
     */
    public ExpenseEntry retrieveEntry(int entryId) throws PersistenceException {
        createConnection(connectionProducerName);

        PreparedStatement statement = null;
        ResultSet resultSet = null;
        ExpenseEntry entry;

        try {
            statement = connection.prepareStatement(RETRIEVE_ENTRY_SQL);

            // Set parameter
            statement.setInt(1, entryId);

            // Execute
            resultSet = statement.executeQuery();

            entry = createExpenseEntry(resultSet);

            if (entry == null) {
                return null;
            }

            // retrieve the reject reasons
            statement = connection.prepareStatement(RETRIEVE_REJECT_REASON_SQL);
            retrieveRejectReasons(statement, entry);
        } catch (SQLException e) {
            throw new PersistenceException("Accessing database fails.", e);
        } finally {
            PersistenceHelper.close(resultSet);
            PersistenceHelper.close(statement);
        }

        return entry;
    }

    /**
     * <p>
     * Retrieves all <code>ExpenseEntry</code> instances from the database. The related <code>ExpenseEntryType</code>
     * and <code>ExpenseEntryStatus</code> instances are also retrieved.
     * </p>
     *
     * <p>
     * Changes in 1.1: The expense entry may contain now several reject reasons. The retrieval query must now be joined
     * with exp_reject_reason on the ExpenseEntriesID field (a separate query is acceptable too and in this case
     * perhaps it is recommended since retrieving all the information in one query is probably too much, especially
     * because of the one-to-many join between expense entries and reject reasons). The reject reason fields will
     * populate instances of the ExpenseEntryRejectReason class.
     * </p>
     *
     * @return a list of <code>ExpenseEntry</code> instances retrieved from the database.
     *
     * @throws PersistenceException if error occurs when retrieving the expense entries; or the value in database is
     *         invalid.
     *
     * @since 1.0
     */
    public List retrieveAllEntries() throws PersistenceException {
        createConnection(connectionProducerName);

        PreparedStatement statement = null;
        ResultSet resultSet = null;
        ExpenseEntry entry;
        List list = new ArrayList();

        try {
            statement = connection.prepareStatement(RETRIEVE_ALL_ENTRY_SQL);

            // Execute
            resultSet = statement.executeQuery();

            // For each record, create an expense entry status instance
            while ((entry = createExpenseEntry(resultSet)) != null) {
                list.add(entry);
            }

            // retrieve the reject reasons
            statement = connection.prepareStatement(RETRIEVE_REJECT_REASON_SQL);

            for (Iterator iter = list.iterator(); iter.hasNext();) {
                entry = (ExpenseEntry) iter.next();
                retrieveRejectReasons(statement, entry);
            }
        } catch (SQLException e) {
            throw new PersistenceException("Accessing database fails.", e);
        } finally {
            PersistenceHelper.close(resultSet);
            PersistenceHelper.close(statement);
        }

        return list;
    }

    /**
     * Retrieve the reject reasons with given expense entry. All the retrieved values will be added into the expense
     * entry instance.
     *
     * @param statement the statement to the database queries.
     * @param entry the expense entry instance to retrieve the reject reasons.
     *
     * @throws PersistenceException any exception during the database operations.
     *
     * @since 1.1
     */
    private void retrieveRejectReasons(PreparedStatement statement, ExpenseEntry entry)
        throws PersistenceException {
        ResultSet resultSet = null;

        try {
            statement.setInt(1, entry.getId());
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                ExpenseEntryRejectReason reason = new ExpenseEntryRejectReason(resultSet.getInt(REASON_ID_COLUMN));
                reason.setCreationDate(resultSet.getDate(REASON_CREATION_DATE_COLUMN));
                reason.setCreationUser(resultSet.getString(REASON_CREATION_USER_COLUMN));
                reason.setModificationDate(resultSet.getDate(REASON_MODIFICATION_DATE_COLUMN));
                reason.setModificationUser(resultSet.getString(REASON_MODIFICATION_USER_COLUMN));
                entry.addRejectReason(reason);
            }
        } catch (SQLException e) {
            throw new PersistenceException("Accessing database fails.", e);
        } finally {
            PersistenceHelper.close(resultSet);
        }
    }

    /**
     * <p>
     * Initializes the existing connection which is used to access database for future operations. The previous
     * connection is closed if it exists. The new connection is created from DB connection factory according to the
     * given connection producer name.
     * </p>
     *
     * @param connectionProducerName the connection producer name to obtain a connection from DB connection factory.
     *
     * @throws NullPointerException if <code>connectionProducerName</code> is <code>null</code>.
     * @throws IllegalArgumentException if <code>connectionProducerName</code> is empty string.
     * @throws PersistenceException if error occurs when closing the previous connection; or error occurs when
     *         obtaining the new connection from DB connection factory.
     */
    public void initConnection(String connectionProducerName) throws PersistenceException {
        PersistenceHelper.validateNotNullOrEmpty(connectionProducerName, "connectionProducerName");

        closeConnection();

        createConnection(connectionProducerName);
    }

    /**
     * <p>
     * Sets the existing connection which is used to access database for future operations. The previous connection is
     * closed if it exists.
     * </p>
     *
     * @param connection the connection used to access database.
     *
     * @throws NullPointerException if <code>connection</code> is <code>null</code>.
     * @throws PersistenceException if error occurs when closing the existing connection.
     */
    public void setConnection(Connection connection) throws PersistenceException {
        PersistenceHelper.validateNotNull(connection, "connection");

        closeConnection();

        this.connection = connection;
    }

    /**
     * <p>
     * Gets the existing connection. If there is no existing connection, <code>null</code> is returned.
     * </p>
     *
     * @return the existing connection if it is available; <code>null</code> otherwise.
     */
    public Connection getConnection() {
        return connection;
    }

    /**
     * <p>
     * Closes the existing connection if available.
     * </p>
     *
     * @throws PersistenceException if error occurs when closing the connection.
     */
    public void closeConnection() throws PersistenceException {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                throw new PersistenceException("Error occurs when closing connection.", e);
            }

            connection = null;
        }
    }

    /**
     * <p>
     * Adds a set of entries to the database.
     * </p>
     *
     * <p>
     * If the addition is atomic then it means that entire retrieval will fail if a single expense entry addition
     * fails.
     * </p>
     *
     * <p>
     * If the addition is non-atomic then it means each expense entry is added individually. If it fails, that won't
     * affect the others. A list with the failed entries is returned to the user (empty if no error occurs). If the
     * exception is related to acquiring an SQL connection or something like that, it is obvious that all entries will
     * fail so the exception should be thrown.
     * </p>
     *
     * <p>
     * Note: an JDBC transaction must be used in atomic mode to be able to rollback everything in case of failure.
     * </p>
     *
     * @param entries the entries to add.
     * @param isAtomic whether the operation should be atomic or not.
     *
     * @return the entries that failed to be added in non atomic mode.
     *
     * @throws IllegalArgumentException if the array is <code>null</code>, empty or has <code>null</code> element.
     * @throws PersistenceException wraps a persistence implementation specific exception (such as SQL exception).
     *
     * @since 1.1
     */
    public ExpenseEntry[] addEntries(ExpenseEntry[] entries, boolean isAtomic)
        throws PersistenceException {
        // argument validation
        if (entries == null) {
            throw new IllegalArgumentException("entries should not be null.");
        }

        if (entries.length == 0) {
            throw new IllegalArgumentException("entries should not be empty.");
        }

        for (int i = 0; i < entries.length; i++) {
            if (entries[i] == null) {
                throw new IllegalArgumentException("entries should not contain a null element.");
            }
        }

        return (ExpenseEntry[]) this.doTransaction(entries, isAtomic, 0);
    }

    /**
     * Does the transaction according to the type of action.
     *
     * @param objects the input collection to do the transaction.
     * @param isAtomic whether it is in atomic module.
     * @param action the type of the transaction. 0 means add; 1 means delete; 2 means update.
     *
     * @return the return value of the transaction.
     *
     * @throws PersistenceException wraps a persistence implementation specific exception (such as SQL exception).
     *
     * @since 1.1
     */
    private Object[] doTransaction(Object[] objects, boolean isAtomic, int action)
        throws PersistenceException {
        // If the exception is related to acquiring an SQL connection or something like that, it is obvious that
        // all entries will fail so the exception should be thrown.
        createConnection(connectionProducerName);

        boolean preAutoCommit = this.beginTransaction(connection, isAtomic);

        try {
            List errors = new ArrayList();

            boolean totalSuccess = true;
            PersistenceException persistenceException = null;

            // if it fails to add the entry(the same entry has already existed in the database), should
            // also cancel the entire retrieval. totalSuccess is used for this purpose
            for (int i = 0; i < objects.length; i++) {
                boolean success = true;

                try {
                    if (action == 0) {
                        success = this.addEntry((ExpenseEntry) objects[i]);
                    } else if (action == 1) {
                        success = this.deleteEntry(((Integer) objects[i]).intValue());
                    } else {
                        success = this.updateEntry((ExpenseEntry) objects[i]);
                    }
                } catch (PersistenceException e) {
                    success = false;
                    persistenceException = e;
                }

                totalSuccess &= success;

                if (!success) {
                    if (isAtomic) {
                        // in atomic mode, cancel the entire retrieval
                        errors.add(objects[i]);
                        break;
                    } else {
                        // in non atomic mode, add the unsuccessful transaction
                        errors.add(objects[i]);
                    }
                }
            }

            this.endTransaction(connection, isAtomic, totalSuccess, persistenceException);

            if (isAtomic && errors.isEmpty()) {
                return null;
            }

            if ((action == 0) || (action == 2)) {
                return (ExpenseEntry[]) errors.toArray(new ExpenseEntry[errors.size()]);
            }

            return (Integer[]) errors.toArray(new Integer[errors.size()]);
        } finally {
            try {
                if (isAtomic) {
                    // load the previous status of autoCommit
                    connection.setAutoCommit(preAutoCommit);
                }
            } catch (SQLException e) {
                throw new PersistenceException("Fail to access the value of autoCommit.", e);
            }
        }
    }

    /**
     * Begins the transaction. If it is in atomic module, the connection's autoCommit value will be set to false.
     *
     * @param connection the connection to the database.
     * @param isAtomic whether it is in atomic module.
     *
     * @return the previous value of connection's autoCommit.
     *
     * @throws PersistenceException wraps a persistence implementation specific exception (such as SQL exception).
     *
     * @since 1.1
     */
    private boolean beginTransaction(Connection connection, boolean isAtomic)
        throws PersistenceException {
        boolean preAutoCommit = true;

        try {
            if (connection.isClosed()) {
                throw new PersistenceException("The connection has already benn closed.");
            }

            if (isAtomic) {
                // save the previous status of autoCommit
                preAutoCommit = connection.getAutoCommit();

                // an JDBC transaction must be used in atomic mode to be able to rollback everything in case of failure.
                connection.setAutoCommit(false);
            }
        } catch (SQLException e) {
            throw new PersistenceException("Fail to access the value of autoCommit.", e);
        }

        return preAutoCommit;
    }

    /**
     * Ends the transaction. If it is in atomic module, should manually commit the transaction.
     *
     * @param connection the connection to the database.
     * @param isAtomic whether it is in atomic module.
     * @param shouldCommit whether it should commit the transaction.
     * @param persistenceException if it is not <code>null</code> and in atomic module, this exception should be thrown
     *        directly.
     *
     * @throws PersistenceException wraps a persistence implementation specific exception (such as SQL exception).
     *
     * @since 1.1
     */
    private void endTransaction(Connection connection, boolean isAtomic, boolean shouldCommit,
        PersistenceException persistenceException) throws PersistenceException {
        if (isAtomic) {
            try {
                if (shouldCommit) {
                    connection.commit();
                } else {
                    rollback(connection);
                }
            } catch (SQLException e) {
                rollback(connection);
            }

            if (persistenceException != null) {
                throw persistenceException;
            }
        }
    }

    /**
     * Rollback the transaction of the database.
     *
     * @param connection the connection of the database.
     *
     * @throws PersistenceException it fails to rollback the transaction.
     *
     * @since 1.1
     */
    private void rollback(Connection connection) throws PersistenceException {
        try {
            connection.rollback();
        } catch (SQLException e) {
            throw new PersistenceException("Can not rollback the transaction.", e);
        }
    }

    /**
     * <p>
     * Deletes a set of entries from the database with the given IDs from the persistence.
     * </p>
     *
     * <p>
     * If the deletion is atomic then it means that entire retrieval will fail if a single expense entry deletion
     * fails.
     * </p>
     *
     * <p>
     * If the deletion is non-atomic then it means each expense entry is deleted individually. If it fails, that won't
     * affect the others. A list with the failed ids is returned to the user (empty if no error occurs).
     * </p>
     *
     * <p>
     * Note: an JDBC transaction must be used in atomic mode to be able to rollback everything in case of failure.
     * </p>
     *
     * @param entryIds the ids of the entries to delete.
     * @param isAtomic whether the operation should be atomic or not.
     *
     * @return the entries that failed to be deleted in non atomic mode.
     *
     * @throws IllegalArgumentException if the array is <code>null</code> or empty.
     * @throws PersistenceException wraps a persistence implementation specific exception (such as SQL exception).
     *
     * @since 1.1
     */
    public int[] deleteEntries(int[] entryIds, boolean isAtomic) throws PersistenceException {
        // argument validation
        if (entryIds == null) {
            throw new IllegalArgumentException("entryIds should not be null.");
        }

        if (entryIds.length == 0) {
            throw new IllegalArgumentException("entryIds should not be empty.");
        }

        // change them to object collection to avoid code redundance
        Integer[] ids = new Integer[entryIds.length];

        for (int i = 0; i < entryIds.length; i++) {
            ids[i] = new Integer(entryIds[i]);
        }

        // do the transaction
        Integer[] returnIds = (Integer[]) this.doTransaction(ids, isAtomic, 1);

        // return the transaction result
        if (returnIds == null) {
            return null;
        }

        int[] ret = new int[returnIds.length];

        for (int i = 0; i < returnIds.length; i++) {
            ret[i] = returnIds[i].intValue();
        }

        return ret;
    }

    /**
     * <p>
     * Updates a set of entries in the database.
     * </p>
     *
     * <p>
     * If the update is atomic then it means that entire retrieval will fail if a single expense entry update fails.
     * </p>
     *
     * <p>
     * If the update is non-atomic then it means each expense entry is updated individually. If it fails, that won't
     * affect the others. A list with the failed entries is returned to the user (empty if no error occurs).
     * </p>
     *
     * <p>
     * Note: an JDBC transaction must be used in atomic mode to be able to rollback everything in case of failure.
     * </p>
     *
     * @param entries the ids of the entries to update.
     * @param isAtomic whether the operation should be atomic or not.
     *
     * @return the entries that failed to be updated in non atomic mode.
     *
     * @throws IllegalArgumentException if the array is <code>null</code>, empty or has <code>null</code> element.
     * @throws PersistenceException wraps a persistence implementation specific exception. (such as SQL exception)
     *
     * @since 1.1
     */
    public ExpenseEntry[] updateEntries(ExpenseEntry[] entries, boolean isAtomic)
        throws PersistenceException {
        // argument validation
        if (entries == null) {
            throw new IllegalArgumentException("entries should not be null.");
        }

        if (entries.length == 0) {
            throw new IllegalArgumentException("entries should not be empty.");
        }

        for (int i = 0; i < entries.length; i++) {
            if (entries[i] == null) {
                throw new IllegalArgumentException("entries should not contain a null element.");
            }
        }

        return (ExpenseEntry[]) this.doTransaction(entries, isAtomic, 2);
    }

    /**
     * <p>
     * Retrieves a set of entries with given ids from the database.
     * </p>
     *
     * <p>
     * If the retrieval is atomic then it means that entire retrieval will fail if a single expense entry retrieval
     * fails.
     * </p>
     *
     * <p>
     * If the retrieval is non-atomic then it means each expense entry is retrieved individually. If it fails that
     * won't affect the others. The potentially partial list of results will be returned. If any error occurs or if
     * the billable column of an entry is not 0 or 1 PersistenceException will be thrown in atomic mode but it is
     * ignored in non-atomic node and the next entry is processed.
     * </p>
     *
     * @param entryIds the ids of the entries to retrieve.
     * @param isAtomic whether the operation should be atomic or not.
     *
     * @return the entries that were retrieved in both modes.
     *
     * @throws IllegalArgumentException if the array is <code>null</code> or empty.
     * @throws PersistenceException wraps a persistence implementation specific exception (such as SQL exception).
     *
     * @since 1.1
     */
    public ExpenseEntry[] retrieveEntries(int[] entryIds, boolean isAtomic)
        throws PersistenceException {
        // argument validation
        if (entryIds == null) {
            throw new IllegalArgumentException("entryIds should not be null.");
        }

        if (entryIds.length == 0) {
            throw new IllegalArgumentException("entryIds should not be empty.");
        }

        // If the exception is related to acquiring an SQL connection or something like that, it is obvious that
        // all entries will fail so the exception should be thrown.
        createConnection(connectionProducerName);

        try {
            if (connection.isClosed()) {
                throw new PersistenceException("The connection is already closed.");
            }
        } catch (SQLException e) {
            throw new PersistenceException("Exceptions occur in the database operation.", e);
        }

        List ret = new ArrayList();

        for (int i = 0; i < entryIds.length; i++) {
            ExpenseEntry expenseEntry = null;

            try {
                // try to get the expenseEntry
                expenseEntry = this.retrieveEntry(entryIds[i]);
            } catch (PersistenceException e) {
                if (isAtomic) {
                    throw e;
                }

                continue;
            }

            if (expenseEntry != null) {
                ret.add(expenseEntry);
            } else if (isAtomic) {
                throw new PersistenceException("Can not find the entry with the id of " + entryIds[i] + ".");
            }
        }

        return (ExpenseEntry[]) ret.toArray(new ExpenseEntry[ret.size()]);
    }

    /**
     * <p>
     * Performs a search for expense entries matching a given criteria. The criteria is abstracted using the <code>
     * Criteria</code> interface. The <code>Criteria</code> implementations cover the basic SQL filtering abilities
     * (=, like, between, or, and, not). The result of the search is an array with the matched expense entries. It is
     * empty if no matches found (but it can't be <code>null</code> or contain <code>null</code>) elements.
     * </p>
     *
     * @param criteria the criteria to be used in the search.
     *
     * @return the results of the search (can be empty if no matches found).
     *
     * @throws IllegalArgumentException if the argument is <code>null</code>
     * @throws PersistenceException wraps a persistence implementation specific exception (such as SQL exception).
     *
     * @since 1.1
     */
    public ExpenseEntry[] searchEntries(Criteria criteria) throws PersistenceException {
        // argument validation
        if (criteria == null) {
            throw new IllegalArgumentException("criteria should not be null.");
        }

        List ret = new ArrayList();

        createConnection(connectionProducerName);

        PreparedStatement statement = null;
        ResultSet resultSet = null;
        ExpenseEntry entry;

        try {
            statement = connection.prepareStatement(RETRIEVE_ALL_ENTRY_SQL + " where " + criteria.getWhereClause());

            // Set parameter
            Object[] parameters = criteria.getParameters();

            for (int i = 0; i < parameters.length; ++i) {
                if (parameters[i] instanceof java.util.Date) {
                    parameters[i] = new Date(((java.util.Date) parameters[i]).getTime());
                }
                statement.setObject(i + 1, parameters[i]);
            }

            // Execute
            resultSet = statement.executeQuery();

            // For each record, create an expense entry status instance
            while ((entry = createExpenseEntry(resultSet)) != null) {
                ret.add(entry);
            }

            // retrieve the reject reasons
            statement = connection.prepareStatement(RETRIEVE_REJECT_REASON_SQL);

            for (Iterator iter = ret.iterator(); iter.hasNext();) {
                entry = (ExpenseEntry) iter.next();
                retrieveRejectReasons(statement, entry);
            }
        } catch (SQLException e) {
            throw new PersistenceException("Accessing database fails.", e);
        } finally {
            PersistenceHelper.close(resultSet);
            PersistenceHelper.close(statement);
        }

        return (ExpenseEntry[]) ret.toArray(new ExpenseEntry[ret.size()]);
    }

    /**
     * <p>
     * Creates a database connection using the DB connection factory and connection producer name if the current
     * connection is <code>null</code>. The connection field is set. If the current connection already exists, it does
     * nothing.
     * </p>
     *
     * @param connectionProducerName the DB connection producer name.
     *
     * @throws PersistenceException if the connection producer name is <code>null</code> or empty string; or the
     *         connection producer name does not exist in DB connection factory; or database connection cannot be
     *         created.
     */
    private void createConnection(String connectionProducerName) throws PersistenceException {
        if (connection != null) {
            return;
        }

        try {
            connection = factory.createConnection(connectionProducerName);
        } catch (DBConnectionException e) {
            throw new PersistenceException("Database connection cannot be created.", e);
        } catch (IllegalArgumentException e) {
            throw new PersistenceException("The connection producer name cannot be empty string.", e);
        } catch (NullPointerException e) {
            throw new PersistenceException("The connection producer name cannot be null.", e);
        }
    }

    /**
     * <p>
     * Creates an expense entry status instance from the given result set. The next record in the result set is used to
     * create the instance. If there is no more record in the result set, <code>null</code> is returned. The entry
     * type and entry status instances are also created from the result set.
     * </p>
     *
     * @param resultSet the result set used to create expense entry status instance.
     *
     * @return a new <code>ExpenseEntryStatus</code> instance created from the next record in the result set; or
     *         <code>null</code> if there is no more record.
     *
     * @throws SQLException if error occurs when accessing the result set.
     * @throws PersistenceException if the flag for billable is neither 0 nor 1; or the column value is
     *         <code>null</code>, empty string or invalid value.
     */
    private ExpenseEntry createExpenseEntry(ResultSet resultSet) throws SQLException, PersistenceException {
        if (!resultSet.next()) {
            return null;
        }

        ExpenseEntry entry;

        try {
            // Set properties
            entry = new ExpenseEntry(resultSet.getInt(ID_COLUMN));
            entry.setDescription(resultSet.getString(DESCRIPTION_COLUMN));
            entry.setCreationDate(resultSet.getDate(CREATION_DATE_COLUMN));
            entry.setCreationUser(resultSet.getString(CREATION_USER_COLUMN));
            entry.setModificationDate(resultSet.getDate(MODIFICATION_DATE_COLUMN));
            entry.setModificationUser(resultSet.getString(MODIFICATION_USER_COLUMN));
            entry.setAmount(resultSet.getBigDecimal(AMOUNT_COLUMN));
            entry.setDate(resultSet.getDate(DATE_COLUMN));

            // Set billable
            switch (resultSet.getShort(BILLABLE_COLUMN)) {
            case 0:
                entry.setBillable(false);

                break;

            case 1:
                entry.setBillable(true);

                break;

            default:
                throw new PersistenceException("Billable column is neither 0 nor 1.");
            }

            // Create type and status
            ExpenseEntryStatus status = new ExpenseEntryStatus(resultSet.getInt(STATUS_ID_COLUMN));
            ExpenseEntryType type = new ExpenseEntryType(resultSet.getInt(TYPE_ID_COLUMN));

            status.setDescription(resultSet.getString(STATUS_DESCRIPTION_COLUMN));
            status.setCreationDate(resultSet.getDate(STATUS_CREATION_DATE_COLUMN));
            status.setCreationUser(resultSet.getString(STATUS_CREATION_USER_COLUMN));
            status.setModificationDate(resultSet.getDate(STATUS_MODIFICATION_DATE_COLUMN));
            status.setModificationUser(resultSet.getString(STATUS_MODIFICATION_USER_COLUMN));

            type.setDescription(resultSet.getString(TYPE_DESCRIPTION_COLUMN));
            type.setCreationDate(resultSet.getDate(TYPE_CREATION_DATE_COLUMN));
            type.setCreationUser(resultSet.getString(TYPE_CREATION_USER_COLUMN));
            type.setModificationDate(resultSet.getDate(TYPE_MODIFICATION_DATE_COLUMN));
            type.setModificationUser(resultSet.getString(TYPE_MODIFICATION_USER_COLUMN));

            // Set type and status
            entry.setExpenseType(type);
            entry.setStatus(status);
        } catch (NullPointerException e) {
            throw new PersistenceException("Column value cannot be null.", e);
        } catch (IllegalArgumentException e) {
            throw new PersistenceException("Column value cannot be empty string.", e);
        }

        return entry;
    }
}
