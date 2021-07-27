import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BackofficeListProblemComponent } from './backoffice-list-problem.component';

describe('BackofficeListProblemComponent', () => {
  let component: BackofficeListProblemComponent;
  let fixture: ComponentFixture<BackofficeListProblemComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ BackofficeListProblemComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(BackofficeListProblemComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
