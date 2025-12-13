import functions.Function;
import functions.TabulatedFunction;
import functions.TabulatedFunctions;
import functions.Functions;
import functions.basic.Sin;
import functions.basic.Cos;
import functions.basic.Exp;
import functions.basic.Log;

import java.io.*;

public class    Main {
    private static final double PI = Math.PI;

    public static void main(String[] args) {
        try {
            // Тест 1: Sin и Cos на отрезке от 0 до π
            testSinAndCos();

            // Тест 2: Табулированные аналоги Sin и Cos
            testTabulatedSinAndCos();

            // Тест 3: Сумма квадратов табулированных синуса и косинуса
            testSumOfSquares();

            // Тест 4: Экспонента с записью/чтением через символьный поток
            testExpWithTextFile();

            // Тест 5: Логарифм с записью/чтением через байтовый поток
            testLogWithBinaryFile();

            // Тест 6: Изучение содержимого файлов
            analyzeFiles();

        } catch (Exception e) {
            System.err.println("Ошибка: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Тест 1: Создание объектов Sin и Cos, вывод значений на отрезке от 0 до π с шагом 0.1
     */
    private static void testSinAndCos() {
        System.out.println("=== ТЕСТ 1: SIN И COS ===\n");

        Function sin = new Sin();
        Function cos = new Cos();

        System.out.println("Значения sin(x) и cos(x) на отрезке [0, π] с шагом 0.1:");
        System.out.printf("%-8s %-15s %-15s%n", "x", "sin(x)", "cos(x)");
        System.out.println("---------------------------------------------------");

        for (double x = 0; x <= PI + 0.05; x += 0.1) {
            double sinValue = sin.getFunctionValue(x);
            double cosValue = cos.getFunctionValue(x);
            System.out.printf("%-8.2f %-15.6f %-15.6f%n", x, sinValue, cosValue);
        }
        System.out.println();
    }

    /**
     * Тест 2: Табулированные аналоги Sin и Cos, сравнение с исходными функциями
     */
    private static void testTabulatedSinAndCos() {
        System.out.println("=== ТЕСТ 2: ТАБУЛИРОВАННЫЕ АНАЛОГИ SIN И COS ===\n");

        Function sin = new Sin();
        Function cos = new Cos();

        // Создание табулированных аналогов с 10 точками
        TabulatedFunction tabulatedSin = TabulatedFunctions.tabulate(sin, 0, PI, 10);
        TabulatedFunction tabulatedCos = TabulatedFunctions.tabulate(cos, 0, PI, 10);

        System.out.println("Сравнение исходных и табулированных функций на отрезке [0, π] с шагом 0.1:");
        System.out.printf("%-8s %-15s %-15s %-15s %-15s%n", 
            "x", "sin(x)", "tab_sin(x)", "cos(x)", "tab_cos(x)");
        System.out.println("--------------------------------------------------------------------------------");

        for (double x = 0; x <= PI + 0.05; x += 0.1) {
            double sinValue = sin.getFunctionValue(x);
            double tabSinValue = tabulatedSin.getFunctionValue(x);
            double cosValue = cos.getFunctionValue(x);
            double tabCosValue = tabulatedCos.getFunctionValue(x);

            System.out.printf("%-8.2f %-15.6f %-15.6f %-15.6f %-15.6f%n",
                x, sinValue, tabSinValue, cosValue, tabCosValue);
        }

        // Вычисление максимальной погрешности
        double maxSinError = 0;
        double maxCosError = 0;
        for (double x = 0; x <= PI + 0.05; x += 0.1) {
            double sinError = Math.abs(sin.getFunctionValue(x) - tabulatedSin.getFunctionValue(x));
            double cosError = Math.abs(cos.getFunctionValue(x) - tabulatedCos.getFunctionValue(x));
            if (sinError > maxSinError) maxSinError = sinError;
            if (cosError > maxCosError) maxCosError = cosError;
        }

        System.out.printf("\nМаксимальная погрешность для sin: %.6f%n", maxSinError);
        System.out.printf("Максимальная погрешность для cos: %.6f%n", maxCosError);
        System.out.println();
    }

    /**
     * Тест 3: Сумма квадратов табулированных синуса и косинуса
     */
    private static void testSumOfSquares() {
        System.out.println("=== ТЕСТ 3: СУММА КВАДРАТОВ ТАБУЛИРОВАННЫХ SIN И COS ===\n");

        Function sin = new Sin();
        Function cos = new Cos();

        // Тестирование с разным количеством точек
        int[] pointCounts = {10, 20, 50};

        for (int pointsCount : pointCounts) {
            System.out.println("Количество точек табуляции: " + pointsCount);

            TabulatedFunction tabulatedSin = TabulatedFunctions.tabulate(sin, 0, PI, pointsCount);
            TabulatedFunction tabulatedCos = TabulatedFunctions.tabulate(cos, 0, PI, pointsCount);

            // Создание квадратов функций
            Function sinSquared = Functions.power(tabulatedSin, 2);
            Function cosSquared = Functions.power(tabulatedCos, 2);

            // Создание суммы квадратов
            Function sumOfSquares = Functions.sum(sinSquared, cosSquared);

            System.out.printf("%-8s %-20s %-20s%n", "x", "sin²(x)+cos²(x)", "Ожидаемое (≈1.0)");
            System.out.println("------------------------------------------------------------");

            for (double x = 0; x <= PI + 0.05; x += 0.1) {
                double value = sumOfSquares.getFunctionValue(x);
                double expected = Math.sin(x) * Math.sin(x) + Math.cos(x) * Math.cos(x);
                System.out.printf("%-8.2f %-20.10f %-20.10f%n", x, value, expected);
            }

            // Вычисление максимального отклонения от 1.0
            double maxDeviation = 0;
            for (double x = 0; x <= PI + 0.05; x += 0.1) {
                double deviation = Math.abs(sumOfSquares.getFunctionValue(x) - 1.0);
                if (deviation > maxDeviation) maxDeviation = deviation;
            }
            System.out.printf("Максимальное отклонение от 1.0: %.10f%n%n", maxDeviation);
        }
    }

    /**
     * Тест 4: Экспонента с записью/чтением через символьный поток
     */
    private static void testExpWithTextFile() throws IOException {
        System.out.println("=== ТЕСТ 4: ЭКСПОНЕНТА С СИМВОЛЬНЫМ ПОТОКОМ ===\n");

        Function exp = new Exp();
        TabulatedFunction tabulatedExp = TabulatedFunctions.tabulate(exp, 0, 10, 11);

        // Запись в файл через символьный поток
        String textFileName = "exp_text.txt";
        try (FileWriter writer = new FileWriter(textFileName)) {
            TabulatedFunctions.writeTabulatedFunction(tabulatedExp, writer);
        }

        // Чтение из файла через символьный поток
        TabulatedFunction readExp;
        try (FileReader reader = new FileReader(textFileName)) {
            readExp = TabulatedFunctions.readTabulatedFunction(reader);
        }

        // Вывод исходного набора точек
        System.out.println("Исходный набор точек:");
        for (int i = 0; i < tabulatedExp.getPointsCount(); i++) {
            System.out.printf("  (%f, %f)%n", tabulatedExp.getPointX(i), tabulatedExp.getPointY(i));
        }

        // Вывод считанного набора точек
        System.out.println("\nСчитанный набор точек:");
        for (int i = 0; i < readExp.getPointsCount(); i++) {
            System.out.printf("  (%f, %f)%n", readExp.getPointX(i), readExp.getPointY(i));
        }

        System.out.println("\nСравнение исходной и считанной функции на отрезке [0, 10] с шагом 1:");
        System.out.printf("%-8s %-20s %-20s %-20s%n", "x", "Исходная exp(x)", "Считанная exp(x)", "Разница");
        System.out.println("--------------------------------------------------------------------------------");

        for (double x = 0; x <= 10; x += 1) {
            double originalValue = tabulatedExp.getFunctionValue(x);
            double readValue = readExp.getFunctionValue(x);
            double difference = Math.abs(originalValue - readValue);
            System.out.printf("%-8.1f %-20.10f %-20.10f %-20.10f%n", x, originalValue, readValue, difference);
        }
        System.out.println();
    }

    /**
     * Тест 5: Логарифм с записью/чтением через байтовый поток
     */
    private static void testLogWithBinaryFile() throws IOException {
        System.out.println("=== ТЕСТ 5: ЛОГАРИФМ С БАЙТОВЫМ ПОТОКОМ ===\n");

        // Создание логарифма по натуральному основанию (e)
        Function ln = new Log(Math.E);
        TabulatedFunction tabulatedLn = TabulatedFunctions.tabulate(ln, 0.1, 10, 11);

        // Запись в файл через байтовый поток
        String binaryFileName = "ln_binary.bin";
        try (FileOutputStream fos = new FileOutputStream(binaryFileName)) {
            TabulatedFunctions.outputTabulatedFunction(tabulatedLn, fos);
        }

        // Чтение из файла через байтовый поток
        TabulatedFunction readLn;
        try (FileInputStream fis = new FileInputStream(binaryFileName)) {
            readLn = TabulatedFunctions.inputTabulatedFunction(fis);
        }

        // Вывод исходного набора точек
        System.out.println("Исходный набор точек:");
        for (int i = 0; i < tabulatedLn.getPointsCount(); i++) {
            System.out.printf("  (%f, %f)%n", tabulatedLn.getPointX(i), tabulatedLn.getPointY(i));
        }

        // Вывод считанного набора точек
        System.out.println("\nСчитанный набор точек:");
        for (int i = 0; i < readLn.getPointsCount(); i++) {
            System.out.printf("  (%f, %f)%n", readLn.getPointX(i), readLn.getPointY(i));
        }

        System.out.println("\nСравнение исходной и считанной функции на отрезке [0.1, 10] с шагом 1:");
        System.out.printf("%-8s %-20s %-20s %-20s%n", "x", "Исходная ln(x)", "Считанная ln(x)", "Разница");
        System.out.println("--------------------------------------------------------------------------------");

        for (double x = 0.1; x <= 10; x += 1) {
            if (x < tabulatedLn.getLeftDomainBorder() || x > tabulatedLn.getRightDomainBorder()) {
                continue;
            }
            double originalValue = tabulatedLn.getFunctionValue(x);
            double readValue = readLn.getFunctionValue(x);
            double difference = Math.abs(originalValue - readValue);
            System.out.printf("%-8.1f %-20.10f %-20.10f %-20.10f%n", x, originalValue, readValue, difference);
        }
        System.out.println();
    }

    /**
     * Тест 6: Изучение содержимого файлов
     */
    private static void analyzeFiles() {
        System.out.println("=== ТЕСТ 6: АНАЛИЗ ФАЙЛОВ ===\n");

        String textFileName = "exp_text.txt";
        String binaryFileName = "ln_binary.bin";

        // Анализ текстового файла
        System.out.println("1. СОДЕРЖИМОЕ ТЕКСТОВОГО ФАЙЛА (exp_text.txt):");
        try (BufferedReader reader = new BufferedReader(new FileReader(textFileName))) {
            String line = reader.readLine();
            System.out.println("Содержимое: " + line);
            System.out.println("Размер файла: " + new File(textFileName).length() + " байт");
        } catch (IOException e) {
            System.err.println("Ошибка при чтении текстового файла: " + e.getMessage());
        }

        System.out.println("\n2. СОДЕРЖИМОЕ БАЙТОВОГО ФАЙЛА (ln_binary.bin):");
        try (FileInputStream fis = new FileInputStream(binaryFileName)) {
            System.out.println("Размер файла: " + new File(binaryFileName).length() + " байт");
            System.out.print("Первые 50 байт (в шестнадцатеричном виде): ");
            byte[] buffer = new byte[50];
            int bytesRead = fis.read(buffer);
            for (int i = 0; i < bytesRead; i++) {
                System.out.printf("%02X ", buffer[i]);
            }
            System.out.println();
        } catch (IOException e) {
            System.err.println("Ошибка при чтении байтового файла: " + e.getMessage());
        }

        System.out.println("\n3. ВЫВОДЫ О ПРЕИМУЩЕСТВАХ И НЕДОСТАТКАХ:");
        System.out.println("ТЕКСТОВЫЙ ФОРМАТ:");
        System.out.println("  Преимущества:");
        System.out.println("    - Человекочитаемый формат");
        System.out.println("    - Легко отлаживать и проверять");
        System.out.println("    - Можно редактировать вручную");
        System.out.println("    - Кроссплатформенный");
        System.out.println("  Недостатки:");
        System.out.println("    - Больший размер файла");
        System.out.println("    - Медленнее парсинг");
        System.out.println("    - Зависимость от локали (разделитель десятичных чисел)");
        System.out.println();
        System.out.println("БАЙТОВЫЙ ФОРМАТ:");
        System.out.println("  Преимущества:");
        System.out.println("    - Компактный размер файла");
        System.out.println("    - Быстрый ввод/вывод");
        System.out.println("    - Точное представление чисел (без потери точности при парсинге)");
        System.out.println("    - Независимость от локали");
        System.out.println("  Недостатки:");
        System.out.println("    - Нечитаемый формат");
        System.out.println("    - Сложнее отлаживать");
        System.out.println("    - Невозможно редактировать вручную");
        System.out.println("    - Зависимость от порядка байт (endianness)");
    }
}
