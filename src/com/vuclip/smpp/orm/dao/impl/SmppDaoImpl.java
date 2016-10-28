package com.vuclip.smpp.orm.dao.impl;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.vuclip.smpp.exceptions.SMPPException;
import com.vuclip.smpp.exceptions.constant.SMPPExceptionConstant;
import com.vuclip.smpp.orm.dao.SmppDao;
import com.vuclip.smpp.orm.dto.SmppData;
import com.vuclip.smpp.util.HibernateSupportDAO;

@Component
@Transactional
public class SmppDaoImpl extends HibernateSupportDAO implements SmppDao {

	Logger smpplogger = LogManager.getLogger("smpplogger");

	@Override
	public SmppData getById(int id) throws SMPPException {
		SmppData smppData = null;
		if (smpplogger.isDebugEnabled()) {
			smpplogger.debug("In SmppDaoImpl : getById");
		}
		try {
			smppData = (SmppData) getSession().get(SmppData.class, id);
		} catch (Exception e) {
			smpplogger.debug("In Dao : getById Exception : " + e.getMessage(), e);
			e.printStackTrace();
			throw new SMPPException(SMPPExceptionConstant.HIBERNATE_CONNECTION_EXCEPTION, e.getMessage());
		}
		return smppData;
	}

	@Override
	public SmppData getByMsisdn(String msisdn) throws SMPPException {
		Criteria criteria = null;
		if (smpplogger.isDebugEnabled()) {
			smpplogger.debug("In SmppDaoImpl : getByMsisdn");
		}
		try {
			criteria = getSession().createCriteria(SmppData.class);
			if (smpplogger.isDebugEnabled()) {
				smpplogger.debug("In Dao : getByMsisdn :HbSession created and criteria obj : " + criteria);
			}
			criteria.add(Restrictions.eq("msisdn", msisdn).ignoreCase());
		} catch (Exception e) {
			smpplogger.debug("In Dao : getByMsisdn Exception : " + e.getMessage(), e);
			e.printStackTrace();
			throw new SMPPException(SMPPExceptionConstant.HIBERNATE_CONNECTION_EXCEPTION, e.getMessage());
		}
		return (SmppData) criteria.uniqueResult();
	}

	@Override
	public SmppData getRecord(SmppData smppData) throws SMPPException {
		Criteria criteria = null;
		if (smpplogger.isDebugEnabled()) {
			smpplogger.debug("In SmppDaoImpl : getRecord");
		}
		try {
			criteria = getSession().createCriteria(SmppData.class);
			if (smpplogger.isDebugEnabled()) {
				smpplogger.debug("In Dao : getRecord : HbSession created and criteria obj : " + criteria);
			}
			if (smppData != null && smppData.getMsisdn() != null && smppData.getMessageId() != null) {
				criteria.add(Restrictions.eq("msisdn", smppData.getMsisdn()).ignoreCase());
				criteria.add(Restrictions.eq("messageId", smppData.getMessageId()).ignoreCase());
				return (SmppData) criteria.uniqueResult();
			}
		} catch (Exception e) {
			smpplogger.debug("In Dao : getRecord Exception : " + e.getMessage(), e);
			e.printStackTrace();
			throw new SMPPException(SMPPExceptionConstant.HIBERNATE_CONNECTION_EXCEPTION, e.getMessage());
		}
		return smppData;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<SmppData> getAllSmppData() throws SMPPException {
		List<SmppData> smppList = null;
		if (smpplogger.isDebugEnabled()) {
			smpplogger.debug("In SmppDaoImpl : getAllSmppData");
		}
		try {
			Criteria criteria = getSession().createCriteria(SmppData.class);
			smppList = criteria.list();
		} catch (Exception e) {
			smpplogger.debug("In Dao : getAllSmppData Exception : " + e.getMessage(), e);
			e.printStackTrace();
			throw new SMPPException(SMPPExceptionConstant.HIBERNATE_CONNECTION_EXCEPTION, e.getMessage());
		}
		return smppList;
	}

	@Override
	public int save(SmppData smppData) throws SMPPException {
		int flag = -1;
		if (smpplogger.isDebugEnabled()) {
			smpplogger.debug("In SmppDaoImpl : save");
		}
		try {
			flag = (Integer) getSession().save(smppData);
		} catch (Exception e) {
			smpplogger.debug("In Dao : save Exception : " + e.getMessage(), e);
			e.printStackTrace();
			throw new SMPPException(SMPPExceptionConstant.HIBERNATE_CONNECTION_EXCEPTION, e.getMessage());
		}
		return flag;
	}

	@Override
	public void update(SmppData smppData) throws SMPPException {
		if (smpplogger.isDebugEnabled()) {
			smpplogger.debug("In SmppDaoImpl : update ");
		}
		try {
			if (smpplogger.isDebugEnabled()) {
				smpplogger.debug("In Dao : update : " + smppData.toString());
			}
			Session session = getSession();
			session.beginTransaction();
			session.update(smppData);
			session.getTransaction().commit();
			session.flush();
			session.clear();
			if (smpplogger.isDebugEnabled()) {
				smpplogger.debug("In Dao : update success.");
			}
		} catch (Exception e) {
			smpplogger.debug("In Dao : update Exception : " + e.getMessage(), e);
			e.printStackTrace();
			throw new SMPPException(SMPPExceptionConstant.HIBERNATE_CONNECTION_EXCEPTION, e.getMessage());
		}
	}

	@Override
	public void delete(int id) throws SMPPException {
		if (smpplogger.isDebugEnabled()) {
			smpplogger.debug("In SmppDaoImpl : delete");
		}
		try {
			SmppData c = getById(id);
			getSession().delete(c);
		} catch (Exception e) {
			smpplogger.debug("In Dao : delete Exception : " + e.getMessage(), e);
			e.printStackTrace();
			throw new SMPPException(SMPPExceptionConstant.HIBERNATE_CONNECTION_EXCEPTION, e.getMessage());
		}
	}
}
