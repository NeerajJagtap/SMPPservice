package com.vuclip.smpp.orm.dao;

import java.util.List;

import org.hibernate.Session;
import org.springframework.stereotype.Component;

import com.vuclip.smpp.exceptions.SMPPException;
import com.vuclip.smpp.orm.dto.SmppData;

@Component
public interface SmppBackendDao {
	
	public void purgeSmppDB();
	
	
	public void generateInfoLogs(Session session, String requestsInLast2HoursHQL,
			String failRequestsOverLast2hours, String responseOKRequestsInLast2HoursHQL, String talendRespFailHQL)
			throws Exception;


	List<SmppData> getRetryToTalendList() throws SMPPException;

}
