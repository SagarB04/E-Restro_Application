package com.manage.restaurant.dtos;

import com.manage.restaurant.entity.OrderStatus;

public class CartItemDto {

	private int id;
	private OrderStatus status;
	private int userId;
	private String name;
	private int productId;
	private String productName;
	private String price;
	private int categoryId;

	public CartItemDto(int id, OrderStatus status, int userId, String name, int productId, String productName,
			String price, int categoryId) {
		super();
		this.id = id;
		this.status = status;
		this.userId = userId;
		this.name = name;
		this.productId = productId;
		this.productName = productName;
		this.price = price;
		this.categoryId = categoryId;
	}

	public CartItemDto() {
		super();
	}

	public int getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public OrderStatus getStatus() {
		return status;
	}

	public void setStatus(OrderStatus status) {
		this.status = status;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getProductId() {
		return productId;
	}

	public void setProductId(int productId) {
		this.productId = productId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public Boolean checkPending() {
		if (status == OrderStatus.PENDING_PAYMENT) {
			return true;
		}
		return false;
	}
	public Boolean checkInProgress() {
		if (status == OrderStatus.INPROGRESS) {
			return true;
		}
		return false;
	}
	public Boolean checkDelivered() {
		if (status == OrderStatus.DELIVERED) {
			return true;
		}
		return false;
	}
	public Boolean checkRejected() {
		if (status == OrderStatus.REJECTED) {
			return true;
		}
		return false;
	}

}
