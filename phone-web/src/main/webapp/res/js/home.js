var ws=null;
var log=null;
var plane=null;
var planeStatus = null;
window.onload=function(){ 
	log=document.getElementById("log");
	plane =document.getElementById("plane");
<<<<<<< HEAD
	role = document.getElementById("role");
=======
	planeStatus =document.getElementById("fightStatus");
>>>>>>> branch 'master' of https://github.com/HustYrf/phoneV2.0.git
	HomeChatOperateUtil.ready();
}
//
//var WebTypeUtil=
//{
//		SENDUSERLOGIN:"send@login",
//		LANDUSERLOGIN:"land@login",
//		SENDUSERFLYING:"send@flying",
//		SENDUSERRETURN:"send@return",
//		LANDUSERRETURN:"land@return",
//		MESSAGETYPEFLYING:"flying",
//		MESSAGETYPEFRETURN:"return"
//}
//
//  window.onload=function(){
//	  			//var ws=null;
//	  			 log=document.getElementById("log");
//	  			 plane =document.getElementById("plane");
//               
//                connect();
//  				}
//  			function print(text) {
//  					log.innerHTML = (new Date).getTime() + ": " + text + log.innerHTML;
//			  }
//                 function connect(){
//                    ws = new WebSocket("ws:///127.0.0.1:7020");
//                    ws.onopen = function() {
//                       var content = plane.value+"send@login";
//                       send(content);
//                    }
//                    ws.onmessage = function(e) {
//                        print("[onmessage] '" + e.data + "'\n");
//                    }
//                    ws.onclose = function() {
//                    }
//                }
//               
//                function send(content){
//                   
//                        ws.send(content);
//                       
//                    }
               
