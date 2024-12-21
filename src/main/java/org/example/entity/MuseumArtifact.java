package org.example.entity;

import java.util.Date;

public class MuseumArtifact {

    private int artifactId;
    private String name;
    private String category;
    private String description;
    private Date acquisitionDate;
    private String locationInMuseum;
    private byte[] imageData;

    public MuseumArtifact() {
    }
    public MuseumArtifact(String name, String category, String description, Date acquisitionDate, String locationInMuseum, byte[] imageData) {
        this.name = name;
        this.category = category;
        this.description = description;
        this.acquisitionDate = acquisitionDate;
        this.locationInMuseum = locationInMuseum;
        this.imageData = imageData;
    }


    public int getArtifactId() {
        return artifactId;
    }

    public void setArtifactId(int artifactId){
        this.artifactId=artifactId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getAcquisitionDate() {
        return acquisitionDate;
    }

    public void setAcquisitionDate(Date acquisitionDate) {
        this.acquisitionDate = acquisitionDate;
    }

    public String getLocationInMuseum() {
        return locationInMuseum;
    }

    public void setLocationInMuseum(String locationInMuseum) {
        this.locationInMuseum = locationInMuseum;
    }
    public String toString(){
        return this.name;
    }

    public byte[] getImageData() {
        return  imageData;
    }
    public void setImageData(byte[] imageData){this.imageData = imageData;}
}
