import { Routes } from '@angular/router';
import { TaskComponent } from './task/task.component';

export const appRoutes: Routes = [
{ path: '', redirectTo: 'tasks', pathMatch: 'full' }, // Переадресация на главный маршрут
{ path: 'tasks', component: TaskComponent }, // Основной маршрут для TaskComponent
{ path: '**', redirectTo: 'tasks' }, // Переадресация для неопределенных маршрутов
];
