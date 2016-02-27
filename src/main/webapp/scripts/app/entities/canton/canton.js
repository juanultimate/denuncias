'use strict';

angular.module('denunciasApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('canton', {
                parent: 'entity',
                url: '/cantons',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Cantons'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/canton/cantons.html',
                        controller: 'CantonController'
                    }
                },
                resolve: {
                }
            })
            .state('canton.detail', {
                parent: 'entity',
                url: '/canton/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Canton'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/canton/canton-detail.html',
                        controller: 'CantonDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'Canton', function($stateParams, Canton) {
                        return Canton.get({id : $stateParams.id});
                    }]
                }
            })
            .state('canton.new', {
                parent: 'canton',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/canton/canton-dialog.html',
                        controller: 'CantonDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    codigo: null,
                                    nombre: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('canton', null, { reload: true });
                    }, function() {
                        $state.go('canton');
                    })
                }]
            })
            .state('canton.edit', {
                parent: 'canton',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/canton/canton-dialog.html',
                        controller: 'CantonDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Canton', function(Canton) {
                                return Canton.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('canton', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('canton.delete', {
                parent: 'canton',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/canton/canton-delete-dialog.html',
                        controller: 'CantonDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['Canton', function(Canton) {
                                return Canton.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('canton', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
