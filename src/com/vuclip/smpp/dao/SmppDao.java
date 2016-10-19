package com.vuclip.smpp.dao;

import java.util.List;

import org.springframework.stereotype.Component;

import com.vuclip.smpp.to.SmppData;

@Component
public interface SmppDao {

	public SmppData getById(int id);

	public SmppData getByMsisdn(String msisdn) ;

	public List<SmppData> getAllSmppData() ;

	public int save(SmppData smppData) ;
	
	public void update(SmppData smppData) ;

	public void delete(int id) ;
}
