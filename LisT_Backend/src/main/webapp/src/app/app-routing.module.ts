import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Routes } from '@angular/router';

import { AppComponent } from './app.component';
import { HomeComponent } from './home/home.component';
import { OptionsComponent } from './options/options.component';
import { StatisticsComponent } from './statistics/statistics.component';


export const routes: Routes = [
  {path: '', redirectTo: 'home', pathMatch: 'full'},
 {path: 'api/sensors', redirectTo: 'home', pathMatch: 'full'},
  {path: 'sensors', redirectTo: 'home', pathMatch: 'full'},
    {path: 'home', component: HomeComponent},
    {path: 'statistics', component: StatisticsComponent},
    {path: 'options', component: OptionsComponent},
];

@NgModule({
  imports: [ RouterModule.forRoot(routes)],
  exports: [ RouterModule ],
  declarations: []
})
export class AppRoutingModule { }



