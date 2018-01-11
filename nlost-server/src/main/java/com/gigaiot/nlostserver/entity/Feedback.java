package com.gigaiot.nlostserver.entity;

import lombok.Data;

import javax.persistence.*;

/**
 * Created by cxm on 2017/10/18.
 */
@Data
@Entity(name = "t_feedback")
public class Feedback {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String email;

    @Lob
    @Column(name = "content")
    private String content;


    @ManyToOne
    private User user;
}
