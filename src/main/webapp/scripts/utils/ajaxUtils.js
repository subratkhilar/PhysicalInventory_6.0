var ajaxCall = function(data, url, callback, target, success, errorFunc) {
	$.ajax({
		beforeSend : function(request, settings) {
			if (callback.beforeSend != null && data !== undefined)
				if (!callback.beforeSend(request, settings)) {
					$("#wait").css("display", "none");
					return false;
				}
		},
		url : url,
		type : 'POST',
		data : data,
		error : function(request, status, error) {
			callback.error(request, status, error, target, errorFunc);
		},
		success : function(data, status, request) {
			try {
				callback.success(data, status, request, target, success);
			} catch (Exception) {
				return;
			}
			

		}

	});
};

function clearForm()
{
    $(':input').not(':button, :submit, :reset, :hidden, :checkbox, :radio').val('');
  //  $(':checkbox, :radio').prop('checked', false);
}

