export interface IconfigFlow {
  configFlowId?:         number;
  projectId?:            number;
  modelId?:              number;
  runNote?:              string;
  task?:                 string;
  schedule?:             string;
  outputTablePartition?: string;
  connectionId?:         number;
  parameterString?:      string;
  trainingTable?:        string;
  validationTable?:      string;
  testingTable?:         string;
  inferenceTable?:       string;
  featureColumnArr?:      [];
  labelColumn?:          string;
  outputColumnArr?:        [];
  createTime?:           string;
  createUser?:           string;
  outputTableName?:           string;
  location?:           string;
  runType?:           number;
}
