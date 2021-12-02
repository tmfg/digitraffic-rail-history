import 'rxjs/Rx';
import {Injectable} from "@angular/core";
import {Observable} from "rxjs";
import {HttpClient} from '@angular/common/http';

@Injectable()
export class JsonService {

  constructor(private http: HttpClient) {
  }

  public getJSON(trainNumber: number, departureDate: string, entityName: string): Observable<any> {
    let url = `/api/v1/${entityName}s/history/${departureDate}/${trainNumber}`
    return this.http.get(url);
  }

}
