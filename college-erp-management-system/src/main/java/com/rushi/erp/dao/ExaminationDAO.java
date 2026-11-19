package com.rushi.erp.dao;

import com.rushi.erp.model.Examination;
import com.rushi.erp.util.Connectivity;
import java.sql.*;
import java.util.*;

public class ExaminationDAO {
    public void save(Examination e)throws SQLException{
        String sql="INSERT INTO examinations(student_id,subject,marks,max_marks,exam_type) VALUES(?,?,?,?,?) " +
                   "ON DUPLICATE KEY UPDATE marks=VALUES(marks),max_marks=VALUES(max_marks)";
        try(Connection c=Connectivity.con();PreparedStatement p=c.prepareStatement(sql)){
            p.setInt(1,e.getStudentId());p.setString(2,e.getSubject());p.setDouble(3,e.getMarks());
            p.setDouble(4,e.getMaxMarks());p.setString(5,e.getExamType());p.executeUpdate();
        }
    }
    public List<Examination> findByStudent(int studentId)throws SQLException{
        List<Examination> list=new ArrayList<>();
        String sql="SELECT * FROM examinations WHERE student_id=? ORDER BY subject,exam_type";
        try(Connection c=Connectivity.con();PreparedStatement p=c.prepareStatement(sql)){
            p.setInt(1,studentId);try(ResultSet r=p.executeQuery()){
                while(r.next()){
                    Examination e=new Examination(studentId,r.getString("subject"),r.getDouble("marks"),
                            r.getDouble("max_marks"),r.getString("exam_type"));
                    e.setId(r.getInt("id"));list.add(e);
                }
            }
        } return list;
    }
}
