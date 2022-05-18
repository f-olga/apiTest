import entities.NotFound;
import entities.User;
import lombok.SneakyThrows;
import lombok.val;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ContentType;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Ignore;
import org.testng.annotations.Test;

import static entities.Users.EXISTENT_USER;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class GetUsersTest extends BaseTest {

    private static final String usersUrl = "users/";

    @DataProvider
    public static Object[][] testData() {
        return new Object[][]{
                {usersUrl + EXISTENT_USER.getName(), User.builder().build(), User.class, HttpStatus.SC_OK},
                {usersUrl + "non" + EXISTENT_USER.getName(), NotFound.builder().build(), NotFound.class, HttpStatus.SC_NOT_FOUND}
        };
    }

    @Test(dataProvider = "testData")
    @SneakyThrows
    public void getUserTest(String url, Object expected, Class clazz, int responseCode) {

        HttpUriRequest getRequest = RequestUtils.createGetRequest(url);
        response = client.execute(getRequest);
        val actualResponse = ResponseUtils.parseResponse(response, clazz);

        assertThat(response.getStatusLine().getStatusCode()).isEqualTo(responseCode);
        assertThat(HeaderUtils.getHeader(response, "Content-Type")).containsIgnoringCase(ContentType.APPLICATION_JSON.toString());
        assertThat(actualResponse).usingRecursiveComparison()
                .isEqualTo(expected);
    }

    @Test
    @Ignore("Replaced by parametrised tests")
    @SneakyThrows
    public void userFoundTest() {
        val expectedResponse = User.builder().build();

        HttpUriRequest getRequest = RequestUtils.createGetRequest(usersUrl + EXISTENT_USER.getName());
        response = client.execute(getRequest);
        val actualResponse = ResponseUtils.parseResponse(response, User.class);

        assertThat(response.getStatusLine().getStatusCode()).isEqualTo(HttpStatus.SC_OK);
        assertThat(actualResponse).usingRecursiveComparison()
                .isEqualTo(expectedResponse);
    }

    @Test
    @Ignore("Replaced by parametrised tests")
    @SneakyThrows
    public void userNotFoundTest() {
        val expectedResponse = NotFound.builder().build();

        HttpUriRequest getRequest = RequestUtils.createGetRequest(usersUrl + "non" + EXISTENT_USER.getName());
        response = client.execute(getRequest);
        val actualResponse = ResponseUtils.parseResponse(response, NotFound.class);

        assertThat(response.getStatusLine().getStatusCode()).isEqualTo(HttpStatus.SC_NOT_FOUND);
        assertThat(actualResponse).usingRecursiveComparison()
                .isEqualTo(expectedResponse);
    }
}
