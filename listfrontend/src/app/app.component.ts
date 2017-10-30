import { SensorService } from './sensor.service';
import { Sensors } from './sensors';
import { Component, OnInit } from '@angular/core';


@Component({
  selector: 'bm-list',
  providers: [SensorService],
  template: `
  <h1>{{title}}</h1>
  <h2>{{title2}}</h2>,
  <h3>Fehler: {{errorMessage}}</h3>
  <div *ngIf="loading">loading...</div>
  <pre>Temp: {{ data | json }}</pre>
  `
})
export class AppComponent implements OnInit {
  title = 'LisT';
  title2 = 'Livig in a smart Tank';

  errorMessage = '----';
  data: any;
  loading = true;
  result: string[];

  constructor(private sensorSevice: SensorService) {}

  ngOnInit() {
  this.makeRequest();
  }

  makeRequest() {
    this.sensorSevice.getSensors().subscribe(
      data => this.data = data['result'],
      e => this.errorMessage = e,
      () => this.loading = false);
  }
}
