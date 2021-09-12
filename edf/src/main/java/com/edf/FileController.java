package com.edf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Controller
public class FileController {

    @Autowired
    FileService fileService;

    @GetMapping("/")
    public @ResponseBody
    ResponseEntity<String> startPage() {
        return new ResponseEntity<>("Hello!", HttpStatus.OK);
    }

    @GetMapping(path = "/download")
    public ResponseEntity<Resource> download(@RequestParam("filename") String filename, @RequestParam("username") String userName, HttpServletRequest request) throws IOException {
        Resource resource = fileService.download(filename, userName);

        if (resource == null) {
            return ResponseEntity.notFound().build();
        }

        HttpHeaders headers = createDownloadHeaders(resource.getFile().getName());
        return createDownloadResponse(request, resource, headers);
    }

    @DeleteMapping(path = "/delete")
    public ResponseEntity<String> deleteFile(@RequestParam("filename") String filename, @RequestParam("username") String username) throws IOException {
        return ResponseEntity.ok(fileService.deleteFile(filename, username));
    }

    @PostMapping(path = "/upload", consumes = {"multipart/form-data"})
    public ResponseEntity<String> upload(@RequestParam("file") MultipartFile file, @RequestParam("username") String userName) throws IOException {
        return ResponseEntity.ok(fileService.upload(file, userName));
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
