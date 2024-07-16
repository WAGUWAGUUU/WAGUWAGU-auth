package com.example.wgwg_auth.domain.repository;

import com.example.wgwg_auth.domain.entity.Customer;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CustomerRepository extends ReactiveCrudRepository<Customer,Long> {
    @Query("INSERT INTO CUSTOMERS (CUSTOMER_ID, CUSTOMER_NICKNAME, CUSTOMER_EMAIL) " +
            "VALUES (:customerId, :customerNickname, :customerEmail) " +
            "ON DUPLICATE KEY UPDATE CUSTOMER_EMAIL = CUSTOMER_EMAIL ")
    Mono<Void> insertIfNotExistAndReturn(Long customerId, String customerNickname, String customerEmail);

    @Query("SELECT CUSTOMER_ID, CUSTOMER_NICKNAME, CUSTOMER_EMAIL, CUSTOMER_ADDRESS FROM CUSTOMERS WHERE CUSTOMER_EMAIL = :customerEmail")
    Flux<Customer> findByCustomerEmail(String customerEmail);

    @Query("UPDATE CUSTOMERS SET CUSTOMER_ADDRESS = :customerAddress, " +
            "CUSTOMER_NICKNAME = :customerNickname WHERE CUSTOMER_ID = :customerId")
    Mono<Customer> updateCustomerAddress(Long customerId, String customerNickname, String customerAddress);
}
