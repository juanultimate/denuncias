'use strict';

angular.module('denunciasApp')
    .factory('Register', function ($resource) {
        return $resource('api/register', {}, {
        });
    });


