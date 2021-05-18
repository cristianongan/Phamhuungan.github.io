import {HistoryDetailModel} from './historyDetail.model';

export interface ImodelDetail {
  modelName?: string;
  description?: string;
  projectName?: string;
  createTime?: string;
  createUser?: string;
  modelMode?: string;
  bestModelType?: string;
  tasks?: number[];
  historyDTOS?: HistoryDetailModel;
}

export class ModelDetailModel implements ImodelDetail {
  constructor(
    public modelName?: string,
    public description?: string,
    public projectName?: string,
    public createTime?: string,
    public createUser?: string,
    public modelMode?: string,
    public bestModelType?: string,
    public tasks?: number[],
  public historyDTOS?: HistoryDetailModel,
  ) {}
}
