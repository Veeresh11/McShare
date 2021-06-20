import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IShares, getSharesIdentifier } from '../shares.model';

export type EntityResponseType = HttpResponse<IShares>;
export type EntityArrayResponseType = HttpResponse<IShares[]>;

@Injectable({ providedIn: 'root' })
export class SharesService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/shares');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(shares: IShares): Observable<EntityResponseType> {
    return this.http.post<IShares>(this.resourceUrl, shares, { observe: 'response' });
  }

  update(shares: IShares): Observable<EntityResponseType> {
    return this.http.put<IShares>(`${this.resourceUrl}/${getSharesIdentifier(shares) as number}`, shares, { observe: 'response' });
  }

  partialUpdate(shares: IShares): Observable<EntityResponseType> {
    return this.http.patch<IShares>(`${this.resourceUrl}/${getSharesIdentifier(shares) as number}`, shares, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IShares>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IShares[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addSharesToCollectionIfMissing(sharesCollection: IShares[], ...sharesToCheck: (IShares | null | undefined)[]): IShares[] {
    const shares: IShares[] = sharesToCheck.filter(isPresent);
    if (shares.length > 0) {
      const sharesCollectionIdentifiers = sharesCollection.map(sharesItem => getSharesIdentifier(sharesItem)!);
      const sharesToAdd = shares.filter(sharesItem => {
        const sharesIdentifier = getSharesIdentifier(sharesItem);
        if (sharesIdentifier == null || sharesCollectionIdentifiers.includes(sharesIdentifier)) {
          return false;
        }
        sharesCollectionIdentifiers.push(sharesIdentifier);
        return true;
      });
      return [...sharesToAdd, ...sharesCollection];
    }
    return sharesCollection;
  }
}
