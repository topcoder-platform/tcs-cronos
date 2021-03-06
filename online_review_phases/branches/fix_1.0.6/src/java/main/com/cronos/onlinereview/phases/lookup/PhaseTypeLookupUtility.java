/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.phases.lookup;

import java.sql.Connection;
import java.sql.SQLException;

import java.util.HashMap;
import java.util.Map;


/**
 * <p>This class provides the function to get lookup id from a lookup name of &quot;phase_type_lu&quot; table.
 * Since lookup id/value pairs do not change in the database per installation, this classe caches the id/value pairs
 * to minimize queries to the database. This class is used in various methods of PhaseHandler implementations.</p>
 * <p>Thread safety: This class is thread safe because its static method is synchronized.</p>
 *
 * @author tuenm, bose_java
 * @version 1.0
 */
public class PhaseTypeLookupUtility {
    /**
     * <p>Represents the map to store cached value/id pairs from the database. Key is &quot;lookup value&quot;
     * of type String and the value is the lookup id of type Long. It is initialized to an empty map and will never be
     * null. New entries are added to this map when lookUpId() method is called. Cached ids are used to minimize
     * database queries.</p>
     */
    private static Map cachedPairs = new HashMap();

    /** constant representing the sql query string to get the id for a given lookup value. */
    private static final String SQL_QUERY = "SELECT phase_type_id FROM phase_type_lu WHERE name = ?";

    /**
     * Private constructor to prevent class initialization.
     *
     */
    private PhaseTypeLookupUtility() {
        // do nothing.
    }

    /**
     * Gets the &quot;phase_type_id&quot; for the given value for &quot;name&quot; from
     * &quot;phase_type_lu&quot; table. This method uses caching mechanism for id/value pairs to minimize queries to
     * the database. Hence if the lookup id is present in the cache, it is returned without hitting the database. It
     * is synchronized to ensure thread safety.
     *
     * @param connection The connection used to access the persistence.
     * @param value The value for &quot;name&quot; column.
     *
     * @return The corresponding &quot;phase_type_id&quot;.
     *
     * @throws IllegalArgumentException if any input is null or value is empty.
     * @throws SQLException if there is any problem acessing the database, or if value is not mapped to any record.
     */
    public static synchronized long lookUpId(Connection connection, String value)
        throws SQLException {
        return LookupHelper.lookUpId(cachedPairs, value, connection, SQL_QUERY);
    }
}
