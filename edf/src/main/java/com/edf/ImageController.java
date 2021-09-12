package com.edf;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;

import static com.edf.FileHelper.*;

@Controller
public class ImageController {
    @Value("${datasource.path.map}")
    private String resourceFile;

    @Value("${datasource.path}")
    private String resourcePath;

    private ResourceMap resourceMap;

    @GetMapping("/")
    public @ResponseBody
    ResponseEntity<String> startPage() {
        return new ResponseEntity<>("Hello!", HttpStatus.OK);
    }

    @GetMapping(path = "/download")
    public ResponseEntity<Resource> download(@RequestParam("filename") String filename, @RequestParam("username") String userName, HttpServletRequest request) throws IOException {
        resourceMap = ResourceMap.loadFromFile(resourceFile);
        String path = resourceMap.getFilePath(makeResourceKey(userName, filename));

        if (path == null) {
            return ResponseEntity.notFound().build();
        }

        Resource resource = getResource(path);
        HttpHeaders headers = createDownloadHeaders(resource.getFile().getName());

        return createDownloadResponse(request, resource, headers);
    }

    @PostMapping(path = "/upload", consumes = {"multipart/form-data"})
    public ResponseEntity<String> upload(@RequestParam("file") MultipartFile file, @RequestParam("username") String userName) throws IOException {
        String filename = file.getOriginalFilename();

        resourceMap = ResourceMap.loadFromFile(resourceFile);

        if (resourceMap.getFilePath(makeResourceKey(userName, filename)) != null) {
            return ResponseEntity.ok().build();
        }

        File newFile = saveNewResource(file, userName, resourcePath, filename);

        resourceMap.put(makeResourceKey(userName, filename), newFile.getPath());
        resourceMap.saveToFile(resourceFile);

        return ResponseEntity.ok().build();
    }

    private ResponseEntity<Resource> createDownloadResponse(HttpServletRequest request, Resource resource, HttpHeaders headers) throws IOException {
        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(resource.getFile().length())
                .contentType(MediaType.valueOf(request.getServletContext().getMimeType(resource.getFile().getPath())))
                .body(resource);
    }

    private HttpHeaders createDownloadHeaders(String file) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + file);
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");
        headers.add("Content-Transfer-Encoding", "binary");

        return headers;
    }
}
