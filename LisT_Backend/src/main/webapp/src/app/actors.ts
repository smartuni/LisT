import {sensors_enum} from './sensors_enum';

export class Actors {
  constructor(
    public timestamp: any,
    public value: Array<number>,
    public sensorType: sensors_enum,
    public techId: string
  ) {}
}
