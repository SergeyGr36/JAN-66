import com.ra.course.janus.faculty.dao.DataSourceUtils;
import com.zaxxer.hikari.HikariDataSource;
import org.junit.jupiter.api.Test;
import javax.sql.DataSource;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class DataSourceUtilTest {
    @Test
    public void whenGetDataSource()  {
        DataSource dataSource = DataSourceUtils.getDataSource();
        assertEquals(true, dataSource instanceof HikariDataSource);
    }
}
