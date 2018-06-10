package com.safaricom.webservice.bean;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class ResponseData {

	private String responseCode;
	private String responseMessage;
	
	@JsonIgnore
	private String errorMessage;

	public ResponseData() {
		super();
	}

	public ResponseData(String responseCode, String responseMessage, String errorMessage) {
		super();
		this.responseCode = responseCode;
		this.responseMessage = responseMessage;
		this.errorMessage = errorMessage;
	}

	public String getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}

	public String getResponseMessage() {
		return responseMessage;
	}

	public void setResponseMessage(String responseMessage) {
		this.responseMessage = responseMessage;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	@Override
	public String toString() {
		return "ResponseData [responseCode=" + responseCode + ", responseMessage=" + responseMessage + ", errorMessage="
				+ errorMessage + "]";
	}

}
