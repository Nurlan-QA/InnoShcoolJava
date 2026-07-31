import java.util.ArrayList;
import java.util.List;

public class AllTasks {

    //**************************************
// УРОК №1. ЗАДАНИЕ №1.
//Разработать метод с сигнатурой publiс static boolean isEven(int n). Метод возвращает true, если число чётное, и false — если нечётное.
//**************************************
    public static boolean isEven(int n) {
        return n % 2 == 0;
    }

    //**************************************
// УРОК №1. ЗАДАНИЕ №2.
// Разработать метод с сигнатурой public static String checkAccess(int age).
// Метод возвращает Allowed, если число строго больше 18, и Denied — если меньше.
//**************************************
    public static String checkAccess(int age) {
        if (age > 18) {
            return "Allowed";
        } else {
            return "Denied";
        }
    }

    //**************************************
// УРОК №1. ЗАДАНИЕ №3.
// Разработать метод с сигнатурой public static boolean isPositive(int n).
// Метод должен возвращать true, если переданное число больше или равно нулю, и false, если переданное число меньше нуля.
// Проверка внутри метода должна происходить с помощью тернарного оператора.
//**************************************
    public static boolean isPositive(int n) {
// Исправление 2: Упрощение тернарного оператора
        return n >= 0;
    }

    //**************************************
// УРОК №1. ЗАДАНИЕ №4.
// Разработать метод с сигнатурой public static String getGrade(int score). Метод возвращает строку, соответствующую строгому вхождению в границы:
// 0–20: E;
// 21–40: D;
// 41–60: C;
// 61–80: B;
// 81–100: A.
// Если переданное число не входит в границы — вернуть строку Error.
//**************************************
    public static String getGrade(int score) {
        if (score >= 0 && score < 21) {
            return "E";
        } else if (score >= 21 && score < 41) {
            return "D";
        } else if (score >= 41 && score < 61) {
            return "C";
        } else if (score >= 61 && score < 81) {
            return "B";
        } else if (score >= 81 && score <= 100) {
            return "A";
        } else {
            return "Error";
        }
    }

    //**************************************
// УРОК №1. ЗАДАНИЕ №5.
// Разработать метод с сигнатурой public static String blastOff(int start).
// Метод принимает стартовое число (например, 5) и возвращает строку со всеми
// числами до 1 и словом «Поехали!» в конце (например, «5 4 3 2 1 Поехали!»).
//**************************************
    public static String blastOff(int start) {
        StringBuilder result = new StringBuilder();
        for (int i = start; i >= 1; i--) {
            result.append(i).append(" ");
        }
        result.append("ПОЕХАЛИ!");
        return result.toString();
    }

    //**************************************
// УРОК №1. ЗАДАНИЕ №6.
// Разработать метод с сигнатурой publiс static int sumToN(int n). Метод возвращает сумму всех целых чисел от 1 до n.
//**************************************
    public static int sumToN(int n) {
        int sum = 0;
        for (int i = 0; i <= n; i++) {
            sum += i;
        }
        return sum;
    }

    //**************************************
// УРОК №1. ЗАДАНИЕ №7.
// Разработать метод с сигнатурой publiс static boolean hasBug(String[] messages).
// Метод принимает массив строк и возвращает true, если хотя бы одна строка в массиве равна Bug.
// Сравнение можно выполнять без учёта регистра.
//**************************************
    public static boolean hasBug(String[] messages) {
        for (String message : messages) {
            if (message != null && message.equalsIgnoreCase("bug")) {
                return true;
            }
        }
        return false;
    }
    //**************************************
// УРОК №1. ЗАДАНИЕ №8.
// Разработать метод с сигнатурой publiс static getEvenInRange(int start, int end).
// Метод принимает границы диапазона и возвращает строку, состоящую только из чётных чисел внутри этого промежутка (включая границы),
// разделённых пробелом. Перед первым и после последнего числа пробел не ставится. Например: (2, 5) -> “2 4”
//**************************************
    public static String getEvenInRange(int start, int end) {
        StringBuilder result = new StringBuilder();
        for (int i = start; i <= end; i++) {
            if (i % 2 == 0) {
                if (!result.isEmpty()) {
                    result.append(" ");
                }
                result.append(i);
            }
        }
        return result.toString();
    }

    //**************************************
// УРОК №1. ЗАДАНИЕ №9.
// Разработать метод с сигнатурой publiс static public int findMax(int[] arr).
// Метод находит и возвращает самое большое число в переданном массиве.
//**************************************
    public static int findMax(int[] arr) {
        if (arr == null ||  arr.length == 0) {
            throw new IllegalArgumentException("Массив не может быть пустым");
        }
        int max = arr[0];
        for (int i = 1; i < arr.length; i++) {
            if (arr[i] > max) {
                max = arr[i];
            }
        }
        return max;
    }

    //**************************************
// УРОК №1. ЗАДАНИЕ №10.
// Разработать метод с сигнатурой publiс static String[] reverse(String[] arr).
// Метод возвращает новый массив, в котором элементы исходного массива расположены в обратном порядке.
// Например, {“One”, “Two”, “Zero”} -> {“Zero”, “Two”, “One}.
//**************************************
    public static String[] reverse(String[] arr) {
        String[] reversedArr = new String[arr.length];
        for (int i = 0; i < arr.length; i++) {
            reversedArr[arr.length - 1 - i] = arr[i];
        }
        return reversedArr;
    }

    //**************************************
// УРОК №1. ЗАДАНИЕ №11.
// Разработать метод с сигнатурой publiс static calcAverage(List<Integer> list).
// Метод вычисляет и возвращает среднее арифметическое всех чисел в списке.
//**************************************
    public static Double calcAverage(List<Integer> list) {
        if (list == null || list.isEmpty()) {
            return 0.0;
        }
        double sum = 0;
        for (int num : list) {
            sum += num;
        }
        return sum / list.size();
    }

    //**************************************
// УРОК №1. ЗАДАНИЕ №12.
// Разработать метод с сигнатурой publiс static List<String> removeSpecificName(List<String> list, String nameToRemove).
// Метод принимает список и имя, которое нужно исключить. Возвращает новый список, не содержащий указанного имени.
//**************************************
    public static List<String> removeSpecificName(List<String> list, String nameToRemove) {
        List<String> result = new ArrayList<>();

        for (String item : list) {
            if (!item.equals(nameToRemove)) {
                result.add(item);
            }
        }
        return result;

    }
}