package com.softtek.assetworx_api.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Component
@Table(name = "contractReminder")
@EntityListeners(AuditingEntityListener.class)
public class ContractReminder {
	@Id
	@GenericGenerator(name = "uuid", strategy = "uuid2")
	@GeneratedValue(generator = "uuid")
	private String id;

	@ManyToOne
	@JoinColumn(name="contract_id")
	@JsonIgnore
	private Contract contract;

	@Column(name = "reminder_Date", nullable = false)
	@JsonFormat(pattern = "yyyy-MM-dd")
	@Temporal(TemporalType.TIMESTAMP)
	private Date reminderDate;

	@Column(name="is_Reminded")
	private boolean isReminded = false;

	@Column(name="is_Snoozed")
	private boolean isSnoozed = false;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Contract getContract() {
		return contract;
	}

	public void setContract(Contract contract) {
		this.contract = contract;
	}

	public Date getReminderDate() {
		return reminderDate;
	}

	public void setReminderDate(Date reminderDate) {
		this.reminderDate = reminderDate;
	}

	public boolean isReminded() {
		return isReminded;
	}

	public void setReminded(boolean isReminded) {
		this.isReminded = isReminded;
	}

	public boolean isSnoozed() {
		return isSnoozed;
	}

	public void setSnoozed(boolean isSnoozed) {
		this.isSnoozed = isSnoozed;
	}



}