package eu.intelcomp.catalogue.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {

    Resource get(String filepath);

    void save(MultipartFile file, String filepath);

    void delete(String filepath);

}
