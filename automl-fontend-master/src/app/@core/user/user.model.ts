export interface IUser {
  id?: any;
  login?: string;
  firstName?: string;
  lastName?: string;
  email?: string;
  activated?: boolean;
  alarmLeader?: boolean;
  langKey?: string;
  authorities?: string[];
  domains?: string[];
  createdBy?: string;
  createdDate?: Date;
  lastModifiedBy?: string;
  lastModifiedDate?: Date;
  password?: string;
  phone?: string;
}

export class User implements IUser {
  constructor(
    public index?: number,
    public id?: any,
    public login?: string,
    public firstName?: string,
    public lastName?: string,
    public email?: string,
    public activated?: boolean,
    public alarmLeader?: boolean,
    public langKey?: string,
    public authorities?: string[],
    public domains?: string[],
    public createdBy?: string,
    public createdDate?: Date,
    public lastModifiedBy?: string,
    public lastModifiedDate?: Date,
    public password?: string,
    public phone?: string,
    public ssoUserId?: string
  ) {}
}
