import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IIndividual, getIndividualIdentifier } from '../individual.model';

export type EntityResponseType = HttpResponse<IIndividual>;
export type EntityArrayResponseType = HttpResponse<IIndividual[]>;

@Injectable({ providedIn: 'root' })
export class IndividualService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/individuals');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(individual: IIndividual): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(individual);
    return this.http
      .post<IIndividual>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(individual: IIndividual): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(individual);
    return this.http
      .put<IIndividual>(`${this.resourceUrl}/${getIndividualIdentifier(individual) as string}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(individual: IIndividual): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(individual);
    return this.http
      .patch<IIndividual>(`${this.resourceUrl}/${getIndividualIdentifier(individual) as string}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: string): Observable<EntityResponseType> {
    return this.http
      .get<IIndividual>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IIndividual[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: string): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addIndividualToCollectionIfMissing(
    individualCollection: IIndividual[],
    ...individualsToCheck: (IIndividual | null | undefined)[]
  ): IIndividual[] {
    const individuals: IIndividual[] = individualsToCheck.filter(isPresent);
    if (individuals.length > 0) {
      const individualCollectionIdentifiers = individualCollection.map(individualItem => getIndividualIdentifier(individualItem)!);
      const individualsToAdd = individuals.filter(individualItem => {
        const individualIdentifier = getIndividualIdentifier(individualItem);
        if (individualIdentifier == null || individualCollectionIdentifiers.includes(individualIdentifier)) {
          return false;
        }
        individualCollectionIdentifiers.push(individualIdentifier);
        return true;
      });
      return [...individualsToAdd, ...individualCollection];
    }
    return individualCollection;
  }

  protected convertDateFromClient(individual: IIndividual): IIndividual {
    return Object.assign({}, individual, {
      dob: individual.dob?.isValid() ? individual.dob.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.dob = res.body.dob ? dayjs(res.body.dob) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((individual: IIndividual) => {
        individual.dob = individual.dob ? dayjs(individual.dob) : undefined;
      });
    }
    return res;
  }
}
