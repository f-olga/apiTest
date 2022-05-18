import entities.CreateRepoRQ;
import entities.Repo;
import lombok.SneakyThrows;
import lombok.val;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ContentType;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class RepoTests extends BaseTest {

    private static final String addRepo = "user/repos";

    @Test
    @SneakyThrows
    public void addRepo() {
        val payload = CreateRepoRQ.builder().build();
        val expected = Repo.builder()
                .name(payload.getName())
                .build();
        val list = getRepoList(expected.getOwner().getLogin());
        createRepo(payload);
        val actualResponse = ResponseUtils.parseResponse(response, Repo.class);
        assertThat(HeaderUtils.getHeader(response, "Content-Type")).containsIgnoringCase(ContentType.APPLICATION_JSON.toString());
        assertThat(actualResponse).usingRecursiveComparison()
                .isEqualTo(expected);
    }

    @SneakyThrows
    private List<String> getRepoList(String user) {
        HttpUriRequest getRequest = RequestUtils.createGetRequest(String.format("/users/%s/repos", user));
        val response = client.execute(getRequest);
        JSONArray jList = new JSONArray(EntityUtils.toString(response.getEntity()));
        return IntStream.range(0, jList.length()).mapToObj(i -> jList.getJSONObject(i))
                .map(e -> e.get("name").toString())
                .collect(Collectors.toList());
    }

    private void createRepo(CreateRepoRQ payload) throws IOException {
        HttpUriRequest postRequest = RequestUtils.createPostRequest(CreateRepoRQ.convertRQToString(payload), addRepo, Configuration.getToken());
        response = client.execute(postRequest);
        assertThat(response.getStatusLine().getStatusCode()).isEqualTo(201);
    }
}
