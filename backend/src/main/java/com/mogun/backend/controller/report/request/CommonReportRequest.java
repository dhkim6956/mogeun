package com.mogun.backend.controller.report.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class CommonReportRequest {

    @JsonProperty("email")
    private String email;

    @JsonProperty("routine_key")
    private int routineKey;

    @JsonProperty("plan_key")
    private int planKey;

    @JsonProperty("is_attached")
    private char isAttached;

    @JsonProperty("start_time")
    private LocalDateTime startTime;

    @JsonProperty("end_time")
    private LocalDateTime endTime;

    @JsonProperty("routine_report_key")
    private Long routineReportKey;

    @JsonProperty("set_number")
    private int setNumber;

    @JsonProperty("muscle_avg")
    private float muscleAverage;

    @JsonProperty("weight")
    private int weight;

    @JsonProperty("target_rep")
    private int targetRepeat;

    @JsonProperty("success_rep")
    private int successRepeat;

    @JsonProperty("muscle_fatigue")
    private float muscleFatigue;

}
