package ru.geekbrains.spring1.lesson11.dto;

import ru.geekbrains.spring1.lesson11.entities.FileInfo;

import java.util.UUID;

public class FileMetaDto {
    private UUID hash;
    private String fileName;

    public FileMetaDto(FileInfo fileInfo) {
        this.hash = fileInfo.getHash();
        this.fileName = fileInfo.getFilename();
    }

    public UUID getHash() {
        return hash;
    }

    public void setHash(UUID hash) {
        this.hash = hash;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
