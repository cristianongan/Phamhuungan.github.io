export interface ModelDTOModel {
  modelName:             string;
  description:           string;
  modelMode:             number;
  algorithmType:         number;
  metrics:               number;
  biggerIsBetter:        number;
  autoTurningType:       number;
  numberOfFolds:         number;
  maxTrialTime:          number;
  checkpointStep:        number;
  optimizationAlgorithm: string;
  dataSubsamplingRatio:  number;
  allowPersist:          number;
}
