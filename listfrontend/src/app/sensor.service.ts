import { Sensors } from './sensors';
import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Observable';
import 'rxjs/add/operator/map';

@Injectable()
export class SensorService {
  public baseUrl = 'http://localhost:8080/api';

  constructor(private http: Http) { }

  getSensors(): Promise<Sensors> {
    return this.http.get(this.baseUrl)
      .toPromise()
      .then(response => response.json().data as Sensors);
//    const sensors$ = this.http
//      .get('${this.baseUrl}/sensors')
//      .map(mapSensors);
//    return sensors$;
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
