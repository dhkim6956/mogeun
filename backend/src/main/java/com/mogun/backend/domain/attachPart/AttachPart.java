package com.mogun.backend.domain.attachPart;

import com.mogun.backend.domain.exercise.Exercise;
import com.mogun.backend.domain.musclePart.MusclePart;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AttachPart implements Serializable {

    @Id
    @Column(name = "part_key")
    private int partKey;

    @Id
    @Column(name = "exec_key", columnDefinition = "TINYINT")
    private int execKey;

    @Column(name = "attach_direction")
    private char attachDirection;

    @Column(name = "muscle_category")
    private char muscleCategory;

    @MapsId
    @ManyToOne
    @JoinColumn(name = "exec_key")
    private Exercise exercise;

    @MapsId
    @ManyToOne
    @JoinColumn(name = "part_key")
    private MusclePart musclePart;

}
