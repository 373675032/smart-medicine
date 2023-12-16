package world.xuewei;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 应用启动类
 *
 * @author XUEW
 */
@SpringBootApplication
@MapperScan("world.xuewei.dao")
public class SmartMedicineApplication {

    public static void main(String[] args) {
        SpringApplication.run(SmartMedicineApplication.class, args);
    }

}
