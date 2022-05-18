package entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.List;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CreationError {

    public static final String REPOSITORY_CREATION_FAILED = "Repository creation failed.";
    public static final String NAME_ALREADY_EXISTS = "name already exists on this account";
    public static final String NAME_IS_TOO_SHORT = "name is too short (minimum is 1 character)";
    String message;
    List<Error> errors;


    public static CreationError repoDuplicationError() {
        return CreationError.builder()
                .message(REPOSITORY_CREATION_FAILED)
                .errors(Collections.singletonList(
                        Error.errorWithMessage(NAME_ALREADY_EXISTS)))
                .build();
    }

    public static CreationError repoNameError() {
        return CreationError.builder()
                .message(REPOSITORY_CREATION_FAILED)
                .errors(List.of(Error.missingFieldError("name"), Error.errorWithMessage(NAME_IS_TOO_SHORT)))
                .build();
    }
}
