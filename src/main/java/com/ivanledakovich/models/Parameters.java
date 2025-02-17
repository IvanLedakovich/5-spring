package com.ivanledakovich.models;

import java.util.ArrayList;
import java.util.List;

public class Parameters {
    private String imageFileType;
    private String imageSaveLocation;
    private List<String> textFilePaths = new ArrayList<>();

    public String getImageFileType() { return imageFileType; }
    public void setImageFileType(String imageFileType) { this.imageFileType = imageFileType; }
    public String getImageSaveLocation() { return imageSaveLocation; }
    public void setImageSaveLocation(String imageSaveLocation) { this.imageSaveLocation = imageSaveLocation; }
    public List<String> getTextFilePaths() { return textFilePaths; }
    public void setTextFilePaths(List<String> textFilePaths) { this.textFilePaths = textFilePaths; }
}