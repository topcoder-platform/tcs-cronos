/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.orpheus.user.persistence;

import java.util.Map;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.rmi.PortableRemoteObject;

import com.orpheus.user.persistence.ejb.UserProfileDTO;
import com.orpheus.user.persistence.ejb.UserProfileHomeLocal;
import com.orpheus.user.persistence.ejb.UserProfileLocal;
import com.topcoder.user.profile.manager.ProfileManagerConfigurationException;

/**
 * <p>
 * An implementation of the <code>UserProfilePersistence</code> interface
 * which provides persistent storage for user profiles. It implements the
 * abstract <code>ejbXXX()</code> methods defined in the
 * {@link OrpheusUserProfilePersistence} base class in order to interact with
 * the local {@link UserProfileLocal} EJB session bean.
 * </p>
 * <p>
 * This class should be used when the application is running in the same
 * execution environment (JVM) as the session bean.
 * </p>
 * <p>
 * <b>Logging Activity:</b><br>
 * If any errors occur while performing the persistence operations, they will be
 * logged using the ERROR log level.
 * </p>
 * <p>
 * <b>Configuration:</b><br>
 * In order to use this class, it needs to be configured in the configuration
 * namespace provided to the {@link #LocalOrpheusUserProfilePersistence(String)}
 * constructor. Besides the configuration properties described in the
 * {@link OrpheusUserProfilePersistence} class documentation, the
 * "jndiEjbRefence" configuration property also needs to be specified. This
 * property is described in the table below.
 * </p>
 * <table border="1"> <thead>
 * <tr>
 * <td><b>Property</b></td>
 * <td><b>Description</b></td>
 * <td><b>Required</b></td>
 * </tr>
 * </thead> <tbody>
 * <tr>
 * <td>jndiEjbReference</td>
 * <td>The JNDI reference to the local {@link UserProfileLocal} EJB session
 * bean</td>
 * <td>Yes</td>
 * </tr>
 * </tbody> </table>
 * <p>
 * <b>Thread-safety:</b><br>
 * This class is immutable and thread-safe.
 * </p>
 *
 * @author argolite, mpaulse
 * @version 1.0
 * @see UserProfileLocal
 */
public class LocalOrpheusUserProfilePersistence extends OrpheusUserProfilePersistence {

    /**
     * <p>
     * The local EJB session bean that to which all persistent operations will
     * be delegated.
     * </p>
     * <p>
     * This field is set in the constructor, and will never be changed
     * afterwards. It cannot be null.
     * </p>
     */
    private final UserProfileLocal userProfileEJB;

    /**
     * <p>
     * Creates a new <code>LocalOrpheusUserProfilePersistence</code> instance
     * using the specified configuration namespace.
     * </p>
     * <p>
     * The configuration namespace is used to load the {@link ObjectTranslator},
     * which is used to convert <code>UserProfile</code> objects to
     * serializable {@link UserProfileDTO} instances that are transported to the
     * {@link UserProfileLocal} EJB session bean, and vice-versa. It is also
     * used to load the <code>Cache</code>, which is used to cache user
     * profiles within this class, the name of the logger which will be used to
     * log persistence errors, and the JNDI reference to the EJB session bean.
     * If an error occurs reading the configuration information, while
     * instantiating the <code>ObjectTranslator</code> or <code>Cache</code>
     * objects or while looking up the <code>UserProfileLocal</code> EJB
     * session bean, a <code>ProfileManagerConfigurationException</code> is
     * thrown. Please consult the class documentation for more information on
     * the configuration parameters.
     * </p>
     *
     * @param namespace the configuration namespace from which to read
     *        configuration information
     * @throws IllegalArgumentException if the configuration namespace is
     *         <code>null</code> or a blank string
     * @throws ProfileManagerConfigurationException if an error occurs reading
     *         from the configuration namespace, while instantiating the
     *         <code>ObjectTranslator</code> or <code>Cache</code> objects,
     *         or while looking up the <code>UserProfileLocal</code> EJB
     *         session bean
     */
    public LocalOrpheusUserProfilePersistence(String namespace) throws ProfileManagerConfigurationException {
        super(namespace);

        // Get the EJB JNDI reference.
        String jndiRef = null;
        try {
            jndiRef = UserLogicPersistenceHelper.getConfigProperty(namespace, "jndiEjbReference", true);
        } catch (ObjectInstantiationException e) {
            throw new ProfileManagerConfigurationException(e.getMessage(), e.getCause());
        }

        // Lookup the UserProfileHomeLocal EJB interface.
        UserProfileHomeLocal home = null;
        try {
            Context context = new InitialContext();
            home = (UserProfileHomeLocal) PortableRemoteObject.narrow(context.lookup(jndiRef),
                                                                      UserProfileHomeLocal.class);
        } catch (NamingException e) {
            throw new ProfileManagerConfigurationException("Unable to lookup UserProfileHomeLocal session bean "
                                                           + "with JNDI name, " + jndiRef, e);
        }

        // Create the UserProfileLocal EJB interface.
        try {
            userProfileEJB = home.create();
        } catch (CreateException e) {
            throw new ProfileManagerConfigurationException("Could not create UserProfileLocal object", e);
        }
    }

    /**
     * <p>
     * Inserts the given user profile into the persistent store. If the
     * persistent store already contains a user profile with the same identifier
     * as that of the given user profile, a <code>DuplicateEntryException</code>
     * is thrown.
     * </p>
     *
     * @param profile a data transfer object (DTO) representing the user profile
     *        to insert
     * @throws IllegalArgumentException if the user profile DTO is
     *         <code>null</code> or contains an invalid key to object mapping
     * @throws DuplicateEntryException if the persistent store contains a user
     *         profile with the same identifier as that of the given user
     *         profile
     * @throws PersistenceException if inserting the user profile into the
     *         persistent store fails
     */
    protected void ejbInsertProfile(UserProfileDTO profile) throws PersistenceException {
        userProfileEJB.insertProfile(profile);
    }

    /**
     * <p>
     * Updates the given user profile in the persistent store. If the user
     * profile does not exist in the persistent store, an
     * <code>EntryNotFoundException</code> is thrown.
     * </p>
     *
     * @param profile a data transfer object (DTO) representing the user profile
     *        to update
     * @throws IllegalArgumentException if the user profile DTO is
     *         <code>null</code> or contains an invalid key to object mapping
     * @throws EntryNotFoundException if the user profile does not exist in the
     *         persistent store
     * @throws PersistenceException if updating the user profile in the
     *         persistent store fails
     */
    protected void ejbUpdateProfile(UserProfileDTO profile) throws PersistenceException {
        userProfileEJB.updateProfile(profile);
    }

    /**
     * <p>
     * Retrieves the user profile with the specified identifier from the
     * persistent store. If no such user profile could be found, an
     * <code>EntryNotFoundException</code> is thrown.
     * </p>
     *
     * @param id the identifier of the user profile to retrieve
     * @return a <code>UserProfileDTO</code> representing the user profile
     *         with the specified identifier
     * @throws EntryNotFoundException if no user profile with the specified
     *         identifier exists in the persistent store
     * @throws PersistenceException if retrieving the user profile from the
     *         persistent store fails
     */
    protected UserProfileDTO ejbRetrieveProfile(long id) throws PersistenceException {
        return userProfileEJB.retrieveProfile(id);
    }

    /**
     * <p>
     * Deletes the user profile with the specified identifier from the
     * persistent store. If no such user profile could be found, an
     * <code>EntryNotFoundException</code> is thrown.
     * </p>
     *
     * @param id the identifier of the user profile to delete
     * @throws EntryNotFoundException if no user profile with the specified
     *         identifier exists in the persistent store
     * @throws PersistenceException if deleting the user profile from the
     *         persistent store fails
     */
    protected void ejbDeleteProfile(long id) throws PersistenceException {
        userProfileEJB.deleteProfile(id);
    }

    /**
     * <p>
     * Retrieves all the user profiles from the persistent store that match the
     * given criteria, and returns them in an array. A user profile must match
     * all the criteria for it to be included in the returned array. If no user
     * profiles match the criteria, an empty array is returned. If the given
     * criteria map is empty, all the user profiles are retrieved.
     * </p>
     *
     * @param criteria the criteria to use when searching for user profiles in
     *        the persistent store to retrieve
     * @return a <code>UserProfile[]</code> array containing all the user
     *         profiles in the persistent store that match the given criteria;
     *         an empty array if there are no user profiles in the persistent
     *         store that match the given criteria; or, if the criteria map is
     *         empty, a <code>UserProfile[]</code> array containing all the
     *         user profiles in the persistent store
     * @throws IllegalArgumentException if the criteria map is <code>null</code>,
     *         if it contains keys that are <code>null</code>, non-<code>String</code>
     *         instances or blank strings, of if the keys are not equal to one
     *         of the property constants defined in the {@link UserConstants}
     *         interface
     * @throws PersistenceException if retrieving the user profile from the
     *         persistent store that match the given criteria fails
     */
    protected UserProfileDTO[] ejbFindProfiles(Map criteria) throws PersistenceException {
        try {
            return userProfileEJB.findProfiles(criteria);
        } catch (EJBException e) {
            // An EJBException means that some RuntimeException has occurred at
            // the other end, most likely an IllegalArgumentException. If it is
            // not an IllegalArgumentException, re-throw the EJBException wrapped
            // within a PersistenceException.
            if (e.getCause() instanceof IllegalArgumentException) {
                throw (IllegalArgumentException) e.getCause();
            }
            throw new PersistenceException("Failed to find user profiles", e);
        }
    }

    /**
     * <p>
     * Retrieves all the user profiles from the persistent store as an array. If
     * no user profiles exist in the persistent store, an empty array is
     * returned.
     * </p>
     *
     * @return a <code>UserProfileDTO[]</code> array containing all the user
     *         profiles in the persistent store, or an empty array if there are
     *         no user profiles
     * @throws PersistenceException if retrieving all the user profiles from the
     *         persistent store fails
     */
    protected UserProfileDTO[] ejbRetrieveAllProfiles() throws PersistenceException {
        return userProfileEJB.retrieveAllProfiles();
    }

}
