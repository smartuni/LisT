import { ActorService } from '../actor.service';
import { Actors } from '../actors';
import { HttpClient } from '@angular/common/http';
import { HttpHeaders } from '@angular/common/http';
import { Component, OnInit, Input } from '@angular/core';
import { Observable } from 'rxjs/Observable';

const httpOptions = {
  headers: new HttpHeaders({ 'Content-Type': 'application/json' })
};

@Component({
  selector: 'app-options',
  templateUrl: './options.component.html',
  styleUrls: ['./options.component.css']
})
export class OptionsComponent implements OnInit {


  resopnse: any;
  title: 'Options';
  temperature: 'Temperature';
  light: 'Light';

  baseUrl = 'http://141.22.28.86:8080/api';
  @Input() temp: Actors;
  date: Date = new Date();
  dateNow = this.date.getTime();
  data1 = {techId: 's1', sensorType: 'TEMP', value: this.temp, timestamp: this.dateNow};

  constructor(
  private actorService: ActorService,
    private http: HttpClient) { }

  ngOnInit() {
  }

  private tempSwitch(): Observable<any> {
   return this.http.put(this.baseUrl + '/actor/3/values', this.data1, httpOptions);
  }

}
