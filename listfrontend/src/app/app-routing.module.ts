import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Routes } from '@angular/router';

import { AppComponent } from './app.component';
import { StatisticsComponent } from './statistics/statistics.component';


export const appRoutes: Routes = [
    {path: 'home', component: AppComponent},
    {path: 'statistics', component: StatisticsComponent},
     {path: '', redirectTo: '/home', pathMatch: 'full'}
];

@NgModule({
  imports: [
    CommonModule
  ],
  declarations: []
})
export class AppRoutingModule { }



