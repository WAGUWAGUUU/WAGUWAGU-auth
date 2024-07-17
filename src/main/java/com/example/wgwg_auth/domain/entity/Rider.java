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
@Table("RIDERS")
public class Rider {
    @Id
    @Column("RIDER_ID")
    private Long riderId;
    @Column("RIDER_NICKNAME")
    private String riderNickname;
    @Column("RIDER_EMAIL")
    private String riderEmail;
    @Column("RIDER_PHONE")
    private String riderPhone;
    @Column("RIDER_ACTIVITY_AREA")
    private String riderActivityArea;
    @Column("RIDER_ACTIVATE")
    private Boolean riderActivate;
    @Column("RIDER_TRANSPORTATION")
    private String riderTransportation;
    @Column("RIDER_ADDRESS")
    private String riderAddress;
    @Column("RIDER_LATITUDE")
    private double riderLatitude;
    @Column("RIDER_LONGITUDE")
    private double riderLongitude;
}
