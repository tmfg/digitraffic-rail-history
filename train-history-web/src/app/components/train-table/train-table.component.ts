import {Component, OnInit} from '@angular/core';
import {TrainService} from "../../services/train.service";
import {Observable} from "rxjs";
import {ActivatedRoute} from '@angular/router';
import {Location} from '@angular/common';

@Component({
  selector: 'app-train-table',
  templateUrl: './train-table.component.html',
  styleUrls: ['./train-table.component.css']
})
export class TrainTableComponent implements OnInit {
  trains: Observable<any>;

  public selectedTrain: any;
  public trainNumber: number = 1;
  public departureDate: string;
  public loading: boolean = false;
  private trainService: TrainService;

  constructor(trainService: TrainService, private route: ActivatedRoute, private location: Location) {
    this.trainService = trainService;

    var now = new Date();
    this.departureDate = now.toISOString().substring(0, 10);
  }

  public selectTrain(train: any) {
    this.selectedTrain = train;
  }

  public fetchTrain() {
    this.loading = true;
    this.trains = this.trainService.getJSON(this.trainNumber, this.departureDate);
    this.trains.subscribe(s => this.endLoading(s));
  }

  ngOnInit() {
  }

  private endLoading(s: Observable<any>) {
    this.loading = false;
    this.selectTrain(s[0]);
  }

}
