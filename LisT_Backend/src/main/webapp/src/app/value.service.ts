import { Values } from './values';
import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { of } from 'rxjs/observable/of';
import 'rxjs/add/operator/map';

@Injectable()
export class ValueService {
  public baseUrl = 'http://171.22.28.86:8181/api';

  constructor(private http: HttpClient) { }

  getValues(): Observable<any> {
  return this.http.get(this.baseUrl + '/sensors/1/values');
  }
}
