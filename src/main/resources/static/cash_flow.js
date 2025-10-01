<html>
	<body>
		<th:block layout:fragment="fragment_script">

			<script type="text/javascript">
				$(function() {
				// Prefill date if empty
				const today = new Date().toISOString().split('T')[0];
				if (!$('#recordDate').val())
					$('#recordDate').val(today);

				// Initialize select2 if not already
				if ($.fn.select2) {
					$('select').select2();
				}

				// Helpers
				function clearErrors() {
					$('.js-error').remove();
					$('.is-invalid').removeClass('is-invalid');
					$('.select2-selection').removeClass('is-invalid');
				}
				function showError(el, msg) {
					const $el = $(el);
					const $group = $el.closest('.form-group');
					const $target = $el.hasClass('select2-hidden-accessible') ? $el
							.next('.select2').find('.select2-selection')
							: $el;

					$target.addClass('is-invalid');
					if ($group.find('.js-error').length === 0) {
						$group
								.append('<div class="js-error text-danger small mt-1"></div>');
					}
					$group.find('.js-error').text(msg);
				}
				function getVal(sel) {
					const v = $(sel).val();
					return v == null ? '' : (Array.isArray(v) ? v.join('')
							: ('' + v).trim());
				}

				// Validate on submit
				$('.js-cash').on(
								'submit',
								function(ev) {
									clearErrors();
									let ok = true;

									const clientId = getVal('#client_Id');
									const staffId = getVal('#staff_Id');
									const dateVal = ($('#recordDate').val() || '')
											.trim();
									const amountStr = ($('#amount').val() || '')
											.replace(/,/g, '').trim();
									const amount = parseFloat(amountStr);

									// Client/Staff rule
									if (!clientId && !staffId) {
										showError('#client_Id',
												'Select a client or a staff member.');
										showError('#staff_Id',
												'Select a client or a staff member.');
										ok = false;
									}

									// Date required
									if (!dateVal) {
										showError('#recordDate',
												'Date is required.');
										ok = false;
									}

									// Amount > 0
									if (isNaN(amount) || amount <= 0) {
										showError('#amount',
												'Enter a valid amount greater than 0.');
										ok = false;
									}

									if (!ok) {
										ev.preventDefault();
										return false;
									}

									// If both chosen, confirm
									if (clientId && staffId) {
										const clientText = $(
												'#client_Id option:selected')
												.text().trim();
										const staffText = $(
												'#staff_Id option:selected')
												.text().trim();
										const confirmed = window
												.confirm('Confirm both were involved:\n\n'
														+ 'Client: '
														+ clientText
														+ '\n'
														+ 'Staff:  '
														+ staffText
														+ '\n\nContinue?');
										if (!confirmed) {
											ev.preventDefault();
											return false;
										}
									}
									// allow submit
								});

				// Clear field error on change/typing
				$('#client_Id, #staff_Id, #amount, #recordDate').on(
						'change keyup',
						function() {
							$(this).closest('.form-group').find('.js-error')
									.remove();
							$(this).removeClass('is-invalid');
							if ($(this).hasClass('select2-hidden-accessible')) {
								$(this).next('.select2').find(
										'.select2-selection').removeClass(
										'is-invalid');
							}
						});

				// Optional: reset hook if you add a reset button with id="btnReset"
				$('#btnReset').on('click', function() {
					if ($.fn.select2) {
						$('#client_Id').val('').trigger('change');
						$('#staff_Id').val('').trigger('change');
					} else {
						$('#client_Id').val('');
						$('#staff_Id').val('');
					}
					$('#amount').val('');
					$('#remarks').val('');
					$('#recordDate').val(today);
					clearErrors();
				});
			});
			</script>
			</th:block>
	</body>
</html>