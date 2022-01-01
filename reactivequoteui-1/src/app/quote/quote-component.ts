import { Quote} from './Quote';
import {QuoteReactiveService} from  './QuoteReactiveService';

import {Observable} from 'rxjs';
import {ChangeDetectorRef, Component} from '@angular/core';

@Component({
  selector: 'app-component-quote',
  providers: [QuoteReactiveService],
  templateUrl: './quote-component.html'
})
export class QuoteComponent{

  quoteArray:Quote[] = [];
  selectedQuote: Quote | undefined;
  mode: string | undefined;

  constructor(private quoteReactiveService: QuoteReactiveService, private cdr: ChangeDetectorRef) {
    this.mode= 'reactive';
  }

  resetData(){
    this.quoteArray = [];
  }

  requestQuoteStream(): void {
    this.resetData();
    let quoteObservable: Observable<Quote>;
    quoteObservable = this.quoteReactiveService.getQuoteStream();

    quoteObservable.subscribe(quote => {
      this.quoteArray.push(quote);
      this.cdr.detectChanges();
    });
   }
    onSelect(quote:Quote):void {
      this.selectedQuote = quote;
      this.cdr.detectChanges();
    }
}

