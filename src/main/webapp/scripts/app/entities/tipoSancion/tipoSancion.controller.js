'use strict';

angular.module('denunciasApp')
    .controller('TipoSancionController', function ($scope, $state, Reporte, ParseLinks) {
        $scope.tipoReporte='dia';
        $scope.tipoGrafico='column3d';
        $scope.myDataSource = {
            chart: {
                caption: "",
                subCaption: "",
                numberPrefix: "",
                theme: ""
            },
            data:[]
        };
        $scope.loadAll = function(tipoReporte) {
            Reporte.query({tipo : tipoReporte}, function(result, headers) {
                $scope.myDataSource.data = result;
            });
            switch(tipoReporte) {
                case 'dia':{
                    $scope.myDataSource.chart.caption=""
                    $scope.myDataSource.chart.subCaption="Alertas por día"
                    break;
                }
                case 'canton':{
                    $scope.myDataSource.chart.caption=""
                    $scope.myDataSource.chart.subCaption="Alertas por cantón"
                    break;
                }
                case 'pago':{
                    $scope.myDataSource.chart.caption=""
                    $scope.myDataSource.chart.subCaption="Alertas por estado de pago"
                    break;
                }
                default:{
                    break;
                }
            }
        };

        $scope.loadAll($scope.tipoReporte);
        $scope.updateReport = function(tipoReporte){
            console.log(tipoReporte);
            $scope.loadAll(tipoReporte);
        };
    });
