import { Injectable } from "@angular/core";
import { Component } from '@angular/core';
import { Http, Response, Headers, RequestOptions } from '@angular/http';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
  })
export class AppComponent {
  title = 'Smartes Aquarium';
  result = '';

  constructor(
    private http: Http){
  }

  private printUsers(): void {
    this.result = 'loading...';
    this.http.get(`/user`).subscribe(response => this.result = response.text());
  }
  private printTools(): void {
    this.result = 'loading...';
    this.http.get(`/tools`).subscribe(response => this.result = response.text());
  }
  private printSensor(): void {
    this.result = 'loading...';
    this.http.get(`/api/sensors`).subscribe(response => this.result = response.text());
  }

  private printSKData(): void {
    this.result = 'loading...';
    this.http.get('http://141.22.28.85/sensor').subscribe(response => this.result = response.text());
  }

}
