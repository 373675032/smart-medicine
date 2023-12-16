package world.xuewei.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 响应异常
 *
 * @author XUEW
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class RespError extends RespResult {

    /**
     * 请求地址（发生异常时返回）
     */
    private String requestUrl;

    /**
     * 异常类（发生异常时返回）
     */
    private String exception;
}



