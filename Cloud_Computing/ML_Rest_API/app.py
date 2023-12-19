from flask import Flask, jsonify, request, json
from tensorflow.keras.models import load_model
from tensorflow.keras.preprocessing.image import img_to_array, load_img
import tensorflow as tf
import random, base64
from PIL import Image
from io import BytesIO
import numpy as np

# ML Model
model = load_model("model/model_inception.h5")
probability_model = tf.keras.Sequential([model, tf.keras.layers.Softmax()])     # normalize into probability of total 100%
class_names = ["immature", "mature", "normal"]

# Flask REST API
app = Flask(__name__)

@app.route("/", methods=["GET"])
def get_home_handler():
    return jsonify({"success": True, "msg": "ML Rest API"}), 200

@app.route("/predict", methods=["POST"])
def preidct_handler():
    req = json.loads(request.data)
    data = req["data"]
    b64_img_data = data["b64_img_data"]
    decoded_data = base64.b64decode(b64_img_data)

    image = Image.open(BytesIO(decoded_data))
    target_size = (160, 160)
    image = image.resize(target_size)

    x = img_to_array(image)
    x = np.expand_dims(x, axis=0)
    images = np.vstack([x])

    classes = probability_model.predict(images, batch_size=64)
    i = np.argmax(classes[0])
    prediction_result = class_names[i]

    print(f"Prediction classes = {classes}")
    print(f"Prediction result = {prediction_result}")

    filename = data["filename"]
    
    return jsonify({"success": True, "prediction": prediction_result}), 200

if __name__=="__main__":
    app.run(host="0.0.0.0", port=8080, debug=True)