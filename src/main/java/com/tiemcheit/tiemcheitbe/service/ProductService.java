package com.tiemcheit.tiemcheitbe.service;

import com.tiemcheit.tiemcheitbe.model.Product;
import com.tiemcheit.tiemcheitbe.repository.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    private final ProductRepo productRepo;

    @Autowired
    public ProductService(ProductRepo productRepo) {
        this.productRepo = productRepo;
    }

    public Product getProductById(Long id) {
        Product p = productRepo.findById(id).get();
        //System.out.println(p.getName());
//        return productRepo.getReferenceById(id);
        return p;
    }

}
