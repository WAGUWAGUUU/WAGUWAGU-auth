package com.example.wgwg_auth.service;

import com.example.wgwg_auth.domain.dto.request.CustomerRequest;
import com.example.wgwg_auth.domain.dto.request.CustomerSignInRequest;
import com.example.wgwg_auth.domain.dto.response.CustomerSignInResponse;
import com.example.wgwg_auth.domain.entity.Customer;
import reactor.core.publisher.Mono;

public interface CustomerService {
    Mono<CustomerSignInResponse> saveCustomerInfo(CustomerSignInRequest request);
    Mono<Customer> getCustomerInfo(Long customerId);
    Mono<Customer> updateCustomerInfo(Long customerId, CustomerRequest req);
}
