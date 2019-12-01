package com.alten.web.rest;

import com.alten.AltenVehicleMonitorServerApp;
import com.alten.domain.CustomerVehicleStatus;
import com.alten.repository.CustomerVehicleStatusRepository;
import com.alten.web.rest.errors.ExceptionTranslator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.List;

import static com.alten.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.alten.domain.enumeration.VehicleStatus;
/**
 * Integration tests for the {@Link CustomerVehicleStatusResource} REST controller.
 */
@SpringBootTest(classes = AltenVehicleMonitorServerApp.class)
public class CustomerVehicleStatusResourceIT {

    private static final LocalDateTime DEFAULT_TIMESTAMP = LocalDateTime.ofEpochSecond(0L, 0, ZoneOffset.UTC);
    private static final LocalDateTime UPDATED_TIMESTAMP = LocalDateTime.now(ZoneId.systemDefault());

    private static final VehicleStatus DEFAULT_STATUS = VehicleStatus.CONNECTED;
    private static final VehicleStatus UPDATED_STATUS = VehicleStatus.NOT_CONNECTED;

    @Autowired
    private CustomerVehicleStatusRepository customerVehicleStatusRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restCustomerVehicleStatusMockMvc;

    private CustomerVehicleStatus customerVehicleStatus;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CustomerVehicleStatusResource customerVehicleStatusResource = new CustomerVehicleStatusResource(customerVehicleStatusRepository, null);
        this.restCustomerVehicleStatusMockMvc = MockMvcBuilders.standaloneSetup(customerVehicleStatusResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CustomerVehicleStatus createEntity(EntityManager em) {
        CustomerVehicleStatus customerVehicleStatus = new CustomerVehicleStatus()
            .timestamp(DEFAULT_TIMESTAMP)
            .status(DEFAULT_STATUS);
        return customerVehicleStatus;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CustomerVehicleStatus createUpdatedEntity(EntityManager em) {
        CustomerVehicleStatus customerVehicleStatus = new CustomerVehicleStatus()
            .timestamp(UPDATED_TIMESTAMP)
            .status(UPDATED_STATUS);
        return customerVehicleStatus;
    }

    @BeforeEach
    public void initTest() {
        customerVehicleStatus = createEntity(em);
    }

    @Test
    @Transactional
    public void createCustomerVehicleStatus() throws Exception {
        int databaseSizeBeforeCreate = customerVehicleStatusRepository.findAll().size();

        // Create the CustomerVehicleStatus
        restCustomerVehicleStatusMockMvc.perform(post("/api/customer-vehicle-statuses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(customerVehicleStatus)))
            .andExpect(status().isCreated());

        // Validate the CustomerVehicleStatus in the database
        List<CustomerVehicleStatus> customerVehicleStatusList = customerVehicleStatusRepository.findAll();
        assertThat(customerVehicleStatusList).hasSize(databaseSizeBeforeCreate + 1);
        CustomerVehicleStatus testCustomerVehicleStatus = customerVehicleStatusList.get(customerVehicleStatusList.size() - 1);
        assertThat(testCustomerVehicleStatus.getTimestamp()).isEqualTo(DEFAULT_TIMESTAMP);
        assertThat(testCustomerVehicleStatus.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    public void createCustomerVehicleStatusWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = customerVehicleStatusRepository.findAll().size();

        // Create the CustomerVehicleStatus with an existing ID
        customerVehicleStatus.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCustomerVehicleStatusMockMvc.perform(post("/api/customer-vehicle-statuses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(customerVehicleStatus)))
            .andExpect(status().isBadRequest());

        // Validate the CustomerVehicleStatus in the database
        List<CustomerVehicleStatus> customerVehicleStatusList = customerVehicleStatusRepository.findAll();
        assertThat(customerVehicleStatusList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = customerVehicleStatusRepository.findAll().size();
        // set the field null
        customerVehicleStatus.setStatus(null);

        // Create the CustomerVehicleStatus, which fails.

        restCustomerVehicleStatusMockMvc.perform(post("/api/customer-vehicle-statuses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(customerVehicleStatus)))
            .andExpect(status().isBadRequest());

        List<CustomerVehicleStatus> customerVehicleStatusList = customerVehicleStatusRepository.findAll();
        assertThat(customerVehicleStatusList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCustomerVehicleStatuses() throws Exception {
        // Initialize the database
        customerVehicleStatusRepository.saveAndFlush(customerVehicleStatus);

        // Get all the customerVehicleStatusList
        restCustomerVehicleStatusMockMvc.perform(get("/api/customer-vehicle-statuses?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(customerVehicleStatus.getId().intValue())))
            .andExpect(jsonPath("$.[*].timestamp").value(hasItem(DEFAULT_TIMESTAMP.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }
    
    @Test
    @Transactional
    public void getCustomerVehicleStatus() throws Exception {
        // Initialize the database
        customerVehicleStatusRepository.saveAndFlush(customerVehicleStatus);

        // Get the customerVehicleStatus
        restCustomerVehicleStatusMockMvc.perform(get("/api/customer-vehicle-statuses/{id}", customerVehicleStatus.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(customerVehicleStatus.getId().intValue()))
            .andExpect(jsonPath("$.timestamp").value(DEFAULT_TIMESTAMP.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingCustomerVehicleStatus() throws Exception {
        // Get the customerVehicleStatus
        restCustomerVehicleStatusMockMvc.perform(get("/api/customer-vehicle-statuses/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCustomerVehicleStatus() throws Exception {
        // Initialize the database
        customerVehicleStatusRepository.saveAndFlush(customerVehicleStatus);

        int databaseSizeBeforeUpdate = customerVehicleStatusRepository.findAll().size();

        // Update the customerVehicleStatus
        CustomerVehicleStatus updatedCustomerVehicleStatus = customerVehicleStatusRepository.findById(customerVehicleStatus.getId()).get();
        // Disconnect from session so that the updates on updatedCustomerVehicleStatus are not directly saved in db
        em.detach(updatedCustomerVehicleStatus);
        updatedCustomerVehicleStatus
            .timestamp(UPDATED_TIMESTAMP)
            .status(UPDATED_STATUS);

        restCustomerVehicleStatusMockMvc.perform(put("/api/customer-vehicle-statuses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedCustomerVehicleStatus)))
            .andExpect(status().isOk());

        // Validate the CustomerVehicleStatus in the database
        List<CustomerVehicleStatus> customerVehicleStatusList = customerVehicleStatusRepository.findAll();
        assertThat(customerVehicleStatusList).hasSize(databaseSizeBeforeUpdate);
        CustomerVehicleStatus testCustomerVehicleStatus = customerVehicleStatusList.get(customerVehicleStatusList.size() - 1);
        assertThat(testCustomerVehicleStatus.getTimestamp()).isEqualTo(UPDATED_TIMESTAMP);
        assertThat(testCustomerVehicleStatus.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void updateNonExistingCustomerVehicleStatus() throws Exception {
        int databaseSizeBeforeUpdate = customerVehicleStatusRepository.findAll().size();

        // Create the CustomerVehicleStatus

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCustomerVehicleStatusMockMvc.perform(put("/api/customer-vehicle-statuses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(customerVehicleStatus)))
            .andExpect(status().isBadRequest());

        // Validate the CustomerVehicleStatus in the database
        List<CustomerVehicleStatus> customerVehicleStatusList = customerVehicleStatusRepository.findAll();
        assertThat(customerVehicleStatusList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteCustomerVehicleStatus() throws Exception {
        // Initialize the database
        customerVehicleStatusRepository.saveAndFlush(customerVehicleStatus);

        int databaseSizeBeforeDelete = customerVehicleStatusRepository.findAll().size();

        // Delete the customerVehicleStatus
        restCustomerVehicleStatusMockMvc.perform(delete("/api/customer-vehicle-statuses/{id}", customerVehicleStatus.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database is empty
        List<CustomerVehicleStatus> customerVehicleStatusList = customerVehicleStatusRepository.findAll();
        assertThat(customerVehicleStatusList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CustomerVehicleStatus.class);
        CustomerVehicleStatus customerVehicleStatus1 = new CustomerVehicleStatus();
        customerVehicleStatus1.setId(1L);
        CustomerVehicleStatus customerVehicleStatus2 = new CustomerVehicleStatus();
        customerVehicleStatus2.setId(customerVehicleStatus1.getId());
        assertThat(customerVehicleStatus1).isEqualTo(customerVehicleStatus2);
        customerVehicleStatus2.setId(2L);
        assertThat(customerVehicleStatus1).isNotEqualTo(customerVehicleStatus2);
        customerVehicleStatus1.setId(null);
        assertThat(customerVehicleStatus1).isNotEqualTo(customerVehicleStatus2);
    }
}
