package com.vuclip.smpp.dao;

import java.util.Collections;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 *
 * @author 
 */
@Repository
public class CommonDAO{

	private static final Logger logger = LoggerFactory.getLogger(CommonDAO.class);
	
    @Autowired
    private SessionFactory sessionFactory;
    private Session hbSession;

    public Session getHbSession() {
        try {
            hbSession = sessionFactory.getCurrentSession();
            logger.info("Current Session : "+hbSession);
        } catch (HibernateException he) {
            he.printStackTrace(System.err);
        } catch (Exception ex) {
            ex.printStackTrace(System.err);
        }
        return hbSession;
    }

    public Session openHbSession() {
        try {
            hbSession = sessionFactory.openSession();
            logger.info("New Session openned : "+hbSession);
        } catch (HibernateException he) {
            he.printStackTrace(System.err);
        } catch (Exception ex) {
            ex.printStackTrace(System.err);
        }
        return hbSession;
    }

    public void setHbSession(Session hbSession) {
        this.hbSession = hbSession;
    }

    public void closeHBSession() throws HibernateException {
        if (hbSession != null) {
            hbSession.flush();
            hbSession.close();
        }
    }

    public Boolean checkForIntegrityContraint(String tableName, Integer pkID) {
        Boolean recordExistFlag = false;

        List<Object[]> pkFkList = Collections.EMPTY_LIST;
        SQLQuery pkFkQery = getHbSession().createSQLQuery("select cast(REFERENCED_TABLE_NAME AS CHAR CHARACTER SET utf8) AS ParentTableName,cast(REFERENCED_COLUMN_NAME AS CHAR CHARACTER SET utf8) AS ParentColumnName,cast(TABLE_NAME AS CHAR CHARACTER SET utf8) AS ChildTableName,cast(COLUMN_NAME AS CHAR CHARACTER SET utf8) AS ChildColumnName from INFORMATION_SCHEMA.KEY_COLUMN_USAGE where cast(REFERENCED_TABLE_NAME AS CHAR CHARACTER SET utf8) is not null AND cast(REFERENCED_TABLE_NAME AS CHAR CHARACTER SET utf8)='"+tableName+"'");
//        SQLQuery pkFkQery = getHbSession().createSQLQuery("select cast(REFERENCED_TABLE_NAME AS CHAR CHARACTER SET utf8) from information_schema.KEY_COLUMN_USAGE");
        System.out.println("Query ---> "+pkFkQery);
        pkFkList = pkFkQery.list();

        if (pkFkList != null) {
            for (Object[] record : pkFkList) {

                //To bypass check for Geo-Location in Office Setup 
                if(String.valueOf(record[0]).equalsIgnoreCase("OrgStructureMst") && String.valueOf(record[2]).equalsIgnoreCase("orgunitgeompg"))
                    break;

                Query query = getHbSession().createSQLQuery("SELECT COUNT(*) from "+record[2]+" where "+record[3]+"="+pkID+" AND isActive=1");
                System.out.println("Query2 ---> "+query);
                Integer count = Integer.parseInt(String.valueOf(query.uniqueResult()));
                if(count>0){
                    recordExistFlag = true;
                    break;
                }
            }
        }

        return recordExistFlag;
    }
}
