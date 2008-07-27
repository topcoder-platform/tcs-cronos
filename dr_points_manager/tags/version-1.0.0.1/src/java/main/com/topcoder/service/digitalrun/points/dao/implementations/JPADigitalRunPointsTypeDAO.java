/*
 * Copyright (C) 2008 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.service.digitalrun.points.dao.implementations;

import java.util.List;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.topcoder.service.digitalrun.entity.DigitalRunPointsType;
import com.topcoder.service.digitalrun.points.DigitalRunPointsManagerPersistenceException;
import com.topcoder.service.digitalrun.points.DigitalRunPointsTypeDAO;
import com.topcoder.service.digitalrun.points.EntityNotFoundException;
import com.topcoder.service.digitalrun.points.Helper;

/**
 * <p>
 * This class implements DigitalRunPointsTypeDAO interface. It also extends the AbstractDAO class.
 * This class manages DigitalRunPointsType entities in a JPA persistence (currently the JPA
 * persistence will use Hibernate as a provider but any provider can be used). Each public method
 * performs logging.
 * </p>
 * <p>
 * Thread Safety: This class is not completely thread safe because it doesn't manage transactions
 * and it is also mutable. Anyway, the intent is to use this implementation in a stateless session
 * bean so there will be no thread safety issues generated by this class since the container will
 * ensure thread safety.
 * </p>
 *
 * @author DanLazar, TCSDEVELOPER
 * @version 1.0
 */
public class JPADigitalRunPointsTypeDAO extends AbstractDAO implements DigitalRunPointsTypeDAO {
    /**
     * Present the class name.
     */
    private static final String CLASS_NAME = JPADigitalRunPointsTypeDAO.class.getName();

    /**
     * Default constructor.
     */
    public JPADigitalRunPointsTypeDAO() {
        // Empty
    }

    /**
     * Creates a new digital run points type entity into persistence. Returns the
     * DigitalRunPointsType instance with id generated.
     *
     * @throws IllegalArgumentException
     *             if argument is null or if its id>0
     * @throws DigitalRunPointsManagerPersistenceException
     *             if any errors occur when accessing the persistent storage
     * @param pointsType
     *            the entity to be created
     * @return entity with id set
     */
    public DigitalRunPointsType createDigitalRunPointsType(DigitalRunPointsType pointsType)
        throws DigitalRunPointsManagerPersistenceException {
        String methodName = CLASS_NAME + ".createDigitalRunPointsType()";
        // Log the entrance
        Helper.logEntranceInfo(getLogger(), methodName);

        Helper.checkNullWithLog(pointsType, "pointsType", getLogger(), methodName);
        Helper.checkPositiveWithLog(pointsType.getId(), "pointsType.id", getLogger(), methodName);

        // obtain the EntityManager instance
        EntityManager em = getEntityManager();
        // persist the digital run points type entity in the database
        try {
            em.persist(pointsType);
        } catch (EntityExistsException e) {
            Helper.throwPersitenceExceptionWithLog("EntityExistsException occurs while do persisting.", e,
                    getLogger(), methodName);
        }

        // Log the exit
        Helper.logExitInfo(getLogger(), methodName);
        return pointsType;
    }

    /**
     * Updates the given digital run points type entity into persistence.
     *
     * @throws IllegalArgumentException
     *             if argument is null
     * @throws EntityNotFoundException
     *             if a digital run points type entity with digital run points type entity argument
     *             id does not exist in the persistence
     * @throws DigitalRunPointsManagerPersistenceException
     *             if any errors occur when accessing the persistent storage
     * @param pointsType
     *            the entity to be updated
     */
    public void updateDigitalRunPointsType(DigitalRunPointsType pointsType) throws EntityNotFoundException,
        DigitalRunPointsManagerPersistenceException {
        String methodName = CLASS_NAME + ".updateDigitalRunPointsType()";
        // Log the entrance
        Helper.logEntranceInfo(getLogger(), methodName);

        Helper.checkNullWithLog(pointsType, "pointsType", getLogger(), methodName);

        // obtain the EntityManager instance
        EntityManager em = this.getEntityManager();
        // update the entity
        try {
            em.merge(pointsType);
        } catch (IllegalArgumentException iae) {
            Helper.throwEntityNotFoundExceptionWithLog("The pointsType is not exist.", iae, getLogger(),
                    methodName);
        }

        // Log the exit
        Helper.logExitInfo(getLogger(), methodName);
    }

    /**
     * Removes the digital run points type entity identified by the given id from persistence.
     *
     * @throws IllegalArgumentException
     *             if argument<0
     * @throws EntityNotFoundException
     *             if there is no digital run points type entity with the given id in persistence
     * @throws DigitalRunPointsManagerPersistenceException
     *             if any errors occur when accessing the persistent storage
     * @param pointsTypeId
     *            the id that identified the entity to be removed
     */
    public void removeDigitalRunPointsType(long pointsTypeId) throws EntityNotFoundException,
        DigitalRunPointsManagerPersistenceException {
        String methodName = CLASS_NAME + ".removeDigitalRunPointsType()";
        // Log the entrance
        Helper.logEntranceInfo(getLogger(), methodName + " With Id[" + pointsTypeId + "]");

        Helper.checkNegativeWithLog(pointsTypeId, "pointsTypeId", getLogger(), methodName);

        // obtain the EntityManager instance
        EntityManager em = this.getEntityManager();
        // get the DigitalRunPointsType instance to be removed
        DigitalRunPointsType pointsType = em.find(DigitalRunPointsType.class, new Long(pointsTypeId));
        if (pointsType == null) {
            Helper.throwEntityNotFoundExceptionWithLog("DigitalRunPointsType with id[" + pointsTypeId
                    + "] is not exist.", null, getLogger(), methodName);
        }
        // remove the entity
        em.remove(pointsType);

        // Log the exit
        Helper.logExitInfo(getLogger(), methodName);
    }

    /**
     * Gets the digital run points type entity identified by the given id from persistence.
     *
     * @throws IllegalArgumentException
     *             if argument<0
     * @throws EntityNotFoundException
     *             if there is no digital run points type entity with the given id in persistence
     * @throws DigitalRunPointsManagerPersistenceException
     *             if any errors occur when accessing the persistent storage
     * @param pointsTypeId
     *            the id that identifies the entity top be retrieved
     * @return the entity identified by the id
     */
    public DigitalRunPointsType getDigitalRunPointsType(long pointsTypeId) throws EntityNotFoundException,
        DigitalRunPointsManagerPersistenceException {
        String methodName = CLASS_NAME + ".getDigitalRunPointsStatus()";
        // Log the entrance
        Helper.logEntranceInfo(getLogger(), methodName + " With Id[" + pointsTypeId + "]");

        Helper.checkNegativeWithLog(pointsTypeId, "pointsTypeId", getLogger(), methodName);

        // obtain the EntityManager instance
        EntityManager em = this.getEntityManager();
        // get and return the digital run points type entity from the database
        DigitalRunPointsType pointsType = em.find(DigitalRunPointsType.class, new Long(pointsTypeId));
        if (pointsType == null) {
            Helper.throwEntityNotFoundExceptionWithLog("DigitalRunPointsType with id[" + pointsTypeId
                    + "] is not exist.", null, getLogger(), methodName);
        }

        // Log the exit
        Helper.logExitInfo(getLogger(), methodName);
        return pointsType;
    }

    /**
     * Gets all the digital run points types from persistence. If there is no such entity in
     * persistence an empty list is returned.
     *
     * @throws DigitalRunPointsManagerPersistenceException
     *             if any errors occur when accessing the persistent storage
     * @return a list containing all types or an empty list if there is no such entity
     */
    public List<DigitalRunPointsType> getAllDigitalRunPointsTypes()
        throws DigitalRunPointsManagerPersistenceException {
        String methodName = CLASS_NAME + ".getAllDigitalRunPointsTypes()";
        // Log the entrance
        Helper.logEntranceInfo(getLogger(), methodName);

        // get the EntityManager instance
        EntityManager em = this.getEntityManager();
        // create a query that will retrieve all the rows from dr_points_type_lu table
        // create a Query instance
        Query query = em.createQuery("from DigitalRunPointsType");
        // retrieve the result
        List res = query.getResultList();

        // Log the exit
        Helper.logExitInfo(getLogger(), methodName);
        return res;
    }
}
