package com.teammachine.staffrostering.web.rest;

import com.teammachine.staffrostering.ShiftworkApp;
import com.teammachine.staffrostering.domain.BooleanContractLine;
import com.teammachine.staffrostering.repository.BooleanContractLineRepository;

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
 * Test class for the BooleanContractLineResource REST controller.
 *
 * @see BooleanContractLineResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ShiftworkApp.class)
@WebAppConfiguration
@IntegrationTest
public class BooleanContractLineResourceIntTest {


    private static final ContractLineType DEFAULT_CONTRACT_LINE_TYPE = ContractLineType.SINGLE_ASSIGNMENT_PER_DAY;
    private static final ContractLineType UPDATED_CONTRACT_LINE_TYPE = ContractLineType.TOTAL_ASSIGNMENTS;

    private static final Boolean DEFAULT_ENABLED = false;
    private static final Boolean UPDATED_ENABLED = true;

    private static final Integer DEFAULT_WEIGHT = 1;
    private static final Integer UPDATED_WEIGHT = 2;

    @Inject
    private BooleanContractLineRepository booleanContractLineRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restBooleanContractLineMockMvc;

    private BooleanContractLine booleanContractLine;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        BooleanContractLineResource booleanContractLineResource = new BooleanContractLineResource();
        ReflectionTestUtils.setField(booleanContractLineResource, "booleanContractLineRepository", booleanContractLineRepository);
        this.restBooleanContractLineMockMvc = MockMvcBuilders.standaloneSetup(booleanContractLineResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        booleanContractLine = new BooleanContractLine();
        booleanContractLine.setContractLineType(DEFAULT_CONTRACT_LINE_TYPE);
        booleanContractLine.setEnabled(DEFAULT_ENABLED);
        booleanContractLine.setWeight(DEFAULT_WEIGHT);
    }

    @Test
    @Transactional
    public void createBooleanContractLine() throws Exception {
        int databaseSizeBeforeCreate = booleanContractLineRepository.findAll().size();

        // Create the BooleanContractLine

        restBooleanContractLineMockMvc.perform(post("/api/boolean-contract-lines")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(booleanContractLine)))
                .andExpect(status().isCreated());

        // Validate the BooleanContractLine in the database
        List<BooleanContractLine> booleanContractLines = booleanContractLineRepository.findAll();
        assertThat(booleanContractLines).hasSize(databaseSizeBeforeCreate + 1);
        BooleanContractLine testBooleanContractLine = booleanContractLines.get(booleanContractLines.size() - 1);
        assertThat(testBooleanContractLine.getContractLineType()).isEqualTo(DEFAULT_CONTRACT_LINE_TYPE);
        assertThat(testBooleanContractLine.isEnabled()).isEqualTo(DEFAULT_ENABLED);
        assertThat(testBooleanContractLine.getWeight()).isEqualTo(DEFAULT_WEIGHT);
    }

    @Test
    @Transactional
    public void getAllBooleanContractLines() throws Exception {
        // Initialize the database
        booleanContractLineRepository.saveAndFlush(booleanContractLine);

        // Get all the booleanContractLines
        restBooleanContractLineMockMvc.perform(get("/api/boolean-contract-lines?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(booleanContractLine.getId().intValue())))
                .andExpect(jsonPath("$.[*].contractLineType").value(hasItem(DEFAULT_CONTRACT_LINE_TYPE.toString())))
                .andExpect(jsonPath("$.[*].enabled").value(hasItem(DEFAULT_ENABLED.booleanValue())))
                .andExpect(jsonPath("$.[*].weight").value(hasItem(DEFAULT_WEIGHT)));
    }

    @Test
    @Transactional
    public void getBooleanContractLine() throws Exception {
        // Initialize the database
        booleanContractLineRepository.saveAndFlush(booleanContractLine);

        // Get the booleanContractLine
        restBooleanContractLineMockMvc.perform(get("/api/boolean-contract-lines/{id}", booleanContractLine.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(booleanContractLine.getId().intValue()))
            .andExpect(jsonPath("$.contractLineType").value(DEFAULT_CONTRACT_LINE_TYPE.toString()))
            .andExpect(jsonPath("$.enabled").value(DEFAULT_ENABLED.booleanValue()))
            .andExpect(jsonPath("$.weight").value(DEFAULT_WEIGHT));
    }

    @Test
    @Transactional
    public void getNonExistingBooleanContractLine() throws Exception {
        // Get the booleanContractLine
        restBooleanContractLineMockMvc.perform(get("/api/boolean-contract-lines/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBooleanContractLine() throws Exception {
        // Initialize the database
        booleanContractLineRepository.saveAndFlush(booleanContractLine);
        int databaseSizeBeforeUpdate = booleanContractLineRepository.findAll().size();

        // Update the booleanContractLine
        BooleanContractLine updatedBooleanContractLine = new BooleanContractLine();
        updatedBooleanContractLine.setId(booleanContractLine.getId());
        updatedBooleanContractLine.setContractLineType(UPDATED_CONTRACT_LINE_TYPE);
        updatedBooleanContractLine.setEnabled(UPDATED_ENABLED);
        updatedBooleanContractLine.setWeight(UPDATED_WEIGHT);

        restBooleanContractLineMockMvc.perform(put("/api/boolean-contract-lines")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedBooleanContractLine)))
                .andExpect(status().isOk());

        // Validate the BooleanContractLine in the database
        List<BooleanContractLine> booleanContractLines = booleanContractLineRepository.findAll();
        assertThat(booleanContractLines).hasSize(databaseSizeBeforeUpdate);
        BooleanContractLine testBooleanContractLine = booleanContractLines.get(booleanContractLines.size() - 1);
        assertThat(testBooleanContractLine.getContractLineType()).isEqualTo(UPDATED_CONTRACT_LINE_TYPE);
        assertThat(testBooleanContractLine.isEnabled()).isEqualTo(UPDATED_ENABLED);
        assertThat(testBooleanContractLine.getWeight()).isEqualTo(UPDATED_WEIGHT);
    }

    @Test
    @Transactional
    public void deleteBooleanContractLine() throws Exception {
        // Initialize the database
        booleanContractLineRepository.saveAndFlush(booleanContractLine);
        int databaseSizeBeforeDelete = booleanContractLineRepository.findAll().size();

        // Get the booleanContractLine
        restBooleanContractLineMockMvc.perform(delete("/api/boolean-contract-lines/{id}", booleanContractLine.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<BooleanContractLine> booleanContractLines = booleanContractLineRepository.findAll();
        assertThat(booleanContractLines).hasSize(databaseSizeBeforeDelete - 1);
    }
}
