import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BackofficeListEmailComponent } from './backoffice-list-email.component';

describe('BackofficeListEmailComponent', () => {
  let component: BackofficeListEmailComponent;
  let fixture: ComponentFixture<BackofficeListEmailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ BackofficeListEmailComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(BackofficeListEmailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
