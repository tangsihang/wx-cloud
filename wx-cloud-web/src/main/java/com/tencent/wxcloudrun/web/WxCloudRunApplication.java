package com.tencent.wxcloudrun.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


/**
 * @author tangsh
 * @date 2022/11/01
 */
@SpringBootApplication(scanBasePackages = "com.tencent.wxcloudrun")
public class WxCloudRunApplication {

  public static void main(String[] args) {
    SpringApplication.run(WxCloudRunApplication.class, args);
  }
}
