var width, height, cols, url, data, type;

function findItemMenu(boxobject, menuname) {
	// alert(menuname);
	if (boxobject != undefined) {
		return boxobject.menu('findItem', menuname); // find the menu item
	}
}

function disableItemMenu(boxobject, item) {
	if (boxobject != undefined) {
		boxobject.menu('disableItem', item.target);
		$('#search-menu div').each(function() {
			if ($(this).text() == 'SKU') {
				$(this).trigger('click');
			}
			;
		});
	}
}

function enableItemMenu(boxobject, item) {
	if (boxobject != undefined) {
		boxobject.menu('enableItem', item.target);
	}
}

function findAndRemove(array, value) {
	$.each(array, function(index, result) {
		if (index == value) {
			// Remove from array
			array.splice(index, 1);
		}
	});
}

function findAndAdd(array, value) {
	array.splice(0, 0, value);
}

function getRowValues(target) {
	var valueArray = [];
	var tr = $(target).closest('tr.datagrid-row');
	var count = 0;
	tr.find("div.datagrid-editable").each(function() {
		// var _6a1 = $(this).parent().attr("field");
		var ed = $.data(this, "datagrid.editor");
		// alert(typeof($(ed.target)));
		var t = $(ed.target);
		// var _6a2 = t.data("textbox") ? t.textbox("textbox") : t;
		// alert('texbox value'+_6a2);
		// _6a2.triggerHandler("blur");
		var _6a3 = ed.actions.getValue(ed.target);
		// alert('texbox value2'+_6a3);
		valueArray[count] = _6a3;
		count++;

	});
	return valueArray;
}

function updateActions(index) {
	$('#tt').datagrid('updateRow', {
		index : index,
		row : {}
	});
}

function setType(typevar) {
	type = typevar;
}

function setWidth(widthvar) {
	width = widthvar;
}

function setHeight(heightvar) {
	height = heightvar;
}

function setUrl(urlvar) {
	url = urlvar;
}
function toggleGrid() {
	// $('#qtyeditdiv').slideUp();
	if ($('#collapse3').is(':hidden')) {
		$('#collapse3anchor').trigger("click");
	}
}

function getRowIndex(target) {
	var tr = $(target).closest('tr.datagrid-row');
	return parseInt(tr.attr('datagrid-row-index'));
}

function hideUpdateDiv() {
	$('#qtyeditdiv').slideUp(1000);
}

function showUpdateDiv() {
	// $('#qtyeditdiv').slideDown(1000);
	$('#qtyeditdiv').hide();
}

function showGridDiv() {
	$('#skueditdiv').slideDown(1000);
}

function hideGridDiv() {
	$('#skueditdiv').slideUp(1000);
}

function toggleUpdateDivBody() {
	if ($('#collapse2').is(':hidden')) {
		$('#collapse2anchor').trigger("click");

	}

}
function toggleGrid() {
	// $('#qtyeditdiv').slideUp();
	if ($('#collapse3').is(':hidden')) {
		$('#collapse3anchor').trigger("click");
	}
}

function gnrcCodeSelectToggle() {
	if ($('select[name=gnrc]').val() == 'SELECT') {
		/* $('#qtyeditdiv').slideUp(1000); */
		$('#qtyeditdiv').hide();
		$('#skueditdiv').slideUp(1000);
	} else {
		/* hideUpdateDiv(); */
		// hideGridDiv();
		/* $('#qtyeditdiv').slideDown(1000); */
		$('#qtyeditdiv').hide();
		$('#skueditdiv').slideDown(1000);
	}
}

function toggleCategoryDiv() {
	if ($('#collapse1').is(':hidden')) {
		$('#collapse1anchor').trigger("click");

	}
}
