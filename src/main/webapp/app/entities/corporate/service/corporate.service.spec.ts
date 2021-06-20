import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ICorporate, Corporate } from '../corporate.model';

import { CorporateService } from './corporate.service';

describe('Service Tests', () => {
  describe('Corporate Service', () => {
    let service: CorporateService;
    let httpMock: HttpTestingController;
    let elemDefault: ICorporate;
    let expectedResult: ICorporate | ICorporate[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(CorporateService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 'AAAAAAA',
        dateIncorp: currentDate,
        regNo: 'AAAAAAA',
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            dateIncorp: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        service.find('ABC').subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a Corporate', () => {
        const returnedFromService = Object.assign(
          {
            id: 'ID',
            dateIncorp: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            dateIncorp: currentDate,
          },
          returnedFromService
        );

        service.create(new Corporate()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Corporate', () => {
        const returnedFromService = Object.assign(
          {
            id: 'BBBBBB',
            dateIncorp: currentDate.format(DATE_TIME_FORMAT),
            regNo: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            dateIncorp: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Corporate', () => {
        const patchObject = Object.assign(
          {
            dateIncorp: currentDate.format(DATE_TIME_FORMAT),
          },
          new Corporate()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign(
          {
            dateIncorp: currentDate,
          },
          returnedFromService
        );

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Corporate', () => {
        const returnedFromService = Object.assign(
          {
            id: 'BBBBBB',
            dateIncorp: currentDate.format(DATE_TIME_FORMAT),
            regNo: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            dateIncorp: currentDate,
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a Corporate', () => {
        service.delete('ABC').subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addCorporateToCollectionIfMissing', () => {
        it('should add a Corporate to an empty array', () => {
          const corporate: ICorporate = { id: 'ABC' };
          expectedResult = service.addCorporateToCollectionIfMissing([], corporate);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(corporate);
        });

        it('should not add a Corporate to an array that contains it', () => {
          const corporate: ICorporate = { id: 'ABC' };
          const corporateCollection: ICorporate[] = [
            {
              ...corporate,
            },
            { id: 'CBA' },
          ];
          expectedResult = service.addCorporateToCollectionIfMissing(corporateCollection, corporate);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Corporate to an array that doesn't contain it", () => {
          const corporate: ICorporate = { id: 'ABC' };
          const corporateCollection: ICorporate[] = [{ id: 'CBA' }];
          expectedResult = service.addCorporateToCollectionIfMissing(corporateCollection, corporate);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(corporate);
        });

        it('should add only unique Corporate to an array', () => {
          const corporateArray: ICorporate[] = [{ id: 'ABC' }, { id: 'CBA' }, { id: 'Account' }];
          const corporateCollection: ICorporate[] = [{ id: 'ABC' }];
          expectedResult = service.addCorporateToCollectionIfMissing(corporateCollection, ...corporateArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const corporate: ICorporate = { id: 'ABC' };
          const corporate2: ICorporate = { id: 'CBA' };
          expectedResult = service.addCorporateToCollectionIfMissing([], corporate, corporate2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(corporate);
          expect(expectedResult).toContain(corporate2);
        });

        it('should accept null and undefined values', () => {
          const corporate: ICorporate = { id: 'ABC' };
          expectedResult = service.addCorporateToCollectionIfMissing([], null, corporate, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(corporate);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
