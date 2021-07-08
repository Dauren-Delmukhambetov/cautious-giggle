package kz.toko.app.service;


import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * This interfaces provides methods to manipulate with files.
 *
 * @author D.Delmukhambetov
 * @since 22.06.2021
 */
public interface FileStorageService {

    String write(MultipartFile fileToSave) throws IOException;

    void delete(String filePath);

}

