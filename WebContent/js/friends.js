/**
 * La fonction d'affichage des amis
 */
function showFriends() {
	$.ajax({
			type : "GET",
			url : "li328_twitti/showFriends"
			data : "key=" + environnement.key
			datatype : "json"
			success : successResponseShowFriends
			error : function (errorThrown) {
				alert(errorThrown);
			}
	});
}

function traiteReponseShowFriends(resp) {
	if (resp.error != undefined) {
		func_erreur(rep.error);
	} else
		window.location.href = "test.jsp?key='" + resp.key +"'";
}