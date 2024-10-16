package com.javaprojects.ecom_proj.controller;

import com.javaprojects.ecom_proj.model.Product;
import com.javaprojects.ecom_proj.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class ProductController {
    @Autowired
    private  ProductService service;
    @GetMapping("/product")
    public ResponseEntity<List<Product>> getAllProducts()
    {
        
        return new ResponseEntity<>(service.getAllProducts(),HttpStatus.OK);
    }
    @GetMapping("/product/{id}")
    public ResponseEntity<Optional<Product>> getProductById(@PathVariable int id) {
        Optional<Product> product = service.getProductsById(id);
        if (product.isPresent()) {
            return new ResponseEntity<>(product, HttpStatus.OK);  // Return 200 OK with product
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);    // Return 404 Not Found
        }
    }
    @PostMapping("/product")
    public ResponseEntity<?> addProduct(@RequestPart Product product,
                                    @RequestPart MultipartFile imageFile) {
       try {
           Product product1=service.addProduct(product,imageFile);
           return new ResponseEntity<>(product1,HttpStatus.CREATED);
       }
       catch(Exception e){
           return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
       }
    }
    @GetMapping("/product/{productId}/image")
    public ResponseEntity<byte[]>getImageByProductId(@PathVariable int productId){
        Optional<Product> product=service.getProductsById(productId);
        byte[] imageFile = product.get().getImageData();
        return ResponseEntity.ok()
                .body(imageFile);
    }
    @PutMapping("/product/{id}")
    public ResponseEntity<String> updateProduct(
            @PathVariable int id,
            @RequestPart Product product,
            @RequestPart MultipartFile imageFile) {
        Product product1 = null;
        try {
            product1 = service.updateProduct(id, product, imageFile);
        } catch (IOException e) {
            return new ResponseEntity<>("Failed to update", HttpStatus.BAD_REQUEST);
        }
        if (product1 != null) {
            return new ResponseEntity<>("Product updated", HttpStatus.OK);
        } else
            return new ResponseEntity<>("Failed to update", HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("/product/{productId}")
    public ResponseEntity<String> deleteProductById(@PathVariable int productId) {
        try {
            // Call the service to delete the product by ID
            service.deleteProductsById(productId);
            return new ResponseEntity<>("Product deleted successfully", HttpStatus.OK);  // Return 200 OK with success message
        } catch (Exception e) {
            return new ResponseEntity<>("Error deleting product: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
@GetMapping("/products/search")
    public ResponseEntity<List<Product>> searchProducts(@RequestParam String keyword){
    List<Product>products=service.searchProducts(keyword);
    return new ResponseEntity<>(products,HttpStatus.OK);

}
}

