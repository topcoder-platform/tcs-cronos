
package com.topcoder.timetracker.project.ejb;
import com.topcoder.naming.jndiutility.JNDIUtils;
import com.topcoder.timetracker.project.ejb.ProjectWorkerUtilityLocal;
import com.topcoder.util.config.ConfigManager;
/**
 * <p>
 * LocalHome interface for the ProjectWorkerUtility.  It contains only a
 * single no-param create method that produces an instance of the 
 * local interface.  it is used to obtain a handle to the Stateless 
 * SessionBean.
 * </p>
 * <p>
 * Thread Safety:
 *  Implementation will be generated by EJB container and thread-safety
 * is dependent on the container.
 * </p>
 * 
 * @poseidon-object-id [I693c885bm1110ffc43d4m5375]
 */
public interface ProjectWorkerUtilityLocalHome extends javax.ejb.EJBLocalHome {
/**
 * <p>
 * Creates an instance of ProjectWorkerUtilityLocal, which is a 
 * handle to the SessionBean.
 * </p>
 * 
 * @poseidon-object-id [I693c885bm1110ffc43d4m53ad]
 * @return an instance of ProjectWorkerLocal
 * @throws CreateException if the local object or session bean couldn't be created
 */
    public com.topcoder.timetracker.project.ejb.ProjectWorkerUtilityLocal create();
}


