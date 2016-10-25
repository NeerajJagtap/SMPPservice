package com.vuclip.smpp.orm.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
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
					criteria.add(Restrictions.eq("message_id", smppData.getMessageId()).ignoreCase());
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
		getSession().merge(smppData);
	}

	@Override
	public void delete(int id) {
		SmppData c = getById(id);
		getSession().delete(c);
	}

	/*
	 * public SessionFactory getSessionFactory(){ SessionFactory sessionFactory = null; try{ sessionFactory = new Configuration().configure().buildSessionFactory(); }catch
	 * (Throwable ex) { System.err.println("Failed to create sessionFactory object." + ex); throw new ExceptionInInitializerError(ex); } return sessionFactory; }
	 * 
	 * public Session getHibernationSession(){
	 * 
	 * Session session = sessionFactory.getCurrentSession();
	 * 
	 * if(session == null){ sessionFactory = getSessionFactory(); session = sessionFactory.openSession(); }
	 * 
	 * return session; }
	 */

	/*
	 * public HashData getById(int id) { return (HashData) getSession().get(HashData.class, id); }
	 * 
	 * public HashData getByCustomerNo(String customerNo) { Criteria criteria = null; try { criteria = getSession().createCriteria(HashData.class); if (logger.isDebugEnabled()) {
	 * logger.debug("HbSession created and criteria obj : " + criteria); } criteria.add(Restrictions.eq("customerNo", customerNo).ignoreCase()); } catch (Exception ex) {
	 * ex.printStackTrace(); logger.error("Exception : " + ex.getMessage(), ex); } return (HashData) criteria.uniqueResult(); }
	 * 
	 * @SuppressWarnings("unchecked") public List<HashData> searchCustomer(String customerNo) { Criteria criteria = getSession().createCriteria(HashData.class);
	 * criteria.add(Restrictions.ilike("customerNo", customerNo + "%")); return criteria.list(); }
	 * 
	 * @SuppressWarnings("unchecked") public List<HashData> getAllHashData() { Criteria criteria = getSession().createCriteria(HashData.class); return criteria.list(); }
	 * 
	 * public int save(HashData hashData) { return (Integer) getSession().save(hashData); }
	 * 
	 * public void update(HashData hashData) { getSession().merge(hashData); }
	 * 
	 * public void delete(int id) { HashData c = getById(id); getSession().delete(c); }
	 */

}
