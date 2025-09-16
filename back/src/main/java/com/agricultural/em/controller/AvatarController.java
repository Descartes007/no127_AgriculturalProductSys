package com.agricultural.em.controller;

import com.agricultural.em.constants.Constants;
import com.agricultural.em.common.Result;
import com.agricultural.em.annotation.Authority;
import com.agricultural.em.entity.AuthorityType;
import com.agricultural.em.entity.Avatar;
import com.agricultural.em.service.AvatarService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/avatar")
public class AvatarController {

    @Resource
    private AvatarService avatarService;

    // 上传头像
    @PostMapping()
    public Result uploadAvatar(@RequestParam MultipartFile file) {
        System.out.println("uploadAvatar====>");
        String url = avatarService.upload(file);
        return Result.success(url);
    }

    // 根据文件名下载文件，即文件的url（允许带点的文件名）
    @GetMapping("/{fileName:.+}")
    public void download(@PathVariable String fileName, HttpServletResponse response) {
        avatarService.download(fileName, response);
    }

    // 根据文件id删除文件
    // @Authority(AuthorityType.requireAuthority)
    @DeleteMapping("/{id}")
    public Result deleteById(@PathVariable int id) {
        int i = avatarService.delete(id);
        if (i == 1) {
            return Result.success();
        } else {
            return Result.error(Constants.CODE_500, "删除失败");
        }
    }

    // 查询
    @GetMapping("/page")
    public Result selectPage(@RequestParam int pageNum,
            @RequestParam int pageSize) {
        int index = (pageNum - 1) * pageSize;
        List<Avatar> avatars = avatarService.selectPage(index, pageSize);
        int total = avatarService.getTotal();
        HashMap<String, Object> map = new HashMap<>();
        map.put("records", avatars);
        map.put("total", total);
        return Result.success(map);
    }
}
