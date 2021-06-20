import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IIndividual, Individual } from '../individual.model';

import { IndividualService } from './individual.service';

describe('Service Tests', () => {
  describe('Individual Service', () => {
    let service: IndividualService;
    let httpMock: HttpTestingController;
    let elemDefault: IIndividual;
    let expectedResult: IIndividual | IIndividual[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(IndividualService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 'AAAAAAA',
        dob: currentDate,
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            dob: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        service.find('ABC').subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a Individual', () => {
        const returnedFromService = Object.assign(
          {
            id: 'ID',
            dob: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            dob: currentDate,
          },
          returnedFromService
        );

        service.create(new Individual()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Individual', () => {
        const returnedFromService = Object.assign(
          {
            id: 'BBBBBB',
            dob: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            dob: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Individual', () => {
        const patchObject = Object.assign({}, new Individual());

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign(
          {
            dob: currentDate,
          },
          returnedFromService
        );

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Individual', () => {
        const returnedFromService = Object.assign(
          {
            id: 'BBBBBB',
            dob: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            dob: currentDate,
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a Individual', () => {
        service.delete('ABC').subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addIndividualToCollectionIfMissing', () => {
        it('should add a Individual to an empty array', () => {
          const individual: IIndividual = { id: 'ABC' };
          expectedResult = service.addIndividualToCollectionIfMissing([], individual);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(individual);
        });

        it('should not add a Individual to an array that contains it', () => {
          const individual: IIndividual = { id: 'ABC' };
          const individualCollection: IIndividual[] = [
            {
              ...individual,
            },
            { id: 'CBA' },
          ];
          expectedResult = service.addIndividualToCollectionIfMissing(individualCollection, individual);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Individual to an array that doesn't contain it", () => {
          const individual: IIndividual = { id: 'ABC' };
          const individualCollection: IIndividual[] = [{ id: 'CBA' }];
          expectedResult = service.addIndividualToCollectionIfMissing(individualCollection, individual);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(individual);
        });

        it('should add only unique Individual to an array', () => {
          const individualArray: IIndividual[] = [{ id: 'ABC' }, { id: 'CBA' }, { id: 'Platinum' }];
          const individualCollection: IIndividual[] = [{ id: 'ABC' }];
          expectedResult = service.addIndividualToCollectionIfMissing(individualCollection, ...individualArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const individual: IIndividual = { id: 'ABC' };
          const individual2: IIndividual = { id: 'CBA' };
          expectedResult = service.addIndividualToCollectionIfMissing([], individual, individual2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(individual);
          expect(expectedResult).toContain(individual2);
        });

        it('should accept null and undefined values', () => {
          const individual: IIndividual = { id: 'ABC' };
          expectedResult = service.addIndividualToCollectionIfMissing([], null, individual, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(individual);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
