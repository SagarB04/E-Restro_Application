package com.manage.restaurant.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.manage.restaurant.dtos.CartItemDto;
import com.manage.restaurant.dtos.ProductDto;
import com.manage.restaurant.dtos.ReservationDto;
import com.manage.restaurant.entity.CartItem;
import com.manage.restaurant.entity.Category;
import com.manage.restaurant.entity.OrderStatus;
import com.manage.restaurant.entity.Product;
import com.manage.restaurant.entity.Reservation;
import com.manage.restaurant.entity.ReservationStatus;
import com.manage.restaurant.entity.User;
import com.manage.restaurant.repository.CartRepository;
import com.manage.restaurant.repository.ProductRepository;
import com.manage.restaurant.repository.ReservationRepository;

@Service
public class ServiceCustomer {

	@Autowired
	ServiceAdmin adservice;

	@Autowired
	UserService userservice;

	@Autowired
	ReservationRepository resRepo;

	@Autowired
	ProductRepository prodRepo;

	@Autowired
	CartRepository cartRepo;

	public List<Category> getCategories() {
		return adservice.getCategories();
	}

	public List<Category> getCategoriesByTitle(String title) {
		return adservice.getCategoriesByTitle(title);
	}

	public List<ProductDto> getProductbyCategoryId(int categoryId) {
		return adservice.getProductbyCategoryId(categoryId);
	}

	public List<ProductDto> getProductsByTitleAndCategory(String title, int categoryId) {
		return adservice.getProductsByTitleAndCategory(title, categoryId);
	}

	public List<ProductDto> getProductsByTitle(String title) {
		return adservice.getProductsByTitle(title);
	}
	
	public Product getProductById(int id) {

		Optional<Product> prod = prodRepo.findById(id);
		if (prod.isPresent()) {
			return prod.get();
		}
		return null;

	}

	/*
	 * Reservation related services
	 */

	public ReservationDto postReservation(ReservationDto requestDto) {

		User user = userservice.getUserDetails().getUser();

		Reservation request = new Reservation();
		request.setTableType(requestDto.getTableType());
		request.setDescription(requestDto.getDescription());
		request.setDateTime(requestDto.getDateTime());
		request.setStatus(ReservationStatus.PENDING);
		request.setUser(user);
		try {
			Reservation createdRequest = resRepo.save(request);
			return createdRequest.getReservationDto();

		} catch (Exception e) {
			System.out.println(e);
		}
		return null;
	}

	public List<ReservationDto> getReservationsByUserId() {

		User user = userservice.getUserDetails().getUser();
		List<Reservation> reservations = resRepo.findAllByUserId(user.getId());
		List<ReservationDto> list = new ArrayList<>();
		for (Reservation reservation : reservations) {
			list.add(reservation.getReservationDto());
		}
		return list;
	}

	public Boolean deleteByReservationId(int id) {

		try {
			resRepo.deleteById(id);
		} catch (Exception e) {
			System.out.println(e);
			return false;
		}

		return true;
	}

	/*
	 * cart service
	 */

	public List<CartItemDto> getOrdersByUserId() {
		User user = userservice.getUserDetails().getUser();
		List<CartItem> orders = cartRepo.findAllByUserId(user.getId());
		List<CartItemDto> list = new ArrayList<>();
		for (CartItem cartItem : orders) {
			list.add(cartItem.getCartItemDto());
		}
		return list;
	}

	public CartItem getOrderById(int id) {
		Optional<CartItem> item = cartRepo.findById(id);
		if (item.isPresent()) {
			return item.get();
		}
		return null;
	}

	public CartItemDto saveCartItem(int id) {

		Product product = getProductById(id);
		User user = userservice.getUserDetails().getUser();
		if (product != null) {

			CartItem item = new CartItem();
			item.setProduct(product);
			item.setUser(user);
			item.setStatus(OrderStatus.PENDING_PAYMENT);
			try {
				CartItem createdItem = cartRepo.save(item);
				return createdItem.getCartItemDto();
			} catch (Exception e) {
				System.out.println(e);
				return null;
			}
		}
		return null;
	}

	public Boolean deleteCartItemById(int id) {
		try {
			cartRepo.deleteById(id);
			return true;
		} catch (Exception e) {
			System.out.println(e);
			return false;
		}
	}

	public CartItemDto updateCartStatusById(int id) {

		CartItem item = getOrderById(id);
		if (item != null) {
			item.setStatus(OrderStatus.INPROGRESS);
			try {
				CartItem createdItem = cartRepo.save(item);
				return createdItem.getCartItemDto();
			} catch (Exception e) {
				System.out.println(e);
				return null;
			}
		}
		return null;
	}

	public List<ProductDto> getAllProducts() {
		return adservice.getAllProducts();
	}

	
	
	public long getReservationsCountByUserId() {
		User user = userservice.getUserDetails().getUser();
		return resRepo.countByUserId(user.getId());
	}

	public long getOrdersCountByUserId() {
		User user = userservice.getUserDetails().getUser();
		return cartRepo.countByUserId(user.getId());
	}

}
