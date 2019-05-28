package com.ibay.tea.common;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
public class CommonService {

    public String imageUpload(MultipartFile file){
        if (file.isEmpty()){
            return null;
        }
        long currentTime = System.currentTimeMillis();
        String fileName = file.getOriginalFilename();  // 文件名
        String suffixName = fileName.substring(fileName.lastIndexOf("."));  // 后缀名
        String filePath = "D://temp-image//"; // 上传后的路径
        fileName = currentTime+ suffixName; // 新文件名
        File dest = new File(filePath + fileName);
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdirs();
        }
        try {
            file.transferTo(dest);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String filename = CommonConstant.IMAGE_URL_PREFIX +"/temp-image/" + fileName;
        return filename;
    }
}
