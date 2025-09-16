package com.agricultural.em.service;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.agricultural.em.constants.Constants;
import com.agricultural.em.exception.ServiceException;
import com.agricultural.em.entity.Avatar;
import com.agricultural.em.mapper.AvatarMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.List;
import java.util.UUID;

@Service
public class AvatarService {
    @Resource
    private AvatarMapper avatarMapper;

    public String upload(MultipartFile uploadFile) {
        String url = null;
        // 通过md5判断文件是否已经存在，防止在服务器存储相同文件
        InputStream inputStream = null;
        try {
            inputStream = uploadFile.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String md5 = SecureUtil.md5(inputStream);
        // 通过对比数据库中的md5的数据来判断是否存在
        Avatar dbAvatar = avatarMapper.selectByMd5(md5);

        // 假如查询到dbAvatar这个数值不为null,但是，在对应文件夹中没有对应的文件，就创建对应的文件重命名成对应的文件名
        if (dbAvatar == null) {
            String originalFilename = uploadFile.getOriginalFilename(); // 文件原始名字
            String type = originalFilename.substring(originalFilename.lastIndexOf(".") + 1); // 文件后缀
            System.out.println(originalFilename + "   " + type);
            long size = uploadFile.getSize() / 1024; // 文件大小，单位kb
            // 文件不存在，则保存文件
            File folder = new File(Constants.avatarFolderPath);
            if (!folder.exists()) {
                folder.mkdir();
            }
            String folderPath = folder.getAbsolutePath() + "/"; // 文件存储文件夹的位置
            System.out.println("文件存储地址" + folderPath);

            // 将文件保存为UUID的名字，通过uuid生成url
            String uuid = UUID.randomUUID().toString().replace("-", "").toLowerCase();
            String finalFileName = uuid + "." + type;
            File targetFile = new File(folderPath + finalFileName);
            try {
                uploadFile.transferTo(targetFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
            url = "/avatar/" + finalFileName;
            Avatar avatar = new Avatar(type, size, url, md5);
            System.out.println(avatar);
            avatarMapper.save(avatar);
            return url;
        } else {
            String originalFilename = uploadFile.getOriginalFilename(); // 文件原始名字
            String type = originalFilename.substring(originalFilename.lastIndexOf(".") + 1); // 文件后缀
            System.out.println(originalFilename + "   " + type);
            long size = uploadFile.getSize() / 1024; // 文件大小，单位kb
            // 文件不存在，则创建对应的文件夹
            File folder = new File(Constants.avatarFolderPath);
            if (!folder.exists()) {
                folder.mkdir();
            }
            String folderPath = folder.getAbsolutePath() + "/"; // 文件存储文件夹的位置
            System.out.println("文件存储地址" + folderPath);
            String dbAvatarUrl = dbAvatar.getUrl();
            String substringUrl = dbAvatarUrl.substring(dbAvatarUrl.lastIndexOf("/") + 1);
            File file = new File(folderPath + substringUrl);
            if (!file.exists()) {
                // 假如不存在，就创建对应的文件
                try {
                    uploadFile.transferTo(file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
        return dbAvatar.getUrl();
    }

    // 根据文件名下载文件
    public void download(String fileName, HttpServletResponse response) {
        // 标准化 fileName，去掉可能的前缀路径（例如传入的是 /avatar/xxx 或包含额外斜杠）
        if (fileName.contains("/")) {
            fileName = fileName.substring(fileName.lastIndexOf('/') + 1);
        }
        if (fileName.startsWith("/")) {
            fileName = fileName.substring(1);
        }
        File file = new File(Constants.avatarFolderPath + fileName);
        if (!file.exists()) {
            // 打印尝试的路径，便于调试
            System.out.println("尝试读取文件：" + file.getAbsolutePath());
            throw new ServiceException(Constants.CODE_500, "文件不存在");
        }
        try {
            ServletOutputStream os = response.getOutputStream();
            // 根据文件后缀设置 MIME 类型，避免浏览器下载而无法直接显示图片
            String ext = fileName.substring(fileName.lastIndexOf('.') + 1).toLowerCase();
            String mime = "application/octet-stream";
            if ("jpg".equals(ext) || "jpeg".equals(ext)) {
                mime = "image/jpeg";
            } else if ("png".equals(ext)) {
                mime = "image/png";
            } else if ("gif".equals(ext)) {
                mime = "image/gif";
            } else if ("bmp".equals(ext)) {
                mime = "image/bmp";
            } else if ("webp".equals(ext)) {
                mime = "image/webp";
            }
            response.setContentType(mime);
            // 使用 inline 使浏览器直接在页面中展示，而不是以附件形式下载
            response.setHeader("Content-Disposition", "inline;filename=" + URLEncoder.encode(fileName, "UTF-8"));
            os.write(FileUtil.readBytes(file));
            os.flush();
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int delete(int id) {
        Avatar avatar = avatarMapper.selectById(id);
        int delete = avatarMapper.delete(id);
        System.out.println(delete);
        if (delete == 1) {
            String fileName = StrUtil.subAfter(avatar.getUrl(), "/", true);
            System.out.println(fileName);
            File file = new File(Constants.avatarFolderPath + fileName);
            System.out.println(file.getAbsolutePath());
            if (file.exists()) {

                boolean delete1 = file.delete();
                if (delete1) {
                    System.out.println("删除成功");
                }
            }
        }
        return delete;
    }

    public List<Avatar> selectPage(int index, int pageSize) {
        return avatarMapper.selectPage(index, pageSize);
    }

    public int getTotal() {
        return avatarMapper.getTotal();
    }
}
