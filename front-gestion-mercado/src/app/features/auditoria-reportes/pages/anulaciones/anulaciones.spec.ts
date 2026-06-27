import { ComponentFixture, TestBed } from '@angular/core/testing';

import { Anulaciones } from './anulaciones';

describe('Anulaciones', () => {
  let component: Anulaciones;
  let fixture: ComponentFixture<Anulaciones>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [Anulaciones],
    }).compileComponents();

    fixture = TestBed.createComponent(Anulaciones);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
