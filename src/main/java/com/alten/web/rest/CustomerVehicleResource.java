package com.alten.web.rest;

import com.alten.domain.CustomerVehicle;
import com.alten.repository.CustomerVehicleRepository;
import com.alten.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.alten.domain.CustomerVehicle}.
 */
@RestController
@RequestMapping("/api")
public class CustomerVehicleResource {

    private final Logger log = LoggerFactory.getLogger(CustomerVehicleResource.class);

    private static final String ENTITY_NAME = "customerVehicle";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CustomerVehicleRepository customerVehicleRepository;

    public CustomerVehicleResource(CustomerVehicleRepository customerVehicleRepository) {
        this.customerVehicleRepository = customerVehicleRepository;
    }

    /**
     * {@code POST  /customer-vehicles} : Create a new customerVehicle.
     *
     * @param customerVehicle the customerVehicle to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new customerVehicle, or with status {@code 400 (Bad Request)} if the customerVehicle has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/customer-vehicles")
    public ResponseEntity<CustomerVehicle> createCustomerVehicle(@Valid @RequestBody CustomerVehicle customerVehicle) throws URISyntaxException {
        log.debug("REST request to save CustomerVehicle : {}", customerVehicle);
        if (customerVehicle.getId() != null) {
            throw new BadRequestAlertException("A new customerVehicle cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CustomerVehicle result = customerVehicleRepository.save(customerVehicle);
        return ResponseEntity.created(new URI("/api/customer-vehicles/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /customer-vehicles} : Updates an existing customerVehicle.
     *
     * @param customerVehicle the customerVehicle to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated customerVehicle,
     * or with status {@code 400 (Bad Request)} if the customerVehicle is not valid,
     * or with status {@code 500 (Internal Server Error)} if the customerVehicle couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/customer-vehicles")
    public ResponseEntity<CustomerVehicle> updateCustomerVehicle(@Valid @RequestBody CustomerVehicle customerVehicle) throws URISyntaxException {
        log.debug("REST request to update CustomerVehicle : {}", customerVehicle);
        if (customerVehicle.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        CustomerVehicle result = customerVehicleRepository.save(customerVehicle);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, customerVehicle.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /customer-vehicles} : get all the customerVehicles.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of customerVehicles in body.
     */
    @GetMapping("/customer-vehicles")
    public List<CustomerVehicle> getAllCustomerVehicles() {
        log.debug("REST request to get all CustomerVehicles");
        return customerVehicleRepository.findAll();
    }

    /**
     * {@code GET  /customer-vehicles/:id} : get the "id" customerVehicle.
     *
     * @param id the id of the customerVehicle to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the customerVehicle, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/customer-vehicles/{id}")
    public ResponseEntity<CustomerVehicle> getCustomerVehicle(@PathVariable Long id) {
        log.debug("REST request to get CustomerVehicle : {}", id);
        Optional<CustomerVehicle> customerVehicle = customerVehicleRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(customerVehicle);
    }

    /**
     * {@code DELETE  /customer-vehicles/:id} : delete the "id" customerVehicle.
     *
     * @param id the id of the customerVehicle to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/customer-vehicles/{id}")
    public ResponseEntity<Void> deleteCustomerVehicle(@PathVariable Long id) {
        log.debug("REST request to delete CustomerVehicle : {}", id);
        customerVehicleRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
