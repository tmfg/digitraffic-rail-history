import {Component} from '@angular/core';
import {TrainService} from "../../services/train.service";
import {UntypedFormControl} from "@angular/forms";

@Component({
  selector: 'app-train-table',
  templateUrl: './train-table.component.html',
  styleUrls: ['./train-table.component.scss']
})
export class TrainTableComponent {
  public displayedColumns: string[] = ['stationShortCode' ,'commercialTrack', 'type', 'scheduledTime','actualTime','liveEstimateTime','estimateSource','unknownDelay','cancelled','trainReady','causes'];
  public trains: any[];
  public loading: boolean = false;
  public departureDateFormControl = new UntypedFormControl(new Date().toISOString().substring(0, 10))
  public trainNumberFormControl = new UntypedFormControl(1)
  public selectedVersionFormControl = new UntypedFormControl()
  public dataSource

  public constructor(private trainService: TrainService) {
    this.selectedVersionFormControl.valueChanges.subscribe(value => {
      this.dataSource = value.timeTableRows
    })
  }

  public selectTrain = (train: any) => {
    this.selectedVersionFormControl.setValue(train.json);
  };

  public fetchTrain() {
    this.loading = true;
    this.trainService.getJSON(this.trainNumberFormControl.value, this.departureDateFormControl.value).then(result => {
      this.trains = result
      this.endLoading(result)
    });
  }

  private endLoading(s: any[]) {
    this.loading = false;
    this.selectTrain(s[0]);
  }
}
