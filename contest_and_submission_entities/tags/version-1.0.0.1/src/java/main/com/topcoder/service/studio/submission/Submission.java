/*
 * Copyright (C) 2008 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.service.studio.submission;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import com.topcoder.service.studio.contest.Contest;
import com.topcoder.service.studio.contest.FilePath;
import com.topcoder.service.studio.contest.Helper;
import com.topcoder.service.studio.contest.MimeType;

/**
 * <p>
 * Represents the entity class for db table <i>submission</i>.
 * </p>
 *
 * <p>
 * Thread Safety: This entity is not thread safe since it is mutable.
 * </p>
 *
 * @author tushak, cyberjag
 * @version 1.0
 */
public class Submission implements Serializable {
    /**
     * Generated serial version id.
     */
    private static final long serialVersionUID = -2936624282604563363L;

    /**
     * Represents the entity id.
     */
    private Long submissionId;

    /**
     * Represents the id of submitter.
     */
    private Long submitterId;

    /**
     * Represents the contest of submission.
     */
    private Contest contest;

    /**
     * Represents the original file name.
     */
    private String originalFileName;

    /**
     * Represents the system file name.
     */
    private String systemFileName;

    /**
     * Represents the link for full submission.
     */
    private FilePath fullSubmissionPath;

    /**
     * Represents the submission type.
     */
    private SubmissionType type;

    /**
     * Represents the mime type.
     */
    private MimeType mimeType;

    /**
     * Represents the review status.
     */
    private List<SubmissionReview> review;

    /**
     * Represents the link to contest result.
     */
    private ContestResult result;

    /**
     * Represents the submission rank.
     */
    private Integer rank;

    /**
     * Represents the created date.
     */
    private Date createDate;

    /**
     * Represents the modify date.
     */
    private Date modifyDate;

    /**
     * Represents the height.
     */
    private Integer height;

    /**
     * Represents the width.
     */
    private Integer width;

    /**
     * Represents the submission status.
     */
    private SubmissionStatus status;

    /**
     * Represents the prizes for submission.
     */
    private Set<Prize> prizes = new TreeSet<Prize>();

    /**
     * Represents the submission from model.
     */
    private Long orSubmission;

    /**
     * Represents the submission date.
     */
    private Date submissionDate;

    /**
     * Default constructor.
     */
    public Submission() {
        // empty
    }

    /**
     * Returns the submissionId.
     *
     * @return the submissionId.
     */
    public Long getSubmissionId() {
        return submissionId;
    }

    /**
     * Updates the submissionId with the specified value.
     *
     * @param submissionId
     *            the submissionId to set.
     */
    public void setSubmissionId(Long submissionId) {
        this.submissionId = submissionId;
    }

    /**
     * Returns the submitterId.
     *
     * @return the submitterId.
     */
    public Long getSubmitterId() {
        return submitterId;
    }

    /**
     * Updates the submitterId with the specified value.
     *
     * @param submitterId
     *            the submitterId to set.
     */
    public void setSubmitterId(Long submitterId) {
        this.submitterId = submitterId;
    }

    /**
     * Returns the contest.
     *
     * @return the contest.
     */
    public Contest getContest() {
        return contest;
    }

    /**
     * Updates the contest with the specified value.
     *
     * @param contest
     *            the contest to set.
     */
    public void setContest(Contest contest) {
        this.contest = contest;
    }

    /**
     * Returns the originalFileName.
     *
     * @return the originalFileName.
     */
    public String getOriginalFileName() {
        return originalFileName;
    }

    /**
     * Updates the originalFileName with the specified value.
     *
     * @param originalFileName
     *            the originalFileName to set.
     */
    public void setOriginalFileName(String originalFileName) {
        this.originalFileName = originalFileName;
    }

    /**
     * Returns the systemFileName.
     *
     * @return the systemFileName.
     */
    public String getSystemFileName() {
        return systemFileName;
    }

    /**
     * Updates the systemFileName with the specified value.
     *
     * @param systemFileName
     *            the systemFileName to set.
     */
    public void setSystemFileName(String systemFileName) {
        this.systemFileName = systemFileName;
    }

    /**
     * Returns the fullSubmissionPath.
     *
     * @return the fullSubmissionPath.
     */
    public FilePath getFullSubmissionPath() {
        return fullSubmissionPath;
    }

    /**
     * Updates the fullSubmissionPath with the specified value.
     *
     * @param fullSubmissionPath
     *            the fullSubmissionPath to set.
     */
    public void setFullSubmissionPath(FilePath fullSubmissionPath) {
        this.fullSubmissionPath = fullSubmissionPath;
    }

    /**
     * Returns the type.
     *
     * @return the type.
     */
    public SubmissionType getType() {
        return type;
    }

    /**
     * Updates the type with the specified value.
     *
     * @param type
     *            the type to set.
     */
    public void setType(SubmissionType type) {
        this.type = type;
    }

    /**
     * Returns the mimeType.
     *
     * @return the mimeType.
     */
    public MimeType getMimeType() {
        return mimeType;
    }

    /**
     * Updates the mimeType with the specified value.
     *
     * @param mimeType
     *            the mimeType to set.
     */
    public void setMimeType(MimeType mimeType) {
        this.mimeType = mimeType;
    }

    /**
     * Returns the review.
     *
     * @return the review.
     */
    public List<SubmissionReview> getReview() {
        return review;
    }

    /**
     * Updates the review with the specified value.
     *
     * @param review
     *            the review to set.
     */
    public void setReview(List<SubmissionReview> review) {
        this.review = review;
    }

    /**
     * Returns the result.
     *
     * @return the result.
     */
    public ContestResult getResult() {
        return result;
    }

    /**
     * Updates the result with the specified value.
     *
     * @param result
     *            the result to set.
     */
    public void setResult(ContestResult result) {
        this.result = result;
    }

    /**
     * Returns the rank.
     *
     * @return the rank.
     */
    public Integer getRank() {
        return rank;
    }

    /**
     * Updates the rank with the specified value.
     *
     * @param rank
     *            the rank to set.
     */
    public void setRank(Integer rank) {
        this.rank = rank;
    }

    /**
     * Returns the createDate.
     *
     * @return the createDate.
     */
    public Date getCreateDate() {
        return createDate;
    }

    /**
     * Updates the createDate with the specified value.
     *
     * @param createDate
     *            the createDate to set.
     */
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    /**
     * Returns the height.
     *
     * @return the height.
     */
    public Integer getHeight() {
        return height;
    }

    /**
     * Updates the height with the specified value.
     *
     * @param height
     *            the height to set.
     */
    public void setHeight(Integer height) {
        this.height = height;
    }

    /**
     * Returns the width.
     *
     * @return the width.
     */
    public Integer getWidth() {
        return width;
    }

    /**
     * Updates the width with the specified value.
     *
     * @param width
     *            the width to set.
     */
    public void setWidth(Integer width) {
        this.width = width;
    }

    /**
     * Returns the status.
     *
     * @return the status.
     */
    public SubmissionStatus getStatus() {
        return status;
    }

    /**
     * Updates the status with the specified value.
     *
     * @param status
     *            the status to set.
     */
    public void setStatus(SubmissionStatus status) {
        this.status = status;
    }

    /**
     * Returns the prizes.
     *
     * @return the prizes.
     */
    public Set<Prize> getPrizes() {
        return prizes;
    }

    /**
     * Updates the prizes with the specified value.
     *
     * @param prizes
     *            the prizes to set.
     */
    public void setPrizes(Set<Prize> prizes) {
        this.prizes = prizes;
    }

    /**
     * Returns the oRSubmission.
     *
     * @return the oRSubmission.
     */
    public Long getOrSubmission() {
        return orSubmission;
    }

    /**
     * Updates the oRSubmission with the specified value.
     *
     * @param submission
     *            the oRSubmission to set.
     */
    public void setOrSubmission(Long submission) {
        orSubmission = submission;
    }

    /**
     * Returns the submissionDate.
     *
     * @return the submissionDate.
     */
    public Date getSubmissionDate() {
        return submissionDate;
    }

    /**
     * Updates the submissionDate with the specified value.
     *
     * @param submissionDate
     *            the submissionDate to set.
     */
    public void setSubmissionDate(Date submissionDate) {
        this.submissionDate = submissionDate;
    }

    /**
     * Returns the modifyDate.
     *
     * @return the modifyDate.
     */
    public Date getModifyDate() {
        return modifyDate;
    }

    /**
     * Updates the modifyDate with the specified value.
     *
     * @param modifyDate
     *            the modifyDate to set.
     */
    public void setModifyDate(Date modifyDate) {
        this.modifyDate = modifyDate;
    }

    /**
     * Compares this object with the passed object for equality. Only the id will be compared.
     *
     * @param obj
     *            the {@code Object} to compare to this one
     * @return true if this object is equal to the other, {@code false} if not
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Submission) {
            return getSubmissionId() == ((Submission) obj).getSubmissionId();
        }
        return false;
    }

    /**
     * Overrides {@code Object.hashCode()} to provide a hash code consistent with this class's
     * {@link #equals(Object)}} method.
     *
     * @return a hash code for this {@code Submission}
     */
    @Override
    public int hashCode() {
        return Helper.calculateHash(submissionId);
    }
}
