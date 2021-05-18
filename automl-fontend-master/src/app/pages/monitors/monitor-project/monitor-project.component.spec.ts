import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { MonitorProjectComponent } from './monitor-project.component';

describe('MonitorProjectComponent', () => {
  let component: MonitorProjectComponent;
  let fixture: ComponentFixture<MonitorProjectComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ MonitorProjectComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(MonitorProjectComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
