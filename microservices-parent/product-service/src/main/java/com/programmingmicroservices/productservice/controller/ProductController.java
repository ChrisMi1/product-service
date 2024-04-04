package com.programmingmicroservices.productservice.controller;

import com.programmingmicroservices.productservice.Repository.ProductRepository;
import com.programmingmicroservices.productservice.Service.ProductService;
import com.programmingmicroservices.productservice.dto.ProductRequest;
import com.programmingmicroservices.productservice.dto.ProductResponse;
import com.programmingmicroservices.productservice.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product")
public class ProductController {

    private final ProductService productService;
    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createProduct(@RequestBody ProductRequest productRequest){
        productService.createProduct(productRequest);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ProductResponse> getAllProducts(){
        return productService.getAllProducts();
    }
}
