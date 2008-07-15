/*
 * Copyright (C) 2008 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.service.digitalrun.points;

import javax.ejb.Local;

/**
 * <p>
 * This EJB Local interface extends the DigitalRunPointsManager, and it is marked with Local
 * annotation to indicate it's an EJB Local interface.
 * </p>
 * <p>
 * Thread Safety: Implementation will run in an EJB container and thread safety depends on the
 * container. Generally the implementation is thread safe when it's running inside the container.
 * </p>
 * @author  DanLazar, TCSDEVELOPER
 * @version 1.0
 */
@Local
public interface DigitalRunPointsManagerLocal extends DigitalRunPointsManager {
    // Empty
}
