package com.cs.dto;

import java.io.Serializable;

public class CustomerDTO implements Serializable {
	
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 18677887L;

	private Long customerId;
	private String firstName;
	private String lastName;
	private String emailId;
	private String mobileNumber;
	private Long rewardPoints = 0L;

	public CustomerDTO() {

	}

	public CustomerDTO(String firstName, String lastName, String mobileNumber, String emailId) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.mobileNumber = mobileNumber;
		this.emailId = emailId;
	}

	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}
	
	public Long getRewardPoints() {
		return rewardPoints;
	}

	public void setRewardPoints(Long rewardPoints) {
		this.rewardPoints = rewardPoints;
	}

	@Override
	public String toString() {
		return "Customer [customerId=" + customerId + ", Name=" + firstName + " " + lastName + ", Mobile Number=" + mobileNumber
				+ ", emailId=" + emailId + ", rewardPoints=" + rewardPoints + "]";
	}

}
