package entities;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Users {
    EXISTENT_USER("fiomay");
    String name;
}
