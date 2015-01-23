function formatCurrency(value, currency) {
	return currency
			+ value.replace(/\D/g, "").replace(/\B(?=(\d{3})+(?!\d))/g, ".");
}

function updateHeaderStatus(currentActiveDiv, newActiveDiv){
	var currentElem = document.getElementById(currentActiveDiv);
	var newElem = document.getElementById(newActiveDiv);
	currentElem.className="";
	newElem.className="active";
}

function setDivActive(newActiveDiv){
	var currentActive = document.getElementsByClassName('active');
	for (var i =0; i < currentActive.length; i++){
		currentActive[i].className="";
	}
	var newElem = document.getElementById(newActiveDiv);
	if(newElem)
		newElem.className="active";
} 