package entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Error {

    String resource;
    String code;
    String field;
    String message;

    private static ErrorBuilder basicCustomRepoError() {
        return Error.builder()
                .resource("Repository")
                .code("custom")
                .field("name");
    }

    public static Error errorWithMessage(String message) {
        return basicCustomRepoError()
                .message(message)
                .build();
    }

    public static Error missingFieldError(String fieldName) {
        return Error.builder()
                .resource("Repository")
                .code("missing_field")
                .field(fieldName)
                .build();
    }
}
