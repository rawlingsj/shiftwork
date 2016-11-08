package com.teammachine.staffrostering.web.rest;


import com.google.common.collect.ImmutableSet;
import com.teammachine.staffrostering.domain.Task;
import com.teammachine.staffrostering.domain.enumeration.TaskType;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.Test;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

public class ScheduleResourceTest {

    @Test
    public void testTasksOfDifferentTypeCellsOccupation() {
        ScheduleResource scheduleResource = new ScheduleResource();
        Set<Task> tasks = ImmutableSet.of(
            createTask(1, TaskType.FULL),
            createTask(2, TaskType.MAIN),
            createTask(3, TaskType.SHORT)
        );

        // Business method
        List<Pair<Task, Integer>> taskCellsOccupation = scheduleResource.getTaskCellsOccupation(tasks, 16);

        // Asserts
        assertThat(taskCellsOccupation).hasSize(3);
        assertThat(taskCellsOccupation.get(0).getKey().getId()).isEqualTo(3);
        assertThat(taskCellsOccupation.get(0).getValue()).isEqualTo(1);

        assertThat(taskCellsOccupation.get(1).getKey().getId()).isEqualTo(2);
        assertThat(taskCellsOccupation.get(1).getValue()).isEqualTo(2);

        assertThat(taskCellsOccupation.get(2).getKey().getId()).isEqualTo(1);
        assertThat(taskCellsOccupation.get(2).getValue()).isEqualTo(13);
    }

    @Test
    public void testTasksOfTheDifferentTypeCellsOccupation_2() {
        ScheduleResource scheduleResource = new ScheduleResource();
        Set<Task> tasks = ImmutableSet.of(
            createTask(1, TaskType.FULL),
            createTask(2, TaskType.FULL),
            createTask(3, TaskType.SHORT)
        );

        // Business method
        List<Pair<Task, Integer>> taskCellsOccupation = scheduleResource.getTaskCellsOccupation(tasks, 5);

        // Asserts
        assertThat(taskCellsOccupation).hasSize(3);
        assertThat(taskCellsOccupation.get(0).getKey().getId()).isEqualTo(3);
        assertThat(taskCellsOccupation.get(0).getValue()).isEqualTo(1);

        assertThat(taskCellsOccupation.get(1).getKey().getId()).isEqualTo(1);
        assertThat(taskCellsOccupation.get(1).getValue()).isEqualTo(2);

        assertThat(taskCellsOccupation.get(2).getKey().getId()).isEqualTo(2);
        assertThat(taskCellsOccupation.get(2).getValue()).isEqualTo(2);
    }

    @Test
    public void testTasksOfTheDifferentTypeCellsOccupation_3() {
        ScheduleResource scheduleResource = new ScheduleResource();
        Set<Task> tasks = ImmutableSet.of(
            createTask(1, TaskType.FULL),
            createTask(2, TaskType.MAIN),
            createTask(3, TaskType.MAIN)
        );

        // Business method
        List<Pair<Task, Integer>> taskCellsOccupation = scheduleResource.getTaskCellsOccupation(tasks, 3);

        // Asserts
        assertThat(taskCellsOccupation).hasSize(3);
        assertThat(taskCellsOccupation.get(0).getKey().getId()).isEqualTo(2);
        assertThat(taskCellsOccupation.get(0).getValue()).isEqualTo(1);

        assertThat(taskCellsOccupation.get(1).getKey().getId()).isEqualTo(3);
        assertThat(taskCellsOccupation.get(1).getValue()).isEqualTo(1);

        assertThat(taskCellsOccupation.get(2).getKey().getId()).isEqualTo(1);
        assertThat(taskCellsOccupation.get(2).getValue()).isEqualTo(1);
    }

    @Test
    public void testTasksOfTheSameTypeCellsOccupation() {
        ScheduleResource scheduleResource = new ScheduleResource();
        Set<Task> tasks = ImmutableSet.of(
            createTask(1, TaskType.SHORT),
            createTask(2, TaskType.SHORT),
            createTask(3, TaskType.SHORT)
        );

        // Business method
        List<Pair<Task, Integer>> taskCellsOccupation = scheduleResource.getTaskCellsOccupation(tasks, 16);

        // Asserts
        assertThat(taskCellsOccupation).hasSize(3);
        assertThat(taskCellsOccupation.get(0).getKey().getId()).isEqualTo(1);
        assertThat(taskCellsOccupation.get(0).getValue()).isEqualTo(5);

        assertThat(taskCellsOccupation.get(1).getKey().getId()).isEqualTo(2);
        assertThat(taskCellsOccupation.get(1).getValue()).isEqualTo(5);

        assertThat(taskCellsOccupation.get(2).getKey().getId()).isEqualTo(3);
        assertThat(taskCellsOccupation.get(2).getValue()).isEqualTo(6);
    }

    @Test
    public void testTasksOfTheSameTypeCellsOccupation_2() {
        ScheduleResource scheduleResource = new ScheduleResource();
        Set<Task> tasks = ImmutableSet.of(
            createTask(1, TaskType.SHORT),
            createTask(2, TaskType.SHORT),
            createTask(3, TaskType.SHORT)
        );

        // Business method
        List<Pair<Task, Integer>> taskCellsOccupation = scheduleResource.getTaskCellsOccupation(tasks, 15);

        // Asserts
        assertThat(taskCellsOccupation).hasSize(3);
        assertThat(taskCellsOccupation.get(0).getKey().getId()).isEqualTo(1);
        assertThat(taskCellsOccupation.get(0).getValue()).isEqualTo(5);

        assertThat(taskCellsOccupation.get(1).getKey().getId()).isEqualTo(2);
        assertThat(taskCellsOccupation.get(1).getValue()).isEqualTo(5);

        assertThat(taskCellsOccupation.get(2).getKey().getId()).isEqualTo(3);
        assertThat(taskCellsOccupation.get(2).getValue()).isEqualTo(5);
    }

    private Task createTask(long id, TaskType type) {
        Task task = new Task();
        task.setId(id);
        task.setTaskType(type);
        return task;
    }
}
