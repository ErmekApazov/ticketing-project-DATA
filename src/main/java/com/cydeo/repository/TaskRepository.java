package com.cydeo.repository;

import com.cydeo.entity.Project;
import com.cydeo.entity.Task;
import com.cydeo.entity.User;
import com.cydeo.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {

    //jpql // get all projects from db that belongs to (String projectCode) with Status NonCompleted. "<>" - means not equal to
    @Query("SELECT COUNT(t) FROM Task t WHERE t.project.projectCode = ?1 AND t.taskStatus<> 'COMPLETE'") // join table
    int totalNonCompletedTasks(String projectCode);

    //native query // get all projects from db that belong to (String projectCode) with Status Completed
    @Query(value = "SELECT COUNT(*) " +
        "FROM tasks t JOIN projects p ON t.project_id=p.id " +
        "WHERE p.project_code = ?1 AND t.task_status = 'COMPLETE'", nativeQuery = true)
    int totalCompletedTasks(String projectCode);

    List<Task> findAllByProject(Project project);

    List<Task> findAllByTaskStatusIsNotAndAssignedEmployee(Status status, User user);
    List<Task> findAllByTaskStatusAndAssignedEmployee(Status status, User user);

}