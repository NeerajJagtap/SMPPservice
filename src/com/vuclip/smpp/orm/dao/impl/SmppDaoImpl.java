package com.vuclip.smpp.orm.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

	private static final Logger logger = LoggerFactory.getLogger(SmppDaoImpl.class);

	@Override
	public SmppData getById(int id) {
		return (SmppData) getSession().get(SmppData.class, id);
	}

	@Override
	public SmppData getByMsisdn(String msisdn) throws SMPPException {
		Criteria criteria = null;
		try {
			criteria = getSession().createCriteria(SmppData.class);
			if (logger.isDebugEnabled()) {
				logger.debug("HbSession created and criteria obj : " + criteria);
			}
			criteria.add(Restrictions.eq("msisdn", msisdn).ignoreCase());
		} catch (Exception e) {
			throw new SMPPException(SMPPExceptionConstant.HIBERNATE_CONNECTION_EXCEPTION, e.getMessage());
		}
		return (SmppData) criteria.uniqueResult();
	}

	@Override
	public SmppData getRecord(SmppData smppData) throws SMPPException {
		Criteria criteria = null;
		try {
			criteria = getSession().createCriteria(SmppData.class);
			if (logger.isDebugEnabled()) {
				logger.debug("HbSession created and criteria obj : " + criteria);
			}
			if (smppData != null && smppData.getMsisdn() != null && smppData.getMessageId() != null) {
				criteria.add(Restrictions.eq("msisdn", smppData.getMsisdn()).ignoreCase());
				criteria.add(Restrictions.eq("messageId", smppData.getMessageId()).ignoreCase());
				return (SmppData) criteria.uniqueResult();
			}
		} catch (Exception e) {
			throw new SMPPException(SMPPExceptionConstant.HIBERNATE_CONNECTION_EXCEPTION, e.getMessage());
		}
		return smppData;
	}

	@Override
	public List<SmppData> getAllSmppData() throws SMPPException {
		List list = null;
		try {
			Criteria criteria = getSession().createCriteria(SmppData.class);
			list = criteria.list();
		} catch (Exception e) {
			throw new SMPPException(SMPPExceptionConstant.HIBERNATE_CONNECTION_EXCEPTION, e.getMessage());
		}
		return list;
	}

	@Override
	public int save(SmppData smppData) throws SMPPException {
		Integer save = 0;
		try {
			save = (Integer) getSession().save(smppData);
		} catch (Exception e) {
			throw new SMPPException(SMPPExceptionConstant.HIBERNATE_CONNECTION_EXCEPTION, e.getMessage());
		}
		return save;
	}

	@Override
	public void update(SmppData smppData) throws SMPPException {
		try {
			Session session = getSession();
			session.beginTransaction();
			session.update(smppData);
			session.getTransaction().commit();
			session.flush();
			session.clear();
		} catch (Exception e) {
			throw new SMPPException(SMPPExceptionConstant.HIBERNATE_CONNECTION_EXCEPTION, e.getMessage());
		}
	}

	@Override
	public void delete(int id) throws SMPPException {
		try {
			SmppData c = getById(id);
			getSession().delete(c);
		} catch (Exception e) {
			throw new SMPPException(SMPPExceptionConstant.HIBERNATE_CONNECTION_EXCEPTION, e.getMessage());
		}
	}
}
