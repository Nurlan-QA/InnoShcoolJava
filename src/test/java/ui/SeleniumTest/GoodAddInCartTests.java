package ui.SeleniumTest;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;

// 1.2. Добавить товар в корзину и проверить, что он отображается.
// Сначала добавляем товар в админке, потом добавляем его в корзину, проверяем и удаляем товар

public class GoodAddInCartTests extends BaseTestSelenium {


    @Test
    void GoodsAdd() {

//        ************* ВХОД В АДМИНКУ *************
        loginToAdmin("admin", "secret123");

//        ************* ДОБАВЛЕНИЕ ТОВАРА В АДМИНКЕ *************
        WebElement nameGoodsField = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@id='n-name']"))
        );
//        Вводим название товара.
        WebElement goodsName = driver.findElement(By.xpath("//*[@placeholder='Название']"));
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

        // Ждем, пока элемент с data-name='Кефир' появится в DOM и будет видимым
        WebElement kefirElement = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@data-name='Кефир']"))
        );

        Assertions.assertThat(kefirElement)
                .as("На витрине товаров должен быть 'Кефир'")
                .isNotNull();


//        ************* ДОБАВЛЯЕМ ТОВАР В КОРЗИНУ И ПРОВЕРЯЕМ *************
//        Нажимаем на кнопку 'В корзину' у нашего товара
        driver.findElement(By.xpath("//div[@data-name='Кефир']//button[@data-action='add-to-cart']")).click();
//        Нажимаем на кнопку корзины
        driver.findElement(By.xpath("//*[@id='open-cart-btn']")).click();
//        Проверяем наличие добавленного товара в корзине
        List<WebElement> emptyCartItems = driver.findElements(By.xpath("//div[@id='cart-items']//*"));
//        Проверяем наличие товара в корзине
        Assertions.assertThat(emptyCartItems)
                .as("Корзина не должна быть пустой")
                .isNotEmpty();
//        Проверяем, что конкретно этот товар есть в корзине
        Assertions.assertThat(driver.findElements(By.xpath("//div[@id='cart-items']//*[text()='Кефир']")))
                .as("В списке товаров должен быть Кефир")
                .isNotEmpty();
//        Закрываем модальное окно корзины
        driver.findElement(By.xpath("//span[@id='close-modal']")).click();


//        *********** УДАЛЕНИЕ ТОВАРА *************
        deleteProduct("Кефир");

//        Проверяем, что товар удален
        Assertions.assertThat(driver.findElements(By.xpath("//*[@value='Кефир']")))
                .as("В списке товаров не должен быть Кефир")
                .isEmpty();
    }
}
