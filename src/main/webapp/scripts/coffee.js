function formatCurrency(value, currency) {
	return currency
			+ value.replace(/\D/g, "").replace(/\B(?=(\d{3})+(?!\d))/g, ".");
}