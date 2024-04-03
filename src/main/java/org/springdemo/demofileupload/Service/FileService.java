package org.springdemo.demofileupload.Service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


public interface FileService {
    String saveFile(MultipartFile file) throws IOException;

    Resource getFileByIdName(String fileName) throws IOException;

    String uploadFileOnly(MultipartFile file) throws IOException;
}

