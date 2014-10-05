

//var cart = (function ($) {
theme = $.jqx.theme;
//var productsOffset = 3,
var productsOffset = 50, products = sampleProducts, theme, onCart = false, cartItems = [], totalPrice = 0;
var existingTable;
var sttID;

function render() {
	productsRendering();
	gridRendering();
};

function addClasses() {
	$('.draggable-demo-catalog')
			.addClass('jqx-scrollbar-state-normal-' + theme);
	$('.draggable-demo-title').addClass('jqx-expander-header-' + theme);
	$('.draggable-demo-title')
			.addClass('jqx-expander-header-expanded-' + theme);
	$('.draggable-demo-total').addClass('jqx-expander-header-' + theme)
			.addClass('jqx-expander-header-expanded-' + theme);
	if (theme === 'shinyblack') {
		$('.draggable-demo-shop').css('background-color', '#555');
		$('.draggable-demo-product').css('background-color', '#999');
	}
};

function productsRendering() {
	var catalog = $('#catalog'), imageContainer = $('</div>'), image, product, left = 0, top = 30, counter = 0;
	for ( var name in products) {
		product = products[name];
		image = createProduct(name, product);
		image.appendTo(catalog);
		if (counter !== 0 && counter % 4 === 0) {
			top += 140 + 30; // image.outerHeight() + productsOffset;
			left = 0;
		}
		image.css({
			left : left,
			top : top
		});
		left += 115 + productsOffset; // image.outerWidth() + productsOffset;
		counter += 1;
	}
	$('.draggable-demo-product').jqxDragDrop({
		dropTarget : $('#cart'),
		revert : true
	});
};

function createProduct(name, product) {

	return $('<div class="draggable-demo-product jqx-rc-all">'
			+ '<div class="jqx-rc-t draggable-demo-product-header jqx-widget-header-'
			+ theme + ' jqx-fill-state-normal-' + theme + '">'
			+ '<div class="draggable-demo-product-header-label"> ' + name
			+ '</div></div>' + '<div class="jqx-fill-state-normal-' + theme
			+ ' draggable-demo-product-price">Price: <strong>$' + product.price
			+ '</strong></div>' + '<img src="/images/t-shirts/' + product.pic
			+ '"' + ' alt="' + name + '" class="jqx-rc-b" />' + '</div>');
};

function gridRendering() {
	$("#jqxgrid").jqxGrid({
		height : 300,
		//width: 290,
		width : 394,

		keyboardnavigation : false,
		selectionmode : 'none',
		columns : [ {
			text : '',
			dataField : 'stt',
			width : 40
		}, {
			text : '',
			dataField : 'hiddenCount',
			width : 0,
			hidden: true
		}, {
			text : 'Ten',
			dataField : 'name',
			width : 200
		}, {
			text : 'SL',
			dataField : 'count',
			width : 100
		}
		]
	});
	$("#jqxgrid").bind('cellclick', function(event) {
		var index = event.args.rowindex;
		if (event.args.datafield == 'remove') {

			var item = cartItems[index];
			var inputDiv = '#' + 'row' + index + 'jqxgridQuantity';
			var count = item.hiddenCount;

			if (count > 1) {
				updateQuantity(index, -1);
				cartItems[index].hiddenCount -= 1;
			} else {
				cartItems.splice(index, 1);
				removeGridRow(index);
				//update quantity input id
				udpateQuantityID(index);

			}
			updatePrice(-item.price);
		} else if (event.args.datafield == 'add') {
			var item = cartItems[index];

			if (index >= 0)
				updateQuantity(index, 1);

			updatePrice(item.price);
		}
	});

};

function init() {
	// alert(table);

	theme = getDemoTheme();
	render();
	addClasses();
	addEventListeners();
	if (table) {
		loadItem();
		document.getElementById('tableNumber').value = table.tableNumber;
		document.getElementById('customerName').value = table.customerName;
	}
};

//load item to the list.
function loadItem() {

	for (i = 0; i <= table.encounters.length - 1; i++) {
		var item = new Object();
		item.price = parseInt(table.encounters[i].product.productPrice);
		item.name = table.encounters[i].product.productName;

		for (j = 1; j <= table.encounters[i].quantity; j++)
			addItem({
				price : parseInt(item.price),
				name : item.name
			});
	}

};

function addItem(item) {

	var index = getItemIndex(item.name);
	
	if (index >= 0) {
		updateQuantity(index, 1);
		//   updateGridRow(index, cartItems[index]);

	} else {
		
		var id = cartItems.length, item = {
			name : item.name,
			//count: 1,
			count : '<button type="button" id="removeButton'
					+ id
					+ '" onClick="removeButton('
					+ id
					+ ')"><span class="glyphicon glyphicon-arrow-down"></span></button>'
					+ '<input '
					+ 'id="row'
					+ id
					+ 'jqxgridQuantity" '
					+ 'type="text" value="1" style="width: 35%; height: 100%; border: 0px;background-color: rgb(197, 225, 226);" class="rowQuantity" onChange="updateCount(' + id +')"/>'
					+ '<button type="button" id="addButton'
					+ id
					+ '" onClick="addButton('
					+ id
					+ ')"><span class="glyphicon glyphicon-arrow-up"></span></button>',
			price : item.price,
			index : id,
			stt : id + 1,
			hiddenCount : 1
		};

		cartItems.push(item);
		addGridRow(item);

	}
	updatePrice(item.price);
	//addListenerForQuantity();
};
/*
 * add function to update quantity value 
 */
function updateQuantity(index, number) {
	var inputID = '#row' + index + 'jqxgridQuantity';
	var currentValue = parseInt($("#jqxgrid").jqxGrid('getrowdata', index).hiddenCount, 10);
	var newValue = currentValue + number;

	cartItems[index].count = '<button type="button" id="removeButton" onClick="removeButton('
			+ index
			+ ')"><span class="glyphicon glyphicon-arrow-down"></span></button>'
			+ '<input '
			+ 'id="row'
			+ index
			+ 'jqxgridQuantity" '
			+ 'type="text" value="'
			+ newValue
			+ '" style="width: 35%; height: 100%; border: 0px;background-color: rgb(197, 225, 226);" class="rowQuantity" onChange="updateCount(' + id +')"/>'
			+ '<button type="button" id="addButton'
			+ index
			+ '" onClick="addButton('
			+ index
			+ ')"><span class="glyphicon glyphicon-arrow-up"></span></button>'			;
	cartItems[index].hiddenCount += number;
	updateGridRow(index, cartItems[index]);
};

function udpateQuantityID(index) {
	for ( var id = index; id < cartItems.length; id++) {
		var value = cartItems[id].hiddenCount;

		var removeButton = '<button type="button" id="removeButton" onClick="removeButton('
				+ id
				+ ')"><span class="glyphicon glyphicon-arrow-down"></span></button>';
		var input = '<input '
				+ 'id="row'
				+ id
				+ 'jqxgridQuantity" '
				+ 'type="text" value="'
				+ value
				+ '" style="width: 35%; height: 100%; border: 0px;background-color: rgb(197, 225, 226);" class="rowQuantity" onChange="updateCount(' + id +')"/>'
		var addButton = '<button type="button" id="addButton'
				+ id
				+ '" onClick="addButton('
				+ id
				+ ')"><span class="glyphicon glyphicon-arrow-up"></span></button>'
				;
		cartItems[id].count = removeButton + input + addButton;
		updateGridRow(id, cartItems[id]);
	}
};
/*
	* this function is to update the index and stt of cartItems
*/
function updateOrderingNumber(index){
	console.log(index);
	for ( var id = index; id < cartItems.length; id++) {
		cartItems[id].index = id;
		cartItems[id].stt = cartItems[id].index + 1;
		
		updateGridRow(id, cartItems[id]);
	}

};
function updateCount(index){
	var inputID = '#row' + index + 'jqxgridQuantity';
	
	var newValue = parseInt($(inputID).val(), 10);
	var delta = newValue - cartItems[index].hiddenCount;
	console.log(newValue);
	updateQuantity(index, delta);

}

function updatePrice(price) {
	totalPrice += price;
	$('#total').html('$ ' + totalPrice);
};

function addGridRow(row) {
	$("#jqxgrid").jqxGrid('addrow', null, row);
};

function updateGridRow(id, row) {
	var rowID = $("#jqxgrid").jqxGrid('getrowid', id);
	$("#jqxgrid").jqxGrid('updaterow', rowID, row);
};

function removeGridRow(id) {
	var rowID = $("#jqxgrid").jqxGrid('getrowid', id);
	$("#jqxgrid").jqxGrid('deleterow', rowID);
};

function getItemIndex(name) {
	for ( var i = 0; i < cartItems.length; i += 1) {
		if (cartItems[i].name === name) {
			return i;
		}
	}
	return -1;
};

function toArray(obj) {
	var item, array = [], counter = 1;
	for ( var key in obj) {
		item = {};
		item = {
			name : key,
			price : obj[key].count,
			count : obj[key].price,
			number : counter
		}
		array.push(item);
		counter += 1;
	}
	return array;
};

function addEventListeners() {
	$('.draggable-demo-product').mouseenter(function() {
		$(this).children('.draggable-demo-product-price').fadeTo(100, 0.9);
	});
	$('.draggable-demo-product').mouseleave(function() {
		$(this).children('.draggable-demo-product-price').fadeTo(100, 0);
	});
	$('.draggable-demo-product').bind('dropTargetEnter', function(event) {
		$(event.args.target).css('border', '2px solid #000');
		onCart = true;
		$(this).jqxDragDrop('dropAction', 'none');

	});
	$('.draggable-demo-product').bind('dropTargetLeave', function(event) {
		$(event.args.target).css('border', '2px solid #aaa');
		onCart = false;
		$(this).jqxDragDrop('dropAction', 'default');
	});
	$('.draggable-demo-product').bind('dragEnd', function(event) {
		$('#cart').css('border', '2px dashed #aaa');
		if (onCart) {
			addItem({
				price : event.args.price,
				name : event.args.name
			});
			onCart = false;
		}
	});
	$('.draggable-demo-product').bind(
			'dragStart',
			function(event) {
				var tshirt = $(this).find('.draggable-demo-product-header')
						.text(), price = $(this).find(
						'.draggable-demo-product-price').text().replace(
						'Price: $', '');
				$('#cart').css('border', '2px solid #aaa');
				price = parseInt(price, 10);
				$(this).jqxDragDrop('data', {
					price : price,
					name : tshirt
				});
			});
	$('.draggable-demo-product').bind(
			'click',
			function(event) {
				//console.log(this);
				var tshirt = $(this).find('.draggable-demo-product-header')
						.text().trim(), price = $(this).find(
						'.draggable-demo-product-price').text().replace(
						'Price: $', '');
				price = parseInt(price, 10);

				addItem({
					price : price,
					name : tshirt
				});
			});

};

//        return {
//           init: init
//      };

//  } ($));

function removeButton(index) {
	var item = cartItems[index];
	var count = item.hiddenCount;

	if (count > 1) {
		updateQuantity(index, -1);
	} else {
		cartItems.splice(index, 1);
		removeGridRow(index);
		//update quantity input id
		udpateQuantityID(index);
		updateOrderingNumber(index);

	}
	updatePrice(-item.price);
};
function addButton(index) {
	var item = cartItems[index];

	if (index >= 0)
		updateQuantity(index, 1);
	updatePrice(item.price);
};

$(document).ready(function() {
	init();
	if (table)
		existingTable = table;
	 $("#tableNumber").jqxTooltip({ content: '<b>So ban:</b>', position: 'top', name: 'movieTooltip'});
	 
	  $("#customerName").jqxTooltip({ content: '<b>Ten Khach Hang</b>', position: 'top', name: 'movieTooltip'});
});

function submitBan(PAID) {
	$('.btn').attr('disabled','disabled');
	
	if(existingTable)
		tableID = existingTable.tableID;
	
	var productsDiv = document.getElementById('contenttablejqxgrid');
	var encounters = new Array();
	var productsChildren = productsDiv.children;

	var tableNumber = document.getElementById('tableNumber').value;
	var customerName = document.getElementById('customerName').value;

	if (!tableNumber) {
		alert("Vui long nhap so ban");
		return;
	}
	for (i = 0; i < cartItems.length; i++) {
		var product = new Object();
		var encounter = new Object();

		product.productName = $.trim(cartItems[i].name);
		//product.productID = id;

		encounter.product = product;
		encounter.quantity = $.trim(cartItems[i].hiddenCount);

		encounters[i] = encounter;
	}

	var table = new Object();
	table.encounters = encounters;
	table.customerName = customerName;
	table.tableNumber = tableNumber;
	if (PAID)
		table.status = PAID;

	var url, urlGet, urlPost;
	urlGet = '/quanlyban?form';
	
	

	if (tableID) {
		urlPost = '/quanlyban/' + tableID + '?form';
		url = urlPost;
		table.tableID = tableID;
	} else
		url = urlGet;

	
	var jsonData = JSON.stringify(table);
	console.log(jsonData);
	$.ajax({
		url : url,
		type : 'POST',
		contentType : 'application/json',
		data : jsonData,
		dataType : 'json',
		success : function(data) {
			//On ajax success do this
			alert(data);
		},
		error : function(xhr, ajaxOptions, thrownError) {
			//On error do this

			if (xhr.status == 200) {
				$('.btn').removeAttr('disabled');
				if(existingTable.tableID)
					$(".jqx-notification-content-form").text('Cap nhat thanh cong');
				else
					$(".jqx-notification-content-form").text('Them ban thanh cong');
				$("#messageNotification").jqxNotification("open");
				$(".jqx-notification-container").css("z-index", 30000);
			} else {
				alert(xhr.status);
				alert(thrownError);
			}
		}
	});

};
