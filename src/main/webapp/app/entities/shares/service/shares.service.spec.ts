import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IShares, Shares } from '../shares.model';

import { SharesService } from './shares.service';

describe('Service Tests', () => {
  describe('Shares Service', () => {
    let service: SharesService;
    let httpMock: HttpTestingController;
    let elemDefault: IShares;
    let expectedResult: IShares | IShares[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(SharesService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        numShares: 0,
        sharePrice: 0,
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign({}, elemDefault);

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a Shares', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new Shares()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Shares', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            numShares: 1,
            sharePrice: 1,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Shares', () => {
        const patchObject = Object.assign(
          {
            numShares: 1,
          },
          new Shares()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Shares', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            numShares: 1,
            sharePrice: 1,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a Shares', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addSharesToCollectionIfMissing', () => {
        it('should add a Shares to an empty array', () => {
          const shares: IShares = { id: 123 };
          expectedResult = service.addSharesToCollectionIfMissing([], shares);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(shares);
        });

        it('should not add a Shares to an array that contains it', () => {
          const shares: IShares = { id: 123 };
          const sharesCollection: IShares[] = [
            {
              ...shares,
            },
            { id: 456 },
          ];
          expectedResult = service.addSharesToCollectionIfMissing(sharesCollection, shares);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Shares to an array that doesn't contain it", () => {
          const shares: IShares = { id: 123 };
          const sharesCollection: IShares[] = [{ id: 456 }];
          expectedResult = service.addSharesToCollectionIfMissing(sharesCollection, shares);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(shares);
        });

        it('should add only unique Shares to an array', () => {
          const sharesArray: IShares[] = [{ id: 123 }, { id: 456 }, { id: 47015 }];
          const sharesCollection: IShares[] = [{ id: 123 }];
          expectedResult = service.addSharesToCollectionIfMissing(sharesCollection, ...sharesArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const shares: IShares = { id: 123 };
          const shares2: IShares = { id: 456 };
          expectedResult = service.addSharesToCollectionIfMissing([], shares, shares2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(shares);
          expect(expectedResult).toContain(shares2);
        });

        it('should accept null and undefined values', () => {
          const shares: IShares = { id: 123 };
          expectedResult = service.addSharesToCollectionIfMissing([], null, shares, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(shares);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
