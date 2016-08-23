'use strict';

angular.module('denunciasApp')
    .factory('Foto', function ($resource, DateUtils) {
        return $resource('api/fotos/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    });
