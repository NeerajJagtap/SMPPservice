package com.vuclip.smpp.orm.dao.impl;

import java.math.BigInteger;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.vuclip.smpp.exceptions.SMPPException;
import com.vuclip.smpp.exceptions.constant.SMPPExceptionConstant;
import com.vuclip.smpp.orm.dao.SmppBackendDao;
import com.vuclip.smpp.orm.dto.SmppData;
import com.vuclip.smpp.util.HibernateSupportDAO;

@Component
@Transactional
public class SmppBackendDaoImpl extends HibernateSupportDAO implements SmppBackendDao {

	private static final Logger SMPPDBCLEANERLOGGER = LogManager.getLogger("smppDBCleanerLogger");

	private static final Logger SMPPTALENDRETRAIL = LogManager.getLogger("smppTalendRetrail");

	private static final Logger SMPPCURRENTBDSTATUS = LogManager.getLogger("smppDBCurrentStatus");

	public void purgeSmppDB() {
		if (SMPPDBCLEANERLOGGER.isDebugEnabled()) {
			SMPPDBCLEANERLOGGER.debug("SMPPDBCleaner Started");
		}
		Session session = null;
		Query query = null;
		String deleteRecsHQL = "Delete from smpp_data where talend_response = '200' and resp_status = '202' and modified_date <= (current_timestamp() - interval 2 hour)";
		String requestsInLast2HoursHQL = "Select count(*) from smpp_data where modified_date >= (current_timestamp() - interval 2 hour)";
		String failRequestsOverLast2hours = "Select count(*) from smpp_data where resp_status <> '202' and modified_date >= (current_timestamp() - interval 2 hour)";
		String responseOKRequestsInLast2HoursHQL = "Select count(*) from smpp_data where resp_status = '202' and modified_date >= (current_timestamp() - interval 2 hour)";
		String talendRespFailHQL = "Select count(*) from smpp_data where talend_response <> '200' and resp_status = '202' and modified_date >= (current_timestamp() - interval 2 hour)";
		try {
			session = getSession();
			session.beginTransaction();

			generateInfoLogs(session, requestsInLast2HoursHQL, failRequestsOverLast2hours,
					responseOKRequestsInLast2HoursHQL, talendRespFailHQL);

			// Delete Records before currentTime - 2hours
			query = session.createSQLQuery(deleteRecsHQL);
			int result = query.executeUpdate();
			session.getTransaction().commit();

			if (SMPPDBCLEANERLOGGER.isDebugEnabled()) {
				SMPPDBCLEANERLOGGER.debug("SMPPDBCleaner End with no of rows deleted : " + result);
			}
		} catch (Exception e) {
			String message = e.getMessage();
			if (SMPPDBCLEANERLOGGER.isDebugEnabled()) {
				SMPPDBCLEANERLOGGER.debug("SMPPDBCleaner End with Exception : " + message);
			}
			if (SMPPCURRENTBDSTATUS.isInfoEnabled()) {
				SMPPCURRENTBDSTATUS.info("Exception while reading " + message);
			}
		} finally {
			session.flush();
			session.close();
		}
	}

	public void generateInfoLogs(Session session, String requestsInLast2HoursHQL, String failRequestsOverLast2hours,
			String responseOKRequestsInLast2HoursHQL, String talendRespFailHQL) throws Exception {
		Query query;
		// Log Request made in last 2 hours
		query = session.createSQLQuery(requestsInLast2HoursHQL);
		BigInteger requestsMade = (BigInteger) query.uniqueResult();
		// Request failed by carrier in last 2 hours
		query = session.createSQLQuery(failRequestsOverLast2hours);
		BigInteger failRequestsToCarrier = (BigInteger) query.uniqueResult();
		// Request success by carrier in last 2 hours
		query = session.createSQLQuery(responseOKRequestsInLast2HoursHQL);
		BigInteger successReqs = (BigInteger) query.uniqueResult();
		// requests failed to talend in last 2 hours
		query = session.createSQLQuery(talendRespFailHQL);
		BigInteger talendNotReached = (BigInteger) query.uniqueResult();
		if (SMPPCURRENTBDSTATUS.isInfoEnabled()) {
			SMPPCURRENTBDSTATUS.info("==================== DB status in last 2 hours ==================");
			SMPPCURRENTBDSTATUS.info("Requests Made : " + requestsMade);
			SMPPCURRENTBDSTATUS.info("Failed from carrier : " + failRequestsToCarrier);
			SMPPCURRENTBDSTATUS.info("Successful from Carrier : " + successReqs);
			SMPPCURRENTBDSTATUS.info("Talend response not OK : " + talendNotReached);
			SMPPCURRENTBDSTATUS.info("Success Requests : "
					+ (requestsMade.longValue() - (failRequestsToCarrier.longValue() + talendNotReached.longValue())));
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<SmppData> getRetryToTalendList() throws SMPPException {
		List<SmppData> smppList = null;
		Session session = getSession();
		if (SMPPTALENDRETRAIL.isDebugEnabled()) {
			SMPPTALENDRETRAIL.debug("In SmppBackendDaoImpl : getRetryToTalendList");
		}
		Query query = null;
		String selectHQL = "Select s from SmppData s where s.talendResponse <> '200' and s.respStatus = '202' and reties < 5 and (dnMessage <> '' or dnMessage is not null)";

		try {
			// get Recs with in time (currentTime - 2hours)
			query = session.createQuery(selectHQL);
			smppList = query.list();

		} catch (Exception e) {
			SMPPTALENDRETRAIL.debug("In SmppBackendDaoImpl : getRetryToTalendList Exception : " + e.getMessage(), e);
			e.printStackTrace();
			throw new SMPPException(SMPPExceptionConstant.HIBERNATE_CONNECTION_EXCEPTION, e.getMessage());
		} finally {
			session.flush();
			session.close();
		}

		return smppList;
	}
}
