'use strict';

/* http://docs.angularjs.org/guide/dev_guide.e2e-testing */

describe('foosball ranker', function () {

    var randomNum = Math.floor(Math.random() * 100000);

    var NavOpponents = '[ng-view] navigation-radio button:nth-child(2)';
    var NavOpponentsHistory = '[ng-view] subnavigation-radio button:nth-child(2)';
    var NavTournaments = '[ng-view] navigation-radio button:nth-child(1)';
    var NavTournamentHistory = '[ng-view] subnavigation-radio button:nth-child(3)';
    var NavTournamentNewMatch = '[ng-view] subnavigation-radio button:nth-child(1)';
    var NavTournamentMatches = '[ng-view] subnavigation-radio button:nth-child(4)';

    function login(randomNum) {
        browser().navigateTo('#/login');
        input('userId').enter('test1_' + randomNum);
        input('password').enter(randomNum);
        element('#id_login').click();
    }

    function createPlayer(id) {
        input('newUserId').enter('test'+id+'_' + randomNum);
        input('name').enter('Test Player '+id);
        input('newPassword').enter(randomNum);
        element('#id_createaccount').click();
    }

    beforeEach(function () {
        browser().navigateTo('../../index.html');
    });

    it('should automatically redirect to /login when location hash/fragment is empty and user is logged out', function () {
        expect(browser().location().url()).toBe("/login");
    });

    it('should automatically redirect to /opponents when location hash/fragment is empty and user is logged in', function () {
        login(randomNum);
        browser().navigateTo('../../index.html');
        expect(browser().location().url()).toBe("/opponents");
        element('#id_logout').click();
    });

    describe('login', function () {

        beforeEach(function () {
            browser().navigateTo('#/login');
        });

        it('should render login when user navigates to /login', function () {
            expect(element('[ng-view] p:first').text()).
                toMatch(/Register your foosball matches/);
        });

        it('should navigate to opponents when valid account is created', function () {
            createPlayer('1');
            expect(browser().location().url()).toBe('/opponents');
            element('#id_logout').click();
        });

        it('should navigate to opponents when valid user fills input and press login button', function () {
            login(randomNum);
            expect(browser().location().url()).toBe('/opponents');
            element('#id_logout').click();
        });

        it('should show errormessage when used username is entered for new account', function () {
            createPlayer('1');
            expect(browser().location().url()).toBe('/login');
        });
    });

    describe('opponents', function () {

        beforeEach(function () {
            login(randomNum);
        });

        afterEach(function () {
            element('#id_logout').click();
        });

        it('should render /opponents when user navigates to /opponents', function () {
            expect(element('[ng-view] legend:first').text()).
                toMatch('Ranking');
        });

        it('should render /tournaments when button Tournament is clicked', function () {
            element(NavTournaments).click();
            expect(browser().location().url()).toBe('/tournaments');
        });

        it('should render /opponents/rankingchart when button History is clicked', function () {
            element(NavOpponentsHistory).click();
            expect(browser().location().url()).toBe('/opponents/rankingchart');
        });

        it('should show linked opponent when username is entered and Link is clicked', function () {
            element('#id_logout').click();
            createPlayer('2');
            element('#id_logout').click();
            createPlayer('3');
            element('#id_logout').click();
            createPlayer('4');
            element('#id_logout').click();
            login(randomNum);

            expect(element('[ng-view] #table tr').count()).toEqual(2);
            input('userId').enter('test2_'+randomNum);
            element('#id_linkaccount').click();
            expect(element('[ng-view] #table tr').count()).toEqual(3);
            input('userId').enter('test3_'+randomNum);
            element('#id_linkaccount').click();
            expect(element('[ng-view] #table tr').count()).toEqual(4);
            input('userId').enter('test4_'+randomNum);
            element('#id_linkaccount').click();
            expect(element('[ng-view] #table tr').count()).toEqual(5);
        });
    });

    describe('opponents/history', function () {

        beforeEach(function () {
            login(randomNum);
            element(NavOpponentsHistory).click();
        });

        afterEach(function () {
            element('#id_logout').click();
        });

        it('should render /tournaments when button Tournaments is clicked', function () {
            element(NavTournaments).click();
            expect(browser().location().url()).toBe('/tournaments');
        });

        it('should render /opponents when button Opponents is clicked', function () {
            element(NavOpponents).click();
            expect(browser().location().url()).toBe('/opponents');
        });
    });

    describe('tournaments', function () {

        beforeEach(function () {
            login(randomNum);
            element(NavTournaments).click();
        });

        afterEach(function () {
            element('#id_logout').click();
        });

        it('should render /tournaments when user navigates to /tournaments', function () {
            expect(element('[ng-view] legend:first').text()).
                toMatch('Tournaments');
        });

        it('should render /opponents when button Opponents is clicked', function () {
            element(NavOpponents).click();
            expect(browser().location().url()).toBe('/opponents');
        });

        it('should show new tournaments when name and type is entered and Add is clicked', function () {
            expect(element('[ng-view] #table tr').count()).toEqual(1);
            input('name').enter('rankingtest'+randomNum);
            select('type').option('ranking');
            element('#id_createtournament').click();
            expect(element('[ng-view] #table tr').count()).toEqual(2);

            input('name').enter('perftest'+randomNum);
            select('type').option('performance');
            element('#id_createtournament').click();
            expect(element('[ng-view] #table tr').count()).toEqual(3);
        });
    });

    describe('tournament(ranking)', function () {

        beforeEach(function () {
            login(randomNum);
            element(NavTournaments).click();
            element('[ng-view] #table tr:nth-child(1) td:nth-child(1) button').click();
        });

        afterEach(function () {
            element('#id_logout').click();
        });

        it('should render /tournament when user navigates to /tournament', function () {
            expect(element('[ng-view] legend:first').text()).
                toMatch('Ranking');
        });
    });

    describe('tournament(ranking)/new ranking match', function () {

        beforeEach(function () {
            login(randomNum);
            element(NavTournaments).click();
            element('[ng-view] #table tr:nth-child(1) td:nth-child(1) button').click();
        });

        afterEach(function () {
            element('#id_logout').click();
        });

        it('should show 1vs1 match in matches when created', function () {
            element(NavTournamentMatches).click();
            expect(element('[ng-view] #table tr').count()).toEqual(1);
            element(NavTournamentNewMatch).click();
            element('[ng-view] #gametypes button:nth-child(1)').click();
            element('[ng-view] #players button:nth-child(2)').click();
            element('[ng-view] #players button:nth-child(3)').click();
            element('[ng-view] #results button:nth-child(3)').click();
            element('[ng-view] #results button:nth-child(18)').click();
            element('[ng-view] #buttons button:nth-child(3)').click();
            element(NavTournamentMatches).click();
            expect(element('[ng-view] #table tr').count()).toEqual(2);
        });

        it('should show 2vs2 match in matches when created', function () {
            element(NavTournamentMatches).click();
            expect(element('[ng-view] #table tr').count()).toEqual(2);
            element(NavTournamentNewMatch).click();
            element('[ng-view] #gametypes button:nth-child(2)').click();
            element('[ng-view] #players button:nth-child(2)').click();
            element('[ng-view] #players button:nth-child(3)').click();
            element('[ng-view] #players button:nth-child(4)').click();
            element('[ng-view] #players button:nth-child(5)').click();
            element('[ng-view] #results button:nth-child(3)').click();
            element('[ng-view] #results button:nth-child(18)').click();
            element('[ng-view] #buttons button:nth-child(3)').click();
            element(NavTournamentMatches).click();
            expect(element('[ng-view] #table tr').count()).toEqual(3);
        });
    });
});
