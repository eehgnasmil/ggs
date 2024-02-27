package ggs.ggs.member;


import ggs.ggs.domain.MemberFile;
import ggs.ggs.dto.FileDto;
import ggs.ggs.service.FileService;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class MemberFileServiceImpl implements FileService {


    //    파일저장
    public FileDto insert(MultipartFile multipartFile) throws IOException {
        String rootPath = System.getProperty("user.dir") + "/src/main/resources/";
        String ofile = multipartFile.getOriginalFilename();
        String sfile = System.currentTimeMillis() + "_" + ofile;
        String path = rootPath + "/static/img/member/" + sfile;
//                String path = "D:/kdigital2307/boot/bootws/ggs/src/main/resources/static/img/goods/" + sfile;
        multipartFile.transferTo(new File(path));
        path = "/img/member/" + sfile;

        FileDto fileDto = new FileDto(ofile, sfile, path);

        return fileDto;
    }

    @Override
    public void delete(String sfile) {
        String rootPath = System.getProperty("user.dir")+"/src/main/resources/";
        String path = rootPath+"/static/img/member/";
        File file= new File(path+sfile);
        file.delete();
    }
}

