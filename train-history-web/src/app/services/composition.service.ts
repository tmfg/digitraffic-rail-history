import {Injectable} from "@angular/core";
import {JsonService} from "./json.service";

@Injectable()
export class CompositionService extends JsonService {

  public getJSON(trainNumber: number, departureDate: string): Promise<any> {
    return super.getJSON(trainNumber, departureDate, "composition");
  }

}
