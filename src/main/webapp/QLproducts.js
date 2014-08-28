var url = "products/getproductsjson";
            // prepare the data
            var source =
            {
                datatype: "json",
                datafields: [
                    { name: 'productName', type: 'string' },
                    { name: 'productPrice', type: 'float' },
                    { name: 'picLocation', type: 'string' },
                    { name: 'common', type: 'bool' }
                ],
                id: 'productID',
                url: url
            };
            var cellsrenderer = function (row, columnfield, value, defaulthtml, columnproperties, rowdata) {
                if (value < 20) {
                    return '<span style="margin: 4px; float: ' + columnproperties.cellsalign + '; color: #ff0000;">' + value + '</span>';
                }
                else {
                    return '<span style="margin: 4px; float: ' + columnproperties.cellsalign + '; color: #008000;">' + value + '</span>';
                }
            }
            var dataAdapter = new $.jqx.dataAdapter(source, {
                downloadComplete: function (data, status, xhr) { },
                loadComplete: function (data) { },
                loadError: function (xhr, status, error) { }
            });
            // initialize jqxGrid
           $("#jqxgridProducts").jqxGrid(
            {
                width: 1000,
                source: dataAdapter,
               sortable: true,
                pageable: true,
                autoheight: true,
                autoloadstate: false,
                autosavestate: false,
                columnsresize: true,
                columnsreorder: true,
                showfilterrow: true,
                filterable: true,				
                columnsresize: true,
				//rowsheight: 115,
				autorowheight: true,
				editable: true,
				showtoolbar: true,
				rendertoolbar: function (toolbar) {
                    var me = this;
                    var container = $("<div style='margin: 5px;'></div>");
                    toolbar.append(container);
                    container.append('<input id="addrowbutton" type="button" value="Them SP" />');
                    container.append('<input style="margin-left: 5px;" id="deleterowbutton" type="button" value="Xoa SP" />');
                    container.append('<input style="margin-left: 5px;" id="updaterowbutton" type="button" value="Sua SP" />');
                    $("#addrowbutton").jqxButton();
                    $("#deleterowbutton").jqxButton();
                    $("#updaterowbutton").jqxButton();
					
                    $("#deleterowbutton").on('click', function () {
                        var selectedrowindex = $("#jqxgridProducts").jqxGrid('getselectedrowindex');
                        var rowscount = $("#jqxgridProducts").jqxGrid('getdatainformation').rowscount;
                        if (selectedrowindex >= 0 && selectedrowindex < rowscount) {
                            var id = $("#jqxgridProducts").jqxGrid('getrowid', selectedrowindex);
							var productName = $('#jqxgridProducts').jqxGrid('getcellvalue', selectedrowindex, "productName");
							
							var result = confirm("Ban co chac muon xoa " + productName + ' ?');
							if (result==true) {
								//Logic to delete the item
								var url='/products/' + id + '?delete';
								
							 $.ajax({
							   url: url,
							   type: 'GET',
							  // contentType:'application/json',
							//   data: jsonData,
							  // dataType:'json',
							   success: function(data){
								 //On ajax success do this
								   location.reload();
								  },
							   error: function(xhr, ajaxOptions, thrownError) {
								  //On error do this
									
									
									if (xhr.status == 200) {

										alert("delete");
										location.reload();
									}
									else {
										alert(xhr.status);
										alert(thrownError);
									}
								}
							 });
		 
								var commit = $("#jqxgridProducts").jqxGrid('deleterow', selectedrowindex);
							}
							 
                            
                        }
					});					
                },				
				
				
                columns: [
				  { text: 'Ten SP', datafield: 'productName', width: 250 },
                  { text: 'Gia', datafield: 'productPrice', align: 'right', cellsalign: 'right', cellsformat: 'c0', columntype: 'numberinput', width: 250 },
                  { text: 'Hinh anh', datafield: 'picLocation', width: 230,
					cellsrenderer: function (row, column, value) {
						if (value)
							return '<img src="../../images/t-shirts/' + value + '"/>';
                      }
				  },
                  { text: 'SP ban chay', datafield: 'common', width: 120 }
                  ,{ text: 'Edit', datafield: 'Edit' ,columntype: 'button',
					cellsrenderer: function (row, column, value) {
                          return 'Edit';
                      }
					  , buttonclick: function (row) {
                     // open the popup window when the user clicks a button.
                     
					 var value = $('#jqxgridProducts').jqxGrid('getcellvalue', row, "uid");
					 location.href = "/products/" + value + "?form";
					
						}
				  }
              ]
            });
			
			
						           // events
            $("#jqxgridProducts").on('cellbeginedit', function (event) {
                
            });
            $("#jqxgridProducts").on('cellendedit', function (event) {

            });