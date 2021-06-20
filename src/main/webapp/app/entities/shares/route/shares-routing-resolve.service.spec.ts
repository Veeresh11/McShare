jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IShares, Shares } from '../shares.model';
import { SharesService } from '../service/shares.service';

import { SharesRoutingResolveService } from './shares-routing-resolve.service';

describe('Service Tests', () => {
  describe('Shares routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: SharesRoutingResolveService;
    let service: SharesService;
    let resultShares: IShares | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(SharesRoutingResolveService);
      service = TestBed.inject(SharesService);
      resultShares = undefined;
    });

    describe('resolve', () => {
      it('should return IShares returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultShares = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultShares).toEqual({ id: 123 });
      });

      it('should return new IShares if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultShares = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultShares).toEqual(new Shares());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultShares = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultShares).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
