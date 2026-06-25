import { ComponentFixture, TestBed } from '@angular/core/testing';

import { Cobranzas } from './cobranzas';

describe('Cobranzas', () => {
  let component: Cobranzas;
  let fixture: ComponentFixture<Cobranzas>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [Cobranzas],
    }).compileComponents();

    fixture = TestBed.createComponent(Cobranzas);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
