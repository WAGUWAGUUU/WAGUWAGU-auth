package com.example.wgwg_auth.domain.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
//import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.List;

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
    @Column("CUSTOMER_LATITUDE")
    private double customerLatitude;
    @Column("CUSTOMER_LONGITUDE")
    private double customerLongitude;

//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        return List.of(()->"ROLE_USER");
//    }
}
