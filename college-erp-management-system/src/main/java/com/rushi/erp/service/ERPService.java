package com.rushi.erp.service;

import com.rushi.erp.dao.*;
import com.rushi.erp.model.*;
import com.rushi.erp.util.Validator;
import java.sql.SQLException;
import java.util.List;

public class ERPService {
    private final StudentDAO studentDAO=new StudentDAO();
    private final AttendanceDAO attendanceDAO=new AttendanceDAO();
    private final ExaminationDAO examinationDAO=new ExaminationDAO();

    public Student addStudent(Student s)throws SQLException{
        validateStudent(s); return studentDAO.add(s);
    }
    public List<Student> students()throws SQLException{return studentDAO.findAll();}
    public Student student(int id)throws SQLException{return studentDAO.findById(id);}
    public List<Student> searchStudents(String q)throws SQLException{return studentDAO.search(q);}
    public boolean updateStudent(Student s)throws SQLException{validateStudent(s);return studentDAO.update(s);}
    public boolean deleteStudent(int id)throws SQLException{return studentDAO.delete(id);}

    public void saveAttendance(Attendance a)throws SQLException{
        Validator.required(a.getSubject(),"Subject");
        Validator.classes(a.getTotalClasses(),a.getAttendedClasses());
        attendanceDAO.save(a);
    }
    public List<Attendance> attendance(int studentId)throws SQLException{return attendanceDAO.findByStudent(studentId);}

    public void saveExamination(Examination e)throws SQLException{
        Validator.required(e.getSubject(),"Subject");
        Validator.required(e.getExamType(),"Exam type");
        Validator.marks(e.getMarks(),e.getMaxMarks());
        examinationDAO.save(e);
    }
    public List<Examination> examinations(int studentId)throws SQLException{return examinationDAO.findByStudent(studentId);}

    private void validateStudent(Student s){
        Validator.required(s.getRollNo(),"Roll number");
        Validator.required(s.getName(),"Name");
        Validator.required(s.getEmail(),"Email");
        Validator.required(s.getCourse(),"Course");
        Validator.semester(s.getSemester());
    }
}
