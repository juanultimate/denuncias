'use strict';

angular.module('denunciasApp')
    .controller('TipoSancionController', function ($scope, $state, Reporte, ParseLinks) {

        $scope.data;
        $scope.tipoSancion;

        $scope.loadAll = function() {
            Reporte.query({tipo : 'Creada'}, function(result) {
                $scope.data = result;
            });
        };

        $scope.loadAll();



        $scope.myDataSource = {
            chart: {
                caption: "reporte 1",
                subCaption: "Alertas por Cantón",
                numberPrefix: "",
                theme: ""
            },
            data:[{
                label: "Quito",
                value: "200"
            },
            {
                label: "Machachi",
                value: "50"
            },
            {
                label: "Posorja",
                value: "500"
            },
            {
                label: "Pedro Vicente Maldonado",
                value: "500"
            },
            {
                label: "Rumiñahui",
                value: "6"
            }]
        };


    });
