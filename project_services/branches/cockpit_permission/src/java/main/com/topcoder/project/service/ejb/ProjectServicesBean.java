/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.project.service.ejb;

import java.util.List;

import com.topcoder.management.project.Project;
import com.topcoder.management.project.SimpleProjectContestData;
import com.topcoder.management.resource.Resource;
import com.topcoder.security.auth.module.UserProfilePrincipal;
import com.topcoder.management.project.SimpleProjectPermissionData;
import com.topcoder.project.service.ConfigurationException;
import com.topcoder.project.service.ContestSaleData;
import com.topcoder.project.service.FullProjectData;
import com.topcoder.project.service.ProjectServices;
import com.topcoder.project.service.ProjectServicesException;
import com.topcoder.project.service.ProjectServicesFactory;
import com.topcoder.project.service.Util;
import com.topcoder.search.builder.filter.Filter;
import com.topcoder.util.log.Level;
import com.topcoder.util.log.Log;
import com.topcoder.util.log.LogManager;

import javax.annotation.PostConstruct;
import javax.annotation.security.DeclareRoles;
import javax.annotation.security.RolesAllowed;
import javax.annotation.security.RunAs;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

/**
 * <p>
 * This is the Stateless Enterprise bean which wraps all methods of <code>ProjectServices</code>
 * interface. It delegates the business logic to a ProjectServices instance. This instance is
 * retrieved from <code>ProjectServicesFactory</code>, using or not the namespace (it is
 * optional). It has the <b>@Stateless</b> annotation. Stateless refers to javax.ejb.Stateless. It
 * implements both Local and Remote interface. It has also the
 * <b>@TransactionAttribute(TransactionAttributeType.REQUIRED)</b> annotation, to define the
 * required <code>transactions.TransactionAttribute</code> refers to TransactionAttributeType
 * enum. It's sufficient to define this annotation to entire class, it will be applied for default
 * to every method.
 * </p>
 *
 * <p>
 * It needs not to use the <b>@Local</b> and <b>@Remote</b> annotations in this case. It is not
 * recommended to use this approach unless you have to, as implementing the business interfaces
 * directly enforces the contract between the bean class and these interfaces.
 * </p>
 *
 * <p>
 * It will provide streamlined access to project information. It will allow for a simple search for
 * full or basic project information, or to use custom search criteria to locate projects, either in
 * its full or basic form. The basic form involves getting the <code>Project</code> object (from
 * <b>Project Management</b>), and the full form involves the <code>FullProjectData</code>
 * object, which not only provides information as the basic form, but also provides project phase
 * information, all resources participating in the project, and all teams currently existing in it.
 * Furthermore, it provides data about the technologies involved in this project (such as Java, C#).
 * </p>
 *
 * <p>
 * Here is the sample XML deployment descriptor of this stateless bean:
 *
 * <pre>
 * &lt;?xml version=&quot;1.0&quot; encoding=&quot;UTF-8&quot;?&gt;
 *  &lt;ejb-jar xmlns=&quot;http://java.sun.com/xml/ns/javaee&quot;
 *  xmlns:xsi=&quot;http://www.w3.org/2001/XMLSchema-instance&quot;
 *  xsi:schemaLocation=&quot;http://java.sun.com/xml/ns/javaee
 *  http://java.sun.com/xml/ns/javaee/ejb-jar_3_0.xsd&quot;
 *  version=&quot;3.0&quot;&gt;
 *  &lt;enterprise-beans&gt;
 *  &lt;session&gt;
 *  &lt;ejb-name&gt;ProjectServicesBean&lt;/ejb-name&gt;
 *  &lt;env-entry&gt;
 *  &lt;env-entry-name&gt;projectServicesFactoryNamespace&lt;/env-entry-name&gt;
 *  &lt;env-entry-type&gt;java.lang.String&lt;/env-entry-type&gt;
 *  &lt;env-entry-value&gt;ProjectServicesFactory&lt;/env-entry-value&gt;
 *  &lt;/env-entry&gt;
 *  &lt;env-entry&gt;
 *  &lt;env-entry-name&gt;loggerName&lt;/env-entry-name&gt;
 *  &lt;env-entry-type&gt;java.lang.String&lt;/env-entry-type&gt;
 *  &lt;env-entry-value&gt;defaultLogger&lt;/env-entry-value&gt;
 *  &lt;/env-entry&gt;
 *  &lt;/session&gt;
 *  &lt;/enterprise-beans&gt;
 *  &lt;/ejb-jar&gt;
 * </pre>
 *
 * </p>
 * 
 * <p>
 * Module Contest Service Software Contest Sales Assembly change: new methods added to support creating/updating/query contest
 * sale for software contest.
 * </p>
 *
 * <p>
 * Updated for Cockpit Project Admin Release Assembly v1.0: new methods added to support retrieval of project and their permissions.
 * </p>
 *
 * <p>
 * <strong>Thread safety:</strong> It is stateless and it uses a ProjectServices instance which is
 * required to be thread safe.
 * </p>
 *
 * @author fabrizyo, znyyddf, TCSASSEMBLER
 * @version 1.1
 * @since 1.1
 */
@RunAs("Cockpit Administrator")
@RolesAllowed("Cockpit User")
@DeclareRoles( { "Cockpit User", "Cockpit Administrator" })
@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class ProjectServicesBean implements ProjectServicesLocal, ProjectServicesRemote {
	
	/**
     * Private constant specifying user role.
     */
    private static final String USER_ROLE = "Cockpit User";

    /**
     * Private constant specifying administrator role.
     */
    private static final String ADMIN_ROLE = "Cockpit Administrator";
    
    /**
     * <p>
     * Represents the <code>SessionContext</code> injected by the <b>EJB</b> container
     * automatically and is never changed afterwards.
     * </p>
     *
     * <p>
     * It is injected using <b>@Resource</b> annotation, and it can be accessed in the getter
     * method.
     * </p>
     */
    @javax.annotation.Resource
    private SessionContext sessionContext;

    /**
     * <p>
     * Represents the projectServicesFactory namespace used to load the projectServices delegating
     * instance. It is optional and can be null, but can't be empty.
     * </p>
     * <p>
     * It is injected using <b>@Resource(name=&quot;projectServicesFactoryNamespace&quot;)</b>
     * annotation, the env-entry in deployment descriptor will have the
     * &quot;projectServicesFactoryNamespace&quot; name.
     * </p>
     */
    @javax.annotation.Resource(name = "projectServicesFactoryNamespace")
    private String projectServicesFactoryNamespace;

    /**
     * <p>
     * Represents the logger name used to load the Log instance. It is optional and can be null, but
     * can't be empty.
     * </p>
     * <p>
     * It is injected using <b>@Resource(name=&quot;loggerName&quot;)</b> annotation, the env-entry
     * in deployment descriptor will have the &quot;loggerName&quot; name.
     * </p>
     * <p>
     * If it is not present then the logging is not performed. If it is present then the Log
     * instance is created using LogManager.getLog(loggerName). It is retrieved for each method
     * because the bean is stateless.
     * </p>
     */
    @javax.annotation.Resource(name = "loggerName")
    private String logName;

    /**
     * <p>
     * Used extensively in this class to log information. This will include logging method entry and
     * exit, errors, debug information for calls to other components, etc.
     * </p>
     * <p>
     * Note that logging is optional and can be null, in which case, no logging will take place. It
     * will be set in the constructor and will not change.
     * </p>
     */
    private Log logger;

    /**
     * <p>
     * This is the default constructor.
     * </p>
     */
    public ProjectServicesBean() {
        // does nothing
    }

    /**
     * <p>
     * This method is called after this bean is constructed by the EJB container.
     * </p>
     * <p>
     * It assigns the logger field using logName property value, which is assigned by J2EE
     * container.
     * </p>
     */
    @SuppressWarnings("unused")
    @PostConstruct
    private void initialize() {
        logger = logName != null ? LogManager.getLog(logName) : null;
    }

    /**
     * <p>
     * This method finds all active projects along with all known associated information. Returns
     * empty array if no projects found.
     * </p>
     *
     * @return FullProjectData array with full projects info, or empty array if none found
     * @throws ProjectServicesException
     *             If there is a system error while performing the search
     */
    public FullProjectData[] findActiveProjects() {
        String method = "ProjectServicesBean#findActiveProjects() method.";

        Util.log(logger, Level.INFO, "Enters " + method);

        try {
            return getProjectServices().findActiveProjects();
        } catch (ProjectServicesException e) {
            Util.log(logger, Level.ERROR, "ProjectServicesException occurred in " + method);
            throw e;
        } finally {
            Util.log(logger, Level.INFO, "Exits " + method);
        }
    }

    /**
     * <p>
     * This method finds all active projects. Returns empty array if no projects found.
     * </p>
     *
     * @return Project array with project info, or empty array if none found
     * @throws ProjectServicesException
     *             If there is a system error while performing the search
     */
    public Project[] findActiveProjectsHeaders() {
        String method = "ProjectServicesBean#findActiveProjectsHeaders() method.";

        Util.log(logger, Level.INFO, "Enters " + method);

        try {
            return getProjectServices().findActiveProjectsHeaders();
        } catch (ProjectServicesException e) {
            Util.log(logger, Level.ERROR, "ProjectServicesException occurred in " + method);
            throw e;
        } finally {
            Util.log(logger, Level.INFO, "Exits " + method);
        }
    }
    
    /**
     * <p>
     * This method finds projects which have tc direct id.
     * Returns empty array if no projects found.
     * </p>
     *
     * @return Project array with project info, or empty array if none found
     * @throws ProjectServicesException
     *             If there is a system error while performing the search
     */
     
    public Project[] findAllTcDirectProjects() {
    	String method = "ProjectServicesBean#findAllTcDirectProjects() method.";

        Util.log(logger, Level.INFO, "Enters " + method);

        try {
        	  
        	 if (sessionContext.isCallerInRole(ADMIN_ROLE)) {
        		 Util.log(logger, Level.INFO, "User is admin.");
        		 return getProjectServices().findAllTcDirectProjects();	 
        	 } else {
        		 UserProfilePrincipal p = (UserProfilePrincipal) sessionContext.getCallerPrincipal();
        		 Util.log(logger, Level.INFO, "User " + p.getName() + " is non-admin.");
        		 return getProjectServices().findAllTcDirectProjectsForUser(p.getName());   	 
        	 }
        	 
        } catch (ProjectServicesException e) {
            Util.log(logger, Level.ERROR, "ProjectServicesException occurred in " + method);
            throw e;
        } finally {
            Util.log(logger, Level.INFO, "Exits " + method);
        }
    	
    }    
    /**
     * <p>
     * This method finds all given user tc direct projects . Returns empty array if no projects found.
     * </p
     * @param operator 
     * 				The user to search for projects
     * @return   Project array with project info, or empty array if none found
     */
    public Project[] findAllTcDirectProjectsForUser(String user) {
    	String method = "ProjectServicesBean#findAllTcDirectProjectsForUser(user) method.";

        Util.log(logger, Level.INFO, "Enters " + method);

        try {        	  
        	if (sessionContext.isCallerInRole(ADMIN_ROLE)) {
       		 Util.log(logger, Level.INFO, "User is admin.");
       		 return getProjectServices().findAllTcDirectProjectsForUser(user);	 
       	    } 
        	
       		 UserProfilePrincipal p = (UserProfilePrincipal) sessionContext.getCallerPrincipal();
       		 Util.log(logger, Level.INFO, "User " + p.getName() + " is non-admin.");
       		 if(p.equals(user)) {
       			return getProjectServices().findAllTcDirectProjectsForUser(user);	 
       		 } else {
       		   return new Project[0];	 
       		 }    		        	 
        	 
        } catch (ProjectServicesException e) {
            Util.log(logger, Level.ERROR, "ProjectServicesException occurred in " + method);
            throw e;
        } finally {
            Util.log(logger, Level.INFO, "Exits " + method);
        }
    }
    /**
     * <p>
     * This method finds all projects along with all known associated information that match the
     * search criteria. Returns empty array if no projects found.
     * </p>
     *
     * @return FullProjectData array with full projects info, or empty array if none found
     * @param filter
     *            The search criteria to filter projects
     * @throws IllegalArgumentException
     *             If filter is null
     * @throws ProjectServicesException
     *             If there is a system error while performing the search
     */
    public FullProjectData[] findFullProjects(Filter filter) {
        String method = "ProjectServicesBean#findFullProjects(Filter filter) method.";

        Util.log(logger, Level.INFO, "Enters " + method);

        try {
            return getProjectServices().findFullProjects(filter);
        } catch (ProjectServicesException e) {
            Util.log(logger, Level.ERROR, "ProjectServicesException occurred in " + method);
            throw e;
        } finally {
            Util.log(logger, Level.INFO, "Exits " + method);
        }
    }

    /**
     * <p>
     * This method finds all projects that match the search criteria. Returns empty array if no
     * projects found.
     * </p>
     *
     * @return Project array with project info, or empty array if none found
     * @param filter
     *            The search criteria to filter projects
     * @throws IllegalArgumentException
     *             If filter is null
     * @throws ProjectServicesException
     *             If there is a system error while performing the search
     */
    public Project[] findProjectsHeaders(Filter filter) {
        String method = "ProjectServicesBean#findProjectsHeaders(Filter filter) method.";

        Util.log(logger, Level.INFO, "Enters " + method);

        try {
            return getProjectServices().findProjectsHeaders(filter);
        } catch (ProjectServicesException e) {
            Util.log(logger, Level.ERROR, "ProjectServicesException occurred in " + method);
            throw e;
        } finally {
            Util.log(logger, Level.INFO, "Exits " + method);
        }
    }

    /**
     * <p>
     * This method retrieves the project along with all known associated information. Returns null
     * if not found.
     * </p>
     * 
     * <p>
     * Module Contest Service Software Contest Sales Assembly change: fetch the contest sale info.
     * </p>
     *
     * @return the project along with all known associated information
     * @param projectId
     *            The ID of the project to retrieve
     * @throws IllegalArgumentException
     *             If projectId is negative
     * @throws ProjectServicesException
     *             If there is a system error while performing the search
     */
    public FullProjectData getFullProjectData(long projectId) {
        String method = "ProjectServicesBean#getFullProjectData(long projectId) method.";

        Util.log(logger, Level.INFO, "Enters " + method);

        try {
            return getProjectServices().getFullProjectData(projectId);
        } catch (ProjectServicesException e) {
            Util.log(logger, Level.ERROR, "ProjectServicesException occurred in " + method);
            throw e;
        } finally {
            Util.log(logger, Level.INFO, "Exits " + method);
        }
    }

    /**
     * <p>
     * Persist the project and all related data. All ids (of project header, project phases and
     * resources) will be assigned as new, for this reason there is no exception like 'project
     * already exists'.
     * </p>
     * <p>
     * First it persist the projectHeader a com.topcoder.management.project.Project instance. Its
     * properties and associating scorecards, the operator parameter is used as the
     * creation/modification user and the creation date and modification date will be the current
     * date time when the project is created. The id in Project will be ignored: a new id will be
     * created using ID Generator (see Project Management CS). This id will be set to Project
     * instance.
     * </p>
     * <p>
     * Then it persist the phases a com.topcoder.project.phases.Project instance. The id of project
     * header previous saved will be set to project Phases. The phases' ids will be set to 0 (id not
     * set) and then new ids will be created for each phase after persist operation.
     * </p>
     * <p>
     * At last it persist the resources, they can be empty.The id of project header previous saved
     * will be set to resources. The ids of resources' phases ids must be null. See &quot;id problem
     * with resources&quot; thread in design forum. The resources could be empty or null, null is
     * treated like empty: no resources are saved. The resources' ids will be set to UNSET_ID of
     * Resource class and therefore will be persisted as new resources'.
     * </p>
     * 
     * <p>
     * Module Contest Service Software Contest Sales Assembly change: return the wrapped value for project header, phases, resources info.
     * </p>
     *
     * @param projectHeader
     *            the project's header, the main project's data
     * @param projectPhases
     *            the project's phases
     * @param projectResources
     *            the project's resources, can be null or empty, can't contain null values. Null is
     *            treated like empty.
     * @param operator
     *            the operator used to audit the operation, can be null but not empty
     * @return the created project.
     * @throws IllegalArgumentException
     *             if any case in the following occurs:
     *             <ul>
     *             <li>if projectHeader is null;</li>
     *             <li>if projectPhases is null or the phases of projectPhases are empty;</li>
     *             <li>if the project of phases (for each phase: phase.project) is not equal to
     *             projectPhases;</li>
     *             <li>if projectResources contains null entries;</li>
     *             <li>if for each resources: a required field of the resource is not set : if
     *             resource.getResourceRole() is null;</li>
     *             <li>if operator is null or empty;</li>
     *             </ul>
     * @throws ProjectServicesException
     *             if there is a system error while performing the create operation
     */
    public FullProjectData createProject(Project projectHeader, com.topcoder.project.phases.Project projectPhases,
            Resource[] projectResources, String operator) {
        String method = "ProjectServicesBean#createProject(Project projectHeader, com.topcoder.project.phases.Project"
                + " projectPhases, Resource[] projectResources, String operator) method.";

        Util.log(logger, Level.INFO, "Enters " + method);

        try {
            return getProjectServices().createProject(projectHeader, projectPhases, projectResources, operator);
        } catch (ProjectServicesException e) {
            Util.log(logger, Level.ERROR, "ProjectServicesException occurred in " + method);
            throw e;
        } finally {
            Util.log(logger, Level.INFO, "Exits " + method);
        }
    }

    /**
     * <p>
     * Update the project and all related data. First it updates the projectHeader a
     * com.topcoder.management.project.Project instance. All related items will be updated. If items
     * are removed from the project, they will be deleted from the persistence. Likewise, if new
     * items are added, they will be created in the persistence. For the project, its properties and
     * associating scorecards, the operator parameter is used as the modification user and the
     * modification date will be the current date time when the project is updated. See the source
     * code of Project Management component, ProjectManager: there is a 'reason' parameter in
     * updateProject method.
     * </p>
     * <p>
     * Then it updates the phases a com.topcoder.project.phases.Project instance. The id of
     * projectHeader previous saved must be equal to projectPhases' id. The
     * projectPhases.phases.project's id must be equal to projectHeader's id. The phases of the
     * specified project are compared to the phases already in the database. If any new phases are
     * encountered, they are added to the persistent store. If any phases are missing from the
     * input, they are deleted. All other phases are updated.
     * </p>
     * <p>
     * At last it updates the resources, they can be empty. Any resources in the array with UNSET_ID
     * are assigned an id and updated in the persistence. Any resources with an id already assigned
     * are updated in the persistence. Any resources associated with the project in the persistence
     * store, but not appearing in the array are removed. The resource.project must be equal to
     * projectHeader's id. The resources which have a phase id assigned ( a resource could not have
     * the phase id assigned), must have the phase id contained in the projectPhases.phases' ids.
     * </p>
     *
     * @param projectHeader
     *            the project's header, the main project's data
     * @param projectHeaderReason
     *            the reason of projectHeader updating.
     * @param projectPhases
     *            the project's phases
     * @param projectResources
     *            the project's resources, can be null or empty, can't contain null values. Null is
     *            treated like empty.
     * @param operator
     *            the operator used to audit the operation, can be null but not empty
     * @throws IllegalArgumentException
     *             if any case in the following occurs:
     *             <ul>
     *             <li>if projectHeader is null or projectHeader.id is nonpositive;</li>
     *             <li>if projectHeaderReason is null or empty;</li>
     *             <li>if projectPhases is null, or if the phases of projectPhases are empty, or if
     *             the projectPhases.id is not equal to projectHeader.id, or for each phase: if the
     *             phase.object is not equal to projectPhases;</li>
     *             <li>if projectResources contains null entries;</li>
     *             <li>for each resource: if resource.getResourceRole() is null, or if the resource
     *             role is associated with a phase type but the resource is not associated with a
     *             phase, or if the resource.phase (id of phase) is set but it's not in
     *             projectPhases.phases' ids, or if the resource.project (project's id) is not equal
     *             to projectHeader's id;</li>
     *             <li>if operator is null or empty;</li>
     *             </ul>
     * @throws ProjectDoesNotExistException
     *             if the project doesn't exist in persistent store.
     * @throws ProjectServicesException
     *             if there is a system error while performing the update operation
     */
    public FullProjectData updateProject(Project projectHeader, String projectHeaderReason,
            com.topcoder.project.phases.Project projectPhases, Resource[] projectResources, String operator) {
        String method = "ProjectServicesBean#updateProject(Project projectHeader,"
                + " String projectHeaderReason, com.topcoder.project.phases.Project projectPhases,"
                + " Resource[] projectResources, String operator) method.";

        Util.log(logger, Level.INFO, "Enters " + method);

        try {
            return getProjectServices().updateProject(projectHeader, projectHeaderReason, projectPhases, projectResources,
                    operator);
        } catch (ProjectServicesException e) {
            Util.log(logger, Level.ERROR, "ProjectServicesException occurred in " + method);
            throw e;
        } finally {
            Util.log(logger, Level.INFO, "Exits " + method);
        }
    }

    /**
     * <p>
     * Creates a new contest sale and returns the created contest sale.
     * </p>
     *
     * @param contestSaleData the contest sale to create
     *
     * @return the created contest sale.
     *
     * @throws IllegalArgumentException if the arg is null.
     * @throws ProjectServicesException if any other error occurs.
     *
     * @since Module Contest Service Software Contest Sales Assembly
     */
    public ContestSaleData createContestSale(ContestSaleData contestSaleData) throws ProjectServicesException {
        String method = "ProjectServicesBean#createContestSale(ContestSaleData contestSaleData) method.";

        Util.log(logger, Level.INFO, "Enters " + method);

        try {
            return getProjectServices().createContestSale(contestSaleData);
        } catch (ProjectServicesException e) {
            Util.log(logger, Level.ERROR, "ProjectServicesException occurred in " + method);
            throw e;
        } finally {
            Util.log(logger, Level.INFO, "Exits " + method);
        }
    }

    /**
     * <p>
     * Gets contest sale by id, and return the retrieved contest sale. If
     * the contest sale doesn't exist, null is returned.
     * </p>
     *
     * @param contestSaleId the contest sale id
     *
     * @return the retrieved contest sale, or null if id doesn't exist
     *
     * @throws ProjectServicesException if any other error occurs.
     *
     * @since Module Contest Service Software Contest Sales Assembly
     */
    public ContestSaleData getContestSale(long contestSaleId) throws ProjectServicesException {
        String method = "ProjectServicesBean#getContestSale(long contestSaleId) method.";

        Util.log(logger, Level.INFO, "Enters " + method);

        try {
            return getProjectServices().getContestSale(contestSaleId);
        } catch (ProjectServicesException e) {
            Util.log(logger, Level.ERROR, "ProjectServicesException occurred in " + method);
            throw e;
        } finally {
            Util.log(logger, Level.INFO, "Exits " + method);
        }
    }

    /**
     * <p>
     * Gets contest sales by contest id, and return the retrieved contest sales.
     * </p>
     *
     * @param contestId the contest id of the contest sale
     *
     * @return the retrieved contest sales, or empty if none exists
     *
     * @throws ProjectServicesException if any other error occurs.
     *
     * @since Module Contest Service Software Contest Sales Assembly
     */
    public List<ContestSaleData> getContestSales(long contestId) throws ProjectServicesException {
        String method = "ProjectServicesBean#getContestSales(long contestId) method.";

        Util.log(logger, Level.INFO, "Enters " + method);

        try {
            return getProjectServices().getContestSales(contestId);
        } catch (ProjectServicesException e) {
            Util.log(logger, Level.ERROR, "ProjectServicesException occurred in " + method);
            throw e;
        } finally {
            Util.log(logger, Level.INFO, "Exits " + method);
        }
    }

    /**
     * <p>
     * Updates the contest sale data.
     * </p>
     *
     * @param contestSaleData the contest sale to update
     *
     * @throws IllegalArgumentException if the arg is null.
     * @throws ProjectServicesException if any other error occurs.
     *
     * @since Module Contest Service Software Contest Sales Assembly
     */
    public void editContestSale(ContestSaleData contestSaleData) throws ProjectServicesException {
        String method = "ProjectServicesBean#editContestSale(ContestSaleData contestSale) method.";

        Util.log(logger, Level.INFO, "Enters " + method);

        try {
            getProjectServices().editContestSale(contestSaleData);
        } catch (ProjectServicesException e) {
            Util.log(logger, Level.ERROR, "ProjectServicesException occurred in " + method);
            throw e;
        } finally {
            Util.log(logger, Level.INFO, "Exits " + method);
        }
    }

    /**
     * <p>
     * Removes contest sale, return true if the contest sale exists and
     * removed successfully, return false if it doesn't exist.
     * </p>
     *
     * @param contestSaleId the contest sale id
     *
     * @return true if the contest sale exists and removed successfully,
     *         return false if it doesn't exist
     *
     * @throws ProjectServicesException if any other error occurs.
     *
     * @since Module Contest Service Software Contest Sales Assembly
     */
    public boolean removeContestSale(long contestSaleId) throws ProjectServicesException {
        String method = "ProjectServicesBean#removeContestSale(long contestSaleId) method.";

        Util.log(logger, Level.INFO, "Enters " + method);

        try {
            return getProjectServices().removeContestSale(contestSaleId);
        } catch (ProjectServicesException e) {
            Util.log(logger, Level.ERROR, "ProjectServicesException occurred in " + method);
            throw e;
        } finally {
            Util.log(logger, Level.INFO, "Exits " + method);
        }
    }

    /**
     * <p>
     * Return the session context of this bean.
     * </p>
     *
     * @return the session context of this bean.
     */
    protected SessionContext getSessionContext() {
        String method = "ProjectServicesBean#getSessionContext() method.";
        Util.log(logger, Level.INFO, "Enters " + method);
        Util.log(logger, Level.INFO, "Exits " + method);
        return sessionContext;
    }

    /**
     * <p>
     * Gets the projectServices from <code>ProjectServicesFactory</code>.
     * </p>
     *
     * @return the projectServices
     * @throws ProjectServicesException
     *             if any configuration error occurs
     */
    private ProjectServices getProjectServices() {
        ProjectServices projectServices;

        try {
            if (projectServicesFactoryNamespace != null) {
                // If the projectServicesFactoryNamespace is not null then get the projectServices
                // instance from ProjectServicesFactory using namespace
                projectServices = ProjectServicesFactory.getProjectServices(projectServicesFactoryNamespace);
            } else {
                // otherwise get the projectServices instance calling the ProjectServicesFactory's
                // method without namespace
                projectServices = ProjectServicesFactory.getProjectServices();
            }
        } catch (ConfigurationException e) {
            throw new ProjectServicesException(
                    "ConfigurationException occurred while getting ProjectServices from ProjectServicesFactory.", e);
        }

        return projectServices;
    }
    
    /**
     * <p>
     * Persist the project and all related data. All ids (of project header, project phases and
     * resources) will be assigned as new, for this reason there is no exception like 'project
     * already exists'.
     * </p>
     * <p>
     * First it persist the projectHeader a com.topcoder.management.project.Project instance. Its
     * properties and associating scorecards, the operator parameter is used as the
     * creation/modification user and the creation date and modification date will be the current
     * date time when the project is created. The id in Project will be ignored: a new id will be
     * created using ID Generator (see Project Management CS). This id will be set to Project
     * instance.
     * </p>
     * <p>
     * Then it persist the phases a com.topcoder.project.phases.Project instance. The id of project
     * header previous saved will be set to project Phases. The phases' ids will be set to 0 (id not
     * set) and then new ids will be created for each phase after persist operation.
     * </p>
     * <p>
     * At last it persist the resources, they can be empty.The id of project header previous saved
     * will be set to resources. The ids of resources' phases ids must be null. See &quot;id problem
     * with resources&quot; thread in design forum. The resources could be empty or null, null is
     * treated like empty: no resources are saved. The resources' ids will be set to UNSET_ID of
     * Resource class and therefore will be persisted as new resources's.
     * </p>
     *
     * @param projectHeader
     *            the project's header, the main project's data
     * @param projectPhases
     *            the project's phases
     * @param projectResources
     *            the project's resources, can be null or empty, can't contain null values. Null is
     *            treated like empty.
     * @param operator
     *            the operator used to audit the operation, can be null but not empty
     * @throws IllegalArgumentException
     *             if any case in the following occurs:
     *             <ul>
     *             <li>if projectHeader is null;</li>
     *             <li>if projectPhases is null;</li>
     *             <li>if the project of phases (for each phase: phase.project) is not equal to
     *             projectPhases;</li>
     *             <li>if projectResources contains null entries;</li>
     *             <li>if for each resources: a required field of the resource is not set : if
     *             resource.getResourceRole() is null;</li>
     *             <li>if operator is null or empty;</li>
     *             </ul>
     * @throws ProjectServicesException
     *             if there is a system error while performing the create operation
     * @since BUGR-1473
     */
    public FullProjectData createProjectWithTemplate(Project projectHeader, com.topcoder.project.phases.Project projectPhases,
            Resource[] projectResources, String operator) {
        String method = "ProjectServicesBean#createProjectWithTemplate(Project projectHeader, com.topcoder.project.phases.Project"
                + " projectPhases, Resource[] projectResources, String operator) method.";

        Util.log(logger, Level.INFO, "Enters " + method);

        try {
            return getProjectServices().createProjectWithTemplate(projectHeader, projectPhases, projectResources, operator);
        } catch (ProjectServicesException e) {
            Util.log(logger, Level.ERROR, "ProjectServicesException occurred in " + method);
            throw e;
        } finally {
            Util.log(logger, Level.INFO, "Exits " + method);
        }
}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<SimpleProjectContestData> getSimpleProjectContestData()
			throws ProjectServicesException {

		String method = "ProjectServicesBean#getSimpleProjectContestData() method.";

		Util.log(logger, Level.INFO, "Enters " + method);

		try {

			if (sessionContext.isCallerInRole(ADMIN_ROLE)) {

				return getProjectServices().getSimpleProjectContestData();
			} else {
				 UserProfilePrincipal p = (UserProfilePrincipal) sessionContext.getCallerPrincipal();
				 return getProjectServices().getSimpleProjectContestDataByUser(String.valueOf(p.getUserId()));
			}
		} catch (ProjectServicesException e) {
			Util.log(logger, Level.ERROR,
					"ProjectServicesException occurred in " + method);
			throw e;
		} finally {
			Util.log(logger, Level.INFO, "Exits " + method);
		}

	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<SimpleProjectContestData> getSimpleProjectContestData(long pid)
			throws ProjectServicesException {
		String method = "ProjectServicesBean#getSimpleProjectContestData(pid) method.";

		Util.log(logger, Level.INFO, "Enters " + method);

		try {
			return getProjectServices().getSimpleProjectContestData(pid);
		} catch (ProjectServicesException e) {
			Util.log(logger, Level.ERROR,
					"ProjectServicesException occurred in " + method);
			throw e;
		} finally {
			Util.log(logger, Level.INFO, "Exits " + method);
		}
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<SimpleProjectContestData> getSimpleProjectContestDataByUser(
			String user) throws ProjectServicesException {
		String method = "ProjectServicesBean#getSimpleProjectContestDataByUser(user) method.";

		Util.log(logger, Level.INFO, "Enters " + method);

		try {
			return getProjectServices().getSimpleProjectContestDataByUser(user);
		} catch (ProjectServicesException e) {
			Util.log(logger, Level.ERROR,
					"ProjectServicesException occurred in " + method);
			throw e;
		} finally {
			Util.log(logger, Level.INFO, "Exits " + method);
		}
	}
	
	/**
     * <p>
     * Gets the list of project their read/write/full permissions.
     * </p>
     * 
     * @param createdUser
     *            the specified user for which to get the permission
     * @return the list of project their read/write/full permissions.
     * 
     * @throws ProjectServicesException
     *             exception if error during retrieval from persistence.
     * 
     * @since Cockpit Project Admin Release Assembly v1.0
     */
    public List<SimpleProjectPermissionData> getSimpleProjectPermissionDataForUser(long createdUser)
            throws ProjectServicesException {
        String method = "ProjectServicesBean#getSimpleProjectPermissionDataForUser(getSimpleProjectPermissionDataForUser) method.";

        Util.log(logger, Level.INFO, "Enters " + method);

        try {
            return getProjectServices().getSimpleProjectPermissionDataForUser(createdUser);
        } catch (ProjectServicesException e) {
            Util.log(logger, Level.ERROR, "ProjectServicesException occurred in " + method);
            throw e;
        } finally {
            Util.log(logger, Level.INFO, "Exits " + method);
        }
    }
}
