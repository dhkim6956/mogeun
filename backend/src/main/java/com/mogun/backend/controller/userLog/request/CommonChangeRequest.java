package com.mogun.backend.controller.userLog.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
public class CommonChangeRequest {

    @JsonProperty("user_email")
    private String email;

    @JsonProperty("weight")
    private float weight;

    @JsonProperty("height")
    private float height;

    @JsonProperty("muscle_mass")
    private float muscleMass;

    @JsonProperty("body_fat")
    private float bodyFat;

    @Builder
    public CommonChangeRequest(String email, float weight, float height, float muscleMass, float bodyFat) {

        this.email = email;
        this.height = height;
        this.weight = weight;
        this.muscleMass = muscleMass;
        this.bodyFat = bodyFat;

    }

}
