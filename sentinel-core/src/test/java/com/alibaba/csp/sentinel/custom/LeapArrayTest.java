package com.alibaba.csp.sentinel.custom;

import org.junit.Test;

/**
 * @author gao chao
 * @date 2025/7/30
 */
public class LeapArrayTest {

    @Test
    public void testLeapArray() throws InterruptedException {
        int windowLength = 200;
        int arrayLength = 5;
        for (int i = 0; ; i++) {
            Thread.sleep(i);
            calculate(windowLength, arrayLength);
        }
    }

    private void calculate(int windowLength, int arrayLength) {
        long time = System.currentTimeMillis();
        long timeId = time / windowLength;
        long currentWindowStart = time - time % windowLength;
        int idx = (int) (timeId % arrayLength);
        System.out.println("time=" + time + ",currentWindowStart=" + currentWindowStart + ",timeId=" + timeId + ",idx=" + idx);
    }

}
