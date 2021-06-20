import * as dayjs from 'dayjs';

export interface IIndividual {
  id?: string;
  dob?: dayjs.Dayjs | null;
}

export class Individual implements IIndividual {
  constructor(public id?: string, public dob?: dayjs.Dayjs | null) {}
}

export function getIndividualIdentifier(individual: IIndividual): string | undefined {
  return individual.id;
}
