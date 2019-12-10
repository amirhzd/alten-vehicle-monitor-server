package com.alten.web.rest;

import com.alten.domain.CustomerVehicleStatus;
import com.alten.domain.CustomerVehicleStatusView;
import com.alten.domain.enumeration.VehicleStatus;
import com.alten.repository.CustomerVehicleStatusRepository;
import com.alten.service.dto.CustomerVehicleStatusFilter;
import com.alten.service.WebsocketSubscribersMap;
import com.alten.web.rest.errors.BadRequestAlertException;
import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.alten.domain.CustomerVehicleStatus}.
 */
@RestController
@RequestMapping("/api")
public class CustomerVehicleStatusResource {

    private final Logger log = LoggerFactory.getLogger(CustomerVehicleStatusResource.class);

    private static final String ENTITY_NAME = "customerVehicleStatus";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CustomerVehicleStatusRepository customerVehicleStatusRepository;
    private final WebsocketSubscribersMap<CustomerVehicleStatusFilter> websocketSubscribersMap;

    public CustomerVehicleStatusResource(CustomerVehicleStatusRepository customerVehicleStatusRepository, WebsocketSubscribersMap<CustomerVehicleStatusFilter> websocketSubscribersMap) {
        this.customerVehicleStatusRepository = customerVehicleStatusRepository;
        this.websocketSubscribersMap = websocketSubscribersMap;
    }

    /**
     * {@code POST  /customer-vehicle-statuses} : Create a new customerVehicleStatus.
     *
     * @param customerVehicleStatus the customerVehicleStatus to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new customerVehicleStatus, or with status {@code 400 (Bad Request)} if the customerVehicleStatus has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/customer-vehicle-statuses")
    public ResponseEntity<CustomerVehicleStatus> createCustomerVehicleStatus(@Valid @RequestBody CustomerVehicleStatus customerVehicleStatus) throws URISyntaxException {
        log.debug("REST request to save CustomerVehicleStatus : {}", customerVehicleStatus);
        if (customerVehicleStatus.getId() != null) {
            throw new BadRequestAlertException("A new customerVehicleStatus cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CustomerVehicleStatus result = customerVehicleStatusRepository.save(customerVehicleStatus);
        return ResponseEntity.created(new URI("/api/customer-vehicle-statuses/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /customer-vehicle-statuses} : Updates an existing customerVehicleStatus.
     *
     * @param customerVehicleStatus the customerVehicleStatus to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated customerVehicleStatus,
     * or with status {@code 400 (Bad Request)} if the customerVehicleStatus is not valid,
     * or with status {@code 500 (Internal Server Error)} if the customerVehicleStatus couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/customer-vehicle-statuses")
    public ResponseEntity<CustomerVehicleStatus> updateCustomerVehicleStatus(@Valid @RequestBody CustomerVehicleStatus customerVehicleStatus) throws URISyntaxException {
        log.debug("REST request to update CustomerVehicleStatus : {}", customerVehicleStatus);
        if (customerVehicleStatus.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        CustomerVehicleStatus result = customerVehicleStatusRepository.save(customerVehicleStatus);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, customerVehicleStatus.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /customer-vehicle-statuses} : get all the customerVehicleStatuses.
     * @param vehicleStatus status
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of customerVehicleStatuses in body.
     */
    @GetMapping("/customer-vehicle-statuses")
    public List<CustomerVehicleStatusView> getAllCustomerVehicleStatusesByFilter(@RequestParam(name = "status", required = false) VehicleStatus vehicleStatus,
                                                                                   @RequestParam(name = "customerId", required = false) Long customerId) {
        log.debug("REST request to get all CustomerVehicleStatuses");
        return customerVehicleStatusRepository.findAllByCustomerIdAndStatus(customerId, vehicleStatus);
    }

    @MessageMapping("/get-vehicle-statuses")
    public void subscribeToGetAllCustomerVehicleStatusesByFilter(CustomerVehicleStatusFilter filter, @Header("simpSessionId") String sessionId) {
        websocketSubscribersMap.subscribe(sessionId, filter);
    }


    /**
     * {@code GET  /customer-vehicle-statuses/:id} : get the "id" customerVehicleStatus.
     *
     * @param id the id of the customerVehicleStatus to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the customerVehicleStatus, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/customer-vehicle-statuses/{id}")
    public ResponseEntity<CustomerVehicleStatus> getCustomerVehicleStatus(@PathVariable Long id) {
        log.debug("REST request to get CustomerVehicleStatus : {}", id);
        Optional<CustomerVehicleStatus> customerVehicleStatus = customerVehicleStatusRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(customerVehicleStatus);
    }

    /**
     * {@code DELETE  /customer-vehicle-statuses/:id} : delete the "id" customerVehicleStatus.
     *
     * @param id the id of the customerVehicleStatus to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/customer-vehicle-statuses/{id}")
    public ResponseEntity<Void> deleteCustomerVehicleStatus(@PathVariable Long id) {
        log.debug("REST request to delete CustomerVehicleStatus : {}", id);
        customerVehicleStatusRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }

}
