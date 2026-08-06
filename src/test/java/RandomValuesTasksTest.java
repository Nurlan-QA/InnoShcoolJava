import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.Arguments;
import java.util.Random;
import java.util.stream.IntStream;
import java.util.stream.Stream;

// ****************************************************************************************
// ДЗ №2. КЛАСС, КОТОРЫЙ ЗАПУСКАЕТ ТРИ МЕТОДА. ПЕРЕД КАЖДЫМ И ПОСЛЕ КАЖДОГО ВЫВОДИТСЯ
// НУЖНАЯ ЗАПИСЬ О СТАРТЕ И КОНЦЕ ВЫПОЛНЕНИЯ МЕТОДА. ДОБАВЛЕН РАНДОМАЙЗЕР В КАЖДЫЙ МЕТОД.
// ОТДЕЛЬНО ДОБАВЛЕН МЕТОД, КОТОРЫЙ ГЕНЕРИТ МАССИВ СО СЛУЧАЙНЫМИ ЧИСЛАМИ
// boolean isEven(int n) — запустить метод один раз со случайным числом от 1 до 100;
// String checkAccess(int age) — запустить метод 20 раз со случайными числами от 0 до 99;
// String getGrade(int score) — запустить метод в параметризованных тестах с массивом случайных чисел от 0 до 100.
// ****************************************************************************************

public class RandomValuesTasksTest {

    @BeforeEach
    void start() {System.out.println("========================\nTest method start\n");}

    @AfterEach
    void end() {System.out.println("\nTest method end\n========================");}


    // ********** МЕТОД ДЛЯ ГЕНЕРАЦИИ МАССИВА СЛУЧАЙНЫХ ЧИСЕЛ *************
    private static Stream<Arguments> randomNumbersProvider() {
        Random random = new Random();
        return IntStream.range(0, 5) // Генерируем 10 случайных чисел от 0 до 100
                .mapToObj(i -> random.nextInt(100)) // чтобы включить 100
                .map(Arguments::of);
    }

    @RepeatedTest(1)
    public void isEven(){
        // 1. Проверка четности
        Random random = new Random();
        int number = random.nextInt(1, 100);
        System.out.println("ЗАДАНИЕ №1: Проверка числа на четность.");
        System.out.println("Случайное число '" + number + "' четное: " + AllTasks.isEven(number));
    }

    @RepeatedTest(20)
    public void checkAccess() {
        // 2. Проверка доступа
        Random random = new Random();
        int number = random.nextInt(0, 99);
        System.out.println("ЗАДАНИЕ №2: Доступ в зависимости от возраста.");
        System.out.println("Доступ с возрастом '" + number + "':" + AllTasks.checkAccess(number));
    }

    @ParameterizedTest
    @MethodSource("randomNumbersProvider")
    public void getGrade(Integer numb) {
        // 4. Оценка
        String grade = AllTasks.getGrade(numb);
        System.out.println("ЗАДАНИЕ №4: Грейд в зависимости от числа.");
        System.out.println("Число '" + numb + "' входит в группу: " + grade);
    }
}
