import {Injectable} from '@angular/core';
import {Quote} from './Quote';
import {Observable} from 'rxjs';

@Injectable()
export class QuoteReactiveService {

  url: string = 'http://localhost:8080/quotes-reactive';

  getQuoteStream(page?: number, size?: number): Observable<Quote> {

    return new Observable<Quote>((observer) => {
      let eventSource = new EventSource(this.url);
      eventSource.onmessage = (event) => {
        console.log(event);
        let jsonData = JSON.parse(event.data);
        observer.next(new Quote(jsonData['id'], jsonData['book'], jsonData['content']));
        };
      eventSource.onerror = (error) =>{
        if(eventSource.readyState ==0) {
          console.log("The stream has been closed by the server");
          // need to close the event source else it will keep on getting the data.
          // may be we can add spinner or something.
          // Can this be used to fetch the streaming service.
          eventSource.close();
        } else {
          console.log("Error",error);
        }

      };
    });
  }
}
