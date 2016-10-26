package com.vuclip.smpp.orm.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.vuclip.smpp.orm.dao.SmppDao;
import com.vuclip.smpp.orm.dto.SmppData;
import com.vuclip.util.HibernateSupportDAO;

@Component
@Transactional
public class SmppDaoImpl extends HibernateSupportDAO implements SmppDao {

	private static final Logger logger = LoggerFactory.getLogger(SmppDaoImpl.class);

	@Override
	public SmppData getById(int id) {
		return (SmppData) getSession().get(SmppData.class, id);
	}

	@Override
	public SmppData getByMsisdn(String msisdn) {
		Criteria criteria = null;
		try {
			criteria = getSession().createCriteria(SmppData.class);
			if (logger.isDebugEnabled()) {
				logger.debug("HbSession created and criteria obj : " + criteria);
			}
			criteria.add(Restrictions.eq("msisdn", msisdn).ignoreCase());
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error("Exception : " + ex.getMessage(), ex);
		}
		return (SmppData) criteria.uniqueResult();
	}

	@Override
	public SmppData getRecord(SmppData smppData) {
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
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error("Exception : " + ex.getMessage(), ex);
		}
		return smppData;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<SmppData> getAllSmppData() {
		Criteria criteria = getSession().createCriteria(SmppData.class);
		return criteria.list();
	}

	@Override
	public int save(SmppData smppData) {
		return (Integer) getSession().save(smppData);
	}

	@Override
	public void update(SmppData smppData) {
		Session session = getSession();
		session.beginTransaction();
		session.update(smppData);
		session.getTransaction().commit();
		session.flush();
		session.clear();
	}

	@Override
	public void delete(int id) {
		SmppData c = getById(id);
		getSession().delete(c);
	}
}
