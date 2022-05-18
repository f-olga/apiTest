import entities.CreateRepoRQ;
import entities.CreationError;
import entities.Repo;
import lombok.SneakyThrows;
import lombok.val;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ContentType;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.testng.annotations.AfterMethod;
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
        createRepo(payload);
        assertThat(response.getStatusLine().getStatusCode()).isEqualTo(HttpStatus.SC_CREATED);
    }

    @AfterMethod
    @SneakyThrows
    public void deleteRepos() {
        val list = getRepoList(EXISTENT_USER.getName());
        list.forEach(e -> deleteRepo(EXISTENT_USER.getName(), e));
    }

    @Test
    @SneakyThrows
    public void addRepoTest() {
        val expected = Repo.builder()
                .name(payload.getName())
                .build();
   
        val actualResponse = ResponseUtils.parseResponse(response, Repo.class);
        assertThat(HeaderUtils.getHeader(response, "Content-Type")).containsIgnoringCase(ContentType.APPLICATION_JSON.toString());
        assertThat(actualResponse).usingRecursiveComparison()
                .isEqualTo(expected);
    }

    @Test
    @SneakyThrows
    public void duplicateRepoTest() {
        val expected = CreationError.repoDuplicationError();

        createRepo(payload);
        val actualResponse = ResponseUtils.parseResponse(response, CreationError.class);
        assertThat(HeaderUtils.getHeader(response, "Content-Type")).containsIgnoringCase(ContentType.APPLICATION_JSON.toString());
        assertThat(actualResponse).usingRecursiveComparison()
                .isEqualTo(expected);
    }

    @Test
    @SneakyThrows
    public void missedNameRepoTest() {
        val expected = CreationError.repoNameError();
        val anotherPayload = CreateRepoRQ.builder().name("").build();

        createRepo(anotherPayload);
        val actualResponse = ResponseUtils.parseResponse(response, CreationError.class);
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

    @SneakyThrows
    private void createRepo(CreateRepoRQ payload) {
        HttpUriRequest postRequest = RequestUtils.createPostRequest(CreateRepoRQ.convertRQToString(payload), addRepo, Credentials.getToken());
        response = client.execute(postRequest);
    }

    @SneakyThrows
    private void deleteRepo(String user, String repoName) {
        HttpUriRequest deleteRequest = RequestUtils.createDeleteRequest(String.format("/repos/%s/%s", user, repoName), Credentials.getToken());
        response = client.execute(deleteRequest);
    }
}
