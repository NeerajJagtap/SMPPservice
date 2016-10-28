package com.vuclip.smpp.util;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class HibernateSupportDAO {

	@Autowired(required=true)
	public SessionFactory sessionFactory;

	public void setSessionFactory(SessionFactory sessionFactory){
		this.sessionFactory = sessionFactory;
	}
	
	public Session getSession(){
		try {
			Session currentSession=sessionFactory.getCurrentSession();
			if(currentSession==null || !currentSession.isOpen())
				return sessionFactory.openSession();
			else return sessionFactory.getCurrentSession();
		} catch (HibernateException e) {
			return sessionFactory.openSession();
		}
	}
	
}
