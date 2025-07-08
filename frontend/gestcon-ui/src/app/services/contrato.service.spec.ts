import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { ContratoService } from './contrato.service';
import { Contrato } from '../models/contrato.model';

describe('ContratoService', () => {
  let service: ContratoService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [ContratoService]
    });
    service = TestBed.inject(ContratoService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should fetch contratos', () => {
    const dummyContratos: Contrato[] = [
      { id: 1, numeroContrato: '123', objeto: 'Objeto 1' },
      { id: 2, numeroContrato: '456', objeto: 'Objeto 2' }
    ];

    service.getContratos().subscribe(contratos => {
      expect(contratos.length).toBe(2);
      expect(contratos).toEqual(dummyContratos);
    });

    const req = httpMock.expectOne('/api/contratos');
    expect(req.request.method).toBe('GET');
    req.flush(dummyContratos);
  });

  it('should create a contrato', () => {
    const newContrato: Contrato = { id: 3, numeroContrato: '789', objeto: 'Objeto 3' };

    service.createContrato(newContrato).subscribe(contrato => {
      expect(contrato).toEqual(newContrato);
    });

    const req = httpMock.expectOne('/api/contratos');
    expect(req.request.method).toBe('POST');
    req.flush(newContrato);
  });

  it('should update a contrato', () => {
    const updatedContrato: Contrato = { id: 1, numeroContrato: '123', objeto: 'Objeto atualizado' };

    service.updateContrato(1, updatedContrato).subscribe(contrato => {
      expect(contrato).toEqual(updatedContrato);
    });

    const req = httpMock.expectOne('/api/contratos/1');
    expect(req.request.method).toBe('PUT');
    req.flush(updatedContrato);
  });

  it('should delete a contrato', () => {
    service.deleteContrato(1).subscribe(response => {
      expect(response).toBeNull();
    });

    const req = httpMock.expectOne('/api/contratos/1');
    expect(req.request.method).toBe('DELETE');
    req.flush(null);
  });
});
