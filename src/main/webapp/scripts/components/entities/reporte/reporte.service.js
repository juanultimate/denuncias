'use strict';

angular.module('denunciasApp')
    .factory('Reporte', function ($resource, DateUtils) {
        return $resource('api/reportes/:tipo', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.fecha = DateUtils.convertDateTimeFromServer(data.fecha);
                    return data;
                }
            }
        });
    });
