import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { TaskService, Task } from './task.service';

describe('TaskService', () => {
  let service: TaskService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [TaskService],
    });

    service = TestBed.inject(TaskService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify(); // Проверка, что нет незавершенных запросов
  });

  it('should fetch tasks using getTasks()', () => {
    const mockTasks: Task[] = [
      { id: 1, description: 'Task 1', completed: false },
      { id: 2, description: 'Task 2', completed: true },
    ];

    service.getTasks().subscribe((tasks) => {
      expect(tasks).toEqual(mockTasks);
    });

    const req = httpMock.expectOne('http://localhost:8080/api/tasks');
    expect(req.request.method).toBe('GET');
    req.flush(mockTasks); // Симуляция ответа от сервера
  });

  it('should create a task using createTask()', () => {
    const newTask: Task = { description: 'New Task', completed: false };
    const mockResponse: Task = { id: 3, ...newTask };

    service.createTask(newTask).subscribe((task) => {
      expect(task).toEqual(mockResponse);
    });

    const req = httpMock.expectOne('http://localhost:8080/api/tasks');
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual(newTask);
    req.flush(mockResponse); // Симуляция ответа от сервера
  });
});