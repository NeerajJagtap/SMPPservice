/**
 * 
 */
package com.vuclip.smpp.orm.dto;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * @author Devendra
 *
 */
@Entity
@Table(name="smpp_data")
@Cache(usage=CacheConcurrencyStrategy.READ_ONLY, region="smpp_data")
public class SmppData {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	
	@Column(name="msisdn")
	private String msisdn;
	
	@Column(name="transaction_id")
	private String transactionId;
	
	@Column(name="message_id")
	private String messageId; 
	
	@Column(name="price_point")
	private String pricePoint; 
	
	@Column(name="created_date",updatable=false,insertable=false)
	private String createdDate;
	
	@Column(name="modified_date",updatable=false,insertable=false)
	private String modifiedDate;
		
	public SmppData()
	{
	}
	
	public SmppData(int id,String msisdn, String transactionId, String messageId, String pricePoint, String createdDate,String modifiedDate) {
		super();
		this.id = id;
		this.msisdn = msisdn;
		this.transactionId = transactionId;
		this.messageId = messageId;
		this.pricePoint = pricePoint;
		this.createdDate = createdDate;
		this.modifiedDate = modifiedDate;
	}
	
	public SmppData(String msisdn, String transactionId, String messageId, String pricePoint, String createdDate,String modifiedDate) {
		super();
		this.msisdn = msisdn;
		this.transactionId = transactionId;
		this.messageId = messageId;
		this.pricePoint = pricePoint;
		this.createdDate = createdDate;
		this.modifiedDate = modifiedDate;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
		
	public String getMsisdn() {
		return msisdn;
	}

	public void setMsisdn(String msisdn) {
		this.msisdn = msisdn;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public String getMessageId() {
		return messageId;
	}

	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}

	public String getPricePoint() {
		return pricePoint;
	}

	public void setPricePoint(String pricePoint) {
		this.pricePoint = pricePoint;
	}

	/**
	 * @return the createdDate
	 */
	public String getCreatedDate() {
		return createdDate;
	}

	/**
	 * @param createdDate the createdDate to set
	 */
	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	/**
	 * @return the modifiedDate
	 */
	public String getModifiedDate() {
		return modifiedDate;
	}

	/**
	 * @param modifiedDate the modifiedDate to set
	 */
	public void setModifiedDate(String modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	@Override
	public String toString() {
		return "SmppData [id=" + id + ", msisdn=" + msisdn + ", transactionId=" + transactionId + ", messageId=" + messageId + ", pricePoint=" + pricePoint + ", createdDate="
				+ createdDate + ", modifiedDate=" + modifiedDate + "]";
	}
		
}
