package com.teammachine.staffrostering.web.rest;

import com.teammachine.staffrostering.ShiftworkApp;
import com.teammachine.staffrostering.domain.ContractLine;
import com.teammachine.staffrostering.repository.ContractLineRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.teammachine.staffrostering.domain.enumeration.ContractLineType;

/**
 * Test class for the ContractLineResource REST controller.
 *
 * @see ContractLineResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ShiftworkApp.class)
@WebAppConfiguration
@IntegrationTest
public class ContractLineResourceIntTest {


    private static final ContractLineType DEFAULT_CONTRACT_LINE_TYPE = ContractLineType.SINGLE_ASSIGNMENT_PER_DAY;
    private static final ContractLineType UPDATED_CONTRACT_LINE_TYPE = ContractLineType.TOTAL_ASSIGNMENTS;

    private static final Boolean DEFAULT_ENABLED = false;
    private static final Boolean UPDATED_ENABLED = true;

    private static final Integer DEFAULT_WEIGHT = 1;
    private static final Integer UPDATED_WEIGHT = 2;

    private static final Boolean DEFAULT_MINIMUM_ENABLED = false;
    private static final Boolean UPDATED_MINIMUM_ENABLED = true;

    private static final Integer DEFAULT_MINIMUM_VALUE = 1;
    private static final Integer UPDATED_MINIMUM_VALUE = 2;

    private static final Integer DEFAULT_MINIMUM_WEIGHT = 1;
    private static final Integer UPDATED_MINIMUM_WEIGHT = 2;

    private static final Boolean DEFAULT_MAXIMUM_ENABLED = false;
    private static final Boolean UPDATED_MAXIMUM_ENABLED = true;

    private static final Integer DEFAULT_MAXIMUM_VALUE = 1;
    private static final Integer UPDATED_MAXIMUM_VALUE = 2;

    private static final Integer DEFAULT_MAXIMUM_WEIGHT = 1;
    private static final Integer UPDATED_MAXIMUM_WEIGHT = 2;

    @Inject
    private ContractLineRepository contractLineRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restContractLineMockMvc;

    private ContractLine contractLine;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ContractLineResource contractLineResource = new ContractLineResource();
        ReflectionTestUtils.setField(contractLineResource, "contractLineRepository", contractLineRepository);
        this.restContractLineMockMvc = MockMvcBuilders.standaloneSetup(contractLineResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        contractLine = new ContractLine();
        contractLine.setContractLineType(DEFAULT_CONTRACT_LINE_TYPE);
        contractLine.setEnabled(DEFAULT_ENABLED);
        contractLine.setWeight(DEFAULT_WEIGHT);
        contractLine.setMinimumEnabled(DEFAULT_MINIMUM_ENABLED);
        contractLine.setMinimumValue(DEFAULT_MINIMUM_VALUE);
        contractLine.setMinimumWeight(DEFAULT_MINIMUM_WEIGHT);
        contractLine.setMaximumEnabled(DEFAULT_MAXIMUM_ENABLED);
        contractLine.setMaximumValue(DEFAULT_MAXIMUM_VALUE);
        contractLine.setMaximumWeight(DEFAULT_MAXIMUM_WEIGHT);
    }

    @Test
    @Transactional
    public void createContractLine() throws Exception {
        int databaseSizeBeforeCreate = contractLineRepository.findAll().size();

        // Create the ContractLine

        restContractLineMockMvc.perform(post("/api/contract-lines")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(contractLine)))
                .andExpect(status().isCreated());

        // Validate the ContractLine in the database
        List<ContractLine> contractLines = contractLineRepository.findAll();
        assertThat(contractLines).hasSize(databaseSizeBeforeCreate + 1);
        ContractLine testContractLine = contractLines.get(contractLines.size() - 1);
        assertThat(testContractLine.getContractLineType()).isEqualTo(DEFAULT_CONTRACT_LINE_TYPE);
        assertThat(testContractLine.isEnabled()).isEqualTo(DEFAULT_ENABLED);
        assertThat(testContractLine.getWeight()).isEqualTo(DEFAULT_WEIGHT);
        assertThat(testContractLine.isMinimumEnabled()).isEqualTo(DEFAULT_MINIMUM_ENABLED);
        assertThat(testContractLine.getMinimumValue()).isEqualTo(DEFAULT_MINIMUM_VALUE);
        assertThat(testContractLine.getMinimumWeight()).isEqualTo(DEFAULT_MINIMUM_WEIGHT);
        assertThat(testContractLine.isMaximumEnabled()).isEqualTo(DEFAULT_MAXIMUM_ENABLED);
        assertThat(testContractLine.getMaximumValue()).isEqualTo(DEFAULT_MAXIMUM_VALUE);
        assertThat(testContractLine.getMaximumWeight()).isEqualTo(DEFAULT_MAXIMUM_WEIGHT);
    }

    @Test
    @Transactional
    public void getAllContractLines() throws Exception {
        // Initialize the database
        contractLineRepository.saveAndFlush(contractLine);

        // Get all the contractLines
        restContractLineMockMvc.perform(get("/api/contract-lines?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(contractLine.getId().intValue())))
                .andExpect(jsonPath("$.[*].contractLineType").value(hasItem(DEFAULT_CONTRACT_LINE_TYPE.toString())))
                .andExpect(jsonPath("$.[*].enabled").value(hasItem(DEFAULT_ENABLED.booleanValue())))
                .andExpect(jsonPath("$.[*].weight").value(hasItem(DEFAULT_WEIGHT)))
                .andExpect(jsonPath("$.[*].minimumEnabled").value(hasItem(DEFAULT_MINIMUM_ENABLED.booleanValue())))
                .andExpect(jsonPath("$.[*].minimumValue").value(hasItem(DEFAULT_MINIMUM_VALUE)))
                .andExpect(jsonPath("$.[*].minimumWeight").value(hasItem(DEFAULT_MINIMUM_WEIGHT)))
                .andExpect(jsonPath("$.[*].maximumEnabled").value(hasItem(DEFAULT_MAXIMUM_ENABLED.booleanValue())))
                .andExpect(jsonPath("$.[*].maximumValue").value(hasItem(DEFAULT_MAXIMUM_VALUE)))
                .andExpect(jsonPath("$.[*].maximumWeight").value(hasItem(DEFAULT_MAXIMUM_WEIGHT)));
    }

    @Test
    @Transactional
    public void getContractLine() throws Exception {
        // Initialize the database
        contractLineRepository.saveAndFlush(contractLine);

        // Get the contractLine
        restContractLineMockMvc.perform(get("/api/contract-lines/{id}", contractLine.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(contractLine.getId().intValue()))
            .andExpect(jsonPath("$.contractLineType").value(DEFAULT_CONTRACT_LINE_TYPE.toString()))
            .andExpect(jsonPath("$.enabled").value(DEFAULT_ENABLED.booleanValue()))
            .andExpect(jsonPath("$.weight").value(DEFAULT_WEIGHT))
            .andExpect(jsonPath("$.minimumEnabled").value(DEFAULT_MINIMUM_ENABLED.booleanValue()))
            .andExpect(jsonPath("$.minimumValue").value(DEFAULT_MINIMUM_VALUE))
            .andExpect(jsonPath("$.minimumWeight").value(DEFAULT_MINIMUM_WEIGHT))
            .andExpect(jsonPath("$.maximumEnabled").value(DEFAULT_MAXIMUM_ENABLED.booleanValue()))
            .andExpect(jsonPath("$.maximumValue").value(DEFAULT_MAXIMUM_VALUE))
            .andExpect(jsonPath("$.maximumWeight").value(DEFAULT_MAXIMUM_WEIGHT));
    }

    @Test
    @Transactional
    public void getNonExistingContractLine() throws Exception {
        // Get the contractLine
        restContractLineMockMvc.perform(get("/api/contract-lines/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateContractLine() throws Exception {
        // Initialize the database
        contractLineRepository.saveAndFlush(contractLine);
        int databaseSizeBeforeUpdate = contractLineRepository.findAll().size();

        // Update the contractLine
        ContractLine updatedContractLine = new ContractLine();
        updatedContractLine.setId(contractLine.getId());
        updatedContractLine.setContractLineType(UPDATED_CONTRACT_LINE_TYPE);
        updatedContractLine.setEnabled(UPDATED_ENABLED);
        updatedContractLine.setWeight(UPDATED_WEIGHT);
        updatedContractLine.setMinimumEnabled(UPDATED_MINIMUM_ENABLED);
        updatedContractLine.setMinimumValue(UPDATED_MINIMUM_VALUE);
        updatedContractLine.setMinimumWeight(UPDATED_MINIMUM_WEIGHT);
        updatedContractLine.setMaximumEnabled(UPDATED_MAXIMUM_ENABLED);
        updatedContractLine.setMaximumValue(UPDATED_MAXIMUM_VALUE);
        updatedContractLine.setMaximumWeight(UPDATED_MAXIMUM_WEIGHT);

        restContractLineMockMvc.perform(put("/api/contract-lines")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedContractLine)))
                .andExpect(status().isOk());

        // Validate the ContractLine in the database
        List<ContractLine> contractLines = contractLineRepository.findAll();
        assertThat(contractLines).hasSize(databaseSizeBeforeUpdate);
        ContractLine testContractLine = contractLines.get(contractLines.size() - 1);
        assertThat(testContractLine.getContractLineType()).isEqualTo(UPDATED_CONTRACT_LINE_TYPE);
        assertThat(testContractLine.isEnabled()).isEqualTo(UPDATED_ENABLED);
        assertThat(testContractLine.getWeight()).isEqualTo(UPDATED_WEIGHT);
        assertThat(testContractLine.isMinimumEnabled()).isEqualTo(UPDATED_MINIMUM_ENABLED);
        assertThat(testContractLine.getMinimumValue()).isEqualTo(UPDATED_MINIMUM_VALUE);
        assertThat(testContractLine.getMinimumWeight()).isEqualTo(UPDATED_MINIMUM_WEIGHT);
        assertThat(testContractLine.isMaximumEnabled()).isEqualTo(UPDATED_MAXIMUM_ENABLED);
        assertThat(testContractLine.getMaximumValue()).isEqualTo(UPDATED_MAXIMUM_VALUE);
        assertThat(testContractLine.getMaximumWeight()).isEqualTo(UPDATED_MAXIMUM_WEIGHT);
    }

    @Test
    @Transactional
    public void deleteContractLine() throws Exception {
        // Initialize the database
        contractLineRepository.saveAndFlush(contractLine);
        int databaseSizeBeforeDelete = contractLineRepository.findAll().size();

        // Get the contractLine
        restContractLineMockMvc.perform(delete("/api/contract-lines/{id}", contractLine.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<ContractLine> contractLines = contractLineRepository.findAll();
        assertThat(contractLines).hasSize(databaseSizeBeforeDelete - 1);
    }
}
