package com.lt.puredesign.service.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lt.puredesign.common.Constants;
import com.lt.puredesign.common.Result;
import com.lt.puredesign.entity.Files;
import com.lt.puredesign.mapper.FileMapper;
import com.lt.puredesign.service.FileService;
import com.lt.puredesign.util.RedisUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

/**
 * @description: 文件服务实现类
 * @author: Lt
 * @date: 2022/3/15 9:07
 */
@Service
public class FileServiceImpl extends ServiceImpl<FileMapper, Files> implements FileService {

    @Value("${files.upload.path}")
    private String fileUploadPath;

    @Autowired
    private FileMapper fileMapper;

    @Override
    public Page<Files> findPage(Page<Files> page, String name) {
        QueryWrapper<Files> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_delete", false);
        queryWrapper.orderByDesc("id");
        if (StringUtils.isNotBlank(name)) {
            queryWrapper.like("name", name);
        }
        return fileMapper.selectPage(page, queryWrapper);
    }

    @Override
    public Result delete(Integer id) {
        Files files = fileMapper.selectById(id);
        if (files == null) {
            return Result.error(Constants.CODE_400, "找不到该文件！！！");
        }
        // 逻辑删除
        files.setIsDelete(true);
        fileMapper.updateById(files);
        RedisUtils.flushRedis(Constants.FILES_KEY);
        return Result.success();
    }

    @Override
    public Result deleteBatch(List<Integer> ids) {
        QueryWrapper<Files> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("id", ids);
        List<Files> files = fileMapper.selectList(queryWrapper);
        if (files == null || files.size() == 0) {
            return Result.error(Constants.CODE_400, "未找到该文件！！！");
        }
        for (Files file : files) {
            file.setIsDelete(true);
            fileMapper.updateById(file);
        }
        return Result.success();
    }

    @Override
    public String upload(MultipartFile file) throws IOException {
        // 1.获取文件的相关属性
        String originalFilename = file.getOriginalFilename();
        String type = FileUtil.extName(originalFilename);
        long size = file.getSize();
        // 2.存储到本地磁盘
        // 2.1 定义文件的标识码
        String uuid = IdUtil.fastSimpleUUID();
        String fileUUID = uuid + StrUtil.DOT + type;
        // 2.2 创建吸纳管文件目录
        File uploadFile = new File(fileUploadPath + fileUUID);
        File parentFile = uploadFile.getParentFile();
        if (!parentFile.exists()) {
            parentFile.mkdirs();
        }
        // 2.3 获取文件的md5码
        String md5 = SecureUtil.md5(file.getInputStream());
        // 2.4 根据MD5从数据库中查询是否有相同的资源
        Files dbFiles = getFileByMd5(md5);
        String url;
        if (dbFiles != null) {
            // 有相同的资源 不用在保存到数据库与磁盘中
            url = dbFiles.getUrl();
        } else {
            // 上传到文件磁盘
            file.transferTo(uploadFile);
            url = "http://localhost:9000/file/" + fileUUID;

            // 3.存储数据库
            Files saveFile = new Files();
            saveFile.setName(originalFilename);
            saveFile.setType(type);
            saveFile.setSize(size / 1024);
            saveFile.setUrl(url);
            saveFile.setMd5(md5);
            fileMapper.insert(saveFile);
        }
        // 清空缓存
        RedisUtils.flushRedis(Constants.FILES_KEY);
        // 4.返回url
        return url;
    }

    @Override
    public void download(String fileUUID, HttpServletResponse response) throws IOException {
        // 1.根据文件的唯一标识码获取文件路径
        File uploadFile = new File(fileUploadPath + fileUUID);
        // 2.设置输出流格式
        ServletOutputStream outputStream = response.getOutputStream();
        response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileUUID, "UTF-8"));
        response.setContentType("application/octet-stream");
        // 3.通过输出流写出
        outputStream.write(FileUtil.readBytes(uploadFile));
        outputStream.flush();
        outputStream.close();
    }

    private Files getFileByMd5(String md5) {
        QueryWrapper<Files> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("md5", md5);
        List<Files> files = fileMapper.selectList(queryWrapper);
        return files.size() == 0 ? null : files.get(0);
    }
}
