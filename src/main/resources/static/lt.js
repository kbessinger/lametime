var buildDate = "";
var funkifyInterval;
var uuid;
var stompClient = null;
var hidden = "hidden";

$(document).ready(function() {
	/*
	$("#from").on('keyup', function(e) {
	if(e.which == 13) {
		if( !$("#from").val() ) {
			alert("You must enter a nickname");
			$("#from").focus();
		} else {
        	connect();
		}
	}
	*/
	
	uuid = localStorage.getItem("_lt_uuid");
	if(uuid == null) {
		uuid = 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function(c) {
	        var r = Math.random()*16|0, v = c == 'x' ? r : (r&0x3|0x8);
	        return v.toString(16);
	    });    
		localStorage.setItem("_lt_uuid", uuid);
	}    		
	connect();
	setHiddenCheck();
	if( document[hidden] !== undefined ) {
		onchange({type: document[hidden] ? "blur" : "focus"});
	}
	$("#textEntry").on('keyup', function(e) {
		if(e.which == 13) {
	        sendMessage();
		}
	});	
});





/*
var from = localStorage.getItem("_lt_login");
if(from !== null && from.length > 0) {
	$("#from").val(from);
	connect();
} else {
	$("#from").focus();
	}
	
	setHiddenCheck();
	if( document[hidden] !== undefined )
		onchange({type: document[hidden] ? "blur" : "focus"});
});
*/
function setHiddenCheck() {
	// Standards:
	if (hidden in document)
		document.addEventListener("visibilitychange", onchange);
	else if ((hidden = "mozHidden") in document)
		document.addEventListener("mozvisibilitychange", onchange);
	else if ((hidden = "webkitHidden") in document)
		document.addEventListener("webkitvisibilitychange", onchange);
	else if ((hidden = "msHidden") in document)
		document.addEventListener("msvisibilitychange", onchange);	
}

function onchange(evt) {
	var v = "visible", h = "hidden",
    	evtMap = {
			focus:v, focusin:v, pageshow:v, blur:h, focusout:h, pagehide:h
       	};

	evt = evt || window.event;
	if (evt.type in evtMap)
		document.body.className = evtMap[evt.type];
	else
		document.body.className = this[hidden] ? "hidden" : "visible";
}

    
     
    function setConnected(connected) {
        //document.getElementById('connect').disabled = connected;
        //document.getElementById('disconnect').disabled = !connected;
    	document.getElementById('textDiv').style.visibility 
    		= connected ? 'visible' : 'hidden';
    	document.getElementById('container').style.visibility 
    		= connected ? 'visible' : 'hidden';
    	document.getElementById('clearDiv').style.visibility 
    		= connected ? 'visible' : 'hidden';
    	//var from = document.getElementById('from').value;
    	if(connected == true) {
    	//	localStorage.setItem("_lt_login", from);
    		loadHistory();
    	}                
    }
 
function connect() {
	//var from = document.getElementById('from').value;
    var socket = new SockJS('/chat');
    stompClient = Stomp.over(socket);
    //stompClient.connect({cid: uuid, nick: from}, function(frame) {
    stompClient.connect({cid: uuid}, function(frame) {
        setConnected(true);
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/messages', function(messageOutput) {
            showMessageOutput(JSON.parse(messageOutput.body));
        });
        stompClient.subscribe('/topic/state', function(stateInfo) {
        	updateState(JSON.parse(stateInfo.body));
        });
        //$("#from").attr('disabled', 'disabled');
        $("#textEntry").focus();
        stompClient.send("/app/state", {}, 
                JSON.stringify({'connectionId':uuid}));
    }, function() {
    	setTimeout(connect, 30000);
    });
}

function updateState(stateInfo) {
	console.log(stateInfo.state + ": " + stateInfo.user.fullName);
	checkVersionChange(stateInfo.connectionInfo.versionInfo);
	$("#userList").html("");
	for(var i=0; i<stateInfo.connectionInfo.users.length; i++) {
		var user = stateInfo.connectionInfo.users[i];
		$("#userList").prepend("<div><img src='" + user.principal.userAuthentication.details.picture + "'/><span>" + user.fullName + "</span></div>");
	}            	
}


function checkVersionChange(versionInfo) {
	oldBuildDate = buildDate;
	buildDate = versionInfo.buildDate;
	if(buildDate != oldBuildDate && oldBuildDate.length > 0) {
		setTimeout(function() {
			window.location.reload(false); 
		}, 1000);
	}
}
 
function disconnect(userInitiated) {
    if(stompClient != null) {
        stompClient.disconnect();
    }
    /*
    if(userInitiated) {
    	localStorage.setItem("_lt_login", "");
    	$("#from").removeAttr('disabled');
    	$("#from").val('');
    }
    */
    setConnected(false);
    console.log("Disconnected");
}
 
function sendMessage() {
    var text = document.getElementById('textEntry').value;
    processBeforeSend(text, doSend);
}

function doSend(text) {
    stompClient.send("/app/chat", {}, 
            JSON.stringify({'text':text}));
    $("#textEntry").val("");            	
}
 
function showMessageOutput(messageOutput) {
    var response = document.getElementById('response');
    processAfterSend(messageOutput);
    var text = messageOutput.text;
    var exp = /(\b(https?|ftp|file):\/\/[-A-Z0-9+&@#\/%?=~_|!:,.;]*[-A-Z0-9+&@#\/%=~_|])/ig;
    text = text.replace(exp,"<a target='_blank' href='$1'>$1</a>");               
   	$("#response").prepend("<p class='message'><span class='from " + messageOutput.user.cid + " " + messageOutput.user.hostName + 
   			"'>" + messageOutput.user.fullName + "</span>: " + text + " (" + messageOutput.time + ")</p>");
   	emojify.setConfig({img_dir: '/webjars/emojify.js/images/basic'});
   	emojify.run(document.getElementById('response'));
   	
   	if(document.body.className == "hidden" && uuid != messageOutput.user.cid && Notification) {
   		if(Notification.permission !== "granted") {
   			Notification.requestPermission();
   		} else {
   			var notification = new Notification('Message recvd from: ' + messageOutput.user.fullName, {
   				icon: 'message-recvd.png',
   				body: messageOutput.user.fullName + " (" + 
   						messageOutput.user.hostName + "): " + text + 
   						" (" + messageOutput.time + ")"
   			});    
   		   notification.onclick = function(){
   			   window.focus();
   			   this.cancel();
   		   };
   		   setTimeout(notification.close.bind(notification), 15000);
   		}
   	}
   	
    $("a[href$='.png'], a[href$='.jpg'], a[href$='.tiff'], a[href$='.gif']").each(function() {
        var img = $('<img>',{src: this.href});
        $(this).replaceWith(img);
    });                    
    saveHistory();
    $(".from").not('.' + uuid).each(function() {
    	//var newColor = '#'+(0x1000000+(Math.random())*0xffffff).toString(16).substr(1,6);
    	$(this).css('color', 'brown');
    });
}

function saveHistory() {
	localStorage.setItem("_lt_history", $("#response").html());
}

function loadHistory() {
	var history = localStorage.getItem("_lt_history");
	if(history !== null && history.length > 0) {
		$("#response").html(history);
	}
}

function clearHistory() {
	localStorage.setItem("_lt_history", "");
	$("#response").html("");
}


function funkify(enable) {
	if(enable) {
		funkifyInterval = setInterval(function() {
			var newColor = '#'+(0x1000000+(Math.random())*0xffffff).toString(16).substr(1,6);
    		$('body').animate( { backgroundColor: newColor }, 300);
    		}, 750);
	} else {
		$('body').animate( { backgroundColor: 'white'}, 300);
		clearInterval(funkifyInterval);
	}
}

function processBeforeSend(text, sendCallback) {
	var firstChar = text[0];
	if(firstChar != "/") {
		sendCallback(text);
		return;
	}
	var command = text.substr(1, text.indexOf(" ")-1);
	 
    if(command == "wiki") {
		    var result = text.split('/wiki ');
			var firstPart = "";
			var lastPart = "";
			if(result.length > 1) {
				firstPart = result[0];
				lastPart = result[1];
			} else {
				lastPart = result[0];
			}
			var whatToWiki = lastPart;
		 
			sendCallback("I'm wiki'ing: " + whatToWiki)
         $.ajax({
             url: 'http://en.wikipedia.org/w/api.php',
             data: { action: 'query', list: 'search', srsearch: whatToWiki, format: 'json' },
             dataType: 'jsonp',
             success: function(apiResult) {
            	 var answer = apiResult.query.search[0].title;            	 
                 $.ajax({
                     url: 'http://en.wikipedia.org/w/api.php',
                     data: { action: 'query', prop: 'extracts', exintro: '', explaintext: '', titles: answer, format: 'json' },
                     dataType: 'jsonp',
                     success: function(processResult) {
                	    for(var key in processResult.query.pages) {
                			sendCallback(processResult.query.pages[key].extract);
                		}	 
                     }
                 });            	 
             }
         });
	 } else {
		 sendCallback(text);
	 }
	 
}

function processAfterSend(messageOutput) {
    if(messageOutput.text.indexOf("/funkify") >= 0) {
    	funkify(true);
    } else if(messageOutput.text.indexOf("/stahp") >= 0) {
    	funkify(false);
    }            	
}