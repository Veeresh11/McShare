jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IIndividual, Individual } from '../individual.model';
import { IndividualService } from '../service/individual.service';

import { IndividualRoutingResolveService } from './individual-routing-resolve.service';

describe('Service Tests', () => {
  describe('Individual routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: IndividualRoutingResolveService;
    let service: IndividualService;
    let resultIndividual: IIndividual | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(IndividualRoutingResolveService);
      service = TestBed.inject(IndividualService);
      resultIndividual = undefined;
    });

    describe('resolve', () => {
      it('should return IIndividual returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 'ABC' };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultIndividual = result;
        });

        // THEN
        expect(service.find).toBeCalledWith('ABC');
        expect(resultIndividual).toEqual({ id: 'ABC' });
      });

      it('should return new IIndividual if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultIndividual = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultIndividual).toEqual(new Individual());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 'ABC' };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultIndividual = result;
        });

        // THEN
        expect(service.find).toBeCalledWith('ABC');
        expect(resultIndividual).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
