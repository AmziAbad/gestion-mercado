import { TestBed } from '@angular/core/testing';

import { AuditoriaReportesApi } from './auditoria-reportes-api';

describe('AuditoriaReportesApi', () => {
  let service: AuditoriaReportesApi;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AuditoriaReportesApi);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
