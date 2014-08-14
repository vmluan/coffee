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
                width: 850,
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
				rowsheight: 115,
                columns: [
                  { text: 'Ten SP', datafield: 'productName', width: 250 },
                  { text: 'Gia', datafield: 'productPrice', width: 250 },
                  { text: 'Hinh anh', datafield: 'picLocation', width: 230,
					cellsrenderer: function (row, column, value) {
                          return '<img src="../../images/t-shirts/' + value + '"/>';
                      }
				  },
                  { text: 'SP ban chay', datafield: 'common', width: 120 }
              ]
            });