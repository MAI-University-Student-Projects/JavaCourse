package org.mai.dep810.java_sql_lesson;

import org.junit.*;
import org.apache.derby.jdbc.EmbeddedDataSource; //new version for derby: ClientDataSource
import org.junit.runners.MethodSorters;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

//порядок выполнения тестов
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class LibraryImplTest {

    private static EmbeddedDataSource _ds;
    private Library _lib;
    /*Вызывается при инициализации класса AuctionImplTest*/
    @BeforeClass
    public static void setupClass() {
        _ds = new EmbeddedDataSource();
        _ds.setDatabaseName("memory:" + "data_lib");
        _ds.setDatabaseName("memory:" + "datab");
        _ds.setCreateDatabase("create");
        _ds.setUser("user");
        _ds.setPassword("password");
        try(Connection c = _ds.getConnection()) {
            c.setAutoCommit(false);
            try(Statement st = c.createStatement()) {
                st.executeUpdate("CREATE TABLE ABONENTS(\n" +
                        "    student_id int not null,\n" +
                        "    student_name varchar(255),\n" +
                        "    primary key (student_id)\n" +
                        ")");
                st.executeUpdate("CREATE TABLE BOOKS(\n" +
                        "    book_id int not null,\n" +
                        "    book_title varchar(255),\n" +
                        "    student_id int,\n" +
                        "    foreign key(student_id) references ABONENTS(student_id),\n" +
                        "    primary key (book_id)\n" +
                        ")");
                c.commit();
            }
            catch (SQLException ex) {
                c.rollback();
                ex.printStackTrace();
            }
            c.setAutoCommit(true);
        }
        catch(SQLException ex) {
            ex.printStackTrace();
        }
    }
    @Before
    public void setUp() {
        _lib = new LibraryImpl(_ds);
    }

    @After
    public void tearDown() {
        _lib = null;
    }

    @Test
    public void addNewBook() {
        _lib.addNewBook(new Book(0, "Harry Potter"));
        _lib.addNewBook(new Book(1, "Hobbit"));
        Map<Integer, String> m_tmp = new HashMap<>();
        try(Statement st = _ds.getConnection().createStatement();
            ResultSet res = st.executeQuery("select * from BOOKS")) {
            while(res.next()) {
                m_tmp.put(res.getInt("book_id"), res.getString("book_title"));
            }
            assertTrue(m_tmp.containsValue("Harry Potter"));
            assertTrue(m_tmp.containsValue("Hobbit"));
        }
        catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    @Test
    public void addAbonent() {
        _lib.addAbonent(new Student(0, "Vitya"));
        _lib.addAbonent(new Student(1, "Petya"));
        Map<Integer, String> m_tmp = new HashMap<>();
        try(Statement st = _ds.getConnection().createStatement();
            ResultSet res = st.executeQuery("select * from ABONENTS")) {
            while(res.next()) {
                m_tmp.put(res.getInt("student_id"), res.getString("student_name"));
            }
            assertTrue(m_tmp.containsValue("Vitya"));
            assertTrue(m_tmp.containsValue("Petya"));
        }
        catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    @Test
    public void borrowBook() {
        Book b = new Book(2, "Harry Potter");
        Student s = new Student(2, "Petya");
        _lib.addNewBook(b);
        _lib.addAbonent(s);
        _lib.borrowBook(b, s);
        try(Statement st = _ds.getConnection().createStatement();
            ResultSet res = st.executeQuery("select * from BOOKS where book_id=2")) {
            res.next();
            assertEquals(2, res.getInt("student_id"));

        }
        catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    @Test
    public void returnBook() {
        Book b = new Book(3, "Harry Potter");
        Student s = new Student(3, "Petya");
        _lib.addNewBook(b);
        _lib.addAbonent(s);
        _lib.returnBook(b, s);
        try(Statement st = _ds.getConnection().createStatement();
            ResultSet res = st.executeQuery("select * from BOOKS where book_id=3")) {
            res.next();
            assertEquals(0, res.getInt("student_id"));
        }
        catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    @Test
    public void findAvailableBooks() {
        _lib.addNewBook(new Book(4, "Harry Potter"));
        _lib.addNewBook(new Book(5, "Hobbit"));
        _lib.addNewBook(new Book(6, "Idiot"));
        _lib.addNewBook(new Book(7, "Lord of the Rings I"));
        List<Book> res = _lib.findAvailableBooks();
        assertThat(res, hasItem(new Book(6, "Idiot")));
    }

    @Test
    public void getAllStudents() {
        _lib.addAbonent(new Student(4, "Vitya"));
        _lib.addAbonent(new Student(5, "Petya"));
        _lib.addAbonent(new Student(6, "Vova"));
        _lib.addAbonent(new Student(7, "Ilya"));
        List<Student> res = _lib.getAllStudents();
        assertThat(res, hasItem(new Student(7, "Ilya")));
    }
}