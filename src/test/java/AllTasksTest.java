import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
//import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;


import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;
import java.util.stream.IntStream;
import java.util.stream.Stream;

// ***************************************************************************************************************
// Добавить тестовый класс или набор классов, содержащих запуск всех тестовых методов,
// разработанных в ДЗ к теме «Базовая Java», и проверку, что результат работы равен эталонному.
// Использование assert не требуется, достаточно проверить в условном операторе и вывести на экран строку TEST PASSED или TEST FAILED.
// Данные для запуска тестов должны быть сгенерированы случайно или взяты из CSV, прикреплённого к проекту.
// ДОБАВИЛ (НА ВСЯКИЙ СЛУЧАЙ) ВЫВОД ЗАПИСЕЙ ПЕРЕД И ПОСЛЕ КАЖДОГО МЕТОДА, ТАК БЫЛО СКАЗАНО ЛЕКТОРОМ, В ДЗ НЕ НАПИСАНО
// ***************************************************************************************************************

public class AllTasksTest {


    /** РАНДОМАЙЗЕР ЧИСЛОВЫХ ЗНАЧЕНИЙ **/
    private static Stream<Arguments> randomNumbersProvider() {
        Random random = new Random();
        return IntStream.range(0, 5) // Генерируем 10 случайных чисел от 0 до 100
                .mapToObj(_ -> random.nextInt(101)) // чтобы включить 100
                .map(Arguments::of);
    }


    @BeforeEach
    void start() {System.out.println("========================\nTest method start\n");}
    @AfterEach
    void end() {System.out.println("\nTest method end\n========================");}


    @RepeatedTest(3)
    public void isEven(){
        // 1. Проверка четности с рандомным числом и выводом результата теста
        Random random = new Random();
        int number = random.nextInt(1, 1000);
        System.out.println("Случайное число '" + number + "' четное: " + AllTasks.isEven(number));
        boolean result = AllTasks.isEven(number);
        if (result) {
            System.out.println("TEST PASSED");
        } else {
            System.out.println("TEST FAILED");
        }
    }

    //    ======================================================================================

    @RepeatedTest(5)
    public void checkAccess() {
        // 2. Проверка доступа
        Random random = new Random();
        int numb = random.nextInt(0, 99);
        System.out.println("Доступ с возрастом '" + numb + "': " + AllTasks.checkAccess(numb));
        String result = AllTasks.checkAccess(numb);
        if ("Allowed".equals(result)) {
            System.out.println("TEST PASSED");
        } else {
            System.out.println("TEST FAILED");
        }
    }

    //    ======================================================================================

    @Test
    public void isPositive() {
        // 3. Проверка положительности
        Random random = new Random();
        int numb = random.nextInt(-100, 100);
        System.out.println("Число '" + numb + "' положительное: " + AllTasks.isPositive(numb));
        boolean result = AllTasks.isPositive(numb);
        if (result) {
            System.out.println("TEST PASSED");
        } else {
            System.out.println("TEST FAILED");
        }
    }

    //    ======================================================================================

    @Test
    @Tag("Smoke")
    public void getGrade() {
        // 4. Оценка
        Random random = new Random();
        int numb = random.nextInt(0, 100);
        System.out.println("Число '" + numb + "' входит в группу: " + AllTasks.getGrade(numb));
        String result = AllTasks.getGrade(numb);
        if (    "A".equals(result) ||
                "B".equals(result) ||
                "C".equals(result) ||
                "D".equals(result) ||
                "E".equals(result))
        {
            System.out.println("TEST PASSED");
        } else {
            System.out.println("TEST FAILED");
        }
    }

    //    ======================================================================================

    @Test
    public void blastOff() {
        // 5. Обратный отсчет
        Random random = new Random();
        int numb = random.nextInt(0, 20);

        System.out.println("Стартовое число '" + numb + "':\n" + AllTasks.blastOff(numb));

        // Сохраняем в переменную полученный результат
        String result = AllTasks.blastOff(numb);

        // Формируем ожидаемую строку вручную для проверки
        StringBuilder expectedBuilder = new StringBuilder();
        for (int i = numb; i >= 1; i--) {
            expectedBuilder.append(i).append(" ");
        }
        expectedBuilder.append("Поехали!");
        String expected = expectedBuilder.toString();

        // Сравниваем результаты
        if (expected.equals(result)) {
            System.out.println("TEST PASSED");
        } else {
            System.out.println("TEST FAILED");
        }
    }

    //    ======================================================================================

    @ParameterizedTest
    @MethodSource("randomNumbersProvider")
//    @ValueSource( ints = {5, 8, 66, 156})
    public void sumToN(int n) {
        // 6. Сумма до N

        System.out.println("Task 6: Сумма чисел в интервале от 1 до " + n + " = " + AllTasks.sumToN(n));

        int result = AllTasks.sumToN(n);

        int sum = 0;
        for (int i = 0; i <= n; i++) {
            sum = sum + i;
        }

        // Сравниваем результаты
        if (sum == result) {
            System.out.println("TEST PASSED");
        } else {
            System.out.println("TEST FAILED");
        }
    }

    //    ======================================================================================

    @Test
    public void hasBug() {
        // 7. Поиск бага
        String[] logs = {"info", "warning", "bug", "error", "debug", "Bug", "ERROR", "INFO", "Warning",
                "Error", "Critical", "critical", "CRITICAL", "Info"};
        Random random = new Random();

        // Генерируем случайную длину от 1 до длины исходного массива
        // random.nextInt(n) дает числа от 0 до n-1, поэтому +1 для диапазона [1, length]
        int subArrayLength = random.nextInt(logs.length) + 1;

        // Создаем новый массив нужной длины
        String[] resultArray = new String[subArrayLength];

        // Заполняем его случайными элементами из исходного массива
        for (int i = 0; i < subArrayLength; i++) {
            int randomIndex = random.nextInt(logs.length);
            resultArray[i] = logs[randomIndex];
        }
        System.out.println("Сгенерированный массив: " + Arrays.toString(resultArray));
        System.out.println("В массиве есть слово 'bug': " + AllTasks.hasBug(resultArray));

        boolean result = AllTasks.hasBug(resultArray);
        if (result) {
            System.out.println("TEST PASSED");
        } else {
            System.out.println("TEST FAILED");
        }
    }


    //    ======================================================================================

    @Test
    public void EvenInRange() {
        // 8. Четные в диапазоне
        Random random = new Random();
        int a = random.nextInt(0, 20);
        int b = random.nextInt(21, 40);
        System.out.println("Четные числа в диапазоне от '" + a + "' до '" + b +"':\n " + AllTasks.getEvenInRange(a,b));

        StringBuilder result = new StringBuilder();
        for (int i = a; i <= b; i++) {
            if (i % 2 == 0) {
                if (!result.isEmpty()) {
                    result.append(" ");
                }
                result.append(i);
            }
        }
        String res = result.toString();
        String getEvenInRange = AllTasks.getEvenInRange(a,b);

        if (res.equals(getEvenInRange))
        {
            System.out.println("TEST PASSED");
        } else {
            System.out.println("TEST FAILED");
        }
    }


    //    ======================================================================================

    @Test
    public void findMax() {
        // 9. Максимум в массиве
        Random random = new Random();
        // Генерируем случайную длину массива
        int arrayLength = random.nextInt(30) + 1;

        // Создаем массив нужной длины
        int[] resultArray = new int[arrayLength];

        // Заполняем его случайными числами от 0 до 1000
        for (int i = 0; i < arrayLength; i++) {
            int randomValue = random.nextInt(1001); // от 0 до 1000
            resultArray[i] = randomValue;
        }

        System.out.println("Сгенерированный массив: " + java.util.Arrays.toString(resultArray));
        System.out.println("Максимальное число в массиве: " + AllTasks.findMax(resultArray));

        // проверка результата
        int max = resultArray[0];
        for (int i = 1; i < resultArray.length; i++) {
            if (resultArray[i] > max) {
                max = resultArray[i];
            }
        }
        int result = AllTasks.findMax(resultArray);
        if (result == max) {
            System.out.println("TEST PASSED");
        } else {
            System.out.println("TEST FAILED");
        }
    }

    //    ======================================================================================

    @Test
    public void reverse() {Random random = new Random();

        // Генерируем случайную длину массива (например, от 3 до 10 элементов)
        int length = random.nextInt(8) + 3;

        // Создаем массив случайных строк
        String[] words = new String[length];
        for (int i = 0; i < length; i++) {
            words[i] = "Word" + i; // Или можно использовать другие случайные слова
        }

        // Вызываем метод разворота
        String[] reversed = AllTasks.reverse(words);

        System.out.print("Изначальный список слов: [");
        for (int i = 0; i < words.length; i++) {
            System.out.print(words[i]);
            if (i < words.length - 1) System.out.print(", ");
        }
        System.out.println("]");

        // Выводим результаты для наглядности
        System.out.print("Обратный список слов: [");
        for (int i = 0; i < reversed.length; i++) {
            System.out.print(reversed[i]);
            if (i < reversed.length - 1) System.out.print(", ");
        }
        System.out.println("]");


        boolean testPassed = true;

        // 1. Проверяем длину массивов
        if (words.length != reversed.length) {
            testPassed = false;
        } else {
            // 2. Проверяем, что элементы стоят в обратном порядке
            for (int i = 0; i < words.length; i++) {
                // Элемент i в исходном должен быть равен элементу (length - 1 - i) в развернутом
                if (!words[i].equals(reversed[words.length - 1 - i])) {
                    testPassed = false;
                    break;
                }
            }
        }

        // Вывод результата теста
        if (testPassed) {
            System.out.println("TEST PASSED");
        } else {
            System.out.println("TEST FAILED");
        }
    }


    //    ======================================================================================

    @Test
    public void calcAverage() {
        // 11. Среднее арифметическое
        Random random = new Random();

        // 1. Генерируем случайную длину списка (от 1 до 20)
        int listSize = random.nextInt(20) + 1;

        // 2. Создаем список и заполняем его случайными числами (от 0 до 100)
        List<Integer> list = new ArrayList<>(); // заменил его на new ArrayList<>(), чтобы мы могли динамически добавлять туда случайные числа
        double summa = 0;

        // 3. Заполняем его случайными числами (например, от 0 до 100)
        for (int i = 0; i < listSize; i++) {
            int randomNum = random.nextInt(101); // от 0 до 100 включительно
            list.add(randomNum);
            summa = summa + randomNum;
        }

        // Вычисляем ожидаемое среднее вручную для проверки
        double expectedAverage = summa / listSize;

        // Вызываем тестируемый метод
        Double actualAverage = AllTasks.calcAverage(list);

        // 4. Выводим сгенерированный список для наглядности (опционально)
        System.out.println("Сгенерированный список (" + listSize + " элементов):\n" + list);
        System.out.println("Ожидаемое среднее: " + expectedAverage);
        System.out.println("Полученное среднее: " + actualAverage);

        if (expectedAverage == actualAverage)
        {
            System.out.println("ТЕСТ PASSED");
        } else {
            System.out.println("TEST FAILED");
        }

    }

    //    ======================================================================================

    @Test
    public void removeWord() {
        // 12. Удаление слова из списка
        List<String> spisok = List.of("Jhon", "Nikita", "Anika", "Alexandr", "Petr", "Nurlan", "Egor", "Misha");
        Random random = new Random();

        // Добавляем рандомное исключение имени из списка
        int a = random.nextInt(8) + 1;

        String nameToRemove = spisok.get(a);
        System.out.println("Исключенное имя из списка: " + nameToRemove);
        System.out.println("Итоговый список имен: " + AllTasks.removeSpecificName(spisok, nameToRemove));

        List<String> result = new ArrayList<>();

        for (String item : spisok) {
            if (!item.equals(nameToRemove)) {
                result.add(item);
            }
        }

        // Попробовал через ассерт реализацию
        try {
            // Проверяем, что имя НЕ содержится в результате
            Assertions.assertFalse(result.contains(nameToRemove), "Имя должно быть удалено из списка");

            // Если код дошел сюда, значит проверка прошла успешно
            System.out.println("TEST PASSED");

        } catch (AssertionError e) {
            // Если проверка не прошла, ловим ошибку и выводим сообщение о провале
            System.out.println("TEST FAILED: " + e.getMessage());

            // Важно: перевыбрасываем исключение, чтобы JUnit знал, что тест провален
            throw e;
        }
    }
}

