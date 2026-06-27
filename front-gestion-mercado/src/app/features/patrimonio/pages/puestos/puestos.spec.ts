import { ComponentFixture, TestBed } from '@angular/core/testing';

import { Puestos } from './puestos';

describe('Puestos', () => {
  let component: Puestos;
  let fixture: ComponentFixture<Puestos>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [Puestos],
    }).compileComponents();

    fixture = TestBed.createComponent(Puestos);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
