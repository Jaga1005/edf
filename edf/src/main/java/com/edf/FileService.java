package com.edf;

import com.edf.db.ResourceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static com.edf.FileHelper.copyResource;
import static com.edf.FileHelper.getResource;

@Service
public class FileService {
    @Value("${storage.path}")
    private String storagePath;

    @Autowired
    ResourceRepository resourceRepository;

    @Transactional(readOnly = true)
    public Resource download(String filename, String userName) throws IOException {
        String path = resourceRepository.getDirectoryByUserNameAndFilename(userName, filename);

        if (path == null) {
            return null;
        }

        return getResource(path);
    }

    @Transactional
    public String upload(MultipartFile file, String userName) throws IOException {
        String filename = file.getOriginalFilename();

        if (resourceRepository.existsByUserNameAndFilename(userName, filename)) {
            return "ALREADY_ADDED";
        }

        String newFilePath = copyToNewFile(file, userName, filename);
        resourceRepository.save(new com.edf.db.orm.Resource(userName, filename, newFilePath));

        return HttpStatus.OK.name();
    }

    String copyToNewFile(MultipartFile file, String userName, String filename) throws IOException {
        return copyResource(file, userName, storagePath, filename);
    }

    @Transactional()
    public String deleteFile(String filename, String username) throws IOException {
        if (resourceRepository.existsByUserNameAndFilename(username, filename)) {
            boolean deleted = deleteResourceFromStorage(filename, username);
            resourceRepository.deleteByUserNameAndFilename(username, filename);
            return deleted ? "OK" : "Something goes wrong";
        }
        return "NOT_EXISTS";
    }

    private boolean deleteResourceFromStorage(String filename, String username) throws IOException {
        String path = resourceRepository.getDirectoryByUserNameAndFilename(username, filename);
        return Files.deleteIfExists(Paths.get(path));
    }
}
