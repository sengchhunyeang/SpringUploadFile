package org.springdemo.demofileupload.Controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springdemo.demofileupload.Model.ApiResponse;
import org.springdemo.demofileupload.Model.FileResponse;
import org.springdemo.demofileupload.Service.FileService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("files")
@AllArgsConstructor

public class FileController {
    private final FileService fileService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Insert Image multiple")
    public ResponseEntity<?> uploadFile(@RequestPart List<MultipartFile> files) throws IOException {
        String fileName = null;
        List<FileResponse> fileResponses = new ArrayList<>();
        for (MultipartFile file : files) {
            fileName = fileService.saveFile(file);
            String fileUrl = "http://localhost:8080/" + fileName;
            FileResponse fileResponse = new FileResponse(fileName, fileUrl, file.getContentType(), file.getSize());
            fileResponses.add(fileResponse);
        }


        ApiResponse<List<FileResponse>> response = ApiResponse.<List<FileResponse>>builder().message("successfully uploaded file").status(HttpStatus.OK).code(200).payload(fileResponses).build();
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<?> getFile(@RequestParam String fileName) throws IOException {
        Resource resource = fileService.getFileByIdName(fileName);
        MediaType mediaType;
        if (fileName.endsWith(".pdf")) {
            mediaType = MediaType.APPLICATION_PDF;
        } else if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg") || fileName.endsWith(".png") || fileName.endsWith(".gif")) {
            mediaType = MediaType.IMAGE_PNG;
        } else {
            mediaType = MediaType.APPLICATION_OCTET_STREAM;
        }
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + fileName + "\"").contentType(mediaType).body(resource);
    }

    @PostMapping(value = "onlyPost", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Insert Image Only one ")
    public ResponseEntity<?> uploadFileOnly(@RequestPart MultipartFile file) throws IOException {
        String fileName = fileService.saveFile(file);
        String fileUrl = "http://localhost:8080/" + fileName;

        FileResponse fileResponse = new FileResponse(fileName, fileUrl, file.getContentType(), file.getSize());
        ApiResponse<FileResponse> response = ApiResponse.<FileResponse>builder().message("Successfully upload file").status(HttpStatus.OK).code(200).payload(fileResponse).build();

        return ResponseEntity.ok(response);
    }
}
