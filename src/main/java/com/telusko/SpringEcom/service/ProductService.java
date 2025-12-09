package com.telusko.SpringEcom.service;

import com.telusko.SpringEcom.model.Product;
import com.telusko.SpringEcom.repository.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;


@Service
public class ProductService {

    @Autowired
    ProductRepo productRepo;
    public List<Product> getAllProduct(){
        return productRepo.findAll();
    }

    public Product getProductById(int id){
        Optional<Product> optID = productRepo.findById(id);
        return optID.orElse(null);
    }

    public Product addProdToDB(Product product, MultipartFile imageFile) throws IOException {
        //we have to add image to our data model.
        product.setImageName(imageFile.getOriginalFilename());
        product.setImageType(imageFile.getContentType());
        product.setImageData(imageFile.getBytes());

        return productRepo.save(product);
    }

    public Product updateThis(Product foundProd, Product product, MultipartFile imageFile) throws IOException {

        foundProd.setBrand(product.getBrand());
        foundProd.setCategory(product.getCategory());
        foundProd.setDescription(product.getDescription());
        foundProd.setName(product.getName());
        foundProd.setPrice(product.getPrice());
        foundProd.setProductAvailable(product.isProductAvailable());
        foundProd.setReleaseDate(product.getReleaseDate());
        foundProd.setStockQuantity(product.getStockQuantity());

        foundProd.setImageType(imageFile.getContentType());
        foundProd.setImageName(imageFile.getOriginalFilename());
        foundProd.setImageData(imageFile.getBytes());

        return productRepo.save(foundProd);

    }

    public void deleteThis(int id) {
        productRepo.deleteById(id);
    }
}
