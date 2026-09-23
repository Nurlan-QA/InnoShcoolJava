package ui.SelenideTest.pages;

import com.codeborne.selenide.SelenideElement;
import ui.SelenideTest.asserts.PageAssert;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

public class LoginPage {

    public final SelenideElement loginField = $("#username");
    private final SelenideElement passwordField = $("#password");
    private final SelenideElement submitButton = $("button[type='submit']");

    // --- Методы взаимодействия ---
    public void enterLogin(String login) {
        loginField.setValue(login);
    }

    public void enterPassword(String password) {
        passwordField.setValue(password);
    }

    public void clickLoginButton() {
        submitButton.click();
    }

    public void login(String login, String password) {
        enterLogin(login);
        enterPassword(password);
        clickLoginButton();
    }

    /**
     * Проверяет, что на странице отображается сообщение об ошибке авторизации.
     */
    public void assertLoginErrorVisible() {
        // Передаем селектор и текст внутрь PageAssert, чтобы не хранить их в тесте
        SelenideElement errorMessage = $(byText("Неверные учетные данные пользователя"));
        PageAssert.isVisible(errorMessage);
    }

    // --- Проверки ---
    public void assertLoginFieldVisible() {
        PageAssert.isVisible(loginField);
    }

    public void assertPasswordFieldVisible() {
        PageAssert.isVisible(passwordField);
    }

    public void assertSubmitButtonVisible() {
        PageAssert.isVisible(submitButton);
    }

    public void assertPageLoaded() {
        assertLoginFieldVisible();
        assertPasswordFieldVisible();
        assertSubmitButtonVisible();
    }
}

