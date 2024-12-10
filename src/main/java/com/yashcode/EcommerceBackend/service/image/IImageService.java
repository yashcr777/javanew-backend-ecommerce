package com.yashcode.EcommerceBackend.service.image;

import com.yashcode.EcommerceBackend.entity.dto.ImageDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IImageService {
//    Image getImageById(Long id);
//    void deleteImageById(Long id);
    List<ImageDto>saveImages(Long productId, List<MultipartFile> files);
//    void updateImage(MultipartFile file,Long imageId);
}
