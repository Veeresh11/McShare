export interface IShares {
  id?: number;
  numShares?: number | null;
  sharePrice?: number | null;
}

export class Shares implements IShares {
  constructor(public id?: number, public numShares?: number | null, public sharePrice?: number | null) {}
}

export function getSharesIdentifier(shares: IShares): number | undefined {
  return shares.id;
}
