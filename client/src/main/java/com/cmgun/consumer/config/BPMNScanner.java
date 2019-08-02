package com.cmgun.consumer.config;

import com.cmgun.api.common.Response;
import com.cmgun.api.enums.SourceEnum;
import com.cmgun.api.service.ActProcessService;
import com.cmgun.consumer.utils.FileUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

/**
 * 自动扫描BPMN流程文件，自动部署
 *
 * @author chenqilin
 * @Date 2019/7/16
 */
@Slf4j
@Component
public class BPMNScanner implements CommandLineRunner {

    private static final String AUTO_CONFIG_TAG = "AutoConfig-";

    @Autowired
    private ActProcessService actProcessService;
    @Autowired
    private ApplicationContext applicationContext;

    /**
     * 自动扫描是否开启，默认关闭
     */
    @Value("${workflow.autoScanner.switch:false}")
    private Boolean autoScanner;

    /**
     * 自动扫描路径
     */
    @Value("${workflow.autoScanner.path:classpath:/processes/**}")
    private String scannerPath;

    @Override
    public void run(String... args) throws Exception {
        if (autoScanner && scannerPath != null) {
            // 扫描指定路径
            Resource[] resources = applicationContext.getResources(scannerPath);
            log.info("自动扫描BPMN文件，资源数量:" + resources.length);
            for (Resource resource : resources) {
                // 读取文件流
                MultipartFile multipartFile = FileUtils.createMultipartFile("file", resource);
                try {
                    Response response = actProcessService.deploy(multipartFile.getOriginalFilename(), AUTO_CONFIG_TAG + multipartFile.getOriginalFilename()
                            , SourceEnum.INVOICE_LOAN.getCode()
                            , AUTO_CONFIG_TAG + SourceEnum.INVOICE_LOAN.getCode(), multipartFile);
                    if (!Response.OK.equals(response.getCode())) {
                        // 非正常，记录异常信息
                        log.warn("自动部署BPMN文件失败，文件名：{}", resource.getFilename());
                    }
                } catch (Exception e) {
                    log.warn("自动部署BPMN文件失败，文件名：{}", resource.getFilename(), e);
                }
            }
        }
    }
}
