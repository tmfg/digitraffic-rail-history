import {Http} from '@angular/http';
import 'rxjs/Rx';
import {Injectable} from "@angular/core";
import {Observable} from "rxjs/Observable";

@Injectable()
export class JsonService {

  constructor(private http: Http) {
  }

  private jsonToEntity(json: any): any {
    var output = [];

    var jsonDoc = json.json();
    for (var i = 0; i < jsonDoc.length; i++) {
      var item = jsonDoc[i];
      Object.assign(item, jsonDoc[i].json);
      output.push(item);
    }
    return output;
  }

  public getJSON(trainNumber: number, departureDate: string, entityName: string): Observable<any> {
    let url = `/api/v1/${ entityName }s/history/${departureDate}/${trainNumber}`
    return this.http.get(url).map(this.jsonToEntity).publishLast().refCount();
  }

}
