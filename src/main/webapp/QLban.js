        function submitBan(tableID, PAID){
		
		//alert($.trim('  string with spaces at the ends   '));
		var productsDiv = document.getElementById('contenttablejqxgrid');
		var encounters = new Array();
		var productsChildren = productsDiv.children;
		
		var tableNumber = document.getElementById('tableNumber').value;
		var customerName = document.getElementById('customerName').value;
		
		if (!tableNumber){
			alert("Vui long nhap so ban");
			return;
		}
		for(i =0; i< productsChildren.length-1; i++){
			var product = new Object();
			var encounter = new Object();
			
			var rowID = 'row' + i + 'jqxgrid';
			var rowDiv = productsChildren[i];
			
			var rowDivName = rowDiv.children[0];
			
			var name = rowDiv.children[1].textContent
			var id = 0;
			var quantity = rowDiv.children[2].textContent
			
			product.productName = $.trim(name);
			product.productID = id;
			
			encounter.product = product;
			encounter.quantity = quantity;
			
			if(name){
				
				encounters[i] = encounter
				}
		}

		//var jsonData = JSON.stringify(myArray[0]);
		//var jsonData = '{"encounters":[{"productName":"shail1","productID":"2"},{"productName":"shail2","productID":"3"}]}		'
	//			var jsonData = '{"encounters":[{"product":{"productName":"shail1","productID":"1"},"quantity": "2"}]}';
		var table =new Object();
		table.encounters = encounters;
		table.customerName = customerName;
		table.tableNumber = tableNumber;
		if(PAID)
			table.status = PAID;
		
		
		var url, urlGet, urlPost;
		urlGet = '/quanlyban?form';
		
		if(tableID){
			urlPost = '/quanlyban/' + tableID + '?form';
			url = urlPost;
			table.tableID = tableID;
		}else
			url = urlGet;
			
		var jsonData = JSON.stringify(table);

//alert(jsonData);
		 $.ajax({
		   url: url,
		   type: 'POST',
		   contentType:'application/json',
		   data: jsonData,
		   dataType:'json',
		   success: function(data){
			 //On ajax success do this
			 alert(data);
			  },
		   error: function(xhr, ajaxOptions, thrownError) {
			  //On error do this
				
				
				if (xhr.status == 200) {

				//	alert("Them ban thanh cong");
					location.reload();
				}
				else {
					alert(xhr.status);
					alert(thrownError);
				}
			}
		 });	
	
};
		
		var cart = (function ($) {
            theme = $.jqx.theme;
            var productsOffset = 3,
                products = sampleProducts,
            theme, onCart = false, cartItems = [], totalPrice = 0;
			
			var sttID;

            function render() {
                productsRendering();
                gridRendering();
            };

            function addClasses() {
                $('.draggable-demo-catalog').addClass('jqx-scrollbar-state-normal-' + theme);
                $('.draggable-demo-title').addClass('jqx-expander-header-' + theme);
                $('.draggable-demo-title').addClass('jqx-expander-header-expanded-' + theme);
                $('.draggable-demo-total').addClass('jqx-expander-header-' + theme).addClass('jqx-expander-header-expanded-' + theme);
                if (theme === 'shinyblack') {
                    $('.draggable-demo-shop').css('background-color', '#555');
                    $('.draggable-demo-product').css('background-color', '#999');
                }
            };

            function productsRendering() {
                var catalog = $('#catalog'),
                    imageContainer = $('</div>'),
                    image, product, left = 0, top = 30, counter = 0;
                for (var name in products) {
                    product = products[name];
                    image = createProduct(name, product);
                    image.appendTo(catalog);
                    if (counter !== 0 && counter % 6 === 0) {
                        top += 140; // image.outerHeight() + productsOffset;
                        left = 0;
                    }
                    image.css({
                        left: left,
                        top: top
                    });
                    left += 115; // image.outerWidth() + productsOffset;
                    counter += 1;
                }
                $('.draggable-demo-product').jqxDragDrop({ dropTarget: $('#cart'), revert: true });
            };

            function createProduct(name, product) {
                return $('<div class="draggable-demo-product jqx-rc-all">' +
                        '<div class="jqx-rc-t draggable-demo-product-header jqx-widget-header-' + theme + ' jqx-fill-state-normal-' + theme + '">' +
                        '<div class="draggable-demo-product-header-label"> ' + name + '</div></div>' +
                        '<div class="jqx-fill-state-normal-' + theme + ' draggable-demo-product-price">Price: <strong>$' + product.price + '</strong></div>' +
                        '<img src="/images/t-shirts/' + product.pic + '" alt='
                        + name + '" class="jqx-rc-b" />' +
                        '</div>');
            };

            function gridRendering() {
                $("#jqxgrid").jqxGrid(
                {
                    height: 300,
                    //width: 290,
					width: 394,
                    
                    keyboardnavigation: false,
                    selectionmode: 'none',
                    columns: [
						{ text: '', dataField: 'stt', width: 40 },
                      { text: 'Ten', dataField: 'name', width: 200 },
                      { text: 'SL', dataField: 'count', width: 70 },
                      { text: 'Giam', dataField: 'remove', width: 42 },
					  { text: 'Tang', dataField: 'add', width: 42 }
					  
                    ]
                });
                $("#jqxgrid").bind('cellclick', function (event) {
                    var index = event.args.rowindex;
                    if (event.args.datafield == 'remove') {
                        var item = cartItems[index];
                        if (item.count > 1) {
                            item.count -= 1;
                            updateGridRow(index, item);
                        }
                        else {
                            cartItems.splice(index, 1);
                            removeGridRow(index);
                        }
                        updatePrice(-item.price);
                    }
					else if (event.args.datafield == 'add'){
                        var item = cartItems[index];
                        if (item.count > 0) {
                            item.count += 1;
                            updateGridRow(index, item);
                        }

                        updatePrice(item.price);					
					}
                });
				
			//add dropdownlist
			/*
                var urlDropList = "/products/getproductsjson";
                var source =
                {
                    datatype: "json",
                    datafields: [
                        { name: 'productName' },
                        { name: 'productPrice' }
                    ],
                    url: urlDropList,
                    async: false
                };
                var dataAdapter = new $.jqx.dataAdapter(source);
          
				$("#jqxWidget").jqxDropDownList({ selectedIndex: -1, source: dataAdapter, displayMember: "productName"
					, valueMember: "productPrice", width: 400, height: 25});
                $('#jqxWidget').on('select', function (event) {
                    var args = event.args;
                    var item = $('#jqxWidget').jqxDropDownList('getItem', args.index);
					
                    if (item != null) {
                        var price = sampleProducts[item.label].price;
						addItem({ price: parseInt(price), name: item.label });
						$("#jqxWidget").jqxDropDownList('selectIndex', -1); 
                    }
                });
	*/			
				
	 		

	/*
		using jqxcombobox
	
				$("#jqxWidget").jqxComboBox({ selectedIndex: -1, source: dataAdapter, displayMember: "productName"
				, valueMember: "productPrice", width: 400, height: 25
				//, checkboxes: true
				});
		*/		
			
             /*   $('#jqxWidget').on('select', function (event) {
					 if (event.args){
						var args = event.args;
						var item = $('#jqxWidget').jqxComboBox('getItem', args.index);
						
						if (item != null) {
							var price = sampleProducts[item.label].price;
							addItem({ price: parseInt(price), name: item.label });
							$("#jqxWidget").jqxComboBox('selectIndex', -1); 
						}
					}
                });
				*/
			/*	$('#jqxWidget').on('checkChange', function (event) 
				{
					var args = event.args;
					if (args) {
					// index represents the item's index.                          
					var index = args.index;
					var item = args.item;
					// get item's label and value.
					var label = item.label;
					var value = item.value;
					alert(label);
				}
				}); 
			*/
  
				
			//end of dropdown
				
            };

            function init() {
				// alert(table);

                theme = getDemoTheme();
                render();
                addClasses();
                addEventListeners();
				 if(table){
					loadItem();
					document.getElementById('tableNumber').value = table.tableNumber;
					document.getElementById('customerName').value = table.customerName;
				 }				
            };

		//load item to the list.
		function loadItem(){
			
			for(i =0; i<= table.encounters.length-1; i++){
				var item = new Object();
				item.price = parseInt(table.encounters[i].product.productPrice);
				item.name = table.encounters[i].product.productName;
				
				
				for (j=1; j <=table.encounters[i].quantity; j++)
					addItem({ price: parseInt(item.price), name: item.name });
			}

			
		};			

            function addItem(item) {
				
                var index = getItemIndex(item.name);
			//	alert(index);
                if (index >= 0) {
                    cartItems[index].count += 1;
                    updateGridRow(index, cartItems[index]);
                } else {
                    var id = cartItems.length,
                        item = {
                            name: item.name,
                            count: 1,
                            price: item.price,
                            index: id,
                            remove: '<div style="text-align: center; cursor: pointer; width: 40px;"' +
                         'id="draggable-demo-row-' + id + '">X</div>',
							 add: '<div style="text-align: center; cursor: pointer; width: 40px;"' +
							 'id="draggable-demo-row-' + id + '">+</div>',
						 stt: id + 1
                        };
						
                    cartItems.push(item);
                    addGridRow(item);
                }
                updatePrice(item.price);
            };


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
                for (var i = 0; i < cartItems.length; i += 1) {
                    if (cartItems[i].name === name) {
                        return i;
                    }
                }
                return -1;
            };

            function toArray(obj) {
                var item, array = [], counter = 1;
                for (var key in obj) {
                    item = {};
                    item = {
                        name: key,
                        price: obj[key].count,
                        count: obj[key].price,
                        number: counter
                    }
                    array.push(item);
                    counter += 1;
                }
                return array;
            };

            function addEventListeners() {
                $('.draggable-demo-product').mouseenter(function () {
                    $(this).children('.draggable-demo-product-price').fadeTo(100, 0.9);
                });
                $('.draggable-demo-product').mouseleave(function () {
                    $(this).children('.draggable-demo-product-price').fadeTo(100, 0);
                });
                $('.draggable-demo-product').bind('dropTargetEnter', function (event) {
                    $(event.args.target).css('border', '2px solid #000');
                    onCart = true;
                    $(this).jqxDragDrop('dropAction', 'none');
					
                });
                $('.draggable-demo-product').bind('dropTargetLeave', function (event) {
                    $(event.args.target).css('border', '2px solid #aaa');
                    onCart = false;
                    $(this).jqxDragDrop('dropAction', 'default');
                });
                $('.draggable-demo-product').bind('dragEnd', function (event) {
                    $('#cart').css('border', '2px dashed #aaa');
                    if (onCart) {
                        addItem({ price: event.args.price, name: event.args.name });
                        onCart = false;
                    }
                });
                $('.draggable-demo-product').bind('dragStart', function (event) {
                    var tshirt = $(this).find('.draggable-demo-product-header').text(),
                        price = $(this).find('.draggable-demo-product-price').text().replace('Price: $', '');
                    $('#cart').css('border', '2px solid #aaa');
                    price = parseInt(price, 10);
                    $(this).jqxDragDrop('data', {
                        price: price,
                        name: tshirt
                    });
                });
            };

            return {
                init: init
            };
			
        } ($));


        $(document).ready(function () {
            cart.init();
        });
 