package com.topcoder.web.common.dao;

import java.util.Date;
import java.util.List;

import com.topcoder.web.common.model.algo.Round;


/**
 * @author dok
 * @version $Revision: 1.2 $ Date: 2005/01/01 00:00:00
 *          Create Date: Mar 22, 2007
 */
public interface RoundDAO {

    Round find(Integer id);

    List<Round> getRoundsAfter(Date date);
    
    void saveOrUpdate(Round r);


}
