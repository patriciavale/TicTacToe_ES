# TicTacToe
A Tic-Tac-Toe full stack application. . Made for the Software Engineering (ES) course, in University of Aveiro.

## Introduction
Play Tic-Tac-Toe using a smartphone as controller. Using the **accelerometer** to make a movement, the system translates it into a play. The game can be **shown on any web browser**, and can have multiple spectators. The game is played by two human players, both with smartphones. The devices are mapped to a single game at the beginning, and can be used remotely, giving the game an automatic multiplayer experience.

## Features
- Register the movement made on a smartphone
- Translate the movement into a play
- Apply the translated movement on the current game
- Ability to start games, binding the devices at the beginning
- Multiple spectators in real-time
- Ability to play multiplayer
- Ability to play remotely

## Architecture
The architecture has a central point a Kafka broker. The smartphone actions triggers data to Kafka which in turn triggers jobs in modules, thus making and Event Driven Architecture.

Each module performs a specific and isolated function. For real-time, event driven information the Kafka is used. However for get static information such a game information, some modules might have a REST API, which in turn is used by Web Server.
