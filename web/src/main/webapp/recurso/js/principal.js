function textCounter(field, countfield, maxlimit) {
    var spanElement = document.getElementById(countfield);
    if (field.value.length > maxlimit) // if too long...trim it!
        field.value = field.value.substring(0, maxlimit);
    // otherwise, update 'characters left' counter
    else
        spanElement.innerHTML = maxlimit - field.value.length;
}

var chartStyle = {
	     padding : 0,
	     border: {color: 0x96acb4, size: 0},
	     legend: {
	     	     display: "top"
	     }
};

var chartStyle2 = {
	     padding : 2,
	     border: {color: 0x96acb4, size: 2},
	     legend: {
	     	     display: "right"
	     }
};

var lineChartStyle = {         
	     padding : 0,
	     border: {color: 0x96acb4, size: 0},
	     legend: {
	     	     display: "top"
	     },
         xAxis: {             
             labelRotation:-60
         },
         font: {
           size: 8.5
         }
};


 var barStyle = {	     
	     size: 10
};

 var candidatosStyle = {
         color: 0xFFD700,
	     size: 10
};

 var lineStyle = {
         size: 10,
         color: 0xFF3333
};

 var lineStyleVerde = {
         size: 10,
         color: 0x66CC33
};