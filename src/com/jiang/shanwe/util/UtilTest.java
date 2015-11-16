package com.jiang.shanwe.util;

import java.util.Date;

import org.junit.Test;

public class UtilTest {

    @Test
    public void testGetDateRange() {
        Date date = new Date();
        System.out.println(DateUtil.getDateRange(date));
    }
}
