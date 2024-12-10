package com.yashcode.EcommerceBackend.service.ImageClient;


import com.yashcode.EcommerceBackend.entity.dto.ImageDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@FeignClient(url="http://localhost:8082",value="Image-Client")
public interface ImageClient {
    @PostMapping("/api/v1/images/upload")
    List<ImageDto> addImage(@RequestParam List<MultipartFile> files, @RequestParam Long productId);
}
