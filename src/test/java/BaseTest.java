import lombok.SneakyThrows;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;


public class BaseTest {
    CloseableHttpClient client;
    CloseableHttpResponse response;

    @BeforeTest
    @SneakyThrows
    public void setup() {
        client = HttpClientBuilder.create().build();
    }

    @AfterMethod
    @SneakyThrows
    public void close() {
        response.close();
        //client.close();
    }

    @AfterTest
    @SneakyThrows
    public void closeClient() {

        client.close();
    }

}
