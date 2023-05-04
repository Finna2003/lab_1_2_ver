import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

public class TestNG {
    public  double x;
    public double xFalse;
    public  double y;
    public double yAdams;
    public double h;
    public double derivativeY;
    public double derivativeY1;
    public double derivativeY2;
    public double derivativeY3;

    @BeforeClass
    public static void init() {
    }
    @BeforeMethod(groups={"groupA", "groupB"})
    public void init2() {
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
    @Test(groups="groupA")
    public void testFunc() {
        double expected = 2 * y + Math.sqrt(x);
        double actual = Main.func(x, y);
        Assert.assertNotNull(actual);
        Assert.assertEquals(expected, actual, 0.0001);
    }
    @Test(groups="groupB")
    public void testRunge() {
        double actual = Main.Runge(y, x, h);
        Assert.assertNotNull(actual);
        double expected = 2.55244044;
        Assert.assertEquals(actual, expected, 0.0001);
    }

    @DataProvider(name = "funcValues")
    public Object[][] createDataForFunc() {
        return new Object[][]{{2.0, 1.0, 3.41421}, {3.0, 2.0, 5.73205}};
    }

    @Test(dataProvider = "funcValues",groups="groupA")
    public void testFunc(double x1, double y1, double expected) {
        double actual = Main.func(x1, y1);
        Assert.assertNotNull(actual);
        Assert.assertEquals(expected, actual, 0.0001);
    }

    @Test(expectedExceptions = IllegalArgumentException.class,groups="groupA")
    public void testFuncThrowsException() {
        Main.func(xFalse, y);
    }


    @Test(groups="groupB")
    public void testImplicitAdams() {
        double expected = 5.0875;
        double actual = Main.implicitAdams(h, yAdams, derivativeY, derivativeY1, derivativeY2, derivativeY3);
        Assert.assertNotNull(actual);
        Assert.assertEquals(expected, actual, 0.0001);
    }

    @Test(groups="groupB")
    public void testResult() {
        double[] expected = {2.0, 2.552440442408508, 3.2316781263791055, 4.065382792338085, 5.0875349381951755, 6.319587606804799, 7.8255361548065485, 9.665169898098803, 11.90844788846739, 14.643217923566356, 17.976101852764664};
        double[] actual = new double[11];
        actual[0] = y;
        for (int i = 11; i < 14; i++) {
            y = Main.Runge(y, x, h);
            actual[i - 10] = y;
            x += h;
        }
        double[] arrayfunc = new double[11];
        double x1 = x;
        for (int i = 0; i < 4; i++) {
            arrayfunc[i] = Main.func(x1, actual[i]);
            x1 += h;
        }
        double[] array1 = new double[10];
        for (int i = 0; i < 3; i++) {
            array1[i] = arrayfunc[i + 1] - arrayfunc[i];
        }

        double[] array2 = new double[9];
        for (int i = 0; i < 2; i++) {
            array2[i] = array1[i + 1] - array1[i];
        }

        double[] array3 = new double[8];
        for (int i = 0; i < 1; i++) {
            array3[i] = array2[i + 1] - array2[i];
        }

        for (int i = 4; i < 11; i++) {
            actual[i] = Main.implicitAdams(h, actual[i - 1], arrayfunc[i - 1], array1[i - 2], array2[i - 3], array3[i - 4]);
            x += h;
            arrayfunc[i] = Main.func(x, actual[i]);
            array1[i - 1] = arrayfunc[i] - arrayfunc[i - 1];
            array2[i - 2] = array1[i - 1] - array1[i - 2];
            array3[i - 3] = array2[i - 2] - array2[i - 3];
        }

        assertArrayEquals(expected, actual, 0.0001);


    }
}

