package ru.geekbrains.spring1.lesson11.services;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.geekbrains.spring1.lesson11.dto.FileMetaDto;
import ru.geekbrains.spring1.lesson11.entities.FileInfo;
import ru.geekbrains.spring1.lesson11.repositories.FileRepository;
import ru.geekbrains.spring1.lesson11.utils.FileUtil;
import ru.geekbrains.spring1.lesson11.utils.HashHelper;

import java.util.Collection;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class FileService {
    private FileRepository fileRepository;

    @Autowired
    public FileService(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }


    public String storeFile(byte[] content, String filename, int subType) throws NoSuchAlgorithmException, IOException {
        UUID hash = HashHelper.getMd5Hash(content);
        Optional<FileInfo> fileInfo = fileRepository.findByHash(hash);
        if (!fileInfo.isPresent()) {
            FileUtil.storeFile(content, hash, filename);
        }
        this.save(hash, filename, subType);
        return filename;
    }

    public void save(UUID hash, String filename, int subType) {
        FileInfo fileInfo = new FileInfo();
        fileInfo.setFilename(filename);
        fileInfo.setHash(hash);
        fileInfo.setSubtype(subType);
        fileRepository.save(fileInfo);
    }

    public byte[] downloadFile(UUID hash) throws IOException {
        Optional<FileInfo> fileInfo = fileRepository.findByHash(hash);
        if (fileInfo.isPresent()) {
            String extension = FilenameUtils.getExtension(fileInfo.get().getFilename().toString());
            String fullFileName = hash.toString() + "." + extension;
            return FileUtil.downloadFile(fullFileName);
        } else {
            return new byte[0];
        }
    }

    public Collection<FileMetaDto> downloadFiles(int subtype) {
        List<FileMetaDto> fileMetaList = fileRepository.findAllBySubtype(subtype).stream().
                map(FileMetaDto::new).collect(Collectors.toList());
        return fileMetaList;
    }


    //Т.к. в нашей БД нет информации о том, какой пользователь сохранил файл, то удаление настроено таким образом,
    //что в БД удаляется одна любая строка с нужным hash
    public String deleteFile(UUID hash) throws IOException {
        List<FileInfo> fileInfoList = fileRepository.findAllByHash(hash);
        if (fileInfoList.size() == 0) {
            return "Файл не найден";
        }

        String fileName = fileInfoList.get(0).getFilename();
        String extension = FilenameUtils.getExtension(fileName);
        String fullFileName = hash.toString() + "." + extension;

        if (fileInfoList.size() == 1) {
            FileUtil.deleteFile(fullFileName);
        }

        fileRepository.delete(fileInfoList.get(0));

        return fileName;

    }
}