export interface Ifolder {
  path?:              string;
  length?:            number;
  isdir?:             boolean;
  block_replication?: number;
  blocksize?:         number;
  size?:              number;
  modification_time?: number;
  access_time?:       number;
  permission?:        string;
  owner?:             string;
  group?:             string;
}
