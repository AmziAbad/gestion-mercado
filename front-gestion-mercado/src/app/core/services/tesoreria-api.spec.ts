import { TestBed } from '@angular/core/testing';

import { TesoreriaApi } from './tesoreria-api';

describe('TesoreriaApi', () => {
  let service: TesoreriaApi;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(TesoreriaApi);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
