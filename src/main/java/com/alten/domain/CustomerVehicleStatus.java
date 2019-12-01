package com.alten.domain;

import com.alten.domain.enumeration.VehicleStatus;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * A CustomerVehicleStatus.
 */
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
