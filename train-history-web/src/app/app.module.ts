import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';

import {AppComponent} from './app.component';
import {TrainTableComponent} from './components/train-table/train-table.component';
import {HashLocationStrategy, LocationStrategy} from "@angular/common";
import {TrainService} from "./services/train.service";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {CompositionTableComponent} from './components/composition-table/composition-table.component';
import {CompositionService} from "./services/composition.service";
import {RouterModule, Routes} from "@angular/router";
import {HttpClientModule} from "@angular/common/http";
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {MatLegacySelectModule as MatSelectModule} from "@angular/material/legacy-select";
import {MatLegacyInputModule as MatInputModule} from "@angular/material/legacy-input";
import {MatIconModule} from "@angular/material/icon";
import {MatLegacyButtonModule as MatButtonModule} from "@angular/material/legacy-button";
import {MatLegacyProgressSpinnerModule as MatProgressSpinnerModule} from "@angular/material/legacy-progress-spinner";
import {MatLegacyTableModule as MatTableModule} from "@angular/material/legacy-table";


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
    BrowserModule,
    HttpClientModule,
    FormsModule,
    MatSelectModule,
    MatInputModule,
    MatButtonModule,
    RouterModule.forRoot(
      appRoutes,
      { enableTracing: false }
    ),
    BrowserAnimationsModule,
    MatIconModule,
    MatProgressSpinnerModule,
    ReactiveFormsModule,
    MatTableModule
  ],
  bootstrap: [AppComponent],
  providers: [{
    provide: LocationStrategy,
    useClass: HashLocationStrategy
  }, TrainService, CompositionService]
})
export class AppModule {
}
