package lesson2;

import kotlin.*;

import java.io.*;
import java.util.*;
import java.util.regex.Pattern;

@SuppressWarnings("unused")
public class JavaAlgorithms {

    /**
     * Получение наибольшей прибыли (она же -- поиск максимального подмассива)
     * Простая
     * <p>
     * Во входном файле с именем inputName перечислены цены на акции компании в различные (возрастающие) моменты времени
     * (каждая цена идёт с новой строки). Цена -- это целое положительное число. Пример:
     * <p>
     * 201
     * 196
     * 190
     * 198
     * 187
     * 194
     * 193
     * 185
     * <p>
     * Выбрать два момента времени, первый из них для покупки акций, а второй для продажи, с тем, чтобы разница
     * между ценой продажи и ценой покупки была максимально большой. Второй момент должен быть раньше первого.
     * Вернуть пару из двух моментов.
     * Каждый момент обозначается целым числом -- номер строки во входном файле, нумерация с единицы.
     * Например, для приведённого выше файла результат должен быть Pair(3, 4)
     * <p>
     * В случае обнаружения неверного формата файла бросить любое исключение.
     */
    static public Pair<Integer, Integer> optimizeBuyAndSell(String inputName) {
        throw new NotImplementedError();
    }

    /**
     * Задача Иосифа Флафия.
     * Простая
     * <p>
     * Образовав круг, стоят menNumber человек, пронумерованных от 1 до menNumber.
     * <p>
     * 1 2 3
     * 8   4
     * 7 6 5
     * <p>
     * Мы считаем от 1 до choiceInterval (например, до 5), начиная с 1-го человека по кругу.
     * Человек, на котором остановился счёт, выбывает.
     * <p>
     * 1 2 3
     * 8   4
     * 7 6 х
     * <p>
     * Далее счёт продолжается со следующего человека, также от 1 до choiceInterval.
     * Выбывшие при счёте пропускаются, и человек, на котором остановился счёт, выбывает.
     * <p>
     * 1 х 3
     * 8   4
     * 7 6 Х
     * <p>
     * Процедура повторяется, пока не останется один человек. Требуется вернуть его номер (в данном случае 3).
     * <p>
     * 1 Х 3
     * х   4
     * 7 6 Х
     * <p>
     * 1 Х 3
     * Х   4
     * х 6 Х
     * <p>
     * х Х 3
     * Х   4
     * Х 6 Х
     * <p>
     * Х Х 3
     * Х   х
     * Х 6 Х
     * <p>
     * Х Х 3
     * Х   Х
     * Х х Х
     * <p>
     * Трудоёмкость = О(n*m), ресурсоёмкость = O(n).
     */
    static public int josephTask(int menNumber, int choiceInterval) {
        if (menNumber < 1 || choiceInterval < 1) throw new IllegalArgumentException();
        boolean[] people = new boolean[menNumber];
        int countOfElementsWithFalse = menNumber;
        int countOfSteps;
        int indexOfPerson = 0;
        int result = 1;
        while (countOfElementsWithFalse != 1) {                                  //O(n)
            if (!people[indexOfPerson]) {
                countOfSteps = choiceInterval;
                while (countOfSteps != 0) {                                      //O(m)
                    if (countOfSteps == 1 && !people[indexOfPerson]) {
                        countOfElementsWithFalse--;
                        people[indexOfPerson] = true;
                        countOfSteps--;
                    } else if (!people[indexOfPerson]) {
                        countOfSteps--;
                    }
                    indexOfPerson = (indexOfPerson + 1) % menNumber;
                }
            } else {
                indexOfPerson = (indexOfPerson + 1) % menNumber;
            }
        }
        for (int i = 0; i < people.length; i++) {                                //O(n)
            if (!people[i]) return i + 1;
        }
        return result;
    }

    /**
     * Наибольшая общая подстрока.
     * Средняя
     * <p>
     * Дано две строки, например ОБСЕРВАТОРИЯ и КОНСЕРВАТОРЫ.
     * Найти их самую длинную общую подстроку -- в примере это СЕРВАТОР.
     * Если общих подстрок нет, вернуть пустую строку.
     * При сравнении подстрок, регистр символов *имеет* значение.
     * Если имеется несколько самых длинных общих подстрок одной длины,
     * вернуть ту из них, которая встречается раньше в строке first.
     * <p>
     * <p>
     * Трудоёмкость = O(n*m).
     */
    static public String longestCommonSubstring(String first, String second) {
        StringBuilder stringBuilder = new StringBuilder();
        String maxSubString = "";
        int countOfChangingI = 0;
        boolean needChangeJ = false;
        int i = 0;
        while (i < first.length()) {                                                //O(n)
            int j = 0;
            while (j < second.length()) {                                           //O(m)
                if (first.charAt(i) == second.charAt(j)) {
                    needChangeJ = true;
                    stringBuilder.append(first.charAt(i));
                    countOfChangingI++;
                    if (maxSubString.length() < stringBuilder.length()) {
                        maxSubString = String.valueOf(stringBuilder);
                    }
                    if (i == first.length() - 1) break;
                    i++;
                } else if (needChangeJ) {
                    i -= countOfChangingI;
                    countOfChangingI = 0;
                    stringBuilder.setLength(0);
                    needChangeJ = false;
                    j--;
                }
                j++;
            }
            i++;
        }
        return maxSubString;
    }

    /**
     * Число простых чисел в интервале
     * Простая
     * <p>
     * Рассчитать количество простых чисел в интервале от 1 до limit (включительно).
     * Если limit <= 1, вернуть результат 0.
     * <p>
     * Справка: простым считается число, которое делится нацело только на 1 и на себя.
     * Единица простым числом не считается.
     */
    static public int calcPrimesNumber(int limit) {
        throw new NotImplementedError();
    }

    /**
     * Балда
     * Сложная
     * <p>
     * В файле с именем inputName задана матрица из букв в следующем формате
     * (отдельные буквы в ряду разделены пробелами):
     * <p>
     * И Т Ы Н
     * К Р А Н
     * А К В А
     * <p>
     * В аргументе words содержится множество слов для поиска, например,
     * ТРАВА, КРАН, АКВА, НАРТЫ, РАК.
     * <p>
     * Попытаться найти каждое из слов в матрице букв, используя правила игры БАЛДА,
     * и вернуть множество найденных слов. В данном случае:
     * ТРАВА, КРАН, АКВА, НАРТЫ
     * <p>
     * И т Ы Н     И т ы Н
     * К р а Н     К р а н
     * А К в а     А К В А
     * <p>
     * Все слова и буквы -- русские или английские, прописные.
     * В файле буквы разделены пробелами, строки -- переносами строк.
     * Остальные символы ни в файле, ни в словах не допускаются.
     * <p>
     * Трудоёмкость = О(n*m), ресуемкость = О(n).
     */
    static public Set<String> baldaSearcher(String inputName, Set<String> words) throws Exception {
        Set<String> result = new HashSet<>();
        List<CharInWord> chars = new ArrayList<>();
        Pattern pattern = Pattern.compile("([А-Я]|Ё|[A-Z])( ([А-Я]|Ё|[A-Z]))*");

        BufferedReader reader = new BufferedReader(new FileReader(new File(inputName)));
        String line = reader.readLine();
        if (line == null) throw new IllegalAccessException();
        int countOfColumns = line.split(" ").length;
        int countOfLines = 0;
        while (line != null) {
            String[] strings = line.split(" ");
            if (!pattern.matcher(line).matches() || strings.length != countOfColumns)
                throw new IllegalArgumentException();
            Arrays.asList(strings).forEach(e -> chars.add(new CharInWord(e.charAt(0))));
            countOfLines++;
            line = reader.readLine();
        }
        reader.close();

        for (String word : words) {                                             //перебираем каждое слово:   О(m)
            List<Integer> indexesOfCharsThatCanBeInOurWord = new ArrayList<>();
            int i = 0;
            while (i < chars.size()) {                                          //перебираем каждый символ:  О(n)
                search(i, 0, chars, indexesOfCharsThatCanBeInOurWord,
                        word, countOfColumns, countOfLines, result);
                if (indexesOfCharsThatCanBeInOurWord.size() == word.length()) break;
                i++;
            }
        }
        return result;
    }

    private static void search(int i, int j, List<CharInWord> chars, List<Integer> indexesOfCharsThatCanBeInOurWord,
                               String word, int columns, int lines, Set<String> result) {
        if (chars.get(i).getSymbol() == word.charAt(j)) {
            indexesOfCharsThatCanBeInOurWord.add(i);
            chars.get(i).setWeUsedKey();
            j++;

            if (indexesOfCharsThatCanBeInOurWord.size() == word.length()) {
                result.add(word);
                indexesOfCharsThatCanBeInOurWord.forEach(e -> chars.get(e).setWeUsedKey());
            }

            if (indexesOfCharsThatCanBeInOurWord.size() != word.length() && i % columns != columns - 1 &&
                    columns > 1 && !chars.get(i + 1).getWeUsedSymbol())
                search(i + 1, j, chars, indexesOfCharsThatCanBeInOurWord, word, columns, lines, result);

            if (indexesOfCharsThatCanBeInOurWord.size() != word.length() && i % columns != 1 && columns > 1 &&
                    i > 1 && !chars.get(i - 1).getWeUsedSymbol())
                search(i - 1, j, chars, indexesOfCharsThatCanBeInOurWord, word, columns, lines, result);

            if (indexesOfCharsThatCanBeInOurWord.size() != word.length() && i >= columns &&
                    !chars.get(i - columns).getWeUsedSymbol())
                search(i - columns, j, chars, indexesOfCharsThatCanBeInOurWord, word, columns, lines, result);

            if (indexesOfCharsThatCanBeInOurWord.size() != word.length() && i <= columns * (lines - 1) - 1 &&
                    !chars.get(i + columns).getWeUsedSymbol())
                search(i + columns, j, chars, indexesOfCharsThatCanBeInOurWord, word, columns, lines, result);

            if (indexesOfCharsThatCanBeInOurWord.size() != word.length()) {
                indexesOfCharsThatCanBeInOurWord.remove(j - 1);
                chars.get(i).setWeUsedKey();
            }

        }
    }

    /**
     * Используется, чтобы при поиске слова избегать
     * повтора одних и тех же символов.
     */
    static class CharInWord {
        private char symbol;
        private boolean weUsedSymbol;

        CharInWord(char symbol) {
            this.symbol = symbol;
            this.weUsedSymbol = false;
        }

        char getSymbol() {
            return symbol;
        }

        boolean getWeUsedSymbol() {
            return weUsedSymbol;
        }

        void setWeUsedKey() {
            weUsedSymbol = !weUsedSymbol;
        }
    }
}