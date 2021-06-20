import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IShares, Shares } from '../shares.model';
import { SharesService } from '../service/shares.service';

@Injectable({ providedIn: 'root' })
export class SharesRoutingResolveService implements Resolve<IShares> {
  constructor(protected service: SharesService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IShares> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((shares: HttpResponse<Shares>) => {
          if (shares.body) {
            return of(shares.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Shares());
  }
}
