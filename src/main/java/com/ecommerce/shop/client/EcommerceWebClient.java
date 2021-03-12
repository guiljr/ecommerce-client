package com.ecommerce.shop.client;

import com.ecommerce.shop.product.Product;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EcommerceWebClient {

private final WebClient webClient;

    private static final ObjectMapper mapper = new ObjectMapper();

    @Autowired
    public EcommerceWebClient() {

        this.webClient = WebClient.builder()
            .baseUrl("http://localhost:9090")
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .defaultHeader(HttpHeaders.USER_AGENT, "Spring Client")
            .build();
    }

    public List<Product> getAllItems() {
        Mono<Object[]> response = this.webClient.get().uri("/item/all")
                .retrieve()
                .bodyToMono(Object[].class)
                .log();

        Object[] objects = response.block();

        return Arrays.stream(objects)
                .map(object -> mapper.convertValue(object, Product.class))
                .collect(Collectors.toList());

    }

    public Long countItems() {
        return this.webClient.get()
                .uri("/item/count")
                .retrieve()
                .bodyToMono(Long.class)
                .block();
    }

    public Product getItem(int id) {
        Product product = this.webClient.get().uri("/item/{id}", id)
                .retrieve()
                .bodyToMono(Product.class)
                .block();

        return product;
    }
    
    public void deleteItem(int id) {
        this.webClient.delete()
                .uri("/item/{id}", id)
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }

    public Product editItem(int id, Product product) {
        Product editedProduct = this.webClient.put().uri("/item/{id}", id)
                .body(Mono.just(product), Product.class)
                .retrieve()
                .bodyToMono(Product.class)
                .block();

        return editedProduct;
    }

    public Product createItem(Product product) {
        Product newProduct = this.webClient.post()
                .uri("/item")
                .body(Mono.just(product), Product.class)
                .retrieve()
                .bodyToMono(Product.class)
                .block();

        return newProduct;
    }

    public void createTxn(BigDecimal totalPrice) {

        TransactionDetails details = new TransactionDetails();
        details.setTotalPrice(totalPrice);
        this.webClient.post()
                .uri("/txn")
                .body(Mono.just(details), TransactionDetails.class)
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }

    public class TransactionDetails {
        private BigDecimal totalPrice;

        public BigDecimal getTotalPrice() {
            return totalPrice;
        }

        public void setTotalPrice(BigDecimal totalPrice) {
            this.totalPrice = totalPrice;
        }
    }



}
