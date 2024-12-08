package com.realestate.propertyapp.service;

import com.realestate.propertyapp.exception.ResourceNotFoundException;
import com.realestate.propertyapp.model.Image;
import com.realestate.propertyapp.model.Property;
import com.realestate.propertyapp.repository.ImageRepository;
import com.realestate.propertyapp.repository.PropertyRepository;

import java.io.File;
import java.nio.file.AccessDeniedException;
import java.nio.file.Files;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.beans.factory.annotation.Value;


@Service
public class PropertyService {
    private final PropertyRepository repository;
    private final ImageRepository imageRepository;

    @Value("${file.upload-dir}")
    private String uploadDir;

     public PropertyService(PropertyRepository repository, ImageRepository imageRepository) {
        this.repository = repository;
        this.imageRepository = imageRepository;
    }

    public Iterable<Property> getAllProperties() {
        return repository.findAll();
    }

    public Iterable<Property> getPropertiesByType(String type) {
        return repository.findByType(type);
    }

    public Iterable<Property> getPropertiesBySize(double size) {
        return repository.findBySizeGreaterThanEqual(size);
    }

    public Iterable<Property> getPropertiesByPrice(double price) {
        return repository.findByPriceLessThanEqual(price);
    }

    public Property getPropertyById(long id) {
        return repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Property not found with id: " + id));
    }
    
    public Property savePropertyWithImages(Property property, List<MultipartFile> files) throws IOException {
        List<Image> images = new ArrayList<>();
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // Go through the images and move the images to the right place and set the image path 
        for (MultipartFile file : files) {
            if (!file.isEmpty()) {
                try {
                    String filePath = uploadPath.resolve(file.getOriginalFilename()).toString();
                    File destFile = new File(filePath);
                    file.transferTo(destFile);
                    Image image = new Image();
                    image.setImagePath(filePath);
                    image.setProperty(property);
                    images.add(image);
                } catch (IOException e) {
                    e.printStackTrace();
                    throw new IOException("Failed to save file: " + file.getOriginalFilename(), e);
                }
            }
        }
        property.setImages(images);
        Property savedProperty = repository.save(property);
        imageRepository.saveAll(images);
        return savedProperty;
    }


    public void deleteProperty(long id, String username) throws AccessDeniedException {
        Property property = repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Property not found with id: " + id));
    
        if (!property.getAddedBy().getUsername().equals(username)) {
            throw new AccessDeniedException("You do not have permission to delete this property");
        }
    
        repository.deleteById(id);
    }
    
    public Property updateProperty(long id, Property propertyDetails, String username) throws AccessDeniedException {
        Property property = repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Property not found with id: " + id));
    
        if (!property.getAddedBy().getUsername().equals(username)) {
            throw new AccessDeniedException("You do not have permission to update this property");
        }
    
        property.setName(propertyDetails.getName());
        property.setType(propertyDetails.getType());
        property.setSize(propertyDetails.getSize());
        property.setStatus(propertyDetails.getStatus());
        property.setPrice(propertyDetails.getPrice());
        property.setLocation(propertyDetails.getLocation());
        property.setRooms(propertyDetails.getRooms());
    
        return repository.save(property);
    }
}