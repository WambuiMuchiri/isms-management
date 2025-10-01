<html>
	<body>
		  <th:block layout:fragment="script">

			<script>							 
				 $(function () {
					  $("select").select2();
					});									
			 </script>
			
			 <script>
				$(document).ready(function() {
					const today = new Date().toISOString().split('T')[0];
					$("#paymentDate").val(today);
				});
			</script>
			
			 <script>
			 	$(document).ready(function() {												
						// GET REQUEST
						$("#client_Id").change(function(event) {
						
							event.preventDefault();
							ajaxGet();
						});
			
						// DO GET
						function ajaxGet() {
						
						 let cid = $('#client_Id').val();
						 let data = {                        
									 id: cid
								    };
								    
							$.ajax({
								type : "GET",
								url : "/getJobItemById/",
								data: data,
								success : function(result) {
									if (result.status == "success") {
										$('#getResultDiv ul').empty();
										
										$('#amountPaid').val(0.0);
										$('#balanceCf_Id').val(0.0);				
										document.getElementById('balanceCf_Id').style.color = 'black';
										document.getElementById('balanceCf_Id').style.backgroundColor = 'white';																								
												
										
										//document.getElementById('amountPaid').style.display = 'none';
										document.getElementById("amountPaid").style.color = 'Navy';
										document.getElementById("amountPaid").style.fontSize = "20px";
										document.getElementById("amountPaid").style.fontWeight = "bold";
										
										//document.getElementById('balanceBf_Id').style.display = 'none';
										document.getElementById("balanceBf_Id").style.color = 'Navy';
										document.getElementById("balanceBf_Id").style.fontSize = "20px";
										document.getElementById("balanceBf_Id").style.fontWeight = "bold";
										
										//document.getElementById('balanceCf_Id').style.display = 'none';
										document.getElementById("balanceCf_Id").style.color = 'Navy';
										document.getElementById("balanceCf_Id").style.fontSize = "20px";
										document.getElementById("balanceCf_Id").style.fontWeight = "bold";
										
												
											var balanceBf = result.data.balanceBf;														
											var client = ""+result.data.clientName;											
										
											document.getElementById('paidBy').value=client;
											
											var balanceBroughtForward = (balanceBf).toLocaleString(undefined, { minimumFractionDigits: 2 });																		
											//document.getElementById('balanceBf_Id').value = balanceBroughtForward;													
											document.getElementById('balanceBf_Id').value= balanceBf;
					
								
												if(balanceBf<0)
												{
													document.getElementById('balanceBf_Id').style.color = 'white';
													document.getElementById('balanceBf_Id').style.backgroundColor = 'red';								
												}
												else if(balanceBf>0){
													document.getElementById('balanceBf_Id').style.color = 'white';
													document.getElementById('balanceBf_Id').style.backgroundColor = 'green';
												}	
												else{
													document.getElementById('balanceBf_Id').style.color = 'black';
													document.getElementById('balanceBf_Id').style.backgroundColor = 'white';
												}	
					
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
				        $("#client_Id").select2('val', 0);
				        $("#item_Id").select2('val', 0);					        				
						document.getElementById('balanceCf_Id').style.color = 'black';
						document.getElementById('balanceCf_Id').style.backgroundColor = 'white';
						$('#paidBy').val("enter paid by");
				    });
				});
			 </script> 
			 
			 <script>										
				$(".input").on('input', function(){
				 		//alert("Hi Sankale"); 
					if(document.getElementById('amountPaid').value==null)
					{
						var paid = document.getElementById('amountPaid').value = 0;
						amountPaid = parseFloat(paid);
					}
					else{
						var paid = document.getElementById('amountPaid').value || 0;
						amountPaid = parseFloat(paid);
					}		
				
					
					var bBf = document.getElementById('balanceBf_Id').value || 0;
					balanceBf = parseFloat(bBf);
					
					var balanceCf = balanceBf + amountPaid;	
					var balanceCarriedForward = (balanceCf).toLocaleString(undefined, { minimumFractionDigits: 2 });																		
					//document.getElementById('balanceCf_Id').value = balanceCarriedForward;	
					document.getElementById('balanceCf_Id').value=balanceCf;
					
					if(balanceCf<0)
					{
						document.getElementById('balanceCf_Id').style.color = 'white';
						document.getElementById('balanceCf_Id').style.backgroundColor = 'red';								
					}
					else{
						document.getElementById('balanceCf_Id').style.color = 'white';
						document.getElementById('balanceCf_Id').style.backgroundColor = 'green';
					}	
						});								
			</script>
				   
	     </th:block>
	</body>
</html>

		