package com.example.wgwg_auth.domain.dto.response;

import com.example.wgwg_auth.domain.entity.Rider;
import com.example.wgwg_auth.domain.entity.RiderActivityArea;
import com.example.wgwg_auth.global.RiderTransportation;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class RiderWithActivityAreas {
    private Rider rider;
    private List<RiderActivityArea> activityAreas;
}
