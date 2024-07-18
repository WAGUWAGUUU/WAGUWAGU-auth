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
@Table("RIDER_ACTIVITY_AREAS")
public class RiderActivityArea {
    @Id
    @Column("RIDER_ACTIVITY_AREAS_ID")
    private Long id;
    @Column("RIDER_ID")
    private Long riderId;
    @Column("RIDER_ACTIVITY_AREA")
    private String riderActivityArea;
}
