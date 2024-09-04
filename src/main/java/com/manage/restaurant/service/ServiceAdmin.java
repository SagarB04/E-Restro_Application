package com.manage.restaurant.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.manage.restaurant.dtos.CartItemDto;
import com.manage.restaurant.dtos.CategoryDto;
import com.manage.restaurant.dtos.ProductDto;
import com.manage.restaurant.dtos.ReservationDto;
import com.manage.restaurant.entity.CartItem;
import com.manage.restaurant.entity.Category;
import com.manage.restaurant.entity.OrderStatus;
import com.manage.restaurant.entity.Product;
import com.manage.restaurant.entity.Reservation;
import com.manage.restaurant.entity.ReservationStatus;
import com.manage.restaurant.repository.CartRepository;
import com.manage.restaurant.repository.CategoryRepository;
import com.manage.restaurant.repository.ProductRepository;
import com.manage.restaurant.repository.ReservationRepository;

@Service
public class ServiceAdmin {

	@Autowired
	CategoryRepository catRepo;

	@Autowired
	ProductRepository prodRepo;

	@Autowired
	ReservationRepository resRepo;

	@Autowired
	CartRepository cartRepo;

	/**
	 * Category related Service
	 */

	public Category saveCategory(CategoryDto catdto) throws IOException {
		Category cat = new Category();
		cat.setName(catdto.getName());
		cat.setDescription(catdto.getDescription());
		cat.setImageUrl(catdto.getImageUrl());
		return catRepo.save(cat);
	}

	public List<Category> getCategories() {
		List<Category> categories = catRepo.findAll();
		return categories;
	}

	public Category getCategory(int id) {
		Optional<Category> cat = catRepo.findById(id);
		if (cat.isPresent()) {
			return cat.get();
		} else {
			return null;
		}
	}

	public List<Category> getCategoriesByTitle(String title) {
		List<Category> categories = catRepo.findAllByNameContainingIgnoreCase(title);
		return categories;
	}

	/**
	 * Product related Services
	 */

	public Product saveProduct(ProductDto proddto) {
		Category cat = getCategory(proddto.getCategoryId());
		if (cat != null) {
			Product prod = new Product();

			if (proddto.getId() > 0) {
				prod.setId(proddto.getId());
			}

			prod.setCategory(cat);
			prod.setName(proddto.getName());
			prod.setPrice(proddto.getPrice());
			prod.setDescription(proddto.getDescription());
			prod.setImageUrl(proddto.getImageUrl());

			return prodRepo.save(prod);
		}

		return null;
	}

	public List<ProductDto> getProductbyCategoryId(int categoryId) {

		List<Product> productlist = prodRepo.findAllByCategoryId(categoryId);
		List<ProductDto> dtolist = new ArrayList<>();

		for (Product product : productlist) {
			dtolist.add(product.getProductDto());
		}

		return dtolist;
	}
	public List<ProductDto> getAllProducts() {
		
		List<Product> productlist = prodRepo.findAll();
		List<ProductDto> dtolist = new ArrayList<>();
		
		for (Product product : productlist) {
			dtolist.add(product.getProductDto());
		}
		
		return dtolist;
	}

	public List<ProductDto> getProductsByTitleAndCategory(String title, int categoryId) {

		List<Product> productlist = prodRepo.findAllByNameContainingIgnoreCaseAndCategoryId(title, categoryId);
		List<ProductDto> dtolist = new ArrayList<>();

		for (Product product : productlist) {
			dtolist.add(product.getProductDto());
		}

		return dtolist;
	}
	
	public List<ProductDto> getProductsByTitle(String title) {
		List<Product> productlist = prodRepo.findAllByNameContainingIgnoreCase(title);
		List<ProductDto> dtolist = new ArrayList<>();

		for (Product product : productlist) {
			dtolist.add(product.getProductDto());
		}

		return dtolist;
	}
	

	public Boolean deleteProductById(int productId) {
		try {
			prodRepo.deleteById(productId);
		} catch (Exception e) {
			System.out.println(e);
			return false;
		}

		return true;
	}

	public ProductDto getProductById(int productId) {
		try {
			Optional<Product> prod = prodRepo.findById(productId);
			if (prod.isPresent()) {

				ProductDto dto = prod.get().getProductDto();

				return dto;
			}
		} catch (Exception e) {
			System.out.println(e);
			return null;
		}
		return null;
	}

	
	/*
	 * reservation related
	 */
	public Reservation getReservationById(int id) {

		Optional<Reservation> reservation = resRepo.findById(id);
		if (reservation.isPresent()) {
			return reservation.get();
		} else {
			return null;
		}
	}

	public Boolean approveByReservationId(int id) {

		Reservation res = getReservationById(id);
		if (res != null) {
			res.setStatus(ReservationStatus.APPROVED);
			try {
				res = resRepo.save(res);
				return true;
			} catch (Exception e) {
				System.out.println(e);
				return false;
			}
		} else {
			return false;
		}
	}

	public Boolean disapproveByReservationId(int id) {
		Reservation res = getReservationById(id);
		if (res != null) {
			res.setStatus(ReservationStatus.DISAPPROVED);
			try {
				res = resRepo.save(res);
				return true;
			} catch (Exception e) {
				System.out.println(e);
				return false;
			}
		} else {
			return false;
		}
	}

	public List<ReservationDto> getAllReservations() {

		List<Reservation> reservations = resRepo.findAll();
		List<ReservationDto> list = new ArrayList<>();
		for (Reservation reservation : reservations) {
			list.add(reservation.getReservationDto());
		}
		return list;
	}

	
	/*
	 * orders related services
	 */
	
	public List<CartItemDto> getAllOrders() {
		
		List<CartItem> orders = cartRepo.findAll();
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
	
	public CartItemDto setDeliveredStatusById(int id) {
		CartItem item = getOrderById(id);
		if (item != null) {
			item.setStatus(OrderStatus.DELIVERED);
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

	public CartItemDto setRejectedStatusById(int id) {
		CartItem item = getOrderById(id);
		if (item != null) {
			item.setStatus(OrderStatus.REJECTED);
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

	public long getCategoriesCount() {
		return catRepo.count();
	}

	public long getProductsCount() {
		return prodRepo.count();
	}

	public long getReservationsCount() {
		return resRepo.count();
	}

	public long getOrdersCount() {
		return cartRepo.count();
	}

	

}
