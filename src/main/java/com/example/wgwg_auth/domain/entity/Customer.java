package com.example.wgwg_auth.domain.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table("CUSTOMERS")
public class Customer  {
    @Id
    @Column("CUSTOMER_ID")
    private Long customerId;
    @Column("CUSTOMER_NICKNAME")
    private String customerNickname;
    @Column("CUSTOMER_EMAIL")
    private String customerEmail;
    @Column("CUSTOMER_ADDRESS")
    private String customerAddress;
}
