package world.xuewei.component;

import cn.hutool.core.util.IdUtil;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.CannedAccessControlList;
import com.aliyun.oss.model.CreateBucketRequest;
import com.aliyun.oss.model.PutObjectRequest;
import com.aliyun.oss.model.PutObjectResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * 对象存储工具
 *
 * @author XUEW
 */
@Component
public class OssClient {

    @Value("${oss.bucket-name}")
    private String bucketName;

    @Value("${oss.end-point}")
    private String endPoint;

    @Value("${oss.access-key}")
    private String accessKeyId;

    @Value("${oss.access-secret}")
    private String accessKeySecret;

    /**
     * 上传文件
     */
    public String upload(MultipartFile file, String path) throws IOException {
        if (file == null || path == null) {
            return null;
        }
        OSSClient ossClient = new OSSClient(endPoint, accessKeyId, accessKeySecret);
        if (!ossClient.doesBucketExist(bucketName)) {
            ossClient.createBucket(bucketName);
            CreateBucketRequest createBucketRequest = new CreateBucketRequest(bucketName);
            createBucketRequest.setCannedACL(CannedAccessControlList.PublicRead);
            ossClient.createBucket(createBucketRequest);
        }
        String extension = OssClient.getFileExtension(file);
        //设置文件路径
        String fileUrl = path + "/" + IdUtil.simpleUUID() + extension;
        String url = "https://" + bucketName + "." + endPoint + "/" + fileUrl;
        PutObjectResult result = ossClient.putObject(new PutObjectRequest(bucketName, fileUrl, file.getInputStream()));
        //上传文件
        ossClient.setBucketAcl(bucketName, CannedAccessControlList.PublicRead);
        return url;
    }

    /**
     * 获取文件的扩展名
     */
    public static String getFileExtension(MultipartFile file) {
        String filename = file.getOriginalFilename();
        assert filename != null;
        return filename.substring(filename.lastIndexOf("."));
    }
}
