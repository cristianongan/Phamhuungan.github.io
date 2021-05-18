export interface IConnection {
  connectionId?: number;
  connectionName?: string;
  connectionUrl?: string;
  driverClassName?: string;
  userName?: string;
  passWord?: string;
  configFlowDTOS?: string;
}

export class ConnectionModel implements IConnection {
  constructor(
    public connectionId?: number,
    public connectionName?: string,
    public connectionUrl?: string,
    public driverClassName?: string,
    public userName?: string,
    public passWord?: string,
    public configFlowDTOS?: string,
  ) {}
}
