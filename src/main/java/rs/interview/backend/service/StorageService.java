package rs.interview.backend.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;

public interface StorageService {

    File saveFile(MultipartFile file);

    void deleteFile(String fileName);

}
