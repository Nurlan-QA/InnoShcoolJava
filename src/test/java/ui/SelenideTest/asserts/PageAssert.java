package ui.SelenideTest.asserts;

import com.codeborne.selenide.SelenideElement;
import static com.codeborne.selenide.Condition.*;

public class PageAssert {

    public static void isVisible(SelenideElement element) {
        element.shouldBe(visible);
    }

    public static void hasText(SelenideElement element, String expectedText) {
        element.shouldHave(exactText(expectedText));
    }

    public static void containsText(SelenideElement element, String expectedSubstring) {
        element.shouldHave(text(expectedSubstring));
    }

    public static void notExists(SelenideElement element) {
        element.shouldNot(exist);
    }
}
