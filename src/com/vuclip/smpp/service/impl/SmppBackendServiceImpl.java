package com.vuclip.smpp.service.impl;

import java.util.List;

import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vuclip.smpp.exceptions.SMPPException;
import com.vuclip.smpp.orm.dao.SmppBackendDao;
import com.vuclip.smpp.orm.dto.SmppData;
import com.vuclip.smpp.service.SmppBackendService;

@Service
@Transactional
public class SmppBackendServiceImpl implements SmppBackendService {

	@Autowired
	private SmppBackendDao smppBackendDao;

	private static final Logger SMPPDBCLEANERLOGGER = LogManager.getLogger("smppDBCleanerLogger");

	private static final Logger SMPPTALENDRETRAIL = LogManager.getLogger("smppTalendRetrail");

	private static final Logger SMPPREQRETRAIL = LogManager.getLogger("smppReqRetrail");

	@Override
	public void purgeSmppDB() throws SMPPException {
		if (SMPPDBCLEANERLOGGER.isDebugEnabled()) {
			SMPPDBCLEANERLOGGER.debug("In SmppDBCleanServiceImpl : purgeSmppDB() ");
		}
		smppBackendDao.purgeSmppDB();
	}

	@Override
	public List<SmppData> getRetryToTalendList() throws SMPPException {
		if (SMPPTALENDRETRAIL.isDebugEnabled()) {
			SMPPTALENDRETRAIL.debug("In SmppDBCleanServiceImpl : getRetryToTalendList() ");
		}
		return smppBackendDao.getRetryToTalendList();
	}

}
