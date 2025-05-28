package com.sc2.cmtrip.common;

//import com.sc2.cmtrip.config.SecurityConfig;
import com.sc2.cmtrip.config.Sc2Config;
import com.sc2.cmtrip.entity.CtTrip;
import com.sc2.cmtrip.service.CtTripService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Component
public class CtUtils {

    @Autowired
    private Sc2Config sc2Config;

    String virtualPathStart = "/profile";

    /**
     * Convert the plaintext password into a hashed ciphertext
     * 将密码从明文转为哈希过后的密文
     * @param password
     * @param salt
     * @return
     */
    public String getHashedPassword(String password, String salt) {
        String newPwd = password + salt +password + salt;
        return DigestUtils.sha256Hex(newPwd.getBytes());
    }

    /**
     * Convert virtual path to physical path
     * 虚拟路径转真实路径
     * @param virtualPath
     * @return
     */
    public String getPhysicalPath(String virtualPath) {
        if (virtualPath.startsWith(virtualPathStart)) {
            String relativePath = virtualPath.substring(virtualPathStart.length());
            return sc2Config.getProfile() + relativePath;
        }
        return null;
    }

    /**
     * Convert physical path to virtual path
     * 校验上传的文件文件是否为图片类型
     * @param absolutePath
     * @return
     */
    public String getVirtualPath(String absolutePath) {
        if (absolutePath.startsWith(sc2Config.getProfile())) {
            String relativePath = absolutePath.substring(sc2Config.getProfile().length());
            return virtualPathStart + relativePath;
        }
        return null;
    }

    /**
     * Upload an image
     * 获取图片文件全名
     * @param file
     * @return
     */
    public String uploadImage(MultipartFile file) {
        // If the file is empty 如果文件为空
        if (file.isEmpty()) {
            throw new RuntimeException("Image is empty!");
        }
        // Verify whether the uploaded file is of an image type 校验上传的文件文件是否为图片类型
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new RuntimeException("File is not an image!");
        }
        // Get the full name of the image file 获取图片文件全名
        String originalFilename = file.getOriginalFilename();
        // Get the file extension 获取文件扩展名
        String ext = originalFilename.substring(originalFilename.lastIndexOf("."));
        // Generate the full file name in the format of UUID with file extension 生成UUID+扩展名形式的文件全名
        String filename = UUID.randomUUID() + ext;
        // Use the profile path defined in `application.yml` (the top-level directory for saving images) 使用 application.yml 中定义的 profile 路径（保存图片的顶级目录）
        String uploadPath = sc2Config.getProfile();
        File dir = new File(uploadPath);
        if (!dir.exists()) dir.mkdirs();
        // Prepare to upload 准备上传
        File dest = new File(dir, filename);
        try {
            file.transferTo(dest);
        } catch (IOException e) {
            throw new RuntimeException("Image uploaded failed! Message: " + e.getMessage());
        }
        String url = "/profile/" + filename;
        // Return the complete virtual path of the file 返回文件的虚拟完整路径
        return url;
    }

    /**
     * Download a file
     * 下载文件
     * @param virtualPath
     * @return
     */
    public Resource getFileResource(String virtualPath) {
        String absolutePath = getPhysicalPath(virtualPath);
        if (absolutePath == null) {
            throw new RuntimeException("Invalid virtual path: " + virtualPath);
        }
        Path filePath = Paths.get(absolutePath);
        try {
            if (!Files.exists(filePath)) {
                throw new IOException("File not found: " + absolutePath);
            }
        } catch (Exception e) {
            throw new RuntimeException("Paths can not be used!" + e.getMessage());
        }
        try{
            return new UrlResource(filePath.toUri());
        } catch (Exception e) {
            throw new RuntimeException("UrlResource can not be gotten!" + e.getMessage());
        }
    }

    /**
     * Delete a file
     * 删除文件
     * @param virtualPath
     * @return
     */
    public boolean deleteFile(String virtualPath) {
        String absolutePath = getPhysicalPath(virtualPath);
        if (absolutePath == null) {
            throw new RuntimeException("Invalid virtual Path: " + virtualPath);
        }
        Path filePath = Paths.get(absolutePath);
        // File corresponding to the physical path not found 未找到物理地址对应的文件
        if (!Files.exists(filePath)) {
            return false;
        }
        // Delete the file using `Files.delete` from NIO 使用NIO的Files.delete删除文件
        try {
            Files.delete(filePath);
        } catch (Exception e) {
            throw new RuntimeException("Can not find the file to delete!" + e.getMessage());
        }
        return true;
    }

    /**
     * Delete files in batch; continue deleting other files even if some deletions fail
     * 批量删除文件，即使部分失败也会继续删除其他文件
     * @param virtualPaths List of virtual paths to be deleted 要删除的虚拟路径列表
     * @throws RuntimeException If at least one file deletion fails, throw an exception including the failure details 如果至少有一个文件删除失败，抛出异常并包含失败详情
     */
    public void deleteFiles(List<String> virtualPaths) {
        // Return immediately if the input list is empty 如果传入空列表，直接返回
        if (virtualPaths == null || virtualPaths.isEmpty()) {
            return;
        }
        // Virtual path corresponding to the file that failed to delete 删除失败文件对应的虚拟地址
        List<String> failedDeletions = new ArrayList<>();
        for (String virtualPath : virtualPaths) {
            try {
                String absolutePath = getPhysicalPath(virtualPath);
                if (absolutePath == null) {
                    failedDeletions.add(virtualPath + ": Invalid virtual path");
                    continue;
                }
                Path filePath = Paths.get(absolutePath);
                if (!Files.exists(filePath)) {
                    failedDeletions.add(virtualPath + ": File not found");
                    continue;
                }
                // Try to delete 尝试删除
                Files.delete(filePath);
            } catch (Exception e) {
                failedDeletions.add(virtualPath + ": " + e.getMessage());
            }
        }
        if (!failedDeletions.isEmpty()) {
            // 以下文件删除失败 The following files failed to delete
            throw new RuntimeException("The following files failed to delete:\n " + String.join(", ", failedDeletions));
        }
    }

}
