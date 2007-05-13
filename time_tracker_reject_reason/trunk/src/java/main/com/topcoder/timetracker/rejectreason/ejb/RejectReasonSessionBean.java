/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.timetracker.rejectreason.ejb;

import com.topcoder.search.builder.filter.Filter;

import com.topcoder.timetracker.rejectreason.RejectReason;
import com.topcoder.timetracker.rejectreason.RejectReasonDAO;
import com.topcoder.timetracker.rejectreason.RejectReasonDAOException;

import com.topcoder.util.errorhandling.BaseException;

import javax.ejb.SessionBean;
import javax.ejb.SessionContext;

/**
 * <p>
 * This is a Stateless SessionBean that is used to provided business services to manage RejectReasons within the Time
 * Tracker Application. It contains all methods in RejectReasonDAO interface, and delegates to an instance of
 * RejectReasonDAO.
 * </p>
 *
 * <p>
 * The instance of RejectReasonDAO to use is stored in the class variable DAO, and are warned to be access only via
 * protected method getDAO (in this method the DAO is checked, if it is null then create a new one).
 * </p>
 *
 * <p>
 * <b>Thread Safety:</b> The RejectReasonDAO interface implementations are required to be thread-safe, and so this
 * stateless session bean is thread-safe also. And this bean is a Container Managed Transaction session bean with
 * trans-attribute as &quot;Required&quot; in the deploy descriptor (see the example deploy descriptor of this ejb).
 * </p>
 *
 * @author wangqing, TCSDEVELOPER
 * @version 3.2
 */
public class RejectReasonSessionBean implements RejectReasonDAO, SessionBean {

	/**
	 * Automatically generated unique ID for use with serialization.
	 */
	private static final long serialVersionUID = -1543898544928131371L;

	/**
     * <p>
     * This is the instance of RejectReasonDAO that this session bean delegates all the work to. It is a private class
     * variable that should not be access directly even within, instead use getDAO method get it. That's because we
     * need the getDAO method to make sure the RejectReasonDAO is properly initialized no matter under what situation
     * (the session bean can be passivate and then activate, and can be created newly) there should be a and only one
     * RjectReasonDAO the session bean can delegate its tasks to.
     * </p>
     */
    private static transient RejectReasonDAO dao = null;

    /**
     * <p>
     * This is the instance of SessionContext that was provided by the EJB container. It is stored and made available
     * to subclasses.
     * </p>
     */
    private SessionContext sessionContext = null;

    /**
     * <p>
     * Default constructor.
     * </p>
     */
    public RejectReasonSessionBean() {
        // Empty.
    }

    /**
     * <p>
     * This method is invoked by the container when the instance is in the process of being added by the container.
     * </p>
     *
     * <p>
     * <b>Note:</b> For current implementation is empty.
     * </p>
     */
    public void ejbCreate() {
        // Empty.
    }

    /**
     * <p>
     * This method is invoked by the container when it wants to activate the instance.
     * </p>
     *
     * <p>
     * <b>Note:</b> For current implementation is empty.
     * </p>
     */
    public void ejbActivate() {
        // Empty.
    }

    /**
     * <p>
     * This method is invoked by the container when it wants to passivate the instance.
     * </p>
     *
     * <p>
     * <b>Note:</b> For current implementation is empty.
     * </p>
     */
    public void ejbPassivate() {
        // Empty.
    }

    /**
     * <p>
     * This method is invoked by the container when it wants to Remove the instance.
     * </p>
     *
     * <p>
     * <b>Note:</b> For current implementation is empty.
     * </p>
     */
    public void ejbRemove() {
        // Empty.
    }

    /**
     * <p>
     * Sets the SessionContext to use for this session. This method is included to comply with the SessionBean
     * interface.
     * </p>
     *
     * @param sessionContext the session context to set.
     */
    public void setSessionContext(SessionContext sessionContext) {
        this.sessionContext = sessionContext;
    }

    /**
     * <p>
     * Creates a data store entry for the given Reject Reason. An id is automatically generated by the DAO and assigned
     * to the reason. The RejectReason is also considered to have been created by the specified username. If the
     * argument isAudit is true, insert the corresponding audit record in data store.
     * </p>
     *
     * @param rejectReason the RejectReason to be persistent in the data store
     * @param username the username of the user responsible for creating the RejectReason entry within the data store.
     * @param isAudit indicates audit or not.
     *
     * @return the same rejectReason Object, with an assigned id, creationDate, modificationDate, creationUser and
     *         modificationUser assigned appropriately (none null).
     *
     * @throws IllegalArgumentEException if the rejectReason or username is null, or if username is an empty String, or
     *         if the company id of the reject reason is not set.
     * @throws RejectReasonDAOException if a problem occurs while accessing the data store.
     * @throws RejectReasonDAOConfigurationException if any required environment variables can't be found.
     */
    public RejectReason createRejectReason(RejectReason rejectReason, String username, boolean isAudit)
        throws RejectReasonDAOException {
        try {
            return getDAO().createRejectReason(rejectReason, username, isAudit);
        } catch (IllegalArgumentException e) {
            sessionContext.setRollbackOnly();
            throw e;
        } catch (RejectReasonDAOException e) {
            sessionContext.setRollbackOnly();
            throw e;
        }
    }

    /**
     * <p>
     * Retrieves a RejectReason from the data store with the provided id. If no RejectReason with that id exists, then
     * a null is returned.
     * </p>
     *
     * @param id the id of the RejectReason to retrieve from the data store.
     *
     * @return the retrieved RejectReason object or null if there is no corresponding RejectReason.
     *
     * @throws IllegalArgumentException if id is less than or equals to zero.
     * @throws RejectReasonDAOException if a problem occurs while accessing the data store.
     * @throws RejectReasonDAOConfigurationException if any required environment variables can't be found.
     */
    public RejectReason retrieveRejectReason(long id) throws RejectReasonDAOException {
        try {
            return getDAO().retrieveRejectReason(id);
        } catch (IllegalArgumentException e) {
            sessionContext.setRollbackOnly();
            throw e;
        } catch (RejectReasonDAOException e) {
            sessionContext.setRollbackOnly();
            throw e;
        }
    }

    /**
     * <p>
     * Updates the given RejectReason in the data store. The RejectReason is considered to have been modified by the
     * specified username. If the argument isAudit is true, insert the corresponding audit record in data store.
     * </p>
     *
     * @param rejectReason the RejectReason entity to modify.
     * @param username the username of the user responsible for performing the update.
     * @param isAudit indicates audit or not.
     *
     * @throws IllegalArgumentEException if the rejectReason or username is null, or if username is an empty String.
     * @throws RejectReasonDAOException if a problem occurs while accessing the data store.
     * @throws RejectReasonNotFoundException if the RejectReason to update was not found in the data store.
     * @throws RejectReasonDAOConfigurationException if any required environment variables can't be found.
     */
    public void updateRejectReason(RejectReason rejectReason, String username, boolean isAudit)
        throws RejectReasonDAOException {
        try {
            getDAO().updateRejectReason(rejectReason, username, isAudit);
        } catch (IllegalArgumentException e) {
            sessionContext.setRollbackOnly();
            throw e;
        } catch (RejectReasonDAOException e) {
            sessionContext.setRollbackOnly();
            throw e;
        }
    }

    /**
     * <p>
     * Removes the specified RejectReason from the data store. If the argument isAudit is true, insert the
     * corresponding audit record in data store.
     * </p>
     *
     * @param rejectReason the rejectReason to delete.
     * @param username the username of the user responsible for performing the deletion.
     * @param isAudit indicates audit or not.
     *
     * @throws IllegalArgumentException if the rejectReason is null.
     * @throws RejectReasonDAOException if a problem occurs while accessing the data store.
     * @throws RejectReasonNotFoundException if the RejectReason to delete was not found in the data store.
     * @throws RejectReasonDAOConfigurationException if any required environment variables can't be found.
     */
    public void deleteRejectReason(RejectReason rejectReason, String username, boolean isAudit)
        throws RejectReasonDAOException {
        try {
            getDAO().deleteRejectReason(rejectReason, username, isAudit);
        } catch (IllegalArgumentException e) {
            sessionContext.setRollbackOnly();
            throw e;
        } catch (RejectReasonDAOException e) {
            sessionContext.setRollbackOnly();
            throw e;
        }
    }

    /**
     * <p>
     * Enumerates all the RejectReasons that are present within the data store. If no record found an empty array will
     * be return.
     * </p>
     *
     * @return a list of all the RejectReasons within the data store.
     *
     * @throws RejectReasonDAOException if a problem occurs while accessing the data store.
     * @throws RejectReasonDAOConfigurationException if any required environment variables can't be found.
     */
    public RejectReason[] listRejectReasons() throws RejectReasonDAOException {
        try {
            return getDAO().listRejectReasons();
        } catch (RejectReasonDAOException e) {
            sessionContext.setRollbackOnly();
            throw e;
        }
    }

    /**
     * <p>
     * Returns a list of all the RejectReasons within the datastore that satisfy the filters that are provided. If no
     * record found an empty array will be return. The filters are defined using classes from the Search Builder
     * component.
     * </p>
     *
     * @param filter the filter that is used as criterion to facilitate the search..
     *
     * @return a list of RejectReasons that satisfy the search criterion.
     *
     * @throws IllegalArgumentException if the filter is null.
     * @throws RejectReasonDAOException if a problem occurs while accessing the data store.
     * @throws RejectReasonDAOConfigurationException if any required environment variables can't be found.
     */
    public RejectReason[] searchRejectReasons(Filter filter)
        throws RejectReasonDAOException {
        try {
            return getDAO().searchRejectReasons(filter);
        } catch (IllegalArgumentException e) {
            sessionContext.setRollbackOnly();
            throw e;
        } catch (RejectReasonDAOException e) {
            sessionContext.setRollbackOnly();
            throw e;
        }
    }

    /**
     * <p>
     * Gets the SessionContext used by this session.
     * </p>
     *
     * @return the session context used by this session.
     */
    protected SessionContext getSessionContext() {
        return sessionContext;
    }

    /**
     * <p>
     * Gets the RejectReasonDAO this session bean delegates all the work to.
     * </p>
     *
     * @return the RejectReasonDAO this session bean delegates all the work to.
     *
     * @throws RejectReasonDAOConfigurationException if any required environment variables can't be found or if they
     *         are invalid.
     */
    protected RejectReasonDAO getDAO() throws RejectReasonDAOConfigurationException {
        if (dao == null) {
            // Create new RejectReasonDAO object.
            try {
                dao = (RejectReasonDAO) DAOFactory.getDAO(false);
            } catch (BaseException e) {
                throw new RejectReasonDAOConfigurationException(e.getMessage(), e.getCause());
            }
        }

        return dao;
    }
}
