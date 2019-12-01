package com.alten.web.rest;

import com.alten.AltenVehicleMonitorServerApp;
import com.alten.domain.CustomerVehicle;
import com.alten.repository.CustomerVehicleRepository;
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
import java.util.List;

import static com.alten.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@Link CustomerVehicleResource} REST controller.
 */
@SpringBootTest(classes = AltenVehicleMonitorServerApp.class)
public class CustomerVehicleResourceIT {

    private static final String DEFAULT_VEHICLE_ID = "AAAAAAAAAA";
    private static final String UPDATED_VEHICLE_ID = "BBBBBBBBBB";

    private static final String DEFAULT_REGISTRATION_PLATE = "AAAAAAAAAA";
    private static final String UPDATED_REGISTRATION_PLATE = "BBBBBBBBBB";

    @Autowired
    private CustomerVehicleRepository customerVehicleRepository;

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

    private MockMvc restCustomerVehicleMockMvc;

    private CustomerVehicle customerVehicle;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CustomerVehicleResource customerVehicleResource = new CustomerVehicleResource(customerVehicleRepository);
        this.restCustomerVehicleMockMvc = MockMvcBuilders.standaloneSetup(customerVehicleResource)
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
    public static CustomerVehicle createEntity(EntityManager em) {
        CustomerVehicle customerVehicle = new CustomerVehicle()
            .vehicleId(DEFAULT_VEHICLE_ID)
            .registrationPlate(DEFAULT_REGISTRATION_PLATE);
        return customerVehicle;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CustomerVehicle createUpdatedEntity(EntityManager em) {
        CustomerVehicle customerVehicle = new CustomerVehicle()
            .vehicleId(UPDATED_VEHICLE_ID)
            .registrationPlate(UPDATED_REGISTRATION_PLATE);
        return customerVehicle;
    }

    @BeforeEach
    public void initTest() {
        customerVehicle = createEntity(em);
    }

    @Test
    @Transactional
    public void createCustomerVehicle() throws Exception {
        int databaseSizeBeforeCreate = customerVehicleRepository.findAll().size();

        // Create the CustomerVehicle
        restCustomerVehicleMockMvc.perform(post("/api/customer-vehicles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(customerVehicle)))
            .andExpect(status().isCreated());

        // Validate the CustomerVehicle in the database
        List<CustomerVehicle> customerVehicleList = customerVehicleRepository.findAll();
        assertThat(customerVehicleList).hasSize(databaseSizeBeforeCreate + 1);
        CustomerVehicle testCustomerVehicle = customerVehicleList.get(customerVehicleList.size() - 1);
        assertThat(testCustomerVehicle.getVehicleId()).isEqualTo(DEFAULT_VEHICLE_ID);
        assertThat(testCustomerVehicle.getRegistrationPlate()).isEqualTo(DEFAULT_REGISTRATION_PLATE);
    }

    @Test
    @Transactional
    public void createCustomerVehicleWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = customerVehicleRepository.findAll().size();

        // Create the CustomerVehicle with an existing ID
        customerVehicle.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCustomerVehicleMockMvc.perform(post("/api/customer-vehicles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(customerVehicle)))
            .andExpect(status().isBadRequest());

        // Validate the CustomerVehicle in the database
        List<CustomerVehicle> customerVehicleList = customerVehicleRepository.findAll();
        assertThat(customerVehicleList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkVehicleIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = customerVehicleRepository.findAll().size();
        // set the field null
        customerVehicle.setVehicleId(null);

        // Create the CustomerVehicle, which fails.

        restCustomerVehicleMockMvc.perform(post("/api/customer-vehicles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(customerVehicle)))
            .andExpect(status().isBadRequest());

        List<CustomerVehicle> customerVehicleList = customerVehicleRepository.findAll();
        assertThat(customerVehicleList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkRegistrationPlateIsRequired() throws Exception {
        int databaseSizeBeforeTest = customerVehicleRepository.findAll().size();
        // set the field null
        customerVehicle.setRegistrationPlate(null);

        // Create the CustomerVehicle, which fails.

        restCustomerVehicleMockMvc.perform(post("/api/customer-vehicles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(customerVehicle)))
            .andExpect(status().isBadRequest());

        List<CustomerVehicle> customerVehicleList = customerVehicleRepository.findAll();
        assertThat(customerVehicleList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCustomerVehicles() throws Exception {
        // Initialize the database
        customerVehicleRepository.saveAndFlush(customerVehicle);

        // Get all the customerVehicleList
        restCustomerVehicleMockMvc.perform(get("/api/customer-vehicles?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(customerVehicle.getId().intValue())))
            .andExpect(jsonPath("$.[*].vehicleId").value(hasItem(DEFAULT_VEHICLE_ID.toString())))
            .andExpect(jsonPath("$.[*].registrationPlate").value(hasItem(DEFAULT_REGISTRATION_PLATE.toString())));
    }
    
    @Test
    @Transactional
    public void getCustomerVehicle() throws Exception {
        // Initialize the database
        customerVehicleRepository.saveAndFlush(customerVehicle);

        // Get the customerVehicle
        restCustomerVehicleMockMvc.perform(get("/api/customer-vehicles/{id}", customerVehicle.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(customerVehicle.getId().intValue()))
            .andExpect(jsonPath("$.vehicleId").value(DEFAULT_VEHICLE_ID.toString()))
            .andExpect(jsonPath("$.registrationPlate").value(DEFAULT_REGISTRATION_PLATE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingCustomerVehicle() throws Exception {
        // Get the customerVehicle
        restCustomerVehicleMockMvc.perform(get("/api/customer-vehicles/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCustomerVehicle() throws Exception {
        // Initialize the database
        customerVehicleRepository.saveAndFlush(customerVehicle);

        int databaseSizeBeforeUpdate = customerVehicleRepository.findAll().size();

        // Update the customerVehicle
        CustomerVehicle updatedCustomerVehicle = customerVehicleRepository.findById(customerVehicle.getId()).get();
        // Disconnect from session so that the updates on updatedCustomerVehicle are not directly saved in db
        em.detach(updatedCustomerVehicle);
        updatedCustomerVehicle
            .vehicleId(UPDATED_VEHICLE_ID)
            .registrationPlate(UPDATED_REGISTRATION_PLATE);

        restCustomerVehicleMockMvc.perform(put("/api/customer-vehicles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedCustomerVehicle)))
            .andExpect(status().isOk());

        // Validate the CustomerVehicle in the database
        List<CustomerVehicle> customerVehicleList = customerVehicleRepository.findAll();
        assertThat(customerVehicleList).hasSize(databaseSizeBeforeUpdate);
        CustomerVehicle testCustomerVehicle = customerVehicleList.get(customerVehicleList.size() - 1);
        assertThat(testCustomerVehicle.getVehicleId()).isEqualTo(UPDATED_VEHICLE_ID);
        assertThat(testCustomerVehicle.getRegistrationPlate()).isEqualTo(UPDATED_REGISTRATION_PLATE);
    }

    @Test
    @Transactional
    public void updateNonExistingCustomerVehicle() throws Exception {
        int databaseSizeBeforeUpdate = customerVehicleRepository.findAll().size();

        // Create the CustomerVehicle

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCustomerVehicleMockMvc.perform(put("/api/customer-vehicles")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(customerVehicle)))
            .andExpect(status().isBadRequest());

        // Validate the CustomerVehicle in the database
        List<CustomerVehicle> customerVehicleList = customerVehicleRepository.findAll();
        assertThat(customerVehicleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteCustomerVehicle() throws Exception {
        // Initialize the database
        customerVehicleRepository.saveAndFlush(customerVehicle);

        int databaseSizeBeforeDelete = customerVehicleRepository.findAll().size();

        // Delete the customerVehicle
        restCustomerVehicleMockMvc.perform(delete("/api/customer-vehicles/{id}", customerVehicle.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database is empty
        List<CustomerVehicle> customerVehicleList = customerVehicleRepository.findAll();
        assertThat(customerVehicleList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CustomerVehicle.class);
        CustomerVehicle customerVehicle1 = new CustomerVehicle();
        customerVehicle1.setId(1L);
        CustomerVehicle customerVehicle2 = new CustomerVehicle();
        customerVehicle2.setId(customerVehicle1.getId());
        assertThat(customerVehicle1).isEqualTo(customerVehicle2);
        customerVehicle2.setId(2L);
        assertThat(customerVehicle1).isNotEqualTo(customerVehicle2);
        customerVehicle1.setId(null);
        assertThat(customerVehicle1).isNotEqualTo(customerVehicle2);
    }
}
