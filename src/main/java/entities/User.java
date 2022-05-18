package entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Builder.Default
    String login = "fiomay";
    @Builder.Default
    long id = 105494622;
    @Builder.Default
    String type = "User";
    @Builder.Default
    String email = null;
}
