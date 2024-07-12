package com.example.wgwg_auth.service;

import com.example.wgwg_auth.domain.dto.request.CustomerRequest;
import com.example.wgwg_auth.domain.dto.response.CustomerSignInResponse;
import com.example.wgwg_auth.domain.entity.Customer;
import reactor.core.publisher.Mono;

public interface CustomerService {
    Mono<CustomerSignInResponse> saveCustomerInfo(CustomerRequest request);
    Mono<Customer> getCustomerInfo(Long customerId);
}
