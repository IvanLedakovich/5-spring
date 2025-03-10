package com.ivanledakovich.database.jpa;

import com.ivanledakovich.models.FileModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

public interface FileJpaRepository extends JpaRepository<FileModel, String> {
    @Query("SELECT f FROM FileModel f WHERE f.fileName = :name OR f.imageName = :name")
    FileModel findByFileNameOrImageName(@Param("name") String name);

    @Transactional
    @Modifying
    @Query("DELETE FROM FileModel f WHERE f.fileName = :name OR f.imageName = :name")
    void deleteByFileNameOrImageName(@Param("name") String name);

    @Query("SELECT f FROM FileModel f WHERE f.fileName = :name OR f.imageName = :name")
    List<FileModel> findAllByFileNameOrImageName(@Param("name") String name);
}