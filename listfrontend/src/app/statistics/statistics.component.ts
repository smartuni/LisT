import { Component, NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import {NgxChartsModule} from '@swimlane/ngx-charts';
import { temp } from './temp';

@Component({
  selector: 'bm-statistics',
  templateUrl: './statistics.component.html',
  styleUrls: ['./statistics.component.css']
})
export class StatisticsComponent {
  temp: any[];
  view: any[] = [700, 400];


  showXAxis = true;
  showYAxis = true;
  gradient = false;
  showLegend = true;
  showXAxisLabel = true;
  xAxisLabel = 'Time';
  showYAxisLabel = true;
  yAxisLabel = 'Temperature';

  colorScheme = {
    domain: ['#5AA454', '#A10A28', '#C7B42C', '#AAAAAA']
  };

  autoScale = true;

  constructor() {
    Object.assign(this, {temp});
  }

  onSelect(event) {
    console.log(event);
  }

}

export class AppModule {}
