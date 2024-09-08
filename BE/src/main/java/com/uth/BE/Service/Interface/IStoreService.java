package com.uth.BE.Service.Interface;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface IStoreService {
    public boolean saveFile(MultipartFile file, String filename);
    Resource loadFile(String filename);
    boolean deleteFile(String filename);
}
