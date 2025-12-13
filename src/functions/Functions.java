package functions;

import functions.meta.*;

/**
 * Утилитарный класс, содержащий вспомогательные статические методы
 * для работы с функциями. Нельзя создать объект этого класса.
 */
public class Functions {

    /**
     * Приватный конструктор для предотвращения создания экземпляров класса.
     */
    private Functions() {
        throw new AssertionError("Cannot instantiate utility class");
    }

    /**
     * Возвращает объект функции, полученной из исходной сдвигом вдоль осей.
     * @param f исходная функция
     * @param shiftX величина сдвига вдоль оси абсцисс
     * @param shiftY величина сдвига вдоль оси ординат
     * @return функция, полученная сдвигом исходной функции
     */
    public static Function shift(Function f, double shiftX, double shiftY) {
        return new Shift(f, shiftX, shiftY);
    }

    /**
     * Возвращает объект функции, полученной из исходной масштабированием вдоль осей.
     * @param f исходная функция
     * @param scaleX коэффициент масштабирования вдоль оси абсцисс
     * @param scaleY коэффициент масштабирования вдоль оси ординат
     * @return функция, полученная масштабированием исходной функции
     */
    public static Function scale(Function f, double scaleX, double scaleY) {
        return new Scale(f, scaleX, scaleY);
    }

    /**
     * Возвращает объект функции, являющейся заданной степенью исходной.
     * @param f исходная функция
     * @param power степень, в которую возводятся значения функции
     * @return функция, являющаяся степенью исходной функции
     */
    public static Function power(Function f, double power) {
        return new Power(f, power);
    }

    /**
     * Возвращает объект функции, являющейся суммой двух исходных.
     * @param f1 первая функция
     * @param f2 вторая функция
     * @return функция, являющаяся суммой двух исходных функций
     */
    public static Function sum(Function f1, Function f2) {
        return new Sum(f1, f2);
    }

    /**
     * Возвращает объект функции, являющейся произведением двух исходных.
     * @param f1 первая функция
     * @param f2 вторая функция
     * @return функция, являющаяся произведением двух исходных функций
     */
    public static Function mult(Function f1, Function f2) {
        return new Mult(f1, f2);
    }

    /**
     * Возвращает объект функции, являющейся композицией двух исходных.
     * @param f1 первая функция (внутренняя)
     * @param f2 вторая функция (внешняя)
     * @return функция, являющаяся композицией f2(f1(x))
     */
    public static Function composition(Function f1, Function f2) {
        return new Composition(f1, f2);
    }
}



