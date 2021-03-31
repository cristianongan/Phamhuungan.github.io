export interface RunNewModel {
  modelDTO?:      ModelDTO;
  configFlowDTO?: ConfigFlowDTO;
  subModelDTOS?:  SubModelDTO[];
  projectDTO?:    ProjectDTO;
  modelTypeDTOS?: ModelTypeDTO;
  connectionId?:  number;
}

export interface ConfigFlowDTO {
  runNode?:              string;
  task?:                 string;
  schedule?:             null;
  connectionId?:         number;
  parameterString?:      string;
  trainingTable?:         string;
  validationTable?:      string;
  testingTable?:         string;
  inferenceTable?:       string;
  featureColumns?:       string;
  labelColumn?:         string;
  outputColumns?:        string;
  outputTableName?:      string;
  outputTableMode?:      number;
  outputTablePartition?: string;
  location?:             string;
}

export interface ModelDTO {
  modelName?:             string;
  description?:           string;
  modelMode?:             number;
  algorithmType?:         number;
  metrics?:               number;
  biggerIsBetter?:        number;
  autoTurningType?:       number;
  numberOfFolds?:         number;
  maxTrialTime?:          number;
  checkpointStep?:        number;
  optimizationAlgorithm?: string;
  dataSubsamplingRatio?:  number;
  allowPersist?:          number;
}

export interface ModelTypeDTO {
  parameterDTOS?: ModelTypeDTOParameterDTO[];
}

export interface ModelTypeDTOParameterDTO {
  parameterName?: string;
  parameterId?:   number;
  parameterType?: number;
  subModelId?:    number;
  modelTypeId?:   number;
  dataType?:      number;
  min?:           number;
  max?:           number;
  uniform?:       number;
  step?:          number;
}

export interface ProjectDTO {
  projectId?: number;
  projectName?: string;
  description?: string;
}

export interface SubModelDTO {
  modelTypeId?:   number;
  subModelName?:  string;
  parameterDTOS?: SubModelDTOParameterDTO[];
}

export interface SubModelDTOParameterDTO {
  parameterName?:  string;
  parameterId?:    number;
  parameterType?:  number;
  subModelId?:     number;
  modelTypeId?:    number;
  dataType?:       number;
  parameterValue?: string;
}
