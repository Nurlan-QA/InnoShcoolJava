package ui.SeleniumTest;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.assertj.core.api.Assertions;
import org.openqa.selenium.support.ui.ExpectedConditions;

//1.1. Добавить товар через админку, выйти на витрину и проверить, что товар отображается.


public class GoodAddAdminTests extends BaseTestSelenium {

    @Test
    void GoodsAdd() {
//        ************* ВХОД В АДМИНКУ *************
        loginToAdmin("admin", "secret123");

//        ************* ДОБАВЛЕНИЕ ТОВАРА В АДМИНКЕ *************
//        Ждем отображения админки
        WebElement nameGoodsField = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@id='n-name']"))
        );
//        Вводим название товара.
        WebElement goodsName = driver.findElement(By.xpath("//input[@id='n-name']"));
        goodsName.click();
        goodsName.clear();
        goodsName.sendKeys("Кефир");
//        Вводим цену товара
        WebElement goodsPrice = driver.findElement(By.xpath("//*[@id='n-price']"));
        goodsPrice.click();
        goodsPrice.clear();
        goodsPrice.sendKeys("60");
        // Нажимаем на кнопку Создать
        driver.findElement(By.xpath("//*[@id='add-btn']")).click();

//        ************* ИДЕМ НА ВИТРИНУ И ПРОВЕРЯЕМ НАЛИЧИЕ ТОВАРА *************
//        Нажимаем на кнопку возврата на сайт
        driver.findElement(By.xpath("//*[text()='Вернуться на сайт']")).click();

        Assertions.assertThat(driver.findElement(By.xpath("//*[@data-name='Кефир']")))
                .as("На витрине товаров должен быть Кефир");

//        *********** УДАЛЕНИЕ ТОВАРА *************
        deleteProduct("Кефир");

//        Проверяем, что товар удален
        Assertions.assertThat(driver.findElements(By.xpath("//*[@value='Кефир']")))
                .as("В списке товаров не должен быть Кефир")
                .isEmpty();
    }
}
