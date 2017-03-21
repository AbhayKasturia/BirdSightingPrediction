# BirdSightingPrediction
Distributed Parallel Algorithm to predict the presence or absence of the Red-winged Blackbird in each birding session with an 83% accuracy

# Key Points
1. Labelled Data is split into 80-20 for building a model and validation, with only the required attributes.
2. Ensemble algorithm is used with building  a configurable number of models of Random Trees and the final prediction is achieved via Voting.
3. Each Random Tree is built in parallel on separate worker machines with tuned parameters which are set according to the suggestions made in recent studies and  experimental findings.
4. Predictions are done in parallel and is scalable to the number of  records to be predicted.

# Approach
3 MapReduce Jobs are implemented that achieve the following tasks :
- Performing preprocessing on the labelled data - Job 1
- Building the model - Job 1
- Validation of the model and finding the accuracy - Job 2
- Predictions - Job 3

# Job 1 - Preprocessing and Modelling
