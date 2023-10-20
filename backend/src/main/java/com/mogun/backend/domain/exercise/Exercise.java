package com.mogun.backend.domain.exercise;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Exercise {

    @Id
    @GeneratedValue
    @Column(name = "exec_key", columnDefinition = "TINYINT")
    private int execKey;

    @Column
    private String name;

    @Column(name = "exec_desc")
    private String execDesc;

    @Column(name = "main_part", columnDefinition = "TINYINT")
    private int mainPart;

    @Column(name = "image_path")
    private String imagePath;

}
