package com.rushi.erp.model;

public class Examination {
    private int id, studentId;
    private String subject, examType;
    private double marks, maxMarks;

    public Examination(){}
    public Examination(int studentId,String subject,double marks,double maxMarks,String examType){
        this.studentId=studentId; this.subject=subject; this.marks=marks;
        this.maxMarks=maxMarks; this.examType=examType;
    }
    public int getId(){return id;} public void setId(int v){id=v;}
    public int getStudentId(){return studentId;} public void setStudentId(int v){studentId=v;}
    public String getSubject(){return subject;} public void setSubject(String v){subject=v;}
    public String getExamType(){return examType;} public void setExamType(String v){examType=v;}
    public double getMarks(){return marks;} public void setMarks(double v){marks=v;}
    public double getMaxMarks(){return maxMarks;} public void setMaxMarks(double v){maxMarks=v;}
    public double getPercentage(){return maxMarks==0?0:marks*100.0/maxMarks;}
    @Override public String toString(){
        return String.format("Subject=%s | Exam=%s | Marks=%.2f/%.2f | Percentage=%.2f%%",
                subject,examType,marks,maxMarks,getPercentage());
    }
}
