package com.manage.restaurant.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.manage.restaurant.dtos.CartItemDto;
import com.manage.restaurant.dtos.ProductDto;
import com.manage.restaurant.dtos.ReservationDto;
import com.manage.restaurant.entity.Category;
import com.manage.restaurant.service.ServiceCustomer;
import com.manage.restaurant.service.UserService;

@Controller
@RequestMapping("/restaurant/customer")
public class ControllerCustomer {

	@Autowired
	UserService service;

	@Autowired
	ServiceCustomer customservice;

	/**
	 * Dashboard related mappings
	 */
	
	@GetMapping("/dashboard")
	public String getDashboard(Model model) {
		
		long resCount = customservice.getReservationsCountByUserId();
		long orderCount = customservice.getOrdersCountByUserId();
		

		model.addAttribute("name", service.getUserDetails().getName());
		model.addAttribute("resCount", resCount);
		model.addAttribute("orderCount", orderCount);
		
		return "customerDashboard";
	}
	
	
	/**
	 *  Categories related mappings
	 */
	

	@GetMapping("/categories")
	public String getCategories(Model model) {
		List<Category> categories = customservice.getCategories();
		model.addAttribute("name", service.getUserDetails().getName());
		model.addAttribute("categories", categories);
		return "customerCategories";
	}

	@GetMapping("/categories/{title}")
	public String getCategoriesByTitle(Model model, @PathVariable String title) {
		List<Category> categories = customservice.getCategoriesByTitle(title);
		model.addAttribute("name", service.getUserDetails().getName());
		model.addAttribute("categories", categories);
		return "customerCategories";
	}

	/*
	 * View Product related mapping
	 */

	@GetMapping("/allviewproducts")
	public String viewAllProducts(Model model) {

		List<ProductDto> productlist = customservice.getAllProducts();
		if (productlist.size() > 0) {
			model.addAttribute("name", service.getUserDetails().getName());
			model.addAttribute("productList", productlist);
			return "customerViewAllProducts";
		} else {
			model.addAttribute("message", "No Products Available!!");
			return "customerViewAllProducts";
		}
	}

	@GetMapping("/allviewproduct")
	public String findProductByTitle(Model model, @RequestParam String title) {

		List<ProductDto> productlist = customservice.getProductsByTitle(title);
		if (productlist.size() > 0) {
			model.addAttribute("name", service.getUserDetails().getName());
			model.addAttribute("productList", productlist);
			return "customerViewAllProducts";
		} else {
			model.addAttribute("message", "No Products Found!");
			model.addAttribute("secondary", "secondary");
			return "customerViewAllProducts";
		}
	}

	@GetMapping("/{categoryId}/viewproducts")
	public String viewProductByCategory(Model model, @PathVariable int categoryId) {

		List<ProductDto> productlist = customservice.getProductbyCategoryId(categoryId);
		if (productlist.size() > 0) {
			model.addAttribute("name", service.getUserDetails().getName());
			model.addAttribute("productList", productlist);
			return "customerViewProduct";
		} else {
			model.addAttribute("message", "No Products Available!!");
			model.addAttribute("secondary", "secondary");
			return getCategories(model);
		}

	}

	@GetMapping("/viewproducts")
	public String findProductByTitleAndCategory(Model model, @RequestParam String title, @RequestParam int categoryId) {

		List<ProductDto> productlist = customservice.getProductsByTitleAndCategory(title, categoryId);
		if (productlist.size() > 0) {
			model.addAttribute("name", service.getUserDetails().getName());
			model.addAttribute("productList", productlist);
			return "customerViewProduct";
		} else {
			model.addAttribute("message", "No Products Found!");
			model.addAttribute("secondary", "secondary");
			return viewProductByCategory(model, categoryId);
		}
	}

	@GetMapping("/reservation")
	public String getPostReservation(Model model) {

		ArrayList<String> tables = new ArrayList<>();
		tables.add("Standard Table");
		tables.add("Communal Table");
		tables.add("Outdoor Table");
		tables.add("Bistro Table");
		tables.add("Booth Table");
		tables.add("Booth Table");
		model.addAttribute("tables", tables);
		model.addAttribute("name", service.getUserDetails().getName());

		return "customerReservation";
	}

	@PostMapping("/reservation/request")
	public String requestReservation(Model model, @ModelAttribute ReservationDto requestDto,
			RedirectAttributes redirectAttribute) {

		ReservationDto createdRequest = customservice.postReservation(requestDto);

		if (createdRequest != null) {
			redirectAttribute.addFlashAttribute("success", "Request Posted Successfully!");
			return "redirect:/restaurant/customer/reservation";
		}
		redirectAttribute.addFlashAttribute("danger", "Request not posted, Try again!");
		return "redirect:/restaurant/customer/reservation";
	}

	@GetMapping("/myreservations")
	public String getPostCategory(Model model) {

		List<ReservationDto> reservations = customservice.getReservationsByUserId();
		model.addAttribute("reservations", reservations);
		model.addAttribute("name", service.getUserDetails().getName());

		return "customerMyReservation";
	}

	@GetMapping("/myreservation/delete")
	public String deleteByReservationId(Model model, @RequestParam int id, RedirectAttributes redirectAttribute) {

		Boolean response = customservice.deleteByReservationId(id);

		if (response) {
			redirectAttribute.addFlashAttribute("success", "Request Deleted Successfully!");
			return "redirect:/restaurant/customer/myreservations";
		}
		redirectAttribute.addFlashAttribute("danger", "Request Not Deleted!");
		return "redirect:/restaurant/customer/myreservations";
	}

	/*
	 * cart related mappings
	 */

	@GetMapping("/cart")
	public String getAllOrders(Model model) {
		List<CartItemDto> orders = customservice.getOrdersByUserId();
		model.addAttribute("orders", orders);
		model.addAttribute("name", service.getUserDetails().getName());
		return "customerCart";
	}

	@PostMapping("/cart/save")
	public String saveCartItem(Model model, @ModelAttribute ProductDto productDto,
			RedirectAttributes redirectAttribute) {
		CartItemDto resp = customservice.saveCartItem(productDto.getId());
		if (resp != null) {
			redirectAttribute.addFlashAttribute("message", "Product Added to your Cart!");
			redirectAttribute.addFlashAttribute("success", "success");
			return "redirect:/restaurant/customer/" + resp.getCategoryId() + "/viewproducts";
		}
		redirectAttribute.addFlashAttribute("message", "Product not added to cart, Try Again!");
		redirectAttribute.addFlashAttribute("danger", "danger");
		return "redirect:/restaurant/customer/" + productDto.getCategoryId() + "/viewproducts";
	}

	@GetMapping("/cart/remove")
	public String deleteCartItemById(@RequestParam int id, RedirectAttributes redirectAttribute) {

		Boolean resp = customservice.deleteCartItemById(id);
		if (resp) {
			redirectAttribute.addFlashAttribute("success", "Item removed Successfully!");
			return "redirect:/restaurant/customer/cart";
		}
		redirectAttribute.addFlashAttribute("success", "Item not removed, Try Item!");
		return "redirect:/restaurant/customer/cart";
	}

	@GetMapping("/cart/pay")
	public String updateCartStatusById(@RequestParam int id, RedirectAttributes redirectAttribute) {

		CartItemDto resp = customservice.updateCartStatusById(id);
		if (resp != null) {
			redirectAttribute.addFlashAttribute("success", "Payment Successful!");
			return "redirect:/restaurant/customer/cart";
		}

		redirectAttribute.addFlashAttribute("danger", "Payment Failed, Try again!");
		return "redirect:/restaurant/customer/cart";
	}

	
	@GetMapping("/contactus")
	public String getContactus(Model model) {
		model.addAttribute("name", service.getUserDetails().getName());

		return "contactus";
	}
	@GetMapping("/aboutus")
	public String getAboutus(Model model) {
		model.addAttribute("name", service.getUserDetails().getName());

		return "aboutus";
	}
	
	
}
