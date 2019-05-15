package aos.ru.cypix.candy;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public abstract class CandyServiceBase {

    protected ICandyEater[] candyEaters;

    /**
     * Сервис получает при инициализации массив доступных пожирателей конфет
     * @param candyEaters
     */
    public CandyServiceBase(ICandyEater[] candyEaters) {
        this.candyEaters = candyEaters;
    }

    /**
     * Добавить конфету на съедение
     * @param candy
     */
    public abstract void addCandy(ICandy candy);
}
