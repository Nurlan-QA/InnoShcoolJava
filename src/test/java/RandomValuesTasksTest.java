import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.Arguments;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.opentest4j.AssertionFailedError;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;
import java.util.stream.Stream;

// ****************************************************************************************
// ДЗ №2.
// СОЗДАН КЛАСС, КОТОРЫЙ ЗАПУСКАЕТ ТРИ МЕТОДА. ПЕРЕД КАЖДЫМ И ПОСЛЕ КАЖДОГО ВЫВОДИТСЯ
// НУЖНАЯ ЗАПИСЬ О СТАРТЕ И КОНЦЕ ВЫПОЛНЕНИЯ МЕТОДА. ДОБАВЛЕН РАНДОМАЙЗЕР В КАЖДЫЙ МЕТОД.
// ОТДЕЛЬНО ДОБАВЛЕН МЕТОД, КОТОРЫЙ ГЕНЕРИТ МАССИВ СО СЛУЧАЙНЫМИ ЧИСЛАМИ
// boolean isEven(int n) — запустить метод один раз со случайным числом от 1 до 100;
// String checkAccess(int age) — запустить метод 20 раз со случайными числами от 0 до 99;
// String getGrade(int score) — запустить метод в параметризованных тестах с массивом случайных чисел от 0 до 100.

// ****************************************************************************************
// ДЗ N3.
// ДОБАВЛЕН 4-ЫЙ МЕТОД, КОТОРЫЙ СПЕЦИАЛЬНО ПРОВАЛЬНЫЙ, ЧТОБЫ УВИДЕТЬ ЧЕРЕЗ АССЕРТЫ ПОДРОБНОСТИ
// В КАЖДЫЙ МЕТОД ДОБАВЛЕНЫ АССЕРТЫ. МЕТОДЫ ВОЗВРАЩАЮТ БУЛЕВО, СТРОКИ, СПИСОК

//****************************************************************************************

public class RandomValuesTasksTest {

    @BeforeEach
    void start() {System.out.println("========================\nTest method start\n");}

    @AfterEach
    void end() {System.out.println("\nTest method end\n========================");}


    // ********** МЕТОД ДЛЯ ГЕНЕРАЦИИ МАССИВА СЛУЧАЙНЫХ ЧИСЕЛ *************
    private static Stream<Arguments> randomNumbersProvider() {
        Random random = new Random();
        return IntStream.range(0, 5) // Генерируем 10 случайных чисел от 0 до 100
                .mapToObj(_ -> random.nextInt(100)) // чтобы включить 100
                .map(Arguments::of);
    }

    @RepeatedTest(1)
    public void isEven(){

        // 1. Проверка четности.
        // Так как стоит рандомайзер, то 50/50 будут выпадать нечетные числа и тест будет падать,
        // вернее не падать, а выбрасывать исключение через catch с выводом информации

        Random random = new Random();
        int number = random.nextInt(1, 100);
        System.out.println("ЗАДАНИЕ №1: Проверка числа на четность.");
        System.out.println("Случайное число '" + number + "' четное: " + AllTasks.isEven(number));

        try {
            assertTrue(AllTasks.isEven(number), "TEST FAILED");
            System.out.println("TEST PASSED");

        } catch (AssertionFailedError e) {
            System.out.println("TEST FAILED");
            System.out.println("Число не четное! " + e.getMessage());
        }
    }

    @RepeatedTest(10)
    public void checkAccess() {
        // 2. Проверка доступа. Аналогично первому методу. Denied - проброс исключения
        Random random = new Random();
        int number = random.nextInt(0, 99);
        System.out.println("ЗАДАНИЕ №2: Доступ в зависимости от возраста.");
        System.out.println("Доступ с возрастом '" + number + "': " + AllTasks.checkAccess(number));

        try {
            assertEquals("Allowed", AllTasks.checkAccess(number));
            System.out.println("TEST PASSED");
        } catch (AssertionFailedError e) {
            System.out.println("TEST FAILED");
            System.out.println("Доступ запрещен: " + e.getMessage());
        }
    }

    @ParameterizedTest
    @MethodSource("randomNumbersProvider")
    public void getGrade(Integer numb) {
        // 4. Оценка

        System.out.println("ЗАДАНИЕ №4: Грейд в зависимости от числа до от 0 до 100");
        String result = AllTasks.getGrade(numb);
        System.out.println("Число '" + numb + "' входит в группу: " + result);

        assertThat(result)
                .as("Оценка '%s' должна быть одной из: A, B, C, D, E", result)
                .isIn("A", "B", "C", "D", "E");

        System.out.println("TEST PASSED");
    }

    @Test
    public void getReduce() {
        // Отсечение списка (вызываемый метод отрабатывает неверно умышленно, чтобы тест падал)

        List<Integer> startValues = List.of(123, 213, 2133, 12, 234, 312, 32, 43, 454, 21, 223);
        Random random = new Random();
        int remove = random.nextInt(1, 11);

        System.out.println("Количество элементов для сохранения: " + remove);
        List<Integer> result = AllTasks.reduce(startValues, remove);

        assertEquals(remove, result.size(), "Размер списка не соответствует ожидаемому значению 'remove'.");

        //        Проверяем, что элементы в начале списка совпадают с исходными
        List<Integer> expected = new ArrayList<>();
        for (int i = 0; i < remove; i++) {
            expected.add(startValues.get(i));
        }

        assertEquals(expected, result, "Содержимое списка не совпадает с ожидаемым.");
    }

}
