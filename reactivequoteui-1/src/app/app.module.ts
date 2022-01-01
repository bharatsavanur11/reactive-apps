import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { BrowserModule } from '@angular/platform-browser';

import { AppComponent } from './app.component';
import { QuoteComponent } from './quote/quote-component';
import {HttpClientModule} from '@angular/common/http'
import {QuoteReactiveService} from './quote/QuoteReactiveService'

@NgModule({
  declarations: [
    AppComponent,
    QuoteComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    HttpClientModule
  ],
  providers: [
    QuoteReactiveService
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
