package com.vuclip.smpp.exceptions.constant;

public class SMPPExceptionConstant {

	public static final String PROPERTIES_LOADING_EXCEPTION = "xxxPropertiesExceptionxxx";

	public static final String CONNECTION_EXCEPTION = "bind.connection";

	// SMPP Related Codes

	public static final String TLV_EXCEPTION = "submit.sm.tlv";

	public static final String WRONG_DATE_FORMAT_EXCEPTION = "submit.sm.wrong.date.format";

	public static final String WRONG_LENGTH_OF_STRING_EXCEPTION = "submit.sm.wrong.length.of.string";

	public static final String SUBMIT_SM_PDU_EXCEPTION = "submit.sm.pdu";

	public static final String SUBMIT_SM_TIMEOUT_EXCEPTION = "submit.sm.timeout";

	public static final String SUBMIT_SM_WRONG_SESSION_STATE_EXCEPTION = "submit.sm.wrong.session.state";

	public static final String SUBMIT_SM_IO_EXCEPTION = "submit.sm.io";

	public static final String BIND_PDU_EXCEPTION = "bind.pdu";

	public static final String BIND_TIMEOUT_EXCEPTION = "bind.timeout";

	public static final String BIND_WRONG_SESSION_STATE_EXCEPTION = "bind.wrong.session.state";

	public static final String BIND_IO_EXCEPTION = "bind.io";

	public static final String ENQUIRE_PDU_EXCEPTION = "enquire.pdu";

	public static final String ENQUIRE_TIMEOUT_EXCEPTION = "enquire.timeout";

	public static final String ENQUIRE_WRONG_SESSION_STATE_EXCEPTION = "enquire.wrong.session.state";

	public static final String ENQUIRE_IO_EXCEPTION = "enquire.io";

	public static final String UNBIND_PDU_EXCEPTION = "unbind.pdu";

	public static final String UNBIND_TIMEOUT_EXCEPTION = "unbind.timeout";

	public static final String UNBIND_WRONG_SESSION_STATE_EXCEPTION = "unbind.wrong.session.state";

	public static final String UNBIND_IO_EXCEPTION = "unbind.io";

	public static final String UNBIND_VALUE_NOT_SET_EXCEPTION = "unbind.value.not.set";

	public static final String LISTENER_PDU_EXCEPTION = "listener.pdu";

	public static final String LISTENER_TIMEOUT_EXCEPTION = "listener.timeout";

	public static final String LISTENER_WRONG_SESSION_STATE_EXCEPTION = "listener.wrong.session.state";

	public static final String LISTENER_IO_EXCEPTION = "listener.io";

	public static final String LISTENER_VALUE_NOT_SET_EXCEPTION = "listener.value.not.set";

	public static final String LISTENER_SMPP_NESTED_EXCEPTION = "listener.smpp.nested";

	public static final String LISTENER_UNKNOWN_COMMAND_ID_EXCEPTION = "listener.unknown.command.id";

	public static final String LISTENER_NOT_SYNC_EXCEPTION = "listener.not.sync";

}
