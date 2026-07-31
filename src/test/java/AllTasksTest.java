import org.junit.jupiter.api.Test;
import java.util.List;


public class AllTasksTest {

        @Test
    public void runAllTests() {
//    public static void main(String[] args) {
        System.out.println("========== Run tasks =========");

        // 1. Проверка четности
        System.out.println("Task 1: isEven(4) = " + AllTasks.isEven(4));
        System.out.println("Task 1: isEven(5) = " + AllTasks.isEven(5));

        // 2. Проверка доступа
        System.out.println("Task 2: checkAccess(20) = " + AllTasks.checkAccess(20));
        System.out.println("Task 2: checkAccess(15) = " + AllTasks.checkAccess(15));

        // 3. Проверка положительности
        System.out.println("Task 3: isPositive(10) = " + AllTasks.isPositive(10));
        System.out.println("Task 3: isPositive(-5) = " + AllTasks.isPositive(-5));

        // 4. Оценка
        System.out.println("Task 4: getGrade(85) = " + AllTasks.getGrade(85));
        System.out.println("Task 4: getGrade(15) = " + AllTasks.getGrade(15));
        System.out.println("Task 4: getGrade(105) = " + AllTasks.getGrade(105));

        // 5. Обратный отсчет
        System.out.println("Task 5: blastOff(3) = " + AllTasks.blastOff(3));

        // 6. Сумма до N
        System.out.println("Task 6: sumToN(5) = " + AllTasks.sumToN(5));

        // 7. Поиск бага
        String[] logs = {"info", "warning", "bug", "error"};
        System.out.println("Task 7: hasBug(logs) = " + AllTasks.hasBug(logs));

        // 8. Четные в диапазоне
        System.out.println("Task 8: getEvenInRange(2, 5) = " + AllTasks.getEvenInRange(2, 5));

        // 9. Максимум в массиве
        int[] numbers = {3, 7, 2, 9, 1};
        System.out.println("Task 9: findMax(numbers) = " + AllTasks.findMax(numbers));

        // 10. Разворот массива
        String[] words = {"One", "Two", "Zero"};
        String[] reversed = AllTasks.reverse(words);
        System.out.print("Task 10: reverse(words) = [");
        for (int i = 0; i < reversed.length; i++) {
            System.out.print(reversed[i]);
            if (i < reversed.length - 1) System.out.print(", ");
        }
        System.out.println("]");

        // 11. Среднее арифметическое
        List<Integer> list = List.of(10, 20, 30);
        System.out.println("Task 11: Average = " + AllTasks.calcAverage(list));

        // 12. Удаление слова из списка
        List<String> spisok = List.of("Jhon", "Ram", "Anika", "Alexandr");
        String nameToRemove = spisok.get(2);
        System.out.println("Task 12: removeSpecificName(Anika) = " + AllTasks.removeSpecificName(spisok, nameToRemove));

        System.out.println("============ Done! ===========");

    }
}