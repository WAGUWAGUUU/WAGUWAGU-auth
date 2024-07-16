package com.example.wgwg_auth.service;

import com.example.wgwg_auth.domain.dto.request.CustomerRequest;
import com.example.wgwg_auth.domain.dto.request.CustomerSignInRequest;
import com.example.wgwg_auth.domain.dto.response.CustomerSignInResponse;
import com.example.wgwg_auth.domain.entity.Customer;
import com.example.wgwg_auth.domain.repository.CustomerRepository;
import com.example.wgwg_auth.global.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;
    private final JwtUtil jwtUtil;

    @Override
    public Mono<CustomerSignInResponse> saveCustomerInfo(CustomerSignInRequest request) {
        return customerRepository.findByCustomerEmail(request.toEntity().getCustomerEmail())
                .collectList()
                .flatMap(customers -> {
                    if (customers.size() > 1) {
                        log.error("Multiple customers found with the same email: " + request.toEntity().getCustomerEmail());
                        return Mono.error(new RuntimeException("Non unique result for email: " + request.toEntity().getCustomerEmail()));
                    } else if (customers.size() == 1) {
                        Customer existingCustomer = customers.get(0);
                        log.info("Welcome back, " + existingCustomer.getCustomerNickname() + "!");
                        return getCustomerInfo(existingCustomer.getCustomerId())
                                .flatMap(customer -> {
                                    String token = jwtUtil.generateToken(customer);
                                    CustomerSignInResponse response = CustomerSignInResponse.from(token);
                                    log.info(response.token());
                                    return Mono.just(response);
                                });
                    } else {
                        return customerRepository.insertIfNotExistAndReturn(
                                        request.customerId(),
                                        request.customerNickname(),
                                        request.customerEmail()
                                ).then(customerRepository.save(request.toEntity()))
                                .flatMap(customer -> {
                                    String token = jwtUtil.generateToken(customer);
                                    CustomerSignInResponse response = CustomerSignInResponse.from(token);
                                    log.info(response.token());
                                    return Mono.just(response);
                                });
                    }
                })
                .onErrorResume(e -> {
                    log.error("Error occurred while saving customer info: " + e.getMessage());
                    return Mono.error(e);
                });
    }

    @Override
    public Mono<Customer> getCustomerInfo(Long customerId) {
        return customerRepository.findById(customerId);
    }

    @Override
    public Mono<Customer> updateCustomerInfo(Long customerId, CustomerRequest request) {
        return customerRepository.updateCustomerAddress(
                customerId, request.customerNickname(),request.customerAddress())
                .then(customerRepository.findById(customerId))
                .doOnSuccess(customer -> log.info("Customer address updated successfully for customerId: " + customerId))
                .doOnError(e -> log.error("Error updating customer address for customerId: " + customerId, e));
    }
}
