import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IIndividual, Individual } from '../individual.model';
import { IndividualService } from '../service/individual.service';

@Injectable({ providedIn: 'root' })
export class IndividualRoutingResolveService implements Resolve<IIndividual> {
  constructor(protected service: IndividualService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IIndividual> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((individual: HttpResponse<Individual>) => {
          if (individual.body) {
            return of(individual.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Individual());
  }
}
