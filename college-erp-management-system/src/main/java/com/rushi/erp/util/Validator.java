package com.rushi.erp.util;

public final class Validator {
    private Validator(){}
    public static String required(String value,String field){
        if(value==null || value.trim().isEmpty())
            throw new IllegalArgumentException(field+" cannot be empty.");
        return value.trim();
    }
    public static int semester(int value){
        if(value<1 || value>12) throw new IllegalArgumentException("Semester must be between 1 and 12.");
        return value;
    }
    public static int classes(int total,int attended){
        if(total<=0) throw new IllegalArgumentException("Total classes must be greater than 0.");
        if(attended<0 || attended>total) throw new IllegalArgumentException("Attended classes must be between 0 and total classes.");
        return attended;
    }
    public static double marks(double marks,double max){
        if(max<=0 || marks<0 || marks>max) throw new IllegalArgumentException("Marks must be between 0 and maximum marks.");
        return marks;
    }
}
