import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';

import { AppComponent } from './app.component';
import { SensorService } from './sensor.service';
import { ValueService } from './value.service';
import { StatisticsComponent } from './statistics/statistics.component';

import { NgxLineChartModule } from 'ngx-line-chart';
import { AppRoutingModule } from './/app-routing.module';
import { ToolbarComponent } from './toolbar/toolbar.component';
import { HomeComponent } from './home/home.component';


@NgModule({
  declarations: [
    AppComponent,
    StatisticsComponent,
    ToolbarComponent,
    HomeComponent
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    NgxLineChartModule,
    AppRoutingModule
  ],
  providers: [
    SensorService,
    ValueService
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
