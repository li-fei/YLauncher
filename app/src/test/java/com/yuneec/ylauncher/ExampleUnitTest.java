package com.yuneec.ylauncher;

import android.util.Log;

import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);

        int max = 200;
        int min = -200;
        Random random = new Random();
        int x = random.nextInt(max - min) + min;
        System.out.println("ExampleUnitTest  x:" + x);

        int translateX = -random.nextInt(200);
        int translateY = -random.nextInt(100);
        if (translateX > -100) {
            translateX = 200 + translateX;
        }
        if (translateY > -50) {
            translateY = 100 + translateY;
        }

        System.out.println("ExampleUnitTest  translateX:" + translateX + "  translateY:" + translateY);

    }
}