<html>
	<body>
		  <th:block layout:fragment="navbar_script">
		  
        				<script type="text/javascript">
						       /* 	$(document).ready(function(){
						        		$('#logoId').change(function() {
						        			showImageThumbnail(this);					        			
						        			});						        		
						        		});
						        	
						        		function showImageThumbnail(fileInput){
						        			file = fileInput.files[0];	
						        			console.log(file);
						        			reader = new FileReader();
						        			reader.onload = function(e){
						        				$('#thumbnail').attr('src', e.target.result);
						        			}
						        			reader.readAsDataURL(file);
						        	}
						        	*/
						        	$(document).ready(()=>{
								      $('#logoId').change(function(){
								        const file = this.files[0];
								        console.log(file);
								        if (file){
								          let reader = new FileReader();
								          reader.onload = function(event){
								            console.log(event.target.result);
								            $('#thumbnail').attr('src', event.target.result);
								          }
								          reader.readAsDataURL(file);
								        }
								      });
								    });
					  </script>
    </body>
</html>