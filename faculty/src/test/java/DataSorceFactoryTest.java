/*
import org.h2.jdbcx.JdbcDataSource;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.Properties;



public class DataSorceFactoryTest {
     if (DataSource == null) {
                   final Properties properties = PropertyReader.INSTANCE.getProperties("config.properties");
                   DataSource = new JdbcDataSource();
                    dataSource.setURL(properties.getProperty("db.url"));
                    dataSource.


                            setUser(properties.getProperty("db.username"));
                   dataSource.setPassword(properties.getProperty("db.password"));
            public DataSource get() {

                            try {
                            if (dataSource == null) {
                                    final Properties properties = PropertyReader.INSTANCE.getProperties("config.properties");                dataSource = new JdbcDataSource();
                                    dataSource.setURL(properties.getProperty("db.url"));
                                    dataSource.setUser(properties.getProperty("db.username"));
                                    dataSource.setPassword(properties.getProperty("db.password"));
                }
                        } catch (IOException ex) {
                          throw new DataSourceException(ex);
            }



}

*/
