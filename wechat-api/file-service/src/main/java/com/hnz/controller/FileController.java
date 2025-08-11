package com.hnz.controller;

import com.hnz.result.R;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

/**
 * @Author：hnz
 * @Project：wechat
 * @name：HelloController
 * @Date：2025/7/2 19:29
 * @Filename：HelloController
 */

@RestController
@RequestMapping("/file")
public class FileController {
    @PostMapping("/uploadFace")
    public R uploadFace(@RequestParam("file") MultipartFile file, @RequestParam("userId")String userId, HttpServletRequest request) throws IOException {
        String filename = file.getOriginalFilename();
        String suffixName = null;
        if (filename != null) {
            suffixName = filename.substring(filename.lastIndexOf("."));
        }
        String newFileName = userId + suffixName;
//        文件的存放路径
        String rootPath = "/temp" + File.separator;
        String filePath = rootPath + File.separator + "face" + File.separator + newFileName;
        File dest = new File(filePath);
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdirs();
        }
        file.transferTo(dest);
        return R.ok();
    }
}
