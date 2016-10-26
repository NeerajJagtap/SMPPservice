package com.vuclip.smpp.service.impl;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vuclip.smpp.orm.dao.SmppDao;
import com.vuclip.smpp.orm.dto.SmppData;
import com.vuclip.smpp.service.SmppService;


@Service
@Transactional
public class SmppServiceImpl implements SmppService{

	@Autowired
	private SmppDao smppDao;
	
	
	@Override
	public SmppData getById(int id) {
		return smppDao.getById(id);
	}

	@Override
	public SmppData getByMsisdn(String msisdn) {
		return smppDao.getByMsisdn(msisdn);
	}

	@Override
	public List<SmppData> getAllSmppData() {
		return smppDao.getAllSmppData();
	}

	@Override
	public int save(SmppData smppData) {
		return smppDao.save(smppData);
	}

	@Override
	public void update(SmppData smppData) {
		smppDao.update(smppData);
	}

	@Override
	public void delete(int id) {
		smppDao.delete(id);
	}

	@Override
	public SmppData getRecord(SmppData smppData) {
		return smppDao.getRecord(smppData);
	}

}
