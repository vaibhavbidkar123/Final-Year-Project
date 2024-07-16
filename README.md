# Final-Year-Project

##B.Tech Final Year Project: Reducing Traffic Delay on 2-Way Roads

##Project Overview:
Our project aims to mitigate delays caused by high traffic density on two-way roads by dynamically increasing the bandwidth of roads where traffic density is high or where an ambulance path is identified. 

##The project consists of three main sections:
Hardware: Involves using Raspberry Pi, sensors, etc.
Software: Development of an Android app for ambulance drivers, PWD officials, and traffic police.
Basic Model Demonstration: Conceptual demonstration of the idea.

##My Contribution:
I was responsible for developing the software application for this project.

##Challenges Faced:

###Working with Google Maps API:
Integrating the Google Maps API into the Android app to display real-time traffic data.
Handling API calls and managing the retrieval of necessary data.
Parsing JSON Data:

###Extracting location coordinates from the JSON response received from the Google Maps API.
Using Google Polyline to draw routes on the map based on the parsed coordinates.
Connecting Raspberry Pi and App via Flask Server:

###Setting up a Flask server to facilitate communication between the Raspberry Pi hardware and the Android application.
###Ensuring seamless data transmission and reception between the hardware and software components.
Storing Data in Room Database:

###Designing the database schema to accommodate two types of users: drivers and dividers.
Implementing Room database to store and manage user data efficiently within the application.