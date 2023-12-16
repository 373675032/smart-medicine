package world.xuewei.dto;

import com.alibaba.fastjson.JSONObject;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import world.xuewei.utils.Assert;

import java.util.List;

/**
 * 响应结果
 *
 * @author XUEW
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RespResult {

    /**
     * 响应编码
     */
    protected String code;

    /**
     * 响应信息
     */
    protected String message;

    /**
     * 响应数据
     */
    protected Object data;

    /**
     * 请求成功
     */
    public static RespResult success() {
        return RespResult.builder()
                .code("SUCCESS")
                .message("请求成功")
                .build();
    }

    /**
     * 请求成功
     */
    public static RespResult success(String message) {
        return RespResult.builder()
                .code("SUCCESS")
                .message(message)
                .build();
    }

    /**
     * 请求成功
     */
    public static RespResult success(String message, Object data) {
        return RespResult.builder()
                .code("SUCCESS")
                .message(message)
                .data(data)
                .build();
    }

    /**
     * 请求失败
     */
    public static RespResult fail() {
        return RespResult.builder()
                .code("FAIL")
                .message("请求失败")
                .build();
    }


    /**
     * 请求失败
     */
    public static RespResult fail(String message) {
        return RespResult.builder()
                .code("FAIL")
                .message(message)
                .build();
    }

    /**
     * 未查询到数据
     */
    public static RespResult notFound(String message, Object data) {
        return RespResult.builder()
                .code("NOT_FOUND")
                .message(message)
                .data(data)
                .build();
    }

    /**
     * 未查询到数据
     */
    public static RespResult notFound() {
        return RespResult.builder()
                .code("NOT_FOUND")
                .message("请求失败")
                .build();
    }


    /**
     * 未查询到数据
     */
    public static RespResult notFound(String message) {
        return RespResult.builder()
                .code("NOT_FOUND")
                .message(message)
                .build();
    }

    /**
     * 请求失败
     */
    public static RespResult fail(String message, Object data) {
        return RespResult.builder()
                .code("FAIL")
                .message(message)
                .data(data)
                .build();
    }

    /**
     * 请求是否成功
     */
    public boolean isSuccess() {
        return "SUCCESS".equals(code);
    }

    /**
     * 请求是否成功并有数据返回
     */
    public boolean isSuccessWithDateResp() {
        return "SUCCESS".equals(code) && Assert.notEmpty(data);
    }

    /**
     * 请求是否成功
     */
    public boolean notSuccess() {
        return !isSuccess();
    }

    /**
     * 获取响应的数据集合
     */
    public <T> List<T> getDataList(Class<T> clazz) {
        String jsonString = JSONObject.toJSONString(data);
        return JSONObject.parseArray(jsonString, clazz);
    }

    /**
     * 获取响应的数据
     */
    public <T> T getDataObj(Class<T> clazz) {
        String jsonString = JSONObject.toJSONString(data);
        return JSONObject.parseObject(jsonString, clazz);
    }

}
