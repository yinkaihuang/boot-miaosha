package cn.bucheng.miaosha;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@MapperScan(basePackages = "cn.bucheng.miaosha.mapper")
@Slf4j
public class BootMiaoshaApplication {

    public static void main(String[] args) {
        SpringApplication.run(BootMiaoshaApplication.class, args);
        log.info(" miaosha server start success");
    }

}
