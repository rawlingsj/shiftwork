package com.teammachine.staffrostering.web.rest;

import com.teammachine.staffrostering.ShiftworkApp;
import com.teammachine.staffrostering.domain.MinMaxContractLine;
import com.teammachine.staffrostering.repository.MinMaxContractLineRepository;

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
 * Test class for the MinMaxContractLineResource REST controller.
 *
 * @see MinMaxContractLineResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ShiftworkApp.class)
@WebAppConfiguration
@IntegrationTest
public class MinMaxContractLineResourceIntTest {


    private static final ContractLineType DEFAULT_CONTRACT_LINE_TYPE = ContractLineType.SINGLE_ASSIGNMENT_PER_DAY;
    private static final ContractLineType UPDATED_CONTRACT_LINE_TYPE = ContractLineType.TOTAL_ASSIGNMENTS;

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
    private MinMaxContractLineRepository minMaxContractLineRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restMinMaxContractLineMockMvc;

    private MinMaxContractLine minMaxContractLine;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        MinMaxContractLineResource minMaxContractLineResource = new MinMaxContractLineResource();
        ReflectionTestUtils.setField(minMaxContractLineResource, "minMaxContractLineRepository", minMaxContractLineRepository);
        this.restMinMaxContractLineMockMvc = MockMvcBuilders.standaloneSetup(minMaxContractLineResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        minMaxContractLine = new MinMaxContractLine();
        minMaxContractLine.setContractLineType(DEFAULT_CONTRACT_LINE_TYPE);
        minMaxContractLine.setMinimumEnabled(DEFAULT_MINIMUM_ENABLED);
        minMaxContractLine.setMinimumValue(DEFAULT_MINIMUM_VALUE);
        minMaxContractLine.setMinimumWeight(DEFAULT_MINIMUM_WEIGHT);
        minMaxContractLine.setMaximumEnabled(DEFAULT_MAXIMUM_ENABLED);
        minMaxContractLine.setMaximumValue(DEFAULT_MAXIMUM_VALUE);
        minMaxContractLine.setMaximumWeight(DEFAULT_MAXIMUM_WEIGHT);
    }

    @Test
    @Transactional
    public void createMinMaxContractLine() throws Exception {
        int databaseSizeBeforeCreate = minMaxContractLineRepository.findAll().size();

        // Create the MinMaxContractLine

        restMinMaxContractLineMockMvc.perform(post("/api/min-max-contract-lines")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(minMaxContractLine)))
                .andExpect(status().isCreated());

        // Validate the MinMaxContractLine in the database
        List<MinMaxContractLine> minMaxContractLines = minMaxContractLineRepository.findAll();
        assertThat(minMaxContractLines).hasSize(databaseSizeBeforeCreate + 1);
        MinMaxContractLine testMinMaxContractLine = minMaxContractLines.get(minMaxContractLines.size() - 1);
        assertThat(testMinMaxContractLine.getContractLineType()).isEqualTo(DEFAULT_CONTRACT_LINE_TYPE);
        assertThat(testMinMaxContractLine.isMinimumEnabled()).isEqualTo(DEFAULT_MINIMUM_ENABLED);
        assertThat(testMinMaxContractLine.getMinimumValue()).isEqualTo(DEFAULT_MINIMUM_VALUE);
        assertThat(testMinMaxContractLine.getMinimumWeight()).isEqualTo(DEFAULT_MINIMUM_WEIGHT);
        assertThat(testMinMaxContractLine.isMaximumEnabled()).isEqualTo(DEFAULT_MAXIMUM_ENABLED);
        assertThat(testMinMaxContractLine.getMaximumValue()).isEqualTo(DEFAULT_MAXIMUM_VALUE);
        assertThat(testMinMaxContractLine.getMaximumWeight()).isEqualTo(DEFAULT_MAXIMUM_WEIGHT);
    }

    @Test
    @Transactional
    public void getAllMinMaxContractLines() throws Exception {
        // Initialize the database
        minMaxContractLineRepository.saveAndFlush(minMaxContractLine);

        // Get all the minMaxContractLines
        restMinMaxContractLineMockMvc.perform(get("/api/min-max-contract-lines?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(minMaxContractLine.getId().intValue())))
                .andExpect(jsonPath("$.[*].contractLineType").value(hasItem(DEFAULT_CONTRACT_LINE_TYPE.toString())))
                .andExpect(jsonPath("$.[*].minimumEnabled").value(hasItem(DEFAULT_MINIMUM_ENABLED.booleanValue())))
                .andExpect(jsonPath("$.[*].minimumValue").value(hasItem(DEFAULT_MINIMUM_VALUE)))
                .andExpect(jsonPath("$.[*].minimumWeight").value(hasItem(DEFAULT_MINIMUM_WEIGHT)))
                .andExpect(jsonPath("$.[*].maximumEnabled").value(hasItem(DEFAULT_MAXIMUM_ENABLED.booleanValue())))
                .andExpect(jsonPath("$.[*].maximumValue").value(hasItem(DEFAULT_MAXIMUM_VALUE)))
                .andExpect(jsonPath("$.[*].maximumWeight").value(hasItem(DEFAULT_MAXIMUM_WEIGHT)));
    }

    @Test
    @Transactional
    public void getMinMaxContractLine() throws Exception {
        // Initialize the database
        minMaxContractLineRepository.saveAndFlush(minMaxContractLine);

        // Get the minMaxContractLine
        restMinMaxContractLineMockMvc.perform(get("/api/min-max-contract-lines/{id}", minMaxContractLine.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(minMaxContractLine.getId().intValue()))
            .andExpect(jsonPath("$.contractLineType").value(DEFAULT_CONTRACT_LINE_TYPE.toString()))
            .andExpect(jsonPath("$.minimumEnabled").value(DEFAULT_MINIMUM_ENABLED.booleanValue()))
            .andExpect(jsonPath("$.minimumValue").value(DEFAULT_MINIMUM_VALUE))
            .andExpect(jsonPath("$.minimumWeight").value(DEFAULT_MINIMUM_WEIGHT))
            .andExpect(jsonPath("$.maximumEnabled").value(DEFAULT_MAXIMUM_ENABLED.booleanValue()))
            .andExpect(jsonPath("$.maximumValue").value(DEFAULT_MAXIMUM_VALUE))
            .andExpect(jsonPath("$.maximumWeight").value(DEFAULT_MAXIMUM_WEIGHT));
    }

    @Test
    @Transactional
    public void getNonExistingMinMaxContractLine() throws Exception {
        // Get the minMaxContractLine
        restMinMaxContractLineMockMvc.perform(get("/api/min-max-contract-lines/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMinMaxContractLine() throws Exception {
        // Initialize the database
        minMaxContractLineRepository.saveAndFlush(minMaxContractLine);
        int databaseSizeBeforeUpdate = minMaxContractLineRepository.findAll().size();

        // Update the minMaxContractLine
        MinMaxContractLine updatedMinMaxContractLine = new MinMaxContractLine();
        updatedMinMaxContractLine.setId(minMaxContractLine.getId());
        updatedMinMaxContractLine.setContractLineType(UPDATED_CONTRACT_LINE_TYPE);
        updatedMinMaxContractLine.setMinimumEnabled(UPDATED_MINIMUM_ENABLED);
        updatedMinMaxContractLine.setMinimumValue(UPDATED_MINIMUM_VALUE);
        updatedMinMaxContractLine.setMinimumWeight(UPDATED_MINIMUM_WEIGHT);
        updatedMinMaxContractLine.setMaximumEnabled(UPDATED_MAXIMUM_ENABLED);
        updatedMinMaxContractLine.setMaximumValue(UPDATED_MAXIMUM_VALUE);
        updatedMinMaxContractLine.setMaximumWeight(UPDATED_MAXIMUM_WEIGHT);

        restMinMaxContractLineMockMvc.perform(put("/api/min-max-contract-lines")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedMinMaxContractLine)))
                .andExpect(status().isOk());

        // Validate the MinMaxContractLine in the database
        List<MinMaxContractLine> minMaxContractLines = minMaxContractLineRepository.findAll();
        assertThat(minMaxContractLines).hasSize(databaseSizeBeforeUpdate);
        MinMaxContractLine testMinMaxContractLine = minMaxContractLines.get(minMaxContractLines.size() - 1);
        assertThat(testMinMaxContractLine.getContractLineType()).isEqualTo(UPDATED_CONTRACT_LINE_TYPE);
        assertThat(testMinMaxContractLine.isMinimumEnabled()).isEqualTo(UPDATED_MINIMUM_ENABLED);
        assertThat(testMinMaxContractLine.getMinimumValue()).isEqualTo(UPDATED_MINIMUM_VALUE);
        assertThat(testMinMaxContractLine.getMinimumWeight()).isEqualTo(UPDATED_MINIMUM_WEIGHT);
        assertThat(testMinMaxContractLine.isMaximumEnabled()).isEqualTo(UPDATED_MAXIMUM_ENABLED);
        assertThat(testMinMaxContractLine.getMaximumValue()).isEqualTo(UPDATED_MAXIMUM_VALUE);
        assertThat(testMinMaxContractLine.getMaximumWeight()).isEqualTo(UPDATED_MAXIMUM_WEIGHT);
    }

    @Test
    @Transactional
    public void deleteMinMaxContractLine() throws Exception {
        // Initialize the database
        minMaxContractLineRepository.saveAndFlush(minMaxContractLine);
        int databaseSizeBeforeDelete = minMaxContractLineRepository.findAll().size();

        // Get the minMaxContractLine
        restMinMaxContractLineMockMvc.perform(delete("/api/min-max-contract-lines/{id}", minMaxContractLine.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<MinMaxContractLine> minMaxContractLines = minMaxContractLineRepository.findAll();
        assertThat(minMaxContractLines).hasSize(databaseSizeBeforeDelete - 1);
    }
}
