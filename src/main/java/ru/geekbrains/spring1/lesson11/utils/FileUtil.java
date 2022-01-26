package ru.geekbrains.spring1.lesson11.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import java.util.stream.Stream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class FileUtil {
    static final String STORE_FOLDER = "./Store";

    private static Path storePath;

    @PostConstruct
    public void init() {
        String currentPath = Paths.get("").toAbsolutePath().toString();
        storePath = Paths.get(currentPath, STORE_FOLDER);
    }

    public static String storeFile(byte[] content, UUID md5, String fileName) throws IOException {
        String fileNameExtension = FilenameUtils.getExtension(fileName);
        fileName = String.format("%s.%s", md5, fileNameExtension);

        Path fullFileNamePath = Paths.get(storePath.toString(), fileName);
        String fullFileName = fullFileNamePath.toString();

        if (!Files.exists(fullFileNamePath)) {
            FileUtils.writeByteArrayToFile(new File(fullFileName), content);
        }
        return fullFileName;
    }

    public static byte[] downloadFile(String fileHash) throws IOException {
        try (Stream<Path> walk = Files.walk(storePath)) {
            String fileName = walk.map(Path::toString)
                    .filter(f -> f.contains(fileHash))
                    .findFirst()
                    .orElseThrow(() -> new IOException("file not found"));

            File file = new File(fileName);
            return Files.readAllBytes(file.toPath());
        }
    }

    public static void deleteFile(String fileName) throws IOException {
        Path fullFileNamePath = Paths.get(storePath.toString(), fileName);
        if (Files.exists(fullFileNamePath)) {
            Files.delete(fullFileNamePath);
        }
    }

}