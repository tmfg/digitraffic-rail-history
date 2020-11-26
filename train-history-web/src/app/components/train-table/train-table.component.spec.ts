import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { TrainTableComponent } from './train-table.component';

describe('TrainTableComponent', () => {
  let component: TrainTableComponent;
  let fixture: ComponentFixture<TrainTableComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ TrainTableComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(TrainTableComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
