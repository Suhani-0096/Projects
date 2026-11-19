package com.rushi.erp.model;

public class Student {
    private int id;
    private String rollNo;
    private String name;
    private String email;
    private String course;
    private int semester;
    private String phone;

    public Student() {}

    public Student(String rollNo, String name, String email,
                   String course, int semester, String phone) {
        this.rollNo=rollNo; this.name=name; this.email=email;
        this.course=course; this.semester=semester; this.phone=phone;
    }

    public int getId(){return id;}
    public void setId(int id){this.id=id;}
    public String getRollNo(){return rollNo;}
    public void setRollNo(String v){rollNo=v;}
    public String getName(){return name;}
    public void setName(String v){name=v;}
    public String getEmail(){return email;}
    public void setEmail(String v){email=v;}
    public String getCourse(){return course;}
    public void setCourse(String v){course=v;}
    public int getSemester(){return semester;}
    public void setSemester(int v){semester=v;}
    public String getPhone(){return phone;}
    public void setPhone(String v){phone=v;}

    @Override
    public String toString() {
        return String.format("ID=%d | Roll=%s | Name=%s | Email=%s | Course=%s | Semester=%d | Phone=%s",
                id, rollNo, name, email, course, semester, phone);
    }
}
