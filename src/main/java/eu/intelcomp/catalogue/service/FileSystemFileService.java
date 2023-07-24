package eu.intelcomp.catalogue.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

@Service
public class FileSystemFileService implements FileService {

    private static final Logger logger = LoggerFactory.getLogger(FileSystemFileService.class);

    @Override
    public Resource get(String filepath) {
        Path path = Paths.get(filepath);
        Resource resource = null;
        try {
            resource = new InputStreamResource(new FileInputStream(path.toFile()));
        } catch (FileNotFoundException e) {
            logger.error("Could not find file: {}", filepath);
        }
        return resource;
    }

    @Override
    public void save(MultipartFile file, String filepath) {
        try {
            String dir = filepath.substring(0, filepath.length() - Objects.requireNonNull(file.getOriginalFilename()).length());
            Files.createDirectory(Paths.get(dir));
            File f = new File(filepath);
            file.transferTo(f);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(String filepath) {
        try {
            Files.deleteIfExists(Paths.get(filepath));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
