import { IAddress } from 'app/entities/address/address.model';
import { IShares } from 'app/entities/shares/shares.model';

export interface ICustomer {
  id?: string;
  name?: string | null;
  phone?: string | null;
  address?: IAddress | null;
  share?: IShares | null;
}

export class Customer implements ICustomer {
  constructor(
    public id?: string,
    public name?: string | null,
    public phone?: string | null,
    public address?: IAddress | null,
    public share?: IShares | null
  ) {}
}

export function getCustomerIdentifier(customer: ICustomer): string | undefined {
  return customer.id;
}
