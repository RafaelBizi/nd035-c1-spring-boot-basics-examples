package com.example.demo.controller;


import com.example.demo.model.User;
import com.example.demo.service.FileService;
import com.example.demo.service.UserService;
import com.example.demo.utils.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

@Controller
@RequestMapping("/file")
public class FileController {
    private Logger logger =  LoggerFactory.getLogger(FileController.class);

    private String redirectPath = "redirect:/home";

    @Autowired
    private FileService fileService;

    @Autowired
    private UserService userService;


    @PostMapping
    public String addFile(@RequestParam(value = "fileUpload") MultipartFile file,
                                RedirectAttributes redirectAttributes) throws IOException {

        User user = userService.getUser(userService.getCurrentUsername());

            if(!file.isEmpty()){
                if(file.getSize() > Constants.FILE_SIZE){
                    redirectAttributes.addFlashAttribute
                            ("errorMessage","The file exceeds allowed size " + Constants.FILE_SIZE + ". Please try again");
                    return redirectPath;
                }
            if(fileService.findFile(file.getOriginalFilename()) != null){
                redirectAttributes.addFlashAttribute("errorMessage","The file exist. Please check the name");
                return redirectPath;
            }

                String savedFile=fileService.storeFile(file,user);
                logger.info("Saved filename: "+savedFile);
                redirectAttributes.addFlashAttribute("successMessage","The file"+savedFile+" saved successful.");
                return redirectPath ;
        }
        redirectAttributes.addFlashAttribute("errorMessage","The were an error during saving the file. Please try again");
        return redirectPath;

    }
    @GetMapping("/{name}")
    public String getFile(@PathVariable String name, Model model){

        model.addAttribute("file",fileService.findFile(name));
        return "result";
    }


    @GetMapping("/delete/{id}")
    public String deleteFile(@PathVariable int id, Model model){
        model.addAttribute("successMessage",fileService.deleteFile(id));
        return "result";
    }

    @GetMapping("/download/{name}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String name){
        Resource file =fileService.loadFile(name);
        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
            .body(file);
    }
}
