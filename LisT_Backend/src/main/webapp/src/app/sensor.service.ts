import { Sensors } from './sensors';
import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { of } from 'rxjs/observable/of';
import 'rxjs/add/operator/map';

@Injectable()
export class SensorService {
  public baseUrl = 'http://141.22.28.86:8080/api';

  constructor(private http: HttpClient) { }

  getSensors(): Observable<any> {
   return this.http.get(this.baseUrl + '/sensors');
  }
}
