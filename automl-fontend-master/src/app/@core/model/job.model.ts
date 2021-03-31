export interface IJob {
  base64Img?: string;
  base64File?: string;
  description?: string;
  fileURL?: string;
  jobId?: any;
  jobName?: string;
  oldFile?: string;
  oldImage?: string;
}

export class Job implements IJob {
  constructor(
    public base64Img?: string,
    public base64File?: string,
    public description?: string,
    public fileURL?: string,
    public jobId?: any,
    public jobName?: string,
    public oldImage?: string,
    public oldFile?: string,
  ) {}
}
