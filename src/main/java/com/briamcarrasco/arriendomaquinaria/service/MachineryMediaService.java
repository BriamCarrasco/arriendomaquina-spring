package com.briamcarrasco.arriendomaquinaria.service;

import java.util.List;
import com.briamcarrasco.arriendomaquinaria.model.MachineryMedia;

/**
 * Interfaz para el servicio de gestión de archivos multimedia asociados a
 * maquinarias.
 * Define métodos para agregar imágenes y videos, obtener archivos multimedia
 * por maquinaria,
 * subir archivos de imagen y eliminar archivos multimedia.
 */
public interface MachineryMediaService {
    /**
     * Obtiene la lista de archivos multimedia asociados a una maquinaria.
     *
     * @param machineryId identificador de la maquinaria
     * @return lista de objetos MachineryMedia
     */
    List<MachineryMedia> getMediaByMachinery(Long machineryId);

    /**
     * Agrega una imagen a una maquinaria usando una URL.
     *
     * @param machineryId identificador de la maquinaria
     * @param imageUrl    URL de la imagen
     * @return el objeto MachineryMedia creado
     */
    MachineryMedia addImage(Long machineryId, String imageUrl);

    /**
     * Agrega una imagen a una maquinaria subiendo un archivo.
     *
     * @param machineryId identificador de la maquinaria
     * @param file        archivo de imagen a subir
     * @return el objeto MachineryMedia creado
     */
    MachineryMedia addImageFile(Long machineryId, org.springframework.web.multipart.MultipartFile file);

    /**
     * Agrega un video a una maquinaria usando una URL.
     *
     * @param machineryId identificador de la maquinaria
     * @param videoUrl    URL del video
     * @return el objeto MachineryMedia creado
     */
    MachineryMedia addVideo(Long machineryId, String videoUrl);

    /**
     * Elimina un archivo multimedia por su identificador.
     *
     * @param mediaId identificador del archivo multimedia a eliminar
     */
    void deleteMedia(Long mediaId);
}