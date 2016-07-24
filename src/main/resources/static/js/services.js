var myTasksServices = angular.module('myTasksServices', ['ngResource'])

.constant('contextPath', document.getElementsByTagName('base')[0].getAttribute('href'))

.factory('projectService', ['$resource', 'contextPath', function($resource, contextPath) {
	return $resource(contextPath + 'api/projects/:id', {}, {
		list: {method: 'GET', isArray: true, transformResponse: [angular.fromJson, function(data) { return data.projects }]},
		getReadMe:  {method: 'GET', url: contextPath + 'api/projects/:id/readme'},
		saveReadMe: {method: 'POST', url: contextPath + 'api/projects/:id/readme'}
	});
}])

.factory('taskService', ['$resource', 'contextPath', function($resource, contextPath) {
	return $resource(contextPath + 'api/tasks/:id', {}, {
		list: {method: 'GET', isArray: true, transformResponse: [angular.fromJson, function(data) { return data.tasks }]},
		query: {method: 'GET', isArray: true, transformResponse: [angular.fromJson, function(data) { return data.tasks }]},
		getReadMe:  {method: 'GET', url: contextPath + 'api/tasks/:id/readme'}
	});
}])

.factory('tagService', ['$resource', 'contextPath', function($resource, contextPath) {
	return $resource(contextPath + 'api/tags', {}, {
		get: {method: 'GET', isArray: true, transformResponse: [angular.fromJson, function(data) { return data.tags }]}
	});
}])

.factory('userService', ['$resource', 'contextPath', function($resource, contextPath) {
	return $resource(contextPath + 'api/admin/users/:id', {}, {
		list: {method: 'GET', isArray: true, transformResponse: [angular.fromJson, function(data) { return data.users }]},
		getCurrent: {method: 'GET', params: {id: 'current'}}
	});
}]);