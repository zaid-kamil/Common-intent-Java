package com.example.commonintent;

import org.hamcrest.MatcherAssert;
import org.junit.Test;

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
    }

    @Test
    public void wordCount_test(){

        int count = MainActivity.wordCount("this is sparta");
        assertEquals(count,3);
    }
}