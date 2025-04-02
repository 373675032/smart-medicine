package world.xuewei.controller;

import org.apache.commons.io.FileUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import world.xuewei.dto.RespResult;
import world.xuewei.entity.MedicalNews;
import world.xuewei.service.MedicalNewsService;
import world.xuewei.utils.Assert;

import javax.annotation.Resource;
import java.io.File;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

/**
 * 咨询控制器
 *
 * @author XUEW
 */
@RestController
@RequestMapping("medical_news")
public class MedicalNewsController extends BaseController<MedicalNews> {

    @Resource
    private MedicalNewsService medicalNewsService;

    @Override
    @PostMapping("save")
    public RespResult save(@RequestBody MedicalNews news) {
        try {
            if (Assert.isEmpty(news.getId())) {
                news.setCreateTime(new Date());
            }
            news.setUpdateTime(new Date());
            medicalNewsService.save(news);
            return RespResult.success("保存成功");
        } catch (Exception e) {
            e.printStackTrace();
            return RespResult.fail("保存失败");
        }
    }

    @PostMapping("/saveNews")
    public RespResult saveNews(MedicalNews news, @RequestParam(value = "file", required = false) MultipartFile file) {
        try {
            if (Assert.notEmpty(file)) {
                // 获取文件名和后缀
                String fileName = file.getOriginalFilename();
                String prefix = fileName.substring(fileName.lastIndexOf("."));
                // 用uuid作为文件名
                final File excelFile = File.createTempFile(UUID.randomUUID().toString(), prefix);
                // 保存文件
                file.transferTo(excelFile);
                
                // 移动到静态资源目录
                String filePath = "/upload/" + excelFile.getName();
                FileUtils.copyFile(excelFile, new File("src/main/resources/static" + filePath));
                // 删除临时文件
                deleteFile(excelFile);
                
                news.setImgPath(filePath);
            }
            
            if (Assert.isEmpty(news.getId())) {
                news.setCreateTime(new Date());
            }
            news.setUpdateTime(new Date());
            medicalNewsService.save(news);
            return RespResult.success("保存成功",news);
        } catch (Exception e) {
            e.printStackTrace();
            return RespResult.fail("保存失败：" + e.getMessage());
        }
    }

    @PostMapping("delete")
    public RespResult delete(Integer id) {
        try {
            medicalNewsService.delete(id);
            return RespResult.success("删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            return RespResult.fail("删除失败");
        }
    }

    /**
     * 删除临时文件
     */
    private void deleteFile(File... files) {
        for (File file : files) {
            if (file.exists()) {
                file.delete();
            }
        }
    }

    @GetMapping("/medical_news/page")
    @ResponseBody
    public Map<String, Object> page(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        
        return medicalNewsService.page(pageNum, pageSize);
    }
}
