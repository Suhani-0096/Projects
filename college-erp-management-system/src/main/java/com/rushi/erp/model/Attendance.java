package com.rushi.erp.model;

public class Attendance {
    private int id, studentId, totalClasses, attendedClasses;
    private String subject;

    public Attendance(){}
    public Attendance(int studentId,String subject,int total,int attended){
        this.studentId=studentId; this.subject=subject;
        this.totalClasses=total; this.attendedClasses=attended;
    }
    public int getId(){return id;} public void setId(int v){id=v;}
    public int getStudentId(){return studentId;} public void setStudentId(int v){studentId=v;}
    public String getSubject(){return subject;} public void setSubject(String v){subject=v;}
    public int getTotalClasses(){return totalClasses;} public void setTotalClasses(int v){totalClasses=v;}
    public int getAttendedClasses(){return attendedClasses;} public void setAttendedClasses(int v){attendedClasses=v;}
    public double getPercentage(){return totalClasses==0?0:(attendedClasses*100.0/totalClasses);}
    @Override public String toString(){
        return String.format("Subject=%s | Total=%d | Attended=%d | Attendance=%.2f%%",
                subject,totalClasses,attendedClasses,getPercentage());
    }
}
