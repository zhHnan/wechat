package com.hnz.controller;

import com.alibaba.cloud.commons.lang.StringUtils;
import com.hnz.api.feign.UserInfoServiceFeign;
import com.hnz.config.MinIOConfig;
import com.hnz.config.MinIOUtils;
import com.hnz.exceptions.GraceException;
import com.hnz.result.R;
import com.hnz.result.ResponseStatusEnum;
import com.hnz.utils.JcodecVideoUtil;
import com.hnz.utils.JsonUtils;
import com.hnz.utils.QrCodeUtils;
import com.hnz.vo.UserVO;
import com.hnz.vo.VideoMsgVO;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @Author：hnz
 * @Project：wechat
 * @name：HelloController
 * @Date：2025/7/2 19:29
 * @Filename：HelloController
 */

@RestController
@RequestMapping("file")
public class FileController {
    @Resource
    private MinIOConfig minIOConfig;
    @Resource
    private UserInfoServiceFeign userInfoServiceFeign;

    @PostMapping("uploadFace")
    public R uploadFace(@RequestParam("file") MultipartFile file, @RequestParam("userId") String userId, HttpServletRequest request) throws Exception {
        if (StringUtils.isEmpty(userId)) {
            return R.errorCustom(ResponseStatusEnum.FILE_UPLOAD_FAILD);
        }
        String filename = file.getOriginalFilename();
        if (StringUtils.isEmpty(filename)) {
            return R.errorCustom(ResponseStatusEnum.FILE_UPLOAD_FAILD);
        }
        filename = "face" + File.separator + userId + File.separator + filename;
        String imageUrl = MinIOUtils.uploadFile(minIOConfig.getBucketName(), filename, file.getInputStream(), true);
//        更新到数据库
        R res = userInfoServiceFeign.updateFace(userId, imageUrl);
        String s = JsonUtils.objectToJson(res.getData());
        UserVO userVO = JsonUtils.jsonToPojo(s, UserVO.class);
        return R.ok(userVO);
    }

//    二维码生成
    @PostMapping("generatorQrCode")
    public String generatorQrCode(String wechatNumber, String userId) throws Exception {
        Map<String, String> map = new HashMap<>();
        map.put("wechatNumber", wechatNumber);
        map.put("userId", userId);
        String qrCodePath = QrCodeUtils.generateQRCode(JsonUtils.objectToJson(map));
        if (StringUtils.isNotEmpty(qrCodePath)){
            String uuid = UUID.randomUUID().toString();
            String objectName = "wechatNumber" + File.separator + userId + File.separator + uuid + ".png";
            return MinIOUtils.uploadFile(minIOConfig.getBucketName(), objectName, qrCodePath, true);
        }
        return null;
    }

    @PostMapping("uploadFriendCircleBg")
    public R uploadFriendCircleBg(@RequestParam("file") MultipartFile file, @RequestParam("userId") String userId) throws Exception {
        if (StringUtils.isEmpty(userId)) {
            return R.errorCustom(ResponseStatusEnum.FILE_UPLOAD_FAILD);
        }
        String filename = file.getOriginalFilename();
        if (StringUtils.isEmpty(filename)) {
            return R.errorCustom(ResponseStatusEnum.FILE_UPLOAD_FAILD);
        }
        filename = "friendCircleBg" + File.separator + userId + File.separator + dealWithoutFilename(filename);
        String imageUrl = MinIOUtils.uploadFile(minIOConfig.getBucketName(), filename, file.getInputStream(), true);
//        更新到数据库
        R res = userInfoServiceFeign.updateFriendCircleBg(userId, imageUrl);
        String s = JsonUtils.objectToJson(res.getData());
        UserVO userVO = JsonUtils.jsonToPojo(s, UserVO.class);
        return R.ok(userVO);
    }
    @PostMapping("uploadChatBg")
    public R uploadChatBg(@RequestParam("file") MultipartFile file, @RequestParam("userId") String userId) throws Exception {
        if (StringUtils.isEmpty(userId)) {
            return R.errorCustom(ResponseStatusEnum.FILE_UPLOAD_FAILD);
        }
        String filename = file.getOriginalFilename();
        if (StringUtils.isEmpty(filename)) {
            return R.errorCustom(ResponseStatusEnum.FILE_UPLOAD_FAILD);
        }
        filename = "chatBg" + File.separator + userId + File.separator + dealWithoutFilename(filename);
        String imageUrl = MinIOUtils.uploadFile(minIOConfig.getBucketName(), filename, file.getInputStream(), true);
//        更新到数据库
        R res = userInfoServiceFeign.updateFriendCircleBg(userId, imageUrl);
        String s = JsonUtils.objectToJson(res.getData());
        UserVO userVO = JsonUtils.jsonToPojo(s, UserVO.class);
        return R.ok(userVO);
    }
    @PostMapping("uploadChatPhoto")
    public R uploadChatPhoto(@RequestParam("file") MultipartFile file, String userId) throws Exception {

        if (StringUtils.isBlank(userId)) {
            return R.errorCustom(ResponseStatusEnum.FILE_UPLOAD_FAILD);
        }
        String filename = file.getOriginalFilename();   // 获得文件原始名称
        if (StringUtils.isBlank(filename)) {
            return R.errorCustom(ResponseStatusEnum.FILE_UPLOAD_FAILD);
        }
        filename = "chat" + File.separator + userId + File.separator + "photo" + File.separator + dealWithoutFilename(filename);
        String imageUrl = MinIOUtils.uploadFile(minIOConfig.getBucketName(), filename, file.getInputStream(), true);
        return R.ok(imageUrl);
    }
    @PostMapping("uploadChatVideo")
    public R uploadChatVideo(@RequestParam("file") MultipartFile file, String userId) throws Exception {
        if (StringUtils.isBlank(userId)) {
            return R.errorCustom(ResponseStatusEnum.FILE_UPLOAD_FAILD);
        }
        String filename = file.getOriginalFilename();   // 获得文件原始名称
        if (StringUtils.isBlank(filename)) {
            return R.errorCustom(ResponseStatusEnum.FILE_UPLOAD_FAILD);
        }
        filename = "chat" + File.separator + userId + File.separator + "video" + File.separator + dealWithoutFilename(filename);
        String videoUrl = MinIOUtils.uploadFile(minIOConfig.getBucketName(), filename, file.getInputStream(), true);
        // 帧，封面获取 = 视频截帧 截取第一帧
        String coverName = UUID.randomUUID() + ".jpg";   // 视频封面的名称
        String coverPath = JcodecVideoUtil.videoFramesPath + File.separator + "videos" + File.separator + coverName;
        File coverFile = new File(coverPath);
        if (!coverFile.getParentFile().exists()) {
            coverFile.getParentFile().mkdirs();
        }
        JcodecVideoUtil.fetchFrame(file, coverFile);

        // 上传封面到minio
        String coverUrl = MinIOUtils.uploadFile(minIOConfig.getBucketName(), coverName, new FileInputStream(coverFile), true);

        VideoMsgVO videoMsgVO = new VideoMsgVO();
        videoMsgVO.setVideoPath(videoUrl);
        videoMsgVO.setCover(coverUrl);

        return R.ok(videoMsgVO);
    }

    @PostMapping("uploadChatVoice")
    public R uploadChatVoice(@RequestParam("file") MultipartFile file, String userId) throws Exception {
        String voiceUrl = uploadForChatFiles(file, userId);
        return R.ok(voiceUrl);
    }

    private String uploadForChatFiles(MultipartFile file, String userId) throws Exception {
        if (StringUtils.isBlank(userId)) {
            GraceException.display(ResponseStatusEnum.FILE_UPLOAD_FAILD);
        }

        String filename = file.getOriginalFilename();   // 获得文件原始名称
        if (StringUtils.isBlank(filename)) {
            GraceException.display(ResponseStatusEnum.FILE_UPLOAD_FAILD);
        }
        filename = "chat" + File.separator + userId + File.separator + "voice" + File.separator + dealWithoutFilename(filename);
        return MinIOUtils.uploadFile(minIOConfig.getBucketName(), filename, file.getInputStream(), true);
    }
    private String dealWithFilename(String filename){
        String suffixName = filename.substring(filename.lastIndexOf("."));
        String fName = filename.substring(0, filename.lastIndexOf("."));
        String uuid = UUID.randomUUID().toString();
        return fName + "-" + uuid + suffixName;
    }
    private String dealWithoutFilename(String filename){
        String suffixName = filename.substring(filename.lastIndexOf("."));
        String uuid = UUID.randomUUID().toString();
        return uuid + suffixName;
    }
}
