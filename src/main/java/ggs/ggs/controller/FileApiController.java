package ggs.ggs.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

@RestController
@RequestMapping("/tui-editor")
public class FileApiController {

    String rootPath = System.getProperty("user.dir")+"/src/main/resources";
    private final String uploadDir = Paths.get(rootPath, "tui-editor", "upload/").toString();
    private static final Logger log = LoggerFactory.getLogger(FileApiController.class);

    @PostMapping("/image-upload")
    public String uploadEditorImage(@RequestParam("image") MultipartFile image) {
        if (image.isEmpty()) {
            return "";
        }

        String orgFilename = image.getOriginalFilename();
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        String extension = orgFilename.substring(orgFilename.lastIndexOf(".") + 1);
        String saveFilename = uuid + "." + extension;
        String fileFullPath = Paths.get(uploadDir, saveFilename).toString();

        File dir = new File(uploadDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        try {
            File uploadFile = new File(fileFullPath);
            image.transferTo(uploadFile);
            return saveFilename;
        } catch (IOException e) {
            log.error("이미지 업로드 실패: {}", e.getMessage());
            throw new RuntimeException("이미지 업로드에 실패했습니다.");
        }
    }

    @GetMapping(value = "/image-print", produces = { MediaType.IMAGE_GIF_VALUE, MediaType.IMAGE_JPEG_VALUE,
            MediaType.IMAGE_PNG_VALUE })
    public byte[] printEditorImage(@RequestParam("filename") String filename) {
        String fileFullPath = Paths.get(uploadDir, filename).toString();

        File uploadedFile = new File(fileFullPath);
        if (!uploadedFile.exists()) {
            throw new RuntimeException("해당 파일이 존재하지 않습니다.");
        }

        try {
            byte[] imageBytes = Files.readAllBytes(uploadedFile.toPath());
            return imageBytes;
        } catch (IOException e) {
            log.error("이미지 로드 실패: {}", e.getMessage());
            throw new RuntimeException("이미지 로드에 실패했습니다.");
        }
    }
}