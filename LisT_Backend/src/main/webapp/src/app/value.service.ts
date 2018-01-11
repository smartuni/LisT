import { Values } from './values';
import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { of } from 'rxjs/observable/of';
import 'rxjs/add/operator/map';

@Injectable()
export class ValueService {
  public baseUrl = 'http://141.22.28.86:8181/api';

  constructor(private http: HttpClient) { }

  getValue1(): Observable<any> {
  return this.http.get(this.baseUrl + '/sensors/1/values');
  }

    getValue2(): Observable<any> {
  return this.http.get(this.baseUrl + '/sensors/2}/values');
  }

    getValue3(): Observable<any> {
  return this.http.get(this.baseUrl + '/sensors/3/values');
  }

    getValue4(): Observable<any> {
  return this.http.get(this.baseUrl + '/sensors/4/values');
  }
}
