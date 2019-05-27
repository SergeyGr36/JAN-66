package com.ra.janus.developersteam.utils;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.io.IOException;
import java.util.Properties;

public class PropertyReaderTest {

    @BeforeEach
    public void before() {

    }

    @Test
    public void whenPropertyReaderReturnsProperties() throws IOException {

        Properties properties = PropertyReader.INSTANCE.getProperties();

        assertNotNull(properties);
    }

    @Test
    public void whenPropertyReaderThrowsException() {

        final Executable executable = () ->  PropertyReader.INSTANCE.getProperties("suchFileDoesntExist.properties");

        assertThrows(NullPointerException.class, executable);
    }

    @Test
    public void whenPropertyReaderReturnsPropertyValue() throws IOException {

        Properties properties = PropertyReader.INSTANCE.getProperties();

        assertNotNull(properties.getProperty("db.url"));
    }

    @Test
    public void whenPropertyReaderReturnsNotExistedProperty() throws IOException {

        Properties properties = PropertyReader.INSTANCE.getProperties();

        assertNull(properties.getProperty("this.property.doesnt.exist"));
    }
}
