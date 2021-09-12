package com.edf;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;

public class FileHelper {

    private FileHelper() {
    }

    public static String makeResourceKey(String userName, String filename) {
        return filename.hashCode() + "" + userName.hashCode();
    }

    public static File saveNewResource(MultipartFile file, String userName, String resourcePath, String filename) throws IOException {
        File newFile = createFileForNewResource(userName, resourcePath, filename);
        file.transferTo(newFile);
        return newFile;
    }

    private static File createFileForNewResource(String userName, String resourcePath, String filename) throws IOException {
        File newFile = new File(resourcePath + userName + "\\" + filename);
        newFile.getParentFile().mkdirs();
        newFile.createNewFile();
        return newFile;
    }

    public static Resource getResource(String file) throws MalformedURLException {
        Path filePath = Path.of(file).normalize();
        return new UrlResource(filePath.toUri());
    }
}
