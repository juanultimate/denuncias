'use strict';

angular.module('denunciasApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('persona', {
                parent: 'entity',
                url: '/personas',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Personas'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/persona/personas.html',
                        controller: 'PersonaController'
                    }
                },
                resolve: {
                }
            })
            .state('persona.detail', {
                parent: 'entity',
                url: '/persona/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Persona'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/persona/persona-detail.html',
                        controller: 'PersonaDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'Persona', function($stateParams, Persona) {
                        return Persona.get({id : $stateParams.id});
                    }]
                }
            })
            .state('persona.new', {
                parent: 'persona',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/persona/persona-dialog.html',
                        controller: 'PersonaDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    nombre: null,
                                    edad: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('persona', null, { reload: true });
                    }, function() {
                        $state.go('persona');
                    })
                }]
            })
            .state('persona.edit', {
                parent: 'persona',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/persona/persona-dialog.html',
                        controller: 'PersonaDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Persona', function(Persona) {
                                return Persona.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('persona', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('persona.delete', {
                parent: 'persona',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/persona/persona-delete-dialog.html',
                        controller: 'PersonaDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['Persona', function(Persona) {
                                return Persona.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('persona', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
