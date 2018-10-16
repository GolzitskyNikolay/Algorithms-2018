package lesson1;

import kotlin.NotImplementedError;

import java.io.*;
import java.util.*;
import java.util.regex.Pattern;

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
     * <p>
     * <p>
     * Collections.sort() использует сортировку слиянием, также перебирается каждая строка в файле =>
     * трудоёмкость алгоритма = О(n*log(n)). Ресурсоёмкость = O(n), т.к. в списке хранится n элементов.
     */
    static public void sortTimes(String inputName, String outputName) throws Exception {
        List<Integer> resultList = new ArrayList<>();
        StringBuilder stringBuilder = new StringBuilder();
        Pattern pattern = Pattern.compile("(([0-1][0-9])|([2][0-3]))(:[0-5][0-9]){2}");
        BufferedReader reader = new BufferedReader(new FileReader(new File(inputName)));
        String line = reader.readLine();
        if (line == null) throw new IllegalArgumentException();
        while (line != null) {                                                              //O(n)
            if (!pattern.matcher(line).matches()) throw new IllegalArgumentException();
            Arrays.stream(line.split(":")).forEach(stringBuilder::append);
            resultList.add(Integer.parseInt(String.valueOf(stringBuilder)));
            line = reader.readLine();
            stringBuilder.setLength(0);
        }
        reader.close();
        Collections.sort(resultList);                                                       //O(n*log(n))
        FileWriter fileWriter = new FileWriter(outputName);
        for (int j = 0; j <= resultList.size() - 1; j++) {                                  //O(n)
            stringBuilder.append(String.format("%06d", resultList.get(j)));
            stringBuilder.insert(2, ":");
            stringBuilder.insert(5, ":");
            fileWriter.write(String.valueOf(stringBuilder) + "\n");
            stringBuilder.setLength(0);
        }
        fileWriter.close();
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
     * <p>
     * <p>
     * Трудоёмкость алгоритма = О(n*log(n)). Ресурсоёмкость = O(n).
     */
    static public void sortAddresses(String inputName, String outputName) throws Exception {
        File input = new File(inputName);
        StringBuilder stringBuilder = new StringBuilder();
        List<String> resultList = new ArrayList<>();
        Pattern pattern = Pattern.compile("([А-Я][а-я]* ){2}- [А-Я][а-я]* [1-9]+");
        BufferedReader reader = new BufferedReader(new FileReader(input));
        String line = reader.readLine();
        if (line == null) throw new IllegalArgumentException();
        while (line != null) {                                                                  //O(n)
            if (!pattern.matcher(line).matches()) throw new IllegalArgumentException();
            String[] strings = line.split(" ");
            stringBuilder.append(strings[3]).append(" ").append(strings[4]).append(" - ");
            stringBuilder.append(strings[0]).append(" ").append(strings[1]);
            resultList.add(String.valueOf(stringBuilder));
            line = reader.readLine();
            stringBuilder.setLength(0);
        }
        reader.close();
        Collections.sort(resultList);                                                           //O(n*log(n))
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(outputName));
        if (resultList.size() != 0) {
            bufferedWriter.write(resultList.get(0));
        }
        for (int i = 1; i < resultList.size(); i++) {                                           //O(n)
            stringBuilder.append("\n").append(resultList.get(i));
            String[] elementOfResultList = resultList.get(i).split(" - ");
            if (resultList.get(i - 1).split(" - ")[0].equals(elementOfResultList[0])) {
                stringBuilder.setLength(0);
                stringBuilder.append(", ").append(elementOfResultList[1]);
            }
            bufferedWriter.write(String.valueOf(stringBuilder));
            stringBuilder.setLength(0);
        }
        bufferedWriter.close();
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
     * <p>
     * <p>
     * Трудоёмкость = О(n). Ресурсоёмкость = О(n) (список элементов О(n) + массив элементов О(n)).
     */
    static public void sortTemperatures(String inputName, String outputName) throws Exception {
        StringBuilder stringBuilder = new StringBuilder();
        List<Integer> list = new ArrayList<>();
        File input = new File(inputName);
        int max = -2731;
        int min = 5001;
        Pattern pattern = Pattern.compile("(-)?[0-9]+.[0-9]");
        BufferedReader reader = new BufferedReader(new FileReader(input));
        String line = reader.readLine();
        if (line == null) throw new IllegalArgumentException();
        while (line != null) {                                                               //O(n)
            if (!pattern.matcher(line).matches()) throw new IllegalArgumentException();
            String[] strings = line.split("\\.");
            int number = Integer.parseInt(strings[0] + strings[1]);
            if (number > max) {
                max = number;
            } else if (number < min) {
                min = number;
            }
            list.add(number);
            line = reader.readLine();
        }
        reader.close();
        int[] newArray = new int[max - min + 1];                                              //O(n)
        for (int element : list) {
            newArray[element - min]++;
        }
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(outputName));
        for (int i = 0; i < newArray.length; i++) {                                          //O(n)
            int count = newArray[i];
            if (count != 0) {
                stringBuilder.append(i + min);
                if (String.valueOf((i + min)).matches("(-)?[0-9]")) {
                    stringBuilder.insert(stringBuilder.length() - 1, "0.");
                } else {
                    stringBuilder.insert(stringBuilder.length() - 1, ".");
                }
            }
            while (count != 0) {
                bufferedWriter.write(stringBuilder + "\n");
                count--;
            }
            stringBuilder.setLength(0);
        }
        bufferedWriter.close();
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
     * <p>
     * <p>
     * Трудоёмкость = О(n*log(n)), Ресурсоёмкость = О(n).
     */
    static public void sortSequence(String inputName, String outputName) throws Exception {
        File input = new File(inputName);
        List<Integer> list = new ArrayList<>();
        Pattern pattern = Pattern.compile("[1-9][0-9]*");
        BufferedReader reader = new BufferedReader(new FileReader(input));
        String line = reader.readLine();
        if (line == null || !pattern.matcher(line).matches()) throw new IllegalArgumentException();
        while (line != null) {                                                                      //O(n)
            if (!pattern.matcher(line).matches()) throw new IllegalArgumentException();
            list.add(Integer.parseInt(line));
            line = reader.readLine();
        }
        reader.close();
        List<Integer> unsortedList = new ArrayList<>(list);
        Collections.sort(list);                                                                     //O(n*log(n))
        int maxQuantityOfRepeats = 1;
        int maxRepeatingNumber = list.get(0);
        int quantityOfRepeats = 1;
        for (int i = 1; i < list.size(); i++) {                                                     //O(n)
            if (list.get(i).equals(list.get(i - 1))) {
                quantityOfRepeats++;
                if (i == list.size() - 1 && maxQuantityOfRepeats < quantityOfRepeats) {
                    maxQuantityOfRepeats = quantityOfRepeats;
                    maxRepeatingNumber = list.get(i - 1);
                }
            } else {
                if (maxQuantityOfRepeats < quantityOfRepeats) {
                    maxQuantityOfRepeats = quantityOfRepeats;
                    maxRepeatingNumber = list.get(i - 1);
                }
                quantityOfRepeats = 1;
            }
        }
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(outputName));
        for (int element : unsortedList) {                                                          //O(n)
            if (element != maxRepeatingNumber) {
                bufferedWriter.write(element + "\n");
            }
        }
        while (maxQuantityOfRepeats != 0) {
            bufferedWriter.write(maxRepeatingNumber + "\n");
            maxQuantityOfRepeats--;
        }
        bufferedWriter.close();
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
