package com.vuclip.smpp.orm.dao;

import java.util.List;

import org.springframework.stereotype.Component;

import com.vuclip.smpp.orm.dto.SmppData;

@Component
public interface SmppDao {

	public SmppData getById(int id);

	public SmppData getByMsisdn(String msisdn) ;
	
	public SmppData getRecord(SmppData smppData);

	public List<SmppData> getAllSmppData() ;

	public int save(SmppData smppData) ;
	
	public void update(SmppData smppData) ;

	public void delete(int id) ;
}
