package com.rushi.erp.dao;

import com.rushi.erp.model.Student;
import com.rushi.erp.util.Connectivity;
import java.sql.*;
import java.util.*;

public class StudentDAO {
    public Student add(Student s) throws SQLException {
        String sql="INSERT INTO students(roll_no,name,email,course,semester,phone) VALUES(?,?,?,?,?,?)";
        try(Connection c=Connectivity.con(); PreparedStatement p=c.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS)){
            p.setString(1,s.getRollNo()); p.setString(2,s.getName()); p.setString(3,s.getEmail());
            p.setString(4,s.getCourse()); p.setInt(5,s.getSemester()); p.setString(6,s.getPhone());
            p.executeUpdate();
            try(ResultSet r=p.getGeneratedKeys()){if(r.next())s.setId(r.getInt(1));}
        }
        return s;
    }

    public List<Student> findAll() throws SQLException {
        List<Student> list=new ArrayList<>();
        try(Connection c=Connectivity.con();
            PreparedStatement p=c.prepareStatement("SELECT * FROM students ORDER BY id DESC");
            ResultSet r=p.executeQuery()){
            while(r.next())list.add(map(r));
        }
        return list;
    }

    public Student findById(int id) throws SQLException {
        try(Connection c=Connectivity.con(); PreparedStatement p=c.prepareStatement("SELECT * FROM students WHERE id=?")){
            p.setInt(1,id);
            try(ResultSet r=p.executeQuery()){if(r.next())return map(r);}
        }
        return null;
    }

    public List<Student> search(String key) throws SQLException {
        List<Student> list=new ArrayList<>();
        String q="%"+key.toLowerCase()+"%";
        String sql="SELECT * FROM students WHERE LOWER(name) LIKE ? OR LOWER(roll_no) LIKE ? OR LOWER(course) LIKE ? ORDER BY id DESC";
        try(Connection c=Connectivity.con(); PreparedStatement p=c.prepareStatement(sql)){
            p.setString(1,q);p.setString(2,q);p.setString(3,q);
            try(ResultSet r=p.executeQuery()){while(r.next())list.add(map(r));}
        }
        return list;
    }

    public boolean update(Student s) throws SQLException {
        String sql="UPDATE students SET roll_no=?,name=?,email=?,course=?,semester=?,phone=? WHERE id=?";
        try(Connection c=Connectivity.con(); PreparedStatement p=c.prepareStatement(sql)){
            p.setString(1,s.getRollNo());p.setString(2,s.getName());p.setString(3,s.getEmail());
            p.setString(4,s.getCourse());p.setInt(5,s.getSemester());p.setString(6,s.getPhone());p.setInt(7,s.getId());
            return p.executeUpdate()>0;
        }
    }

    public boolean delete(int id) throws SQLException {
        try(Connection c=Connectivity.con(); PreparedStatement p=c.prepareStatement("DELETE FROM students WHERE id=?")){
            p.setInt(1,id); return p.executeUpdate()>0;
        }
    }

    private Student map(ResultSet r)throws SQLException{
        Student s=new Student(r.getString("roll_no"),r.getString("name"),r.getString("email"),
                r.getString("course"),r.getInt("semester"),r.getString("phone"));
        s.setId(r.getInt("id")); return s;
    }
}
