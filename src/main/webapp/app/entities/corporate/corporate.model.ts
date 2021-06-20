import * as dayjs from 'dayjs';

export interface ICorporate {
  id?: string;
  dateIncorp?: dayjs.Dayjs | null;
  regNo?: string | null;
}

export class Corporate implements ICorporate {
  constructor(public id?: string, public dateIncorp?: dayjs.Dayjs | null, public regNo?: string | null) {}
}

export function getCorporateIdentifier(corporate: ICorporate): string | undefined {
  return corporate.id;
}
