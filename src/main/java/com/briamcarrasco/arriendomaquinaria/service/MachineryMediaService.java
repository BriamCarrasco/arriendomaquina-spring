package com.briamcarrasco.arriendomaquinaria.service;

import java.util.List;
import com.briamcarrasco.arriendomaquinaria.model.MachineryMedia;

public interface MachineryMediaService {
    List<MachineryMedia> getMediaByMachinery(Long machineryId);

    MachineryMedia addImage(Long machineryId, String imageUrl);

    MachineryMedia addImageFile(Long machineryId, org.springframework.web.multipart.MultipartFile file);

    MachineryMedia addVideo(Long machineryId, String videoUrl);

    void deleteMedia(Long mediaId);
}
