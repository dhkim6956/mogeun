package com.mogun.backend.service.user.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
public class IsJoinedResponse {

    @JsonProperty(value = "is_joined")
    private boolean isJoined;

    @Builder
    public IsJoinedResponse(boolean isJoined) { this.isJoined = isJoined; }
}
