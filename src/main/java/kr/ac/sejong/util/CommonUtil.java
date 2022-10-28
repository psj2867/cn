package kr.ac.sejong.util;

public class CommonUtil{
    public static boolean isEmpty(String s){
        if( s == null)
            return true;
        return s.isBlank();
    }
}