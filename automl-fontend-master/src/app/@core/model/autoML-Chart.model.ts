export interface Ichart {
  percent?: number;
  statusCode?: number;
}

export class AutoMLChartModel implements Ichart {
  constructor(
    public percent?: number,
    public statusCode?: number
  ) {}
}
