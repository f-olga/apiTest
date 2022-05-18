import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;


public class ResponseUtils {

    @SneakyThrows
    public static <T> T parseResponse(CloseableHttpResponse response, Class<T> clazz) {
        String body = EntityUtils.toString(response.getEntity());
        T object = new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .readValue(body, clazz);
        return object;
    }

}
