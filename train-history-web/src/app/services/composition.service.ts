import {Http, Response, Headers, RequestOptions} from '@angular/http';
import 'rxjs/Rx';
import {Injectable} from "@angular/core";
import {Observable} from "rxjs/Observable";
import { environment } from '../../environments/environment';
import {JsonService} from "./json.service";

@Injectable()
export class CompositionService extends JsonService{

  public getJSON(trainNumber: number, departureDate: string): Observable<any> {
    return super.getJSON(trainNumber,departureDate,"composition");
  }

}
