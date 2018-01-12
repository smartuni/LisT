import { ActorService } from '../actor.service';
import { Actors } from '../actors';
import { Component, OnInit, Input } from '@angular/core';

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
  @Input() temp: Actors;

  constructor(
  private actorService: ActorService
  ) { }

  ngOnInit() {
  }

  private tempSwitch(): void {
    this.actorService.putTemp(this.temp)
      .subscribe();
  }

}
