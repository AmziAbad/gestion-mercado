import { TestBed } from '@angular/core/testing';

import { PatrimonioApi } from './patrimonio-api';

describe('PatrimonioApi', () => {
  let service: PatrimonioApi;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PatrimonioApi);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
