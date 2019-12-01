package com.alten.repository;

import com.alten.domain.CustomerVehicle;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * Spring Data  repository for the CustomerVehicle entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CustomerVehicleRepository extends JpaRepository<CustomerVehicle, Long> {
     List<CustomerVehicle> findAllByVehicleId(String vehicleId);
}
