export interface Isearch {
  modelId?: number;
  modelMode?: number;
  modelName?: string;
  projectName?: string;
  createUser?: string;
  bestModelType?: string;
  tasks?: number[];
  projectId?: number;
}
export class SearchModel implements Isearch {
  constructor(
    public modelId?: number,
    public modelMode?: number,
    public modelName?: string,
    public projectName?: string,
    public createUser?: string,
    public bestModelType?: string,
    public tasks?: number[],
    public projectId?: number
) {}
}
