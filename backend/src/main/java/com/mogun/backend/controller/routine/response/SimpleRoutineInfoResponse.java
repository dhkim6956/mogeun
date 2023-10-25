package com.mogun.backend.controller.routine.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class SimpleRoutineInfoResponse {

    private int key;
    private String name;
}
