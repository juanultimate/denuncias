'use strict';

angular.module('denunciasApp').controller('PersonaDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Persona',
        function($scope, $stateParams, $uibModalInstance, entity, Persona) {

        $scope.persona = entity;
        $scope.load = function(id) {
            Persona.get({id : id}, function(result) {
                $scope.persona = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('denunciasApp:personaUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.persona.id != null) {
                Persona.update($scope.persona, onSaveSuccess, onSaveError);
            } else {
                Persona.save($scope.persona, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
