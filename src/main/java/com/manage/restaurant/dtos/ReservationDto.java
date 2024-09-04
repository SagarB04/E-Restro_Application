package com.manage.restaurant.dtos;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import com.manage.restaurant.entity.ReservationStatus;

public class ReservationDto {

	private int id;
	private String tableType;
	private String description;
	private LocalDateTime dateTime;
	private ReservationStatus status;
	private int userId;
	private String username;

	public ReservationDto() {
		super();
	}

	public ReservationDto(int id, String tableType, String description, LocalDateTime dateTime,
			ReservationStatus status, int userId, String username) {
		super();
		this.id = id;
		this.tableType = tableType;
		this.description = description;
		this.dateTime = dateTime;
		this.status = status;
		this.userId = userId;
		this.username = username;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTableType() {
		return tableType;
	}

	public void setTableType(String tableType) {
		this.tableType = tableType;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public LocalDateTime getDateTime() {
		return dateTime;
	}

	public void setDateTime(LocalDateTime dateTime) {
		this.dateTime = dateTime;
	}

	public ReservationStatus getStatus() {
		return status;
	}

	public void setStatus(ReservationStatus status) {
		this.status = status;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getTime() {
		LocalTime time24 = dateTime.toLocalTime();
		DateTimeFormatter formatter12 = DateTimeFormatter.ofPattern("hh:mm a");
		String time12 = time24.format(formatter12);
		return time12;
	}

	public Boolean checkPending() {
		if (this.status == ReservationStatus.PENDING)
			return true;
		return false;
	}
	public Boolean checkApproved() {
		if (this.status == ReservationStatus.APPROVED)
			return true;
		return false;
	}
	public Boolean checkDisapproved() {
		if (this.status == ReservationStatus.DISAPPROVED)
			return true;
		return false;
	}

}
