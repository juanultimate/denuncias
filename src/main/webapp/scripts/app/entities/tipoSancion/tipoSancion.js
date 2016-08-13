'use strict';

angular.module('denunciasApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('tipoSancion', {
                parent: 'entity',
                url: '/tipoSancions',
                data: {
                    authorities: ['ROLE_ADMIN'],
                    pageTitle: 'TipoSancions'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/tipoSancion/tipoSancions.html',
                        controller: 'TipoSancionController'
                    }
                },
                resolve: {
                }
            })
            .state('tipoSancion.detail', {
                parent: 'entity',
                url: '/tipoSancion/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'TipoSancion'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/tipoSancion/tipoSancion-detail.html',
                        controller: 'TipoSancionDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'TipoSancion', function($stateParams, TipoSancion) {
                        return TipoSancion.get({id : $stateParams.id});
                    }]
                }
            })
            .state('tipoSancion.new', {
                parent: 'tipoSancion',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/tipoSancion/tipoSancion-dialog.html',
                        controller: 'TipoSancionDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    nombre: null,
                                    costo: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('tipoSancion', null, { reload: true });
                    }, function() {
                        $state.go('tipoSancion');
                    })
                }]
            })
            .state('tipoSancion.edit', {
                parent: 'tipoSancion',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/tipoSancion/tipoSancion-dialog.html',
                        controller: 'TipoSancionDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['TipoSancion', function(TipoSancion) {
                                return TipoSancion.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('tipoSancion', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('tipoSancion.delete', {
                parent: 'tipoSancion',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/tipoSancion/tipoSancion-delete-dialog.html',
                        controller: 'TipoSancionDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['TipoSancion', function(TipoSancion) {
                                return TipoSancion.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('tipoSancion', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
