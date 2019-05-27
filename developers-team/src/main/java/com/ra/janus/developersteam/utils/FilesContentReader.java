package com.ra.janus.developersteam.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.function.BiPredicate;


public enum FilesContentReader {

    INSTANCE;

    private static final String EXCEPTION_WARN = "An exception occurred!";
    private static final Logger LOGGER = LoggerFactory.getLogger(FilesContentReader.class);

    private Map<String, List<FileContent>> contentMap;

    public List<String> getContent(final String directoryName, final String... fileNames) {
        if (contentMap == null) {
            contentMap = new Hashtable<>();
        }

        if (!contentMap.containsKey(directoryName)) {
            contentMap.put(directoryName, new ArrayList<>());
            final Path path = getDirectoryPath(directoryName);
            cacheDirectory(path);
        }

        return findContents(directoryName, fileNames);
    }

    private List<String> findContents(final String directoryName, final String[] fileNames) {
        final List<String> result;
        if (fileNames.length == 0) {
            final List<FileContent> filesContents = contentMap.get(directoryName);
            result = new ArrayList<>();
            for (final FileContent fileContent : filesContents) {
                result.add(fileContent.content);
            }
        } else {
            result = new ArrayList<>();
            for (final String fileName : fileNames) {
                final int wasSize = result.size();
                final List<FileContent> filesContents = contentMap.get(directoryName);
                for (final FileContent fileContent : filesContents) {
                    if (fileName.equals(fileContent.getFileName())) {
                        result.add(fileContent.getContent());
                        break;
                    }

                }
                if (wasSize == result.size()) {
                    throw new IllegalStateException("The file " + fileName + " was not found in " + directoryName);
                }
            }
        }

        return result;
    }

    private Path getDirectoryPath(final String dirName) {
        //final URL url = getClass().getClassLoader().getResource(dirName);
        final URL url = Thread.currentThread().getContextClassLoader().getResource(dirName);
        if (url == null) {
            final IllegalStateException e = new IllegalStateException("The directory does not exist: " + dirName);
            LOGGER.error(EXCEPTION_WARN, e);
            throw e;
        }

        final Path path = Paths.get(URI.create(url.toString()));
        validatePath(path);
        return path;
    }

    private void validatePath(final Path path) {
        if (!Files.isDirectory(path)) {
            final IllegalStateException e = new IllegalStateException("The path is not a directory: " + path);
            LOGGER.error(EXCEPTION_WARN, e);
            throw e;
        }
    }

    private void cacheDirectory(final Path dirPath) {
        try {
            Files.find(dirPath, 1,
                    new BiPredicate<Path, BasicFileAttributes>() {
                        @Override
                        public boolean test(final Path filePath, final BasicFileAttributes fileAttributes) {
                            final File file = filePath.toFile();
                            return !file.isDirectory();
                        }
                    }).forEach(path -> cacheFile(dirPath.toFile().getName(), path));
        } catch (IOException e) {
            LOGGER.error(EXCEPTION_WARN, e);
            throw new UncheckedIOException(e);
        }
    }

    private void cacheFile(final String directoryName, final Path filePath) {
        final List<FileContent> list = contentMap.get(directoryName);
        try {
            final String content = new String(Files.readAllBytes(filePath));
            list.add(new FileContent(filePath.toFile().getName(), content));
        } catch (IOException e) {
            LOGGER.error(EXCEPTION_WARN, e);
            throw new UncheckedIOException(e);
        }
    }

    private class FileContent {
        transient private final String fileName;
        transient private final String content;

        public FileContent(final String fileName, final String content) {
            this.fileName = fileName;
            this.content = content;
        }

        public String getFileName() {
            return fileName;
        }

        public String getContent() {
            return content;
        }
    }
}
