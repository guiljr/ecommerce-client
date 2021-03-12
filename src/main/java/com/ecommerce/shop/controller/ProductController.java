package com.ecommerce.shop.controller;

import com.ecommerce.shop.client.EcommerceWebClient;
import com.ecommerce.shop.product.Product;
import com.ecommerce.shop.product.ProductValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ProductController {
    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);
    private final ProductValidator productValidator;

    @Autowired
    EcommerceWebClient ecommerceWebClient;

    @Autowired
    public ProductController(ProductValidator productValidator) {
        this.productValidator = productValidator;
    }

    @GetMapping("/product/new")
    public String newProduct(Model model) {
        model.addAttribute("productForm", new Product());
        model.addAttribute("method", "new");
        return "product";
    }

    @PostMapping("/product/new")
    public String newProduct(@ModelAttribute("productForm") Product productForm, BindingResult bindingResult, Model model) {
        productValidator.validate(productForm, bindingResult);

        if (bindingResult.hasErrors()) {
            logger.error(String.valueOf(bindingResult.getFieldError()));
            model.addAttribute("method", "new");
            return "product";
        }

        ecommerceWebClient.createItem(productForm);
        logger.debug(String.format("Product with id: %s successfully created.", productForm.getId()));

        return "redirect:/home";
    }

    @GetMapping("/product/edit/{id}")
    public String editProduct(@PathVariable("id") int productId, Model model){

        Product product = ecommerceWebClient.getItem(productId);
        if (product != null){
            model.addAttribute("productForm", product);
            model.addAttribute("method", "edit");
            return "product";
        }else {
            return "error/404";
        }
    }

    @PostMapping("/product/edit/{id}")
    public String editProduct(@PathVariable("id") int productId, @ModelAttribute("productForm") Product productForm, BindingResult bindingResult, Model model){
        productValidator.validate(productForm, bindingResult);

        if (bindingResult.hasErrors()) {
            logger.error(String.valueOf(bindingResult.getFieldError()));
            model.addAttribute("method", "edit");
            return "product";
        }

        ecommerceWebClient.editItem(productId, productForm);
        logger.debug(String.format("Product with id: %s has been successfully edited.", productId));

        return "redirect:/home";
    }

    @PostMapping("/product/delete/{id}")
    public String deleteProduct(@PathVariable("id") int productId){

        Product product = ecommerceWebClient.getItem(productId);
        if (product != null){
            ecommerceWebClient.deleteItem(productId);
           logger.debug(String.format("Product with id: %s successfully deleted.", product.getId()));
           return "redirect:/home";
        }else {
            return "error/404";
        }
    }
}
