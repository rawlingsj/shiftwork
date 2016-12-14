package com.teammachine.staffrostering.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.teammachine.staffrostering.domain.ShiftDate;
import com.teammachine.staffrostering.domain.enumeration.DayOfWeek;
import com.teammachine.staffrostering.repository.ShiftDateRepository;
import com.teammachine.staffrostering.web.rest.dto.ShiftDateDTO;
import com.teammachine.staffrostering.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing ShiftDate.
 */
@RestController
@RequestMapping({"/api", "/api_basic"})
public class ShiftDateResource {

    private final Logger log = LoggerFactory.getLogger(ShiftDateResource.class);

    @Inject
    private ShiftDateRepository shiftDateRepository;

    /**
     * POST  /shift-dates : Create a new shiftDate.
     *
     * @param shiftDate the shiftDate to create
     * @return the ResponseEntity with status 201 (Created) and with body the new shiftDate, or with status 400 (Bad Request) if the shiftDate has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/shift-dates",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ShiftDateDTO> createShiftDate(@RequestBody ShiftDateDTO shiftDate) throws URISyntaxException {
        log.debug("REST request to save ShiftDate : {}", shiftDate);
        ShiftDate entity = new ShiftDate();
        generateRecords(shiftDate, entity);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("shiftDate", shiftDate.getDate().toString()))
            .body(shiftDate);
    }

    /**
     * PUT  /shift-dates : Updates an existing shiftDate.
     *
     * @param shiftDate the shiftDate to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated shiftDate,
     * or with status 400 (Bad Request) if the shiftDate is not valid,
     * or with status 500 (Internal Server Error) if the shiftDate couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/shift-dates",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ShiftDateDTO> updateShiftDate(@RequestBody ShiftDateDTO shiftDate) throws URISyntaxException {
        log.debug("REST request to update ShiftDate : {}", shiftDate);
        ShiftDate entity = new ShiftDate();
        if (shiftDate.getId() == null) {
            return createShiftDate(shiftDate);
        }
        MapDtoToEntity(entity, shiftDate);
        ShiftDate result = shiftDateRepository.save(entity);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("shiftDate", shiftDate.getId().toString()))
            .body(shiftDate);
    }

    /**
     * GET  /shift-dates : get all the shiftDates.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of shiftDates in body
     */
    @RequestMapping(value = "/shift-dates",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<ShiftDate> getAllShiftDates() {
        log.debug("REST request to get all ShiftDates");
        List<ShiftDate> shiftDates = shiftDateRepository.findAll();
        return shiftDates;
    }

    /**
     * GET  /shift-dates/:id : get the "id" shiftDate.
     *
     * @param id the id of the shiftDate to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the shiftDate, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/shift-dates/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ShiftDate> getShiftDate(@PathVariable Long id) {
        log.debug("REST request to get ShiftDate : {}", id);
        ShiftDate shiftDate = shiftDateRepository.findOne(id);
        return Optional.ofNullable(shiftDate)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /shift-dates/:id : delete the "id" shiftDate.
     *
     * @param id the id of the shiftDate to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/shift-dates/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteShiftDate(@PathVariable Long id) {
        log.debug("REST request to delete ShiftDate : {}", id);
        shiftDateRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("shiftDate", id.toString())).build();
    }

    private DayOfWeek getDayOfWeekFromDate(LocalDate date) {
        return DayOfWeek.valueOf(String.valueOf(date.getDayOfWeek()));
    }

    /**
     * Service Layer Logic
     * @param shiftDateDTO
     * @param shiftDateEntity
     */
    private void generateRecords(ShiftDateDTO shiftDateDTO, ShiftDate shiftDateEntity) {
        LocalDate date = shiftDateDTO.getDate();
        DayOfWeek[] daysOfWeek = shiftDateDTO.getDaysOfWeek();
        for (int rf = 0; rf < shiftDateDTO.getRepeatFor(); rf++) {
            for (int dayIndex = 0; dayIndex < daysOfWeek.length; dayIndex++) {
                int range = rf * 7 + (mapDayToNumber(daysOfWeek[dayIndex]) - mapDayToNumber(getDayOfWeekFromDate(date)));
                shiftDateDTO.setDate(date.plusDays(range));
                shiftDateDTO.setDayOfWeek(getDayOfWeekFromDate(shiftDateDTO.getDate()));
                MapDtoToEntity(shiftDateEntity, shiftDateDTO);
                shiftDateRepository.save(shiftDateEntity);
            }
        }


    }

    private void MapDtoToEntity(ShiftDate entity, ShiftDateDTO dto) {
        entity.setId(dto.getId());
        entity.setDayOfWeek(dto.getDayOfWeek());
        entity.setDate(dto.getDate());
        entity.setDayIndex(dto.getDayIndex());
    }

    private int mapDayToNumber(DayOfWeek dayOfWeek) {
        switch (dayOfWeek) {
            case MONDAY:
                return 1;
            case TUESDAY:
                return 2;
            case WEDNESDAY:
                return 3;
            case THURSDAY:
                return 4;
            case FRIDAY:
                return 5;
            case SATURDAY:
                return 6;
            case SUNDAY:
                return 7;
            default:
                return 0;
        }
    }


}
