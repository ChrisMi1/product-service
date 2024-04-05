package com.programmingmicroservices.orderservice.service;

import com.programmingmicroservices.orderservice.dto.OrderLineItemsDto;
import com.programmingmicroservices.orderservice.dto.OrderRequest;
import com.programmingmicroservices.orderservice.model.Order;
import com.programmingmicroservices.orderservice.model.OrderLineItems;
import com.programmingmicroservices.orderservice.repository.OrderRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrderService {
    private final OrderRepository orderRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public void placeOrder(OrderRequest orderRequest){
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());

        List<OrderLineItems> orderLineItems=  orderRequest.getOrderLineItemsDto()
                .stream()
                .map(orderLineItemsDto -> toDto(orderLineItemsDto)).collect(Collectors.toList());
        order.setOrderLineItems(orderLineItems);
        //call inventory service and place order if product is in stock
        orderRepository.save(order);
    }

    public OrderLineItems toDto(OrderLineItemsDto orderLineItemsDto){
        OrderLineItems orderLineItems=  new OrderLineItems();
        orderLineItems.setPrice(orderLineItemsDto.getPrice());
        orderLineItems.setQuantity(orderLineItemsDto.getQuantity());
        orderLineItems.setSkuCode(orderLineItemsDto.getSkuCode());
        return orderLineItems;
    }
}
