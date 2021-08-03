package com.adjs.gaoleng_plus.controller;

import com.adjs.gaoleng_plus.annoation.UserLoginToken;
import com.adjs.gaoleng_plus.common.PageBean;
import com.adjs.gaoleng_plus.service.FileServiceImpl;
import common.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/file")
public class FileController {

    @Autowired
    FileServiceImpl fileService;

    @RequestMapping("/queryList")
    @ResponseBody
    public Response queryFile(HttpServletRequest req, PageBean pageBean) {
        return fileService.queryFile(pageBean);
    }

    @UserLoginToken
    @RequestMapping("/query/{id}")
    @ResponseBody
    public Response queryFile(@PathVariable("id") String id) {
        return fileService.queryFile(id);
    }

    @UserLoginToken
    @RequestMapping("/download/{id}")
    public void downloadFile(@PathVariable("id") String fileId,
                             HttpServletRequest request, HttpServletResponse response) {
        fileService.downloadFile(fileId, request, response);
    }

    @UserLoginToken
    @PostMapping("/upload")
    @ResponseBody
    public Response upload(@RequestParam("file")MultipartFile file) {
        return fileService.uploadFile(file);
    }
}
