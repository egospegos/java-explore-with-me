package ru.practicum.ewm.event.comment;

import lombok.Data;
import org.hibernate.validator.constraints.Length;
import ru.practicum.ewm.util.common.Marker;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Data
public class CommentDto {
    private int id;

    @NotBlank(groups = {Marker.OnCreate.class})
    @Length(groups = {Marker.OnCreate.class}, min = 1, max = 7000)
    private String text;
    private String authorName;
    private LocalDateTime created;
}
