package com.kopetto.sample.service.order;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.kopetto.sample.domain.entity.order.Order;
import com.kopetto.sample.domain.entity.profile.User;

/**
 *
 */
public interface OrderService {

	Page<Order> getAllOrdersForUser(Pageable pageable, User loginUser);

	void saveOrder(Order order);
	
}