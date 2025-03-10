package com.ivanledakovich.models;

import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "files")
public class FileModel {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "uuid2")
    @Column(columnDefinition = "UUID", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "creation_date", nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate = new Date();

    @Column(name = "file_name", nullable = false, length = 255)
    private String fileName;

    @Lob
    @JdbcTypeCode(SqlTypes.BINARY)
    @Column(name = "file_data", nullable = false)
    private byte[] fileData;

    @Column(name = "image_name", nullable = false, length = 255)
    private String imageName;

    @Column(name = "image_type", nullable = false, length = 10)
    private String imageType;

    @Lob
    @JdbcTypeCode(SqlTypes.BINARY)
    @Column(name = "image_data", nullable = false)
    private byte[] imageData;

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public Date getCreationDate() { return creationDate; }
    public void setCreationDate(Date creationDate) { this.creationDate = creationDate; }
    public String getFileName() { return fileName; }
    public void setFileName(String fileName) { this.fileName = fileName; }
    public byte[] getFileData() { return fileData; }
    public void setFileData(byte[] fileData) { this.fileData = fileData; }
    public String getImageName() { return imageName; }
    public void setImageName(String imageName) { this.imageName = imageName; }
    public String getImageType() { return imageType; }
    public void setImageType(String imageType) { this.imageType = imageType; }
    public byte[] getImageData() { return imageData; }
    public void setImageData(byte[] imageData) { this.imageData = imageData; }

    @PrePersist
    protected void onCreate() {
        if (creationDate == null) {
            creationDate = Date.from(Instant.now());
        }
    }
}