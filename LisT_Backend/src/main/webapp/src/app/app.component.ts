import { Sensors } from './sensors';
import { Injectable, Component } from '@angular/core';
import { Http, Response, Headers, RequestOptions } from '@angular/http';
import { Observable } from 'rxjs/Observable';

import { SensorService } from '../sensor.service';
import { Sensors } from '../sensors';
import { ValueService } from '../value.service';
import { Values } from '../values';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
  })
export class AppComponent {
  allSensors: Sensors[];
  value: Response;
  title = 'Smartes Aquarium';
  result = '';

  constructor(
    private http: Http,
    private sensorService: SensorService,
    private valueService: ValueService) { }

  private printValuesSensor1(): void {
    this.result = 'loading...';
    this.http.get(`/api/sensors/1/values/`).subscribe(value => this.value = value);
  }
  private printValuesSensor2(): void {
    this.result = 'loading...';
    this.http.get(`/api/sensors/2/values/`).subscribe(value => this.value = value);
  }
    private printValuesSensor3(): void {
    this.result = 'loading...';
    this.http.get(`/api/sensors/3/values/`).subscribe(value => this.value = value);
  }
    private printValuesSensor4(): void {
    this.result = 'loading...';
    this.http.get(`/api/sensors/4/values/`).subscribe(value => this.value = value);
  }

  private printSensor(): void {
    this.sensorService.getSensors()
    .subscribe(sensors => this.allSensors = sensors);
    // this.http.get(`/api/sensors`).subscribe(response => this.result = response.text()); response => this.result = response.text()

  }



}
