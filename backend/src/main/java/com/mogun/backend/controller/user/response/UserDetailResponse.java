package com.mogun.backend.controller.user.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
public class UserDetailResponse {

    private float height;
    private float weight;

    @JsonProperty(value = "muscle_mass")
    private float muscleMass;

    @JsonProperty(value = "body_fat")
    private float bodyFat;

    @Builder
    public UserDetailResponse(float height, float weight, float muscleMass, float bodyFat) {
        this.height = height;
        this.weight = weight;
        this.muscleMass = muscleMass;
        this.bodyFat = bodyFat;
    }

}
