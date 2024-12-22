import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Task, TaskService } from '../task.service';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-task',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './task.component.html',
  styleUrls: ['./task.component.css']
})
export class TaskComponent implements OnInit {
  tasks: Task[] = [];
  newTask = '';

  constructor(private taskService: TaskService) {}

  ngOnInit(): void {
    this.loadTasks();
  }

  loadTasks(): void {
    this.taskService.getTasks().subscribe((data) => (this.tasks = data));
  }

  addTask(): void {
    if (this.newTask.trim()) {
      const task: Task = { description: this.newTask, completed: false };
      this.taskService.createTask(task).subscribe(() => {
        this.newTask = '';
        this.loadTasks();
      });
    }
  }

  toggleTask(task: Task): void {
    task.completed = !task.completed;
    this.taskService.updateTask(task).subscribe();
  }

  deleteTask(id: number): void {
    this.taskService.deleteTask(id).subscribe(() => this.loadTasks());
  }
}
