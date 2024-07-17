package com.example.wgwg_auth.service;

import com.example.wgwg_auth.domain.dto.request.CustomerRequest;
import com.example.wgwg_auth.domain.dto.request.UserSignInRequest;
import com.example.wgwg_auth.domain.dto.response.UserSignInResponse;
import com.example.wgwg_auth.domain.entity.Customer;
import com.example.wgwg_auth.domain.repository.CustomerRepository;
import com.example.wgwg_auth.global.utils.JwtUtil;
import com.example.wgwg_auth.global.kafka.CustomerProducer;
import com.example.wgwg_auth.global.kafka.dto.KafkaCustomerDto;
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
    private final CustomerProducer customerProducer;

    @Override
    public Mono<UserSignInResponse> saveCustomerInfo(UserSignInRequest request) {
        return customerRepository.findByCustomerEmail(request.toCustomerEntity().getCustomerEmail())
                .collectList()
                .flatMap(customers -> {
                    if (customers.size() > 1) {
                        log.error("Multiple customers found with the same email: " + request.toCustomerEntity().getCustomerEmail());
                        return Mono.error(new RuntimeException("Non unique result for email: " + request.toCustomerEntity().getCustomerEmail()));
                    } else if (customers.size() == 1) {
                        Customer existingCustomer = customers.get(0);
                        log.info("Welcome back, " + existingCustomer.getCustomerNickname() + "!");
                        return getCustomerInfo(existingCustomer.getCustomerId())
                                .flatMap(customer -> {
                                    String token = jwtUtil.generateCustomerToken(customer);
                                    UserSignInResponse response = UserSignInResponse.from(token);
                                    log.info(response.token());
                                    return Mono.just(response);
                                });
                    } else {
                        return customerRepository.insertIfNotExistAndReturn(
                                        request.userId(),
                                        request.UserNickname(),
                                        request.UserEmail()
                                ).then(customerRepository.save(request.toCustomerEntity()))
                                .flatMap(customer -> {
                                    String token = jwtUtil.generateCustomerToken(customer);
                                    UserSignInResponse response = UserSignInResponse.from(token);
                                    log.info(response.token());
                                    // Kafka 메시지 전송
                                    KafkaCustomerDto dto = new KafkaCustomerDto(customer.getCustomerId(), customer.getCustomerLatitude(), customer.getCustomerLongitude());
                                    customerProducer.sendCustomerInfo(dto, "customer_info_to_store");
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
        return customerRepository.updateCustomerInfo(
                        customerId, request.toEntity().getCustomerNickname(), request.toEntity().getCustomerAddress(),
                        request.toEntity().getCustomerLatitude(), request.toEntity().getCustomerLongitude())
                .then(customerRepository.findById(customerId))
                .doOnSuccess(customer -> {
                    log.info("Customer address updated successfully for customerId: " + customerId);
                    // Kafka 메시지 전송
                    KafkaCustomerDto dto = new KafkaCustomerDto(customer.getCustomerId(), request.toEntity().getCustomerLatitude(),
                            request.toEntity().getCustomerLongitude());
                    customerProducer.sendCustomerInfo(dto, "customer_info_to_store");
                })
                .doOnError(e -> log.error("Error updating customer address for customerId: " + customerId, e));
    }
}
