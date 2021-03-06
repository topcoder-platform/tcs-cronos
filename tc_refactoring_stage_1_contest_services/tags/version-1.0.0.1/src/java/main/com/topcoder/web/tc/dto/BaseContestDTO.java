/*
 * Copyright (C) 2011 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.web.tc.dto;

/**
 * <p>
 * This is a simple container class that defines the common properties of active contest and past contest.
 * </p>
 * <p>
 * Thread Safety: This class is not thread-safe because it's mutable.
 * </p>
 *
 * @author mekanizumu, TCSDEVELOPER
 * @version 1.0
 */
public abstract class BaseContestDTO {
    /**
     * <p>
     * The contest type. It has both getter and setter. It can be any value. It does not need to be
     * initialized when the instance is created. It is used in getType(), setType().
     * </p>
     */
    private String type;

    /**
     * <p>
     * The contest sub-type. It has both getter and setter. It can be any value. It does not need to be
     * initialized when the instance is created. It is used in setSubType(), getSubType().
     * </p>
     */
    private String subType;

    /**
     * <p>
     * The contest catalog. It has both getter and setter. It can be any value. It does not need to be
     * initialized when the instance is created. It is used in setCatalog(), getCatalog().
     * </p>
     */
    private String catalog;

    /**
     * <p>
     * The contest name. It has both getter and setter. It can be any value. It does not need to be
     * initialized when the instance is created. It is used in getContestName(), setContestName().
     * </p>
     */
    private String contestName;

    /**
     * <p>
     * The number of submissions. It has both getter and setter. It can be any value. It does not need to be
     * initialized when the instance is created. It is used in getNumberOfSubmissions(),
     * setNumberOfSubmissions().
     * </p>
     */
    private int numberOfSubmissions;

    /**
     * <p>
     * The number of registrants. It has both getter and setter. It can be any value. It does not need to be
     * initialized when the instance is created. It is used in setNumberOfRegistrants(),
     * getNumberOfRegistrants().
     * </p>
     */
    private int numberOfRegistrants;

    /**
     * <p>
     * This is the default constructor for the class.
     * </p>
     */
    protected BaseContestDTO() {
        // Empty
    }

    /**
     * <p>
     * Getter method for type, simply return the value of the namesake field.
     * </p>
     *
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * <p>
     * Setter method for the type, simply set the value to the namesake field.
     * </p>
     *
     * @param type
     *            The contest type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * <p>
     * Getter method for subType, simply return the value of the namesake field.
     * </p>
     *
     * @return the subType
     */
    public String getSubType() {
        return subType;
    }

    /**
     * <p>
     * Setter method for the subType, simply set the value to the namesake field.
     * </p>
     *
     * @param subType
     *            The contest sub-type
     */
    public void setSubType(String subType) {
        this.subType = subType;
    }

    /**
     * <p>
     * Getter method for catalog, simply return the value of the namesake field.
     * </p>
     *
     * @return the catalog
     */
    public String getCatalog() {
        return catalog;
    }

    /**
     * <p>
     * Setter method for the catalog, simply set the value to the namesake field.
     * </p>
     *
     * @param catalog
     *            The contest catalog
     */
    public void setCatalog(String catalog) {
        this.catalog = catalog;
    }

    /**
     * <p>
     * Getter method for contestName, simply return the value of the namesake field.
     * </p>
     *
     * @return the contestName
     */
    public String getContestName() {
        return contestName;
    }

    /**
     * <p>
     * Setter method for the contestName, simply set the value to the namesake field.
     * </p>
     *
     * @param contestName
     *            The contest name
     */
    public void setContestName(String contestName) {
        this.contestName = contestName;
    }

    /**
     * <p>
     * Getter method for numberOfSubmissions, simply return the value of the namesake field.
     * </p>
     *
     * @return the numberOfSubmissions
     */
    public int getNumberOfSubmissions() {
        return numberOfSubmissions;
    }

    /**
     * <p>
     * Setter method for the numberOfSubmissions, simply set the value to the namesake field.
     * </p>
     *
     * @param numberOfSubmissions
     *            The number of submissions
     */
    public void setNumberOfSubmissions(int numberOfSubmissions) {
        this.numberOfSubmissions = numberOfSubmissions;
    }

    /**
     * <p>
     * Getter method for numberOfRegistrants, simply return the value of the namesake field.
     * </p>
     *
     * @return the numberOfRegistrants
     */
    public int getNumberOfRegistrants() {
        return numberOfRegistrants;
    }

    /**
     * <p>
     * Setter method for the numberOfRegistrants, simply set the value to the namesake field.
     * </p>
     *
     * @param numberOfRegistrants
     *            The number of registrants
     */
    public void setNumberOfRegistrants(int numberOfRegistrants) {
        this.numberOfRegistrants = numberOfRegistrants;
    }
}
