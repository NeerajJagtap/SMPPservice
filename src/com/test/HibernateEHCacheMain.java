package com.test;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.stat.SecondLevelCacheStatistics;
import org.hibernate.stat.Statistics;

import com.vuclip.contacts.Contact;
import com.vuclip.util.HibernateUtil;
public class HibernateEHCacheMain {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
System.out.println("Temp Dir:"+System.getProperty("java.io.tmpdir"));
		
		//Initialize Sessions
		SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
		Statistics stats = sessionFactory.getStatistics();
		System.out.println("Stats enabled="+stats.isStatisticsEnabled());
		stats.setStatisticsEnabled(true);
		System.out.println("Stats enabled="+stats.isStatisticsEnabled());
		
		Session session = sessionFactory.openSession();
		Session otherSession = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		Transaction otherTransaction = otherSession.beginTransaction();
		
		printStats(stats, 0);
		
		Contact emp = (Contact) session.load(Contact.class, 1);
		printData(emp, stats, 1);
		
		emp = (Contact) session.load(Contact.class, 3);
		printData(emp, stats, 2);
		
		emp = (Contact) session.load(Contact.class, 5);
		printData(emp, stats, 3);
		
		emp = (Contact) session.load(Contact.class, 1);
		printData(emp, stats, 4);
		
		//clear first level cache, so that second level cache is used
		session.evict(emp);
		emp = (Contact) session.load(Contact.class, 1);
		printData(emp, stats, 5);
		
		emp = (Contact) session.load(Contact.class, 1);
		printData(emp, stats, 6);
		
		emp = (Contact) otherSession.load(Contact.class, 1);
		printData(emp, stats, 7);
		
		//Release resources
		transaction.commit();
		otherTransaction.commit();
		sessionFactory.close();

	}
	
	private static void printStats(Statistics stats, int i) {
		//System.out.println("***** " + i + " *****");
		System.out.println("Fetch Count="
				+ stats.getEntityFetchCount());
		System.out.println("Second Level Hit Count="
				+ stats.getSecondLevelCacheHitCount()+" | Second Level Miss Count="
						+ stats
						.getSecondLevelCacheMissCount()+" | Second Level Put Count="
								+ stats.getSecondLevelCachePutCount());
	}

	private static void printData(Contact emp, Statistics stats, int count) {
		System.out.println("\n***** Hit " + count + " STARTS *****");
		System.out.println("# Name="+emp.getName()+", Address="+emp.getAddress());
		printStats(stats, count);
		System.out.println("***** Hit " + count + " ENDS *****");
	}

	public static void printStatistics(SessionFactory sessionFactory) {
	    Statistics stat = sessionFactory.getStatistics();
	    String regions[] = stat.getSecondLevelCacheRegionNames();
	    System.out.println(regions.toString());
	    for(String regionName:regions) {
	        SecondLevelCacheStatistics stat2 = stat.getSecondLevelCacheStatistics(regionName);
	        System.out.println("2nd Level Cache(" +regionName+") Put Count: "+stat2.getPutCount());
	        System.out.println("2nd Level Cache(" +regionName+") HIt Count: "+stat2.getHitCount());
	        System.out.println("2nd Level Cache(" +regionName+") Miss Count: "+stat2.getMissCount());
	    }
	}
}
