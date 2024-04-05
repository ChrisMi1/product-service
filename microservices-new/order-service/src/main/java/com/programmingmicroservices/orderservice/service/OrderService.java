package com.programmingmicroservices.orderservice.service;

import com.programmingmicroservices.orderservice.config.WebClientConfig;
import com.programmingmicroservices.orderservice.dto.InventoryResponse;
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
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrderService {
    private final OrderRepository orderRepository;
    private final WebClient webClient;

    @Autowired
    public OrderService(OrderRepository orderRepository,WebClient webClient) {
        this.orderRepository = orderRepository;
        this.webClient=webClient;
    }

    public void placeOrder(OrderRequest orderRequest){
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());

        List<OrderLineItems> orderLineItems=  orderRequest.getOrderLineItemsDto()
                .stream()
                .map(orderLineItemsDto -> toDto(orderLineItemsDto)).collect(Collectors.toList());
        order.setOrderLineItems(orderLineItems);

        List<String> skuCodes=  order.getOrderLineItems().stream().map(OrderLineItems::getSkuCode).collect(Collectors.toList());
        //call inventory service and place order if product is in stock
        InventoryResponse[] inventoryResponses = webClient.get()
                        .uri("http://localhost:8082/api/inventory",uriBuilder -> uriBuilder.queryParam("skuCodes",skuCodes).build())
                        .retrieve()
                        .bodyToMono(InventoryResponse[].class)
                        .block();
        boolean allProductsInStock= Arrays.stream(inventoryResponses).allMatch(InventoryResponse::isInStock);
        if(allProductsInStock) {
            orderRepository.save(order);
        }else{
            throw new IllegalArgumentException("Product is not in stock,please try again later ");
        }
    }

    public OrderLineItems toDto(OrderLineItemsDto orderLineItemsDto){
        OrderLineItems orderLineItems=  new OrderLineItems();
        orderLineItems.setPrice(orderLineItemsDto.getPrice());
        orderLineItems.setQuantity(orderLineItemsDto.getQuantity());
        orderLineItems.setSkuCode(orderLineItemsDto.getSkuCode());
        return orderLineItems;
    }
}
