'use strict';

angular.module('denunciasApp')
    .factory('Canton', function ($resource, DateUtils,$http) {
        return $resource('api/cantons/:id/:provincias/', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'update': { method:'PUT' },
            'provincias': {method: 'GET',
                params: {
                    provincias: 'provincias'
                },
                isArray: true
            }
        });
    });
