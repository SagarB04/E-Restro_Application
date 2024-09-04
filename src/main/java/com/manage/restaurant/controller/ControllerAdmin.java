package com.manage.restaurant.controller;

import java.io.IOException;
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
import com.manage.restaurant.dtos.CategoryDto;
import com.manage.restaurant.dtos.ProductDto;
import com.manage.restaurant.dtos.ReservationDto;
import com.manage.restaurant.entity.Category;
import com.manage.restaurant.entity.Product;
import com.manage.restaurant.service.ServiceAdmin;
import com.manage.restaurant.service.UserService;

@Controller
@RequestMapping("/restaurant/admin")
public class ControllerAdmin {

	@Autowired
	UserService service;

	@Autowired
	ServiceAdmin adservice;

	/**
	 * Dashboard related mappings
	 */
	
	@GetMapping("/dashboard")
	public String getDashboard(Model model) {
		
		long catCount = adservice.getCategoriesCount();
		long prodCount = adservice.getProductsCount();
		long resCount = adservice.getReservationsCount();
		long orderCount = adservice.getOrdersCount();

		model.addAttribute("name", service.getUserDetails().getName());
		model.addAttribute("catCount", catCount);
		model.addAttribute("prodCount", prodCount);
		model.addAttribute("resCount", resCount);
		model.addAttribute("orderCount", orderCount);
		
		return "adminDashboard";
	}
	
	
	/**
	 *  Categories related mappings
	 */
	

	@GetMapping("/categories")
	public String getCategories(Model model) {
		List<Category> categories = adservice.getCategories();
		model.addAttribute("name", service.getUserDetails().getName());
		model.addAttribute("categories", categories);
		return "adminCategories";
	}

	@GetMapping("/categories/{title}")
	public String getCategoriesByTitle(Model model, @PathVariable String title) {
		List<Category> categories = adservice.getCategoriesByTitle(title);
		model.addAttribute("name", service.getUserDetails().getName());
		model.addAttribute("categories", categories);
		return "adminCategories";
	}

	/*
	 * Post Category related mappings
	 */

	@GetMapping("/postcategory")
	public String postCategory(Model model) {
		model.addAttribute("name", service.getUserDetails().getName());
		return "adminPostCategory";
	}

	@PostMapping("/postcategory")
	public String getPostCategory(@ModelAttribute CategoryDto catdto, Model model,RedirectAttributes redirectAttribute) throws IOException {
		Category cat = adservice.saveCategory(catdto);
		if (cat == null) {
			redirectAttribute.addFlashAttribute("danger", "Category not added, Try Again");
		} else {
			redirectAttribute.addFlashAttribute("success", "Category added successfully!");
		}
		return "redirect:/restaurant/admin/postcategory";
	}

	/**
	 * Product related mappings
	 */

	@GetMapping("/{id}/products")
	public String getAddProduct(Model model, @PathVariable int id) {
		model.addAttribute("name", service.getUserDetails().getName());
		model.addAttribute("categoryId", id);
		return "adminAddProducts";
	}

	@PostMapping("/save/product")
	public String postProducts(Model model, @ModelAttribute ProductDto proddto) {
		model.addAttribute("name", service.getUserDetails().getName());
		Product createdProduct = adservice.saveProduct(proddto);

		if (createdProduct != null) {

			if (proddto.getId() == createdProduct.getId()) {
				model.addAttribute("message", "Product updated succesfully!");
				model.addAttribute("success", "success");
				return viewProductByCategory(model, createdProduct.getCategory().getId());
			}
			model.addAttribute("message", "Product Added Successfully!");
			return getCategories(model);

		} else {
			model.addAttribute("message", "Product not added, Try again!");
			return "adminAddProducts";
		}
	}

	/*
	 * View Product related mapping
	 */
	
	@GetMapping("/allviewproducts")
	public String viewAllProducts(Model model) {

		List<ProductDto> productlist = adservice.getAllProducts();
		if (productlist.size() > 0) {
			model.addAttribute("name", service.getUserDetails().getName());
			model.addAttribute("productList", productlist);
			return "adminViewAllProducts";
		} else {
			model.addAttribute("message", "No Products Available!!");
			return "adminViewAllProducts";
		}
	}

	@GetMapping("/allviewproduct")
	public String findProductByTitle(Model model, @RequestParam String title) {

		List<ProductDto> productlist = adservice.getProductsByTitle(title);
		if (productlist.size() > 0) {
			model.addAttribute("name", service.getUserDetails().getName());
			model.addAttribute("productList", productlist);
			return "adminViewAllProducts";
		} else {
			model.addAttribute("message", "No Products Found!");
			model.addAttribute("secondary", "secondary");
			return "adminViewAllProducts";
		}
	}

	@GetMapping("/{categoryId}/viewproducts")
	public String viewProductByCategory(Model model, @PathVariable int categoryId) {

		List<ProductDto> productlist = adservice.getProductbyCategoryId(categoryId);
		if (productlist.size() > 0) {
			model.addAttribute("name", service.getUserDetails().getName());
			model.addAttribute("productList", productlist);
			return "adminViewProduct";
		} else {
			model.addAttribute("message", "No Products Available!!");
			return getCategories(model);
		}

	}

	@GetMapping("/viewproducts")
	public String findProductByTitleAndCategory(Model model, @RequestParam String title, @RequestParam int categoryId) {

		List<ProductDto> productlist = adservice.getProductsByTitleAndCategory(title, categoryId);
		if (productlist.size() > 0) {
			model.addAttribute("name", service.getUserDetails().getName());
			model.addAttribute("productList", productlist);
			return "adminViewProduct";
		} else {
			model.addAttribute("message", "No Products Found!");
			model.addAttribute("secondary", "secondary");
			return viewProductByCategory(model, categoryId);
		}
	}

	@PostMapping("/product/delete")
	public String deleteProductById(Model model, @RequestParam(name = "id") int productId,
			@RequestParam int categoryId) {

		Boolean response = adservice.deleteProductById(productId);
		if (response) {
			model.addAttribute("message", "Product Deleted!");
			model.addAttribute("success", "success");
		} else {
			model.addAttribute("message", "Product not Deleted!");
			model.addAttribute("danger", "danger");
		}
		return viewProductByCategory(model, categoryId);
	}

	@PostMapping("/product/update")
	public String updateProductById(Model model, @RequestParam(name = "id") int productId,
			@RequestParam int categoryId) {

		ProductDto response = adservice.getProductById(productId);
		if (response != null) {
			model.addAttribute("product", response);
			return getAddProduct(model, response.getCategoryId());
		} else {
			model.addAttribute("message", "Something went wrong, Try again later!");
			model.addAttribute("secondary", "secondary");
		}
		return viewProductByCategory(model, categoryId);
	}

	/**
	 * reservations related mappings
	 */

	@GetMapping("/reservations")
	public String getReservations(Model model) {

		List<ReservationDto> reservations = adservice.getAllReservations();
		model.addAttribute("reservations", reservations);
		model.addAttribute("name", service.getUserDetails().getName());

		return "adminReservation";
	}

	@GetMapping("/reservation/approve")
	public String approveByReservationId(Model model, @RequestParam int id, RedirectAttributes redirectAttribute) {

		Boolean response = adservice.approveByReservationId(id);

		if (response) {
			redirectAttribute.addFlashAttribute("success", "Request Approved Successfully!");
			return "redirect:/restaurant/admin/reservations";
		}
		redirectAttribute.addFlashAttribute("danger", "Request Not Disapproved, Try Again!");
		return "redirect:/restaurant/admin/reservations";
	}

	@GetMapping("/reservation/disapprove")
	public String disapproveByReservationId(Model model, @RequestParam int id, RedirectAttributes redirectAttribute) {

		Boolean response = adservice.disapproveByReservationId(id);

		if (response) {
			redirectAttribute.addFlashAttribute("success", "Request Disapproved Successfully!");
			return "redirect:/restaurant/admin/reservations";
		}
		redirectAttribute.addFlashAttribute("danger", "Request Not Disapproved, Try Again!");
		return "redirect:/restaurant/admin/reservations";
	}

	@GetMapping("/orders")
	public String getAllOrders(Model model) {
		List<CartItemDto> orders = adservice.getAllOrders();
		model.addAttribute("orders", orders);
		model.addAttribute("name", service.getUserDetails().getName());
		return "adminOrders";
	}

	@GetMapping("/order/delivered")
	public String setDeliveredStatusById(@RequestParam int id, RedirectAttributes redirectAttribute) {

		CartItemDto resp = adservice.setDeliveredStatusById(id);
		if (resp != null) {
			redirectAttribute.addFlashAttribute("success", "Item Delivered Successfully!");
			return "redirect:/restaurant/admin/orders";
		}
		redirectAttribute.addFlashAttribute("success", "Item not delivered, Try again!");
		return "redirect:/restaurant/admin/orders";
	}

	@GetMapping("/order/rejected")
	public String setRejectedStatusById(@RequestParam int id, RedirectAttributes redirectAttribute) {

		CartItemDto resp = adservice.setRejectedStatusById(id);
		if (resp != null) {
			redirectAttribute.addFlashAttribute("success", "Item Rejected Successfully!");
			return "redirect:/restaurant/admin/orders";
		}

		redirectAttribute.addFlashAttribute("danger", "Item not rejected, Try again!");
		return "redirect:/restaurant/admin/orders";
	}

}
