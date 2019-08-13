package com.cmgun.consumer.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.util.Streams;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 文件处理工具类
 *
 * @author chenqilin
 * @date 2019/7/17
 */
@Slf4j
public class FileUtils {

    /**
     * Resource 转 MultipartFile
     *
     * @param fileParamName multipartFile参数名称
     * @param resource 待传输的文件资源
     */
    public static MultipartFile createMultipartFile(String fileParamName, Resource resource) throws IOException {
        FileItem fileItem = new DiskFileItemFactory().createItem(fileParamName, MediaType.MULTIPART_FORM_DATA_VALUE
                , true
                , resource.getFilename());
        // 处理结束后释放资源
        try (InputStream input = resource.getInputStream();
             OutputStream output = fileItem.getOutputStream()) {
            IOUtils.copy(input, output);
        } catch (IOException e) {
            log.warn("[FileUtils] 创建MultipartFile失败", e);
            throw e;
        }
        return new CommonsMultipartFile(fileItem);
    }
}
