package com.example.wgwg_auth.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table("OWNERS")
public class Owner {
    @Id
    @Column("OWNER_ID")
    private Long ownerId;
    @Column("OWNER_NAME")
    private String ownerName;
    @Column("OWNER_EMAIL")
    private String ownerEmail;
    @Column("OWNER_ADDRESS")
    private String ownerAddress;
    @Column("OWNER_LATITUDE")
    private double ownerLatitude;
    @Column("OWNER_LONGITUDE")
    private double ownerLongitude;
}