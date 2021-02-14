package org.mai.dep810.java_sql_lesson;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/*
Derby
        Сохранение состояния бд:
        1.в файловой системе, откуда к данным можно получить доступ после работы программы
        jdbc:derby:D:/dev/projects/database;create=true

        2.в оперативной памяти на время выполнения программы
        jdbc:derby:memory:database;create=true
*/

public class LibraryImpl implements Library {

    DataSource _ds;

    public LibraryImpl(DataSource ds) {
        _ds = ds;
    }

    @Override
    public void addNewBook(Book book) {
        String insert_query = "insert into BOOKS(book_id, book_title) values(?, ?)";
        try(PreparedStatement st = _ds.getConnection().prepareStatement(insert_query)) {
            st.setInt(1, book.getId());
            st.setString(2, book.getTitle());
            st.executeUpdate();
        }
        catch(SQLException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void addAbonent(Student student) {
        String insert_query = "insert into ABONENTS values(?, ?)";
        try(PreparedStatement st = _ds.getConnection().prepareStatement(insert_query)) {
            st.setInt(1, student.getId());
            st.setString(2, student.getName());
            st.executeUpdate();
        }
        catch(SQLException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void borrowBook(Book book, Student student) {
        String update_query = "update BOOKS set student_id = ? where book_id = ? and " +
                "exists(select * from ABONENTS where student_id = ? and student_name = ?)";
        try(PreparedStatement st = _ds.getConnection().prepareStatement(update_query)) {
            st.setInt(1, student.getId());
            st.setInt(2, book.getId());
            st.setInt(3, student.getId());
            st.setString(4, student.getName());
            st.executeUpdate();
        }
        catch(SQLException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void returnBook(Book book, Student student) {
        String update_query = "update BOOKS set student_id = null where book_id = ? and" +
                " student_id = ?";
        try(PreparedStatement st = _ds.getConnection().prepareStatement(update_query)) {
            st.setInt(1, book.getId());
            st.setInt(2, student.getId());
            st.executeUpdate();
        }
        catch(SQLException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public List<Book> findAvailableBooks() {
        List<Book> b_l = new ArrayList<>();
        try(Statement st = _ds.getConnection().createStatement();
            ResultSet res = st.executeQuery("select book_id, book_title from BOOKS where student_id is null")) {
            while(res.next()) {
                b_l.add(new Book(res.getInt("book_id"), res.getString("book_title")));
            }
        }
        catch (SQLException ex) {
            ex.printStackTrace();
        }
        return b_l;
    }

    @Override
    public List<Student> getAllStudents() {
        List<Student> b_l = new ArrayList<>();
        try(Statement st = _ds.getConnection().createStatement();
            ResultSet res = st.executeQuery("select * from ABONENTS")) {
            while(res.next()) {
                b_l.add(new Student(res.getInt("student_id"), res.getString("student_name")));
            }
        }
        catch (SQLException ex) {
            ex.printStackTrace();
        }
        return b_l;
    }
}
