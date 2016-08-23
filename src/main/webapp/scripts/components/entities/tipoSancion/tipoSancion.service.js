'use strict';

angular.module('denunciasApp')
    .factory('TipoSancion', function ($resource, DateUtils) {
        return $resource('api/tipoSancions/:id', {}, {
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
