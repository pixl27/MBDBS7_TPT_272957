import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DetailMatchComponent } from './detail-match.component';

describe('DetailMatchComponent', () => {
  let component: DetailMatchComponent;
  let fixture: ComponentFixture<DetailMatchComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ DetailMatchComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(DetailMatchComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
