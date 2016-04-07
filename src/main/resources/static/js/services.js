var myTasksServices = angular.module('myTasksServices', ['ngResource'])

.factory('projectService', ['$resource', function($resource) {
	return $resource('/api/projects/:id', {}, {
		list: {method: 'GET', isArray: true, transformResponse: [angular.fromJson, function(data) { return data.projects }]},
		getReadMe:  {method: 'GET', url: '/api/projects/:id/readme'},
		saveReadMe: {method: 'POST', url: '/api/projects/:id/readme'}
	});
}])

.factory('taskService', ['$resource', function($resource) {
	return $resource('/api/tasks/:id', {}, {
		list: {method: 'GET', isArray: true, transformResponse: [angular.fromJson, function(data) { return data.tasks }]},
		query: {method: 'GET', isArray: true, transformResponse: [angular.fromJson, function(data) { return data.tasks }]},
		getReadMe:  {method: 'GET', url: '/api/tasks/:id/readme'}
	});
}])

.factory('userService', ['$resource', function($resource) {
	return $resource('/api/admin/users/:id', {}, {
		list: {method: 'GET', isArray: true, transformResponse: [angular.fromJson, function(data) { return data.users }]},
		getCurrent: {method: 'GET', params: {id: 'current'}}
	});
}]);