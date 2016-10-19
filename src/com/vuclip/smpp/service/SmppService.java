package com.vuclip.smpp.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.vuclip.smpp.to.SmppData;

@Service
public interface SmppService {
	
	public SmppData getById(int id);

	public SmppData getByMsisdn(String msisdn) ;

	public List<SmppData> getAllSmppData() ;

	public int save(SmppData smppData) ;
	
	public void update(SmppData smppData) ;

	public void delete(int id) ;

}
