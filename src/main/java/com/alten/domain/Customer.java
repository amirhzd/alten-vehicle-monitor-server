package com.alten.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Customer.
 */
@Entity
@Table(name = "customer")
public class Customer implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Column(name = "address")
    private String address;

    @JsonIgnore
    @OneToMany(mappedBy = "customer")
    private Set<CustomerVehicle> customerVehicles = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public Customer fullName(String fullName) {
        this.fullName = fullName;
        return this;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getAddress() {
        return address;
    }

    public Customer address(String address) {
        this.address = address;
        return this;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Set<CustomerVehicle> getCustomerVehicles() {
        return customerVehicles;
    }

    public Customer customerVehicles(Set<CustomerVehicle> customerVehicles) {
        this.customerVehicles = customerVehicles;
        return this;
    }

    public Customer addCustomerVehicle(CustomerVehicle customerVehicle) {
        this.customerVehicles.add(customerVehicle);
        customerVehicle.setCustomer(this);
        return this;
    }

    public Customer removeCustomerVehicle(CustomerVehicle customerVehicle) {
        this.customerVehicles.remove(customerVehicle);
        customerVehicle.setCustomer(null);
        return this;
    }

    public void setCustomerVehicles(Set<CustomerVehicle> customerVehicles) {
        this.customerVehicles = customerVehicles;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Customer)) {
            return false;
        }
        return id != null && id.equals(((Customer) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Customer{" +
            "id=" + getId() +
            ", fullName='" + getFullName() + "'" +
            ", address='" + getAddress() + "'" +
            "}";
    }
}
