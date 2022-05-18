package entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static entities.Users.EXISTENT_USER;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Repo {

    @JsonProperty("private")
    @Builder.Default
    Boolean publicity = false;
    @Builder.Default
    String name = EXISTENT_USER.getName();
    @Builder.Default
    User owner = User.builder().build();
}
