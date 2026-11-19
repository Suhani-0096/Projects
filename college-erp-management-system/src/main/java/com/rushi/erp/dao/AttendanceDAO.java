package com.rushi.erp.dao;

import com.rushi.erp.model.Attendance;
import com.rushi.erp.util.Connectivity;
import java.sql.*;
import java.util.*;

public class AttendanceDAO {
    public void save(Attendance a)throws SQLException{
        String sql="INSERT INTO attendance(student_id,subject,total_classes,attended_classes) VALUES(?,?,?,?) " +
                   "ON DUPLICATE KEY UPDATE total_classes=VALUES(total_classes),attended_classes=VALUES(attended_classes)";
        try(Connection c=Connectivity.con();PreparedStatement p=c.prepareStatement(sql)){
            p.setInt(1,a.getStudentId());p.setString(2,a.getSubject());
            p.setInt(3,a.getTotalClasses());p.setInt(4,a.getAttendedClasses());p.executeUpdate();
        }
    }
    public List<Attendance> findByStudent(int studentId)throws SQLException{
        List<Attendance> list=new ArrayList<>();
        String sql="SELECT * FROM attendance WHERE student_id=? ORDER BY subject";
        try(Connection c=Connectivity.con();PreparedStatement p=c.prepareStatement(sql)){
            p.setInt(1,studentId);try(ResultSet r=p.executeQuery()){
                while(r.next()){
                    Attendance a=new Attendance(studentId,r.getString("subject"),r.getInt("total_classes"),r.getInt("attended_classes"));
                    a.setId(r.getInt("id"));list.add(a);
                }
            }
        } return list;
    }
}
