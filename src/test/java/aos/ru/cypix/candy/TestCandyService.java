package aos.ru.cypix.candy;

import static org.mockito.Mockito.*;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.stubbing.answers.Returns;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.*;

public class TestCandyService {
    private ICandy candy1,candy2,candy3,candy4,candy5,candy6,candy7;
    private ICandyEater candyEater1,candyEater2,candyEater3,candyEater4;
    ICandyEater[] iCandyEaters;
    private CandyServiceBase candyService;
    @Before
    public void init(){
        candy1 = mock(ICandy.class);
        candy2 = mock(ICandy.class);
        candy3 = mock(ICandy.class);
        candy4 = mock(ICandy.class);
        candy5 = mock(ICandy.class);
        candy6 = mock(ICandy.class);
        candy7 = mock(ICandy.class);


        candyEater1 = mock(ICandyEater.class);
        candyEater2 = mock(ICandyEater.class);
        candyEater3 = mock(ICandyEater.class);
        candyEater4 = mock(ICandyEater.class);

        when(candy1.getCandyFlavour()).thenReturn(CandyFlavour.ALMOND.ordinal());
        when(candy2.getCandyFlavour()).thenReturn(CandyFlavour.ALMOND.ordinal());
        when(candy3.getCandyFlavour()).thenReturn(CandyFlavour.ALMOND.ordinal());
        when(candy4.getCandyFlavour()).thenReturn(CandyFlavour.CHERRY.ordinal());
        when(candy5.getCandyFlavour()).thenReturn(CandyFlavour.CHERRY.ordinal());
        when(candy6.getCandyFlavour()).thenReturn(CandyFlavour.CHOCOLATE.ordinal());
        when(candy7.getCandyFlavour()).thenReturn(CandyFlavour.CHOCOLATE.ordinal());

        iCandyEaters = new ICandyEater[]{candyEater1,candyEater2,candyEater3,candyEater4};

        candyService = new CandyService(iCandyEaters);

    }

    @After
    public void end(){}

    @Test
    public void testStrategy() throws InterruptedException{
//        Arrays.asList(iCandyEaters).parallelStream().
        Thread t = new Thread((CandyService)candyService);
        t.setDaemon(false);
        t.start();

        getCandies()
                .parallelStream()
                .forEach(candy->candyService.addCandy(candy));
        t.join();


    }

    private List<ICandy> getCandies(){
        List<ICandy> candies = new ArrayList<ICandy>();
        candies.add(candy1);
        candies.add(candy2);
        candies.add(candy3);
        candies.add(candy4);
        candies.add(candy5);
        candies.add(candy6);
        candies.add(candy7);

        return candies;
    }

}
