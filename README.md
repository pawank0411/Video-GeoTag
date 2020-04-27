# Video-GeoTag
Smart India Hackathon Problem Statement Number NM380
Here we are developing a mobile application for recording and playing geotagged videos. Unlike photos in which geotag data is of a single point and orientation pair, for videos geotag data is a sequence of point and orientation pairs. The mobile application should have two views.
In one view the recorded video should play while simultaneously plotting field-of-view (orientation) cone and marker on an interactive map in the other view in a synchronized manner. 
The position shown on the map should match the play position of the video.
Our project aims to record and play geo-tagged videos. Whenever our app is used to record videos, the coordinates (latitude and longitude) after every 10 seconds data are written to a kml file. Which is later written to meta data of recorded media file as base-64 encoding.
Once the recording is completed, the coordinates from the meta data are decoded and fetched to the cache memory and is parsed using google map API. Thus, a split screen shows the recorded video in the upper part while the synchronised map tracking is shown in the lower part.
Our app will stand useful for android and iOS as well. 
At the time of recording any video, the tracking process can be done not only with the help of our app but also with the normal video cameras present in the mobile or with any other video recorder but the only necessity is that our app should be present in the mobile. 
This application can be processed with the help of background services. This can be done either with front camera or back camera.
Moreover, an enhancement is done through which we send the coordinates to our database along with time stamp value, while the video is being recorded and is further loaded from the database and parsed using map API so that anyone having the access permission, can track our location through a web application.
The project includes further enhancements like live streaming where we can simultaneously play the video being recorded using our app along with the live tracking facility, video seeking so that we can move forward or backward on the video screen depending on the position of the map screen.
