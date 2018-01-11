import {Injectable, Component} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Response, Headers, RequestOptions} from '@angular/http';
import {Observable} from 'rxjs/Observable';

import {SensorService} from './sensor.service';
import {Sensors} from './sensors';
import {ValueService} from './value.service';
import {Values} from './values';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  allSensors: Sensors[];
  values: Values[];
  title = 'Smartes Aquarium';

  constructor(
    private http: HttpClient,
    private sensorService: SensorService,
    private valueService: ValueService) {}


  private printValuesSensor1(): void {
    this.valueService.getValue1()
      .subscribe(values => this.values = values);
  }
  private printValuesSensor2(): void {
    this.valueService.getValue2()
      .subscribe(values => this.values = values);
  }
  private printValuesSensor3(): void {
    this.valueService.getValue3()
      .subscribe(values => this.values = values);
  }
  private printValuesSensor4(): void {
    this.valueService.getValue4()
      .subscribe(values => this.values = values);
  }

  private printSensor(): void {
    this.sensorService.getSensors()
      .subscribe(sensors => this.allSensors = sensors);
    // this.http.get(`/api/sensors`).subscribe(response => this.result = response.text()); response => this.result = response.text()

  }



}
