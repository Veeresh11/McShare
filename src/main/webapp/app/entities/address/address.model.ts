export interface IAddress {
  id?: number;
  adress1?: string | null;
  address2?: string | null;
  town?: string | null;
  country?: string | null;
}

export class Address implements IAddress {
  constructor(
    public id?: number,
    public adress1?: string | null,
    public address2?: string | null,
    public town?: string | null,
    public country?: string | null
  ) {}
}

export function getAddressIdentifier(address: IAddress): number | undefined {
  return address.id;
}
