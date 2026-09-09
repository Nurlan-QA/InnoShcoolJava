import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class AllTasksTest {

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

    // ******* 1. isEven *******
    @RepeatedTest(3)
    public void isEven() {
        Random random = new Random();
        int number = random.nextInt(1, 101); // 1..100
        System.out.println("Случайное число '" + number + "' четное: " + AllTasks.isEven(number));

        // <<< ИЗМЕНЕНО >>> Ожидаемый результат вычисляется из входного числа
        boolean expected = (number % 2 == 0);
        boolean result = AllTasks.isEven(number);

        if (result == expected) {
            System.out.println("TEST PASSED");
        } else {
            System.out.println("TEST FAILED");
        }
    }

    // ******* 2. checkAccess *******
    @RepeatedTest(5)
    public void checkAccess() {
        Random random = new Random();
        int numb = random.nextInt(0, 100); // 0..99
        System.out.println("Доступ с возрастом '" + numb + "': " + AllTasks.checkAccess(numb));

        // <<< ИЗМЕНЕНО >>> Ожидаемый результат вычисляется из входного возраста
        String expected = (numb >= 18) ? "Allowed" : "Denied";
        String result = AllTasks.checkAccess(numb);

        if (expected.equals(result)) {
            System.out.println("TEST PASSED");
        } else {
            System.out.println("TEST FAILED");
        }
    }

    // ******* 3. isPositive *******
    @Test
    public void isPositive() {
        Random random = new Random();
        int numb = random.nextInt(-100, 101); // -100..100
        System.out.println("Число '" + numb + "' положительное: " + AllTasks.isPositive(numb));

        // <<< ИЗМЕНЕНО >>> Ожидаемый результат вычисляется из входного числа
        boolean expected = (numb > 0);
        boolean result = AllTasks.isPositive(numb);

        if (result == expected) {
            System.out.println("TEST PASSED");
        } else {
            System.out.println("TEST FAILED");
        }
    }

    // ******* 4. getGrade *******
    @Test
    @Tag("Smoke")
    public void getGrade() {
        Random random = new Random();
        int numb = random.nextInt(0, 101); // 0..100
        System.out.println("Число '" + numb + "' входит в группу: " + AllTasks.getGrade(numb));

        // <<< ИЗМЕНЕНО >>> Ожидаемый результат вычисляется из входного балла
        String expected;
        if (numb >= 90) expected = "A";
        else if (numb >= 80) expected = "B";
        else if (numb >= 70) expected = "C";
        else if (numb >= 60) expected = "D";
        else expected = "E";

        String result = AllTasks.getGrade(numb);

        if (expected.equals(result)) {
            System.out.println("TEST PASSED");
        } else {
            System.out.println("TEST FAILED");
        }
    }

    // ******* 5. blastOff *******
    @Test
    public void blastOff() {
        Random random = new Random();
        int numb = random.nextInt(0, 21);
        System.out.println("Стартовое число '" + numb + "':\n" + AllTasks.blastOff(numb));

        String result = AllTasks.blastOff(numb);

        StringBuilder expectedBuilder = new StringBuilder();
        for (int i = numb; i >= 1; i--) {
            expectedBuilder.append(i).append(" ");
        }
        expectedBuilder.append("Поехали!");
        String expected = expectedBuilder.toString();

        if (expected.equals(result)) {
            System.out.println("TEST PASSED");
        } else {
            System.out.println("TEST FAILED");
        }
    }

    // ******* 6. sumToN *******
    @ParameterizedTest
    @MethodSource("randomNumbersProvider")
    public void sumToN(int n) {
        System.out.println("Сумма чисел от 1 до " + n + " = " + AllTasks.sumToN(n));

        int result = AllTasks.sumToN(n);

        // <<< ИЗМЕНЕНО >>> Ожидаемый результат вычисляется из входного n
        int expected = 0;
        for (int i = 1; i <= n; i++) {
            expected += i;
        }

        if (expected == result) {
            System.out.println("TEST PASSED");
        } else {
            System.out.println("TEST FAILED");
        }
    }

    // ******* 7. hasBug *******
    @Test
    public void hasBug() {
        String[] logs = {"info", "warning", "bug", "error", "debug", "Bug",
                "ERROR", "INFO", "Warning", "Error", "Critical", "critical", "CRITICAL", "Info"};
        Random random = new Random();

        int subArrayLength = random.nextInt(logs.length) + 1;
        String[] resultArray = new String[subArrayLength];
        for (int i = 0; i < subArrayLength; i++) {
            int randomIndex = random.nextInt(logs.length);
            resultArray[i] = logs[randomIndex];
        }
        System.out.println("Сгенерированный массив: " + Arrays.toString(resultArray));
        System.out.println("В массиве есть слово 'bug': " + AllTasks.hasBug(resultArray));

        boolean result = AllTasks.hasBug(resultArray);

        // <<< ИЗМЕНЕНО >>> Ожидаемый результат вычисляется из сгенерированного массива
        boolean expected = false;
        for (String s : resultArray) {
            if ("bug".equals(s)) {
                expected = true;
                break;
            }
        }

        if (result == expected) {
            System.out.println("TEST PASSED");
        } else {
            System.out.println("TEST FAILED");
        }
    }

    // ******* 8. getEvenInRange *******
    @Test
    public void EvenInRange() {
        Random random = new Random();
        int a = random.nextInt(0, 20);
        int b = random.nextInt(21, 41);
        System.out.println("Четные числа от '" + a + "' до '" + b + "':\n" + AllTasks.getEvenInRange(a, b));

        StringBuilder expectedBuilder = new StringBuilder();
        for (int i = a; i <= b; i++) {
            if (i % 2 == 0) {
                if (!expectedBuilder.isEmpty()) {
                    expectedBuilder.append(" ");
                }
                expectedBuilder.append(i);
            }
        }
        String expected = expectedBuilder.toString();
        String result = AllTasks.getEvenInRange(a, b);

        if (expected.equals(result)) {
            System.out.println("TEST PASSED");
        } else {
            System.out.println("TEST FAILED");
        }
    }

    // ******* 9. findMax *******
    @Test
    public void findMax() {
        Random random = new Random();
        int arrayLength = random.nextInt(30) + 1;
        int[] resultArray = new int[arrayLength];
        for (int i = 0; i < arrayLength; i++) {
            resultArray[i] = random.nextInt(1001);
        }

        System.out.println("Сгенерированный массив: " + Arrays.toString(resultArray));
        System.out.println("Максимальное число: " + AllTasks.findMax(resultArray));

        int expected = resultArray[0];
        for (int i = 1; i < resultArray.length; i++) {
            if (resultArray[i] > expected) {
                expected = resultArray[i];
            }
        }
        int result = AllTasks.findMax(resultArray);

        if (result == expected) {
            System.out.println("TEST PASSED");
        } else {
            System.out.println("TEST FAILED");
        }
    }

    // ******* 10. reverse *******
    @Test
    public void reverse() {
        Random random = new Random();
        int length = random.nextInt(8) + 3;

        String[] words = new String[length];
        for (int i = 0; i < length; i++) {
            words[i] = "Word" + i;
        }

        String[] reversed = AllTasks.reverse(words);
        System.out.println("Изначальный список: " + Arrays.toString(words));
        System.out.println("Обратный список: " + Arrays.toString(reversed));

        boolean testPassed = true;
        if (words.length != reversed.length) {
            testPassed = false;
        } else {
            for (int i = 0; i < words.length; i++) {
                if (!words[i].equals(reversed[words.length - 1 - i])) {
                    testPassed = false;
                    break;
                }
            }
        }

        if (testPassed) {
            System.out.println("TEST PASSED");
        } else {
            System.out.println("TEST FAILED");
        }
    }

    // ******* 11. calcAverage *******
    @Test
    public void calcAverage() {
        Random random = new Random();
        int listSize = random.nextInt(20) + 1;

        // <<< ИЗМЕНЕНО >>> Убран дубликат объявления list
        List<Integer> list = new ArrayList<>();
        double summa = 0;
        for (int i = 0; i < listSize; i++) {
            int randomNum = random.nextInt(101);
            list.add(randomNum);
            summa += randomNum;
        }

        double expectedAverage = summa / listSize;
        Double actualAverage = AllTasks.calcAverage(list);

        System.out.println("Сгенерированный список (" + listSize + " элементов):\n" + list);
        System.out.println("Ожидаемое среднее: " + expectedAverage);
        System.out.println("Полученное среднее: " + actualAverage);

        if (expectedAverage == actualAverage) {
            System.out.println("TEST PASSED");
        } else {
            System.out.println("TEST FAILED");
        }
    }

    // ******* 12. removeWord *******
    @Test
    public void removeWord() {
        // <<< ИЗМЕНЕНО >>> Убран дубликат объявления spisok
        List<String> spisok = List.of("Jhon", "Nikita", "Anika", "Alexandr", "Petr", "Nurlan", "Egor", "Misha");
        Random random = new Random();

        int a = random.nextInt(8);
        String nameToRemove = spisok.get(a);
        System.out.println("Исключенное имя: " + nameToRemove);
        System.out.println("Итоговый список: " + AllTasks.removeSpecificName(spisok, nameToRemove));

        // <<< ИЗМЕНЕНО >>> Ожидаемый результат вычисляется из входных данных
        List<String> expected = new ArrayList<>();
        for (String item : spisok) {
            if (!item.equals(nameToRemove)) {
                expected.add(item);
            }
        }

        List<String> result = AllTasks.removeSpecificName(spisok, nameToRemove);

        if (expected.equals(result)) {
            System.out.println("TEST PASSED");
        } else {
            System.out.println("TEST FAILED");
        }
    }
}
