package edu.qingchenjia.heimacomments.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.crypto.digest.DigestUtil;
import edu.qingchenjia.heimacomments.common.Constant;
import edu.qingchenjia.heimacomments.common.R;
import org.springframework.web.bind.annotation.*;
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

    /**
     * 删除博客中的图片
     * 此方法通过接收图片名称，尝试从指定的路径中删除该图片
     * 主要用于清理不再需要的博客配图，以节省服务器存储空间
     *
     * @param imageName 要删除的图片名称
     * @return 返回删除操作的结果，如果删除成功则返回R.ok()，否则返回R.fail("错误的文件名称")
     */
    @GetMapping("/blog/delete")
    public R<?> deleteImage(@RequestParam("name") String imageName) {
        // 根据图片名称构建File对象，用于后续的删除操作
        File image = new File(Constant.IMAGE_PATH, imageName);

        // 检查构建的File对象是否为目录，如果是，则返回失败响应
        if (FileUtil.isDirectory(image)) {
            return R.fail("错误的文件名称");
        }

        // 尝试删除文件，删除成功后返回成功响应
        FileUtil.del(image);
        return R.ok();
    }
}
