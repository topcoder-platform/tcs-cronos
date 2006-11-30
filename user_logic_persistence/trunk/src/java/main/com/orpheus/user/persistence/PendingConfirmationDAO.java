/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.orpheus.user.persistence;

import com.orpheus.user.persistence.ejb.ConfirmationMessageDTO;
import com.orpheus.user.persistence.ejb.PendingConfirmationBean;
import com.orpheus.user.persistence.impl.ConfirmationMessageTranslator;

/**
 * <p>
 * The interface for data access objects (DAO) that are responsible for the
 * persistence of pending confirmation messages. A
 * <code>PendingConfirmationDAO</code> instance is created by the
 * {@link DAOFactory} class and is used by the {@link PendingConfirmationBean}
 * session bean. It does not handle <code>ConfirmationMessage</code> objects
 * directly however. Rather, the {@link ConfirmationMessageTranslator} is used
 * by a client to first convert the <code>ConfirmationMessage</code> object to
 * a serializable {@link ConfirmationMessageDTO} transfer object, which is then
 * passed to it.
 * </p>
 * <p>
 * This interface defines operation to store, retrieve and delete pending
 * confirmation messages, as well as to check if a message is in the persistent
 * store. IT thus supports all methods in the
 * {@link OrpheusPendingConfirmationStorage} client.
 * </p>
 * <p>
 * <b>Thread-safety:</b><br>
 * it is recommended that implementation classes be made thread-safe. However,
 * this is not required, as the component will still use them in a thread-safe
 * manner.
 * </p>
 *
 * @author argolite, mpaulse
 * @version 1.0
 * @see DAOFactory
 * @see PendingConfirmationBean
 * @see ConfirmationMessageDTO
 */
public interface PendingConfirmationDAO {

    /**
     * <p>
     * Stores the given confirmation message in the persistent store. If the
     * persistent store already contains a confirmation message with the same
     * address as that of the given message, a
     * <code>DuplicateEntryException</code> is thrown.
     * </p>
     *
     * @param message the data transfer object (DTO) representing the
     *        confirmation message to store
     * @throws IllegalArgumentException if the confirmation message is
     *         <code>null</code>
     * @throws DuplicateEntryException if the persistent store contains a
     *         confirmation message with the same address as that of the given
     *         message
     * @throws PersistenceException if storing the confirmation message in the
     *         persistent store fails
     */
    public void store(ConfirmationMessageDTO message) throws PersistenceException;

    /**
     * <p>
     * Checks if a confirmation message with the specified address exists in the
     * persistent store. It it does, a data transfer object (DTO) representing
     * the confirmation message is returned. Otherwise, <code>null</code> is
     * returned.
     * </p>
     *
     * @param address the address of the confirmation message to check for
     * @return a <code>ConfirmationMessageDTO</code> representing the
     *         confirmation message with the specified address, or
     *         <code>null</code> if no such confirmation message exists in the
     *         persistent store
     * @throws IllegalArgumentException if the specified address is
     *         <code>null</code> or a blank string
     * @throws PersistenceException if checking if the confirmation message is
     *         in the persistent store fails
     */
    public ConfirmationMessageDTO contains(String address) throws PersistenceException;

    /**
     * <p>
     * Retrieves the confirmation message with the specified address from the
     * persistent store. If no such confirmation message could be found, an
     * <code>EntryNotFoundException</code> is thrown.
     * </p>
     *
     * @param address the address of the confirmation message to retrieve
     * @return a <code>ConfirmationMessageDTO</code> representing the
     *         confirmation message with the specified address
     * @throws IllegalArgumentException if the specified address is
     *         <code>null</code> or a blank string
     * @throws EntryNotFoundException if no confirmation message with the
     *         specified address exists in the persistent store
     * @throws PersistenceException if retrieving the confirmation message from
     *         the persistent store fails
     * @see #getMessages()
     */
    public ConfirmationMessageDTO retrieve(String address) throws PersistenceException;

    /**
     * <p>
     * Deletes the confirmation message with the specified address from the
     * persistent store. If no such confirmation message could be found, an
     * <code>EntryNotFoundException</code> is thrown.
     * </p>
     *
     * @param address the address of the confirmation message to delete
     * @throws IllegalArgumentException if the specified address is
     *         <code>null</code> or a blank string
     * @throws EntryNotFoundException if no confirmation message with the
     *         specified address exists in the persistent store
     * @throws PersistenceException if deleting the confirmation message from
     *         the persistent store fails
     */
    public void delete(String address) throws PersistenceException;

    /**
     * <p>
     * Retrieves all the confirmation messages from the persistent store as an
     * array. If no confirmation messages exist in the persistent store, an
     * empty array is returned.
     * </p>
     *
     * @return a <code>ConfirmationMessageDTO[]</code> array containing all
     *         the confirmation messages in the persistent store, or an empty
     *         array if there are no confirmation messages
     * @throws PersistenceException if retrieving the all confirmation messages
     *         from the persistent store fails
     * @see #retrieve(String)
     */
    public ConfirmationMessageDTO[] getMessages() throws PersistenceException;

}
