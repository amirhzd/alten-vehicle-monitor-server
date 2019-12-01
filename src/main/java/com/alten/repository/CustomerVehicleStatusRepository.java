package com.alten.repository;

import com.alten.domain.CustomerVehicleStatus;
import com.alten.domain.enumeration.VehicleStatus;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;


/**
 * Spring Data  repository for the CustomerVehicleStatus entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CustomerVehicleStatusRepository extends JpaRepository<CustomerVehicleStatus, Long> {


    @Query(value = "select cvs0 " +
        "from CustomerVehicleStatus cvs0 join cvs0.customerVehicle cv0 join cv0.customer c0 where cvs0.id in " +
        "   (select max(cvs.id) from CustomerVehicleStatus cvs join cvs.customerVehicle cv join cv.customer c " +
        "   group by cv.id ) " +
        "and (:customerId is null or c0.id = :customerId) and (:status is null or cvs0.status = :status) ")
    List<CustomerVehicleStatus> findAllByCustomerIdAndStatus(@Param("customerId") Long customerId, @Param("status") VehicleStatus vehicleStatus);
}
