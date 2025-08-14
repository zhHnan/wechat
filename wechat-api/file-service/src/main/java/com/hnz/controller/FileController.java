package com.hnz.controller;

import com.alibaba.cloud.commons.lang.StringUtils;
import com.hnz.config.MinIOConfig;
import com.hnz.config.MinIOUtils;
import com.hnz.result.R;
import com.hnz.result.ResponseStatusEnum;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

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
    @Resource
    private MinIOConfig minIOConfig;

    @PostMapping("/uploadFace")
    public R uploadFace(@RequestParam("file") MultipartFile file, @RequestParam("userId") String userId, HttpServletRequest request) throws Exception {
        if (StringUtils.isEmpty(userId)) {
            return R.errorCustom(ResponseStatusEnum.FILE_UPLOAD_FAILD);
        }
        String filename = file.getOriginalFilename();
        if (StringUtils.isEmpty(filename)) {
            return R.errorCustom(ResponseStatusEnum.FILE_UPLOAD_FAILD);
        }
        filename = "face" + File.separator + userId + File.separator + filename;
        MinIOUtils.uploadFile(minIOConfig.getBucketName(), filename, file.getInputStream());
//        String faceUrl = MinIOUtils.getPresignedObjectUrl(minIOConfig.getBucketName(), filename);
        String faceUrl = minIOConfig.getFileHost() + "/" + minIOConfig.getBucketName() + "/" + filename;
//        更新到数据库
        return R.ok(faceUrl);
    }
}
