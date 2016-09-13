'use strict';

angular.module('denunciasApp')
    .controller('TipoSancionController', function ($scope, $state, Reporte, ParseLinks) {
        $scope.myDataSource = {
            chart: {
                caption: "",
                subCaption: "",
                numberPrefix: "",
                theme: "zune"
            },
            data:[]
        };
        $scope.loadAll = function(tipoReporte) {
            Reporte.query({tipo : tipoReporte}, function(result, headers) {
                $scope.myDataSource.data = result;
            });
            switch(tipoReporte) {
                case 'anio':{
                    $scope.myDataSource.chart.caption=""
                    $scope.myDataSource.chart.subCaption="Alertas por año"
                    break;
                }

                case 'dia':{
                    $scope.myDataSource.chart.caption=""
                    $scope.myDataSource.chart.subCaption="Alertas por día"
                    break;
                }
                case 'mes':{
                    $scope.myDataSource.chart.caption=""
                    $scope.myDataSource.chart.subCaption="Alertas por Mes"
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
