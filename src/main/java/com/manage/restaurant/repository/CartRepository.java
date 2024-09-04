package com.manage.restaurant.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.manage.restaurant.entity.CartItem;

@Repository
public interface CartRepository extends JpaRepository<CartItem, Integer> {

	List<CartItem> findAllByUserId(int id);

	long countByUserId(int id);
	
}
