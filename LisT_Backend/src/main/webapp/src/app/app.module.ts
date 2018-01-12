import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';
import {FormsModule} from '@angular/forms';

import {AppComponent} from './app.component';
import {SensorService} from './sensor.service';
import {ValueService} from './value.service';
import {ActorService} from './actor.service';
import {HttpClientModule} from '@angular/common/http';
import {AppRoutingModule} from './app-routing.module';
import {ToolbarComponent} from './toolbar/toolbar.component';
import {OptionsComponent} from './options/options.component';

@NgModule({
  declarations: [
    AppComponent,
    ToolbarComponent,
    OptionsComponent
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    AppRoutingModule,
    FormsModule
  ],
  providers: [
    SensorService,
    ValueService,
    ActorService
  ],
  bootstrap: [AppComponent]
})
export class AppModule {}
