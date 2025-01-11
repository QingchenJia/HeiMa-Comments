package edu.qingchenjia.heimacomments.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.crypto.digest.DigestUtil;
import edu.qingchenjia.heimacomments.common.Constant;
import edu.qingchenjia.heimacomments.common.R;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@RestController
@RequestMapping("/upload")
public class UploadController {
    /**
     * 处理博客文章的上传图片请求
     * 该方法接收一个multipart文件作为参数，保存该文件并返回其保存名称
     *
     * @param image 用户上传的图片文件，通过表单字段“file”获取
     * @return 返回一个包含保存图片名称的响应对象
     * @throws IOException 如果在保存文件过程中发生I/O错误
     */
    @PostMapping("/blog")
    public R<String> uploadImage(@RequestParam("file") MultipartFile image) throws IOException {
        // 目标目录，存储上传图片的文件夹
        File targetDirectory = new File(Constant.IMAGE_PATH);
        // 检查目标目录是否为空，如果为空则创建目录
        if (FileUtil.isEmpty(targetDirectory)) {
            FileUtil.mkdir(targetDirectory);
        }

        // 获取原始文件名
        String originalFilename = image.getOriginalFilename();
        // 提取文件后缀
        String suffix = FileUtil.getSuffix(originalFilename);
        // 提取文件名前缀
        String originalPrefix = FileUtil.getPrefix(originalFilename);

        // 生成文件名，使用原文件名前缀的MD5散列值以确保唯一性
        String fileName = DigestUtil.md5Hex(originalPrefix);

        // 组合生成的新文件名
        String imgName = fileName + "." + suffix;

        // 创建目标文件对象
        File targetFile = new File(targetDirectory, imgName);
        // 将上传的图片文件移动到目标文件夹中
        FileUtil.move(image.getResource().getFile(), targetFile, true);

        // 返回成功响应，包含保存的图片名称
        return R.ok(imgName);
    }
}
