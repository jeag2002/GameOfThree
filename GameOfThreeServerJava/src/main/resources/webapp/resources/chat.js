
//AJAX LONG POLLING
/////////////////////////////////////////////////////////////////////////////////////////////////////

var poolIt = function () {
    $.ajax({
        type: "GET",
        url: "/app/gengine/msg/" + $("#nick").val(),
        dataType: "text",
        success: function (message) {
        	
        	if (message.indexOf("WIN")!=-1){
        		$("span").html("YOU LOSE!");
        		
        		$("#plus").prop("disabled",true);
            	$("#minus").prop("disabled",true);
            	$("#zero").prop("disabled",true);
        		
        	}else if (message == "VOID"){
        		//ignore message
        	}else{	
        		$("#plus").prop("disabled",false);
            	$("#minus").prop("disabled",false);
            	$("#zero").prop("disabled",false);
            	
        		$("ul").append("<li>" + message + "</li>");
        		
        		poolIt();
        	}
        },
        error: function () {
            poolIt(); 
        }
    });
}

/////////////////////////////////////////////////////////////////////////////////////////////////////

//FIRST STEP
////////////////////////////////////////////////////////////////////////////////////////////////////////
//First Button NICK + enable SEED (IF is the first player)

$("#NickButton").click(function () {
	if ($("#nick").val()!=""){
		if ($(".nick").css("visibility") === "visible") { 
			$(".nick").prop("disabled",true);
			$("#NickButton").prop("disabled",true);
	        $("span").html("Game started.."); 
	        ajaxSendNick();
	    }
	}else{
		alert("nick cannot be empty");
	}
});


function ajaxSendNick(){	
	    $.ajax({
	        type: "GET",
	        url: "/app/gengine/" + $("#nick").val(),
	        dataType: "text",
	        success: function (message) {
	        	//->FIRST PLAYER
	        	if (message == "SEED"){
	        		$("span").html("First player. Activated SEED");
	        		$("#InitButton").prop("disabled",false);
	        	//->SECOND PLAYER	
	        	}else if (message == "NOSEED"){
	        		$("span").html("Second player");
	        		poolIt();
	        		
	        	//->MORE THAN SECOND PLAYER	
	        	}else{
	        		$("span").html("Not admit more than two players");
	        	}
	        },
	        error: function () {
	        }
	    });
}
////////////////////////////////////////////////////////////////////////////////////////////////////////

//SEED (IF is the first player)
////////////////////////////////////////////////////////////////////////////////////////////////////////
$("#InitButton").click(function () {
		//Block seed button after sended.
		$("#InitButton").prop("disabled",true);
        $("span").html("Seed Sended"); 
        ajaxSendSeed();
        poolIt();
});

function ajaxSendSeed(){
	
	 $.ajax({
         type: "POST", 
         url: "/app/gengine/" + $("#nick").val(),
         dataType: "text", 
         data: $("#InitButton").val(),
         contentType: "text/plain",
         success: function (data) {
        	 if (data == "NOSEED"){
        		 $("span").html("Not enough players");
        		 $("#InitButton").prop("disabled",false);
        	 }else{
        		 $("#InitButton").prop("disabled",true);
        		 $("span").html(data);        		 
        	 }
             $("span").fadeOut('fast', function () {
                 $(this).fadeIn('fast');
             });
         }
     });
}
//////////////////////////////////////////////////////////////////////////////////////////////////////////


//GAME BUTTONS
//////////////////////////////////////////////////////////////////////////////////////////////////////////

function clickButton(message){
	
	$.ajax({
        type: "POST", // HTTP POST request
        url: "/app/gengine/" + $("#nick").val(),
        dataType: "text", 
        data: message, 
        contentType: "text/plain", 
        success: function (data) {
        	
        	if (data == "ENDOK"){
        		$("span").html("YOU WIN!"); 
        	}else{
        		$("span").html(data); 
        	}
        	
            $("span").fadeOut('fast', function () {
                $(this).fadeIn('fast');
            });
        }
    });	
	
	$("#plus").prop("disabled",true);
	$("#minus").prop("disabled",true);
	$("#zero").prop("disabled",true);
	
}


//////////////////////////////////////////////////////////////////////////////////////////////////////////
