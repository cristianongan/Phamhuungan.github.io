export interface IlistParameter {
  parameterId?:   number;
  parameterName?: string;
  parameterType?: number;
  modelTypeId?:   number;
  dataType?:      number;
}
export class ListParameter implements  IlistParameter {
  constructor(
    public parameterId?:   number,
    public parameterName?: string,
    public parameterType?: number,
    public modelTypeId?:   number,
    public dataType?:      number
  ) {
  }
}
