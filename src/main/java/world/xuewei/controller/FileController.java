package world.xuewei.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import world.xuewei.dto.RespResult;
import world.xuewei.entity.User;
import world.xuewei.utils.Assert;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * 文件控制器
 *
 * @author XUEW
 */
@RestController
@RequestMapping("/file")
public class FileController extends BaseController<User> {

    /**
     * 上传文件
     */
    @PostMapping("/upload")
    public RespResult upload(@RequestParam("file") MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            return RespResult.fail("文件为空");
        }

        try {
            // 获取原始文件名和后缀
            String fileName = file.getOriginalFilename();
            String suffix = fileName.substring(fileName.lastIndexOf("."));
            
            // 创建临时文件
            File tempFile = File.createTempFile(UUID.randomUUID().toString(), suffix);
            // 保存到临时文件
            file.transferTo(tempFile);
            
            // 确保目标目录存在
            File uploadDir = new File("src/main/resources/static/upload/");
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }
            
            // 移动到静态资源目录
            String newFileName = tempFile.getName();
            String filePath = "/upload/" + newFileName;
            FileUtils.copyFile(tempFile, new File("src/main/resources/static" + filePath));
            
            // 删除临时文件
            tempFile.delete();
            
            return RespResult.success("上传成功", filePath);
        } catch (IOException e) {
            e.printStackTrace();
            return RespResult.fail("上传失败：" + e.getMessage());
        }
    }

    private void deleteFile(File file) {
        if (file != null && file.exists()) {
            file.delete();
        }
    }
}
