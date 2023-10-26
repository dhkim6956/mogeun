package com.mogun.backend.controller.routine.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class CommonRoutineRequest {

    @JsonProperty("user_email")
    public String email;

    @JsonProperty("routine_name")
    public String routineName;

    @JsonProperty("routine_key")
    public int routineKey;

    @JsonProperty("exec_key")
    public int execKey;

    @JsonProperty("total_sets")
    public int sets;
}
