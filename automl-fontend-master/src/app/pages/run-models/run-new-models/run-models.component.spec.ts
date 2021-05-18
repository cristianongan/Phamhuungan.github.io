import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { RunModelsComponent } from './run-models.component';

describe('RunModelsComponent', () => {
  let component: RunModelsComponent;
  let fixture: ComponentFixture<RunModelsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ RunModelsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(RunModelsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
