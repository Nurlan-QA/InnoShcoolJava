import org.junit.jupiter.api.*;
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
                .mapToObj(i -> random.nextInt(101)) // чтобы включить 100
                .map(Arguments::of);
    }

    @RepeatedTest(10)
    public void isEven(){

        // 1. Проверка четности.
        // Так как стоит рандомайзер, то 50/50 будут выпадать нечетные числа и тест будет падать,
        // вернее не падать, а выбрасывать исключение через catch с выводом информации

        Random random = new Random();
        // Генерируем заведомо четное число, чтобы тест прошел успешно
        int number = random.nextInt(50) * 2;
        boolean result = AllTasks.isEven(number);

        assertTrue(result, "Число " + number + " должно быть четным");
        System.out.println("TEST PASSED: Число " + number + " четное.");

    }

    @RepeatedTest(10)
    public void checkAccessAllowed() {
        // 2. Проверка доступа с возрастом строго больше 18
        Random random = new Random();
        // Проверяем случайный возраст, который гарантирует доступ (> 18)
        int age = random.nextInt(82) + 19;
        String result = AllTasks.checkAccess(age);
        System.out.println("ЗАДАНИЕ №2: Доступ в зависимости от возраста.");

        assertEquals("Allowed", result,
                "Для возраста " + age + " доступ должен быть разрешен (Allowed)");

        System.out.println("TEST PASSED: Возраст " + age + " -> " + result);

    }

    @RepeatedTest(10)
    @Tag("ShortTest")
    public void testCheckAccessDenied() {
        Random random = new Random();
        // Проверяем случайный возраст, который гарантирует отказ (< 19)
        int age = random.nextInt(19); // Диапазон 0-18

        String result = AllTasks.checkAccess(age);

        assertEquals("Denied", result,
                "Для возраста " + age + " доступ должен быть запрещен (Denied)");

        System.out.println("TEST PASSED: Возраст " + age + " -> " + result);
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

    // ********** ЗАДАНИЕ НА ПАДЕНИЕ **********
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
