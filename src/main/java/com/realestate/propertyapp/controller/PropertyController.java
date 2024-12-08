package com.realestate.propertyapp.controller;

import com.realestate.propertyapp.model.Image;
import com.realestate.propertyapp.model.Property;
import com.realestate.propertyapp.model.Users;
import com.realestate.propertyapp.repository.UserRepository;
import com.realestate.propertyapp.service.PropertyService;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.security.Principal;
import java.util.List;
import java.io.File;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.multipart.MultipartFile;



@RestController
@RequestMapping("/properties")
@CrossOrigin(origins = "http://localhost:5173")
public class PropertyController {
    private final PropertyService service;
    private final UserRepository userRepository;

    public PropertyController(PropertyService service, UserRepository userRepository) {
        this.service = service;
        this.userRepository = userRepository;
    }

@GetMapping
public Iterable<Property> getProperties(
    @RequestParam(required = false) String type,
    @RequestParam(required = false) Double size,
    @RequestParam(required = false) Double price) {
    Iterable<Property> properties;
    if (type != null) {
        properties = service.getPropertiesByType(type);
    } else if (size != null) {
        properties = service.getPropertiesBySize(size);
    } else if (price != null) {
        properties = service.getPropertiesByPrice(price);
    } else {
        properties = service.getAllProperties();
    }

    // Set image image path to url 
    for (Property property : properties) {
        for (Image image : property.getImages()) {
            if (image.getImagePath() != null) {
                image.setImagePath("http://localhost:8081/images/" + new File(image.getImagePath()).getName());
            }
        }
    }

    return properties;
}


@GetMapping("/{id}")
public ResponseEntity<Property> getPropertyById(@PathVariable long id) {
    Property property = service.getPropertyById(id);
    return ResponseEntity.ok(property);
}
    
@PostMapping("/add")
    public ResponseEntity<Property> createProperty(
    @RequestPart("property") String propertyJson,
    @RequestPart("files") List<MultipartFile> files,
    Principal principal) {
    try {
        Property property = new ObjectMapper().readValue(propertyJson, Property.class);

        Users user = userRepository.findByUsername(principal.getName());
        property.setAddedBy(user);
            
        Property savedProperty = service.savePropertyWithImages(property, files);

        return ResponseEntity.ok(savedProperty);
    } catch (IOException e) {
        e.printStackTrace();
        return ResponseEntity.status(500).body(null);
    }
}

// Principal is used to verify ownership of the property
@DeleteMapping("/{id}")
public ResponseEntity<Void> deleteProperty(@PathVariable long id, Principal principal) {
    try {
        service.deleteProperty(id, principal.getName());
        return ResponseEntity.noContent().build();
    } catch (AccessDeniedException e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }
}

@PutMapping("/{id}")
public ResponseEntity<Property> updateProperty(@PathVariable long id, @RequestBody Property propertyDetails, Principal principal) {
    try {
        Property updatedProperty = service.updateProperty(id, propertyDetails, principal.getName());
        return ResponseEntity.ok(updatedProperty);
    } catch (AccessDeniedException e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }
}
}

