

// auxiliary variables
// indexes:
// [0, 1, 2
//  3, 4, 5
//  6, 7, 8]
var squares = ["11", "12", "13",
               "21", "22", "23",
               "31", "32", "33"];

var selection = ["s11", "s12", "s13",
                 "s21", "s22", "s23",
                 "s31", "s32", "s33"];

var xs = ["x11", "x12", "x13",
          "x21", "x22", "x23",
          "x31", "x32", "x33"];

var os = ["o11", "o12", "o13",
          "o21", "o22", "o23",
          "o31", "o32", "o33"];

var played = [-1, -1, -1,
              -1, -1, -1,
              -1, -1, -1];

// square currently selected
var currentSquare = -1;
var currentSquareW   = -1;

// 0 - O player
// 1 - X player
var currentPlayer = -1;

var end = false;

var stompClient = null;

var devices = [];

var games = [];

var currentGame = 0;

var selectedPlayers = [];

var devInt = null;
var gameInt = null;
var watchInt = null;

// 192.168.12.1:8254
var ip = "http://192.168.160.80:8254"

function processAction(action) {
    switch(action) {
        case "UP":
            moveUp();
            break;
        case "DOWN":
            moveDown();
            break;
        case "LEFT":
            moveLeft();
            break;
        case "RIGHT":
            moveRight();
            break;
        case "SELECT":
            playMove();
            break;
        case "WINNER":
            playMove();
            break;
        default:
            console.log("Action not found!");
            break;
    }

    axios.get(ip+"/api/game/"+currentGame)
    .then(function(response) {
        var data = response.data;
        // console.log(data)
    })
    .catch(function(error) {
        console.log(error);
    });
}

function connect() {
    var socket = new SockJS(ip+"/updates");
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function(frame) {
        stompClient.subscribe('/game/' + currentGame, function(messageOutput) {
            var body = JSON.parse(messageOutput.body);
            var action = body.action;
            processAction(action);
        });
    });
}

function disconnect() {
    if(stompClient != null) {
        stompClient.disconnect();
    }
    console.log("Disconnected");
}

function selectPlayer() {

    if (currentPlayer == 0) {
        document.getElementById("po1").classList.remove("turn-p1");
        document.getElementById("po1").classList.add("turn-p");
        document.getElementById("po2").classList.remove("turn-p1");
        document.getElementById("po2").classList.add("turn-p");
        document.getElementById("px1").classList.remove("turn-p");
        document.getElementById("px1").classList.add("turn-p2");
        document.getElementById("px2").classList.remove("turn-p");
        document.getElementById("px2").classList.add("turn-p2");
    }
    else {
        document.getElementById("po1").classList.remove("turn-p");
        document.getElementById("po1").classList.add("turn-p1");
        document.getElementById("po2").classList.remove("turn-p");
        document.getElementById("po2").classList.add("turn-p1");
        document.getElementById("px1").classList.remove("turn-p2");
        document.getElementById("px1").classList.add("turn-p");
        document.getElementById("px2").classList.remove("turn-p2");
        document.getElementById("px2").classList.add("turn-p");
    }
}

function changePlayers() {
    if (currentPlayer == 0) {
        currentPlayer = 1;
    }
    else {
        currentPlayer = 0;
    }
}

function changeHiddenToSelected(square) {
    document.getElementById(square).classList.remove("hide");

    document.getElementById(square).classList.add("selected");
}

function changeSelectedToHidden(square) {
    document.getElementById(square).classList.remove("selected");

    document.getElementById(square).classList.add("hide");
}

function changeHiddenToImg(square) {
    document.getElementById(square).classList.remove("hide");

    document.getElementById(square).classList.add("img");
}

function changeImgToHidden(square) {
    document.getElementById(square).classList.remove("img");

    document.getElementById(square).classList.add("hide");
}

function moveLeft() {
    changeSelectedToHidden(selection[currentSquare]);
    switch (currentSquare) {
        case 0:
        case 3:
        case 6:
            break;
        default:
            currentSquare--;
    }
    changeHiddenToSelected(selection[currentSquare]);
}

function moveRight() {
    changeSelectedToHidden(selection[currentSquare]);
    switch (currentSquare) {
        case 2:
        case 5:
        case 8:
            break;
        default:
            currentSquare++;
    }
    changeHiddenToSelected(selection[currentSquare]);
}

function moveUp() {
    changeSelectedToHidden(selection[currentSquare]);
    switch (currentSquare) {
        case 0:
        case 1:
        case 2:
            break;
        default:
            currentSquare -= 3;
    }
    changeHiddenToSelected(selection[currentSquare]);
}

function moveDown() {
    changeSelectedToHidden(selection[currentSquare]);
    switch (currentSquare) {
        case 6:
        case 7:
        case 8:
            break;
        default:
            currentSquare += 3;
    }
    changeHiddenToSelected(selection[currentSquare]);
}

function checkWin() {
    if(played[0] != -1 && played[0] == played[1] && played[0] == played[2]) {
        end = true;
    }
    if(played[3] != -1 && played[3] == played[4] && played[3] == played[5]) {
        end = true;
    }
    if(played[6] != -1 && played[6] == played[7] && played[6] == played[8]) {
        end = true;
    }
    if(played[0] != -1 && played[0] == played[3] && played[0] == played[6]) {
        end = true;
    }
    if(played[1] != -1 && played[1] == played[4] && played[1] == played[7]) {
        end = true;
    }
    if(played[2] != -1 && played[2] == played[5] && played[2] == played[8]) {
        end = true;
    }
    if(played[0] != -1 && played[0] == played[4] && played[0] == played[8]) {
        end = true;
    }
    if(played[2] != -1 && played[2] == played[4] && played[2] == played[6]) {
        end = true;
    }

    var draw = true;
    played.forEach(function(s) {
        if(s == -1) {
            draw = false;
        }
    });

    if(draw) {
        disconnect();
        clearInterval(watchInt);
        console.log("It's a draw!");
        document.getElementById('medal').classList.add("hide");
        document.getElementById('win').classList.add("hide");
        document.getElementById('draw').classList.remove("hide");
        document.getElementById('win-block').classList.remove("hide");
    }
}

function playMove() {
    if(played[currentSquare] == -1 && !end) {
        played[currentSquare] = currentPlayer;

        if(currentPlayer == 0) {
            changeHiddenToImg(os[currentSquare]);
        }
        else {
            changeHiddenToImg(xs[currentSquare]);
        }

        checkWin();
        if(end) {
            disconnect();
            if(currentPlayer == 0) {
                document.getElementById('o-win').classList.remove("hide");
                document.getElementById('o-win').classList.add("o-win");
            }
            else {
                document.getElementById('x-win').classList.remove("hide");
                document.getElementById('x-win').classList.add("x-win");
            }
            document.getElementById('win-block').classList.remove("hide");
        }

        changePlayers();
        selectPlayer();
    }
}

function checkKey(e) {
    e = e || window.event;

    if (e.keyCode == '38') {
        // up arrow
        moveUp();
    }
    else if (e.keyCode == '40') {
        // down arrow
        moveDown();
    }
    else if (e.keyCode == '37') {
        // left arrow
        moveLeft();
    }
    else if (e.keyCode == '39') {
        // right arrow
        moveRight();
    }
    else if (e.keyCode == '13' || e.keyCode == '32') {
        // enter or space key
        playMove();
    }
}

function connectPlayer(id) {
    e = document.getElementById(id);
    if(window.getComputedStyle(e).display === "none") {
        e.classList.remove("hide");
        selectedPlayers.push(id.replace("p-select", ""));
    }
    else {
        e.classList.add("hide");
        selectedPlayers = selectedPlayers.filter(function(v, i, a) {
            return v != id.replace("p-select", "");
        });
    }
}

function checkConnectedPlayers() {
    if(selectedPlayers.length === 2) {
        return true;
    }

    return false;
}

function listDevices() {
    if(!devInt) {
        devInt = setInterval(listDevices,1000);
    }
    axios.get(ip+"/api/devices").then(function(response) {
        devices = response.data;

        var placeholder = "p-select";
        var id;
        devices.forEach(function(s) {
            id = placeholder + s;
            if(!document.getElementById(id)) {
                var b = document.createElement("button");
                var n = document.createTextNode("Device " + s);
                b.appendChild(n);
                b.setAttribute("class", "player-btn button_no_border");
                b.setAttribute("type", "button");
                b.setAttribute("onclick", "connectPlayer('"+id+"')");

                var i = document.createElement("i");
                i.setAttribute("id", id);
                i.setAttribute("class", "hide fas fa-check");
                b.appendChild(i);

                document.getElementById("connect-players").appendChild(b);
            }
        });
    }).catch(function(error) {
        console.log(error);
    });
}

function listGames() {
    if(!gameInt) {
        gameInt = setInterval(listGames,1000);
    }
    axios.get(ip+"/api/game/notfinished").then(function(response) {
        games = response.data;

        var placeholder = "game";
        var id;
        games.forEach(function(s) {
            id = placeholder + s.id;
            if(!document.getElementById(id)) {
                var b = document.createElement("button");
                var n = document.createTextNode("Game #" + s.id);
                b.appendChild(n);
                b.setAttribute("id", id)
                b.setAttribute("class", "player-btn button_no_border");
                b.setAttribute("type", "button");
                b.setAttribute("onclick", "watchGame('"+s.id+"')");

                document.getElementById("connect-games").appendChild(b);
            }
        });
    }).catch(function(error) {
        console.log(error);
    });
}

function getSquare(pos) {
    if(pos[0] == 0 & pos[1] == 0) return 6;
    else if(pos[0] == 0 & pos[1] == 1) return 3;
    else if(pos[0] == 0 & pos[1] == 2) return 0;
    else if(pos[0] == 1 & pos[1] == 0) return 7;
    else if(pos[0] == 1 & pos[1] == 1) return 4;
    else if(pos[0] == 1 & pos[1] == 2) return 1;
    else if(pos[0] == 2 & pos[1] == 0) return 8;
    else if(pos[0] == 2 & pos[1] == 1) return 5;
    else if(pos[0] == 2 & pos[1] == 2) return 2;
    else console.log("Square " + pos + " not found!"); return -1;
}

function getPlayed(board) {
    var row0 = board[0];
    var row1 = board[1];
    var row2 = board[2];

    if(row0[0] == "") played[6] = -1;
    else played[6] = parseInt(row0[0]-1);
    if(row0[1] == "") played[3] = -1;
    else played[3] = parseInt(row0[1]-1);
    if(row0[2] == "") played[0] = -1;
    else played[0] = parseInt(row0[2]-1);
    if(row1[0] == "") played[7] = -1;
    else played[7] = parseInt(row1[0]-1);
    if(row1[1] == "") played[4] = -1;
    else played[4] = parseInt(row1[1]-1);
    if(row1[2] == "") played[1] = -1;
    else played[1] = parseInt(row1[2]-1);
    if(row2[0] == "") played[8] = -1;
    else played[8] = parseInt(row2[0]-1);
    if(row2[1] == "") played[5] = -1;
    else played[5] = parseInt(row2[1]-1);
    if(row2[2] == "") played[2] = -1;
    else played[2] = parseInt(row2[2]-1);
}

function playAll() {
    var i;
    for(i=0; i < played.length; i++) {
        if(played[i] != -1) {
            if(played[i] == 0) {
                changeHiddenToImg(xs[i]);
            }
            else {
                changeHiddenToImg(os[i]);
            }
        }
    }
}

function spectateGame() {
    if(!watchInt) {
        watchInt = setInterval(spectateGame,500);
    }
    axios.get(ip+"/api/game/"+currentGame)
    .then(function(response) {
        var data = response.data;
        console.log(data)

        selection.forEach(function(s) {
            changeSelectedToHidden(s);
        });

        // connect();

        getPlayed(data.board);
        playAll();

        checkWin();
        if(end) {
            clearInterval(watchInt);
            if(currentPlayer == 0) {
                document.getElementById('o-win').classList.remove("hide");
                document.getElementById('o-win').classList.add("o-win");
            }
            else {
                document.getElementById('x-win').classList.remove("hide");
                document.getElementById('x-win').classList.add("x-win");
            }
            document.getElementById('win-block').classList.remove("hide");
        }

        currentSquareW = getSquare(data.currentPos);
        if(data.player1Turn) currentPlayer =  1;
        else currentPlayer = 0;

        changeHiddenToSelected(selection[currentSquareW]);

        selectPlayer();
    })
    .catch(function(error) {
        console.log(error);
    });
}

function watchGame(id) {
    currentGame = id;
    document.getElementById("connect-block").classList.add("hide");
    document.getElementById("player-turns").classList.remove("hide");
    document.getElementById("game-board").classList.remove("hide");
    document.getElementById("game-id").classList.remove("hide");
    var t = document.createTextNode("Game #" + currentGame);
    document.getElementById("game-id").appendChild(t);


    document.getElementById('o-win').classList.remove("o-win");
    document.getElementById('o-win').classList.add("hide");
    document.getElementById('x-win').classList.remove("x-win");
    document.getElementById('x-win').classList.add("hide");
    document.getElementById('draw').classList.add("hide");
    document.getElementById('win').classList.remove("hide");
    document.getElementById('medal').classList.remove("hide");
    document.getElementById('win-block').classList.add("hide");

    selection.forEach(function(s) {
        changeSelectedToHidden(s);
    });

    xs.forEach(function(s) {
        changeImgToHidden(s);
    });

    os.forEach(function(s) {
        changeImgToHidden(s);
    });

    spectateGame();
}

function startGame() {
    if(checkConnectedPlayers()) {
        clearInterval(devInt);
        axios.post(ip+"/api/newgame/?device1="+selectedPlayers[0]+"&device2="+selectedPlayers[1])
        .then(function(response) {
            console.log(response);
            currentGame = response.data;

            document.getElementById("connect-block").classList.add("hide");
            document.getElementById("player-turns").classList.remove("hide");
            document.getElementById("game-board").classList.remove("hide");
            document.getElementById("game-id").classList.remove("hide");
            document.getElementById("game-id").innerHTML = '';
            var t = document.createTextNode("Game #" + currentGame);
            document.getElementById("game-id").appendChild(t);

            axios.get(ip+"/api/game/"+currentGame)
            .then(function(response) {
                var data = response.data;
                // console.log(data)
                connect();
                end = data.finished;
                currentSquare = getSquare(data.currentPos);
                if(data.player1Turn) currentPlayer =  1;
                else currentPlayer = 0;

                selectPlayer();

                selection.forEach(function(s) {
                    changeSelectedToHidden(s);
                });

                xs.forEach(function(s) {
                    changeImgToHidden(s);
                });

                os.forEach(function(s) {
                    changeImgToHidden(s);
                });

                document.getElementById('o-win').classList.remove("o-win");
                document.getElementById('o-win').classList.add("hide");
                document.getElementById('x-win').classList.remove("x-win");
                document.getElementById('x-win').classList.add("hide");
                document.getElementById('draw').classList.add("hide");
                document.getElementById('win').classList.remove("hide");
                document.getElementById('medal').classList.remove("hide");
                document.getElementById('win-block').classList.add("hide");

                played.fill(-1);

                changeHiddenToSelected(selection[currentSquare]);
            })
            .catch(function(error) {
                console.log(error);
            });
        })
        .catch(function(error) {
            console.log(error);
        });
    }
}

// document.onkeydown = checkKey;
document.onload = disconnect();

// disable button selection when clicked
document.addEventListener('click', function(e) {
    if(document.activeElement.toString() == '[object HTMLButtonElement]') {
        document.activeElement.blur();
    }
});
