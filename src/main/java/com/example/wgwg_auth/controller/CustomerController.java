package com.example.wgwg_auth.controller;

import com.example.wgwg_auth.domain.dto.request.CustomerRequest;
import com.example.wgwg_auth.domain.entity.Customer;
import com.example.wgwg_auth.global.utils.JwtUtil;
import com.example.wgwg_auth.service.CustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("customers")
public class CustomerController {
    private final CustomerService customerService;
    private final JwtUtil jwtUtil;


    @GetMapping
    public Mono<Customer> getCustomerInfo(@RequestHeader("Authorization") String token) {
        String bearerToken = token.substring(7);
        Long customerId = jwtUtil.getCustomerFromToken(bearerToken).getCustomerId();
        return customerService.getCustomerInfo(customerId);
    }

    @PutMapping
    public Mono<Customer> updateCustomer
            (@RequestHeader("Authorization") String token,
             @RequestBody CustomerRequest customerRequest) {
        String bearerToken = token.substring(7);
        Long customerId = jwtUtil.getCustomerFromToken(bearerToken).getCustomerId();
        return customerService.updateCustomerInfo(customerId, customerRequest);
    }
}
