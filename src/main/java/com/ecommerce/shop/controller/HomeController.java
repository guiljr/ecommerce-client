package com.ecommerce.shop.controller;

import com.ecommerce.shop.product.Product;
import com.ecommerce.shop.client.EcommerceWebClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {

    @Autowired
    EcommerceWebClient ecommerceWebClient;

    @GetMapping(value = {"/","/index","/home"})
    public String home(Model model){
        model.addAttribute("products", getAllProducts());
        model.addAttribute("productsCount", productsCount());

        return "home";
    }

    @GetMapping("/about")
    public String about(){
        return "about";
    }

    private List<Product> getAllProducts(){

        List<Product> products = ecommerceWebClient.getAllItems();

        return products;
    }

    private long productsCount(){
        return ecommerceWebClient.countItems();

    }
}
