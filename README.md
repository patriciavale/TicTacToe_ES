# TicTacToe
A Tic-Tac-Toe full stack application. . Made for the Software Engineering (ES) course, in University of Aveiro.

## Introduction
Play Tic-Tac-Toe using a smartphone as controller. Using the **accelerometer** to make a movement, the system translates it into a play. The game can be **shown on any web browser**, and can have multiple spectators. The game is played by two human players, both with smartphones. The devices are mapped to a single game at the beginning, and can be used remotely, giving the game an automatic multiplayer experience.

## Objective
The main goal of the project is to use a smartphone as a controller for a game of Tic-Tac-Toe. The system provides a platform where the players can start a new game and watch the current progress, and a app for smartphones which translates movements. One of the main goals of the system is to **translate phone movements** into plays. This adds a new level of interactivity and differentiates this project.

<img src="https://github.com/patriciavale/TicTacToe_ES/blob/main/img/example.png" width="400">

The user only needs two smartphones and any device that runs a browser to play.

## Features
- Register the movement made on a smartphone
- Translate the movement into a play
- Apply the translated movement on the current game
- Ability to start games, binding the devices at the beginning
- Multiple spectators in real-time
- Ability to play multiplayer
- Ability to play remotely

## High level architecture
The main components of the system are:
- Controllers (Smarthphones)
- The system itself
- The device (browser) showing the game

The system architecture should be invisible to the user itself. All the internal information flow shouldn't matter to the user's end goal/objective.

<img src="https://github.com/patriciavale/TicTacToe_ES/blob/main/img/higharch.png" width="500">

## Architecture
The architecture has a central point a Kafka broker. The smartphone actions triggers data to Kafka which in turn triggers jobs in modules, thus making and Event Driven Architecture.

<img src="https://github.com/patriciavale/TicTacToe_ES/blob/main/img/arch.png" width="600">

Each module performs a specific and isolated function. For real-time, event driven information the Kafka is used. However for get static information such a game information, some modules might have a REST API, which in turn is used by Web Server.

## Credits
This project was made by Patrícia Vale and Davide Cruz, for the Software Engineering (ES) course in University of Aveiro, in 2019.
