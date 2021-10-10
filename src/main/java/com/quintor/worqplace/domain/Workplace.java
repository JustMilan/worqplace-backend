package com.quintor.worqplace.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "workplace")
public class Workplace {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int number;

    @OneToMany
    @JoinColumn(name = "workplace_id")
    private List<Timeslot> timeslots;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    private Room room;
}
