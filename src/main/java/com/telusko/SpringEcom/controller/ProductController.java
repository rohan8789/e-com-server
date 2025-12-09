package com.telusko.SpringEcom.controller;

import com.telusko.SpringEcom.model.Product;
import com.telusko.SpringEcom.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Stream;

@RestController
@RequestMapping("/api")
@CrossOrigin

public class ProductController {

    @Autowired
    ProductService productservice;

    @GetMapping("/products")
    public ResponseEntity<List<Product>> getProducts(){
        return new ResponseEntity<>(productservice.getAllProduct(), HttpStatus.OK); //sending status code logic
    }


    @PostMapping("/product")
    public ResponseEntity<Product> addProduct(@RequestPart Product product, @RequestPart MultipartFile imageFile) throws IOException {
        Product savedProduct=null;
        System.out.println("Finally we are here...");
        if(product!=null && imageFile!=null){
            savedProduct = productservice.addProdToDB(product, imageFile);
            System.out.println("Product saved");
            return new ResponseEntity<>(savedProduct, HttpStatus.CREATED);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);

    }  //RequesBody for normaldata, but image here


    @PutMapping("/product/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable int id, @RequestPart Product product, @RequestPart MultipartFile imageFile) throws IOException {
        Product foundProd = productservice.getProductById(id);
        Product updatedProd = null;
        try {
            updatedProd = productservice.updateThis(foundProd, product, imageFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if(updatedProd!=null){
            return new ResponseEntity<>(updatedProd, HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("/product/{id}")

    public ResponseEntity<String>deleteProduct(@PathVariable int id){
        Product findProd = productservice.getProductById(id);
        if(findProd!=null){
            productservice.deleteThis(findProd.getId());
            return new ResponseEntity<>("deleted", HttpStatus.OK);
        }
        return new ResponseEntity<>("failed to delete", HttpStatus.BAD_REQUEST);
    }



    @GetMapping("/product/{id}")
    public ResponseEntity<Product> getProduct(@PathVariable int id){
        Product product = productservice.getProductById(id);
        if(product!=null){
            return new ResponseEntity<>(product, HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @GetMapping("/product/{productId}/image")
    public ResponseEntity<byte[]> getProductImageById(@PathVariable int productId){

        System.out.println("code control reached finding image, /prod/id/image");
        Product product=productservice.getProductById(productId);
        if(product!=null){
            return new ResponseEntity<>(product.getImageData(), HttpStatus.OK);
        }
        return new ResponseEntity<>(null , HttpStatus.NOT_FOUND);
    }


    @GetMapping("/products/search")
    public ResponseEntity<List<Product>> searchProduct(@RequestParam String keyword){
        List<Product> products = productservice.getAllProduct();
        List<Product> newProd=null;
        Stream<Product> prodStream=products.stream().filter(product-> {
            if(product.getBrand().contains(keyword)||product.getName().contains(keyword))return true;
            return false;
        });
        newProd = prodStream.toList();

        System.out.println("Typed value is: "+keyword);
        return new ResponseEntity<>(newProd, HttpStatus.OK);
    }
}
