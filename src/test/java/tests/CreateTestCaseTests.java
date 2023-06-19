package tests;

import com.codeborne.selenide.Configuration;
import com.github.javafaker.Faker;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import models.CreateTestCaseBody;
import models.CreateTestCaseResponse;
import org.hamcrest.Matcher;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Cookie;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selectors.byName;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.core.Is.is;

public class CreateTestCaseTests {

    static String login = "allure8";
    static String password = "allure8";
    static int projectId = 2243;

    @BeforeAll
    static void setUp() {
        Configuration.baseUrl = "https://allure.autotests.cloud";
        Configuration.holdBrowserOpen = true;
        Configuration.browser = "firefox";
        RestAssured.baseURI = "https://allure.autotests.cloud";
        //Configuration.pageLoadStrategy = "eager";
    }


    @Test
    void createTestWithUI() {

        Faker faker = new Faker();
        String testCaseName = faker.elderScrolls().quote();

        step("authorization", () -> {
            open("/");

            $(byName("username")).setValue(login);
            $(byName("password")).setValue(password);
            $("button[type='submit']").click();
        });
        step("move to project", () -> {
            open("/project/2243/test-cases");


        });
        step("create testcase", () -> {
            $("[data-testid=input__create_test_case]").setValue(testCaseName).pressEnter();

        });
        step("verify testcase name", () -> {
            $(".LoadableTree__view").shouldHave(text(testCaseName));

        });
    }
    @Test
    void createTestWithApi() {

        Faker faker = new Faker();
        String testCaseName = faker.elderScrolls().quote();

        step("authorization", () -> {
            //open("/");

            //$(byName("username")).setValue(login);
           // $(byName("password")).setValue(password);
            //$("button[type='submit']").click();
        });
        step("move to project", () -> {
            //open("/project/2243/test-cases");


        });
        step("create testcase", () -> {
            CreateTestCaseBody testCaseBody = new CreateTestCaseBody();
            testCaseBody.setName(testCaseName);
            given()
                    .log().all()
                    .header("X-XSRF-TOKEN", "0b219942-03ad-4edc-8acf-a3acf5fe9712")
                    .cookies("XSRF-TOKEN", "0b219942-03ad-4edc-8acf-a3acf5fe9712",
                            "ALLURE_TESTOPS_SESSION", "fdbb0d9f-0a77-4b6a-93c9-46a5b9bd3b77")
                    .contentType("application/json;charset=UTF-8")
                    .body(testCaseBody)
                    .queryParam("projectId", projectId)
                    .when()
                    .post("/api/rs/testcasetree/leaf")
                    .then()
                    .log().status()
                    .log().body()
                    .statusCode(200)
                    .body("statusName", is("Draft"))
                    .body("name", is(testCaseName));

        });
        step("verify testcase name", () -> {
            //$(".LoadableTree__view").shouldHave(text(testCaseName));

        });
    }   @Test
    void createTestWithApiAndUI() {

        Faker faker = new Faker();
        String testCaseName = faker.elderScrolls().quote();


        step("authorization", () -> {
        });

        CreateTestCaseBody testCaseBody = new CreateTestCaseBody();
        testCaseBody.setName(testCaseName);


        CreateTestCaseResponse createTestCaseResponse =
        step("create testcase", () ->
            given()
                    .log().all()
                    .header("X-XSRF-TOKEN", "c869d9f8-0d76-4527-840e-670790922e73")
                    .cookies("XSRF-TOKEN", "c869d9f8-0d76-4527-840e-670790922e73",
                            "ALLURE_TESTOPS_SESSION", "5dc1345e-c8b8-44cc-927d-670ce56bcb07")
                    .contentType("application/json;charset=UTF-8")
                    .body(testCaseBody)
                    .queryParam("projectId", projectId)
                    .when()
                    .post("/api/rs/testcasetree/leaf")
                    .then()
                    .log().status()
                    .log().body()
                    .statusCode(200)
                    .extract().as(CreateTestCaseResponse.class)
        );
        step("verify testcase name", () -> {
            //assertThat(createTestCaseResponse.getName()).isEqualTo(testCaseName);
            open("/favicon.ico");
            Cookie authorizationCookie = new Cookie("ALLURE_TESTOPS_SESSION", "5dc1345e-c8b8-44cc-927d-670ce56bcb07");
            getWebDriver().manage().addCookie(authorizationCookie);
            Integer testCaseId = createTestCaseResponse.getId();
            String testCaseURL = format("/project/%s/test-cases/%s", projectId, testCaseId);
            open(testCaseURL);
            $(".TestCaseLayout__name").shouldHave(text(testCaseName));

        });
    }
}

