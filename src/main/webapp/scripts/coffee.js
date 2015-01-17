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