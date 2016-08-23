'use strict';

describe('Controller Tests', function() {

    describe('TipoSancion Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockTipoSancion;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockTipoSancion = jasmine.createSpy('MockTipoSancion');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'TipoSancion': MockTipoSancion
            };
            createController = function() {
                $injector.get('$controller')("TipoSancionDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'denunciasApp:tipoSancionUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
