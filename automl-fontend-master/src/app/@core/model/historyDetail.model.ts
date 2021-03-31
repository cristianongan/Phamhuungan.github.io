export interface IhistoryDetail {
  runNote?: string;
  modelCore?: string;
  tasks?: number[];
  historyId?: number;
  inferTable?: string;
}
export class HistoryDetailModel implements IhistoryDetail {
  constructor(
    public runNote?: string,
    public modelCore?: string,
    public tasks?: number[],
    public historyId?: number,
    public inferTable?: string
  ) {}
}
