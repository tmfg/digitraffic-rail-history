import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';

import {AppComponent} from './app.component';
import {TrainTableComponent} from './components/train-table/train-table.component';
import {HashLocationStrategy, LocationStrategy} from "@angular/common";
import {TrainService} from "./services/train.service";
import {FormsModule} from "@angular/forms";
import {CompositionTableComponent} from './components/composition-table/composition-table.component';
import {CompositionService} from "./services/composition.service";
import {RouterModule, Routes} from "@angular/router";
import {HttpClientModule} from "@angular/common/http";


const appRoutes: Routes = [
  {path: 'compositions', component: CompositionTableComponent},
  {path: 'trains', component: TrainTableComponent}
];


@NgModule({
  declarations: [
    AppComponent,
    TrainTableComponent,
    CompositionTableComponent
  ],
  imports: [
    BrowserModule, HttpClientModule, FormsModule,
    RouterModule.forRoot(
      appRoutes,
      {enableTracing: false} // <-- debugging purposes only
    )
  ],
  bootstrap: [AppComponent],
  providers: [{
    provide: LocationStrategy,
    useClass: HashLocationStrategy
  }, TrainService, CompositionService]
})
export class AppModule {
}
