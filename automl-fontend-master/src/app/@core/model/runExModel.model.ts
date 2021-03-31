export interface RunExModel {
  modelId?:              number;
  projectId?:            number;
  runNode?:              string;
  task?:                 string;
  schedule?:             string;
  connectionId?:         number;
  parameterString?:      string;
  traningTable?:         string;
  validationTable?:      string;
  testingTable?:         string;
  inferenceTable?:       string;
  featureColumns?:       string;
  labelColumns?:         string;
  outputColumns?:        string;
  outputTableName?:      string;
  outputTableMode?:      number;
  outputTablePartition?: string;
  location?:             string;
}
