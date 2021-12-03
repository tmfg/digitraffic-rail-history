import {Injectable} from "@angular/core";
import {JsonService} from "./json.service";

@Injectable()
export class TrainService extends JsonService {

  public getJSON(trainNumber: number, departureDate: string): Promise<any> {
    return super.getJSON(trainNumber, departureDate, "train");
  }

}
