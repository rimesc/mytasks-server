priorities = ['CRITICAL', 'HIGH', 'NORMAL', 'LOW'];

states = ['TO_DO', 'IN_PROGRESS', 'ON_HOLD', 'DONE'];

filters = {
	'OPEN': ['TO_DO', 'IN_PROGRESS', 'ON_HOLD'],
	'CLOSED': ['DONE']
};

labels = {
	'filters' : {
		'OPEN' : 'Open',
		'CLOSED' : 'Closed',
		'ALL' : 'All'
	},
	'states' : {
		'TO_DO' : 'To do',
		'IN_PROGRESS' : 'In progress',
		'ON_HOLD' : 'On hold',
		'DONE' : 'Done'
	},
	'priorities' : {
		'CRITICAL' : 'Critical',
		'HIGH' : 'High',
		'NORMAL' : 'Normal',
		'LOW' : 'Low'
	}
};

function contains(list, element) {
	return list.indexOf(element) >= 0;
}