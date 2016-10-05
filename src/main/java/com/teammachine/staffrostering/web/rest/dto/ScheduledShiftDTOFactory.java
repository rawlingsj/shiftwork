package com.teammachine.staffrostering.web.rest.dto;

import com.teammachine.staffrostering.config.DTOFactory;
import com.teammachine.staffrostering.domain.*;
import com.teammachine.staffrostering.repository.ShiftAssignmentRepository;

import javax.inject.Inject;
import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@DTOFactory
public class ScheduledShiftDTOFactory {

    @Inject
    private ShiftAssignmentRepository shiftAssignmentRepository;

    public List<ScheduledShiftDTO> createScheduledShiftDTOs(List<ShiftAssignment> shiftAssignments) {
        Map<Shift, List<ShiftAssignment>> neighboringAssignments = getShiftAssignmentPerShiftMap(
            shiftAssignments.stream().map(ShiftAssignment::getShift).collect(Collectors.toSet())
        );
        return shiftAssignments.stream()
            .map(shiftAssignment -> createScheduledShiftDTO(shiftAssignment, neighboringAssignments.get(shiftAssignment.getShift())))
            .sorted(Comparator.<ScheduledShiftDTO, LocalDate>comparing(dto -> dto.getShiftDate().getDate()).thenComparing(dto -> dto.getShiftType().getIndex()))
            .collect(Collectors.toList());
    }

    private Map<Shift, List<ShiftAssignment>> getShiftAssignmentPerShiftMap(Set<Shift> shift) {
        return shiftAssignmentRepository.findAllForShifts(shift).stream().collect(Collectors.toMap(
            ShiftAssignment::getShift,
            Collections::singletonList,
            (sha1, sha2) -> {
                List<ShiftAssignment> result = new ArrayList<>(sha1);
                result.addAll(sha2);
                return result;
            }));
    }

    public ScheduledShiftDTO createScheduledShiftDTO(ShiftAssignment shiftAssignment, List<ShiftAssignment> neighboringAssignments) {
        ScheduledShiftDTO dto = new ScheduledShiftDTO();
        dto.setId(shiftAssignment.getId());
        dto.setShiftDate(shiftAssignment.getShift().getShiftDate());
        dto.setShiftType(shiftAssignment.getShift().getShiftType());
        Map<Task, TaskWithCoworkersDTO> tasks = shiftAssignment.getTaskList().stream().collect(Collectors.toMap(
            Function.identity(), this::createTaskWithoutCoworkers, (t1, t2) -> t1
        ));
        if (neighboringAssignments != null) {
            neighboringAssignments.stream()
                .filter(sha -> !sha.getEmployee().equals(shiftAssignment.getEmployee()))
                .forEach(sha -> {
                    EntityRefInfo coworker = createCoworkerDTO(sha.getEmployee());
                    sha.getTaskList().stream().filter(tasks::containsKey).map(tasks::get)
                        .forEach(taskWithCoworkersDTO -> taskWithCoworkersDTO.addCoworker(coworker));
                });
        }
        dto.setTasks(tasks.values().stream().sorted((t1, t2) -> t1.getTask().getCode().compareTo(t2.getTask().getCode())).collect(Collectors.toList()));
        return dto;
    }

    private TaskWithCoworkersDTO createTaskWithoutCoworkers(Task task) {
        TaskWithCoworkersDTO dto = new TaskWithCoworkersDTO();
        dto.setTask(task);
        return dto;
    }

    private EntityRefInfo createCoworkerDTO(Employee employee) {
        return new EntityRefInfo(employee.getId(), employee.getCode(), employee.getName());
    }
}
