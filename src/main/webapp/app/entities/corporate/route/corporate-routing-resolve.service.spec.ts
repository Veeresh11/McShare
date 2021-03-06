jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ICorporate, Corporate } from '../corporate.model';
import { CorporateService } from '../service/corporate.service';

import { CorporateRoutingResolveService } from './corporate-routing-resolve.service';

describe('Service Tests', () => {
  describe('Corporate routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: CorporateRoutingResolveService;
    let service: CorporateService;
    let resultCorporate: ICorporate | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(CorporateRoutingResolveService);
      service = TestBed.inject(CorporateService);
      resultCorporate = undefined;
    });

    describe('resolve', () => {
      it('should return ICorporate returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 'ABC' };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultCorporate = result;
        });

        // THEN
        expect(service.find).toBeCalledWith('ABC');
        expect(resultCorporate).toEqual({ id: 'ABC' });
      });

      it('should return new ICorporate if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultCorporate = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultCorporate).toEqual(new Corporate());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 'ABC' };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultCorporate = result;
        });

        // THEN
        expect(service.find).toBeCalledWith('ABC');
        expect(resultCorporate).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
