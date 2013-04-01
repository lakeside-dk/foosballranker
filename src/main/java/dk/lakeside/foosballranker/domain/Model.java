/*
 * The MIT License
 *
 * Original work sponsored and donated by Lakeside A/S (http://www.lakeside.dk)
 *
 * Copyright (c) to all contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
 * of the Software, and to permit persons to whom the Software is furnished to do
 * so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package dk.lakeside.foosballranker.domain;

import dk.lakeside.foosballranker.Pair;
import dk.lakeside.foosballranker.repository.*;
import dk.lakeside.foosballranker.repository.ofy.*;

import java.io.Serializable;
import java.util.*;

public class Model implements Serializable {

    private static final int INITIAL_RATING = 1000;

    PlayerRepository playerRepository = new PlayerOfyRepository();
    TournamentRepository tournamentRepository = new TournamentOfyRepository();
    PlayerRelationRepository playerRelationRepository = new PlayerRelationOfyRepository();
    TournamentRelationsRepository tournamentRelationsRepository = new TournamentRelationsOfyRepository();
    MatchRepository matchRepository = new MatchOfyRepository();

    public void addPlayer(Player player) {
        playerRepository.put(player);
    }

    public Player getPlayer(String id) {
        return playerRepository.get(id);
    }

    public Player getPlayer(List<Player> players, String id) {
        for (Player player : players) {
            if(player.getId().equals(id)) {
                return player;
            }
        }
        return null;
    }

    //TODO remove
    @Deprecated
    public List<Player> getPlayers() {
        List<Player> tmp = playerRepository.findAll();
        ModelHelper.sortPlayersByName(tmp);
        return tmp;
    }

    public Tournament getTurnering(Long id) {
        if(id == null) return null;
        return tournamentRepository.get(id);
    }

    public void addMatch(Match match) {
        // if turneringskamp - check if playerRepository are related to tournament
        if(match.getTurneringId() != null) {
            List<Player> players = getPlayers(match);
            Tournament tournament = getTurnering(match.getTurneringId());
            List<Player> turneringPlayers = getPlayers(tournament);
            for (Player player : players) {
                // add turneringrelation to player if needed
                if(!turneringPlayers.contains(player)) {
                    addTournamentRelation(tournament, player);
                }

                // add playerrelation between players if needed
                List<Player> competitors = getCompetitors(player);
                for (Player otherPlayer : players) {
                    if(!player.equals(otherPlayer) && !competitors.contains(otherPlayer)) {
                        addPlayerRelation(player, otherPlayer);
                    }
                }
            }
        }
        matchRepository.put(match);
    }

    public List<Player> getPlayerAndCompetitors(Player player) {
        List<Player> players = getCompetitors(player);
        players.add(player);
        ModelHelper.sortPlayersByName(players);
        return players;
    }

    public List<Pair<String,String>> getPlayerAndCompetitorsWithRating(Player player) {
        Pair<List<Player>, List<List<Integer>>> chartData = generatePlayerRatingChartData(player, true);
        List<Pair<String,String>> playersRating = new ArrayList<Pair<String, String>>();
        if(chartData.getFirst().size() > 0) {
            List<Integer> newestRatingList = chartData.getSecond().get(chartData.getSecond().size()-1);
            for (int i = 0; i < chartData.getFirst().size(); i++) {
                Player p = chartData.getFirst().get(i);
                playersRating.add(new Pair<String, String>(p.getName(), newestRatingList.get(i).toString()));
            }
        }
        return playersRating;
    }

    public List<PlayerWithRating> getRankingOfPlayersOpponents(Player player) {
        Pair<List<Player>, List<List<Integer>>> chartData = generatePlayerRatingChartData(player, false);
        List<PlayerWithRating> playersRating = new ArrayList<PlayerWithRating>();
        if(chartData.getFirst().size() > 0) {
            List<Integer> newestRatingList = chartData.getSecond().get(chartData.getSecond().size()-1);
            for (int i = 0; i < chartData.getFirst().size(); i++) {
                Player p = chartData.getFirst().get(i);
                playersRating.add(new PlayerWithRating(p, newestRatingList.get(i)));
            }
        }
        return playersRating;
    }

    public List<Player> getCompetitors(Player player) {
        Iterable<PlayerRelation> q = playerRelationRepository.findByPlayer(player.getId());
        List<Player> players = new ArrayList<Player>();
        for (PlayerRelation playerRelation: q) {
            players.add(getPlayer(playerRelation.getCompetitorId()));
        }
        // ui method - dont belong in model
        ModelHelper.sortPlayersByName(players);
        return players;
    }

    public void addPlayerRelation(Player player, Player competitor) {
        PlayerRelation playerRelation = new PlayerRelation(player.getId(), competitor.getId());
        playerRelationRepository.put(playerRelation);
    }

    public void addTurnering(Tournament tournament, Player player) {
        tournamentRepository.put(tournament);
        addTournamentRelation(tournament, player);
    }

    private void addTournamentRelation(Tournament tournament, Player player) {
        int rating = tournament.isTypePerformance()?getPlayerRating(player):1000;
        TournamentRelation tournamentRelation = new TournamentRelation(tournament.getId(), player.getId(), rating);
        tournamentRelationsRepository.put(tournamentRelation);
    }

    public List<Player> getPlayers(Tournament tournament) {
        Long tournamentId = tournament.getId();
        Iterable<TournamentRelation> q = tournamentRelationsRepository.findByTournament(tournamentId);
        List<Player> players = new ArrayList<Player>();
        for (TournamentRelation playerRelation: q) {
            players.add(getPlayer(playerRelation.getPlayerId()));
        }
        ModelHelper.sortPlayersByName(players);
        return players;
    }

    public List<Player> getPlayers(List<Match> matches) {
        Set<String> playerIds = new HashSet<String>();
        for (Match match : matches) {
            playerIds.addAll(match.getPlayerIds());
        }
        return playerRepository.get(playerIds);
    }

    public List<Player> getPlayers(Match match) {
        return playerRepository.get(match.getPlayerIds());
    }

    public List<Match> getKampe(Tournament tournament) {
        Long tournamentId = tournament.getId();
        Iterable<Match> q = matchRepository.findByTournament(tournamentId);
        List<Match> kampe = new ArrayList<Match>();
        for (Match match : q) {
            kampe.add(match);
        }
        return kampe;
    }

    //TODO optimize
    public List<Match> getKampe(List<Player> players) {
        Set<Match> kampe = new HashSet<Match>();
        for (Player player : players) {
            kampe.addAll(getKampe(player));
        }
        return new ArrayList<Match>(kampe);
    }

    public List<Match> getKampe(Player player) {
        Iterable<Match> q = matchRepository.findByPlayer(player.getId());
        List<Match> kampe = new ArrayList<Match>();
        for (Match match : q) {
            kampe.add(match);
        }
        return kampe;
    }

    public void deleteKamp(long id) {
        matchRepository.delete(id);
    }

    //TODO optimize - add active to query
    public List<Tournament> getAktiveTurneringer(Player player) {
        List<Tournament> turneringer = getTurneringer(player);
        Iterator<Tournament> it = turneringer.iterator();
        while (it.hasNext()) {
            Tournament tournament = it.next();
            // only active
            if(tournament.getEndDate() != null) {
                it.remove();
            }
        }
        ModelHelper.sortTurneringerNewestFirst(turneringer);
        return turneringer;
    }

    public List<Tournament> getTurneringer(Player player) {
        String playerId = player.getId();
        Iterable<TournamentRelation> q = tournamentRelationsRepository.findByPlayer(playerId);
        List<Tournament> turneringer = new ArrayList<Tournament>();
        for (TournamentRelation playerRelation: q) {
            turneringer.add(getTurnering(playerRelation.getTurneringId()));
        }
        ModelHelper.sortTurneringerNewestFirst(turneringer);
        return turneringer;
    }

    public int getPlayerRating(Player player) {
        return getPlayerRating(player, null);
    }

    public int getPlayerRating(Player player, Date before) {
        List<Match> kampe = getKampe(player);
        if(kampe.isEmpty()) {
            return player.getStartRating();
        }

        ModelHelper.sortKampeOldestFirst(kampe);
//        List<Player> playerRepository = getPlayerAndCompetitors(player);
        List<Player> players = getPlayers(kampe);

        // clear ratings
        for (Player p : players) {
            p.resetAntalKampe();
            p.resetRating();
        }
        // update ratings
        for (Match match : kampe) {
            if(before == null || match.getTime().before(before)) {
                updatePlayers(players, match);
                updateRating(players, match, false);
            }
        }
        return players.get(players.indexOf(player)).getRating();
    }

    public Collection<PlayerRatingSnapshot> getTurneringRanking(Tournament tournament) {
        List<Match> kampe = getKampe(tournament);
        ModelHelper.sortKampeOldestFirst(kampe);
        List<Player> players = getPlayers(tournament);
        // clear ratings
        for (Player player : players) {
            player.resetAntalKampe();
            player.setRating(getTurneringStartRating(tournament, player));
        }
        // update ratings
        for (Match match : kampe) {
            updatePlayers(players, match);
            updateRating(players, match, false);
        }

        List<PlayerRatingSnapshot> activePlayers = new ArrayList<PlayerRatingSnapshot>( );
        for (Player player : players) {
            if (player.getAntalKampe() > 0) {
                activePlayers.add( new PlayerRatingSnapshot(player.getId(), player.getName(), player.getRating()));
            }
        }
        Collections.sort(activePlayers);

        return activePlayers;
    }

    /**
     * finds playerRepository rating when joining the tournament - this is startrating
     * sets playerRepository rating to 0 and runs through all turneringskampe and by each
     * updates the player rating based on change by playerRepository startrating
     * @param tournament
     */
    public Collection<PlayerRatingSnapshot> refreshTurneringPerformance(Tournament tournament) {
        List<Match> kampe = getKampe(tournament);
        ModelHelper.sortKampeOldestFirst(kampe);

        List<Player> players = getPlayers(tournament);

        // setup start ratings
        for (Player player : players) {
            player.resetAntalKampe();
            player.setRating(0);
            player.setStartRating(getTurneringStartRating(tournament, player));
        }
        // update ratings
        for (Match match : kampe) {
            updatePlayers(players, match);
            updateRating(players, match, true);
        }

        List<PlayerRatingSnapshot> activePlayers = new ArrayList<PlayerRatingSnapshot>( );
        for (Player player : players) {
            if (player.getAntalKampe() > 0) {
                activePlayers.add( new PlayerRatingSnapshot(player.getId(), player.getName(), player.getRating()) );
            }
        }
        Collections.sort(activePlayers);

        return activePlayers;
    }

    private int getTurneringStartRating(Tournament tournament, Player player) {
        Long tournamentId = tournament.getId();
        String playerId = player.getId();
        TournamentRelation tournamentRelation = tournamentRelationsRepository.findByTournamentAndPlayer(tournamentId, playerId);
        return tournamentRelation.getStartRating();

        //shouldnt happend
//        throw new RuntimeException("Cant get turneringstartrating on player not related to tournament");
    }

    private void updateRating(List<Player> players, Match match, boolean useStartRating) {
        if(match.isSingle()) {
            final Player winner = getWinner(players, match);
            final Player loser = getLoser(players, match);
            int change;
            if(useStartRating){
                change = calculateChange(winner.getStartRating(), loser.getStartRating());
            }else{
                change = calculateChange(winner.getRating(), loser.getRating());
            }
            winner.incrementRating(change);
            loser.incrementRating(-change);
        } else {
            int winnerRating = averageRating(getWinningAttacker(players, match), getWinningDefender(players, match), useStartRating);
            int loserRating = averageRating(getLosingAttacker(players, match), getLosingDefender(players, match), useStartRating);
            int change = calculateChange(winnerRating, loserRating);
            getWinningAttacker(players, match).incrementRating(change);
            getWinningDefender(players, match).incrementRating(change);
            getLosingAttacker(players, match).incrementRating(-change);
            getLosingDefender(players, match).incrementRating(-change);
        }
    }

    private int averageRating(Player p1, Player p2, boolean useStartRating) {
        if(useStartRating){
            return (p1.getStartRating()+p2.getStartRating())/2;
        }else{
            return (p1.getRating()+p2.getRating())/2;
        }
    }

    private Player getWinningAttacker(List<Player> players, Match doubleMatch) {
        Player attacker1 = getPlayer(players, doubleMatch.getAttacker1Id());
        Player attacker2 = getPlayer(players, doubleMatch.getAttacker2Id());
        return doubleMatch.score1 > doubleMatch.score2 ? attacker1 : attacker2;
    }

    private Player getLosingAttacker(List<Player> players, Match doubleMatch) {
        Player attacker1 = getPlayer(players, doubleMatch.getAttacker1Id());
        Player attacker2 = getPlayer(players, doubleMatch.getAttacker2Id());
        return doubleMatch.score1 > doubleMatch.score2 ? attacker2 : attacker1;
    }

    private Player getWinningDefender(List<Player> players, Match doubleMatch) {
        Player defender1 = getPlayer(players, doubleMatch.getDefender1Id());
        Player defender2 = getPlayer(players, doubleMatch.getDefender2Id());
        return doubleMatch.score1 > doubleMatch.score2 ? defender1 : defender2;
    }

    private Player getLosingDefender(List<Player> players, Match doubleMatch) {
        Player defender1 = getPlayer(players, doubleMatch.getDefender1Id());
        Player defender2 = getPlayer(players, doubleMatch.getDefender2Id());
        return doubleMatch.score1 > doubleMatch.score2 ? defender2 : defender1;
    }

    // bordfodbold ligaen bruger følgende rating: 50/(1/(1+Math.pow(10,((r1-r2)/400))))
    protected int calculateChange(int winnerRating, int loserRating) {
        return (int) Math.round(50*(1/(1+Math.pow(10,((winnerRating-loserRating)/400.0)))));
    }

    private Player getWinner(List<Player> players, Match singleMatch) {
        Player player1 = getPlayer(players, singleMatch.getPlayer1Id());
        Player player2 = getPlayer(players, singleMatch.getPlayer2Id());
        return singleMatch.score1 > singleMatch.score2 ? player1 : player2;
    }

    private Player getLoser(List<Player> players, Match singleMatch) {
        Player player1 = getPlayer(players, singleMatch.getPlayer1Id());
        Player player2 = getPlayer(players, singleMatch.getPlayer2Id());
        return singleMatch.score1 > singleMatch.score2 ? player2 : player1;
    }

    private void updatePlayers(List<Player> players, Match match) {
        if(match.isSingle()) {
            getPlayer(players, match.getPlayer1Id()).incrementAntalKampe();
            getPlayer(players, match.getPlayer2Id()).incrementAntalKampe();
        } else {
            getPlayer(players, match.getAttacker1Id()).incrementAntalKampe();
            getPlayer(players, match.getDefender1Id()).incrementAntalKampe();
            getPlayer(players, match.getAttacker2Id()).incrementAntalKampe();
            getPlayer(players, match.getDefender2Id()).incrementAntalKampe();
        }
    }

    /**
     * @param tournament tournament
     * @return pair of playerRepository and matrix of playerRepository ratings.
     * The matrix is a list of kampe where each kamp contains all playerRepository ratings after the kamp
     */
    public Pair<List<Player>, List<List<Integer>>> generateTurneringRatingChartData(Tournament tournament) {
        List<Match> kampe = getKampe(tournament);
        ModelHelper.sortKampeOldestFirst(kampe);
        List<Player> players = getPlayers(tournament);

        // clear ratings
        for (Player player : players) {
            player.resetAntalKampe();
            player.setRating(getTurneringStartRating(tournament, player));
        }
        // add playerRepository
        Pair<List<Player>,List<List<Integer>>> chartData = new Pair<List<Player>,List<List<Integer>>>(new ArrayList<Player>(), new ArrayList<List<Integer>>());
        for (Player player : players) {
            chartData.getFirst().add(player);
        }

        // add initial ratings
        List<Integer> ratings = new ArrayList<Integer>();
        for (Player player : players) {
            ratings.add(player.getRating());
        }
        chartData.getSecond().add(ratings);

        // update ratings
        for (Match match : kampe) {
            updatePlayers(players, match);
            updateRating(players, match, false);
            ratings = new ArrayList<Integer>();
            for (Player player : players) {
                ratings.add(player.getRating());
            }
            chartData.getSecond().add(ratings);
        }

        return chartData;
    }

    /**
     * @param tournament tournament
     * @return pair of playerRepository and matrix of playerRepository ratings.
     * The matrix is a list of kampe where each kamp contains all playerRepository ratings after the kamp
     */
    public Pair<List<Player>, List<List<Integer>>> generateTurneringPerformanceChartData(Tournament tournament) {
        List<Match> kampe = getKampe(tournament);
        ModelHelper.sortKampeOldestFirst(kampe);
        List<Player> players = getPlayers(tournament);
        // clear ratings
        for (Player player : players) {
            player.resetAntalKampe();
            player.setRating(0);
            player.setStartRating(getTurneringStartRating(tournament, player));
        }
        // add playerRepository
        Pair<List<Player>,List<List<Integer>>> chartData = new Pair<List<Player>,List<List<Integer>>>(new ArrayList<Player>(), new ArrayList<List<Integer>>());
        for (Player player : players) {
            chartData.getFirst().add(player);
        }

        // add initial ratings
        List<Integer> ratings = new ArrayList<Integer>();
        for (Player player : players) {
            ratings.add(player.getRating());
        }
        chartData.getSecond().add(ratings);

        // update ratings
        for (Match match : kampe) {
                updatePlayers(players, match);
                updateRating(players, match, true);
                ratings = new ArrayList<Integer>();
                for (Player player : players) {
                    ratings.add(player.getRating());
                }
                chartData.getSecond().add(ratings);
        }

        return chartData;
    }

    public List<List<Object>> generateTurneringRatingChartData2(Tournament tournament) {
        // this tournaments competitors
        List<Player> playersInChart = getPlayers(tournament);
        // this tournaments matches
        List<Match> kampe = getKampe(tournament);
        // the playerRepository that gets calculated rating
        List<Player> players = getPlayers(tournament);

        return generateChartData(playersInChart, kampe, players, tournament);
    }

    public List<List<Object>> generateTurneringPerformanceChartData2(Tournament tournament) {
        // this tournaments competitors
        List<Player> playersInChart = getPlayers(tournament);
        // this tournaments matches
        List<Match> kampe = getKampe(tournament);
        // the playerRepository that gets calculated rating
        List<Player> players = getPlayers(tournament);

        return generateChartData(playersInChart, kampe, players, tournament);
    }

    public List<List<Object>> generatePlayerRatingChartData2(Player player) {

        // this player's competitors
        List<Player> playersInChart = getPlayerAndCompetitors(player);
        // these players matches
        List<Match> kampe = getKampe(playersInChart);
        // the playerRepository that gets calculated rating
        List<Player> players = getPlayers(kampe);
        if(players.isEmpty()) players.add(player);

        return generateChartData(playersInChart, kampe, players, null);
    }

    private List<List<Object>> generateChartData(List<Player> playersInChart, List<Match> kampe, List<Player> players, Tournament tournament) {
        ModelHelper.sortKampeOldestFirst(kampe);

        // every player must be cleared - move rating from player!
        clearAllPlayersRatingAndAntalKampe(players, tournament);

        List<List<Object>> data = new ArrayList<List<Object>>();
        List<Object> row = new ArrayList<Object>();

        // add playerRepository
        row.add("Match");
        Pair<List<Player>,List<List<Integer>>> chartData = new Pair<List<Player>,List<List<Integer>>>(new ArrayList<Player>(), new ArrayList<List<Integer>>());
        for (Player p : playersInChart) {
            chartData.getFirst().add(p);
            row.add(p.getId());
        }
        data.add(row);

        row = new ArrayList<Object>();
        row.add(0);
        // add initial ratings
        List<Integer> ratings = new ArrayList<Integer>();
        for (Player p : playersInChart) {
            int index = players.indexOf(p);
            if (index != -1) {
                Player playerWithRating = players.get(index);
                ratings.add(playerWithRating.getRating());
                row.add(playerWithRating.getRating());
            } else {
                ratings.add(p.getStartRating());
                row.add(p.getStartRating());
            }
        }
        chartData.getSecond().add(ratings);
        data.add(row);

        // update ratings
        int i = 1;
        for (Match match : kampe) {
            row = new ArrayList<Object>();
            row.add(i++);
            updatePlayers(players, match);
            updateRating(players, match, tournament != null && tournament.isTypePerformance());
            ratings = new ArrayList<Integer>();
            for (Player p : playersInChart) {
                int index = players.indexOf(p);
                if (index != -1) {
                    Player playerWithRating = players.get(index);
                    ratings.add(playerWithRating.getRating());
                    row.add(playerWithRating.getRating());
                } else {
                    ratings.add(p.getStartRating());
                    row.add(p.getStartRating());
                }
            }
            chartData.getSecond().add(ratings);
            data.add(row);
        }
        return data;
    }

    /**
     * @return pair of playerRepository and matrix of playerRepository ratings.
     * The matrix is a list of kampe where each kamp contains all playerRepository ratings after the kamp
     * @param player player
     * @param removePlayersNotPlayedYet
     */
    public Pair<List<Player>,List<List<Integer>>> generatePlayerRatingChartData(Player player, boolean removePlayersNotPlayedYet) {

        // this player's competitors
        List<Player> playersInChart = getPlayerAndCompetitors(player);
        // these players matches
        List<Match> kampe = getKampe(playersInChart);
        // the playerRepository that gets calculated rating
        List<Player> players = getPlayers(kampe);
        if(players.isEmpty()) players.add(player);

        ModelHelper.sortKampeOldestFirst(kampe);

        // every player must be cleared - move rating from player!
        clearAllPlayersRatingAndAntalKampe(players, null);

        // add playerRepository
        Pair<List<Player>,List<List<Integer>>> chartData = new Pair<List<Player>,List<List<Integer>>>(new ArrayList<Player>(), new ArrayList<List<Integer>>());
        for (Player p : playersInChart) {
            chartData.getFirst().add(p);
        }

        // add initial ratings
        List<Integer> ratings = new ArrayList<Integer>();
        for (Player p : playersInChart) {
            int index = players.indexOf(p);
            if (index != -1) {
                Player playerWithRating = players.get(index);
                ratings.add(playerWithRating.getRating());
            } else {
                ratings.add(p.getStartRating());
            }
        }
        chartData.getSecond().add(ratings);

        // update ratings
        for (Match match : kampe) {
            updatePlayers(players, match);
            updateRating(players, match, false);
            ratings = new ArrayList<Integer>();
            for (Player p : playersInChart) {
                int index = players.indexOf(p);
                if (index != -1) {
                    Player playerWithRating = players.get(index);
                    ratings.add(playerWithRating.getRating());
                } else {
                    ratings.add(p.getStartRating());
                }
            }
            chartData.getSecond().add(ratings);
        }

        // remove playerRepository not playing
        if(removePlayersNotPlayedYet) {
            for (Player p : playersInChart) {
                int idx = players.indexOf(p);
                Player playerWithRating = idx != -1 ? players.get(idx) : null;
                if (playerWithRating == null || (!player.getId().equals(playerWithRating.getId()) && playerWithRating.getAntalKampe() == 0)) {
                    int index = chartData.getFirst().indexOf(p);
                    chartData.getFirst().remove(index);
                }
            }
        }
        return chartData;
    }

    private void clearAllPlayersRatingAndAntalKampe(List<Player> players, Tournament tournament) {
        // clear ratings
        for (Player p : players) {
            p.resetAntalKampe();
            if(tournament != null && tournament.isTypePerformance()) {
                p.setRating(0);
                p.setStartRating(getTurneringStartRating(tournament, p));
            } else {
                p.setRating(INITIAL_RATING);
            }
        }
    }

    public PlayerStats generatePlayerStats(Player player) {
        PlayerStats stats = new PlayerStats();
        List<Match> playerKampe = getKampe(player);
        stats.setTeamTotalGoals(findTeamTotalGoals(playerKampe));
        stats.setTeamWins(findTeamWins(playerKampe));
        return stats;
    }

    public TurneringStats generateTurneringStats(Tournament tournament) {
        TurneringStats stats = new TurneringStats();
        List<Match> turneringsKampe = getKampe(tournament);
        stats.setTeamTotalGoals(findTeamTotalGoals(turneringsKampe));
        stats.setTeamWins(findTeamWins(turneringsKampe));
        return stats;
    }

    private List<TeamWins> findTeamWins(List<Match> kampe) {
        Map<Team, TeamWins> winsMap = new HashMap<Team, TeamWins>();
        for (Match match : kampe) {
            PlayingTeams playingTeams = new PlayingTeams(match).invoke();
            Team team1 = playingTeams.getTeam1();
            Team team2 = playingTeams.getTeam2();
            if(!winsMap.containsKey(team1)) winsMap.put(team1, new TeamWins(team1));
            if(!winsMap.containsKey(team2)) winsMap.put(team2, new TeamWins(team2));

            winsMap.get(team1).addKamp();
            winsMap.get(team2).addKamp();

            if(match.score1 > match.score2) {
                winsMap.get(team1).addWin();
            } else {
                winsMap.get(team2).addWin();
            }
        }

        List<TeamWins> teamWinsList = new ArrayList<TeamWins>(winsMap.values());
        Collections.sort(teamWinsList);
        return teamWinsList;
    }

    private List<TeamTotalGoals> findTeamTotalGoals(List<Match> kampe) {
        Map<Team, TeamTotalGoals> goalsMap = new HashMap<Team, TeamTotalGoals>();
        for (Match match : kampe) {
            PlayingTeams playingTeams = new PlayingTeams(match).invoke();
            Team team1 = playingTeams.getTeam1();
            Team team2 = playingTeams.getTeam2();
            if(!goalsMap.containsKey(team1)) goalsMap.put(team1, new TeamTotalGoals(team1));
            if(!goalsMap.containsKey(team2)) goalsMap.put(team2, new TeamTotalGoals(team2));

            goalsMap.get(team1).addKamp();
            goalsMap.get(team2).addKamp();
            goalsMap.get(team1).addGoals(match.score1);
            goalsMap.get(team2).addGoals(match.score2);
            goalsMap.get(team1).addTotalGoals(match.score1 + match.score2);
            goalsMap.get(team2).addTotalGoals(match.score1 + match.score2);
        }
        
        List<TeamTotalGoals> teamTotalGoalsList = new ArrayList<TeamTotalGoals>(goalsMap.values());
        Collections.sort(teamTotalGoalsList);
        return teamTotalGoalsList;
    }

    public void closeTournament(Tournament tournament) {
        tournament.afslut();
        tournamentRepository.put(tournament);
    }

    public void openTournament(Tournament tournament) {
        tournament.genaabn();
        tournamentRepository.put(tournament);
    }

    public boolean playerHasTurneringRelation(String playerId, Long tournamentId) {
        Iterable<TournamentRelation> q = tournamentRelationsRepository.findByPlayer(playerId);
        for (TournamentRelation tournamentRelation : q) {
            if(tournamentRelation.getTurneringId().equals(tournamentId)) {
                return true;
            }
        }
        return false;
    }

    public boolean playerHasPlayerRelation(String playerId, String opponentId) {
        if(playerId.equals(opponentId)) {
            return true;
        }
        Iterable<PlayerRelation> q = playerRelationRepository.findByPlayer(playerId);
        for (PlayerRelation playerRelation : q) {
            if(playerRelation.getCompetitorId().equals(opponentId)) {
                return true;
            }
        }
        return false;
    }

    private class PlayingTeams {
        private Match match;
        private Team team1;
        private Team team2;

        public PlayingTeams(Match match) {
            this.match = match;
        }

        public Team getTeam1() {
            return team1;
        }

        public Team getTeam2() {
            return team2;
        }

        public PlayingTeams invoke() {
            if(match.isSingle()) {
                Player player1 = getPlayer(match.getPlayer1Id());
                Player player2 = getPlayer(match.getPlayer2Id());
                team1 = new Team(player1);
                team2 = new Team(player2);
            } else {
                Player def1 = getPlayer(match.getDefender1Id());
                Player att1 = getPlayer(match.getAttacker1Id());
                Player def2 = getPlayer(match.getDefender2Id());
                Player att2 = getPlayer(match.getAttacker2Id());
                team1 = new Team(att1, def1);
                team2 = new Team(att2, def2);
            }
            return this;
        }
    }
}
