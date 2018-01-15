import { ActorService } from '../actor.service';
import { Actors } from '../actors';
import { HttpClient } from '@angular/common/http';
import { HttpHeaders } from '@angular/common/http';
import { Component, OnInit, Input } from '@angular/core';
import { Observable } from 'rxjs/Observable';
import { DatePipe } from '@angular/common';

const httpOptions = {
  headers: new HttpHeaders({ 'Content-Type': 'application/json' })
};

@Component({
  selector: 'app-options',
  templateUrl: './options.component.html',
  styleUrls: ['./options.component.css']
})
export class OptionsComponent implements OnInit {

  baseUrl = 'http://141.22.28.86:8080/api';
  @Input() temp: Actors;

  dateFormat = new Date('2018-1-14 13:50').getTime;

  data1 = {techId: 's1', sensorType: 'TEMP', value: this.temp, timestamp: this.dateFormat};
  dataTest = {techId: 's1', sensorType: 'TEMP', value: [this.temp], timestamp: this.dateFormat};

  red = {techID: 's3', sensorType: 'LIGHT', value: [255, 0 , 0 ], timestamp: this.dateFormat};
    blue = {techID: 's3', sensorType: 'LIGHT', value: [0 , 0 , 255 ], timestamp: this.dateFormat};
    green = {techID: 's3', sensorType: 'LIGHT', value: [0, 255 , 0 ], timestamp: this.dateFormat};
    white = {techID: 's3', sensorType: 'LIGHT', value: [255, 255 , 255 ], timestamp: this.dateFormat};


  constructor(
  private actorService: ActorService,
    private http: HttpClient) { }

  ngOnInit() {
  }

  tempSwitch(): Observable<any> {
   return this.http.put(this.baseUrl + '/actor', this.data1, httpOptions);
  }

  tempLog(): void {
    console.log(this.http.put(this.baseUrl + '/actor', this.dataTest, httpOptions));
  }

  lightRed():  void {
    this.http.put(this.baseUrl + '/actor', this.red, httpOptions);
  }

  lightBlue():  Observable<any> {
    return this.http.put(this.baseUrl + '/actor', this.blue, httpOptions);
  }

  lightGreen():  Observable<any> {
    return this.http.put(this.baseUrl + '/actor', this.green, httpOptions);
  }

  lightWhite():  Observable<any> {
    return this.http.put(this.baseUrl + '/actor', this.white, httpOptions);
  }

}


