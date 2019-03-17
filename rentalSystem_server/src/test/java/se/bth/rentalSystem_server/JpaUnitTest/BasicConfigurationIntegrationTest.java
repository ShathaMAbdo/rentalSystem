package se.bth.rentalSystem_server.JpaUnitTest;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BasicConfigurationIntegrationTest {

    TestRestTemplate restTemplate;
    URL base;
    @LocalServerPort
    int port=8080;

    @Before
    public void setUp() throws MalformedURLException {
        restTemplate = new TestRestTemplate("user", "password");
        base = new URL("http://localhost:" + port);
    }

    @Test
    public void whenLoggedUserRequestsHomePage_ThenSuccess()
            throws IllegalStateException, IOException {
        ResponseEntity<String> response
                = restTemplate.getForEntity(base.toString(), String.class);

        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assert.assertTrue(response
                .getBody()
                .contains("Baeldung"));
    }

    @Test
    public void whenUserWithWrongCredentials_thenUnauthorizedPage()
            throws Exception {

        restTemplate = new TestRestTemplate("user", "wrongpassword");
        ResponseEntity<String> response
                = restTemplate.getForEntity(base.toString(), String.class);

        Assert.assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        Assert.assertTrue(response
                .getBody()
                .contains("Unauthorized"));
    }
}