package aos.ru.cypix.candy;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class CandyService extends CandyServiceBase implements Runnable{

    private Integer countCandy = 0;
    private volatile List<ICandy> candies = new CopyOnWriteArrayList<>();
    protected Integer countCandyEater = 0;
    protected ConcurrentHashMap<ICandyEater, ICandy> candyEaterICandyMap = new ConcurrentHashMap<>();

    public CandyService(ICandyEater[] candyEaters) {
        super(candyEaters);

    }

    /**
     * Добавление конфет на поедание пожирателями
     * */
    @Override
    public void addCandy(ICandy candy) {
        Runnable r = ()->{
            candies.add(candy);
        };
        new Thread(r).start();
    }

    /**
     * Получение пожирателя который удовлитворяет требования
     * */
    private synchronized ICandyEater getCustomEater() throws java.lang.RuntimeException{
        if(Objects.nonNull(getFirstEater())) {
            return getFirstEater();
        }
        ICandyEater tempCandyEater = null;
        try{
            tempCandyEater = Arrays.stream(getCandyEaters())
                    .filter(candyEater->!candyEaterICandyMap.containsKey(candyEater))
                    .findFirst().orElseThrow(RuntimeException::new);
        } catch (RuntimeException e){

        }
        return tempCandyEater;
    }

    /**
     * Получение первого пожирателя
     * */
    private ICandyEater getFirstEater() throws NullPointerException, ArrayIndexOutOfBoundsException{
        if (candyEaterICandyMap.isEmpty()){
            return candyEaters[0];
        }
        return null;
    }

    /**
     * Обработка конфент переданных на поедание
     * */
    @Override
    public void run() {
        while (true) {
            try{
                getCandies().parallelStream().forEach(c -> {
                    putEaterCandy(checkCandyByEat(c));
                    eat();
                    System.out.println(c.getCandyFlavour());
                    System.out.println(Thread.currentThread().getId());
                });

            }catch(ConcurrentModificationException | NullPointerException e){
                    System.out.println(e.getMessage());
            }

        }
    }

    /**
     * Получение списка конфет которые были переданы на поедание (будут съедены)
     * @return List<ICandy>
     * */
    private List<ICandy> getCandies(){
        return candies;
    }

    /**
     * Проверка есть ли искомая конфета в карте конфет на поедание
     * @return ICandy
     * */
    private synchronized ICandy checkCandyByEat(ICandy candy){
        return candyEaterICandyMap.containsValue(candy) ? null : candy;
    }

    /**
     * Получение синхронизированной карты пожирателей
     * @return ConcurrentHashMap<ICandyEater,ICandy>
     * */
    private synchronized ConcurrentHashMap<ICandyEater,ICandy> getCandyEaterICandyMap(){
        return candyEaterICandyMap;
    }

    /**
     * Получение массива пожирателей конфет
     *
     * */
    private synchronized void putEaterCandy(ICandy candy){
        ICandyEater candyEater = getCustomEater();
        candyEaterICandyMap.put(candyEater,candy);
    }

    /**
     * Получение массива пожирателей конфет
     * @return ICandyEater[]
     * */
    private synchronized ICandyEater[] getCandyEaters(){
        return candyEaters;
    }

    /**
     * Получение массива пожирателей конфет
     * @return ICandyEater[]
     * */
    private synchronized void setEatedCandy(ICandyEater eater, ICandy candy){
        candyEaterICandyMap.remove(eater);
        candies.remove(candy);
    }
    /**
     * Отправка сервисом на поедание кофент пожирателем
     * */
    private void eat(){
        candyEaterICandyMap.entrySet().parallelStream().forEach(s->{
            try {
                s.getKey().eat(s.getValue());
                Thread.sleep(getRandTimeForEat());
                setEatedCandy(s.getKey(),s.getValue());

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Получить время ожидание поедания конфеты
     * @return Integer
     * */
    private Integer getRandTimeForEat(){
        return (int)(Math.random()*10000);
    }

}
