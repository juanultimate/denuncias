'use strict';

angular.module('denunciasApp')
    .controller('TipoSancionController', function ($scope, $state, TipoSancion, ParseLinks) {
        $scope.loadPage = function(page) {

        };

        $scope.myDataSource = {
            chart: {
                caption: "reporte 1",
                subCaption: "Alertas por Cantón",
                numberPrefix: "$",
                theme: "asd"
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
