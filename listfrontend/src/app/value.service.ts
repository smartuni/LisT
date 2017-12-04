import { Values } from './values';
import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { of } from 'rxjs/observable/of';
import 'rxjs/add/operator/map';

@Injectable()
export class ValueService {
  public baseUrl = 'http://localhost:4200/api';

  constructor(private http: HttpClient) { }

  getValues(): Observable<Values[]> {
    return this.http.get(this.baseUrl + '/sensors/1/values');
  }
}
