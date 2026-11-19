package com.rushi.erp.ui;

import com.rushi.erp.model.*;
import com.rushi.erp.service.ERPService;
import java.sql.SQLException;
import java.util.*;

public class ERPMenu {
    private final ERPService service=new ERPService();
    private final Scanner sc=new Scanner(System.in);

    public void start(){
        while(true){
            menu();
            try{
                int ch=readInt("Choice: ");
                switch(ch){
                    case 1->addStudent();
                    case 2->viewStudents();
                    case 3->searchStudents();
                    case 4->updateStudent();
                    case 5->deleteStudent();
                    case 6->recordAttendance();
                    case 7->viewAttendance();
                    case 8->recordMarks();
                    case 9->viewMarks();
                    case 10->academicRecord();
                    case 0->{System.out.println("Goodbye.");sc.close();return;}
                    default->System.out.println("Invalid choice.");
                }
            }catch(SQLException e){
                System.out.println("DATABASE ERROR: "+e.getMessage());
            }catch(IllegalArgumentException e){
                System.out.println("VALIDATION ERROR: "+e.getMessage());
            }catch(Exception e){
                System.out.println("ERROR: "+e.getMessage());
            }
        }
    }

    private void menu(){
        System.out.println("\n--------------- COLLEGE ERP ---------------");
        System.out.println("1. Add Student");
        System.out.println("2. View Students");
        System.out.println("3. Search Student");
        System.out.println("4. Update Student");
        System.out.println("5. Delete Student");
        System.out.println("6. Record/Update Attendance");
        System.out.println("7. View Attendance");
        System.out.println("8. Add/Update Examination Marks");
        System.out.println("9. View Examination Marks");
        System.out.println("10. Complete Academic Record");
        System.out.println("0. Exit");
        System.out.println("--------------------------------------------");
    }

    private void addStudent()throws SQLException{
        System.out.println("\n--- Add Student ---");
        Student s=readStudent();
        System.out.println("Added: "+service.addStudent(s));
    }

    private Student readStudent(){
        return new Student(
            text("Roll number: "),text("Name: "),text("Email: "),
            text("Course: "),readInt("Semester: "),text("Phone: ")
        );
    }

    private void viewStudents()throws SQLException{printStudents(service.students());}

    private void searchStudents()throws SQLException{
        printStudents(service.searchStudents(text("Search name/roll/course: ")));
    }

    private void printStudents(List<Student> list){
        if(list.isEmpty()){System.out.println("No students found.");return;}
        System.out.println("\n--- Students ---");
        list.forEach(System.out::println);
    }

    private void updateStudent()throws SQLException{
        int id=readInt("Student ID: ");
        Student old=service.student(id);
        if(old==null){System.out.println("Student not found.");return;}
        System.out.println("Current: "+old);
        Student s=readStudent();s.setId(id);
        System.out.println(service.updateStudent(s)?"Updated successfully.":"Update failed.");
    }

    private void deleteStudent()throws SQLException{
        int id=readInt("Student ID: ");
        Student s=service.student(id);
        if(s==null){System.out.println("Student not found.");return;}
        if("YES".equalsIgnoreCase(text("Type YES to confirm: ")))
            System.out.println(service.deleteStudent(id)?"Deleted successfully.":"Delete failed.");
    }

    private void recordAttendance()throws SQLException{
        int id=studentId();
        Attendance a=new Attendance(id,text("Subject: "),readInt("Total classes: "),readInt("Attended classes: "));
        service.saveAttendance(a);
        System.out.printf("Attendance saved: %.2f%%%n",a.getPercentage());
    }

    private void viewAttendance()throws SQLException{
        int id=studentId();
        List<Attendance> list=service.attendance(id);
        if(list.isEmpty())System.out.println("No attendance records.");
        else list.forEach(System.out::println);
    }

    private void recordMarks()throws SQLException{
        int id=studentId();
        Examination e=new Examination(id,text("Subject: "),readDouble("Marks: "),readDouble("Maximum marks: "),text("Exam type (Internal/Final): "));
        service.saveExamination(e);
        System.out.printf("Marks saved: %.2f%%%n",e.getPercentage());
    }

    private void viewMarks()throws SQLException{
        int id=studentId();
        List<Examination> list=service.examinations(id);
        if(list.isEmpty())System.out.println("No examination records.");
        else list.forEach(System.out::println);
    }

    private void academicRecord()throws SQLException{
        int id=studentId();
        Student s=service.student(id);
        if(s==null){System.out.println("Student not found.");return;}
        System.out.println("\n========== ACADEMIC RECORD ==========");
        System.out.println(s);
        System.out.println("\nAttendance:");
        List<Attendance> at=service.attendance(id);
        if(at.isEmpty())System.out.println("No records.");else at.forEach(System.out::println);
        System.out.println("\nExaminations:");
        List<Examination> ex=service.examinations(id);
        if(ex.isEmpty())System.out.println("No records.");else ex.forEach(System.out::println);
        if(!ex.isEmpty()){
            double total=ex.stream().mapToDouble(Examination::getMarks).sum();
            double max=ex.stream().mapToDouble(Examination::getMaxMarks).sum();
            System.out.printf("\nOverall percentage: %.2f%%%n",total*100/max);
            System.out.println("Result: "+(total*100/max>=40?"PASS":"FAIL"));
        }
    }

    private int studentId()throws SQLException{
        int id=readInt("Student ID: ");
        if(service.student(id)==null)throw new IllegalArgumentException("Student does not exist.");
        return id;
    }

    private String text(String m){System.out.print(m);return sc.nextLine().trim();}
    private int readInt(String m){
        while(true){try{System.out.print(m);return Integer.parseInt(sc.nextLine().trim());}
        catch(NumberFormatException e){System.out.println("Enter a valid integer.");}}
    }
    private double readDouble(String m){
        while(true){try{System.out.print(m);return Double.parseDouble(sc.nextLine().trim());}
        catch(NumberFormatException e){System.out.println("Enter a valid number.");}}
    }
}
