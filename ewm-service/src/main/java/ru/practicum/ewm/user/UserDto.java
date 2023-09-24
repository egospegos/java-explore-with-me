package ru.practicum.ewm.user;

import lombok.Data;
import org.hibernate.validator.constraints.Length;
import ru.practicum.ewm.util.common.Marker;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
public class UserDto {
    private Long id;
    @NotBlank(groups = {Marker.OnCreate.class})
    @Email(groups = {Marker.OnCreate.class, Marker.OnUpdate.class})
    @Length(groups = {Marker.OnCreate.class}, min = 6, max = 254)
    private String email;
    @NotBlank(groups = {Marker.OnCreate.class})
    @Length(groups = {Marker.OnCreate.class}, min = 2, max = 250)
    private String name;
}
