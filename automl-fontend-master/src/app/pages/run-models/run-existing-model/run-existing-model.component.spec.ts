import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { RunExistingModelComponent } from './run-existing-model.component';

describe('RunExistingModelComponent', () => {
  let component: RunExistingModelComponent;
  let fixture: ComponentFixture<RunExistingModelComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ RunExistingModelComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(RunExistingModelComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
