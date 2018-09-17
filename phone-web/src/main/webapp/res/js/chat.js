var WebTypeUtil=
{
		SENDUSERLOGIN:"send@login",
		LANDUSERLOGIN:"land@login",
		SENDUSERFLYING:"send@flying",
		SENDUSERRETURN:"send@return",
		LANDUSERRETURN:"land@return",
		MESSAGETYPEFLYINGEXCUTE:"flyingExcute",
		MESSAGETYPEFLYINGFAILURE:"flyingFailure",
		MESSAGETYPEFLYINGWAIT:"flyingWait",
		MESSAGETYPEFRETURNEXCUTE:"returnExcute",
		MESSAGETYPEFRETURNFAILURE:"returnFailure",
		MESSAGETYPEFRETURNWAIT:"returnWait",
		MESSAGETYPESTATUS:"status"
}

var WebSocketUtil = {
	webSocket : null,
	timeOuter : null,
	isActive : true,
	connect : function() {
		
		WebSocketUtil.webSocket = new WebSocket("ws:///127.0.0.1:7020");
		WebSocketUtil.webSocket.onopen = WebSocketUtil.onOpen;
		WebSocketUtil.webSocket.onmessage = WebSocketUtil.onMessage;
		WebSocketUtil.webSocket.onclose = WebSocketUtil.onClose;
		WebSocketUtil.webSocket.onerror = WebSocketUtil.onError;
	},
	initTimeOuter : function() {
		
	},
	onOpen : function(event) {
		if(role == 1)
		{
			var content = plane.value+WebTypeUtil.SENDUSERLOGIN;
			
		}else if(role ==2)
		{
			var content = plane.value+WebTypeUtil.LANDUSERLOGIN;
		}
		
		WebSocketUtil.webSocket.send(content);
	},
	onMessage : function(event) {
		//alert("收到消息"+event.data);
		var message = event.data;
		var messageType = message.split(":");
		switch(messageType[0]){
		case WebTypeUtil.MESSAGETYPESTATUS:
			//处理接收到的经纬度消息
			PlaneHandleServiceUtil.handleStatus(messageType[1]);
			break;
		case WebTypeUtil.MESSAGETYPEFLYINGEXCUTE:
			//处理起飞执行的消息
			PlaneHandleServiceUtil.handleFlyingExcute();
			break;
		case WebTypeUtil.MESSAGETYPEFLYINGFAILURE:
			//处理起飞失败的消息
			PlaneHandleServiceUtil.handleFlyingFalure();
			break;	
		case WebTypeUtil.MESSAGETYPEFLYINGWAIT:
			//处理起飞等待的消息
			PlaneHandleServiceUtil.handleFlyingWait();
			break;
		case WebTypeUtil.MESSAGETYPEFRETURNEXCUTE:
			//处理返回执行的消息
			PlaneHandleServiceUtil.handleReturnExcute();
			break;	
		case WebTypeUtil.MESSAGETYPEFRETURNFAILURE:
			//处理返回失败的消息
			PlaneHandleServiceUtil.handleReturnFalure();
			break;
		case WebTypeUtil.MESSAGETYPEFRETURNWAIT:
			//处理返回等待的消息
			PlaneHandleServiceUtil.handleReturnWait();
			break;	
		}
		
		//WebSocketUtil.print("[send] '" + event.data + "'\n");
		
	},
	onClose : function(event) {
		//alert("收到消息"+event.data);
	},
	onError : function(event) {
		alert("出错了");
	},
	sendMessage : function(content)
	{
		WebSocketUtil.webSocket.send(content);
		WebSocketUtil.print("[send] '" + content + "'\n");
		 
	},
	disConnection : function() {
		
	},
	print: function (text) {
         log.innerHTML = (new Date).getTime() + ": " + text + log.innerHTML;
    }
}

var PlaneHandleServiceUtil ={
		handleStatus:function(message)
		{
			WebSocketUtil.print("[send] '" + message + "'\n");
		},
		handleFlyingExcute:function()
		{
			$.toast("飞机起飞执行成功");
		},
		handleFlyingFalure:function()
		{
			$.toast("飞机起飞执行失败");
		},
		handleFlyingWait:function()
		{
			
		},
		handleReturnExcute:function()
		{
			
		},
		handleReturnFalure:function()
		{
			
		},
		handleReturnWait:function()
		{
			
		}
		
		
		
}
var HomeChatOperateUtil = {
	ready : function(){
		WebSocketUtil.connect();
	}
}