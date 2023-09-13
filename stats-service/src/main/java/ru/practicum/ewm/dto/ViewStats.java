package ru.practicum.ewm.dto;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;


@Data
public class ViewStats {

    private String app;

    private String uri;

    private Integer hits;


}
