# Machine Learning Medic Vision Scan

Machine learning to detect and classify cataract disesase
We'll use TensorFlow v2 to create an image classification model, train it with a eyes with and without cataract dataset

The model is based on a pre-trained version of SSD-MobileNet V2. We'll start by retraining only the classification layers, reusing MobileNet's pre-trained feature extractor layers. Then we'll fine-tune the model by updating weights in some of the feature extractor layers. This type of transfer learning is much faster than training the entire model from scratch.

Once it's trained, we'll save the model in h5 format

# Import required libraries
In this notebook, we used several libraries to build the model and training model such as :
- TensorFlow
- NumPy
- Matplotlib Pyplot
- Pandas

# Prepare the training data
First let's download and organize the eyes with and without cataract disease dataset we'll use to retrain the model. Here we use an example of an eyes with cataract disease dataset image from [roboflow](https://universe.roboflow.com/cataract/cataract-v01/dataset/7). Beside the dataset from roboflow we also used Google Image Scrape to collecting dataset.

From several datasets, we combined into one [dataset](https://github.com/NEAR07/Medic-Vision-Scan/tree/master/Machine_Learning/Dataset)

We use ImageDataGenerator to rescale the image data into float values (divide by 255 so the tensor values are between 0 and 1), and call flow_from_directory() to create three generators: one for the training dataset, one for the validation dataset, and one for the test dataset.

On each iteration, these generators provide a batch of images by reading images from disk and processing them to the proper tensor size (160 x 160). The output is a tuple of (images, labels).

# Build the model
We'll create a model that's capable of transfer learning on just the last fully-connected layer.

We'll start with MobileNet V2 from Keras as the base model, which is pre-trained with the ImageNet dataset (trained to recognize 1,000 classes). This provides us a great feature extractor for image classification and we can then train a new classification layer with our dataset.

note : Paper [Mobilenet-SSDv2: An Improved Object Detection Model for Embedded Systems](https://ieeexplore.ieee.org/document/9219319)

# Create the base model
When instantiating the MobileNet V2, we specify the include_top=False argument in order to load the network without the classification layers at the top. Then we set trainable false to freeze all the weights in the base model. This effectively converts the model into a feature extractor because all the pre-trained weights and biases are preserved in the lower layers when we begin training for our classification head.

# Add a classification head
Now we create a new Sequential model and pass the frozen MobileNet V2 as the base of the graph, and append new classification layers so we can set the final output dimension to match the number of classes in our dataset.

# Configure the model
Although this method is called compile(), it's basically a configuration step that's required before we can start training. And because the majority of the model graph is frozen in the base model, weights from only the last convolution and dense layers are trainable.

# Train the model
Now we can train the model using data provided by the train_generator and val_generator that we created at the beginning.

# Fine tune the base model
So far, we've only trained the classification layers—the weights of the pre-trained network were not changed.

One way we can increase the accuracy is to train (or "fine-tune") more layers from the pre-trained model. That is, we'll un-freeze some layers from the base model and adjust those weights (which were originally trained with 1,000 ImageNet classes) so they're better tuned for features found in our cataract disease dataset.

# Un-freeze more layers
So instead of freezing the entire base model, we'll freeze individual layers.

# Reconfigure the model
Now configure the model again, but this time with a lower learning rate

# Continue training
Now let's fine-tune all trainable layers. This starts with the weights we already trained in the classification layers, so we don't need as many epochs.

Our model better, but it's not ideal.

The validation loss is still higher than the training loss, so there could be some overfitting during training. The overfitting might also be because the new training set is relatively small with less intra-class variance, compared to the original ImageNet dataset used to train MobileNet V2.

So this model isn't trained to an accuracy that's production ready, but it works well enough as a demonstration.

# Save the model
You can check the model we've been build for this [project](https://github.com/NEAR07/Medic-Vision-Scan/tree/master/Machine_Learning/Result_model)
