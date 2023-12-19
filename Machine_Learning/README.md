# Machine Learning Medic Vision Scan

Machine learning to detect and classify cataract disesase
We'll use TensorFlow 2 to create an image classification model, train it with a eyes with and without cataract dataset

The model is based on a pre-trained version of SSD-MobileNet V2. We'll start by retraining only the classification layers, reusing MobileNet's pre-trained feature extractor layers. Then we'll fine-tune the model by updating weights in some of the feature extractor layers. This type of transfer learning is much faster than training the entire model from scratch.

Once it's trained, we'll save the model in h5 format

# Import required libraries
In this notebook, we used several libraries to build the model and training model such as :
- TensorFlow
- NumPy
- Matplotlib Pyplot
- Pandas
