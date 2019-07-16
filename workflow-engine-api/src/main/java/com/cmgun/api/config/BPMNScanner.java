//package com.cmgun.api.config;
//
//import com.cmgun.api.service.ActProgressService;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.context.ApplicationContext;
//import org.springframework.core.io.Resource;
//import org.springframework.stereotype.Component;
//
///**
// * @author chenqilin
// * @Date 2019/7/16
// */
//@Slf4j
//@Component
//public class BPMNScanner implements CommandLineRunner {
//
//    @Autowired
//    private ActProgressService actProgressService;
//    @Autowired
//    private ApplicationContext applicationContext;
//
//    /**
//     * 自动扫描是否开启，默认关闭
//     */
//    @Value("${workflow.autoScanner.switch:false}")
//    private Boolean autoScanner;
//
//    /**
//     * 自动扫描路径
//     */
//    @Value("${workflow.autoScanner.path:classpath:/processes/}")
//    private String scannerPath;
//
//
//    @Override
//    public void run(String... args) throws Exception {
//        if (autoScanner && scannerPath != null) {
//            // 扫描指定路径
//            Resource[] resources = applicationContext.getResources(scannerPath);
//            log.info("自动扫描BPMN文件，资源数量:" + resources.length);
//            for (Resource resource : resources) {
//                // 读取文件流
//
//            }
//        }
//    }
//}
