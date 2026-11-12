package com.inventory.stresstester.service;
import com.inventory.stresstester.entity.Product;
import com.inventory.stresstester.repository.ProductRepository;
import org.springframework.stereotype.Service;
@Service
public class ProductService extends CrudService<Product> {
    public ProductService(ProductRepository repository) { super(repository); }
}
