package ru.practicum.ewm.category;

import lombok.Data;
import org.hibernate.validator.constraints.Length;
import ru.practicum.ewm.util.common.Marker;

import javax.validation.constraints.NotBlank;

@Data
public class CategoryDto {
    private Long id;
    @NotBlank(groups = {Marker.OnCreate.class, Marker.OnUpdate.class})
    @Length(groups = {Marker.OnCreate.class, Marker.OnUpdate.class}, min = 1, max = 50)
    private String name;
}
