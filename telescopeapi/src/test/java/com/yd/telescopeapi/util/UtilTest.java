package com.yd.telescopeapi.util;

import javafx.scene.input.DataFormat;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 测试工具
 *
 * @author zygong
 * @create 2017-12-28 10:30
 **/
public class UtilTest {

    private static Random random = new Random();

    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Test
    public void getTitle(){
        String firstLine = "Caused by: java.lang.IllegalStateException: Couldn't read row 728, col 0 from CursorWindow. Make sure the Cursor is initialized correctly before accessing data from it.";
        StringBuffer sb = null;
        Pattern pattern = Pattern.compile("(?<=:)(.+)(?=:)");
        Matcher matcher = pattern.matcher(firstLine);
        if(matcher.find()){
            System.out.println(matcher.group(1));
        }else{
            System.out.println("");
        }
    }
    @Test
    public void getDes(){
        String firstLine = "Caused by: java.lang.IllegalStateException: Couldn't read row 728, col 0 from CursorWindow. Make sure the Cursor is initialized correctly before accessing data from it.";
        StringBuffer sb = null;
        Pattern pattern = Pattern.compile("(?<=:)(.+)(?=.)");
        Matcher matcher = pattern.matcher(firstLine);
        if(matcher.find()){
            System.out.println(matcher.group(1));
        }else{
            System.out.println("");
        }
    }
    @Test
    public void getDes2(){
        String firstLine = "Caused by: java.lang.IllegalStateException: Couldn't read row 728, col 0 from CursorWindow. Make sure the Cursor is initialized correctly before accessing data from it.";
        System.out.println(firstLine.substring(firstLine.lastIndexOf(":") + 1));
    }

    @Test
    public void test(){
        System.out.println(UUID.randomUUID().toString().replace("-", ""));

        System.out.println(String.format("%02d", 10));
    }


    public Date randomDate(){
        Date date = null;
        int year = 2010 + random.nextInt(7);
        int month = 1 + random.nextInt(11);
        int day = 1 + random.nextInt(27);
        String dateStr = year + "-" + month + "-" + day + " " + random.nextInt(24) + ":" + random.nextInt(59) + ":" + random.nextInt(59);
        try {
            date = sdf.parse(dateStr);
        } catch (ParseException e) {
            date = new Date();
        }
        return date;
    }
}
