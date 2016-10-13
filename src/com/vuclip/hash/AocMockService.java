package com.vuclip.hash;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class AocMockService {

	private static final String key = "967JKYTF5Z41jmaY2U2kQxVftgxej5EF";

	private static final Logger logger = LoggerFactory.getLogger(AocMockService.class);

	@Autowired
	HashDAO hashDAO = new HashDAO();

	public static void main(String[] args) {

		// TODO Auto-generated method stub

		// AOC flow starts here
		String endocedAOCString = "4u%2Bz3oOVstniZS7VucmMc9ky0r2iBglcQkxAHudhj5p9jSXjH09mXTnPMLGKDINWYMzLD5r5F95b3VZ5CpKg1RoXI8cEzqDSiXuoqajviQU%3D";
		// String endocedAOCString =
		// "MoO6yxJ2ru4mbjvWVm5IOXxNBbFsMhb2uTCUcTe0N5XYvcxXVi5SUaVzCdZCnG7GDQ81JfZBonN%2B0z2lML6HEIUY6pIVOJxBoPrSfrcfCzQ%3D";
		// String finalRespString = aocFlowMock(endocedAOCString);
		// System.out.println("Encoded Output from AOC : " + finalRespString);

	}

	public String aocFlowMock(String encodedString) {
		String resp = "";

		// manipulate
		// decode
		System.out.println("Incoming encoded aocHash : " + encodedString);
		if (logger.isDebugEnabled()) {
			logger.debug("Incoming encoded aocHash : " + encodedString);
		}

		/*
		 * String decodedString = decodeToUtf8(encodedString);
		 * System.out.println("Decoded aocHash : " + decodedString);
		 */

		String decryString = decrypt(key, encodedString);
		System.out.println("Decrypted aocHash : " + decryString);
		if (logger.isDebugEnabled()) {
			logger.debug("Decrypted aocHash : " + decryString);
		}
		// encrypt and send
		// msisdnAppId|ShortCode|ChargeId|CustomerNo|CustomerType|msisdnChargeBillId|ChargePrice|ServiceDescription|Product|ServiceIndicator;
		// Decrypted aocHash :
		// 1815|20222|201351586960000000|601123669163|prepaid|901103|0|VuClip|VIU|0
		// Decoded Param Label:
		// StatusCode|CustomerType|TranactionId|ChargeId|CustomerNo|ChargeBillId|ChargePrice
		// Decoded Param Value:
		// 0|prepaid|201394331400000000|201351586960000000|601123669163|901101|0

		String[] decryData = decryString.split("\\|");

		String msisdnAppId = decryData[0];
		String ShortCode = decryData[1];
		String decChargeId = decryData[2];
		String decCustomerNo = decryData[3];
		String decCustomerType = decryData[4];
		String msisdnChargeBillId = decryData[5];
		String decChargePrice = decryData[6];
		String ServiceDescription = decryData[7];
		String Product = decryData[8];
		String ServiceIndicator = decryData[9];

		System.out.println("After split : " + msisdnAppId + "#" + ShortCode + "#" + decChargeId + "#" + decCustomerNo
				+ "#" + decCustomerType + "#" + msisdnChargeBillId + "#" + decChargePrice + "#" + ServiceDescription
				+ "#" + Product + "#" + ServiceIndicator);
		if (logger.isDebugEnabled()) {
			logger.debug("After split : " + msisdnAppId + "#" + ShortCode + "#" + decChargeId + "#" + decCustomerNo
					+ "#" + decCustomerType + "#" + msisdnChargeBillId + "#" + decChargePrice + "#" + ServiceDescription
					+ "#" + Product + "#" + ServiceIndicator);
		}

		HashData hashData = hashDAO.getByCustomerNo(decCustomerNo);
		System.out.println("hashData from DB : " + hashData);
		if (logger.isDebugEnabled()) {
			logger.debug("hashData from DB : " + hashData);
		}

		String StatusCode = hashData.getStatusCode();
		String CustomerType = decCustomerType;
		String TranactionId = hashData.getTranactionId();
		String ChargeId = decChargeId;
		String CustomerNo = hashData.getCustomerNo();
		String ChargeBillId = msisdnChargeBillId;
		String ChargePrice = hashData.getChargePrice();

		String toEncrypt = StatusCode + "|" + CustomerType + "|" + TranactionId + "|" + ChargeId + "|" + CustomerNo
				+ "|" + ChargeBillId + "|" + ChargePrice;
		System.out.println("Raw Resp string : " + toEncrypt);
		if (logger.isDebugEnabled()) {
			logger.debug("Raw Resp string : " + toEncrypt);
		}

		String encString = encryption(key, toEncrypt);
		System.out.println("Encrypted resp : " + encString);
		if (logger.isDebugEnabled()) {
			logger.debug("Encrypted resp : " + encString);
		}

		String encodeString = encodeToUtf8(encString);
		System.out.println("Encoded resp to send : " + encodeString);
		if (logger.isDebugEnabled()) {
			logger.debug("Encoded resp to send : " + encodeString);
		}

		resp = encodeString;
		return resp;
	}

	public String encryption(String myKey, String unhashedValue) {
		try {
			byte[] key1 = myKey.getBytes("UTF-8");
			MessageDigest sha = MessageDigest.getInstance("SHA-256");
			byte[] key = sha.digest(key1);
			SecretKeySpec secretKey = new SecretKeySpec(key, "AES");
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, secretKey);
			return (Base64.encodeBase64String(cipher.doFinal(unhashedValue.getBytes("UTF-8"))));
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public String decrypt(String myKey, String strToDecrypt) {
		try {
			byte[] key1 = myKey.getBytes("UTF-8");
			MessageDigest sha = MessageDigest.getInstance("SHA-256");
			byte[] key = sha.digest(key1);
			SecretKeySpec secretKey = new SecretKeySpec(key, "AES");
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
			cipher.init(Cipher.DECRYPT_MODE, secretKey);
			return (new String(cipher.doFinal(Base64.decodeBase64(strToDecrypt))));
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public String encodeToUtf8(String input) {
		String encodedString = null;
		try {
			encodedString = java.net.URLEncoder.encode(input, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return encodedString;

	}

	public String decodeToUtf8(String input) {
		String decodedString = null;
		try {
			decodedString = java.net.URLDecoder.decode(input, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return decodedString;

	}

}
