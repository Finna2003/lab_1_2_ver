import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.number.IsCloseTo.closeTo;
import static org.junit.jupiter.api.Assertions.*;


public class Tests {
    public static double  x ;
    public static double  xFalse ;
    public static double  y ;
    public static double  yAdams ;
    public static double  h;
    public static double derivativeY ;
    public static double derivativeY1 ;
    public static double derivativeY2 ;
    public static double derivativeY3 ;
    @BeforeAll
    public static void init() {
        x=1.0;
        h=0.1;
        y=2.0;
        xFalse=-1.0;
        yAdams=4.065382;
        derivativeY = 9.3956766;
        derivativeY1 = 1.7075755;
        derivativeY2 = 0.30757124;
        derivativeY3 = 0.05548837;
    }

    @Test
    void testFunc() {
        double expected = 2*y + Math.sqrt(x);
        double actual = Main.func(x, y);
        assertNotNull(actual);
        assertTrue(expected == actual);
    }
    @ParameterizedTest
    @CsvSource({"2.0, 1.0, 3.41421", "3.0, 2.0, 5.73205"})
    void testFunc(double x1, double y1, double expected) {
        double actual = Main.func(x1, y1);
        assertNotNull(actual);
        assertEquals(expected, actual, 0.0001);
    }
    @Test
    public void testFuncThrowsException() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            Main.func(xFalse, y);
        });
    }

    @Test
    void testRunge() {
        double expected = 2.55244044;
        double actual = Main.Runge(y, x, h);
        assertNotNull(actual);
        assertEquals(expected, actual, 0.0001);
    }
    @Test
    void testImplicitAdams() {
        double expected = 5.0875;
        double actual = Main.implicitAdams(h, yAdams, derivativeY, derivativeY1, derivativeY2, derivativeY3);
        assertNotNull(actual);
        assertEquals(expected, actual, 0.0001);
    }
    @Test
    void testResult() {
        double[] expected = {2.0, 2.552440442408508, 3.2316781263791055, 4.065382792338085, 5.0875349381951755, 6.319587606804799, 7.8255361548065485, 9.665169898098803, 11.90844788846739, 14.643217923566356, 17.976101852764664};
        String arrExpected = Arrays.toString(expected).replaceAll("[\\[\\]]", "");
        double[] actual = new double[11];
        actual[0] = y;
        for(int i=11;i<14;i++){
            y=Main.Runge(y,x,h);
            actual[i-10]=y;
            x+=h;
        }
        double[] arrayfunc=new double[11];
        double x1=x;
        for(int i=0;i<4;i++)
        {
            arrayfunc[i]=Main.func(x1,actual[i]);
            x1+=h;
        }
        double[] array1=new double[10];
        for(int i=0;i<3;i++)
        {
            array1[i]=arrayfunc[i+1]-arrayfunc[i];
        }

        double[] array2=new double[9];
        for(int i=0;i<2;i++)
        {
            array2[i]=array1[i+1]-array1[i];
        }
        double[] array3=new double[8];
        for(int i=0;i<1;i++)
        {
            array3[i]=array2[i+1]-array2[i];
        }
        for(int i=4;i<11;i++)
        {
            actual[i]=Main.implicitAdams(h,actual[i-1],arrayfunc[i-1],array1[i-2],array2[i-3],array3[i-4]);
            x+=h;
            arrayfunc[i]=Main.func(x,actual[i]);
            array1[i-1]=arrayfunc[i]-arrayfunc[i-1];
            array2[i-2]=array1[i-1]-array1[i-2];
            array3[i-3]=array2[i-2]-array2[i-3];
        }
        List<Double> listActual = Arrays.asList(Double.valueOf(actual[0]), Double.valueOf(actual[1]), Double.valueOf(actual[2]), Double.valueOf(actual[3]), Double.valueOf(actual[4]), Double.valueOf(actual[5]), Double.valueOf(actual[6]), Double.valueOf(actual[7]), Double.valueOf(actual[8]), Double.valueOf(actual[9]), Double.valueOf(actual[10]));
        String arrActual = Arrays.toString(actual).replaceAll("[\\[\\]]", "");



        assertArrayEquals(expected, actual, 0.0001);
        assertThat(arrActual, matchesRegex("^\\d+\\.\\d+(, \\d+\\.\\d+){10}$"));
        assertThat(arrExpected, matchesRegex("^\\d+\\.\\d+(, \\d+\\.\\d+){10}$"));
        assertThat(arrExpected, equalTo(arrActual));
        assertThat(listActual, hasItems(17.976101852764664));
    }

}