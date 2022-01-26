package ru.geekbrains.spring1.lesson11.entities;

import lombok.Data;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Data
public class FileInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long Id;

    @Column(name = "hash")
    private UUID hash;

    @Column(name = "filename")
    private String filename;

    @Column(name = "sub_type")
    private int subtype;

}