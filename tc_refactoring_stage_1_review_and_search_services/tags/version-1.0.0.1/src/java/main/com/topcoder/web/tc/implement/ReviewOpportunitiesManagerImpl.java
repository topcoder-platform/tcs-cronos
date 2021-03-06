/*
 * Copyright (C) 2011 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.web.tc.implement;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.hibernate3.HibernateCallback;

import com.topcoder.commons.utils.Log4jUtility;
import com.topcoder.web.tc.ContestsServiceManagerException;
import com.topcoder.web.tc.ServicesHelper;
import com.topcoder.web.tc.ReviewOpportunitiesManager;
import com.topcoder.web.tc.SortingOrder;
import com.topcoder.web.tc.dto.Filterable;
import com.topcoder.web.tc.dto.ReviewOpportunityDTO;
import com.topcoder.web.tc.dto.ReviewOpportunitiesFilter;

/**
 * <p>
 * This is the default implementation of ReviewOpportunitiesManager. It uses Hibernate to perform the searching.
 * </p>
 * <p>
 * This class is not technically thread-safe because it's mutable. However, it can be considered effectively thread-safe
 * after configuration assuming that the parameters passed in do not changed by other threads at the same time.
 * </p>
 * @author flytoj2ee, duxiaoyang
 * @version 1.0
 */
public class ReviewOpportunitiesManagerImpl extends AbstractManagerImpl implements ReviewOpportunitiesManager {

    /**
     * Represents the base HQL statement used to search contests.
     */
    private static final String BASE_HQL = "SELECT pgc.name AS type, pc.name AS subtype,"
            + " ppay.amount AS primaryReviewerPayment, spay.amount AS secondaryReviewerPayment,"
            + " (SELECT COUNT(DISTINCT u.resourceId) FROM Upload u, Submission s WHERE u.projectId = p.projectId"
            + " AND s.submissionTypeId = 1 AND s.submissionStatusId in (1, 2, 3, 4) AND u.uploadId = s.uploadId"
            + " AND u.uploadTypeId = 1) AS submissionsNumber, cc.componentName AS contestName,"
            + " pprev.scheduledStartTime AS reviewStart, pprev.scheduledEndTime AS reviewEnd,"
            + " CAST(ph.parameter AS int) - (SELECT COUNT(*) FROM RBoardApplication rba"
            + " WHERE rba.projectId = p.projectId AND rba.phaseId < 1000) AS numberOfReviewPositionsAvailable,"
            + " ppreg.scheduledStartTime AS opensOn, ppsub.scheduledStartTime AS contestStartTime"
            + " FROM Project p, ProjectPhase ppreg, ProjectPhase ppsub, ProjectPhase ppscr, ProjectPhase pprev,"
            + " PhaseCriteria ph, ProjectInfo pi1, CompVersions cv, CompVersionDates cvd, CompCatalog cc,"
            + " ProjectCategoryLookup pc, ProjectGroupCategoryLookup pgc, ProjectCatalogLookup c,"
            + " RBoardPayment ppay, RBoardPayment spay"
            + " WHERE NOT EXISTS (SELECT 1 FROM ContestEligibility WHERE studio = 0 AND contestId = p.projectId)"
            + " AND ppreg.projectId = p.projectId AND ppreg.phaseTypeId = 1 AND ppreg.scheduledStartTime <= CURRENT"
            + " AND ppsub.projectId = p.projectId AND ppsub.phaseTypeId = 2 AND ppsub.scheduledStartTime <= CURRENT"
            + " AND ppscr.projectId = p.projectId AND ppscr.phaseTypeId = 3"
            + " AND pprev.projectId = p.projectId AND pprev.phaseTypeId = 4"
            + " AND (ppreg.phaseStatusId = 2 OR ppsub.phaseStatusId = 2 OR ppscr.phaseStatusId = 2 OR pprev.phaseStatusId = 2)"
            + " AND ph.projectPhaseId = pprev.projectPhaseId AND ph.phaseCriteriaTypeId = 6"
            + " AND pi1.projectId = p.projectId AND pi1.projectInfoTypeId = 1"
            + " AND pi1.value = cv.compVersId AND cv.phaseId IN (112, 113)"
            + " AND cvd.compVersId = cv.compVersId AND cvd.phaseId = cv.phaseId AND cvd.statusId <> 303"
            + " AND cc.componentId = cv.componentId"
            + " AND p.projectCategoryId = pc.projectCategoryId AND pc.projectGroupCategoryId = pgc.projectGroupCategoryId"
            + " AND pgc.projectCatalogId = c.projectCatalogId AND p.projectStatusId = 1"
            + " AND ppay.projectId = p.projectId AND ppay.phaseId = pprev.projectPhaseId AND ppay.primary = 1"
            + " AND spay.projectId = p.projectId AND spay.phaseId = pprev.projectPhaseId and spay.primary = 0"
            + " AND (SELECT COUNT(*) FROM RBoardApplication app WHERE app.projectId = p.projectId AND app.phaseId < 1000) < CAST(ph.parameter AS int)"
            + " AND (SELECT COUNT(*) FROM RBoardApplication app WHERE app.projectId = p.projectId AND app.phaseId = cv.phaseId) < 3";

    /**
     * Represents the criteria used to filter contest name.
     */
    private static final String CONTEST_NAME_CRITERIA = " AND cc.componentName LIKE :contestName";

    /**
     * Represents the criteria used to filter start value of payment.
     */
    private static final String PAYMENT_START_CRITERIA = " AND ppay.amount >= :paymentStart";

    /**
     * Represents the criteria used to filter end value of payment.
     */
    private static final String PAYMENT_END_CRITERIA = " AND ppay.amount <= :paymentEnd";

    /**
     * Represents the criteria used to filter contest end date before a specific date.
     */
    private static final String REVIEW_END_DATE_BEFORE_CRITERIA = " AND pprev.scheduledEndTime < :reviewEnd";

    /**
     * Represents the criteria used to filter contest end date after a specific date.
     */
    private static final String REVIEW_END_DATE_AFTER_CRITERIA = " AND pprev.scheduledEndTime > :reviewEnd";

    /**
     * Represents the criteria used to filter contest end date on a specific date.
     */
    private static final String REVIEW_END_DATE_ON_CRITERIA = " AND pprev.scheduledEndTime = :reviewEnd";

    /**
     * Represents the criteria used to filter contest end date between two specific dates.
     */
    private static final String REVIEW_END_DATE_BETWEEN_CRITERIA = " AND pprev.scheduledEndTime BETWEEN :reviewEnd1 AND :reviewEnd2";

    /**
     * Represents the criteria used to filter contest end date before current date.
     */
    private static final String REVIEW_END_DATE_BEFORE_CURRENT_CRITERIA = " AND pprev.scheduledEndTime < CURRENT";

    /**
     * Represents the criteria used to filter contest end date after current date.
     */
    private static final String REVIEW_END_DATE_AFTER_CURRENT_CRITERIA = " AND pprev.scheduledEndTime > CURRENT";

    /**
     * <p>
     * Creates an instance of this class. It does nothing.
     * </p>
     */
    public ReviewOpportunitiesManagerImpl() {
    }

    /**
     * <p>
     * Searches review opportunities by given filtering condition with pagination support.
     * </p>
     * @param columnName
     *            the name of the column to sort on. If it is null or empty, the sorting will based on registration
     *            start time.
     * @param sortingOrder
     *            the order of sorting. If it is null, descending order will be used.
     * @param pageNumber
     *            the number of page to retrieve. -1 means all page will be retrieved.
     * @param pageSize
     *            the number of records in each page. -1 means all records should be retrieved.
     * @param filter
     *            the filter used to make constraints on searching criteria.
     * @return a list of ReviewOpportunityDTO that matches given filter. It will be empty if no such contest can be
     *         found.
     * @throws IllegalArgumentException
     *             if filter is null, or either pageNumber or pageSize is zero or negative (-1 is acceptable).
     * @throws ContestsServiceManagerException
     *             is any error occurs while performing the search.
     */
    public List<ReviewOpportunityDTO> retrieveReviewOpportunities(String columnName, SortingOrder sortingOrder,
            int pageNumber, int pageSize, ReviewOpportunitiesFilter filter) throws ContestsServiceManagerException {
        String methodName = getClass().getName()
                + ".retrieveReviewOpportunities(String, SortingOrder, int, int, ReviewOpportunitiesFilter)";
        Log4jUtility.logEntrance(getLogger(), methodName, new String[] { "columnName", "sortingOrder", "pageNumber",
                "pageSize", "filter" }, new Object[] { columnName, sortingOrder, pageNumber, pageSize, filter });

        ServicesHelper.checkNumber(pageNumber, "pageNumber", methodName, getLogger());
        ServicesHelper.checkNumber(pageSize, "pageSize", methodName, getLogger());
        ServicesHelper.checkObject(filter, "filter", methodName, getLogger());

        List<ReviewOpportunityDTO> contests = new ArrayList<ReviewOpportunityDTO>();
        try {
            getHibernateTemplate().execute(
                    new RetrieveReviewOpportunitiesHibernateCallback(columnName, sortingOrder, pageNumber, pageSize,
                            filter, contests));
        } catch (DataAccessException e) {
            throw ServicesHelper.logException(getLogger(), methodName, new ContestsServiceManagerException(
                    "error occurs while retrieving upcoming contests", e));
        }

        Log4jUtility.logExit(getLogger(), methodName, new Object[] { contests });
        return contests;
    }

    /**
     * <p>
     * Searches review opportunities by given filtering condition.
     * </p>
     * @param filter
     *            the filter used to make constraints on searching criteria.
     * @return a list of UpcomingContestDTO that matches given filter. It will be empty if no such contest can be found.
     * @throws IllegalArgumentException
     *             if filter is null.
     * @throws ContestsServiceManagerException
     *             is any error occurs while performing the search.
     */
    public List<ReviewOpportunityDTO> retrieveReviewOpportunities(ReviewOpportunitiesFilter filter)
            throws ContestsServiceManagerException {
        String methodName = getClass().getName() + ".retrieveReviewOpportunities(ReviewOpportunitiesFilter)";
        Log4jUtility.logEntrance(getLogger(), methodName, new String[] { "filter" }, new Object[] { filter });

        ServicesHelper.checkObject(filter, "filter", methodName, getLogger());

        List<ReviewOpportunityDTO> result = null;
        try {
            result = retrieveReviewOpportunities(null, null, Filterable.RETURN_ALL_PAGES, Filterable.RETURN_ALL_PAGES,
                    filter);
        } catch (ContestsServiceManagerException e) {
            throw ServicesHelper.logException(getLogger(), methodName, e);
        }

        Log4jUtility.logExit(getLogger(), methodName, new Object[] { result });
        return result;
    }

    /**
     * <p>
     * This class implements the HibernateCallback interface to perform query for searching review opportunities.
     * </p>
     * <p>
     * This class is thread-safe because all its properties are not changed after initialization.
     * </p>
     * @author TCSDEVELOPER
     * @version 1.0
     */
    private class RetrieveReviewOpportunitiesHibernateCallback implements HibernateCallback<Object> {

        /**
         * Represents the column name on which the sorting is performed.
         */
        private String columnName;

        /**
         * Represents the sorting order.
         */
        private SortingOrder sortingOrder;

        /**
         * Represents the number of page to be retrieved.
         */
        private int pageNumber;

        /**
         * Represents the number of records in each page.
         */
        private int pageSize;

        /**
         * Represents the filter used to make constraints on searching result.
         */
        private ReviewOpportunitiesFilter filter;

        /**
         * Represents a list of UpcomingContestDTO to store the searching result.
         */
        private List<ReviewOpportunityDTO> reviewOpportunities;

        /**
         * <p>
         * Creates an instance of this class using given parameters.
         * </p>
         * @param columnName
         *            the column name on which sorting is performed.
         * @param sortingOrder
         *            the sorting order.
         * @param pageNumber
         *            the number of page to be retrieved.
         * @param pageSize
         *            the number of records in each page.
         * @param filter
         *            the filter used to make constraints on searching result.
         * @param reviewOpportunities
         *            a list of ReviewOpportunityDTO to store the searching result.
         */
        public RetrieveReviewOpportunitiesHibernateCallback(String columnName, SortingOrder sortingOrder,
                int pageNumber, int pageSize, ReviewOpportunitiesFilter filter,
                List<ReviewOpportunityDTO> reviewOpportunities) {
            this.columnName = columnName;
            this.sortingOrder = sortingOrder;
            this.pageNumber = pageNumber;
            this.pageSize = pageSize;
            this.filter = filter;
            this.reviewOpportunities = reviewOpportunities;
        }

        /**
         * <p>
         * Performing searching using Hibernate.
         * </p>
         * @param session
         *            the Hibernate session.
         * @return Object always return null.
         * @throws HibernateException
         *             if any error occurs while performing searching.
         */
        @Override
        public Object doInHibernate(Session session) throws HibernateException {
            // construct the HQL statement
            StringBuilder hql = new StringBuilder(BASE_HQL);
            hql.append(getCommonFilterClause(filter));
            if (filter.getContestName() != null && filter.getContestName().trim().length() != 0) {
                hql.append(CONTEST_NAME_CRITERIA);
            }
            if (filter.getPaymentStart() != null && filter.getPaymentStart() != Filterable.OPEN_INTERVAL) {
                hql.append(PAYMENT_START_CRITERIA);
            }
            if (filter.getPaymentEnd() != null && filter.getPaymentEnd() != Filterable.OPEN_INTERVAL) {
                hql.append(PAYMENT_END_CRITERIA);
            }
            if (filter.getReviewEndDate() != null) {
                switch (filter.getReviewEndDate().getIntervalType()) {
                case BEFORE:
                    hql.append(REVIEW_END_DATE_BEFORE_CRITERIA);
                    break;
                case AFTER:
                    hql.append(REVIEW_END_DATE_AFTER_CRITERIA);
                    break;
                case ON:
                    hql.append(REVIEW_END_DATE_ON_CRITERIA);
                    break;
                case BETWEEN_DATES:
                    hql.append(REVIEW_END_DATE_BETWEEN_CRITERIA);
                    break;
                case BEFORE_CURRENT_DATE:
                    hql.append(REVIEW_END_DATE_BEFORE_CURRENT_CRITERIA);
                    break;
                case AFTER_CURRENT_DATE:
                    hql.append(REVIEW_END_DATE_AFTER_CURRENT_CRITERIA);
                    break;
                }
            }
            hql.append(getSortingClause(columnName, sortingOrder));

            // construct the Query object
            Query query = session.createQuery(hql.toString());
            setCommonFilterCondition(query, filter);
            if (filter.getContestName() != null && filter.getContestName().trim().length() != 0) {
                query.setString("contestName", "%" + filter.getContestName() + "%");
            }
            if (filter.getPaymentStart() != null && filter.getPaymentStart() != Filterable.OPEN_INTERVAL) {
                query.setInteger("paymentStart", filter.getPaymentStart());
            }
            if (filter.getPaymentEnd() != null && filter.getPaymentEnd() != Filterable.OPEN_INTERVAL) {
                query.setInteger("paymentEnd", filter.getPaymentEnd());
            }
            if (filter.getReviewEndDate() != null) {
                switch (filter.getReviewEndDate().getIntervalType()) {
                case BEFORE:
                case AFTER:
                case ON:
                    query.setDate("reviewEnd", filter.getReviewEndDate().getFirstDate());
                    break;
                case BETWEEN_DATES:
                    query.setDate("reviewEnd1", filter.getReviewEndDate().getFirstDate());
                    query.setDate("reviewEnd2", filter.getReviewEndDate().getSecondDate());
                    break;
                }
            }

            // pagination support
            if (pageNumber != Filterable.RETURN_ALL_PAGES && pageSize != Filterable.RETURN_ALL_PAGES) {
                query.setMaxResults(pageSize);
                query.setFirstResult((pageNumber - 1) * pageSize);
            }

            // perform the query, and fill the result into the given list
            @SuppressWarnings("unchecked")
            List<Object[]> result = query.list();
            for (Object[] row : result) {
                ReviewOpportunityDTO dto = new ReviewOpportunityDTO();
                dto.setType((String) row[0]);
                dto.setSubtype((String) row[1]);
                dto.setPrimaryReviewerPayment((Double) row[2]);
                dto.setSecondaryReviewerPayment((Double) row[3]);
                dto.setSubmissionsNumber(((Long) row[4]).intValue());
                dto.setContestName((String) row[5]);
                dto.setReviewStart((Date) row[6]);
                dto.setReviewEnd((Date) row[7]);
                dto.setNumberOfReviewPositionsAvailable(((Long) row[8]).intValue());
                dto.setOpensOn((Date) row[9]);
                reviewOpportunities.add(dto);
            }
            return null;
        }
    }
}
