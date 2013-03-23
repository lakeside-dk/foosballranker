'use strict';

/* http://docs.angularjs.org/guide/dev_guide.e2e-testing */

describe('foosball ranker', function () {

    var randomNum = Math.floor(Math.random() * 1000);

    beforeEach(function () {
        browser().navigateTo('../../index.html');
    });

    it('should automatically redirect to /login when location hash/fragment is empty and user is logged in', function () {
        expect(browser().location().url()).toBe("/opponents");
    });

    describe('login', function () {

        beforeEach(function () {
            browser().navigateTo('#/login');
        });

//        afterEach(function () {
//        });

        it('should render login when user navigates to /login', function () {
            expect(element('[ng-view] p:first').text()).
                toMatch(/Register your foosball matches/);
        });

        it('should navigate to opponents when valid account is created', function () {
            input('newUserId').enter('simon'+randomNum);
            input('name').enter('Simon Vogensen');
            input('newPassword').enter('simon');
            element('#id_createaccount').click();
//            pause();
            expect(browser().location().url()).toBe('/opponents');
            element('#id_logout').click();
        });

        it('should navigate to opponents when valid user fills input and press login button', function () {
            input('userId').enter('simon'+randomNum);
            input('password').enter('simon');
//            pause();
            element('#id_login').click();
//            pause();
            expect(browser().location().url()).toBe('/opponents');
            element('#id_logout').click();
        });

        it('should show errormessage when used username is entered for new account', function () {
            input('newUserId').enter('simon'+randomNum);
            input('name').enter('Simon Vogensen');
            input('newPassword').enter('simon');
//            pause();
            element('#id_createaccount').click();
//            pause();
            expect(browser().location().url()).toBe('/login');
        });

        it('should navigate to opponents when valid account is created when logged in', function () {
            input('newUserId').enter('mette'+randomNum);
            input('name').enter('Mette Vogensen');
            input('newPassword').enter('mette');
            element('#id_createaccount').click();
//            pause();
            expect(browser().location().url()).toBe('/opponents');
            element('#id_logout').click();
        });
    });

    describe('opponents', function () {

        beforeEach(function () {
            browser().navigateTo('#/login');
            input('userId').enter('simon'+randomNum);
            input('password').enter('simon');
            element('#id_login').click();
        });

        afterEach(function () {
            element('#id_logout').click();
        });

        it('should render /opponents when user navigates to /opponents', function () {
//            pause();
            expect(element('[ng-view] legend:first').text()).
                toMatch('Ranking');
        });

        it('should render /tournaments when button Tournament is clicked', function () {
            element('.tournamentbuttonid').click();
            expect(browser().location().url()).toBe('/tournaments');
        });

        it('should render /opponents/rankingchart when button Chart is clicked', function () {
            element('.chartbuttonid').click();
            expect(browser().location().url()).toBe('/opponents/rankingchart');
        });

        it('should show linked opponent when username is entered and Link is clicked', function () {
            expect(element('[ng-view] #table tr').count()).toEqual(2);
            input('userId').enter('mette'+randomNum);
            element('#id_linkaccount').click();
//            pause();
            expect(element('[ng-view] #table tr').count()).toEqual(3);
        });
    });

    describe('chart', function () {

        beforeEach(function () {
            browser().navigateTo('#/login');
            input('userId').enter('simon'+randomNum);
            input('password').enter('simon');
            element('#id_login').click();
            element('.chartbuttonid').click();
//            pause();
        });

        it('should render /tournaments when button Tournaments is clicked', function () {
            element('.tournamentbuttonid').click();
            expect(browser().location().url()).toBe('/tournaments');
        });

        it('should render /opponents when button Opponents is clicked', function () {
            element('.opponentbuttonid').click();
            expect(browser().location().url()).toBe('/opponents');
        });
    });

    describe('tournaments', function () {

        beforeEach(function () {
            browser().navigateTo('#/login');
            input('userId').enter('simon'+randomNum);
            input('password').enter('simon');
            element('#id_login').click();
            element('.tournamentbuttonid').click();
        });

        it('should render /tournaments when user navigates to /tournaments', function () {
//            pause();
            expect(element('[ng-view] legend:first').text()).
                toMatch('Tournaments');
        });

        it('should render /opponents when button Opponents is clicked', function () {
            element('.opponentbuttonid').click();
            expect(browser().location().url()).toBe('/opponents');
        });

        it('should show new tournaments when name and type is entered and Add is clicked', function () {
            expect(element('[ng-view] #table tr').count()).toEqual(1);
            input('name').enter('rankingtest'+randomNum);
            select('type').option('ranking');
            element('#id_createtournament').click();
//            pause();
            expect(element('[ng-view] #table tr').count()).toEqual(2);

            input('name').enter('perftest'+randomNum);
            select('type').option('performance');
            element('#id_createtournament').click();
//            pause();
            expect(element('[ng-view] #table tr').count()).toEqual(3);
        });
    });

    describe('ranking tournament', function () {

        beforeEach(function () {
            browser().navigateTo('#/login');
            input('userId').enter('simon'+randomNum);
            input('password').enter('simon');
            element('#id_login').click();
            element('.tournamentbuttonid').click();
            element('[ng-view] #table tr:nth-child(1) td:nth-child(1) button').click();
        });

        it('should render /tournament when user navigates to /tournament', function () {
//            pause();
            expect(element('[ng-view] legend:first').text()).
                toMatch('Ranking');
        });
    });
});
