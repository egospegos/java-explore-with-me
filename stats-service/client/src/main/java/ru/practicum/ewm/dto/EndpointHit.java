package ru.practicum.ewm.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "hits")
@Getter
@Setter
@ToString
@EqualsAndHashCode(of = "id")
public class EndpointHit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String app;

    @Column
    private String uri;

    @Column(nullable = false)
    private String ip;

    @Column(name = "created")
    private LocalDateTime timestamp;

}
