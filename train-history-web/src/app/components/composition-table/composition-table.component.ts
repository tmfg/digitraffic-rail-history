import { Component } from "@angular/core";
import { CompositionService } from "../../services/composition.service";
import { FormControl } from "@angular/forms";

@Component({
  selector: "app-composition-table",
  templateUrl: "./composition-table.component.html",
  styleUrls: ["./../train-table/train-table.component.scss"]
})
export class CompositionTableComponent {
  public displayedColumns: string[] = [
    "totalLength",
    "maximumSpeed",
    "beginTimeTableRow",
    "endTimeTableRow",
    "locomotives",
    "wagons"
  ];
  public compositions: any[];
  public loading: boolean = false;
  public departureDateFormControl = new FormControl(new Date().toISOString().substring(0, 10));
  public trainNumberFormControl = new FormControl(1);
  public selectedVersionFormControl = new FormControl();
  public dataSource;

  public constructor(private compositionService: CompositionService) {
    this.selectedVersionFormControl.valueChanges.subscribe((value) => {
      this.dataSource = value.journeySections;
    });
  }

  public selectVersion = (version: any) => {
    this.selectedVersionFormControl.setValue(version.json);
  };

  public fetchTrain() {
    this.loading = true;
    this.compositionService
      .getJSON(this.trainNumberFormControl.value, this.departureDateFormControl.value)
      .then((result) => {
        this.compositions = result;
        this.endLoading(result);
      });
  }

  private endLoading(s: any[]) {
    this.loading = false;
    this.selectVersion(s[0]);
  }
}
