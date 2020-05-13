import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { FinalScoreboardComponent } from './final-scoreboard.component';

describe('FinalScoreboardComponent', () => {
  let component: FinalScoreboardComponent;
  let fixture: ComponentFixture<FinalScoreboardComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ FinalScoreboardComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(FinalScoreboardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
