package ggs.ggs.goods;

import ggs.ggs.domain.GoodsFile;
import ggs.ggs.domain.GoodsLike;
import ggs.ggs.dto.FileDto;
import ggs.ggs.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class GoodsFileServiceImpl implements FileService {

    @Autowired
    private GoodsFileRepository fileRepository;

//    파일저장
    @Override
    public FileDto insert(MultipartFile multipartFile) throws IOException {
        String rootPath = System.getProperty("user.dir")+"/src/main/resources/";
        String ofile = multipartFile.getOriginalFilename();
        String sfile = System.currentTimeMillis() + "_" + ofile;
        String path = rootPath+"/static/img/goods/" + sfile;
            multipartFile.transferTo(new File(path));
            path = "/img/goods/" + sfile;
        FileDto fileDto = new FileDto(ofile, sfile, path);

        return fileDto;
    }

    @Override
    public void delete(String sfile) {
        String rootPath = System.getProperty("user.dir")+"/src/main/resources/";
        String path = rootPath+"/static/img/goods/";
        File file= new File(path+sfile);
        file.delete();
    }

}
