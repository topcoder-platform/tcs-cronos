/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.orpheus.game.persistence;


/**
 * Extends PersistenceException. This is a specific persistence exception when retrieving,
 * updating, or deleting a record with a primary id that does not exist.
 * The DAO and the associated EJB and client helper methods use it.
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
public class EntryNotFoundException
    extends com.orpheus.game.persistence.PersistenceException {
    /**
     * <p>
     * Represents the unknown identifier. It is set in the constructor, will not be null.
     * </p>
     *
     */
    private final Object identifier;

    /**
     * <p>Creates new exception with error message and the unknown identifier.</p>
     *
     * @param msg error message
     * @param identifier the unknown identifier
     */
    public EntryNotFoundException(String msg, Object identifier) {
        super(msg);
        this.identifier = identifier;
    }

    /**
     * Gets the unknown identifier.
     *
     * @return the unknown identifier
     */
    public Object getIdentifier() {
        return identifier;
    }
}
