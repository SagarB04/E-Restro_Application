package com.manage.restaurant.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.manage.restaurant.entity.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer>{

	List<Category> findAllByNameContainingIgnoreCase(String title);

}
