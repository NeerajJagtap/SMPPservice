/**
 * 
 */
package com.vuclip.hash;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author Admin
 *
 */
@Entity
@Table(name="hashdata")
public class HashData {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	@Column	private String statusCode;
	@Column private String hashValue; 
	@Column private String msisdnAppId; 
	@Column private String shortCode; 
	@Column private String chargeId;
	@Column private String customerNo; 
	@Column private String customerType;
	@Column private String msisdnChargeBillId;
	@Column private String chargePrice;
	@Column private String serviceDescription;
	@Column private String product;
	@Column private String serviceIndicator;
	@Column private String tranactionId;
	@Column private String respValue;
	@Column private String createdDate;
	@Column private String modifiedDate;
		
	public HashData()
	{
	}
	
	public HashData(int id,String statusCode, String hashValue, String msisdnAppId, String shortCode, String chargeId,
			String customerNo, String customerType, String msisdnChargeBillId, String chargePrice,
			String serviceDescription, String product, String serviceIndicator, String tranactionId, String respValue,String createdDate,String modifiedDate) {
		super();
		this.id = id;
		this.statusCode = statusCode;
		this.hashValue = hashValue;
		this.msisdnAppId = msisdnAppId;
		this.shortCode = shortCode;
		this.chargeId = chargeId;
		this.customerNo = customerNo;
		this.customerType = customerType;
		this.msisdnChargeBillId = msisdnChargeBillId;
		this.chargePrice = chargePrice;
		this.serviceDescription = serviceDescription;
		this.product = product;
		this.serviceIndicator = serviceIndicator;
		this.tranactionId = tranactionId;
		this.respValue = respValue;
		this.createdDate = createdDate;
		this.modifiedDate = modifiedDate;
	}
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}
	public String getHashValue() {
		return hashValue;
	}
	public void setHashValue(String hashValue) {
		this.hashValue = hashValue;
	}
	public String getMsisdnAppId() {
		return msisdnAppId;
	}
	public void setMsisdnAppId(String msisdnAppId) {
		this.msisdnAppId = msisdnAppId;
	}
	public String getShortCode() {
		return shortCode;
	}
	public void setShortCode(String shortCode) {
		this.shortCode = shortCode;
	}
	public String getChargeId() {
		return chargeId;
	}
	public void setChargeId(String chargeId) {
		this.chargeId = chargeId;
	}
	public String getCustomerNo() {
		return customerNo;
	}
	public void setCustomerNo(String customerNo) {
		this.customerNo = customerNo;
	}
	public String getCustomerType() {
		return customerType;
	}
	public void setCustomerType(String customerType) {
		this.customerType = customerType;
	}
	public String getMsisdnChargeBillId() {
		return msisdnChargeBillId;
	}
	public void setMsisdnChargeBillId(String msisdnChargeBillId) {
		this.msisdnChargeBillId = msisdnChargeBillId;
	}
	public String getChargePrice() {
		return chargePrice;
	}
	public void setChargePrice(String chargePrice) {
		this.chargePrice = chargePrice;
	}
	public String getServiceDescription() {
		return serviceDescription;
	}
	public void setServiceDescription(String serviceDescription) {
		this.serviceDescription = serviceDescription;
	}
	public String getProduct() {
		return product;
	}
	public void setProduct(String product) {
		this.product = product;
	}
	public String getServiceIndicator() {
		return serviceIndicator;
	}
	public void setServiceIndicator(String serviceIndicator) {
		this.serviceIndicator = serviceIndicator;
	}
	public String getTranactionId() {
		return tranactionId;
	}
	public void setTranactionId(String tranactionId) {
		this.tranactionId = tranactionId;
	}
	public String getRespValue() {
		return respValue;
	}
	public void setRespValue(String respValue) {
		this.respValue = respValue;
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

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "HashData [id=" + id + ", statusCode=" + statusCode + ", hashValue=" + hashValue + ", msisdnAppId="
				+ msisdnAppId + ", shortCode=" + shortCode + ", chargeId=" + chargeId + ", customerNo=" + customerNo
				+ ", customerType=" + customerType + ", msisdnChargeBillId=" + msisdnChargeBillId + ", chargePrice="
				+ chargePrice + ", serviceDescription=" + serviceDescription + ", product=" + product
				+ ", serviceIndicator=" + serviceIndicator + ", tranactionId=" + tranactionId + ", respValue="
				+ respValue + ", createdDate=" + createdDate + ", modifiedDate=" + modifiedDate + "]";
	}

	
		
}
