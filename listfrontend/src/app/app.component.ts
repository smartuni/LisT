import { SensorService } from './sensor.service';
import { Sensors } from './sensors';
import { ValueService } from './value.service';
import { Values } from './values';
import { Component, OnInit } from '@angular/core';


@Component({
  selector: 'bm-list',
  templateUrl: './app.component.html'
})
export class AppComponent implements OnInit {
  title = 'LisT';
  title2 = 'Livig in a smart Tank';

  errorMessage = '----';
  data: any;
  loading = true;
  sensors: Sensors[];
  values: Values[];

  constructor(
    private sensorService: SensorService,
    private valueService: ValueService
  ) { }

  ngOnInit() {
  this.getSensors();
    this.getValues();
  }

  getSensors(): void {
    this.sensorService.getSensors()
    .subscribe(sensors => this.sensors = sensors);
  }

  getValues(): void {
   this.valueService.getValues()
    .subscribe(values => this.values = values);
  }
}
