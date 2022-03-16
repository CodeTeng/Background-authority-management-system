package com.lt.puredesign.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lt.puredesign.common.Constants;
import com.lt.puredesign.common.Result;
import com.lt.puredesign.entity.Files;
import com.lt.puredesign.service.FileService;
import com.lt.puredesign.util.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @description: 文件控制器
 * @author: Lt
 * @date: 2022/3/15 9:05
 */
@RestController
@RequestMapping("/file")
public class FileController {

    @Autowired
    private FileService fileService;

    /**
     * @param pageNum  当前页数
     * @param pageSize 当前页数显示多少页
     * @param name     文件名称
     * @return 封装结果集
     */
    @GetMapping("/page")
    public Result findPage(@RequestParam Integer pageNum,
                           @RequestParam Integer pageSize,
                           @RequestParam(defaultValue = "") String name) {
        return Result.success(fileService.findPage(new Page<Files>(pageNum, pageSize), name));
    }

    /**
     * 更新文件接口
     *
     * @param files 文件
     * @return 封装结果集
     */
    @PostMapping("/update")
//    @CachePut(value = "files", key = "'frontAll'")
    public Result update(@RequestBody Files files) {
        fileService.updateById(files);
        RedisUtils.flushRedis(Constants.FILES_KEY);
        return Result.success();
    }

    /**
     * 删除单个文件接口
     *
     * @param id 路径参数
     * @return 结果集
     */
    @DeleteMapping("/del/{id}")
    public Result delete(@PathVariable Integer id) {
        return Result.success(fileService.delete(id));
    }

    /**
     * 删除多个文件
     *
     * @param ids 文件id
     * @return 结果集
     */
    @PostMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        return Result.success(fileService.deleteBatch(ids));
    }

    /**
     * 文件上传接口
     *
     * @param file 前端传递过来的文件参数
     * @return 文件的UUID
     * @throws IOException 异常
     */
    @PostMapping("/upload")
    public String upload(@RequestParam MultipartFile file) throws IOException {
        return fileService.upload(file);
    }

    /**
     * 文件下载接口 http://localhost:9000/file/{fileUUID}
     *
     * @param fileUUID 文件的UUID
     * @param response 响应浏览器
     * @throws IOException 异常
     */
    @GetMapping("/{fileUUID}")
    public void download(@PathVariable String fileUUID, HttpServletResponse response) throws IOException {
        fileService.download(fileUUID, response);
    }
}
