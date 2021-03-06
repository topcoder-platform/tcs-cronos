/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.timetracker.entry.time;

/**
 * <p>
 * This class represents the Reject Reason entity. Extends <code>BaseDataObject</code>. Uses its primaryId member for
 * holding the RejectReasonID defined in the database. This entity is used in <code>RejectReasonDAO</code>. As such,
 * when the DAO creates this record, it will ignore the creation and modification fields and assign its own values. It
 * will also ignore the primary Id vaue as it will assign its own. Similarly, the <code>DAO</code> will ignore all of
 * these fields when updating. The creation fields and the primary fields are never updated, and the <code>DAO</code>
 * will assign its own values to the modification fields. As such, from the perspective of the application using this
 * entity, these fields can be considered to be effectively read-only (as assigning fresh values to these fields has
 * no impact in the <code>DAO</code>, and thus on the database. Please see <code>RejectReasonDAO</code> for more
 * details.
 * </p>
 *
 * @author AleaActaEst, TCSDEVELOPER
 * @version 1.1
 */
public class RejectReason extends BaseDataObject {
    /**
     * <p>
     * Sole constructor.
     * </p>
     */
    public RejectReason() {
        // empty here
    }
}
