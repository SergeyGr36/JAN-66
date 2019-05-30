package com.ra.janus.developersteam.utils;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.io.IOException;
import java.util.Properties;

public class PropertyReaderUtilsMockTest {

    @BeforeEach
    public void before() {

    }

    @Test
    public void whenPropertyReaderUtilsReturnsProperties() throws IOException {

        Properties properties = PropertyReaderUtils.getProperties();

        assertNotNull(properties);
    }

    @Test
    public void whenPropertyReaderUtilsThrowsException() {

        final Executable executable = () ->  PropertyReaderUtils.getProperties("suchFileDoesntExist.properties");

        assertThrows(NullPointerException.class, executable);
    }

    @Test
    public void whenPropertyReaderUtilsReturnsPropertyValue() throws IOException {

        Properties properties = PropertyReaderUtils.getProperties();

        assertNotNull(properties.getProperty("db.url"));
    }

    @Test
    public void whenPropertyReaderUtilsReturnsNotExistedProperty() throws IOException {

        Properties properties = PropertyReaderUtils.getProperties();

        assertNull(properties.getProperty("this.property.doesnt.exist"));
    }
}
