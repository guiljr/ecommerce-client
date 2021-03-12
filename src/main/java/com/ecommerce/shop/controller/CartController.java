package com.ecommerce.shop.controller;

import com.ecommerce.shop.cart.ShoppingCartService;
import com.ecommerce.shop.client.EcommerceWebClient;
import com.ecommerce.shop.product.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class CartController {
    private static final Logger logger = LoggerFactory.getLogger(CartController.class);
    private final ShoppingCartService shoppingCartService;

    @Autowired
    EcommerceWebClient ecommerceWebClient;

    @Autowired
    public CartController(ShoppingCartService shoppingCartService) {
        this.shoppingCartService = shoppingCartService;
    }

    @GetMapping("/cart")
    public String cart(Model model){
        model.addAttribute("products", shoppingCartService.productsInCart());
        model.addAttribute("totalPrice", shoppingCartService.totalPrice());

        return "cart";
    }

    @GetMapping("/cart/add/{id}")
    public String addProductToCart(@PathVariable("id") int id){

        try {
            Product product = ecommerceWebClient.getItem(id);

            if (product != null){
                shoppingCartService.addProduct(product);
                logger.debug(String.format("Product with id: %s added to shopping cart.", id));
            }
        } catch (Exception ex) {
            logger.debug(String.format("Product with id: %s not found", id));
        }

        return "redirect:/home";
    }

    @GetMapping("/cart/remove/{id}")
    public String removeProductFromCart(@PathVariable("id") int id){

        Product product = ecommerceWebClient.getItem(id);
        if (product != null){
            shoppingCartService.removeProduct(product);
            logger.debug(String.format("Product with id: %s removed from shopping cart.", id));
        }
        return "redirect:/cart";
    }

    @GetMapping("/cart/clear")
    public String clearProductsInCart(){
        shoppingCartService.clearProducts();

        return "redirect:/cart";
    }

    @GetMapping("/cart/back")
    public String backToProducts(){

        return "redirect:/home";
    }

    @GetMapping("/cart/checkout")
    public String cartCheckout(){
        //call transaction api
        ecommerceWebClient.createTxn(shoppingCartService.totalPrice());
        shoppingCartService.cartCheckout();
        return "redirect:/cart";
    }
}
