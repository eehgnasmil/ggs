package ggs.ggs.service;

import ggs.ggs.domain.GoodsFile;
import ggs.ggs.dto.FileDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface FileService {

    FileDto insert(MultipartFile multipartFile) throws IOException;
    void delete(String sfile);

}
