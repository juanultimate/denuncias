 'use strict';

angular.module('denunciasApp')
    .factory('notificationInterceptor', function ($q, AlertService) {
        return {
            response: function(response) {
                var alertKey = response.headers('X-denunciasApp-alert');
                if (angular.isString(alertKey)) {
                    AlertService.success(alertKey, { param : response.headers('X-denunciasApp-params')});
                }
                return response;
            }
        };
    });
