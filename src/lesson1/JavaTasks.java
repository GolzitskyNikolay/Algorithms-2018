package lesson1;

import kotlin.NotImplementedError;

import java.io.*;
import java.util.*;

@SuppressWarnings("unused")
public class JavaTasks {
    /**
     * Сортировка времён
     * <p>
     * Простая
     * (Модифицированная задача с сайта acmp.ru)
     * <p>
     * Во входном файле с именем inputName содержатся моменты времени в формате ЧЧ:ММ:СС,
     * каждый на отдельной строке. Пример:
     * <p>
     * 13:15:19
     * 07:26:57
     * 10:00:03
     * 19:56:14
     * 13:15:19
     * 00:40:31
     * <p>
     * Отсортировать моменты времени по возрастанию и вывести их в выходной файл с именем outputName,
     * сохраняя формат ЧЧ:ММ:СС. Одинаковые моменты времени выводить друг за другом. Пример:
     * <p>
     * 00:40:31
     * 07:26:57
     * 10:00:03
     * 13:15:19
     * 13:15:19
     * 19:56:14
     * <p>
     * В случае обнаружения неверного формата файла бросить любое исключение.
     */
    static public void sortTimes(String inputName, String outputName) {
        List<Integer> resultList = new ArrayList<>();
        File input = new File(inputName);
        StringBuilder stringBuilder = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(input))) {
            String line = reader.readLine();
            while (line != null) {
                if (!line.matches("(([0-1][0-9])|([2][0-3]))(:[0-5][0-9]){2}"))
                    throw new IllegalArgumentException();
                Arrays.stream(line.split(":")).forEach(stringBuilder::append);
                resultList.add(Integer.parseInt(String.valueOf(stringBuilder)));
                line = reader.readLine();
                stringBuilder.setLength(0);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        Collections.sort(resultList);
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(outputName))) {
            for (int j = 0; j <= resultList.size() - 1; j++) {
                stringBuilder.append(resultList.get(j));
                while (stringBuilder.length() != 6) {
                    stringBuilder.insert(0, "0");
                }
                stringBuilder.insert(2, ":");
                stringBuilder.insert(5, ":");
                bufferedWriter.write(String.valueOf(stringBuilder) + "\n");
                stringBuilder.setLength(0);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Сортировка адресов
     * <p>
     * Средняя
     * <p>
     * Во входном файле с именем inputName содержатся фамилии и имена жителей города с указанием улицы и номера дома,
     * где они прописаны. Пример:
     * <p>
     * Петров Иван - Железнодорожная 3
     * Сидоров Петр - Садовая 5
     * Иванов Алексей - Железнодорожная 7
     * Сидорова Мария - Садовая 5
     * Иванов Михаил - Железнодорожная 7
     * <p>
     * Людей в городе может быть до миллиона.
     * <p>
     * Вывести записи в выходной файл outputName,
     * упорядоченными по названию улицы (по алфавиту) и номеру дома (по возрастанию).
     * Людей, живущих в одном доме, выводить через запятую по алфавиту (вначале по фамилии, потом по имени). Пример:
     * <p>
     * Железнодорожная 3 - Петров Иван
     * Железнодорожная 7 - Иванов Алексей, Иванов Михаил
     * Садовая 5 - Сидоров Петр, Сидорова Мария
     * <p>
     * В случае обнаружения неверного формата файла бросить любое исключение.
     */
    static public void sortAddresses(String inputName, String outputName) {
        File input = new File(inputName);
        StringBuilder stringBuilder = new StringBuilder();
        List<String> resultList = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(input))) {
            String line = reader.readLine();
            while (line != null) {
                if (!line.matches("([А-Я][а-я]* ){2}- [А-Я][а-я]* [1-9]+"))
                    throw new IllegalArgumentException();
                String[] strings = line.split(" ");
                stringBuilder.append(strings[3]).append(" ").append(strings[4]).append(" - ");
                stringBuilder.append(strings[0]).append(" ").append(strings[1]);
                resultList.add(String.valueOf(stringBuilder));
                line = reader.readLine();
                stringBuilder.setLength(0);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        Collections.sort(resultList);
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(outputName))) {
            if (resultList.size() != 0){
                bufferedWriter.write(resultList.get(0));
            }
            for (int i = 1; i < resultList.size(); i++) {
                stringBuilder.append("\n").append(resultList.get(i));
                String[] elementOfResultList = resultList.get(i).split(" - ");
                if (resultList.get(i - 1).split(" - ")[0].equals(elementOfResultList[0])) {
                    stringBuilder.setLength(0);
                    stringBuilder.append(", ").append(elementOfResultList[1]);
                }
                bufferedWriter.write(String.valueOf(stringBuilder));
                stringBuilder.setLength(0);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Сортировка температур
     * <p>
     * Средняя
     * (Модифицированная задача с сайта acmp.ru)
     * <p>
     * Во входном файле заданы температуры различных участков абстрактной планеты с точностью до десятых градуса.
     * Температуры могут изменяться в диапазоне от -273.0 до +500.0.
     * Например:
     * <p>
     * 24.7
     * -12.6
     * 121.3
     * -98.4
     * 99.5
     * -12.6
     * 11.0
     * <p>
     * Количество строк в файле может достигать ста миллионов.
     * Вывести строки в выходной файл, отсортировав их по возрастанию температуры.
     * Повторяющиеся строки сохранить. Например:
     * <p>
     * -98.4
     * -12.6
     * -12.6
     * 11.0
     * 24.7
     * 99.5
     * 121.3
     */
    static public void sortTemperatures(String inputName, String outputName) {
        throw new NotImplementedError();
    }

    /**
     * Сортировка последовательности
     * <p>
     * Средняя
     * (Задача взята с сайта acmp.ru)
     * <p>
     * В файле задана последовательность из n целых положительных чисел, каждое в своей строке, например:
     * <p>
     * 1
     * 2
     * 3
     * 2
     * 3
     * 1
     * 2
     * <p>
     * Необходимо найти число, которое встречается в этой последовательности наибольшее количество раз,
     * а если таких чисел несколько, то найти минимальное из них,
     * и после этого переместить все такие числа в конец заданной последовательности.
     * Порядок расположения остальных чисел должен остаться без изменения.
     * <p>
     * 1
     * 3
     * 3
     * 1
     * 2
     * 2
     * 2
     */
    static public void sortSequence(String inputName, String outputName) {
        throw new NotImplementedError();
    }

    /**
     * Соединить два отсортированных массива в один
     * <p>
     * Простая
     * <p>
     * Задан отсортированный массив first и второй массив second,
     * первые first.size ячеек которого содержат null, а остальные ячейки также отсортированы.
     * Соединить оба массива в массиве second так, чтобы он оказался отсортирован. Пример:
     * <p>
     * first = [4 9 15 20 28]
     * second = [null null null null null 1 3 9 13 18 23]
     * <p>
     * Результат: second = [1 3 4 9 9 13 15 20 23 28]
     */
    static <T extends Comparable<T>> void mergeArrays(T[] first, T[] second) {
        throw new NotImplementedError();
    }
}
