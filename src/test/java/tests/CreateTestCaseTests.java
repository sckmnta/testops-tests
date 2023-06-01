package tests;

import com.codeborne.selenide.Configuration;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selectors.byName;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static io.qameta.allure.Allure.step;

public class CreateTestCaseTests {

    static String login = "allure8";
    static String password = "allure8";

    @BeforeAll
    static void setUp() {
        Configuration.baseUrl = "https://allure.autotests.cloud";
        Configuration.holdBrowserOpen = true;
        Configuration.browser = "firefox";
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
}
