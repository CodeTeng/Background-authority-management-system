package com.lt.puredesign.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lt.puredesign.common.Result;
import com.lt.puredesign.entity.Files;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @description: 文件服务
 * @author: Lt
 * @date: 2022/3/15 9:06
 */
public interface FileService extends IService<Files> {
    /**
     * 删除文件
     *
     * @param id 路径参数
     * @return 结果集
     */
    Result delete(Integer id);

    /**
     * 删除多个文件
     *
     * @param ids 文件id
     * @return 结果集
     */
    Result deleteBatch(List<Integer> ids);

    /**
     * 文件上传
     *
     * @param file 前端传递过来的文件
     * @return 上传文件的URL
     * @throws IOException 异常
     */
    String upload(MultipartFile file) throws IOException;

    /**
     * 文件下载接口 http://localhost:9000/file/{fileUUID}
     *
     * @param fileUUID 文件的UUID
     * @param response 响应浏览器
     * @throws IOException 异常
     */
    void download(String fileUUID, HttpServletResponse response) throws IOException;

    /**
     * 分页查询
     * @param filesPage 分页参数
     * @param name 文件名
     * @return 分页结果集
     */
    Page<Files> findPage(Page<Files> filesPage, String name);
}
