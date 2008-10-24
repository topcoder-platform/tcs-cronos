/*
 * Copyright (C) 2008 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.project.studio.accuracytests;

import com.topcoder.management.project.Project;
import com.topcoder.management.project.studio.ConversionException;
import com.topcoder.management.project.studio.ProjectToContestConverter;
import com.topcoder.management.project.studio.converter.ProjectToContestConverterImpl;

import com.topcoder.search.builder.filter.EqualToFilter;
import com.topcoder.search.builder.filter.Filter;
import com.topcoder.search.builder.filter.NotFilter;
import com.topcoder.search.builder.filter.NullFilter;
import com.topcoder.search.builder.filter.OrFilter;

import com.topcoder.service.studio.contest.Contest;
import com.topcoder.service.studio.contest.ContestChannel;
import com.topcoder.service.studio.contest.ContestConfig;
import com.topcoder.service.studio.contest.ContestManagementException;
import com.topcoder.service.studio.contest.ContestManager;
import com.topcoder.service.studio.contest.ContestPayment;
import com.topcoder.service.studio.contest.ContestProperty;
import com.topcoder.service.studio.contest.ContestStatus;
import com.topcoder.service.studio.contest.ContestType;
import com.topcoder.service.studio.contest.ContestTypeConfig;
import com.topcoder.service.studio.contest.Document;
import com.topcoder.service.studio.contest.DocumentType;
import com.topcoder.service.studio.contest.EntityNotFoundException;
import com.topcoder.service.studio.contest.Medium;
import com.topcoder.service.studio.contest.MimeType;
import com.topcoder.service.studio.contest.StudioFileType;
import com.topcoder.service.studio.submission.PaymentStatus;
import com.topcoder.service.studio.submission.Prize;
import com.topcoder.service.studio.submission.PrizeType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;


/**
 * This is a mock implementation of ContestManager class.
 */
public class MockContestManager implements ContestManager {
    /** Current created contest. */
    private Contest contest;

    /**
     * <p>
     * Creates a new contest and returns the created contest.
     * </p>
     *
     * @param contest the contest to create
     *
     * @return the created contest
     *
     * @throws IllegalArgumentException if the arg is null.
     * @throws EntityAlreadyExistsException if the entity already exists in the persistence.
     * @throws ContestManagementException if any other error occurs.
     */
    public Contest createContest(Contest contest) throws ContestManagementException {
        this.contest = contest;

        return contest;
    }

    /**
     * <p>
     * Gets contest by id, and return the retrieved contest. If the contest doesn't exist, null is returned.
     * </p>
     *
     * @param contestId the contest id
     *
     * @return the retrieved contest, or null if id doesn't exist
     *
     * @throws ContestManagementException if any error occurs when getting contest.
     */
    public Contest getContest(long contestId) throws ContestManagementException {
        Project project = ContestManagerProjectAdapterAccTest.createProjectInstance();
        ProjectToContestConverter converter = new ProjectToContestConverterImpl();

        try {
            return converter.convertProjectToContest(project);
        } catch (ConversionException e) {
            throw new ContestManagementException("Fail to find contest.");
        }
    }

    /**
     * <p>
     * Gets projects's contests by the project id. A list of contests associated with the given tcDirectProjectId
     * should be returned. If there is no such contests, an empty list should be returned.
     * </p>
     *
     * @param tcDirectProjectId the project id
     *
     * @return a list of associated contests
     *
     * @throws ContestManagementException if any error occurs when getting contests
     */
    public List<Contest> getContestsForProject(long tcDirectProjectId)
        throws ContestManagementException {
        StudioFileType fileType = new StudioFileType();
        fileType.setDescription("PS");
        fileType.setExtension(".ps");
        fileType.setImageFile(false);
        fileType.setStudioFileType(34);

        ContestChannel channel = new ContestChannel();
        channel.setContestChannelId(new Long(2));
        channel.setDescription("This is a channel");

        ContestStatus contestStatus = new ContestStatus();
        contestStatus.setContestStatusId(new Long(24));
        contestStatus.setDescription("This is a status");
        contestStatus.setName("name");

        ContestType contestType = new ContestType();
        contestType.setContestType(new Long(234));
        contestType.setDescription("this is a contest type");

        Contest contest = new Contest();
        contest.setContestChannel(channel);
        contest.setContestId(new Long(24));
        contest.setContestType(contestType);
        contest.setCreatedUser(new Long(34654));
        contest.setEndDate(new Date());
        contest.setStatus(contestStatus);
        contest.setStartDate(new Date());

        return Arrays.asList(new Contest[] { contest });
    }

    /**
     * <p>
     * Updates contest data. Note that all data can be updated only if contest is not active. If contest is active it
     * is possible to increase prize amount and duration.
     * </p>
     *
     * @param contest the contest to update
     *
     * @throws IllegalArgumentException if the argument is null.
     * @throws EntityNotFoundException if the contest doesn't exist in persistence.
     * @throws ContestManagementException if any error occurs when updating contest.
     */
    public void updateContest(Contest contest) throws ContestManagementException {
        this.contest = contest;
    }

    /**
     * <p>
     * Updates contest status to the given value.
     * </p>
     *
     * @param contestId the contest id
     * @param newStatusId the new status id
     *
     * @throws EntityNotFoundException if there is no corresponding Contest or ContestStatus in persistence.
     * @throws ContestStatusTransitionException if it's not allowed to update the contest to the given status.
     * @throws ContestManagementException if any error occurs when updating contest's status.
     */
    public void updateContestStatus(long contestId, long newStatusId)
        throws ContestManagementException {
    }

    /**
     * <p>
     * Gets client for contest, the client id is returned.
     * </p>
     *
     * @param contestId the contest id
     *
     * @return the id of the client for this contest
     *
     * @throws EntityNotFoundException if there is no corresponding contest (or project) in persistence.
     * @throws ContestManagementException if any error occurs when retrieving the client id.
     */
    public long getClientForContest(long contestId) throws ContestManagementException {
        return 0;
    }

    /**
     * <p>
     * Gets client for project, and return the retrieved client id.
     * </p>
     *
     * @param projectId the project id
     *
     * @return the client id
     *
     * @throws EntityNotFoundException if there is no corresponding project in persistence.
     * @throws ContestManagementException if any error occurs when retrieving the client id.
     */
    public long getClientForProject(long projectId) throws ContestManagementException {
        return 0;
    }

    /**
     * <p>
     * Adds contest status, and return the added contest status.
     * </p>
     *
     * @param contestStatus the contest status to add
     *
     * @return the added contest status
     *
     * @throws IllegalArgumentException if the arg is null.
     * @throws EntityAlreadyExistsException if the entity already exists in the persistence.
     * @throws ContestManagementException if any other error occurs.
     */
    public ContestStatus addContestStatus(ContestStatus contestStatus)
        throws ContestManagementException {
        return null;
    }

    /**
     * <p>
     * Updates contest status.
     * </p>
     *
     * @param contestStatus the contest status to update
     *
     * @throws IllegalArgumentException if the arg is null.
     * @throws EntityNotFoundException if the contestStatus doesn't exist in persistence.
     * @throws ContestManagementException if any other error occurs.
     */
    public void updateContestStatus(ContestStatus contestStatus)
        throws ContestManagementException {
    }

    /**
     * <p>
     * Removes contest status, return true if the contest status exists and removed successfully, return false if it
     * doesn't exist.
     * </p>
     *
     * @param contestStatusId the contest status id
     *
     * @return true if the contest status exists and removed successfully, return false if it doesn't exist
     *
     * @throws ContestManagementException if any error occurs.
     */
    public boolean removeContestStatus(long contestStatusId)
        throws ContestManagementException {
        return false;
    }

    /**
     * <p>
     * Gets contest status, and return the retrieved contest status. Return null if it doesn't exist.
     * </p>
     *
     * @param contestStatusId the contest status id
     *
     * @return the retrieved contest status, or null if it doesn't exist
     *
     * @throws ContestManagementException if any error occurs when getting contest status.
     */
    public ContestStatus getContestStatus(long contestStatusId)
        throws ContestManagementException {
        return null;
    }

    /**
     * <p>
     * Adds new document, and return the added document.
     * </p>
     *
     * @param document the document to add
     *
     * @return the added document
     *
     * @throws IllegalArgumentException if the arg is null.
     * @throws EntityAlreadyExistsException if the entity already exists in the persistence.
     * @throws ContestManagementException if any other error occurs.
     */
    public Document addDocument(Document document) throws ContestManagementException {
        return null;
    }

    /**
     * <p>
     * Updates the document.
     * </p>
     *
     * @param document the document to update
     *
     * @throws IllegalArgumentException if the arg is null.
     * @throws EntityNotFoundException if the document doesn't exist in persistence.
     * @throws ContestManagementException if any other error occurs.
     */
    public void updateDocument(Document document) throws ContestManagementException {
    }

    /**
     * <p>
     * Gets document by id, and return the retrieved document. Return null if the document doesn't exist.
     * </p>
     *
     * @param documentId the document id
     *
     * @return the retrieved document, or null if it doesn't exist
     *
     * @throws ContestManagementException if any error occurs when getting document.
     */
    public Document getDocument(long documentId) throws ContestManagementException {
        return null;
    }

    /**
     * <p>
     * Removes document, return true if the document exists and removed successfully, return false if it doesn't exist.
     * </p>
     *
     * @param documentId the document id
     *
     * @return true if the document exists and removed successfully, return false if it doesn't exist
     *
     * @throws ContestManagementException if any error occurs.
     */
    public boolean removeDocument(long documentId) throws ContestManagementException {
        return false;
    }

    /**
     * <p>
     * Adds document to contest. Nothing happens if the document already exists in contest.
     * </p>
     *
     * @param documentId the document id
     * @param contestId the contest id
     *
     * @throws EntityNotFoundException if there is no corresponding document or contest in persistence.
     * @throws ContestManagementException if any other error occurs.
     */
    public void addDocumentToContest(long documentId, long contestId)
        throws ContestManagementException {
    }

    /**
     * <p>
     * Removes document from contest. Return true if the document exists in the contest and removed successfully,
     * return false if it doesn't exist in contest.
     * </p>
     *
     * @param documentId the document id
     * @param contestId the contest id
     *
     * @return true if the document exists in the contest and removed successfully, returns false if it doesn't exist
     *         in contest
     *
     * @throws EntityNotFoundException if there is no corresponding document or contest in persistence.
     * @throws ContestManagementException if any other error occurs.
     */
    public boolean removeDocumentFromContest(long documentId, long contestId)
        throws ContestManagementException {
        return false;
    }

    /**
     * <p>
     * Adds contest category, and return the added contest category.
     * </p>
     *
     * @param contestChannel the contest channel to add
     *
     * @return the added contest channel
     *
     * @throws IllegalArgumentException if the arg is null.
     * @throws EntityAlreadyExistsException if the entity already exists in the persistence.
     * @throws ContestManagementException if any other error occurs.
     */
    public ContestChannel addContestChannel(ContestChannel contestChannel)
        throws ContestManagementException {
        return null;
    }

    /**
     * <p>
     * Updates contest channel.
     * </p>
     *
     * @param contestChannel the contest channel to update
     *
     * @throws IllegalArgumentException if the arg is null.
     * @throws EntityNotFoundException if the contestChannel doesn't exist in persistence.
     * @throws ContestManagementException if any other error occurs.
     */
    public void updateContestChannel(ContestChannel contestChannel)
        throws ContestManagementException {
    }

    /**
     * <p>
     * Removes contest channel, return true if the contest category exists and removed successfully, return false if it
     * doesn't exist.
     * </p>
     *
     * @param contestChannelId the contest channel id
     *
     * @return true if the contest channel exists and removed successfully, return false if it doesn't exist.
     *
     * @throws ContestManagementException if fail to remove the contest category when it exists.
     */
    public boolean removeContestChannel(long contestChannelId)
        throws ContestManagementException {
        return false;
    }

    /**
     * <p>
     * Gets contest channel, and return the retrieved contest channel. Return null if it doesn't exist.
     * </p>
     *
     * @param contestChannelId the contest channel id
     *
     * @return the retrieved contest channel, or null if it doesn't exist.
     *
     * @throws ContestManagementException if any error occurs when getting contest channel.
     */
    public ContestChannel getContestChannel(long contestChannelId)
        throws ContestManagementException {
        return null;
    }

    /**
     * <p>
     * Adds contest configuration parameter, and return the added contest configuration parameter.
     * </p>
     *
     * @param contestConfig the contest configuration parameter to add
     *
     * @return the added contest configuration parameter
     *
     * @throws IllegalArgumentException if the arg is null.
     * @throws EntityAlreadyExistsException if the entity already exists in the persistence.
     * @throws ContestManagementException if any other error occurs.
     */
    public ContestConfig addConfig(ContestConfig contestConfig)
        throws ContestManagementException {
        return null;
    }

    /**
     * <p>
     * Updates contest configuration parameter.
     * </p>
     *
     * @param contestConfig the contest configuration parameter to update
     *
     * @throws IllegalArgumentException if the arg is null.
     * @throws EntityNotFoundException if the contest configuration parameter doesn't exist in persistence.
     * @throws ContestManagementException if any other error occurs.
     */
    public void updateConfig(ContestConfig contestConfig)
        throws ContestManagementException {
    }

    /**
     * <p>
     * Gets contest configuration parameter by id, and return the retrieved contest configuration parameter. Return
     * null if it doesn't exist.
     * </p>
     *
     * @param contestConfigId the contest configuration parameter id
     *
     * @return the retrieved contest configuration parameter, or null if it doesn't exist.
     *
     * @throws ContestManagementException if any error occurs when getting contest configuration parameter
     */
    public ContestConfig getConfig(long contestConfigId)
        throws ContestManagementException {
        return null;
    }

    /**
     * <p>
     * Saves document content in file system. This method should call DocumentContentManager.saveDocumentContent to
     * save the document content.
     * </p>
     *
     * @param documentId the document id
     * @param documentContent the file data of the document to save
     *
     * @throws IllegalArgumentException if fileData argument is null or empty array.
     * @throws EntityNotFoundException if the document doesn't exist in persistence.
     * @throws ContestManagementException if any other error occurs.
     */
    public void saveDocumentContent(long documentId, byte[] documentContent)
        throws ContestManagementException {
    }

    /**
     * <p>
     * Gets document content to return. If the document is not saved, null is returned. It will use
     * DocumentContentManager to get document content. It can also return empty array if the document content is
     * empty.
     * </p>
     *
     * @param documentId the document id
     *
     * @return the document content in byte array. If the document is not saved, null is returned.
     *
     * @throws EntityNotFoundException if the document doesn't exist in persistence.
     * @throws ContestManagementException if any other error occurs.
     */
    public byte[] getDocumentContent(long documentId) throws ContestManagementException {
        return null;
    }

    /**
     * <p>
     * Checks the document's content exists or not. Return true if it exists, return false otherwise. It will use
     * DocumentContentManager to check document content's existence.
     * </p>
     *
     * @param documentId the document id
     *
     * @return true if the document content exists, return false otherwise.
     *
     * @throws EntityNotFoundException if the document doesn't exist in persistence.
     * @throws ContestManagementException if any other error occurs.
     */
    public boolean existDocumentContent(long documentId)
        throws ContestManagementException {
        return false;
    }

    /**
     * <p>
     * Gets all contest statuses to return. If no contest status exists, return an empty list.
     * </p>
     *
     * @return a list of contest statuses
     *
     * @throws ContestManagementException if any error occurs when getting contest statuses.
     */
    public List<ContestStatus> getAllContestStatuses()
        throws ContestManagementException {
        ContestStatus contestStatus = new ContestStatus();
        contestStatus.setContestStatusId(new Long(1));
        contestStatus.setDescription("This is a status");
        contestStatus.setName("register");

        ContestStatus contestStatus2 = new ContestStatus();
        contestStatus2.setContestStatusId(new Long(2));
        contestStatus2.setDescription("This is a status");
        contestStatus2.setName("review");

        return Arrays.asList(new ContestStatus[] { contestStatus, contestStatus2 });
    }

    /**
     * <p>
     * Gets all contest channels to return. If no contest category exists, return an empty list.
     * </p>
     *
     * @return a list of contest channels
     *
     * @throws ContestManagementException if any error occurs when getting contest channels.
     */
    public List<ContestChannel> getAllContestChannels()
        throws ContestManagementException {
        StudioFileType fileType = new StudioFileType();
        fileType.setDescription("PS");
        fileType.setExtension(".ps");
        fileType.setImageFile(false);
        fileType.setStudioFileType(34);

        ContestChannel channel1 = new ContestChannel();
        channel1.setContestChannelId(new Long(1));
        channel1.setDescription("This is channel 1");

        ContestChannel channel2 = new ContestChannel();
        channel2.setContestChannelId(new Long(2));
        channel2.setDescription("This is channel 2");

        return Arrays.asList(new ContestChannel[] { channel1, channel2 });
    }

    /**
     * <p>
     * Get all studio file types to return. If no studio file type exists, return an empty list.
     * </p>
     *
     * @return a list of studio file types
     *
     * @throws ContestManagementException if any error occurs when getting studio file types.
     */
    public List<StudioFileType> getAllStudioFileTypes()
        throws ContestManagementException {
        StudioFileType fileType = new StudioFileType();
        fileType.setDescription("PS");
        fileType.setExtension(".ps");
        fileType.setImageFile(false);
        fileType.setStudioFileType(1);

        return Arrays.asList(new StudioFileType[] { fileType });
    }

    /**
     * <p>
     * Adds contest type configuration parameter, and return the added contest type configuration parameter.
     * </p>
     *
     * @param contestTypeConfig the contest type configuration parameter to add
     *
     * @return the added contest type configuration parameter
     *
     * @throws IllegalArgumentException if the arg is null.
     * @throws EntityAlreadyExistsException if the entity already exists in the persistence.
     * @throws ContestManagementException if any other error occurs.
     */
    public ContestTypeConfig addContestTypeConfig(ContestTypeConfig contestTypeConfig)
        throws ContestManagementException {
        return null;
    }

    /**
     * <p>
     * Updates contest type configuration parameter.
     * </p>
     *
     * @param contestTypeConfig the contest type configuration parameter to update
     *
     * @throws IllegalArgumentException if the arg is null.
     * @throws EntityNotFoundException if the contest type configuration parameter doesn't exist in persistence.
     * @throws ContestManagementException if any other error occurs.
     */
    public void updateContestTypeConfig(ContestTypeConfig contestTypeConfig)
        throws ContestManagementException {
    }

    /**
     * <p>
     * Gets contest type configuration parameter by id, and return the retrieved contest type configuration parameter.
     * Return null if it doesn't exist.
     * </p>
     *
     * @param contestTypeConfigId the contest type configuration parameter id
     *
     * @return the retrieved contest type configuration parameter, or null if it doesn't exist
     *
     * @throws ContestManagementException if any error occurs when getting contest type configuration parameter
     */
    public ContestTypeConfig getContestTypeConfig(long contestTypeConfigId)
        throws ContestManagementException {
        return null;
    }

    /**
     * <p>
     * Adds prize to the given contest. Nothing happens if the prize already exists in contest.
     * </p>
     *
     * @param contestId the contest id
     * @param prizeId the prize id
     *
     * @throws EntityNotFoundException if there is no corresponding prize or contest in persistence.
     * @throws ContestManagementException if any other error occurs.
     */
    public void addPrizeToContest(long contestId, long prizeId)
        throws ContestManagementException {
    }

    /**
     * <p>
     * Removes prize from contest. Return true if the prize exists in the contest and removed successfully, return
     * false if it doesn't exist in contest.
     * </p>
     *
     * @param contestId the contest id
     * @param prizeId the prize id
     *
     * @return true if the prize exists in the contest and removed successfully, return false if it doesn't exist in
     *         contest.
     *
     * @throws EntityNotFoundException if there is no corresponding prize or contest in persistence.
     * @throws ContestManagementException if any other error occurs.
     */
    public boolean removePrizeFromContest(long contestId, long prizeId)
        throws ContestManagementException {
        return false;
    }

    /**
     * <p>
     * Retrieves all prizes in the given contest to return. An empty list is returned if there is no such prizes.
     * </p>
     *
     * @param contestId the contest id
     *
     * @return a list of prizes
     *
     * @throws EntityNotFoundException if there is no corresponding contest in persistence.
     * @throws ContestManagementException if any other error occurs.
     */
    public List<Prize> getContestPrizes(long contestId)
        throws ContestManagementException {
        return null;
    }

    /**
     * Gets all contests.
     *
     * @return All contests
     */
    public List<Contest> getAllContests() throws ContestManagementException {
        return null;
    }

    /**
     * Searches contests with filter.
     *
     * @param filter The filter to search with
     *
     * @return The found contests
     */
    public List<Contest> searchContests(Filter filter)
        throws ContestManagementException {
        List<Contest> contests = new ArrayList<Contest>();

        if (validFilter(filter)) {
            Project project = ContestManagerProjectAdapterAccTest.createProjectInstance();
            ProjectToContestConverter converter = new ProjectToContestConverterImpl();

            try {
                contests.add(converter.convertProjectToContest(project));
            } catch (ConversionException e) {
                throw new ContestManagementException("Fail to find contest.");
            }
        }

        return contests;
    }

    private boolean validFilter(Filter filter) {
        if (filter instanceof EqualToFilter) {
            return (validEqualToFilter(((EqualToFilter) filter)));
        } else if (filter instanceof NotFilter) {
            return ((NotFilter) filter).getFilter() instanceof NullFilter;
        } else if ( filter instanceof OrFilter) {
        	return true;
        }

        return false;
    }

    private boolean validEqualToFilter(EqualToFilter theFilter) {
        String name = theFilter.getName();

        if (name.equalsIgnoreCase("createdUser") || name.equalsIgnoreCase(name)) {
            return true;
        } else if (name.equalsIgnoreCase(ProjectToContestConverterImpl.STUDIO_FILE_TYPE_ID) ||
                name.equalsIgnoreCase(ProjectToContestConverterImpl.STUDIO_CONTEST_CHANNEL_ID)) {
            return ((Long) theFilter.getValue()).longValue() == 1;
        } else if (name.equalsIgnoreCase(ProjectToContestConverterImpl.STUDIO_FILE_TYPE_EXTENSION)) {
            return ((String) theFilter.getValue()).equalsIgnoreCase("Custom");
        } else if (name.equalsIgnoreCase(ProjectToContestConverterImpl.STUDIO_CONTEST_CHANNEL_NAME)) {
            return ((String) theFilter.getValue()).equalsIgnoreCase("JAVA");
        } else if (name.equalsIgnoreCase(ProjectToContestConverterImpl.STUDIO_CONTEST_STATUS_ID)) {
            return ((Long) theFilter.getValue()).longValue() == 3;
        } else if (name.equalsIgnoreCase(ProjectToContestConverterImpl.STUDIO_CONTEST_STATUS_NAME)) {
            return ((String) theFilter.getValue()).equalsIgnoreCase("active");
        }

        return false;
    }

    /**
     * Returns the value of contest.
     *
     * @return the contest
     */
    public Contest getContest() {
        return contest;
    }

    public ContestPayment createContestPayment(ContestPayment contestPayment) throws ContestManagementException {
        // TODO Auto-generated method stub
        return null;
    }

    public Prize createPrize(Prize prize) throws ContestManagementException {
        // TODO Auto-generated method stub
        return null;
    }

    public void editContestPayment(ContestPayment contestPayment) throws ContestManagementException {
        // TODO Auto-generated method stub
        
    }

    public List<ContestProperty> getAllContestProperties() throws ContestManagementException {
        // TODO Auto-generated method stub
        return null;
    }

    public List<ContestType> getAllContestTypes() throws ContestManagementException {
        // TODO Auto-generated method stub
        return null;
    }

    public List<DocumentType> getAllDocumentTypes() throws ContestManagementException {
        // TODO Auto-generated method stub
        return null;
    }

    public List<Medium> getAllMedia() throws ContestManagementException {
        // TODO Auto-generated method stub
        return null;
    }

    public List<MimeType> getAllMimeTypes() throws ContestManagementException {
        // TODO Auto-generated method stub
        return null;
    }

    public List<PaymentStatus> getAllPaymentStatuses() throws ContestManagementException {
        // TODO Auto-generated method stub
        return null;
    }

    public List<PrizeType> getAllPrizeTypes() throws ContestManagementException {
        // TODO Auto-generated method stub
        return null;
    }

    public ContestPayment getContestPayment(long contestPaymentId) throws ContestManagementException {
        // TODO Auto-generated method stub
        return null;
    }

    public ContestProperty getContestProperty(long contestPropertyId) throws ContestManagementException {
        // TODO Auto-generated method stub
        return null;
    }

    public List<Contest> getContestsForUser(long createdUser) throws ContestManagementException {
        // TODO Auto-generated method stub
        return null;
    }

    public DocumentType getDocumentType(long documentTypeId) throws ContestManagementException {
        // TODO Auto-generated method stub
        return null;
    }

    public MimeType getMimeType(long mimeTypeId) throws ContestManagementException {
        // TODO Auto-generated method stub
        return null;
    }

    public PaymentStatus getPaymentStatus(long paymentStatusId) throws ContestManagementException {
        // TODO Auto-generated method stub
        return null;
    }

    public PrizeType getPrizeType(long prizeTypeId) throws ContestManagementException {
        // TODO Auto-generated method stub
        return null;
    }

    public boolean removeContestPayment(long contestPaymentId) throws ContestManagementException {
        // TODO Auto-generated method stub
        return false;
    }
}
