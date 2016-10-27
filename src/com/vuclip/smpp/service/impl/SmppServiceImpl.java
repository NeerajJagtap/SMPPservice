package com.vuclip.smpp.service.impl;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vuclip.smpp.exceptions.SMPPException;
import com.vuclip.smpp.orm.dao.SmppDao;
import com.vuclip.smpp.orm.dto.SmppData;
import com.vuclip.smpp.service.SmppService;

@Service
@Transactional
public class SmppServiceImpl implements SmppService {

	@Autowired
	private SmppDao smppDao;

	@Override
	public SmppData getById(int id) throws SMPPException {
		return smppDao.getById(id);
	}

	@Override
	public SmppData getByMsisdn(String msisdn) throws SMPPException {
		return smppDao.getByMsisdn(msisdn);
	}

	@Override
	public List<SmppData> getAllSmppData() throws SMPPException {
		return smppDao.getAllSmppData();
	}

	@Override
	public int save(SmppData smppData) throws SMPPException {
		return smppDao.save(smppData);
	}

	@Override
	public void update(SmppData smppData) throws SMPPException {
		smppDao.update(smppData);
	}

	@Override
	public void delete(int id) throws SMPPException {
		smppDao.delete(id);
	}

	@Override
	public SmppData getRecord(SmppData smppData) throws SMPPException {
		return smppDao.getRecord(smppData);
	}

}
