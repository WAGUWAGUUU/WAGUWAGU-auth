package com.example.wgwg_auth.domain.repository;

import com.example.wgwg_auth.domain.entity.Customer;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface CustomerRepository extends ReactiveCrudRepository<Customer,Long> {
}
