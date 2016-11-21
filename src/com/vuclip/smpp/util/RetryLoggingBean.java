package com.vuclip.smpp.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.vuclip.smpp.orm.dto.SmppData;


public class RetryLoggingBean {

	private Date timeStamp;

	private SmppData smppData;

	private static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");

	public RetryLoggingBean() {
		// Do nothing
	}

	public RetryLoggingBean(Date timeStamp, SmppData smppData) {
		this.timeStamp = timeStamp;
		this.smppData = smppData;
	}

	public String getLogFormat() {

		StringBuilder toStringForm = new StringBuilder(
				"\n+-------------+-------------+-------------+-------------+-------------+-------------+-------------+-------------+-------------+-------------+-------------+-------------+\n");
		toStringForm.append("timeStamp[").append(dateFormat.format(this.timeStamp));
		toStringForm.append("] msisdn[").append(this.smppData.getMsisdn());
		toStringForm.append("] talendResponse[").append(this.smppData.getTalendResponse());
		toStringForm.append("] retries[").append(this.smppData.getReties() + 1);
		toStringForm.append("] DNMessage[").append(this.smppData.getDnMessage() + "]");
		toStringForm.append(
				"\n+-------------+-------------+-------------+-------------+-------------+-------------+-------------+-------------+-------------+-------------+-------------+-------------+\n");

		return toStringForm.toString();
	}
}
