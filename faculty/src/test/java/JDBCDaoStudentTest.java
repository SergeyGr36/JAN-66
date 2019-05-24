import Dao.DaoStudent;
import Entity.Student;
import org.assertj.core.api.AbstractBigDecimalAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.sql.Statement;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class JDBCDaoStudentTest  {
    private static final String SELECT_ONE_SQL = "SELECT * FROM STUDENT WHERE STUDENT_ID = ?";
    private JDBCDaoStudentTest studentDao;
    private DataSource mockDataSource;
    private Connection mockConnection;

    public JDBCDaoStudentTest(DataSource mockDataSource) {

    }

    @BeforeEach
    public void before() throws Exception {
        mockDataSource = mock(DataSource.class);
        studentDao = new JDBCDaoStudentTest(mockDataSource);
        mockConnection = mock(Connection.class);
        when(mockDataSource.getConnection()).thenReturn(mockConnection);
    }


    @Test
    public void whenCallInsertThenSaveItInDB() {

    }

    @Test
    public void whenCallFindByStudentIdThenReturnItFromDb() throws Exception {
        var prepStatement = mock(PreparedStatement.class);
        when(mockConnection.prepareStatement(SELECT_ONE_SQL)).thenReturn(prepStatement);
        var resSet = mock(ResultSet.class);
        when(prepStatement.executeQuery()).thenReturn(resSet);
        when(resSet.next()).thenReturn(true);

       String testNumber = "1";
        when(resSet.getString("STUDENT_ID")).thenReturn(testNumber);
        var student = DaoStudent.findByStudentId(testNumber);

       /* assertThat(student.getId()).isEqualTo(testNumber);*/
        assertThrows(RuntimeException.class, () -> studentDao.findByStudentId(testNumber));

    }



    private void findByStudentId(String testNumber) {
    }


    @Test
    void insert() {
    }

    @Test
    void insert1() {
    }

    @Test
    void update() {
    }

    @Test
    void delete() {
    }

    @Test
    void findByStudentId1() {
    }

    @Test
    void findAll() {
    }

    @Test
    void getName() {
    }

    @Test
    void countAll() {
    }

    @Test
    void findByStudentId2() {
    }
}
