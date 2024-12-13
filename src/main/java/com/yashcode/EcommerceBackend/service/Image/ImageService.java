package com.yashcode.EcommerceBackend.service.Image;

import com.yashcode.EcommerceBackend.entity.dto.ImageDto;
import com.yashcode.EcommerceBackend.service.ImageClient.ImageClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
@Service
@RequiredArgsConstructor
public class ImageService implements IImageService{

    private final ImageClient client;

//    @Override
//    public Image getImageById(Long id) {
//        return imageRepository.findById(id).orElseThrow(()->new RuntimeException("Image not found with id "+id));
//    }
//
//    @Override
//    public void deleteImageById(Long id) {
//        imageRepository.findById(id).ifPresentOrElse(imageRepository::delete,()->{
//            throw new RuntimeException("No image found with id: "+id);
//        });
//    }
//
@Override
public List<ImageDto> saveImages(Long productId, List<MultipartFile> files) {
   return client.addImage(files,productId);
}
//
//    @Override
//    public void updateImage(MultipartFile file, Long imageId) {
//        Image image=getImageById(imageId);
//        try{
//            image.setFileName(file.getOriginalFilename());
//            image.setFileType(file.getContentType());
//            image.setImage(new SerialBlob(file.getBytes()));
//            imageRepository.save(image);
//        }
//        catch (IOException | SQLException e){
//            throw new RuntimeException(e.getMessage());
//        }
//
//    }
}
