package com.example.wgwg_auth.service;

import com.example.wgwg_auth.domain.dto.request.CustomerRequest;
import com.example.wgwg_auth.domain.dto.request.UserSignInRequest;
import com.example.wgwg_auth.domain.dto.response.CustomerResponse;
import com.example.wgwg_auth.domain.dto.response.UserSignInResponse;
import com.example.wgwg_auth.domain.entity.Customer;
import reactor.core.publisher.Mono;

public interface CustomerService {
    Mono<UserSignInResponse> saveCustomerInfo(UserSignInRequest request);
    Mono<Customer> getCustomerInfo(Long customerId);
    Mono<CustomerResponse> updateCustomerInfo(Long customerId, CustomerRequest req);
}
