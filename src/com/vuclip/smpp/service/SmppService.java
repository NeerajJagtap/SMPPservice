package com.vuclip.smpp.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.vuclip.smpp.exceptions.SMPPException;
import com.vuclip.smpp.orm.dto.SmppData;

@Service
public interface SmppService {

	public SmppData getById(int id) throws SMPPException;

	public SmppData getByMsisdn(String msisdn) throws SMPPException;

	public List<SmppData> getAllSmppData() throws SMPPException;

	public int save(SmppData smppData) throws SMPPException;
	
	public void update(SmppData smppData)throws SMPPException ;

	public void delete(int id) throws SMPPException;

	public SmppData getRecord(SmppData smppData) throws SMPPException;

}
