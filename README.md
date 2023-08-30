# Mind Wellness Socket Chat

Welcome to the Mind Wellness Socket Chat project! This repository is a simple chat application that utilizes the powerful STOMP protocol for seamless communication. The application consists of a Spring Boot backend server, a user-friendly Android client, and a convenient HTML-based messaging interface for browsers.

## Table of Contents
* Introduction
* Features
* Technologies
* Getting Started
* Usage

## Introduction
This project mainly focused on scalability. Used MySQL relational DB for maintaining ACID property properly. 

## Features

* Real-time messaging with the STOMP protocol.
* Use Rest protocol for managing other communication.
* Implement JWT token based authentication.
* Spring Boot backend server for handling communication.
* Implement users Avatar.
* Delivery/Seen status indicators.
* Browser-based messaging interface for convenient communication.

## Technology
* Spring Boot
* MySql
* Android (StompProtocolAndroid)
* HTML, CSS, JavaScript (for the browser-based interface)
* Websocket Protocol
* REST API 
* Token based authentication 

## Getting Started
To get started with the project, follow these steps:
* Install Java 17 
* Install MySql
* Setup Android Studio 
* Create a DB called socket_chat or other you want, just change the name from application.properties
* Open the WebSocketServer project as maven project. Run the project using 
```mvn spring-boot:run```
* Import AndroidClient in Android Studio 
* Change the IP_ADDRESS(Should be the server IP address) from Cons.Java file inside the package com.socket.chat.conf
* Then run the client register the account and do the chatting

## Improvement & known issue
* Test case for Server and Android
* Need to create more robust web front end
* Implement pagination in the mobile app
* Need to implement the message receiver event in the chat list  
* Need to check the ID verification on the server side on subscription with the stompclient 
* Feature like group chat, friends, block user can be implemented
* Introduce BOT 

## Video Demo
https://youtu.be/SW0WDbNru7Q
