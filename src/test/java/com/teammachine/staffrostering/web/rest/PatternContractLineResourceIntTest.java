package com.teammachine.staffrostering.web.rest;

import com.teammachine.staffrostering.ShiftworkApp;
import com.teammachine.staffrostering.domain.PatternContractLine;
import com.teammachine.staffrostering.repository.PatternContractLineRepository;

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


/**
 * Test class for the PatternContractLineResource REST controller.
 *
 * @see PatternContractLineResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ShiftworkApp.class)
@WebAppConfiguration
@IntegrationTest
public class PatternContractLineResourceIntTest {


    @Inject
    private PatternContractLineRepository patternContractLineRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restPatternContractLineMockMvc;

    private PatternContractLine patternContractLine;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        PatternContractLineResource patternContractLineResource = new PatternContractLineResource();
        ReflectionTestUtils.setField(patternContractLineResource, "patternContractLineRepository", patternContractLineRepository);
        this.restPatternContractLineMockMvc = MockMvcBuilders.standaloneSetup(patternContractLineResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        patternContractLine = new PatternContractLine();
    }

    @Test
    @Transactional
    public void createPatternContractLine() throws Exception {
        int databaseSizeBeforeCreate = patternContractLineRepository.findAll().size();

        // Create the PatternContractLine

        restPatternContractLineMockMvc.perform(post("/api/pattern-contract-lines")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(patternContractLine)))
                .andExpect(status().isCreated());

        // Validate the PatternContractLine in the database
        List<PatternContractLine> patternContractLines = patternContractLineRepository.findAll();
        assertThat(patternContractLines).hasSize(databaseSizeBeforeCreate + 1);
        PatternContractLine testPatternContractLine = patternContractLines.get(patternContractLines.size() - 1);
    }

    @Test
    @Transactional
    public void getAllPatternContractLines() throws Exception {
        // Initialize the database
        patternContractLineRepository.saveAndFlush(patternContractLine);

        // Get all the patternContractLines
        restPatternContractLineMockMvc.perform(get("/api/pattern-contract-lines?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(patternContractLine.getId().intValue())));
    }

    @Test
    @Transactional
    public void getPatternContractLine() throws Exception {
        // Initialize the database
        patternContractLineRepository.saveAndFlush(patternContractLine);

        // Get the patternContractLine
        restPatternContractLineMockMvc.perform(get("/api/pattern-contract-lines/{id}", patternContractLine.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(patternContractLine.getId().intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingPatternContractLine() throws Exception {
        // Get the patternContractLine
        restPatternContractLineMockMvc.perform(get("/api/pattern-contract-lines/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePatternContractLine() throws Exception {
        // Initialize the database
        patternContractLineRepository.saveAndFlush(patternContractLine);
        int databaseSizeBeforeUpdate = patternContractLineRepository.findAll().size();

        // Update the patternContractLine
        PatternContractLine updatedPatternContractLine = new PatternContractLine();
        updatedPatternContractLine.setId(patternContractLine.getId());

        restPatternContractLineMockMvc.perform(put("/api/pattern-contract-lines")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedPatternContractLine)))
                .andExpect(status().isOk());

        // Validate the PatternContractLine in the database
        List<PatternContractLine> patternContractLines = patternContractLineRepository.findAll();
        assertThat(patternContractLines).hasSize(databaseSizeBeforeUpdate);
        PatternContractLine testPatternContractLine = patternContractLines.get(patternContractLines.size() - 1);
    }

    @Test
    @Transactional
    public void deletePatternContractLine() throws Exception {
        // Initialize the database
        patternContractLineRepository.saveAndFlush(patternContractLine);
        int databaseSizeBeforeDelete = patternContractLineRepository.findAll().size();

        // Get the patternContractLine
        restPatternContractLineMockMvc.perform(delete("/api/pattern-contract-lines/{id}", patternContractLine.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<PatternContractLine> patternContractLines = patternContractLineRepository.findAll();
        assertThat(patternContractLines).hasSize(databaseSizeBeforeDelete - 1);
    }
}
