package com.kopetto.sample.service.order;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.kopetto.sample.domain.dao.order.OrderRepository;
import com.kopetto.sample.domain.entity.order.Order;
import com.kopetto.sample.domain.entity.profile.User;


/**
* Implementation for OrderService.
*
*/
@Service
public class OrderServiceImpl implements OrderService{
    final static Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

    @Autowired
    private OrderRepository orderRepository;
    
	@Override
	public Page<Order> getAllOrdersForUser(Pageable pageable, User user) {
		// TODO Auto-generated method stub
		return orderRepository.findByUserId (pageable, user.getFbUserName());
	}

	@Override
	public void saveOrder(Order order) {
		orderRepository.save(order);
	}


    
}
