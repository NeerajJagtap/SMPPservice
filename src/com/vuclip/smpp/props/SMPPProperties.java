package com.vuclip.smpp.props;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

@Configuration
@PropertySources(value = { @PropertySource("classpath:smpp-config.properties"),
		@PropertySource("classpath:config.properties") })
public class SMPPPropertyConfig {
	@Value("smpp_server_ip")
	private String smppServerIP;

	@Value("smpp_server_port")
	private String smppServerPort;

	@Value("smpp_bind_option")
	private String smppBindOption;

	@Value("smpp_sync_option")
	private String smppSyncOption;

	@Value("system_type")
	private String systemType;

	@Value("system_id")
	private String systemID;

	@Value("password")
	private String password;

	@Value("no_of_optional_param")
	private String noOfOptionalParams;

	@Value("opt_param1")
	private String optionalParam1;

	@Value("opt_param2")
	private String optionalParam2;

	@Value("opt_param3")
	private String optionalParam3;

	@Value("opt_param1_tag")
	private String optionalParam1Tag;

	@Value("opt_param2_tag")
	private String optionalParam2Tag;

	@Value("opt_param3_tag")
	private String optionalParam3Tag;

	public String getSmppServerIP() {
		return smppServerIP;
	}

	public void setSmppServerIP(String smppServerIP) {
		this.smppServerIP = smppServerIP;
	}

	public String getSmppServerPort() {
		return smppServerPort;
	}

	public void setSmppServerPort(String smppServerPort) {
		this.smppServerPort = smppServerPort;
	}

	public String getSmppBindOption() {
		return smppBindOption;
	}

	public void setSmppBindOption(String smppBindOption) {
		this.smppBindOption = smppBindOption;
	}

	public String getSmppSyncOption() {
		return smppSyncOption;
	}

	public void setSmppSyncOption(String smppSyncOption) {
		this.smppSyncOption = smppSyncOption;
	}

	public String getSystemType() {
		return systemType;
	}

	public void setSystemType(String systemType) {
		this.systemType = systemType;
	}

	public String getSystemID() {
		return systemID;
	}

	public void setSystemID(String systemID) {
		this.systemID = systemID;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getNoOfOptionalParams() {
		return noOfOptionalParams;
	}

	public void setNoOfOptionalParams(String noOfOptionalParams) {
		this.noOfOptionalParams = noOfOptionalParams;
	}

	public String getOptionalParam1() {
		return optionalParam1;
	}

	public void setOptionalParam1(String optionalParam1) {
		this.optionalParam1 = optionalParam1;
	}

	public String getOptionalParam2() {
		return optionalParam2;
	}

	public void setOptionalParam2(String optionalParam2) {
		this.optionalParam2 = optionalParam2;
	}

	public String getOptionalParam3() {
		return optionalParam3;
	}

	public void setOptionalParam3(String optionalParam3) {
		this.optionalParam3 = optionalParam3;
	}

	public String getOptionalParam1Tag() {
		return optionalParam1Tag;
	}

	public void setOptionalParam1Tag(String optionalParam1Tag) {
		this.optionalParam1Tag = optionalParam1Tag;
	}

	public String getOptionalParam2Tag() {
		return optionalParam2Tag;
	}

	public void setOptionalParam2Tag(String optionalParam2Tag) {
		this.optionalParam2Tag = optionalParam2Tag;
	}

	public String getOptionalParam3Tag() {
		return optionalParam3Tag;
	}

	public void setOptionalParam3Tag(String optionalParam3Tag) {
		this.optionalParam3Tag = optionalParam3Tag;
	}

}
