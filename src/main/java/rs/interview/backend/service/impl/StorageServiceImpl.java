package rs.interview.backend.service.impl;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import rs.interview.backend.service.StorageService;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
@Transactional
public class StorageServiceImpl implements StorageService {

    private final Logger log = LoggerFactory.getLogger(StorageServiceImpl.class);

    @Value("${uploaded.file.folder}")
    private String PATH_FOLDER;


    @PostConstruct
    private void init() {
        Path path = Paths.get(PATH_FOLDER);
        if (!Files.exists(path)) {
            try {
                Files.createDirectory(path);
            } catch (IOException e) {
                log.error(String.format("Failed when trying to create %s folder", PATH_FOLDER), e);
                e.printStackTrace();
            }
        }
    }

    @Override
    public File saveFile(MultipartFile file) {

        try (InputStream inputStream = file.getInputStream()) {
            File newFile = new File(String.format("%s%s%s", PATH_FOLDER, File.separator, file.getOriginalFilename()));
            FileUtils.copyInputStreamToFile(inputStream, newFile);
            return newFile;
        } catch (IOException e) {
            log.error(String.format("Saving file to %s folder.", PATH_FOLDER), e);
        }
        throw new RuntimeException("Input file was not successfully saved.");
    }


    @Override
    public void deleteFile(String fileName) {
        try {
            Files.deleteIfExists(Paths.get(PATH_FOLDER + fileName));
            log.debug(String.format("File %s is successfully deleted.", fileName));
        } catch (IOException e) {
            log.error(String.format("An error occurred when tried to delete a file %s", fileName), e);
        }
    }
}
