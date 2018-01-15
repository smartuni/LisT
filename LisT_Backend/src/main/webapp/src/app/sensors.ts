import { location_enum } from './location_enum';
import { sensors_enum } from './sensors_enum';
import { Values } from './values';

export class Sensors {
  constructor(
  public value: Values,
  public name: string,
  public typ: sensors_enum,
  public location: location_enum,
  public id: number
    ) {}
}
