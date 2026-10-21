
package com.app.todoapp.services;

import com.app.todoapp.models.Task;
import com.app.todoapp.repository.TaskRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {

    private static final Logger log =
            LoggerFactory.getLogger(TaskService.class);

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public List<Task> getAllTasks() {
        long start = System.nanoTime();

        try {
            return taskRepository.findAll();
        } finally {
            log.info("DB findAll took {} ms",
                    (System.nanoTime() - start) / 1_000_000);
        }
    }

    public void createTask(String title) {
        Task task = new Task();
        task.setTitle(title);
        task.setCompleted(false);

        long start = System.nanoTime();

        try {
            taskRepository.save(task);
        } finally {
            log.info("DB save took {} ms",
                    (System.nanoTime() - start) / 1_000_000);
        }
    }

    public void deleteTask(Long id) {
        long start = System.nanoTime();

        try {
            taskRepository.deleteById(id);
        } finally {
            log.info("DB deleteById took {} ms",
                    (System.nanoTime() - start) / 1_000_000);
        }
    }

    public void toggleTask(Long id) {
        long startFind = System.nanoTime();

        Task task;
        try {
            task = taskRepository.findById(id)
                    .orElseThrow(() ->
                            new IllegalArgumentException("Invalid task id"));
        } finally {
            log.info("DB findById took {} ms",
                    (System.nanoTime() - startFind) / 1_000_000);
        }

        task.setCompleted(!task.isCompleted());

        long startSave = System.nanoTime();

        try {
            taskRepository.save(task);
        } finally {
            log.info("DB toggle save took {} ms",
                    (System.nanoTime() - startSave) / 1_000_000);
        }
    }
}
