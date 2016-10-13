package com.vuclip.contacts;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.vuclip.util.HibernateSupportDAO;

/**
 * @author SivaLabs
 *
 */
@Repository
@Transactional
public class ContactsDAO extends HibernateSupportDAO
{
	
	public Contact getById(int id)
	{
		return (Contact) getSession().get(Contact.class, id);
	}
	
	@SuppressWarnings("unchecked")
	public List<Contact> searchContacts(String name)
	{
		Criteria criteria = getSession().createCriteria(Contact.class);
		criteria.add(Restrictions.ilike("name", name+"%"));
		return criteria.list();
	}
	
	@SuppressWarnings("unchecked")
	public List<Contact> getAllContacts()
	{
		Criteria criteria = getSession().createCriteria(Contact.class);
		return criteria.list();
	}
	
	public int save(Contact contact)
	{
		return (Integer) getSession().save(contact);
	}
	
	public void update(Contact contact)
	{
		sessionFactory.getCurrentSession().merge(contact);
	}
	
	public void delete(int id)
	{
		Contact c = getById(id);
		getSession().delete(c);
	}

}
