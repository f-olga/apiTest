import org.apache.http.client.methods.CloseableHttpResponse;

import java.util.Arrays;

public class HeaderUtils {

    public static String getHeader(CloseableHttpResponse response, String headerName) {
        return Arrays.stream(response.getAllHeaders())
                .filter(header -> headerName.equalsIgnoreCase(header.getName()))
                .findFirst().orElseThrow().getValue();
    }
}
