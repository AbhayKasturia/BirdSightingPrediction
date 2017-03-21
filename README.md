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
![alt tag](https://cloud.githubusercontent.com/assets/19328904/24132568/e8324fe6-0dcc-11e7-9417-91f9583cccf5.png)

# Job 1 - Features
- Random Shuffling
- Preprocessing , data split and modelling in a single job
- Partitioner used for load distribution
- Attributes selected for Modelling :
		- Latitude and Longitude
		- Data related to housing density at the sampling site
		- Data related to elevations at the sampling site
		- Data related to water bodies near or at the sampling site
		- Also , data was filtered to get distinct records and remove redundant observations from an observing group members

# Job 2 - Validation
![alt tag](https://cloud.githubusercontent.com/assets/19328904/24132569/e8398964-0dcc-11e7-8e38-8d4035102ad6.png)

# Job 2 - Features
- Minimum I/O - Map only Job
- Hadoop counters used for calculating Accuracy of the model

# Job 3 - Predictions
![alt tag](https://cloud.githubusercontent.com/assets/19328904/24132570/e83dac56-0dcc-11e7-9886-5eda0ee22e18.png)

# Job 3 - Features
- Scalable solution - can use up to “n” machines

# Design Decisions
- Why forest of Random Trees?
Random Trees proves to be the right choice for unstable and skewed Data like ours. Using a forest of Random Trees made us exploit the distributed parallel feature of MapReduce to a good extent to train models parallely on worker machines.
	Also , the accuracy we obtained using forest of Random trees with Bootstrapping/Bagging was the highest out of all the models we explored.
The approach achieves a lower test error solely by variance reduction. Since each random tree is trained on random subset of bagged data and random attributes out of all the attributes.

- Deciding ‘N’ for number of models?
After trying with lot of values for ‘N’ we selected 10 as the Results was efficient and Prediction were reaching an accuracy of 83%.

- Parameters explored for Random Trees?
After researching and experimenting , we found the following parameter to be best suitable for Random trees :
Minimum size of the each node of a tree = 1 ,
Attributes to randomly select for building the tree= square root of total attributes(25) ,
Depth of Tree= 0(Infinity)

# Accuracy RoadMaps

- Single NaiveBayes Weka Model : 67%
- Single Predefined Mboost Weka Pre-Defined: 74%
- Single Random Tree on 1/10th: 75%
- n Random Trees on 1/n  of 80% Training Data: 81%(n=10) 78%(n=100)
- 10 Random Trees with Bagging/Bootstrapping of 80% 
		- Training Data with Bagging :83.7%