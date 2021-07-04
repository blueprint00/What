package com.example.what.Domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@Entity
@Table(name = "word_list")
public class Word_list {
    @Id
    @Column(name = "word_id", nullable = false)
    @GeneratedValue
    private Long word_id;

    @Column(name = "word", nullable = false)
    private String word;

    @Column(name = "mean", nullable = false)
    private String mean;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User_info user_info;

}
