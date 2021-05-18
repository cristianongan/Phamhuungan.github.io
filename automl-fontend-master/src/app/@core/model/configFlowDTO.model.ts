export interface ConfigFlowDTOModel {
  runNode:              string;
  task:                 string;
  schedule:             null;
  connectionId:         number;
  parameterString:      string;
  traningTable:         string;
  validationTable:      string;
  testingTable:         string;
  inferenceTable:       string;
  featureColumns:       string;
  labelColumns:         string;
  outputColumns:        string;
  outputTableName:      string;
  outputTableMode:      number;
  outputTablePartition: string;
  location:             string;
}
