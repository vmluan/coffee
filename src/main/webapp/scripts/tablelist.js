            var source =
            {
                datafields: [
                    { name: 'tableName' },
					{ name: 'tableID' },
                    { name: 'area' },
                    { name: 'customerName' },
                    { name: 'openedTime' },
                    { name: 'closedTime' },
                    { name: 'totalMoney' },
                    { name: 'status' }
                ],
                localdata: [{"tableName" : "Ban 1", "tableID" : 1 ,"area" : null, "customerName": "Luan" , "openedTime": "2014-11-09 18:30", "closedTime" : "2014-11-09 19:00",
					 "totalMoney" : 100000, "status" : "PAID"}, {"tableName" : "Ban 2", "tableID" : 2 , "area" : null, "customerName": "Hoang" , "openedTime": "2014-11-09 18:30", "closedTime" : "2014-11-09 19:00", "totalMoney" : 100000, "status" : "PAID"}]
            };
            var dataAdapter = new $.jqx.dataAdapter(source);

            $("#customersGrid").jqxGrid(
            {
                width: 850,
                height: 250,
                source: dataAdapter,
                
                keyboardnavigation: false,
                columns: [
                    { text: 'So Ban', datafield: 'tableName', width: 250 },
                    { text: 'Khu', datafield: 'area', width: 150 },
                    { text: 'Ten KH', datafield: 'customerName', width: 180 },
                    { text: 'Thoi gian', datafield: 'openedTime', width: 120 },
                    { text: 'Tong Tien', datafield: 'totalMoney'}
                ]
            });

            // Orders Grid
            // prepare the data
            var dataFields = [
                        { name: 'productName' },
                        { name: 'quantity' },
                        { name: 'unitPrice'},
                        { name: 'totalMoney'},
                        { name: 'tableID' }
                    ];

            var source =
            {
                datafields: dataFields,
                localdata: [{"productName" : "Cafe den", "quantity": 3, "unitPrice" : 10000, "totalMoney" : 30000, "tableID" : 1}]
            };

            var dataAdapter = new $.jqx.dataAdapter(source);
            dataAdapter.dataBind();

            $("#customersGrid").on('rowselect', function (event) {
                var tableID = event.args.row.tableID;
                var records = new Array();
                var length = dataAdapter.records.length;
                for (var i = 0; i < length; i++) {
                    var record = dataAdapter.records[i];
                    if (record.tableID == tableID) {
                        records[records.length] = record;
                    }
                }

                var dataSource = {
                    datafields: dataFields,
                    localdata: records
                }
                var adapter = new $.jqx.dataAdapter(dataSource);
        
                // update data source.
                $("#ordersGrid").jqxGrid({ source: adapter });
            });

            $("#ordersGrid").jqxGrid(
            {
                width: 850,
                height: 250,
                keyboardnavigation: false,
                columns: [
                    { text: 'Ten SP', datafield: 'productName', width: 100 },
                    { text: 'SL', datafield: 'quantity', width: 150 },
                    { text: 'Gia', datafield: 'unitPrice',  width: 150 },
                    { text: 'Thanh Tien', datafield: 'totalMoney' }
                ]
            });

            $("#customersGrid").jqxGrid('selectrow', 0);