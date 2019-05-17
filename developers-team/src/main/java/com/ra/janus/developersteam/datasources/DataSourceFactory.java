package com.ra.janus.developersteam.datasources;

import javax.sql.DataSource;
import java.io.IOException;

public class DataSourceFactory {

    public DataSource get(final DataSourceType dataSourceType) throws IOException {

        if (dataSourceType == DataSourceType.H2) {
            return H2DataSource.INSTANCE.get();
        }

        return null;
    }

//    public DataSource get(final DataSourceType dataSourceType) throws IOException {
//
//        DataSource dataSource = null;
//        switch (dataSourceType) {
//            case H2:
//                dataSource = H2DataSource.INSTANCE.get();
//                break;
//        }
//        return dataSource;
//    }
}
