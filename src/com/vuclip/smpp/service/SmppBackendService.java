package com.vuclip.smpp.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.vuclip.smpp.exceptions.SMPPException;
import com.vuclip.smpp.orm.dto.SmppData;

@Service
public interface SmppBackendService {

	void purgeSmppDB() throws SMPPException;

	List<SmppData> getRetryToTalendList() throws SMPPException;

}
