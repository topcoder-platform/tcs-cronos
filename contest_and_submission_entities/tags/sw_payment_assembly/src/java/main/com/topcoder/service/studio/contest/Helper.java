/*
 * Copyright (C) 2008 TopCoder Inc., All Rights Reserved.
 */

package com.topcoder.service.studio.contest;

/**
 * <p>
 * This class provides static utility methods which are used to facilitate the coding or reduce the redundancies.
 * </p>
 *
 * <p>
 * Thread Safety: This class is thread safe since it is immutable.
 * </p>
 *
 * @author cyberjag
 * @version 1.0
 */
public final class Helper {
    /**
     * <p>
     * Private constructor to prevent instantiation of this class.
     * </p>
     */
    private Helper() {
    }

    /**
     * <p>
     * Calculate the hash code.
     * </p>
     *
     * @param ids
     *            the list of ids to be used.
     * @return the calculated hash
     */
    public static int calculateHash(Long... ids) {
        final int prime = 31;
        final int offset = 32;
        int result = 1;
        for (Long id : ids) {
            if (id != null) {
                result = prime * result + (int) (id ^ (id >>> offset));
            }
        }
        return result;
    }
}
