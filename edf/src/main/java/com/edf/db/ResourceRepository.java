package com.edf.db;

import com.edf.db.orm.Resource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ResourceRepository extends JpaRepository<Resource, Long> {
    @Query(value = "select directory from Resource where userName=:userName and filename=:filename")
    String getDirectoryByUserNameAndFilename(@Param("userName") String userName, @Param("filename") String filename);
    boolean existsByUserNameAndFilename(String userName, String filename);
    long deleteByUserNameAndFilename(String userName, String filename);
}