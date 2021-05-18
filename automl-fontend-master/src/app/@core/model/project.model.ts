export interface Iproject {
  projectId?: number;
  projectName?: string;
  description?: string;
}
export class ProjectModel implements Iproject {
  constructor(
    public projectId?: number,
    public projectName?: string,
    public description?: string
  ) {}
}
