package com.adjs.gaoleng_plus.service;

import com.adjs.gaoleng_plus.api.FileDao;
import com.adjs.gaoleng_plus.common.GLConfig;
import com.adjs.gaoleng_plus.common.PageBean;
import com.adjs.gaoleng_plus.entity.FileDo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import common.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import redis.clients.jedis.JedisPool;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.HashMap;
import java.util.Map;

@Service
public class FileServiceImpl extends BaseService {

    private static final Logger logger  = LoggerFactory.getLogger(FileServiceImpl.class);

    @Autowired
    FileDao fileDao;

    @Autowired
    GLConfig glConfig;

    @Value("${download.prefix:/}")
    String defaultDownloadPrefix;

    @Autowired
    JedisPool jedisPool;

    @Autowired
    LoginInfo loginInfo;


    public Response queryFile(String id) {
        Map<String, Object> data = new HashMap<>();
        FileDo fileDo = fileDao.queryFile(id, defaultDownloadPrefix);
        data.put("file", fileDo);
        return retSuccessResponse(data);
    }

    public Response queryFile(PageBean pageBean) {
        Map<String, Object> data = new HashMap<>();
        Page page = PageHelper.startPage(pageBean.getStart(), pageBean.getLimit());
        fileDao.queryFileList(pageBean.getName(), defaultDownloadPrefix);
        data.put("file", page);
        return retSuccessResponse(data);
    }

    public Response uploadFile(MultipartFile file) {
        if (file.isEmpty()) {
            return retCommonResponse("010001", "上传失败，请选择文件");
        }
        String userId = loginInfo.getUserId();
        String fileName = file.getOriginalFilename();
        String filePath = glConfig.FILE_PATH + fileName;
        File dest = new File(filePath);
        try {
            file.transferTo(dest);
            FileDo fileDo = getFileDo(file, userId);
            fileDo.setPath(filePath);
            fileDao.saveFile(fileDo);
            return retCommonResponse("000000", "上传成功");
        } catch (Exception e) {
            e.printStackTrace();
            return retFailedResponse(e.getMessage());
        }
    }

    public void downloadFile(String fileId, HttpServletRequest request, HttpServletResponse response) {
        FileDo fileDo = fileDao.queryFile(fileId, defaultDownloadPrefix);
        FileInputStream file=null;
        FileChannel fischannel=null;
        try {
            //获取文件类型
            String mimeType = request.getServletContext().getMimeType(fileDo.getName());

            File file1 = new File(fileDo.getPath());
            file = new FileInputStream(file1);
            long fileLength = file1.length();
            //对文件名进行编码，解决中文名乱码
            String fileName = URLEncoder.encode(fileDo.getName(), "UTF-8");
            response.setContentType("application/octet-stream");
            response.setHeader("Access-Control-Expose-Headers", "Content-Disposition");
            //指定文件下载，并解决中文名乱码
            response.setHeader("Content-Disposition", "attachment;filename*=utf-8'zh_cn'" + fileName);//重点
            response.setHeader("Content-Length", String.valueOf(fileLength));
            response.setContentType(mimeType);
            fischannel=file.getChannel();

            int bufferSize = 2048;
            ByteBuffer buff = ByteBuffer.allocateDirect(4096);
            byte[] byteArr = new byte[bufferSize];
            int nGet;
            while(fischannel.read(buff)!=-1){
                buff.flip();
                while (buff.hasRemaining()) {
                    nGet = Math.min(buff.remaining(), bufferSize);
                    // read bytes from disk
                    buff.get(byteArr, 0, nGet);
                    // write bytes to output
                    response.getOutputStream().write(byteArr);
                }
                buff.clear();
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(fischannel!=null&&file!=null){
                try {
                    file.close();
                    fischannel.close();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private FileDo getFileDo(MultipartFile file, String userName) {
        FileDo fileDo = new FileDo();
        fileDo.setName(file.getOriginalFilename());
        fileDo.setType(file.getContentType());
        fileDo.setSize(file.getSize());
        fileDo.setCreateUser(userName);
        return fileDo;
    }

}
