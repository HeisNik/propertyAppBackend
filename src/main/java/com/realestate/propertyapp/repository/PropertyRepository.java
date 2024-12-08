package com.realestate.propertyapp.repository;

import com.realestate.propertyapp.model.Property;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PropertyRepository extends JpaRepository<Property, Long> {
    List<Property> findByType(String type);
    List<Property> findBySizeGreaterThanEqual(double size);
    List<Property> findByPriceLessThanEqual(double price);
}