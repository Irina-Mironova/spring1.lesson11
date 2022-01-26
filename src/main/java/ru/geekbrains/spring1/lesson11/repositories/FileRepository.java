package ru.geekbrains.spring1.lesson11.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.geekbrains.spring1.lesson11.entities.FileInfo;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface FileRepository extends CrudRepository<FileInfo, Long> {
    Optional<FileInfo> findByHash(UUID hash);

    List<FileInfo> findAllBySubtype(int subtype);

    List<FileInfo> findAllByHash(UUID hash);
}

