package com.vuclip.smpp.props;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public final class SMPPProperties {
	
	private final String smppServerIP ;
	private final String smppServerPort ;
	private final String smppBindOption ;
	private final String smppSyncOption;
	private final String systemType;
	private final String systemID;
	private final String password;
	private final String noOfOptionalParams;
	private final String optionalParam1;
	private final String optionalParam2;
	private final String optionalParam3;
	private final String optionalParam1Tag;
	private final String optionalParam2Tag;
	private final String optionalParam3Tag;
	
	 @Autowired
	    public SMPPProperties(@Value("${smpp_server_ip}") String smppServerIP,
	                         @Value("${smpp_server_port}") String smppServerPort,
	                         @Value("${smpp_bind_option}") String smppBindOption, 
	                         @Value("${smpp_sync_option}") String smppSyncOption, 
	                         @Value("${system_id}") String systemID,
	                         @Value("${password}") String password,
	                         @Value("${no_of_optional_param}") String noOfOptionalParams,
	                         @Value("${opt_param1}") String optionalParam1,
	                         @Value("${opt_param2}") String optionalParam2,
	                         @Value("${opt_param3}") String optionalParam3,
	                         @Value("${opt_param1_tag}") String optionalParam1Tag,
	                         @Value("${opt_param2_tag}") String optionalParam2Tag,
	                         @Value("${opt_param3_tag}") String optionalParam3Tag,
	                         @Value("${system_type}") String systemType) {
	 
	        this.smppServerIP = smppServerIP;
	        this.smppServerPort = smppServerPort;
	        this.smppBindOption = smppBindOption;
	        this.smppSyncOption = smppSyncOption;
	        this.systemType = systemType;
	        this.systemID = systemID;
	        this.password = password;
	        this.noOfOptionalParams = noOfOptionalParams;
	        this.optionalParam1 = optionalParam1;
	        this.optionalParam2 = optionalParam2;
	        this.optionalParam3 = optionalParam3;
	        this.optionalParam1Tag = optionalParam1Tag;
	        this.optionalParam2Tag = optionalParam2Tag;
	        this.optionalParam3Tag = optionalParam3Tag;
	    }

	public String getSmppServerIP() {
		return smppServerIP;
	}

	public String getSmppServerPort() {
		return smppServerPort;
	}

	public String getSmppBindOption() {
		return smppBindOption;
	}

	public String getSmppSyncOption() {
		return smppSyncOption;
	}

	public String getSystemType() {
		return systemType;
	}

	public String getPassword() {
		return password;
	}

	public String getNoOfOptionalParams() {
		return noOfOptionalParams;
	}

	public String getOptionalParam1() {
		return optionalParam1;
	}

	public String getOptionalParam2() {
		return optionalParam2;
	}

	public String getOptionalParam3() {
		return optionalParam3;
	}

	public String getOptionalParam1Tag() {
		return optionalParam1Tag;
	}

	public String getOptionalParam2Tag() {
		return optionalParam2Tag;
	}

	public String getOptionalParam3Tag() {
		return optionalParam3Tag;
	}

	public String getSystemID() {
		return systemID;
	}

}
