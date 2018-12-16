var wgs84_to_gcj02 = new WGS84_to_GCJ02();
var WebTypeUtil=
{
		SENDUSERLOGIN:"send@login",
		LANDUSERLOGIN:"land@login",
		SENDUSERFLYING:"send@flying@",
		SENDUSERRETURN:"send@return",
		LANDUSERRETURN:"land@return",
		MESSAGEPUTLINES:"send@putLines@",
		MESSAGEPUTTASKNUM:"send@putTaskNum@",
		MESSAGESEARCHLINES:"send@searchLines",
		MESSAGESEARCHLINESDETAIL:"send@searchLinesDetail",
		MESSAGETYPEFLYINGEXCUTE:"flyingExcute",
		MESSAGETYPEFLYINGFAILURE:"flyingFailure",
		MESSAGETYPEFLYINGWAIT:"flyingWait",
		MESSAGETYPEFRETURNEXCUTE:"returnExcute",
		MESSAGETYPEFRETURNFAILURE:"returnFailure",
		MESSAGETYPEFRETURNWAIT:"returnWait",
		MESSAGETYPESTATUS:"status",
		MESSAGEPLANELOGIN:"planeLogin",
		MESSAGEPLANESEARCHRESULT:"planeLine",
		MESSAGECHECKRESULT:"checkResult",
		MESSAGELINEFINISH:"lineResult",
		MESSAGEPLANESEARCHCHECK:"lineSearch"
		
		
}

var WebSocketUtil = {
	webSocket : null,
	timeOuter : null,
	isActive : true,
	connect : function() {

//		WebSocketUtil.webSocket = new WebSocket("ws:///218.65.240.246:17020");
		WebSocketUtil.webSocket = new WebSocket("ws:///127.0.0.1:17020");
		WebSocketUtil.webSocket.onopen = WebSocketUtil.onOpen;
		WebSocketUtil.webSocket.onmessage = WebSocketUtil.onMessage;
		WebSocketUtil.webSocket.onclose = WebSocketUtil.onClose;
		WebSocketUtil.webSocket.onerror = WebSocketUtil.onError;
	},
	initTimeOuter : function() {
		
	},
	onOpen : function(event) {
		if(role.value == "1")
		{
			var content = plane.value+WebTypeUtil.SENDUSERLOGIN;
			
		}else if(role.value =="2")
		{
			var content = plane.value+WebTypeUtil.LANDUSERLOGIN;
		}
		
		
		WebSocketUtil.webSocket.send(content);
	},
	onMessage : function(event) {
		//alert("收到消息"+event.data);
		var message = event.data;
		var messageType = message.split(":");
		//console.log(messageType[0]);
		switch(messageType[0]){
		case WebTypeUtil.MESSAGETYPESTATUS:
			//处理接收到的经纬度消息
			PlaneHandleServiceUtil.handleStatus(messageType[1],messageType[2],messageType[3]);
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
		case WebTypeUtil.MESSAGEPLANELOGIN:
			PlaneHandleServiceUtil.handleLogin();
			break;
		case WebTypeUtil.MESSAGEPLANESEARCHRESULT:
			//处理查询航线的结果
			PlaneHandleServiceUtil.handleSearchLine(messageType[1],messageType[2],messageType[3]);
			break;
		case WebTypeUtil.MESSAGEPLANESEARCHCHECK:
			//核对航线的结果
			PlaneHandleServiceUtil.handleLineCheckResult(messageType[1]);
			break;
		case WebTypeUtil.MESSAGECHECKRESULT:
			//处理自检结果
			PlaneHandleServiceUtil.handleCheckResult(messageType[1]);
			break;
		case WebTypeUtil.MESSAGELINEFINISH:
			//处理下发航线完成结果
			PlaneHandleServiceUtil.handleLineResult(messageType[1]);
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
		//如果断开连接的话
		//$(".buttons-row .button").addClass("linebtn");
	},
	print: function (text) {
         log.innerHTML = (new Date).getTime() + ": " + text + log.innerHTML;
    }
}

var PlaneHandleServiceUtil ={
		handleStatus:function(message,status,GPS_HDG)
		{
			
			if(status =="有异常" )
			{
				$.ajax({
		            type: "post",
		            url: base.value+'/emergencException.action',
		            data: {
		                'taskid': task.value,
		            },
		            success: function (result) {
		                if (result.errcode == '1') {
		                    $.toast(result.message);
		                } else {
		                    $.toast(result.message);
		                }
		            }
		        });
			}
			
//			if(status=="暂无"){
//				$(".buttons-row .button").addClass("linebtn");
//			}
			
			//显示无人机的状态信息
			planeStatus.innerHTML = status;
			var mes = message.split(",");
			var data = new Array();
	        var value = mes[0] *1
	        var value2=mes[1]*1;
	        data[0] = value;
	        data[1] = value2;
//			map.remove(planeMarker);
			
	        //下面为新版的marker移动代码
		    var realdata = wgs84_to_gcj02.transform(data[0],data[1]);
		    map.setCenter(realdata); 		   
		    planeMarker.setPosition(realdata);
		    planeMarker.setAngle(messageType[3]);
		    
		    
//    	   planeMarker = new AMap.Marker({
//                //map: map,
//                position:  data,
//                icon: new AMap.Icon({
//                size: new AMap.Size(32, 32), //图标大小
//                image: "images/uav-32.png",
//                }),
//                angle:GPS_HDG,
//                offset: new AMap.Pixel(-16,-16) // 相对于基点的偏移位置
//            });
//		    map.setCenter(realdata); 
//		    map.add(planeMarker);
			//WebSocketUtil.print("[send] '" + message + "'\n");	 	   
		    
		},
		handleFlyingExcute:function()
		{
			//$.toast("飞机起飞执行成功");
			//写入数据库，改变任务状态  放飞
			$.ajax({
	            type: "post",
	            url: base.value+'/takeoff.action',
	            data: {
	                'taskid': task.value,
	            },
	            success: function (result) {
	                if (result.errcode == '1') {
	                    $.toast(result.message);
	                } else {
	                    $.toast(result.message);
	                }
	            }
	        });
			
		},
		handleFlyingFalure:function()
		{
			$.toast("飞机起飞执行失败");
		},
		handleFlyingWait:function()
		{
			//$.toast("飞机起飞等待");
			$.ajax({
	            type: "post",
	            url: base.value+'/takeoff.action',
	            data: {
	                'taskid': task.value,
	            },
	            success: function (result) {
	                if (result.errcode == '1') {
	                    $.toast(result.message);
	                } else {
	                    $.toast(result.message);
	                }
	            }
	        });
			
		},
		handleReturnExcute:function()
		{
			//$.toast("飞机返回执行成功");
			//写入数据库，改变任务状态  无人机紧急返航
			$.ajax({
	            type: "post",
	            url: base.value+'/emergencyback.action',
	            data: {
	                'taskid': task.value,
	            },
	            success: function (result) {
	                if (result.errcode == '1') {
	                    $.toast(result.message);
	                } else {
	                    $.toast(result.message);
	                }
	            }
	        });
			
		},
		handleReturnFalure:function()
		{
			$.toast("飞机返回执行失败");
		},
		handleReturnWait:function()
		{
			//$.toast("飞机返回等待");
			$.ajax({
	            type: "post",
	            url: base.value+'/emergencyback.action',
	            data: {
	                'taskid': task.value,
	            },
	            success: function (result) {
	                if (result.errcode == '1') {
	                    $.toast(result.message);
	                } else {
	                    $.toast(result.message);
	                }
	            }
	        });
		},
		handleLogin:function(){
			$.toast("无人机登录");
		},
		handleSearchLine:function(ROUTE_ID,ROUTE_COUNT,ROUTE_STOCK_COUNT)
		{
			//console.log(ROUTE_ID+" "+ROUTE_COUNT+" "+ROUTE_STOCK_COUNT);
			//处理航点显示
			//$("#routeId").val(ROUTE_ID);
//			$("#totalFlyingPoint").val(ROUTE_COUNT);	
//			$("#processedFlyingPoint").val(ROUTE_STOCK_COUNT);	
			routeLineId.innerHTML = ROUTE_ID;
			totalFlyingPoint.innerHTML = ROUTE_COUNT;
			processedFlyingPoint.innerHTML = ROUTE_STOCK_COUNT;
		},
		handleCheckResult:function(RES_RELULT)
		{
			$.toast(RES_RELULT);
		},
		handleLineResult:function(RES_RELULT)
		{
			$.toast(RES_RELULT);
		},
		handleLineCheckResult:function(RES_RELULT)
		{
			$.toast(RES_RELULT);
		}
			
}
var HomeChatOperateUtil = {
	ready : function(){
		WebSocketUtil.connect();
	}
}