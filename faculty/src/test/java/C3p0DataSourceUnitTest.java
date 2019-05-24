import DataSource.C3p0DataSource;
import org.junit.jupiter.api.Test;
import static org.junit.Assert.*;
import java.sql.SQLException;

public class C3p0DataSourceUnitTest {
    @Test
    public void givenC3poDataSourceClass_whenCalledgetConnection_thenCorrect() throws SQLException {
        assertTrue(C3p0DataSource.getConnection().isValid(1));
    }
}
