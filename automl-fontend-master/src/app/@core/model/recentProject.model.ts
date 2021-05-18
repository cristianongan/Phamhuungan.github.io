export interface IrecentProject {
  modelId?: number;
  modelName?: string;
  projectId?: string;
  projectName?: string;
}
export class RecentProjectModel implements IrecentProject {
  constructor(
   public modelId?: number,
  public modelName?: string,
  public projectId?: string,
  public tasks?: number[],
  public projectName?: string
  ) {
  }
}
