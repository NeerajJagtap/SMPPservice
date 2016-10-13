package com.vuclip.hash;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.vuclip.util.HibernateSupportDAO;

@Repository
@Transactional
public class HashDAO extends HibernateSupportDAO {

	private static final Logger logger = LoggerFactory.getLogger(HashDAO.class);

	/*
	 * public SessionFactory getSessionFactory(){ SessionFactory sessionFactory
	 * = null; try{ sessionFactory = new
	 * Configuration().configure().buildSessionFactory(); }catch (Throwable ex)
	 * { System.err.println("Failed to create sessionFactory object." + ex);
	 * throw new ExceptionInInitializerError(ex); } return sessionFactory; }
	 * 
	 * public Session getHibernationSession(){
	 * 
	 * Session session = sessionFactory.getCurrentSession();
	 * 
	 * if(session == null){ sessionFactory = getSessionFactory(); session =
	 * sessionFactory.openSession(); }
	 * 
	 * return session; }
	 */

	public HashData getById(int id) {
		return (HashData) getSession().get(HashData.class, id);
	}

	public HashData getByCustomerNo(String customerNo) {
		Criteria criteria = null;
		try {
			criteria = getSession().createCriteria(HashData.class);
			if (logger.isDebugEnabled()) {
				logger.debug("HbSession created and criteria obj : " + criteria);
			}
			criteria.add(Restrictions.eq("customerNo", customerNo).ignoreCase());
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error("Exception : " + ex.getMessage(), ex);
		}
		return (HashData) criteria.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	public List<HashData> searchCustomer(String customerNo) {
		Criteria criteria = getSession().createCriteria(HashData.class);
		criteria.add(Restrictions.ilike("customerNo", customerNo + "%"));
		return criteria.list();
	}

	@SuppressWarnings("unchecked")
	public List<HashData> getAllHashData() {
		Criteria criteria = getSession().createCriteria(HashData.class);
		return criteria.list();
	}

	public int save(HashData hashData) {
		return (Integer) getSession().save(hashData);
	}

	public void update(HashData hashData) {
		getSession().merge(hashData);
	}

	public void delete(int id) {
		HashData c = getById(id);
		getSession().delete(c);
	}

}
