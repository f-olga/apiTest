import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;

import static org.apache.http.HttpHeaders.*;

public class RequestUtils {
    public static final String baseUrl = "https://api.github.com/";

    @SneakyThrows
    public static HttpUriRequest createPostRequest(String payload, String endpoint, String authorizationToken) {
        var request = new HttpPost(baseUrl + endpoint);
        request.setEntity(new StringEntity(payload, ContentType.APPLICATION_JSON));
        request.setHeader(CONTENT_TYPE, "application/json");
        request.setHeader(ACCEPT, "application/vnd.github.v3+json");
        request.setHeader(AUTHORIZATION, "token " + authorizationToken);
        return request;
    }

    public static HttpUriRequest createPostRequest(String payload, String endpoint) {
        return createPostRequest(payload, endpoint, StringUtils.EMPTY);
    }

    @SneakyThrows
    public static HttpUriRequest createGetRequest(String endpoint) {

        return new HttpGet(baseUrl + endpoint);
    }

}
