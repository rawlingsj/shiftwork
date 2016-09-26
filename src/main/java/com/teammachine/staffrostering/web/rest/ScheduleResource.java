package com.teammachine.staffrostering.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.teammachine.staffrostering.domain.ShiftAssignment;
import com.teammachine.staffrostering.domain.ShiftDate;
import com.teammachine.staffrostering.domain.ShiftType;
import com.teammachine.staffrostering.domain.Task;
import com.teammachine.staffrostering.repository.ShiftAssignmentRepository;
import com.teammachine.staffrostering.repository.ShiftDateRepository;
import com.teammachine.staffrostering.repository.ShiftTypeRepository;
import com.teammachine.staffrostering.web.rest.errors.CustomParameterizedException;
import com.teammachine.staffrostering.web.rest.errors.ErrorConstants;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

/**
 * REST controller for managing Schedule.
 */
@RestController
@RequestMapping({"/api", "/api_basic"})
public class ScheduleResource {

    private final Logger log = LoggerFactory.getLogger(ScheduleResource.class);

    @Inject
    private ShiftTypeRepository shiftTypeRepository;
    @Inject
    private ShiftDateRepository shiftDateRepository;
    @Inject
    private ShiftAssignmentRepository shiftAssignmentRepository;

    @RequestMapping(value = "/schedules",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Timed
    public List<ShiftAssignment> getShiftAssignments(@RequestParam(name = "shiftDate", required = true) Long targetDate) {
        log.debug("REST request to get daily schedule");
        LocalDate targetLocalDate = Instant.ofEpochMilli(targetDate).atZone(ZoneId.systemDefault()).toLocalDate();
        return shiftAssignmentRepository.findAllOnDate(targetLocalDate).stream()
            .sorted(Comparator.<ShiftAssignment, Integer>comparing(sha -> sha.getShift().getShiftType().getIndex()).thenComparing(ShiftAssignment::getIndexInShift))
            .collect(Collectors.toList());
    }

    @RequestMapping(value = "/schedules",
        method = RequestMethod.GET,
        produces = "application/vnd.ms-excel"
    )
    public void exportToExcelSpreadSheet(@RequestParam(name = "shiftDate", required = true) Long shiftDateId, HttpServletResponse response) throws Throwable {
        ShiftDate shiftDate = findShiftDateOrThrowException(shiftDateId);
        List<ShiftAssignment> effectiveShiftAssignments = getShiftAssignments(shiftDate);
        SXSSFWorkbook workbook = new SXSSFWorkbook();
        SXSSFSheet sheet = workbook.createSheet();
        Map<ShiftType, List<Integer>> shiftTypeColumns = prepareSheet(sheet);
        fillOutSheet(effectiveShiftAssignments, workbook, sheet, shiftTypeColumns);
        sendWorkbookInResponse(response, workbook, "Schedule-" + shiftDate.getDateString() + ".xlsx");
    }

    private ShiftDate findShiftDateOrThrowException(Long shiftDateId) {
        ShiftDate shiftDate = shiftDateRepository.findOne(shiftDateId);
        if (shiftDate == null) {
            throw new CustomParameterizedException(ErrorConstants.ERR_NO_SUCH_SHIFT_DATE, "" + shiftDateId);
        }
        return shiftDate;
    }

    private List<ShiftAssignment> getShiftAssignments(ShiftDate shiftDate) {
        return shiftAssignmentRepository.findAllForShiftDate(shiftDate).stream().sorted(byShiftTypeAndIndex()).collect(Collectors.toList());
    }

    private Comparator<ShiftAssignment> byShiftTypeAndIndex() {
        return Comparator.<ShiftAssignment, Integer>comparing(sha -> sha.getShift().getShiftType().getIndex())
            .thenComparing(ShiftAssignment::getIndexInShift);
    }

    private void sendWorkbookInResponse(HttpServletResponse response, SXSSFWorkbook workbook, String fileName) {
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
        try {
            workbook.write(response.getOutputStream());
            workbook.dispose();
        } catch (IOException e) {
            e.printStackTrace();
            throw new CustomParameterizedException(ErrorConstants.ERR_IO);
        }
    }

    /**
     * Generate column header (staff name and shift type time 30 minutes per increment)
     */
    private Map<ShiftType, List<Integer>> prepareSheet(SXSSFSheet sheet) throws ParseException {
        Row header = sheet.createRow(0);
        Cell cell = header.createCell(0);
        cell.setCellValue("Staff Name");
        Map<ShiftType, List<Integer>> shiftTypeColumns = new HashMap<>();
        Map<String, Integer> mapTimeCellIndex = new HashMap<>();
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        SimpleDateFormat outputTimeFormat = new SimpleDateFormat("HH:mm");
        Calendar calendar = Calendar.getInstance();
        Integer latestCellIndex = 1;
        for (ShiftType shiftType : shiftTypeRepository.findAll()) {
            List<Integer> listCellsIndex = new ArrayList<>();
            Date startTime = timeFormat.parse(shiftType.getStartTimeString());
            Date endTime = timeFormat.parse(shiftType.getEndTimeString());
            calendar.setTime(endTime);
            if (calendar.getTime().before(startTime)) {
                calendar.add(Calendar.DATE, 1);
                endTime = calendar.getTime();
            }
            calendar.setTime(startTime);
            while (!calendar.getTime().equals(endTime)) {
                if (mapTimeCellIndex.get(timeFormat.format(calendar.getTime())) == null) {
                    listCellsIndex.add(latestCellIndex);
                    mapTimeCellIndex.put(timeFormat.format(calendar.getTime()), latestCellIndex);

                    Cell stCell = header.createCell(latestCellIndex++);
                    stCell.setCellValue(outputTimeFormat.format(calendar.getTime()));
                } else {
                    listCellsIndex.add(mapTimeCellIndex.get(timeFormat.format(calendar.getTime())));
                }
                calendar.add(Calendar.MINUTE, 30);
            }
            shiftTypeColumns.put(shiftType, listCellsIndex);
        }
        sheet.trackAllColumnsForAutoSizing();
        sheet.autoSizeColumn(0);
        for (String key : mapTimeCellIndex.keySet()) {
            sheet.autoSizeColumn(mapTimeCellIndex.get(key));
        }
        sheet.createFreezePane(1, 1);
        return shiftTypeColumns;
    }

    private void fillOutSheet(List<ShiftAssignment> effectiveShiftAssignments, SXSSFWorkbook workbook, SXSSFSheet sheet, Map<ShiftType, List<Integer>> shiftTypeColumns) {
        for (int i = 0; i < effectiveShiftAssignments.size(); i++) {
            ShiftAssignment shiftAssignment = effectiveShiftAssignments.get(i);
            Row contentRow = sheet.createRow(i + 1);

            // create employee name cell
            Cell employeeNameCell = contentRow.createCell(0);
            if (shiftAssignment.getEmployee() != null) {
                employeeNameCell.setCellValue(shiftAssignment.getEmployee().getName());
            } else {
                employeeNameCell.setCellValue("Unassigned");
            }

            // create employee assignment cell
            List<Integer> listCellsIndex = shiftTypeColumns.get(shiftAssignment.getShift().getShiftType());
            String taskString = "";
            for (Task task : shiftAssignment.getTaskList()) {
                taskString += task.getDescription() + " ";
            }
            CellStyle style = workbook.createCellStyle();
            style.setFillForegroundColor(getShiftTypeColor(shiftAssignment.getShift().getShiftType()));
            style.setFillPattern(CellStyle.SOLID_FOREGROUND);
            for (Integer index : listCellsIndex) {
                Cell assignmentCell = contentRow.createCell(index);
                assignmentCell.setCellValue(taskString);
                assignmentCell.setCellStyle(style);
            }
            sheet.addMergedRegion(new CellRangeAddress(i + 1, i + 1, listCellsIndex.get(0), listCellsIndex.get(listCellsIndex.size() - 1)));
        }
    }

    private Short getShiftTypeColor(ShiftType shiftType) {
        Short colorIndex;
        switch (shiftType.getIndex()) {
            case 0:
                colorIndex = IndexedColors.GREEN.getIndex();
                break;
            case 1:
                colorIndex = IndexedColors.YELLOW.getIndex();
                break;
            case 2:
                colorIndex = IndexedColors.BLUE.getIndex();
                break;
            case 3:
                colorIndex = IndexedColors.ORANGE.getIndex();
                break;
            default:
                colorIndex = IndexedColors.GREEN.getIndex();
                break;
        }
        return colorIndex;
    }
}
