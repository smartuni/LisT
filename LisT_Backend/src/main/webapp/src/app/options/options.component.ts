import { Component, OnInit, Input } from '@angular/core';

import { ValueService } from '../value.service';
import { Values } from '../values';

@Component({
  selector: 'app-options',
  templateUrl: './options.component.html',
  styleUrls: ['./options.component.css']
})
export class OptionsComponent implements OnInit {


  resopnse: any;
  title: 'Options';
  temperature: 'Temperature';
  @Input() temp: Values;

  constructor(
  private valueService: ValueService
  ) { }

  ngOnInit() {
  }

  private tempSwitch(): void {
    this.valueService.putTemp(this.temp)
      .subscribe();
  }

}
