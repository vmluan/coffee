        function submitBan(){
		
		var productsDiv = document.getElementById('contenttablejqxgrid');
		var myObject = new Object();
		myObject.name = "John";
		myObject.age = 12;
		myObject.pets = ["cat", "dog"];
		
		var myArray = new Array();
		



		var productsChildren = productsDiv.children;

		for(i =0; i< productsChildren.length-1; i++){
			var object = new Object();
			var rowID = 'row' + i + 'jqxgrid';
			var rowDiv = productsChildren[i];
			
			var rowDivName = rowDiv.children[0];
			
			var name = rowDiv.children[0].textContent
			var id = 0;
			var quantity = rowDiv.children[1].textContent
			
			object.productName = name;
			object.productID = id;
			//object.quantity = quantity;
			
			if(name){
				
				myArray[i] = object
				}
		}
		//var myString = JSON.stringify(myArray[0]);
		//var myString = '{"encounters":[{"productName":"shail1","productID":"2"},{"productName":"shail2","productID":"3"}]}		'
				var myString = '{"encounters":[{"product":{"productName":"shail1","productID":"1"},"quantity": "2"}]}';	

		alert(myString);
		 $.ajax({
		   url: 'quanlyban?form',
		   type: 'POST',
		   contentType:'application/json',
		   data: myString,
		   dataType:'json',
		   success: function(data){
			 //On ajax success do this
			 alert(data);
			  },
		   error: function(xhr, ajaxOptions, thrownError) {
			  //On error do this
				
				
				if (xhr.status == 200) {

					alert(ajaxOptions);
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
                products = {
                    'Retro Rock T-shirt': {
                        pic: 'black-retro-rock-band-guitar-controller.png',
                        price: 15
                    },
                    'Lucky T-shirt': {
                        pic: 'bright-green-gettin-lucky-in-kentucky.png',
                        price: 18
                    },
                    'Loading T-shirt': {
                        pic: 'brown-loading-bar-computer-geek.png',
                        price: 25
                    },
                    'Cool Story T-shirt': {
                        pic: 'cool-story-bro.png',
                        price: 20
                    },
                    'The beard T-shirt': {
                        pic: 'fear-the-beard.png',
                        price: 17
                    },
                    'Don\'t care T-shirt': {
                        pic: 'honey-badger-don-t-care.png',
                        price: 19
                    },
                    'Guitar T-shirt': {
                        pic: 'scott-pilgrim-red-rock-band.png',
                        price: 24
                    },
                    'Dodgers T-shirt': {
                        pic: '2-sided-dodgers-bankrupt-t-shirt-ash.png',
                        price: 21
                    },
                    'Misfits T-shirt': {
                        pic: 'misfits-sf-giants-white.png',
                        price: 21
                    }
                },
            theme, onCart = false, cartItems = [], totalPrice = 0;

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
                    image, product, left = 0, top = 0, counter = 0;
                for (var name in products) {
                    product = products[name];
                    image = createProduct(name, product);
                    image.appendTo(catalog);
                    if (counter !== 0 && counter % 3 === 0) {
                        top += 147; // image.outerHeight() + productsOffset;
                        left = 0;
                    }
                    image.css({
                        left: left,
                        top: top
                    });
                    left += 127; // image.outerWidth() + productsOffset;
                    counter += 1;
                }
                $('.draggable-demo-product').jqxDragDrop({ dropTarget: $('#cart'), revert: true });
            };

            function createProduct(name, product) {
                return $('<div class="draggable-demo-product jqx-rc-all">' +
                        '<div class="jqx-rc-t draggable-demo-product-header jqx-widget-header-' + theme + ' jqx-fill-state-normal-' + theme + '">' +
                        '<div class="draggable-demo-product-header-label"> ' + name + '</div></div>' +
                        '<div class="jqx-fill-state-normal-' + theme + ' draggable-demo-product-price">Price: <strong>$' + product.price + '</strong></div>' +
                        '<img src="images/t-shirts/' + product.pic + '" alt='
                        + name + '" class="jqx-rc-b" />' +
                        '</div>');
            };

            function gridRendering() {
                $("#jqxgrid").jqxGrid(
                {
                    height: 335,
                    width: 230,
                    
                    keyboardnavigation: false,
                    selectionmode: 'none',
                    columns: [
                      { text: 'Ten', dataField: 'name', width: 120 },
                      { text: 'SL', dataField: 'count', width: 50 },
                      { text: 'Xoa', dataField: 'remove', width: 60 }
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
                });
            };

            function init() {
                theme = getDemoTheme();
                render();
                addClasses();
                addEventListeners();
            };

            function addItem(item) {
                var index = getItemIndex(item.name);
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
                            remove: '<div style="text-align: center; cursor: pointer; width: 53px;"' +
                         'id="draggable-demo-row-' + id + '">X</div>'
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
            }
        } ($));

        $(document).ready(function () {
            cart.init();
        });
 