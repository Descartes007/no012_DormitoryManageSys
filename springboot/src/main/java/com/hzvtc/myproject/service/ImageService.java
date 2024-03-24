package com.hzvtc.myproject.service;

import com.hzvtc.myproject.dao.ImageMapper;
import com.hzvtc.myproject.entity.Image;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author XiongXinxin
 * @date 2021-03-19
 */
@Service
public class ImageService {
    @Autowired
    private ImageMapper imageMapper;

    public void save(Image image) {
        imageMapper.save(image);
    }

    public List<Image> selectByMd5(String md5) {
        return imageMapper.selectByMd5(md5);
    }
}
