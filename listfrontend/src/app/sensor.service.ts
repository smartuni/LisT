import { Sensors } from './sensors';
import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Observable';
import 'rxjs/add/operator/map';

@Injectable()
export class SensorService {
  public baseUrl = 'http://141.22.28.86:3306/';

  constructor(private http: Http) { }

  getSensors(): Observable<Sensors[]> {
    const sensors$ = this.http
      .get('${this.baseUrl}')
      .map(response => response.json().map(toSensor));
    return sensors$;
  }

}

function mapSensors(res: Response): Sensors[] {
  return res.json().map(toSensor);
}

function toSensor(r: any): Sensors {
  const sensor = <Sensors>({
  value: r.value,
  name: r.name,
  typ: r.typ,
  location: r.location,
  id: r.id,
  });
  return sensor;
}
function mapSensor(res: Response): Sensors {
  return toSensor(res.json());
}
