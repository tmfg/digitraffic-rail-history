import { Component, OnInit } from '@angular/core';
import {Observable} from "rxjs/Observable";
import {CompositionService} from "../../services/composition.service";

@Component({
  selector: 'app-composition-table',
  templateUrl: './composition-table.component.html',
  styleUrls: ['./composition-table.component.css']
})
export class CompositionTableComponent implements OnInit {
  compositions: Observable<any>;

  selectedComposition: any;
  private compositionService: CompositionService;
  trainNumber: number = 1;
  departureDate: string;
  loading: boolean = false;

  public selectComposition(composition: any) {
    this.selectedComposition = composition;
  }

  private endLoading(s:Observable<any>) {
    this.loading = false;
    this.selectComposition(s[0]);
  }

  public fetchComposition() {
    this.loading = true;
    this.compositions = this.compositionService.getJSON(this.trainNumber, this.departureDate);
    this.compositions.subscribe(
      s=> this.endLoading(s)
    );
  }

  constructor(compositionService: CompositionService) {
    this.compositionService = compositionService;

    var now = new Date();
    this.departureDate = now.toISOString().substring(0, 10);
  }

  ngOnInit() {
  }

}
