import com.ra.course.janus.faculty.DataSource.ConnectionFactory;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.SQLException;

import static org.junit.Assert.assertNotNull;

public class ConnectionFactoryTest {
    /*    BeforeEach
    public void resetSingleton() throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        Field connFactory = ConnectionFactory.class.getDeclaredField("connFactory");
        connFactory.setAccessible(true);
        connFactory.set(null, null);
    }*/


    @Test
    public void getConnectionTest() throws SQLException, IOException {
        ConnectionFactory singleton = ConnectionFactory.getInstance();
        assertNotNull(singleton.getConnection());
    }

}
