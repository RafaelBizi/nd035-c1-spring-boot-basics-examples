package com.example.demo.service;

import com.example.demo.model.File;
import com.example.demo.model.User;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


public interface FileService {

    String storeFile(MultipartFile file, User owner) throws IOException;

    Resource loadFile(String name);

    File findFile(String filename);

    List<File> getAllFiles(int userId);

    Object deleteFile(int id);
}
