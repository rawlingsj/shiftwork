package com.teammachine.staffrostering.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.teammachine.staffrostering.domain.ShiftAssignment;
import com.teammachine.staffrostering.domain.ShiftDate;
import com.teammachine.staffrostering.domain.ShiftType;
import com.teammachine.staffrostering.domain.Task;
import com.teammachine.staffrostering.domain.enumeration.TaskType;
import com.teammachine.staffrostering.repository.ShiftAssignmentRepository;
import com.teammachine.staffrostering.repository.ShiftDateRepository;
import com.teammachine.staffrostering.repository.ShiftTypeRepository;
import com.teammachine.staffrostering.web.rest.errors.CustomParameterizedException;
import com.teammachine.staffrostering.web.rest.errors.ErrorConstants;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.List;
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
            int rowIdx = i + 1;
            Row contentRow = sheet.createRow(rowIdx);

            // create 'employee name' cell
            Cell employeeNameCell = contentRow.createCell(0);
            if (shiftAssignment.getEmployee() != null) {
                employeeNameCell.setCellValue(shiftAssignment.getEmployee().getName());
            } else {
                employeeNameCell.setCellValue("Unassigned");
            }

            // create employee assignment cell
            List<Integer> listCellsIndex = shiftTypeColumns.get(shiftAssignment.getShift().getShiftType());

            List<Pair<Task, Integer>> tasksWeight = getTaskCellsOccupation(shiftAssignment.getTaskList(), listCellsIndex.size());
            int taskStartIdx = 0;
            for (Pair<Task, Integer> task : tasksWeight) {
                XSSFCellStyle style = (XSSFCellStyle) workbook.createCellStyle();
                XSSFFont font = (XSSFFont) workbook.createFont();
                font.setColor(new XSSFColor(getTaskTextColorOrDefault(task.getKey(), Color.BLACK)));
                style.setFillForegroundColor(new XSSFColor(getTaskBackgroundColorOrDefault(task.getKey(), Color.LIGHT_GRAY)));
                style.setFillPattern(CellStyle.SOLID_FOREGROUND);
                style.setFont(font);
                int taskEndIdx = taskStartIdx + task.getValue() - 1;
                for (Integer index : listCellsIndex.subList(taskStartIdx, taskEndIdx + 1)) {
                    Cell assignmentCell = contentRow.createCell(index);
                    assignmentCell.setCellValue(task.getKey().getCode());
                    assignmentCell.setCellStyle(style);
                }
                if (taskEndIdx > taskStartIdx) {
                    Integer mergeFromColumnIdx = listCellsIndex.get(taskStartIdx);
                    Integer mergeToColumnIdx = listCellsIndex.get(taskEndIdx);
                    sheet.addMergedRegion(new CellRangeAddress(rowIdx, rowIdx, mergeFromColumnIdx, mergeToColumnIdx));
                }
                taskStartIdx = taskEndIdx + 1;
            }
        }
    }

    private Color getTaskTextColorOrDefault(Task task, Color defaultColor) {
        if (task.getStyle() != null) {
            String textColor = task.getStyle().getTextColor();
            if (textColor != null) {
                return Color.decode(textColor);
            }
        }
        return defaultColor;
    }

    private Color getTaskBackgroundColorOrDefault(Task task, Color defaultColor) {
        if (task.getStyle() != null) {
            String backgroundColor = task.getStyle().getBackgroundColor();
            if (backgroundColor != null) {
                return Color.decode(backgroundColor);
            }
        }
        return defaultColor;
    }

    List<Pair<Task, Integer>> getTaskCellsOccupation(Set<Task> tasks, int totalCells) {
        int cellsOccupied = 0;
        List<Pair<Task, Integer>> result = new ArrayList<>(tasks.size());
        List<Task> orderedTasks = new ArrayList<>(tasks);
        Collections.sort(orderedTasks, this::compareTasksByType);
        for (int i = 0; i < orderedTasks.size(); i++) {
            Task task = orderedTasks.get(i);
            if (i + 1 == orderedTasks.size()) { // last element will occupy remaining cells
                result.add(Pair.of(task, totalCells - cellsOccupied));
            } else {
                List<Task> remaining = orderedTasks.subList(i, orderedTasks.size());
                if (haveTasksOfDifferentType(remaining) && (totalCells - cellsOccupied) > remaining.size()) {
                    int cells = getDefaultCellsOccupied(task.getTaskType());
                    result.add(Pair.of(task, cells));
                    cellsOccupied += cells;
                } else {
                    int cells = (totalCells - cellsOccupied) / remaining.size();
                    result.add(Pair.of(task, cells));
                    cellsOccupied += cells;
                }
            }
        }
        return result;
    }

    private boolean haveTasksOfDifferentType(List<Task> tasks) {
        return tasks.stream().map(Task::getTaskType).distinct().count() > 1;
    }

    private int getDefaultCellsOccupied(TaskType taskType) {
        switch (taskType) {
            case SHORT:
                return 1;
            case MAIN:
                return 2;
            case FULL:
                return 3;
            default:
                throw new IllegalArgumentException("This task type is not supported: " + taskType.name());
        }
    }

    private int compareTasksByType(Task t1, Task t2) {
        if (t1.getTaskType() == t2.getTaskType()) {
            return 0;
        }
        switch (t1.getTaskType()) {
            case SHORT:
                return -1;
            case MAIN:
                return TaskType.SHORT == t2.getTaskType() ? 1 : -1;
            case FULL:
                return 1;
            default:
                throw new IllegalArgumentException();
        }
    }
}
