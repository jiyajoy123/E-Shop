package com.javaprojects.ecom_proj.service;

import com.javaprojects.ecom_proj.model.Product;
import com.javaprojects.ecom_proj.repo.ProductRepo;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    @Autowired
    ProductRepo repo;
    public List<Product> getAllProducts() {
        return repo.findAll();
    }

    public Optional<Product> getProductsById(int id) {
        return Optional.ofNullable(repo.findById(id).orElse(null));
    }

    public Product addProduct(Product product, MultipartFile imageFile) throws IOException {
  product.setImageName(imageFile.getOriginalFilename());
  product.setImageType(imageFile.getContentType());
  product.setImageData(imageFile.getBytes());
   return repo.save(product);
    }

    
    public void deleteProductsById(int productId) {
        // Check if the product exists before deleting
        if (repo.existsById(productId)) {
            repo.deleteById(productId);
        } else {
            throw new EntityNotFoundException("Product not found with ID: " + productId);
        }
    }

    public Product updateProduct(int id, Product product, MultipartFile imageFile) throws IOException {
    product.setImageName(imageFile.getOriginalFilename());
    product.setImageData(imageFile.getBytes());
    product.setImageType(imageFile.getContentType());
    return  repo.save(product);
    }

    public List<Product> searchProducts(String keyword) {
        return repo.searchProducts(keyword);
    }
}
