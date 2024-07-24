package com.example.wgwg_auth.domain.dto.response;

import com.example.wgwg_auth.domain.entity.RiderActivityArea;
import com.example.wgwg_auth.global.RiderTransportation;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class RiderWithActivityAreasResponse {
//    private Rider rider;
    private Long riderId;
    private String riderNickname;
    private String riderEmail;
    private String riderPhone;
    private Boolean riderActivate;
    private RiderTransportation riderTransportation;
    private String riderAccount;
    private Boolean riderIsDeleted;
    private List<RiderActivityArea> activityAreas;
}
