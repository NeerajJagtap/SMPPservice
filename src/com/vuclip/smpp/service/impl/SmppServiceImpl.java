package com.vuclip.smpp.service.impl;

import java.util.List;

import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vuclip.smpp.exceptions.SMPPException;
import com.vuclip.smpp.orm.dao.SmppDao;
import com.vuclip.smpp.orm.dto.SmppData;
import com.vuclip.smpp.service.SmppService;


@Service
@Transactional
public class SmppServiceImpl implements SmppService{

	@Autowired
	private SmppDao smppDao;
	
	Logger smpplogger = LogManager.getLogger("smpplogger");
	
	@Override
	public SmppData getById(int id) throws SMPPException {
		if (smpplogger.isDebugEnabled()) {
			smpplogger.debug("In SmppServiceImpl : getById");
		}
		return smppDao.getById(id);
	}

	@Override
	public SmppData getByMsisdn(String msisdn) throws SMPPException {
		if (smpplogger.isDebugEnabled()) {
			smpplogger.debug("In SmppServiceImpl : getByMsisdn");
		}
		return smppDao.getByMsisdn(msisdn);
	}

	@Override
	public List<SmppData> getAllSmppData() throws SMPPException {
		if (smpplogger.isDebugEnabled()) {
			smpplogger.debug("In SmppServiceImpl : getAllSmppData");
		}
		return smppDao.getAllSmppData();
	}

	@Override
	public int save(SmppData smppData) throws SMPPException {
		if (smpplogger.isDebugEnabled()) {
			smpplogger.debug("In SmppServiceImpl : save");
		}
		return smppDao.save(smppData);
	}

	@Override
	public void update(SmppData smppData) throws SMPPException {
		if (smpplogger.isDebugEnabled()) {
			smpplogger.debug("In SmppServiceImpl : update");
		}
		smppDao.update(smppData);
	}

	@Override
	public void delete(int id) throws SMPPException {
		if (smpplogger.isDebugEnabled()) {
			smpplogger.debug("In SmppServiceImpl : delete");
		}
		smppDao.delete(id);
	}

	@Override
	public SmppData getRecord(SmppData smppData) throws SMPPException {
		if (smpplogger.isDebugEnabled()) {
			smpplogger.debug("In SmppServiceImpl : getRecord ");
		}
		return smppDao.getRecord(smppData);
	}

}
