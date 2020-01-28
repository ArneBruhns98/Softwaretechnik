var ws = null; // Websocket
var rooms;	// Map of Rooms

var alertFirstTime = true; // alert for empty room-list -> only shown first time
var intervall = window.setInterval("getRooms()", 3000); // GET Rooms all 3 sec.
var intervall = window.setInterval("setSizes()", 3000); // Resizing all 3 sec.

var asc = true; // Sorting ascending / descending
var sortBy = null; // sort by start
var searchCrit = ""; // String for searching

var chatHidden = false; // chat toggle state

//--------------------------------------------------------------------------
//--------------------------Lobby Functions---------------------------------
//--------------------------------------------------------------------------
$(document).ready(function(){
   	getRooms();
	setSizes();
	document.getElementById('roomName').onclick = function(){
		sortBy = "name";
		asc = !asc;
		getRooms();
	} // onclick sort by name

	document.getElementById('roomUser').onclick = function(){
		sortBy = "user";
		asc = !asc;
		getRooms();
	} // on click sort by users in room
	
	document.getElementById('createRoom').onclick = function(){
		createRoom();
	} // on click creates a room

	document.getElementById("sendButton").onclick = function() {
		sendMessageToServer();
		document.getElementById("chatTextField").value = "";
	} // on click sends message contained in chatTextField to server

	document.getElementById("chatTextField").addEventListener("keydown", function(event) {
		if (event.key === "Enter") {
			document.getElementById("sendButton").click();
		}
	}) // keydown event listener for enter pressed to send message to server
}); // GET Rooms after start

/**
 * GET Rooms from Server and sort rooms by var sortBy
 */
function getRooms(){
	$.getJSON("/rooms", function(data){
	rooms = (Object.entries(data));
    }).done(function(){
		searchRoom();
		sorting();
		showRooms();
	}).fail(function(){
		console.log("ERROR : Die Verbindung zum Server wurde getrennt");
		alert("ERROR : Die Verbindung zum Server wurde getrennt");
	});
}

/// scaling Elements
function setSizes(){
	var screenHeight = window.innerHeight;
	
	document.getElementById("background").style.height = screenHeight + "px";
	document.getElementById("roomSection").style.height = (screenHeight - document.getElementById("welcomeText").offsetHeight) + "px";
	document.getElementById("tableSection").style.height = (screenHeight - document.getElementById("welcomeText").offsetHeight) + "px";
	document.getElementById("whiteboardSection").style.height = screenHeight + "px";
	document.getElementById("memberInfoSection").style.height = screenHeight + "px";
	document.getElementById("backgroundWhiteboard").style.height = screenHeight + "px";
	document.getElementById("chat").style.height = (screenHeight/100) * 25 + "px";
	document.getElementById("panelHead").style.height = (screenHeight / 100) * 5 + "px";
	document.getElementById("panelChat").style.height = (screenHeight / 100) * 15 + "px";
	document.getElementById("panelFooter").style.height = (screenHeight / 100) * 5 + "px";
	
	if(chatHidden == false){ // show Chat
		document.getElementById("roomRow1").style.height = (screenHeight / 100) * 75 + "px";
		document.getElementById("roomRow2").style.height = (screenHeight / 100) * 25 + "px";
	}else{ // hide Chat
		document.getElementById("roomRow1").style.height = (screenHeight / 100) * 95 + "px";
		document.getElementById("roomRow2").style.height = (screenHeight / 100) * 5 + "px";
	}
}

//---------------------------Table Functions----------------------------

/**
 * show rooms in Table
 */
function showRooms(){
	clearTable();
	var rowCounter = 1; // counter for Table row
	rooms.forEach(function(entry) {
		var text = JSON.stringify(entry) 
		var table = document.getElementById("tabelleRooms");
		var row = table.insertRow();
		
		var name;
		var users;
		var maxUsers;
		var locked;
		
		var obj = JSON.parse(text, function (key, value) {
  		if (key == "name") {
    		name = value;
  		}
		if (key == "passwordLocked") {
    		locked = value;
  		}
		if (key == "currentUsers") {
    		users = value;
  		}
		if (key == "maxUsers") {
    		maxUsers = value;
  		}
		});
		
		var cell0 = row.insertCell(0);
		cell0.innerHTML = name;
		
		var cell1 = row.insertCell(1);
		cell1.innerHTML = users + "/" + maxUsers;
			
		var cell2 = row.insertCell(2);
		cell2.innerHTML = locked;
		if(locked == true){
			cell2.innerHTML = "geschützt";
		}else{
			cell2.innerHTML = "offen";
		}
		
		var btn = document.createElement("BUTTON"); 
		btn.innerHTML = "beitreten";
		var cell3 = row.insertCell(3);
		cell3.appendChild(btn);  
		btn.onclick = function(){joinRoom(cell0.innerHTML, cell2.innerHTML);};
		
		rowCounter ++;
    });
	if(rowCounter == 1 && alertFirstTime == true && searchCrit == ""){ // Case of no existing rooms
		alert("Es existieren noch keine Räume"); 
		alertFirstTime = false;
	}else if(rowCounter == 1 && alertFirstTime == true && searchCrit != ""){
		alert("Es wurden keine Räume mit der Sucheingabe " + searchCrit + " gefunden"); 
		alertFirstTime = false;
	} // Case of no search results
}

/**
 * clears the Table without first row(Head Row)
 */
function clearTable(){
	var table = document.getElementById("tabelleRooms");
    for(var i = table.rows.length - 1; i > 0; i--)
    {
        table.deleteRow(i);
    }
}

/**
 * Sorting Function -> sorts rooms after var. sortBy and var. asc
 */
function sorting(){
	if(sortBy != null){
        if (sortBy == "name") {
            rooms = rooms.sort(function(a, b) {
                if (asc) {
                    return a[1].name.localeCompare(b[1].name);
                } else {
                    return b[1].name.localeCompare(a[1].name);
                }
            });
        } else if (sortBy == "user") {
            rooms = rooms.sort(function(a, b) {
                if (asc) {
                    return (a[1].currentUsers > b[1].currentUsers) ? 1 : ((a[1].currentUsers < b[1].currentUsers) ? -1 : 0);
                } else {
                    return (b[1].currentUsers > a[1].currentUsers) ? 1 : ((b[1].currentUsers < a[1].currentUsers) ? -1 : 0);
                }
            });
        }
	}
}

/**
 * searching Rooms from String
 */
function searchRoom() {
    searchCrit = document.getElementById("rS").value.toLowerCase(); // set the search String
	
	if(searchCrit != "" && !searchCrit.includes("\\")){ // filter regex and do no search for empty String
		for(var i = 0; i < rooms.length; i++){
			var tempName = JSON.stringify(rooms[i][1].name);
			tempName = tempName.toLowerCase();
			if(!tempName.includes(searchCrit)){
				delete rooms[i]; // delete if not like search String
			}
		}
	}
}

//----------------------------------------------------------------------------------------------------

/**
 * creation of a room by name and password
 */
function createRoom(){
	var roomName = document.getElementById("rN").value;
	var password = document.getElementById("p").value;
	
	if(roomName.length < 4 || roomName.length > 25 || roomName.includes("\\")){ // fail case -> name too short, too large or regex inside
		alert("Tragen Sie einen Raumnamen mit 4-25 Zeichen in das entsprechende Feld ein. Zeichen aus der Regex-Gruppe sind nicht erlaubt.");
	}else{
		var xhr = new XMLHttpRequest();
		xhr.open("POST", "/rooms", true);
		xhr.setRequestHeader("Content-Type", "application/json");
        xhr.send(JSON.stringify({"name":roomName,"password":password,"maxUsers":"6"}));
		xhr.onload = function () {
    	if (xhr.status != 201) {
       		alert(xhr.responseText);
    	}else{
            //joinRoom(roomName); // perhaps TODO later on Ticket Raum beitreten
		}};
	}
}

/// function to join a room and create a websocket connection to the server
function joinRoom(room, locked) {
	if(locked == "geschützt"){ // case room is locked
		enterPassword(room);
	}else { // case room is not locked
		var url = 'ws://' + window.location.hostname + ':' + window.location.port + '/rooms/' + room + '/ws';
		ws = new WebSocket(url);

		ws.onopen = function () {
			setRoom();
			writeMessageToChat("Connection established.");
		}

		ws.onmessage = function (event) {
			writeMessageToChat(eventToChatMessage(event));
		}

		ws.onclose = function(ev) { // disconnect handling
			var reconnectTimeout = 20;
			var reconnectCycle = 1000;

			while (reconnectTimeout > 0) {
				setTimeout(function () {
					ws = new WebSocket(url);
				}, reconnectCycle);
				if(ws.readyState == 1) return;
				reconnectTimeout--;
			}

			//Back to lobby
			ws = null;
			setLobby();
		}
	}
}

/// function to enter a room with a password
function enterPassword(room){
	// TODO
	alert("ERROR : Räume mit einem Passwort sind noch nicht implementiert!"); // TEST ALERT While passwort not implemented
}

//---------------------Init Lobby and Init Room Functions------------------------

/**
 * Init. Function for Lobby
 */
function setLobby(){
	document.getElementById("room").style.display = "none";
	document.getElementById("lobby").style.display = "unset";
}

/**
 * Init. Function for a Room
 */
function setRoom(){
	document.getElementById("lobby").style.display = "none";
	document.getElementById("room").style.display = "unset";
}

//------------------------------------------------------------
//----------------------Room Functions------------------------
//------------------------------------------------------------

/// hide or show the chatfield
function toggleChat(){
	var screenHeight = window.innerHeight;
	
	if(chatHidden == false){ // hide Chat
		chatHidden = true;
		document.getElementById("toggleChat").style.WebkitTransform = "rotate(180deg)";
		document.getElementById("roomRow1").style.height = (screenHeight / 100) * 95 + "px";
		document.getElementById("roomRow2").style.height = (screenHeight / 100) * 5 + "px";
	}else{ // show Chat
		chatHidden = false;
		document.getElementById("toggleChat").style.WebkitTransform = "rotate(360deg)";
		document.getElementById("roomRow1").style.height = (screenHeight / 100) * 75 + "px";
		document.getElementById("roomRow2").style.height = (screenHeight / 100) * 25 + "px";
	}
}

/// function to write message to room chat
function writeMessageToChat(message) {
	var newLi = document.createElement('li');
	newLi.innerHTML = message;
	document.getElementById("chatField").appendChild(newLi);
	var chatDiv = document.getElementById("panelChat");
	chatDiv.scrollTop = chatDiv.scrollHeight;
}

/// function to send message to server
function sendMessageToServer() {
	var message = document.getElementById("chatTextField").value;
	if (ws != null && message.length > 0) {
		ws.send(msgToTextEvent(message));
	}
}

/// function to create a chat event from entered text
function msgToTextEvent(message) {
	return text = JSON.stringify({
		content:{
			user:	{
				name: "anonymous"	// needs to be changed while implementing '#30 - Namen auswählen'
			},
			properties: {
				chatmessage: message
			}
		},
		type: "CHAT_MESSAGE"
	});
}

/// function to create a chat message from a server send event
function eventToChatMessage(event) {
	var parsed = JSON.parse(event.data);
	return parsed.content.user.name + ": " + parsed.content.properties.chatmessage;
}