package com.alten.domain;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A CustomerVehicle.
 */
@Entity
@Table(name = "customer_vehicle")
public class CustomerVehicle implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "vehicle_id", nullable = false)
    private String vehicleId;

    @NotNull
    @Column(name = "registration_plate", nullable = false)
    private String registrationPlate;

    @ManyToOne
    @JsonIgnoreProperties("customerVehicles")
    private Customer customer;

    @JsonIgnore
    @OneToMany(mappedBy = "customerVehicle")
    private Set<CustomerVehicleStatus> customerVehicleStatuses = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getVehicleId() {
        return vehicleId;
    }

    public CustomerVehicle vehicleId(String vehicleId) {
        this.vehicleId = vehicleId;
        return this;
    }

    public void setVehicleId(String vehicleId) {
        this.vehicleId = vehicleId;
    }

    public String getRegistrationPlate() {
        return registrationPlate;
    }

    public CustomerVehicle registrationPlate(String registrationPlate) {
        this.registrationPlate = registrationPlate;
        return this;
    }

    public void setRegistrationPlate(String registrationPlate) {
        this.registrationPlate = registrationPlate;
    }

    public Customer getCustomer() {
        return customer;
    }

    public CustomerVehicle customer(Customer customer) {
        this.customer = customer;
        return this;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Set<CustomerVehicleStatus> getCustomerVehicleStatuses() {
        return customerVehicleStatuses;
    }

    public CustomerVehicle customerVehicleStatuses(Set<CustomerVehicleStatus> customerVehicleStatuses) {
        this.customerVehicleStatuses = customerVehicleStatuses;
        return this;
    }

    public CustomerVehicle addCustomerVehicleStatus(CustomerVehicleStatus customerVehicleStatus) {
        this.customerVehicleStatuses.add(customerVehicleStatus);
        customerVehicleStatus.setCustomerVehicle(this);
        return this;
    }

    public CustomerVehicle removeCustomerVehicleStatus(CustomerVehicleStatus customerVehicleStatus) {
        this.customerVehicleStatuses.remove(customerVehicleStatus);
        customerVehicleStatus.setCustomerVehicle(null);
        return this;
    }

    public void setCustomerVehicleStatuses(Set<CustomerVehicleStatus> customerVehicleStatuses) {
        this.customerVehicleStatuses = customerVehicleStatuses;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove


    public CustomerVehicle() {
    }

    public CustomerVehicle(@NotNull String vehicleId) {
        this.vehicleId = vehicleId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CustomerVehicle)) {
            return false;
        }
        return id != null && id.equals(((CustomerVehicle) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "CustomerVehicle{" +
            "id=" + getId() +
            ", vehicleId='" + getVehicleId() + "'" +
            ", registrationPlate='" + getRegistrationPlate() + "'" +
            "}";
    }
}
