package com.ivanledakovich.database;

import com.ivanledakovich.models.DatabaseConnectionProperties;
import com.ivanledakovich.models.FileModel;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FileDatabaseFunctions implements FileRepository {

    private final DatabaseConnectionProperties dbProps;

    public FileDatabaseFunctions(DatabaseConnectionProperties dbProps) {
        this.dbProps = dbProps;
    }

    private Connection connect() throws SQLException {
        try {
            Class.forName(dbProps.getDriverClassName());
            return DriverManager.getConnection(
                    dbProps.getUrl(),
                    dbProps.getUsername(),
                    dbProps.getPassword()
            );
        } catch (ClassNotFoundException e) {
            throw new SQLException("PostgreSQL driver not found", e);
        }
    }

    @Override
    public FileModel findByFileNameOrImageName(String name) {
        final String SQL = "SELECT * FROM files WHERE file_name = ? OR image_name = ?";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {

            pstmt.setString(1, name);
            pstmt.setString(2, name);

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                FileModel model = new FileModel();
                model.setId(rs.getLong("id"));
                model.setCreationDate(rs.getDate("creation_date"));
                model.setFileName(rs.getString("file_name"));
                model.setFileData(rs.getBytes("file_data"));
                model.setImageName(rs.getString("image_name"));
                model.setImageType(rs.getString("image_type"));
                model.setImageData(rs.getBytes("image_data"));
                return model;
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException("Query failed", e);
        }
    }

    @Override
    public void insertAFile(File txtFile, File imageFile) {
        final String SQL = """
            INSERT INTO files(creation_date, file_name, file_data, image_name, image_type, image_data)
            VALUES (CURRENT_TIMESTAMP, ?, ?, ?, ?, ?)""";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(SQL);
             FileInputStream txtStream = new FileInputStream(txtFile);
             FileInputStream imgStream = new FileInputStream(imageFile)) {

            pstmt.setString(1, txtFile.getName());
            pstmt.setBinaryStream(2, txtStream, (int) txtFile.length());
            pstmt.setString(3, imageFile.getName());
            pstmt.setString(4, FilenameUtils.getExtension(imageFile.getName()));
            pstmt.setBinaryStream(5, imgStream, (int) imageFile.length());

            pstmt.executeUpdate();
        } catch (SQLException | IOException e) {
            throw new RuntimeException("Insert failed", e);
        }
    }

    @Override
    public List<FileModel> getAllFiles() {
        final String SQL = "SELECT * FROM files";
        List<FileModel> files = new ArrayList<>();

        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(SQL)) {

            while (rs.next()) {
                FileModel model = new FileModel();
                model.setId(rs.getLong("id"));
                model.setCreationDate(rs.getDate("creation_date"));
                model.setFileName(rs.getString("file_name"));
                model.setFileData(rs.getBytes("file_data"));
                model.setImageName(rs.getString("image_name"));
                model.setImageType(rs.getString("image_type"));
                model.setImageData(rs.getBytes("image_data"));
                files.add(model);
            }
            return files;
        } catch (SQLException e) {
            throw new RuntimeException("Query failed", e);
        }
    }

    @Override
    public void deleteFileByName(String name) {
        final String SQL = "DELETE FROM files WHERE file_name = ? OR image_name = ?";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {

            pstmt.setString(1, name);
            pstmt.setString(2, name);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Delete failed", e);
        }
    }
}