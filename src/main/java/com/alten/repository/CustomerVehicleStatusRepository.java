package com.alten.repository;

import com.alten.domain.CustomerVehicleStatus;
import com.alten.domain.CustomerVehicleStatusView;
import com.alten.domain.enumeration.VehicleStatus;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import java.util.List;


/**
 * Spring Data  repository for the CustomerVehicleStatus entity.
 */
@Repository
public interface CustomerVehicleStatusRepository extends JpaRepository<CustomerVehicleStatus, Long> {


    @Query(name = "VehicleStatusByCustomerIdAndStatus", nativeQuery = true)
    List<CustomerVehicleStatusView> findAllByCustomerIdAndStatus(@Param("customerId") Long customerId, @Param("status") VehicleStatus vehicleStatus);

}
