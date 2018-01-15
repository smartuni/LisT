import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { FlexLayoutModule } from '@angular/flex-layout';

import { AppComponent } from './app.component';
import { SensorService } from './sensor.service';
import { ValueService } from './value.service';
import { StatisticsComponent } from './statistics/statistics.component';

import { AppRoutingModule } from './app-routing.module';
import { ToolbarComponent } from './toolbar/toolbar.component';
import { HomeComponent } from './home/home.component';
import { OptionsComponent } from './options/options.component';
import { NgxChartsModule } from '@swimlane/ngx-charts';


@NgModule({
    imports: [
    BrowserModule,
    HttpClientModule,
    NgxChartsModule,
    AppRoutingModule,
    FlexLayoutModule
  ],
  declarations: [
    AppComponent,
    StatisticsComponent,
    ToolbarComponent,
    HomeComponent,
    OptionsComponent
  ],

  providers: [
    SensorService,
    ValueService
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
