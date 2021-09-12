# Electronic Data File
This is simple Spring application to store any kind of files. 

It stores uploaded files in storage and allows to download them using some user name and filename. Files are identified by pair `<userName, filename>`.  


## Configuration

In `resources/application.properties` set up `storage.path` and configure database connection. It should work with databases supported by JPA Repository.

# REST API

The REST API is described below.

## Download file

### Request

`GET /download/?filename=FILENAME.STH&username=USERNAME`

  eg. http://localhost:8083/download/?filename=testVideo.mp4&username=test

### Response
    Returns stored file
    
    Content-Type depends on file extension and is set automaticly.


## Upload file

### Request

`POST /upload/?username=USERNAME` and body with form-data: {filename: FILE.STH}

  eg. http://localhost:8083/upload?username=test form-data: {filename: testVideo.mp4}

### Response

    OK - file added
   
    ALREADY_ADDED - file for the sended user name and the filename already exists in application
