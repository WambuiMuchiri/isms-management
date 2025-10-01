<html>
	<body>
		<th: block layout: fragment="script">

			<script>
				$(document).ready(function() {
					const today = new Date().toISOString().split('T')[0];
				$("#assignmentDate").val(today);
				});
			</script>

			<script type="text/javascript">
				$(document).ready(function() {
					// Initialize Select2 for better dropdown experience
					$('.select2').select2({
						theme: 'bootstrap4'
					});

				// DataTable initialization
				$('#recentAssignmentsTable').DataTable({
					"responsive": true,
				"lengthChange": false,
				"autoWidth": false,
				"buttons": ["copy", "csv", "excel", "pdf", "print", "colvis"]
            }).buttons().container().appendTo('#recentAssignmentsTable_wrapper .col-md-6:eq(0)');

				// Update selection counters
				function updateTotalCount() {
                const personnelCount = $('input.personnel-checkbox:checked').length;
				const equipmentCount = $('input.equipment-checkbox:checked').length;

				$('#selectedPersonnelCount').text(personnelCount + ' personnel selected');
				$('#selectedEquipmentCount').text(equipmentCount + ' equipment selected');
				$('#totalSelectedCount').text('Total: ' + personnelCount + ' personnel, ' + equipmentCount + ' equipment selected');

				// Disable submit button if no selections
				if (personnelCount === 0 || equipmentCount === 0) {
					$('button[type="submit"]').prop('disabled', true);
                } else {
					$('button[type="submit"]').prop('disabled', false);
                }
            }

				// Initial count
				updateTotalCount();

				// Update counts when checkboxes change
				$('.personnel-checkbox, .equipment-checkbox').on('change', updateTotalCount);

				// Personnel select/deselect buttons
				$('#selectAllPersonnel').click(function() {
					$('.personnel-checkbox').prop('checked', true);
				updateTotalCount();
            });

				$('#deselectAllPersonnel').click(function() {
					$('.personnel-checkbox').prop('checked', false);
				updateTotalCount();
            });

				// Equipment select/deselect buttons
				$('#selectAllEquipment').click(function() {
					$('.equipment-checkbox').prop('checked', true);
				updateTotalCount();
            });

				$('#deselectAllEquipment').click(function() {
					$('.equipment-checkbox').prop('checked', false);
				updateTotalCount();
            });

				// Personnel search functionality
				$('#personnelSearch').on('keyup', function() {
                const value = $(this).val().toLowerCase();
				$('.personnel-item').filter(function() {
					$(this).toggle($(this).text().toLowerCase().indexOf(value) > -1);
                });
            });

				// Equipment search functionality
				$('#equipmentSearch').on('keyup', function() {
                const value = $(this).val().toLowerCase();
				$('.equipment-item').filter(function() {
					$(this).toggle($(this).text().toLowerCase().indexOf(value) > -1);
                });
            });

				// Form validation
				$('form').on('submit', function(e) {
                const installationId = $('#installationId').val();
				const assignmentDate = $('#assignmentDate').val();
                const equipmentSelected = $('input.equipment-checkbox:checked').length > 0;
                const personnelSelected = $('input.personnel-checkbox:checked').length > 0;

				if (!installationId) {
					alert('Please select an installation');
				e.preventDefault();
				return false;
                }

				if (!assignmentDate) {
					alert('Please select an assignment date');
				e.preventDefault();
				return false;
                }

				if (!equipmentSelected) {
					alert('Please select at least one equipment to assign');
				e.preventDefault();
				return false;
                }

				if (!personnelSelected) {
					alert('Please select at least one personnel to assign');
				e.preventDefault();
				return false;
                }

				return true;
            });
        });
			</script>
		</th: block>
	</body>
</html>

