<html>
	<body>
		  <th:block layout:fragment="script">

			<script>							 
				 $(function () {
					  $("select").select2();
					});									
			 </script>
			  <script>
					/*$(document.body).on("change","#house_Id",function(event){
					 alert(this.value); 
					 const t = document.getElementById('clientName');
					  var operator = event.target.value+"''";
					  t.value += operator;
					  console.log(`e.target.value = ${ event.target.value }`);
					});*/
			 </script>							   
			 <script type="text/javascript">  			 
				$(function(){
				    $("#btnReset").click(function(){
				        $("#house_Id").select2('val', 0);
				    });
				});
			 </script> 
			 
			 <script>
			 	$(document).ready(function() {												
						// GET REQUEST
						$("#house_Id").change(function(event) {
						
							event.preventDefault();
							ajaxGet();
						});
			
						// DO GET
						function ajaxGet() {
						
						 let cid = $('#house_Id').val();
						 let data = {                        
									 id: cid
								    };
								    
							$.ajax({
								type : "GET",
								url : "/getBillingById/",
								data: data,
								success : function(result) {
									console.log("hi Kelvin Sankale!!! hoorray");
									if (result.status == "success") {
										/*
										$('#getResultDiv ul').empty();
																				
										$.each(result.data,
										function(i, hms) {																					
												var user = "House  " + result.data.id
														+ ", Name  = " + result.data.depotName
														+ "<br>";
												$('#getResultDiv .list-group').append(user)
																				});
														*/
										$('#newWaterBill').val(0.0);
										$('#currentMeterReadings').val(0.0);
										$('#waterUnitsUsed').val(0.0);
										$('#totalBalanceBf').val(0.0);
										$('#totalNewBill').val(0.0);
										$('#totalAmount').val(0.0);
										$('#billingDate').val(Date());
										
										console.log( "Today is on : "+Date());
									
											
											var totalAmountPaid = result.data.totalAmountPaid;
											var totalRentalInvoices = result.data.totalRentInvoices;
											var totalWaterInvoices = result.data.totalWaterInvoices;
											var totalTrashInvoices = result.data.totalTrashInvoices;
																																			
											var newRentalBill = result.data.newRentAmount;
											var waterRates = result.data.waterRates;
											var newTrashCharges = result.data.newTrashCharges;
											var currentMeterReadings = result.data.currentMeterReadings;
											
										var house = ""+result.data.houseId;
										var client = ""+result.data.clientName;
										document.getElementById('clientName').value=client;
										
										document.getElementById('totalAmountPaid').value=totalAmountPaid;										
										
										document.getElementById('totalRentInvoices').value=totalRentalInvoices;
										document.getElementById('totalWaterInvoices').value=totalWaterInvoices;
										document.getElementById('totalTrashInvoices').value=totalTrashInvoices;
										
										document.getElementById('newRentAmount').value=newRentalBill;
										document.getElementById('waterRates').value=waterRates;
										document.getElementById('newTrashCharges').value=newTrashCharges;
										document.getElementById('currentMeterReadings').value=0;
										document.getElementById('previousMeterReadings').value=currentMeterReadings;
										
										//document.getElementById('totalRentInvoices').style.display = 'none';
										document.getElementById("totalRentInvoices").style.color = 'crimson';
										document.getElementById("totalRentInvoices").style.fontSize = "20px";
										document.getElementById("totalRentInvoices").style.fontWeight = "bold";
										document.getElementById("totalRentInvoices").style.textAlign = "center";
										document.getElementById('totalRentInvoices').readOnly = true;										
										
										//document.getElementById('totalWaterInvoices').style.display = 'none';
										document.getElementById("totalWaterInvoices").style.color = 'crimson';
										document.getElementById("totalWaterInvoices").style.fontSize = "20px";
										document.getElementById("totalWaterInvoices").style.fontWeight = "bold";
										document.getElementById("totalWaterInvoices").style.textAlign = "center";
										document.getElementById('totalWaterInvoices').readOnly = true;
										
										//document.getElementById('totalTrashInvoices').style.display = 'none';
										document.getElementById("totalTrashInvoices").style.color = 'crimson';
										document.getElementById("totalTrashInvoices").style.fontSize = "20px";
										document.getElementById("totalTrashInvoices").style.fontWeight = "bold";
										document.getElementById("totalTrashInvoices").style.textAlign = "center";
										document.getElementById('totalTrashInvoices').readOnly = true;
										
										
										//document.getElementById('totalAmountPaid').style.display = 'none';
										document.getElementById("totalAmountPaid").style.color = 'green';
										document.getElementById("totalAmountPaid").style.fontSize = "20px";
										document.getElementById("totalAmountPaid").style.fontWeight = "bold";
										document.getElementById("totalAmountPaid").style.textAlign = "center";
										
										//document.getElementById('newRentAmount').style.display = 'none';
										document.getElementById("newRentAmount").style.color = 'Navy';
										document.getElementById("newRentAmount").style.fontSize = "20px";
										document.getElementById("newRentAmount").style.fontWeight = "bold";
										document.getElementById("newRentAmount").style.textAlign = "center";
										
										//document.getElementById('waterRates').style.display = 'none';
										document.getElementById("waterRates").style.color = 'Navy';
										document.getElementById("waterRates").style.fontSize = "20px";
										document.getElementById("waterRates").style.fontWeight = "bold";
										document.getElementById("waterRates").style.textAlign = "center";
										
										//document.getElementById('newTrashCharges').style.display = 'none';
										document.getElementById("newTrashCharges").style.color = 'Navy';
										document.getElementById("newTrashCharges").style.fontSize = "20px";
										document.getElementById("newTrashCharges").style.fontWeight = "bold";
										document.getElementById("newTrashCharges").style.textAlign = "center";
										
										//document.getElementById('currentMeterReadings').style.display = 'none';
										document.getElementById("currentMeterReadings").style.color = 'Navy';
										document.getElementById("currentMeterReadings").style.fontSize = "20px";
										document.getElementById("currentMeterReadings").style.fontWeight = "bold";
										document.getElementById("currentMeterReadings").style.textAlign = "center";
										
										//document.getElementById('previousMeterReadings').style.display = 'none';
										document.getElementById("previousMeterReadings").style.color = 'Navy';
										document.getElementById("previousMeterReadings").style.fontSize = "20px";
										document.getElementById("previousMeterReadings").style.fontWeight = "bold";
										document.getElementById("previousMeterReadings").style.textAlign = "center";
										
										//document.getElementById('waterUnitsUsed').style.display = 'none';
										document.getElementById("waterUnitsUsed").style.color = 'Navy';
										document.getElementById("waterUnitsUsed").style.fontSize = "20px";
										document.getElementById("waterUnitsUsed").style.fontWeight = "bold";
										document.getElementById("waterUnitsUsed").style.textAlign = "center";
										
										//document.getElementById('newWaterBill').style.display = 'none';
										document.getElementById("newWaterBill").style.color = 'Navy';
										document.getElementById("newWaterBill").style.fontSize = "20px";
										document.getElementById("newWaterBill").style.fontWeight = "bold";
										document.getElementById("newWaterBill").style.textAlign = "center";
										
										
										//document.getElementById('totalNewBill').style.display = 'none';
										document.getElementById("totalNewBill").style.color = 'Navy';
										document.getElementById("totalNewBill").style.fontSize = "20px";
										document.getElementById("totalNewBill").style.fontWeight = "bold";
										document.getElementById("totalNewBill").style.textAlign = "center";
										
										
										//document.getElementById('totalAmount').style.display = 'none';
										document.getElementById("totalAmount").style.color = 'Navy';
										document.getElementById("totalAmount").style.fontSize = "20px";
										document.getElementById("totalAmount").style.fontWeight = "bold";
										document.getElementById("totalAmount").style.textAlign = "center";
										
																											
													//Payments														
										paidAmount = parseFloat(totalAmountPaid);
																														
													//Invoices														
										invoicedRent = parseFloat(totalRentalInvoices);										
										invoicedWater = parseFloat(totalWaterInvoices);										
										invoicedTrash = parseFloat(totalTrashInvoices);
										
										//balanceBf = paidAmount - invoicedRent - invoicedWater - invoicedTrash;
										balanceBf = paidAmount + invoicedRent + invoicedWater + invoicedTrash;	
										
										//var balanceValue = (balanceBf).toLocaleString(undefined, { minimumFractionDigits: 2 });																		
										//document.getElementById('balanceBf').value = balanceValue;
										document.getElementById('balanceBf').value = balanceBf;
										//document.getElementById('balanceBf').style.display = 'none';
										document.getElementById("balanceBf").style.color = 'Navy';
										document.getElementById("balanceBf").style.fontSize = "20px";
										document.getElementById("balanceBf").style.fontWeight = "bold";
										document.getElementById("balanceBf").style.textAlign = "center";
										
										
										document.getElementById('previousMeterReadings').readOnly = true;
										document.getElementById('waterUnitsUsed').readOnly = true;
										document.getElementById('waterRates').readOnly = true;
										document.getElementById('newWaterBill').readOnly = true;
										document.getElementById('newRentAmount').readOnly = true;
										document.getElementById('newTrashCharges').readOnly = true;
										document.getElementById('totalNewBill').readOnly = true;
										document.getElementById('balanceBf').readOnly = true;
										document.getElementById('totalAmount').readOnly = true;
											
										console.log("Success: ", result);
									} else {
										$("#getResultDiv").html("<strong>Error 1</strong>");
										console.log("Fail: ", result);
									}
								},
								error : function(e) {
									$("#getResultDiv").html("<strong>Error 2</strong>");
									console.log("ERROR: ", e);
								}
							});
						}
					})
			 </script>
								 			   
			 <script type="text/javascript">  			 
				$(function(){
				    $("#btnReset").click(function(){
				        $("#house_Id").select2('val', "''");
				    });
				});
			 </script> 
				   
	     </th:block>
	</body>
</html>

		