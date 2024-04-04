package com.programmingmicroservices.productservice.Service;

import com.programmingmicroservices.productservice.Repository.ProductRepository;
import com.programmingmicroservices.productservice.dto.ProductRequest;
import com.programmingmicroservices.productservice.dto.ProductResponse;
import com.programmingmicroservices.productservice.model.Product;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ProductService {
    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public void createProduct(ProductRequest productRequest){
        Product product= Product.builder()
                .name(productRequest.getName())
                .description(productRequest.getDescription())
                .price(productRequest.getPrice()).build();
        productRepository.save(product);
        log.info("Product "+product.getName()+"is saved with id + "+product.getId());
    }

    public List<ProductResponse> getAllProducts(){
        List<Product> products=  productRepository.findAll();
        return products.stream().map(this::mapToProductResponse).collect(Collectors.toList());
    }

    private ProductResponse mapToProductResponse(Product product){
        return ProductResponse.builder()
                .id(product.getId())
                .description(product.getDescription())
                .name(product.getName())
                .price(product.getPrice()).build();
    }
}
