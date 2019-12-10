package com.alten.domain;

import com.alten.domain.enumeration.VehicleStatus;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * A CustomerVehicleStatus.
 */


@NamedNativeQuery(query = "select * from (" +
    "select 'NOT_CONNECTED' as status, null as timestamp, cv.vehicle_Id, cv.registration_Plate, c.full_Name, c.id as customer_Id " +
    "from customer_vehicle cv  " +
    "   join customer c on c.id = cv.customer_id " +
    "   where cv.id not in (select distinct cvsi.customer_vehicle_id from Customer_Vehicle_Status cvsi) " +
    "union all " +
    "select 'NOT_CONNECTED' as status, null as timestamp, cv.vehicle_Id, cv.registration_Plate, c.full_Name, c.id as customer_Id " +
    "from Customer_Vehicle_Status cvs " +
    "   join customer_vehicle cv on cv.id = cvs.customer_Vehicle_id  " +
    "   join customer c on c.id = cv.customer_id where cvs.timestamp < NOW() - INTERVAL 1 MINUTE " +
    "   and cvs.id in (select max(cvsi.id) from Customer_Vehicle_Status cvsi group by cvsi.customer_vehicle_id)" +
    "union all " +
    "select 'CONNECTED' as status, cvs.timestamp, cv.vehicle_Id, cv.registration_Plate, c.full_Name, c.id as customer_Id " +
    "from Customer_Vehicle_Status cvs " +
    "   join customer_vehicle cv on cv.id = cvs.customer_Vehicle_id  " +
    "   join customer c on c.id = cv.customer_id where cvs.timestamp >= NOW() - INTERVAL 1 MINUTE " +
    "   and cvs.id in (select max(cvsi.id) from Customer_Vehicle_Status cvsi group by cvsi.customer_vehicle_id)" +
    ") cvs0 where (:customerId is null or cvs0.customer_Id = :customerId) and (:status is null or cvs0.status = :status) "
    , name="VehicleStatusByCustomerIdAndStatus"
    , resultSetMapping = "VehicleStatusByCustomerIdAndStatus")


@SqlResultSetMapping(
    name="VehicleStatusByCustomerIdAndStatus",
    classes={
        @ConstructorResult(
            targetClass= com.alten.domain.CustomerVehicleStatusView.class ,
            columns={
                @ColumnResult(name="status", type = String.class),
                @ColumnResult(name="timestamp", type = LocalDateTime.class),
                @ColumnResult(name="vehicle_Id", type = String.class),
                @ColumnResult(name="registration_Plate", type = String.class),
                @ColumnResult(name="full_Name", type = String.class),
                @ColumnResult(name="customer_Id", type = Long.class)
            }
        )
    }
)
@Entity
@Table(name = "customer_vehicle_status")
public class CustomerVehicleStatus implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "timestamp")
    private LocalDateTime timestamp;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private VehicleStatus status;

    @ManyToOne
    private CustomerVehicle customerVehicle;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public CustomerVehicleStatus timestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public VehicleStatus getStatus() {
        return status;
    }

    public CustomerVehicleStatus status(VehicleStatus status) {
        this.status = status;
        return this;
    }

    public void setStatus(VehicleStatus status) {
        this.status = status;
    }

    public CustomerVehicle getCustomerVehicle() {
        return customerVehicle;
    }

    public CustomerVehicleStatus customerVehicle(CustomerVehicle customerVehicle) {
        this.customerVehicle = customerVehicle;
        return this;
    }

    public void setCustomerVehicle(CustomerVehicle customerVehicle) {
        this.customerVehicle = customerVehicle;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CustomerVehicleStatus)) {
            return false;
        }
        return id != null && id.equals(((CustomerVehicleStatus) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "CustomerVehicleStatus{" +
            "id=" + getId() +
            ", timestamp='" + getTimestamp() + "'" +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
