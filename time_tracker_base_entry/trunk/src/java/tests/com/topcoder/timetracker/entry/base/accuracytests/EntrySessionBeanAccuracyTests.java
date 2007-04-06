/*
 * Copyright (C) 2006 Topcoder Inc., All Rights Reserved.
 */
package com.topcoder.timetracker.entry.base.accuracytests;

import com.mockrunner.mock.ejb.MockUserTransaction;

import com.topcoder.timetracker.entry.base.BaseEntry;
import com.topcoder.timetracker.entry.base.CutoffTimeBean;
import com.topcoder.timetracker.entry.base.EntryManagerException;

import com.topcoder.timetracker.entry.base.ejb.EntryLocalHome;
import com.topcoder.timetracker.entry.base.ejb.EntryLocalObject;
import com.topcoder.timetracker.entry.base.ejb.EntrySessionBean;

import junit.framework.TestCase;

import org.mockejb.MockContainer;
import org.mockejb.SessionBeanDescriptor;

import org.mockejb.jndi.MockContextFactory;

import java.sql.Connection;
import java.util.Calendar;
import java.util.Date;

import javax.naming.Context;
import javax.naming.InitialContext;

import javax.rmi.PortableRemoteObject;


/**
 * Test cases for the EntryLocalObject and EntrySessionBean.
 *
 * @since Apr 1, 2007
 * @author waits
 * @version 1.0
 */
public class EntrySessionBeanAccuracyTests extends TestCase {
    /** Session bean to test. */
    private EntryLocalObject entryLocalObject = null;

    /** State of this test case. These variables are initialized by setUp method */
    private Context context;

    /** mock user trnasaction. */
    private MockUserTransaction mockTransaction;

    /**
     * Database connection.
     */
    private Connection conn = null;
    
    /**
     * Setup the environement for testing.
     */
    protected void setUp() throws Exception {
        // clear the talble and setup the environment.
    	TestHelper.clearConfiguration();
    	TestHelper.setUpConfiguration();
    	conn = TestHelper.getConnection();
        TestHelper.clearTable(conn);
        TestHelper.insertData(conn);

        /*
         * We need to set MockContextFactory as our JNDI provider. This method sets the necessary system properties.
         */
        MockContextFactory.setAsInitial();

        // create the initial context that will be used for binding EJBs
        context = new InitialContext();

        // Create an instance of the MockContainer
        MockContainer mockContainer = new MockContainer(context);

        /*
         * Create deployment descriptor of our sample bean. MockEjb uses it instead of XML deployment descriptors
         */
        SessionBeanDescriptor sampleServiceDescriptor = new SessionBeanDescriptor("timetracker/entryejb",
                EntryLocalHome.class, EntryLocalObject.class, new EntrySessionBean());
        // Deploy operation creates Home and binds it to JNDI
        mockContainer.deploy(sampleServiceDescriptor);

        // Lookup the home
        Object entryLocalHomeObj = context.lookup("timetracker/entryejb");

        // PortableRemoteObject does not do anything in MockEJB but it does no harm to call it
        EntryLocalHome entryLocalHome = (EntryLocalHome) PortableRemoteObject.narrow(entryLocalHomeObj,
                EntryLocalHome.class);

        // create the bean
        this.entryLocalObject = entryLocalHome.create();

        mockTransaction = new MockUserTransaction();
        context.rebind("javax.transaction.UserTransaction", mockTransaction);
    }

    /**
     * Test createCutoffTime method. The id will be generated by the IDGenerator. And the insert action will be
     * audited.
     *
     * @throws Exception into junit
     */
    public void testCreateCutoffTime() throws Exception {
        // create cutoff time
        CutoffTimeBean bean = TestHelper.createCutoffTimeBean(1, new Date(10000));
        bean.setChanged(true);

        this.entryLocalObject.createCutoffTime(bean, true);

        // fectch it from database
        CutoffTimeBean persisted = entryLocalObject.fetchCutoffTimeByCompanyID(bean.getCompanyId());

        // verify the result
        TestHelper.assertCutoffTimeBean(bean, persisted);
        assertFalse("The changed flag is invalid.", persisted.isChanged());

        // get the result by the id and verify
        CutoffTimeBean anotherPersisted = entryLocalObject.fetchCutoffTimeById(persisted.getId());
        TestHelper.assertCutoffTimeBean(bean, anotherPersisted);
        assertFalse("The changed flag is invalid.", persisted.isChanged());
    }

    /**
     * Test createCutoffTime method. This time, the id is pre set, so we don't need to create it by the IDGenerator.
     * And audit will not taken.
     *
     * @throws Exception into junit
     */
    public void testCreateCutoffTime_withPreSetId() throws Exception {
        // create cutoff time
        CutoffTimeBean bean = TestHelper.createCutoffTimeBean(1, new Date(10000));
        bean.setChanged(true);

        // basically, the id would not be the one created by IDGenerator.
        bean.setId(1);
        entryLocalObject.createCutoffTime(bean, false);

        // fectch it from database
        CutoffTimeBean persisted = entryLocalObject.fetchCutoffTimeByCompanyID(bean.getCompanyId());

        // verify the result
        TestHelper.assertCutoffTimeBean(bean, persisted);
        assertFalse("The changed flag is invalid.", persisted.isChanged());
        assertEquals("The id should be the pre-set one.", 1, persisted.getId());

        // get the result by the id and verify
        CutoffTimeBean anotherPersisted = entryLocalObject.fetchCutoffTimeById(persisted.getId());
        TestHelper.assertCutoffTimeBean(bean, anotherPersisted);
        assertFalse("The changed flag is invalid.", persisted.isChanged());
    }

    /**
     * Test the updateCutoff time method.
     *
     * @throws Exception into Junit
     */
    public void testUpdateCutoffTime() throws Exception {
        // create cutoff time
        CutoffTimeBean bean = TestHelper.createCutoffTimeBean(1, new Date(10000));
        bean.setChanged(true);

        entryLocalObject.createCutoffTime(bean, false);

        // fectch it from database and update it
        CutoffTimeBean persisted = entryLocalObject.fetchCutoffTimeByCompanyID(bean.getCompanyId());

        persisted.setChanged(true);
        persisted.setCutoffTime(new Date(20000));
        persisted.setModificationUser("ivern");

        // update it
        entryLocalObject.updateCutoffTime(persisted, true);

        // re-fetch it
        CutoffTimeBean updated = entryLocalObject.fetchCutoffTimeById(persisted.getId());

        // verify it
        TestHelper.assertCutoffTimeBean(persisted, updated);
        assertFalse("The changed flag is invalid.", updated.isChanged());
    }

    /**
     * Test the updateCutoff time method.
     *
     * @throws Exception into Junit
     */
    public void testUpdateCutoffTime_notChanged() throws Exception {
        // create cutoff time
        CutoffTimeBean bean = TestHelper.createCutoffTimeBean(1, new Date(10000));
        bean.setChanged(true);

        entryLocalObject.createCutoffTime(bean, false);

        // fectch it from database and update it
        CutoffTimeBean persisted = entryLocalObject.fetchCutoffTimeByCompanyID(bean.getCompanyId());

        persisted.setChanged(false);
        persisted.setCutoffTime(new Date(20000));
        persisted.setModificationUser("ivern");

        // update it
        entryLocalObject.updateCutoffTime(persisted, true);

        // re-fetch it
        CutoffTimeBean notUpdated = entryLocalObject.fetchCutoffTimeById(persisted.getId());

        // verify it
        TestHelper.assertCutoffTimeBean(bean, notUpdated);
        assertFalse("The changed flag is invalid.", notUpdated.isChanged());
    }

    /**
     * <p>
     * Test the deleteCutoffTime method.
     * </p>
     *
     * @throws Exception into Junit
     */
    public void testDeleteCutoffTime() throws Exception {
        // create cutoff time
        CutoffTimeBean bean = TestHelper.createCutoffTimeBean(1, new Date(10000));
        bean.setChanged(true);

        entryLocalObject.createCutoffTime(bean, false);

        CutoffTimeBean persisted = entryLocalObject.fetchCutoffTimeByCompanyID(bean.getCompanyId());

        entryLocalObject.deleteCutoffTime(persisted, true);

        assertNull("No bean now.", entryLocalObject.fetchCutoffTimeById(persisted.getId()));
            
    }

    /**
     * Test the fetchCutoffTimeByCompanyID method.
     *
     * @throws Exception into Junit
     */
    public void testFetchCutoffTimeBeanByCompanyId() throws Exception {
        CutoffTimeBean bean = TestHelper.createCutoffTimeBean(1, new Date(10000));
        bean.setChanged(true);

        entryLocalObject.createCutoffTime(bean, false);

        CutoffTimeBean persisted = entryLocalObject.fetchCutoffTimeByCompanyID(bean.getCompanyId());
        TestHelper.assertCutoffTimeBean(bean, persisted);
        assertFalse("The changed flag is invalid.", persisted.isChanged());
        assertTrue("the createdate/modify date is invalid.", persisted.getModificationDate().before(new Date()));
        assertTrue("the createdate/modify date is invalid.", persisted.getCreationDate().before(new Date()));
    }

    /**
     * Test the fetchCutoffTimeById method.
     *
     * @throws Exception into Junit
     */
    public void testFecthCutoffTimeBeanById() throws Exception {
        CutoffTimeBean bean = TestHelper.createCutoffTimeBean(1, new Date(10000));
        bean.setChanged(true);

        entryLocalObject.createCutoffTime(bean, false);

        CutoffTimeBean persisted = entryLocalObject.fetchCutoffTimeByCompanyID(bean.getCompanyId());
        persisted = entryLocalObject.fetchCutoffTimeById(persisted.getId());

        TestHelper.assertCutoffTimeBean(bean, persisted);
        assertFalse("The changed flag is invalid.", persisted.isChanged());
        assertTrue("the createdate/modify date is invalid.", persisted.getModificationDate().before(new Date()));
        assertTrue("the createdate/modify date is invalid.", persisted.getCreationDate().before(new Date()));
    }

    /**
     * Test the CanSubmitEntry method, the entry is in the future,can not submit
     *
     * @throws Exception into Junit
     */
    public void testCanSubmitEntry() throws Exception {
        CutoffTimeBean bean = TestHelper.createCutoffTimeBean(1, new Date());
        bean.setChanged(true);
        this.entryLocalObject.createCutoffTime(bean, false);

        //entry in the future can not submit
        assertFalse("Can submit.",
            this.entryLocalObject.canSubmitEntry(new BaseEntry() {
                public long getCompanyId() {
                    return 1;
                }

                public Date getDate() {
                    return new Date(new Date().getTime() + 1);
                }
            }));

    }
    /**
     * Test the CanSubmitEntry method, the entry is being before the cutoff time.
     *
     * @throws Exception into Junit
     */
    public void testCanSubmitEntry2() throws Exception {
    	
    	Calendar cutoff = Calendar.getInstance();
        cutoff.add(Calendar.DAY_OF_WEEK, 1); //set cutoff time next day of week
        
        CutoffTimeBean bean = TestHelper.createCutoffTimeBean(1, cutoff.getTime());
        bean.setChanged(true);
        this.entryLocalObject.createCutoffTime(bean, false);

        //submit an entry two weeks ago,, so the entry's date is being before the cutoff time
        assertFalse("Can submit.",
            this.entryLocalObject.canSubmitEntry(new BaseEntry() {
                public long getCompanyId() {
                    return 1;
                }

                public Date getDate() {
                	Calendar entryDate = Calendar.getInstance();
                    entryDate.add(Calendar.DAY_OF_YEAR, -14); //the entry date is 2 weeks ago
                    
                    return entryDate.getTime();
                }
            }));
    }

    /**
     * Clear the environment.
     *
     * @throws Exception into Junit
     */
    protected void tearDown() throws Exception {
    	TestHelper.clearTable(conn);
	   	 if( conn != null){
	   		 conn.close();
	   	 }
	   	 TestHelper.clearConfiguration();
    }
}
