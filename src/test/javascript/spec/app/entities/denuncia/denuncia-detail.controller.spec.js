'use strict';

describe('Controller Tests', function() {

    describe('Denuncia Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockDenuncia;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockDenuncia = jasmine.createSpy('MockDenuncia');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Denuncia': MockDenuncia
            };
            createController = function() {
                $injector.get('$controller')("DenunciaDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'denunciasApp:denunciaUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
