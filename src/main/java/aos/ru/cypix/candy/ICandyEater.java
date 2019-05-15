package aos.ru.cypix.candy;


/**
 * Интерфейс пожирателя конфет, ест любые конфеты, но так как захочет.
 */
public interface ICandyEater {
    /**
     * Съесть конфету, может занять время
     * @param candy
     */
    void eat(ICandy candy) throws Exception;
}
