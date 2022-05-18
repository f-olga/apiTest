import entities.CreateRepoRQ;
import entities.Repo;
import entities.ResponseErrors;
import lombok.SneakyThrows;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ContentType;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static entities.Users.EXISTENT_USER;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class RepoTests extends BaseTest {

    private static final String addRepo = "user/repos";
    private CreateRepoRQ payload;

    @BeforeMethod
    @SneakyThrows
    public void setupRepo() {
        payload = CreateRepoRQ.builder().build();
        deleteRepo(EXISTENT_USER.getName(), payload.getName());
        response = createRepo(payload);
    }

    @Test
    @SneakyThrows
    public void addRepoTest() {
        var expected = Repo.builder()
                .name(payload.getName())
                .build();
        assertThat(response.getStatusLine().getStatusCode()).isEqualTo(HttpStatus.SC_CREATED);
        var actualResponse = ResponseUtils.parseResponse(response, Repo.class);
        assertThat(HeaderUtils.getHeader(response, "Content-Type")).containsIgnoringCase(ContentType.APPLICATION_JSON.toString());
        assertThat(actualResponse).usingRecursiveComparison()
                .isEqualTo(expected);
    }

    @Test
    @SneakyThrows
    public void duplicateRepoTest() {
        var expected = ResponseErrors.repoDuplicationError();
        response.close();

        response = createRepo(payload);
        var actualResponse = ResponseUtils.parseResponse(response, ResponseErrors.class);
        assertThat(response.getStatusLine().getStatusCode()).isEqualTo(HttpStatus.SC_UNPROCESSABLE_ENTITY);
        assertThat(HeaderUtils.getHeader(response, "Content-Type")).containsIgnoringCase(ContentType.APPLICATION_JSON.toString());
        assertThat(actualResponse).usingRecursiveComparison()
                .isEqualTo(expected);
    }

    @Test
    @SneakyThrows
    public void missedNameRepoTest() {
        response.close();
        var expected = ResponseErrors.repoNameError();
        var anotherPayload = CreateRepoRQ.builder().name("").build();
        response = createRepo(anotherPayload);
        var actualResponse = ResponseUtils.parseResponse(response, ResponseErrors.class);
        assertThat(response.getStatusLine().getStatusCode()).isEqualTo(HttpStatus.SC_UNPROCESSABLE_ENTITY);
        assertThat(HeaderUtils.getHeader(response, "Content-Type")).containsIgnoringCase(ContentType.APPLICATION_JSON.toString());
        assertThat(actualResponse).usingRecursiveComparison()
                .isEqualTo(expected);
    }

    @Test
    @SneakyThrows
    public void deleteRepoTest() {
        assertThat(response.getStatusLine().getStatusCode()).isEqualTo(HttpStatus.SC_CREATED);
        response.close();
        response = deleteRepo(EXISTENT_USER.getName(), payload.getName());
        assertThat(response.getStatusLine().getStatusCode()).isEqualTo(HttpStatus.SC_NO_CONTENT);
        assertThat(getRepoList(EXISTENT_USER.getName()).contains(payload.getName())).isFalse();
    }

    @Test
    @SneakyThrows
    public void deleteNonExistentRepoTest() {
        var expected = ResponseErrors.repoNotFoundError();
        var deleteResponse = deleteRepo(EXISTENT_USER.getName(), "NotExist");
        var actualResponse = ResponseUtils.parseResponse(deleteResponse, ResponseErrors.class);
        assertThat(deleteResponse.getStatusLine().getStatusCode()).isEqualTo(HttpStatus.SC_NOT_FOUND);
        deleteResponse.close();
        assertThat(actualResponse).usingRecursiveComparison()
                .isEqualTo(expected);
    }

    @SneakyThrows
    private List<String> getRepoList(String user) {
        HttpUriRequest getRequest = RequestUtils.createGetRequest(String.format("/users/%s/repos", user));
        var repoList = client.execute(getRequest);
        JSONArray jList = new JSONArray(EntityUtils.toString(repoList.getEntity()));
        repoList.close();
        return IntStream.range(0, jList.length()).mapToObj(i -> jList.getJSONObject(i))
                .map(e -> e.get("name").toString())
                .collect(Collectors.toList());
    }

    @SneakyThrows
    private CloseableHttpResponse createRepo(CreateRepoRQ payload) {
        HttpUriRequest postRequest = RequestUtils.createPostRequest(CreateRepoRQ.convertRQToString(payload), addRepo, Credentials.getToken());
        return client.execute(postRequest);
    }

    @SneakyThrows
    private CloseableHttpResponse deleteRepo(String user, String repoName) {
        HttpUriRequest deleteRequest = RequestUtils.createDeleteRequest(String.format("/repos/%s/%s", user, repoName), Credentials.getToken());
        return client.execute(deleteRequest);
    }
}
