var dateTimeFormat = 'dd/MM/yyyy HH.mm.ss';
var url = "/quanlyban/tablelistjson";
var source =
            {
		datatype: "json",        
		datafields: [
                    { name: 'tableNumber' },
					{ name: 'tableID' },
                    { name: 'area' },
                    { name: 'customerName' },
                    { name: 'openTime', type: 'date'},
                    { name: 'closedTime', type: 'date'},
                    { name: 'totalMoney' },
                    { name: 'status' }
                ],
                id: 'tableID',
                url: url
            };
            var dataAdapter = new $.jqx.dataAdapter(source, {
                downloadComplete: function (data, status, xhr) { },
                loadComplete: function (data) { },
                loadError: function (xhr, status, error) { }
            });
            $("#customersGrid").jqxGrid(
            {
                width: 1000,
                height: 250,
                source: dataAdapter,                
                keyboardnavigation: false,
                columns: [
                    { text: 'Bàn Số', datafield: 'tableNumber', width: 100 },
                    { text: 'Khu', datafield: 'area', width: 100 },
                    { text: 'Tên KH', datafield: 'customerName', width: 200 },
					{ text: 'Tổng tiền', datafield: 'totalMoney', width: 120, cellsformat: 'c'},
                    { text: 'Thời gian vào', datafield: 'openTime', width: 180, cellsformat: dateTimeFormat},
					{ text: 'Thời gian ra', datafield: 'closedTime', width: 180, cellsformat: dateTimeFormat}
                    
                ]
            });

            // Orders Grid
            // prepare the data
            var dataFields = [
                        { name: 'productName' },
                        { name: 'quantity' },
                        { name: 'productPrice'},
                        { name: 'productPrice'},
                        { name: 'encounterTime' }
                    ];
            


//            var dataAdapter = new $.jqx.dataAdapter(source);
//            dataAdapter.dataBind();


            $("#customersGrid").on('rowselect', function (event) {
                var tableID = event.args.row.tableID;
                var urlEncounter = '/quanlyban/encounterlistjson?' + 'tableid=' + tableID;
                var sourceEncounter =
                {
                	datatype: "json",
                	datafields: [
                                 { name: 'product', map:'product>productName'},
                                 { name: 'quantity' },
                                 { name: 'productPrice'},
                                 { name: 'encounterTime', type: 'date'}
                             ],
                	id: 'encounterID',
                	url: urlEncounter
                };
                var dataAdapter = new $.jqx.dataAdapter(sourceEncounter, {
                    downloadComplete: function (data, status, xhr) { },
                    loadComplete: function (data) { },
                    loadError: function (xhr, status, error) { }
                });
				
                // update data source.
                $("#ordersGrid").jqxGrid({ source: dataAdapter });
				
            });

            $("#ordersGrid").jqxGrid(
            {
                width: 1000,
                height: 250,
                keyboardnavigation: false,
                columns: [
                    { text: 'Tên SP', datafield: 'product', width: 180 },
                    { text: 'SL', datafield: 'quantity', width: 100 },
                    { text: 'Gia', datafield: 'productPrice',  width: 150 },
					{ text: 'Thành Tiền', width: 150 },				
                    { text: 'Thời gian', datafield: 'encounterTime', cellsformat: dateTimeFormat }
                ]
            });
            $("#customersGrid").jqxGrid('selectrow', 0);