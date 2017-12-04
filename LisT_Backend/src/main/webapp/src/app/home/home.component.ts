import { Component, OnInit } from '@angular/core';
import { SensorService } from '../sensor.service';
import { Sensors } from '../sensors';
import { ValueService } from '../value.service';
import { Values } from '../values';

@Component({
  selector: 'bm-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {

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
    this.loading = true;
    this.sensorService.getSensors()
    .subscribe(sensors => this.sensors = sensors), this.loading = false;
  }

  getValues(): void {
    this.loading = true;
    this.valueService.getValues()
    .subscribe(values => this.values = values), this.loading = false;
  }
}
