import 'rxjs/Rx';
import {Injectable} from "@angular/core";
import {Observable} from "rxjs/Observable";
import {JsonService} from "./json.service";

@Injectable()
export class TrainService extends JsonService {

  public getJSON(trainNumber: number, departureDate: string): Observable<any> {
    return super.getJSON(trainNumber, departureDate, "train");
  }

}
