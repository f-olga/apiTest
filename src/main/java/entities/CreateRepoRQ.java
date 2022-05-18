package entities;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.*;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CreateRepoRQ {
    @Builder.Default
    String name = "TestRepo";

    @SneakyThrows
    public static String convertRQToString(CreateRepoRQ request) {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(request);
    }
}
