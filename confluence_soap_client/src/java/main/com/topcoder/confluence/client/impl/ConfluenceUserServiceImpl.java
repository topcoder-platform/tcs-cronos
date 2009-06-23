/*
 * Copyright (C) 2008 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.confluence.client.impl;

import com.atlassian.confluence.rpc.RemoteException;
import com.atlassian.confluence.rpc.soap.beans.RemoteUser;
import com.atlassian.confluence.rpc.soap.confluenceservice_v1.ConfluenceSoapServiceProxy;
import com.topcoder.confluence.client.ConfluenceClientServiceException;
import com.topcoder.confluence.client.ConfluenceUserService;
import com.topcoder.confluence.client.RemoteServiceException;
import com.topcoder.confluence.client.UserAlreadyExistsException;

/**
 * Manages users in Confluence using Confluence's SOAP service.
 * 
 * @author romanoTC
 * @version 1.0
 */
public class ConfluenceUserServiceImpl implements ConfluenceUserService {
    /**
     * Verifies if the given handle exists in Confluence.
     * 
     * @param endPoint a URL of the SOAP service.
     * @param adminUserName the name of the user that has permission to list other users.
     * @param adminPassword the password of the admin user.
     * @param handle the login of the user that is going to be verified.
     * @return <code>true</code> if userName already exists in Confluence, <code>false</code> otherwise.
     * @throws ConfluenceClientServiceException if any exception occurs while looking for the user.
     */
    public boolean hasUser(String endPoint, String adminUserName, String adminPassword, String handle)
        throws ConfluenceClientServiceException {

        ConfluenceSoapServiceProxy proxy = new ConfluenceSoapServiceProxy(endPoint);

        String token;

        try {
            try {
                // Log as an administrator (who can create users)
                token = proxy.login(adminUserName.toLowerCase(), adminPassword);
            } catch (RemoteException ex) {
                throw new RemoteServiceException("Unable to login", ex);
            }

            try {
                // Check if user already exists
                return proxy.hasUser(token, handle.toLowerCase());

            } catch (RemoteException ex) {
                throw new RemoteServiceException("A Confluence remote error occurred", ex);
            } finally {
                // tries to logout if an exception occurs
                proxy.logout(token);
            }
        } catch (java.rmi.RemoteException ex) {
            // If this is a RMI exception, it is probably unrecoverable
            throw new ConfluenceClientServiceException("A connection problem occurred", ex);
        }
    }

    /**
     * Returns an existing user in Confluence.
     * 
     * @param endPoint the URL of the SOAP service.
     * @param adminUserName the name of the user that has permission to list other users.
     * @param adminPassword the password of the admin user.
     * @param handle the login of the user that is going to be returned.
     * @return the user (or null if not found).
     * @throws ConfluenceClientServiceException if any exception occurs while looking for the user.
     */
    public RemoteUser getUser(String endPoint, String adminUserName, String adminPassword, String handle)
        throws ConfluenceClientServiceException {

        ConfluenceSoapServiceProxy proxy = new ConfluenceSoapServiceProxy(endPoint);

        String token;

        try {
            try {
                // Log as an administrator (who can create users)
                token = proxy.login(adminUserName.toLowerCase(), adminPassword);
            } catch (RemoteException ex) {
                throw new RemoteServiceException("Unable to login", ex);
            }

            try {
                // Check if user already exists
                return proxy.getUser(token, handle.toLowerCase());

            } catch (RemoteException ex) {
                throw new RemoteServiceException("A Confluence remote error occurred", ex);

            } finally {
                // tries to logout if an exception occurs
                proxy.logout(token);
            }
        } catch (java.rmi.RemoteException ex) {
            // If this is a RMI exception, it is probably unrecoverable
            throw new ConfluenceClientServiceException("A connection problem occurred", ex);
        }
    }

    /**
     * Creates a new user in Confluence.
     * 
     * @param endPoint the URL of the SOAP service.
     * @param adminUserName the name of the user that has permission to create other users.
     * @param adminPassword the password of the admin user.
     * @param userName the login of the user that is going to be created..
     * @param fullName the full name of the user that is going to be created.
     * @param email the e-mail of the user that is going to be created.
     * @param groupsNames default groups to add the user to.
     * @throws RemoteServiceException any exception thrown by Confluence is encapsulated in this exception.
     * @throws UserAlreadyExistsException if the user already exists in Confluence.
     * @throws ConfluenceClientServiceException if any other error occurs while creating the user (such as a
     *             RemoteException).
     */
    public void createUser(String endPoint, String adminUserName, String adminPassword, String userName,
        String fullName, String email) throws ConfluenceClientServiceException {

        ConfluenceSoapServiceProxy proxy = new ConfluenceSoapServiceProxy(endPoint);

        String token;

        try {
            try {
                // Log as an administrator (who can create users)
                token = proxy.login(adminUserName.toLowerCase(), adminPassword);
            } catch (RemoteException ex) {
                throw new RemoteServiceException("Unable to login", ex);
            }

            try {
                // Check if user already exists
                if (proxy.hasUser(token, userName.toLowerCase())) {
                    throw new UserAlreadyExistsException("User [" + userName.toLowerCase() + "] already exists");
                }

                RemoteUser newUser = new RemoteUser();
                newUser.setName(userName.toLowerCase());
                newUser.setFullname(fullName);
                newUser.setEmail(email);

                proxy.addUser(token, newUser, "password");

            } catch (RemoteException ex) {
                throw new RemoteServiceException("A Confluence remote error occurred", ex);
            } finally {
                // tries to logout if an exception occurs
                proxy.logout(token);
            }
        } catch (java.rmi.RemoteException ex) {
            // If this is a RMI exception, it is probably unrecoverable
            throw new ConfluenceClientServiceException("A connection problem occurred", ex);
        }
    }
}