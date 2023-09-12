package com.lzx.service.impl;

import com.google.gson.Gson;
import com.lzx.domain.ResponseResult;
import com.lzx.enums.AppHttpCodeEnum;
import com.lzx.handler.exception.SystemException;
import com.lzx.service.UploadService;
import com.lzx.utils.PathUtils;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

@Service
@ConfigurationProperties(prefix = "oss")
@Data
public class UploadServiceImpl implements UploadService {
    private String accessKey;
    private String secretKey;
    private String bucket;

    @Override
    public ResponseResult upload(MultipartFile img) {
        //1.判断文件类型
        //1.1获取文件名字
        String originalFileName = img.getOriginalFilename();
        //1.2判断后缀
        if(!originalFileName.endsWith(".png")){
            throw new SystemException(AppHttpCodeEnum.FILE_TYPE_ERROR);
        }

        //2.获取OSS文件名字
        String OSSFileName = PathUtils.generateFilePath(originalFileName);

        //2.上传到OSS
        String url = uploadToOSS(img, OSSFileName);

        //3.返回
        return ResponseResult.okResult(url);
    }

    private String uploadToOSS(MultipartFile img, String OSSFileName){
        //   构造一个带指定 Region 对象的配置类
        Configuration cfg = new Configuration(Region.huanan());
        //...其他参数参考类注释
        UploadManager uploadManager = new UploadManager(cfg);
        //默认不指定key的情况下，以文件内容的hash值作为文件名
        String key = OSSFileName;
        try {
            InputStream inputStream = img.getInputStream();
            Auth auth = Auth.create(accessKey, secretKey);
            String upToken = auth.uploadToken(bucket);
            try {
                Response response = uploadManager.put(inputStream,key,upToken,null, null);
//                Response response = uploadManager.put(originalFileName,key,upToken);
                //解析上传成功的结果
                DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
                System.out.println(putRet.key);
                System.out.println(putRet.hash);
                return "rzfdgl4w8.hn-bkt.clouddn.com/" + key;
            } catch (QiniuException ex) {
                Response r = ex.response;
                System.err.println(r.toString());
                try {
                    System.err.println(r.bodyString());
                } catch (QiniuException ex2) {
                    //ignore
                }
            }
        } catch (Exception ex) {
            //ignore
        }
        return "www";
    }
}
