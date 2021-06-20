import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICorporate, getCorporateIdentifier } from '../corporate.model';

export type EntityResponseType = HttpResponse<ICorporate>;
export type EntityArrayResponseType = HttpResponse<ICorporate[]>;

@Injectable({ providedIn: 'root' })
export class CorporateService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/corporates');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(corporate: ICorporate): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(corporate);
    return this.http
      .post<ICorporate>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(corporate: ICorporate): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(corporate);
    return this.http
      .put<ICorporate>(`${this.resourceUrl}/${getCorporateIdentifier(corporate) as string}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(corporate: ICorporate): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(corporate);
    return this.http
      .patch<ICorporate>(`${this.resourceUrl}/${getCorporateIdentifier(corporate) as string}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http
      .get<ICorporate>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ICorporate[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: string): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addCorporateToCollectionIfMissing(
    corporateCollection: ICorporate[],
    ...corporatesToCheck: (ICorporate | null | undefined)[]
  ): ICorporate[] {
    const corporates: ICorporate[] = corporatesToCheck.filter(isPresent);
    if (corporates.length > 0) {
      const corporateCollectionIdentifiers = corporateCollection.map(corporateItem => getCorporateIdentifier(corporateItem)!);
      const corporatesToAdd = corporates.filter(corporateItem => {
        const corporateIdentifier = getCorporateIdentifier(corporateItem);
        if (corporateIdentifier == null || corporateCollectionIdentifiers.includes(corporateIdentifier)) {
          return false;
        }
        corporateCollectionIdentifiers.push(corporateIdentifier);
        return true;
      });
      return [...corporatesToAdd, ...corporateCollection];
    }
    return corporateCollection;
  }

  protected convertDateFromClient(corporate: ICorporate): ICorporate {
    return Object.assign({}, corporate, {
      dateIncorp: corporate.dateIncorp?.isValid() ? corporate.dateIncorp.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.dateIncorp = res.body.dateIncorp ? dayjs(res.body.dateIncorp) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((corporate: ICorporate) => {
        corporate.dateIncorp = corporate.dateIncorp ? dayjs(corporate.dateIncorp) : undefined;
      });
    }
    return res;
  }
}
