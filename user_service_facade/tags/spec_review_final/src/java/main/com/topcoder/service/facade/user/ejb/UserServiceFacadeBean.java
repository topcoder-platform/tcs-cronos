/*
 * Copyright (C) 2009 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.service.facade.user.ejb;

import java.util.Arrays;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.annotation.security.DeclareRoles;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.jws.WebMethod;
import javax.jws.WebService;

import org.jboss.ws.annotation.EndpointConfig;

import com.topcoder.confluence.client.ConfluenceClientServiceException;
import com.topcoder.confluence.client.ConfluenceUserService;
import com.topcoder.jira.client.JiraClientServiceException;
import com.topcoder.jira.client.JiraUserService;
import com.topcoder.service.Helper;
import com.topcoder.service.user.UserService;
import com.topcoder.service.user.UserServiceException;
import com.topcoder.service.facade.user.UserServiceFacadeException;
import com.topcoder.service.facade.user.UserServiceFacadeLocal;
import com.topcoder.service.facade.user.UserServiceFacadeRemote;
import com.topcoder.util.log.Level;
import com.topcoder.util.log.Log;
import com.topcoder.util.log.LogManager;
import com.topcoder.util.objectfactory.ObjectFactory;

/**
 * <p>
 * This is the implementation class for Jira & Confluence back end service.
 * 
 * This service provides mechanism to create users in both Jira & Confluence.
 * 
 * TopCoder has developed custom login modules for both Jira and Confluence. Basically, all TC's users are
 * Jira/Confluence users in potential.
 * 
 * TC has developed a Jira and Confluence User service facade, that is used in this implementation.
 * </p>
 * 
 * <p>
 * Updated for Jira and Confluence User Sync Widget 1.0
 *  - instead of IllegalArgumentException, UserServiceFacadeException is thrown. IllegalArgumentException can not be serialized as fault in web-service.
 *  - All cases of IllegalArgumentException or tc handle not found, has prefix "TC-HANDLE-NOT-FOUND"
 * </p>
 * 
 * @author snow01, TCSASSEMBLER
 * 
 * @since Jira & Confluence User Sync Service
 * @version 1.0
 */
@RolesAllowed( { "Cockpit User", "Cockpit Administrator" })
@DeclareRoles( { "Cockpit User", "Cockpit Administrator" })
@Stateless
@WebService
@EndpointConfig(configName = "Standard WSSecurity Endpoint")
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class UserServiceFacadeBean implements UserServiceFacadeLocal, UserServiceFacadeRemote {
    /**
     * <p>
     * Represents the default namespace of this class.
     * </p>
     */
    public static final String DEFAULT_NAMESPACE = UserServiceFacadeBean.class.getName();

    /**
     * Represents the config key for object factory namespace - object factory defines & creates implementation classes
     * for JiraUserService and ConfluenceUserService.
     */
    private static final String OBJECT_FACTORY_NAMESPACE = "objectFactoryNamespace";

    /**
     * Represents the config key for JiraUserService
     */
    private static final String JIRA_USER_SERVICE_KEY = "jiraUserServiceKey";

    /**
     * Represents the config key for ConfluenceUserService
     */
    private static final String CONFLUENCE_USER_SERVICE_KEY = "confluenceUserServiceKey";

    /**
     * Represents the config key for Jira User Service WSDL Endpoint URL
     */
    private static final String JIRA_SERVICE_WSDL_ENDPOINT_KEY = "jiraServiceEndPoint";

    /**
     * Represents the config key for Jira User Remote Service Admin User name
     */
    private static final String JIRA_SERVICE_ADMIN_USER_NAME_KEY = "jiraServiceAdminUserName";

    /**
     * Represents the config key for Jira User Remote Service Admin User Password
     */
    private static final String JIRA_SERVICE_ADMIN_USER_PASSWORD_KEY = "jiraServiceAdminUserPassword";

    /**
     * Represents the config key for Confluence User Service WSDL Endpoint URL
     */
    private static final String CONFLUENCE_SERVICE_WSDL_ENDPOINT_KEY = "confluenceServiceEndPoint";

    /**
     * Represents the config key for Confluence User Remote Service Admin User name
     */
    private static final String CONFLUENCE_SERVICE_ADMIN_USER_NAME_KEY = "confluenceServiceAdminUserName";

    /**
     * Represents the config key for Confluence User Remote Service Admin User Password
     */
    private static final String CONFLUENCE_SERVICE_ADMIN_USER_PASSWORD_KEY = "confluenceServiceAdminUserPassword";

    /**
     * Represents the config key for Confluence User Groups for a normal user.
     */
    private static final String NORMAL_USER_CONFLUENCE_GROUPS_KEY = "normalUserConfluenceGroups";

    /**
     * Represents the config key for Confluence User Groups for a admin user.
     */
    private static final String ADMIN_USER_CONFLUENCE_GROUPS_KEY = "adminUserConfluenceGroups";

    /**
     * <p>
     * Represents the sessionContext of the EJB.
     * </p>
     */
    @Resource
    private SessionContext sessionContext;

    /**
     * <p>
     * The configuration manager config namespace that defines various properties used in this class.
     * </p>
     */
    @Resource(name = "configNamespace")
    private String configNamespace;

    /**
     * <p>
     * Represents the tc user service.
     * </p>
     */
    @EJB(name = "ejb/UserService")
    private UserService userService;

    /**
     * <p>
     * Represents the Jira User Service.
     * </p>
     */
    private JiraUserService jiraUserService;

    /**
     * The webservice end point for Jira User Service.
     */
    private String jiraServiceEndPoint;

    /**
     * The jira webservice admin user name.
     */
    private String jiraServiceAdminUserName;

    /**
     * The jira webservice admin user password.
     */
    private String jiraServiceAdminUserPassword;

    /**
     * The webservice end point for Confluence User Service.
     */
    private String confluenceServiceEndPoint;

    /**
     * The confluence webservice admin user name.
     */
    private String confluenceServiceAdminUserName;

    /**
     * The confluence webservice admin user password.
     */
    private String confluenceServiceAdminUserPassword;

    /**
     * <p>
     * Represents the Confluence User Service.
     * </p>
     */
    private ConfluenceUserService confluenceUserService;

    /**
     * <p>
     * Represents the confluence user groups that gets added whenever a normal user gets created in confluence.
     * </p>
     */
    private String[] normalUserConfluenceGroups;

    /**
     * <p>
     * Represents the confluence user groups that gets added whenever a admin user gets created in confluence.
     * </p>
     */
    private String[] adminUserConfluenceGroups;

    /**
     * <p>
     * Represents the loggerName used to retrieve the logger.
     * </p>
     */
    @Resource(name = "logName")
    private String logName;

    /**
     * <p>
     * Represents the log used to log the methods logic of this class.
     * </p>
     */
    private Log logger;

    /**
     * A default empty constructor.
     */
    public UserServiceFacadeBean() {
    }

    /**
     * <p>
     * This is method is performed after the construction of the bean, at this point all the bean's resources will be
     * ready.
     * 
     * It reads config properties and creates various beans using ObjectFactory. It also populates various properties by
     * reading from config properties.
     * </p>
     * 
     * @throws IllegalStateException
     *             On some error this runtime error is thrown.
     */
    @PostConstruct
    public void init() {
        if (logName != null) {
            if (logName.trim().length() == 0) {
                throw new IllegalStateException("logName parameter not supposed to be empty.");
            }

            logger = LogManager.getLog(logName);
        }

        // first record in logger
        logExit("init");

        if (configNamespace == null) {
            configNamespace = DEFAULT_NAMESPACE;
        }

        logDebug("Config Namespace: " + configNamespace);

        try {
            // gets OBJECT_FACTORY_NAMESPACE for ConfigManagerSpecificationFactory
            String objectFactoryNamespace = Helper.getStringPropertyValue(configNamespace, OBJECT_FACTORY_NAMESPACE,
                    true);

            // creates an instance of ObjectFactory
            ObjectFactory objectFactory = Helper.getObjectFactory(objectFactoryNamespace);

            // get the jira user service key value.
            String jiraUserServiceKey = Helper.getStringPropertyValue(configNamespace, JIRA_USER_SERVICE_KEY, true);

            // create the instance of JiraUserService using jiraUserServiceKey value.
            this.jiraUserService = (JiraUserService) Helper.createObject(objectFactory, jiraUserServiceKey,
                    JiraUserService.class);

            // get the confluence user service key value.
            String confluenceUserServiceKey = Helper.getStringPropertyValue(configNamespace,
                    CONFLUENCE_USER_SERVICE_KEY, true);

            // create the instance of ConfluenceUserService using confluenceUserServiceKey value.
            this.confluenceUserService = (ConfluenceUserService) Helper.createObject(objectFactory,
                    confluenceUserServiceKey, ConfluenceUserService.class);

            // get the value for jira service end point
            this.jiraServiceEndPoint = Helper.getStringPropertyValue(configNamespace, JIRA_SERVICE_WSDL_ENDPOINT_KEY,
                    true);

            // get the value for jira admin user.
            this.jiraServiceAdminUserName = Helper.getStringPropertyValue(configNamespace,
                    JIRA_SERVICE_ADMIN_USER_NAME_KEY, true);

            // get the value for jira admin password.
            this.jiraServiceAdminUserPassword = Helper.getStringPropertyValue(configNamespace,
                    JIRA_SERVICE_ADMIN_USER_PASSWORD_KEY, true);

            // get the value for confluence service end point
            this.confluenceServiceEndPoint = Helper.getStringPropertyValue(configNamespace,
                    CONFLUENCE_SERVICE_WSDL_ENDPOINT_KEY, true);

            // get the value for confluence admin user.
            this.confluenceServiceAdminUserName = Helper.getStringPropertyValue(configNamespace,
                    CONFLUENCE_SERVICE_ADMIN_USER_NAME_KEY, true);

            // get the value for confluence admin password.
            this.confluenceServiceAdminUserPassword = Helper.getStringPropertyValue(configNamespace,
                    CONFLUENCE_SERVICE_ADMIN_USER_PASSWORD_KEY, true);

            // get the value for confluence groups for normal users (it's comma separated values)
            this.normalUserConfluenceGroups = Helper.getStringPropertyValue(configNamespace,
                    NORMAL_USER_CONFLUENCE_GROUPS_KEY, true).split(",");

            // get the value for confluence groups for admin users (it's comma separated values)
            this.adminUserConfluenceGroups = Helper.getStringPropertyValue(configNamespace,
                    ADMIN_USER_CONFLUENCE_GROUPS_KEY, true).split(",");
        } catch (Exception e) {
            logError(e, "Error during init()");
            throw new IllegalStateException("Failed to execute the Post Constructor init() method", e);
        }
    }

    /**
     * <p>
     * Creates Jira User (if does not exist already) and gets the email address of it from the Jira Service.
     * 
     * Implementation should create the Jira user if the user does not exist already.
     * </p>
     * 
     * <p>
     * Updated for Jira and Confluence User Sync Widget 1.0
     *  - instead of IllegalArgumentException, UserServiceFacadeException is thrown. IllegalArgumentException can not be serialized as fault in web-service.
     *  - All cases of IllegalArgumentException or tc handle not found, has prefix "TC-HANDLE-NOT-FOUND"
     * </p>
     * 
     * @param userHandle
     *            the user handle for which to retrieve the email address from Jira Service.
     * @return the email address of the Jira user for the given handle.
     * @throws UserServiceFacadeException
     *             if any error occurs when getting user details.
     */
    @WebMethod
    public String getJiraUser(String userHandle) throws UserServiceFacadeException {
        String ret = null;
        try {
            logEnter("getJiraUser(userHandle)", userHandle);

            // Check handle is null or trim'd empty and throw IllegalArgumentException
            Helper.checkNull(userHandle, "userHandle");
            Helper.checkEmpty(userHandle, "userHandle");

            // Check if handle is actually a TopCoder user and throw IllegalArgumentException if not
            String email = this.userService.getEmailAddress(userHandle);
            logDebug("For handle: " + userHandle + " email: " + email);
            if (email == null) {
                throw wrapUserServiceFacadeException("TC-HANDLE-NOT-FOUND: The user handle '" + userHandle + "' should be a valid TC user.");
            }

            // Call JiraUserService#getUser and return the remoteUser#email
            com.atlassian.jira.rpc.soap.beans.RemoteUser jiraUser = this.jiraUserService.getUser(
                    this.jiraServiceEndPoint, this.jiraServiceAdminUserName, this.jiraServiceAdminUserPassword,
                    userHandle);

            logDebug("For handle: " + userHandle + " jiraUser: " + jiraUser);

            if (jiraUser != null) {
                ret = jiraUser.getEmail();
            }
        } catch (IllegalArgumentException e) {
            throw wrapUserServiceFacadeException(e, "TC-HANDLE-NOT-FOUND: IllegalArgumentException.");
        } catch (IllegalStateException e) {
            throw wrapUserServiceFacadeException(e, "IllegalStateException.");
        } catch (UserServiceException e) {
            throw wrapUserServiceFacadeException(e, "Error in UserService.");
        } catch (JiraClientServiceException e) {
            throw wrapUserServiceFacadeException(e, "Error in JiraUserService.");
        } finally {
            logExit("getJiraUser(userHandle)", ret);
        }
        
        if (ret == null) {
            throw wrapUserServiceFacadeException("Could not create Jira User");
        }

        return ret;
    }

    /**
     * <p>
     * Creates Confluence User (if does not exist already) and gets the email address of it from the Confluence Service.
     * 
     * Implementation should create the Confluence user if the user does not exist already.
     * </p>
     * 
     * <p>
     * Updated for Jira and Confluence User Sync Widget 1.0
     *  - instead of IllegalArgumentException, UserServiceFacadeException is thrown. IllegalArgumentException can not be serialized as fault in web-service.
     *  - All cases of IllegalArgumentException or tc handle not found, has prefix "TC-HANDLE-NOT-FOUND"
     * </p>
     * 
     * @param userHandle
     *            the user handle for which to retrieve the email address from Confluence Service.
     * @return the email address of the Confluence user for the given handle.
     * @throws UserServiceFacadeException
     *             if any error occurs when getting user details.
     */
    @WebMethod
    public String getConfluenceUser(String userHandle) throws UserServiceFacadeException {
        String ret = null;
        try {
            logEnter("getConfluenceUser(userHandle)", userHandle);

            // Check handle is null or trim'd empty and throw IllegalArgumentException
            Helper.checkNull(userHandle, "userHandle");
            Helper.checkEmpty(userHandle, "userHandle");

            // Check if handle is actually a TopCoder user and throw IllegalArgumentException if not
            String email = this.userService.getEmailAddress(userHandle);
            logDebug("For handle: " + userHandle + " email: " + email);
            if (email == null) {
                throw wrapUserServiceFacadeException("TC-HANDLE-NOT-FOUND: The user handle '" + userHandle + "' should be a valid TC user.");
            }

            boolean hasUser = this.confluenceUserService.hasUser(this.confluenceServiceEndPoint,
                    this.confluenceServiceAdminUserName, this.confluenceServiceAdminUserPassword, userHandle);
            logDebug("For handle: " + userHandle + " confluence user exists: " + hasUser);
            if (!hasUser) {
                String[] groups = null;
                if (this.userService.isAdmin(userHandle)) {
                    groups = adminUserConfluenceGroups;
                } else {
                    groups = normalUserConfluenceGroups;
                }

                logDebug("For handle: " + userHandle + " confluence groups are: " + Arrays.toString(groups));

                this.confluenceUserService.createUser(this.confluenceServiceEndPoint,
                        this.confluenceServiceAdminUserName, this.confluenceServiceAdminUserPassword, userHandle,
                        email, groups);
            }

            com.atlassian.confluence.rpc.soap.beans.RemoteUser confluenceUser = this.confluenceUserService.getUser(
                    this.confluenceServiceEndPoint, this.confluenceServiceAdminUserName,
                    this.confluenceServiceAdminUserPassword, userHandle);

            logDebug("For handle: " + userHandle + " confluenceUser: " + confluenceUser);

            if (confluenceUser != null) {
                ret = confluenceUser.getEmail();
            }
        } catch (IllegalArgumentException e) {
            throw wrapUserServiceFacadeException(e, "TC-HANDLE-NOT-FOUND: IllegalArgumentException.");
        } catch (IllegalStateException e) {
            throw wrapUserServiceFacadeException(e, "IllegalStateException.");
        } catch (UserServiceException e) {
            throw wrapUserServiceFacadeException(e, "Error in UserService.");
        } catch (ConfluenceClientServiceException e) {
            throw wrapUserServiceFacadeException(e, "Error in ConfluenceUserService.");
        } finally {
            logExit("getConfluenceUser(userHandle)", ret);
        }
        
        if (ret == null) {
            throw wrapUserServiceFacadeException("Could not create Confluence User");
        }

        return ret;
    }

    /**
     * <p>
     * This method used to log enter in method. It will persist both method name and it's parameters if any.
     * </p>
     * 
     * @param method
     *            name of the entered method
     * @param params
     *            array containing parameters used to invoke method
     */
    private void logEnter(String method, Object... params) {
        if (logger != null) {
            logger.log(Level.DEBUG, "Enter method UserSyncServiceBean.{0} with parameters {1}.", method, Arrays
                    .deepToString(params));
        }
    }

    /**
     * <p>
     * This method used to log arbitrary debug message. It will persist debug message.
     * </p>
     * 
     * @param msg
     *            message information
     */
    private void logDebug(String msg) {
        if (logger != null) {
            logger.log(Level.DEBUG, msg);
        }
    }

    /**
     * <p>
     * This method used to log leave of method. It will persist method name.
     * </p>
     * 
     * @param method
     *            name of the leaved method
     */
    private void logExit(String method) {
        if (logger != null) {
            logger.log(Level.DEBUG, "Leave method {0}.", method);
        }
    }

    /**
     * <p>
     * This method used to log leave of method. It will persist method name.
     * </p>
     * 
     * @param method
     *            name of the leaved method
     * @param returnValue
     *            value returned from the method
     */
    private void logExit(String method, Object returnValue) {
        if (logger != null) {
            logger.log(Level.DEBUG, "Leave method {0} with return value {1}.", method, returnValue);
        }
    }

    /**
     * <p>
     * This method used to log arbitrary error. It will persist error's data.
     * </p>
     * 
     * @param error
     *            exception describing error
     * @param message
     *            additional message information
     */
    private void logError(Throwable error, String message) {
        if (error == null) {
            logError(message);
        }
        if (logger != null) {
            logger.log(Level.ERROR, error, message);
        }
    }

    /**
     * <p>
     * This method used to log arbitrary error. It will persist error's data.
     * </p>
     * 
     * @param message
     *            message information
     */
    private void logError(String message) {
        if (logger != null) {
            logger.log(Level.ERROR, message);
        }
    }

    /**
     * <p>
     * Creates a <code>UserServiceFacadeException</code> with inner exception and message. It will log the exception, and
     * set the sessionContext to rollback only.
     * </p>
     * 
     * @param e
     *            the inner exception
     * @param message
     *            the error message
     * @return the created exception
     */
    private UserServiceFacadeException wrapUserServiceFacadeException(Exception e, String message) {
        UserServiceFacadeException ce = new UserServiceFacadeException(message, e);
        logError(ce, message);
        sessionContext.setRollbackOnly();

        return ce;
    }
    
    /**
     * <p>
     * Creates a <code>UserServiceFacadeException</code> with inner exception and message. It will log the exception, and
     * set the sessionContext to rollback only.
     * </p>
     * 
     * @param message
     *            the error message
     * @return the created exception
     */
    private UserServiceFacadeException wrapUserServiceFacadeException(String message) {
        UserServiceFacadeException ce = new UserServiceFacadeException(message);
        logError(ce, message);
        sessionContext.setRollbackOnly();

        return ce;
    }
}