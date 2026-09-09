import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Random;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class RandomValuesTasksTest {

    @BeforeEach
    void start() {
        System.out.println("========================\nTest method start\n");
    }

    @AfterEach
    void end() {
        System.out.println("\nTest method end\n========================");
    }

    private static Stream<Arguments> randomNumbersProvider() {
        Random random = new Random();
        return IntStream.range(0, 5)
                .mapToObj(i -> random.nextInt(101)) // 0..100 включительно
                .map(Arguments::of);
    }


    @Test
    public void isEven() {
        Random random = new Random();
        int number = random.nextInt(1, 101); // 1..100
        System.out.println("ЗАДАНИЕ №1: Проверка числа на четность.");
        System.out.println("Случайное число '" + number + "' четное: " + AllTasks.isEven(number));

        // <<< ИЗМЕНЕНО >>> Добавлена проверка результата
        boolean expected = (number % 2 == 0);
        boolean result = AllTasks.isEven(number);
        if (result == expected) {
            System.out.println("TEST PASSED");
        } else {
            System.out.println("TEST FAILED");
        }
    }

    @RepeatedTest(20)
    public void checkAccess() {
        Random random = new Random();
        int number = random.nextInt(0, 100); // 0..99
        System.out.println("ЗАДАНИЕ №2: Доступ в зависимости от возраста.");
        System.out.println("Доступ с возрастом '" + number + "': " + AllTasks.checkAccess(number));

        // <<< ИЗМЕНЕНО >>> Добавлена проверка результата
        String expected = (number >= 18) ? "Allowed" : "Denied";
        String result = AllTasks.checkAccess(number);
        if (expected.equals(result)) {
            System.out.println("TEST PASSED");
        } else {
            System.out.println("TEST FAILED");
        }
    }

    @ParameterizedTest
    @MethodSource("randomNumbersProvider")
    public void getGrade(Integer numb) {
        String grade = AllTasks.getGrade(numb);
        System.out.println("ЗАДАНИЕ №4: Грейд в зависимости от числа.");
        System.out.println("Число '" + numb + "' входит в группу: " + grade);

        // <<< ИЗМЕНЕНО >>> Добавлена проверка результата
        String expected;
        if (numb >= 90) expected = "A";
        else if (numb >= 80) expected = "B";
        else if (numb >= 70) expected = "C";
        else if (numb >= 60) expected = "D";
        else expected = "E";

        if (expected.equals(grade)) {
            System.out.println("TEST PASSED");
        } else {
            System.out.println("TEST FAILED");
        }
    }
}
