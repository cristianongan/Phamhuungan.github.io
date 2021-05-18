package com.viettel.automl.dto.object;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchSpaceDTO {
	private String type;
	private String featureSubsetStrategy;
	private String numTrees;
	private String maxDepth;
	private String maxBins;
	private String minInstancesPerNode;
	private String subsamplingRate;
	private String minInfoGain;
}
