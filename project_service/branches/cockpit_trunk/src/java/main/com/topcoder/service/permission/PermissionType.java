/*
 * Copyright (C) 2009 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.service.permission;

import java.io.Serializable;

/**
 * Represents the entity class for permission type.
 * 
 * @author PE
 * @version 1.0
 * 
 * @since Module Cockpit Contest Service Enhancement Assembly
 */
@SuppressWarnings("serial")
public class PermissionType implements Serializable {
    /** Represents the entity id. */
    private Long permissionTypeId;

    /** Represents the name of permission type. */
    private String name;

    public static final long PERMISSION_TYPE_PROJECT_READ = 1;

    public static final long PERMISSION_TYPE_PROJECT_FULL = 3;


    /**
     * Default constructor.
     */
    public PermissionType() {
        // empty
    }

    /**
     * Gets the permissionTypeId.
     *
     * @return the permissionTypeId.
     */
    public Long getPermissionTypeId() {
        return permissionTypeId;
    }

    /**
     * Sets the permissionTypeId.
     *
     * @param permissionTypeId the permissionTypeId to set.
     */
    public void setPermissionTypeId(Long permissionTypeId) {
        this.permissionTypeId = permissionTypeId;
    }

    /**
     * Gets the name.
     *
     * @return the name.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name.
     *
     * @param name the name to set.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Compares this object with the passed object for equality. Only the id will be compared.
     *
     * @param obj the {@code Object} to compare to this one
     *
     * @return true if this object is equal to the other, {@code false} if not
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof PermissionType) {
            if (getPermissionTypeId() == null) {
                return (((PermissionType) obj).getPermissionTypeId() == null);
            }

            return getPermissionTypeId().equals(((PermissionType) obj).getPermissionTypeId());
        }

        return false;
    }

    /**
     * Overrides {@code Object.hashCode()} to provide a hash code consistent with this class's {@link #equals(Object)}}
     * method.
     *
     * @return a hash code for this {@code PermissionType}
     */
    @Override
    public int hashCode() {
        return Helper.calculateHash(permissionTypeId);
    }
}
