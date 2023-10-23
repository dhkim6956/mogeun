package com.mogun.backend.domain.report.muscleActInSet;

import com.mogun.backend.domain.musclePart.MusclePart;
import com.mogun.backend.domain.report.routineReport.RoutineReport;
import com.mogun.backend.domain.report.setReport.SetReport;
import com.mogun.backend.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MuscleActInSetLog implements Serializable {

    @Id
    @Column(name = "set_log_key")
    private Long setLogKey;

    @Id
    @Column(name = "routine_report_key")
    private Long routineReportKey;

    @Id
    @Column(name = "user_key")
    private int userKey;

    @ManyToOne
    @JoinColumn(name = "part_key")
    private MusclePart musclePart;

    @Column(name = "sensor_number", columnDefinition = "TINYINT")
    private int sensorNumber;

    @Column(name = "muscle_activity")
    private float muscleActivity;

    @MapsId
    @ManyToOne
    @JoinColumn(name = "set_log_key")
    private SetReport setReport;

    @MapsId
    @ManyToOne
    @JoinColumn(name = "routine_report_key")
    private RoutineReport routineReport;

    @MapsId
    @ManyToOne
    @JoinColumn(name = "user_key")
    private User user;

}
