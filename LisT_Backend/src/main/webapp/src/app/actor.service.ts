import { Actors } from './actors';
import {Values} from './values';
import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Http, Response} from '@angular/http';
import { Data } from '@angular/router';

import {Observable} from 'rxjs/Observable';

const httpOptions = {
  headers: new HttpHeaders({ 'Content-Type': 'application/json' })
};

@Injectable()
export class ActorService {

   public baseUrl = 'http://141.22.28.86:8080/api';

  constructor(private http: HttpClient) {}

  putTemp(temp: Actors): Observable<any> {
   const data = JSON.stringify({temp});
    return this.http
      .put(this.baseUrl + '/actor/3/values', data, httpOptions);
  }
}
