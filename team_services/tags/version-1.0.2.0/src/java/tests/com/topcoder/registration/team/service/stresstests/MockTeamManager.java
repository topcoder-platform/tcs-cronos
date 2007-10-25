/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.registration.team.service.stresstests;

import com.topcoder.management.team.InvalidPositionException;
import com.topcoder.management.team.InvalidTeamException;
import com.topcoder.management.team.PositionRemovalException;
import com.topcoder.management.team.Team;
import com.topcoder.management.team.TeamHeader;
import com.topcoder.management.team.TeamManager;
import com.topcoder.management.team.TeamPosition;
import com.topcoder.management.team.UnknownEntityException;
import com.topcoder.search.builder.filter.Filter;

/**
 * <p>
 * This is a mock implementation of <code>TeamManager</code>.
 * </p>
 * <p>
 * It is just used for tests.
 * </p>
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
public class MockTeamManager implements TeamManager {

    /**
     * <p>
     * Project id.
     * </p>
     */
    private static int id = 1;

    /**
     * Constructs an instance.
     */
    public MockTeamManager() {

    }

    /**
     * Creates the team.
     *
     * @param team   The team to be created
     * @param userId The user Id for logging and auditing purposes
     * @throws IllegalArgumentException If team is null or userId is negative
     * @throws InvalidTeamException     If the team contains invalid data
     * @throws com.topcoder.management.team.TeamPersistenceException
     *                                  If any unexpected system error occurs
     */
    public void createTeam(TeamHeader team, long userId) throws InvalidTeamException {
    }

    /**
     * Updates the team.
     *
     * @param team   The team to be updated
     * @param userId The user Id for logging and auditing purposes
     * @throws IllegalArgumentException If team is null or userId is negative
     * @throws InvalidTeamException     If the team contains invalid data
     * @throws com.topcoder.management.team.TeamPersistenceException
     *                                  If any unexpected system error occurs
     */
    public void updateTeam(TeamHeader team, long userId) throws InvalidTeamException {
        if (team.getTeamId() == 11) {
            throw new InvalidTeamException("Error occurred.");
        }
    }

    /**
     * Removes the team and all positions associated with it.
     *
     * @param teamId The ID of the team to be removed
     * @param userId The user Id for logging and auditing purposes
     * @throws IllegalArgumentException If teamId or userId is negative
     * @throws UnknownEntityException   If teamId is unknown
     * @throws com.topcoder.management.team.TeamPersistenceException
     *                                  If any unexpected system error occurs
     */
    public void removeTeam(long teamId, long userId) throws UnknownEntityException {
    }

    /**
     * Retrieves the team and all associated positions. Returns null if it does not exist.
     *
     * @param teamId The ID of the team to be retrieved
     * @return Team with the given ID, or null if none found
     * @throws IllegalArgumentException If teamId is negative
     * @throws com.topcoder.management.team.TeamPersistenceException
     *                                  If any unexpected system error occurs
     */
    public Team getTeam(long teamId) {
        TeamImpl theTeam = new TeamImpl();

        TeamHeader team = new TeamHeader();
        if (teamId != -1) {
            team.setTeamId(teamId);
        }
        team.setName("Robot Team");
        team.setDescription("A strong team.");
        team.setCaptainResourceId(1);
        if (teamId == 19 || teamId == 18) {
            team.setProjectId(5);
            team.setCaptainPaymentPercentage(40);
        } else if (teamId == 15) {
            team.setProjectId(10);
        } else {
            team.setProjectId(1);
        }

        TeamPosition pos = new TeamPosition();
        pos.setPositionId(2);
        pos.setFilled(true);
        pos.setMemberResourceId(2);
        pos.setName("Good Position");

        if (teamId == 19) {
            pos.setPaymentPercentage(30); 
        }
        TeamPosition pos1 = new TeamPosition();
        pos1.setPositionId(3);
        pos1.setFilled(true);
        pos1.setMemberResourceId(2);
        pos1.setName("Good Position");
        if (teamId == 18) {
            team.setFinalized(true);
        }

        if (teamId == 19) {
            pos1.setPaymentPercentage(30); 
        }
        theTeam.setTeamHeader(team);
        theTeam.setPositions(new TeamPosition[]{pos, pos1});

        return theTeam;
    }

    /**
     * Finds all teams associated with the project with the given ID. Returns empty array if none
     * found.
     *
     * @param projectId The ID of the project whose teams are to be retrieved
     * @return An array of matching TeamHeader, or empty if no matches found
     * @throws IllegalArgumentException If projectId is negative
     * @throws com.topcoder.management.team.TeamPersistenceException
     *                                  If any unexpected system error occurs
     */
    public TeamHeader[] findTeams(long projectId) {
        TeamHeader team = new TeamHeader();
        team.setTeamId(1);
        return new TeamHeader[]{team};
    }

    /**
     * Finds all teams associated with the projects with the given IDs. Returns empty array if none
     * found.
     *
     * @param projectIds The IDs of the projects whose teams are to be retrieved
     * @return An array of matching TeamHeader, or empty if no matches found
     * @throws IllegalArgumentException If projectIds is null or contains any negative IDs
     * @throws com.topcoder.management.team.TeamPersistenceException
     *                                  If any unexpected system error occurs
     */
    public TeamHeader[] findTeams(long[] projectIds) {
        TeamHeader team = new TeamHeader();
        team.setTeamId(1);
        return new TeamHeader[]{team};
    }

    /**
     * Finds all teams that match the criteria in the passed filter. Returns empty array if none
     * found.
     *
     * @param filter The filter criteria to match teams
     * @return An array of matching TeamHeader, or empty if no matches found
     * @throws IllegalArgumentException If filter is null
     * @throws com.topcoder.management.team.TeamPersistenceException
     *                                  If any unexpected system error occurs
     */
    public TeamHeader[] findTeams(Filter filter) {
        return null;
    }

    /**
     * Gets the team, and all its positions, that the position with the given ID belongs to. Returns
     * null if it does not exist.
     *
     * @param positionId The ID of the position whose team is to be retrieved
     * @return Team that contains the position with the given ID
     * @throws IllegalArgumentException If positionId is negative
     * @throws com.topcoder.management.team.TeamPersistenceException
     *                                  If any unexpected system error occurs
     */
    public Team getTeamFromPosition(long positionId) {
        if (positionId == 100) {
            return null;
        }
        TeamImpl theTeam = new TeamImpl();

        TeamHeader team = new TeamHeader();
        team.setName("Robot Team");
        team.setDescription("A strong team.");
        team.setCaptainResourceId(1);
        team.setProjectId(id);
        team.setFinalized(true);
        id++;

        TeamPosition pos = new TeamPosition();
        pos.setPositionId(2);
        pos.setMemberResourceId(2);
        pos.setName("Good Position");
        pos.setPublished(true);
        pos.setFilled(false);

        TeamPosition pos1 = new TeamPosition();
        pos1.setPositionId(3);
        pos1.setMemberResourceId(2);
        pos1.setName("Good Position");
        pos1.setPublished(true);
        pos1.setFilled(false);

        theTeam.setTeamHeader(team);
        theTeam.setPositions(new TeamPosition[]{pos, pos1});
        return theTeam;
    }

    /**
     * Adds a position to the existing team with the given teamID.
     *
     * @param teamId   The ID of the team to which the position is to be added
     * @param position TeamPosition to add to the team with the given teamId
     * @param userId   The user Id for logging and auditing purposes
     * @throws IllegalArgumentException If position is null, or teamId or userId is negative
     * @throws InvalidPositionException If the position contains invalid data
     * @throws com.topcoder.management.team.TeamPersistenceException
     *                                  If any unexpected system error occurs
     */
    public void addPosition(long teamId, TeamPosition position, long userId)
        throws InvalidPositionException {
    }

    /**
     * Updates a position.
     *
     * @param position TeamPosition to update
     * @param userId   The user Id for logging and auditing purposes
     * @throws IllegalArgumentException If position is null, userId is negative
     * @throws InvalidPositionException If the position contains invalid data
     * @throws com.topcoder.management.team.TeamPersistenceException
     *                                  If any unexpected system error occurs
     */
    public void updatePosition(TeamPosition position, long userId) throws InvalidPositionException {
        if (userId == 100) {
            throw new InvalidPositionException("error");
        }
    }

    /**
     * Removes a position
     *
     * @param positionId The ID of the position to remove
     * @param userId     The user Id for logging and auditing purposes
     * @throws IllegalArgumentException If positionId or userId is negative
     * @throws PositionRemovalException if the position is already published or filled, or the team is finalized.
     * @throws UnknownEntityException   If positionID is unknown
     * @throws com.topcoder.management.team.TeamPersistenceException
     *                                  If any unexpected system error occurs
     */
    public void removePosition(long positionId, long userId) throws UnknownEntityException,
        PositionRemovalException {
        if (userId == 100) {
            throw new PositionRemovalException("Error");
        }
        if (userId == 105) {
            throw new PositionRemovalException("Error");
        }
        if (userId == 106) {
            throw new UnknownEntityException("Error");
        }
    }

    /**
     * Retrieves the position with the given ID. Returns null if it does not exist.
     *
     * @param positionId The ID of the position to get
     * @return TeamPosition with the given ID, or null if none found
     * @throws IllegalArgumentException If positionId is negative
     * @throws com.topcoder.management.team.TeamPersistenceException
     *                                  If any unexpected system error occurs
     */
    public TeamPosition getPosition(long positionId) {
        if (positionId == 100) {
            return null;
        }
        TeamPosition pos = new TeamPosition();
        if (positionId == 101) {
            pos.setFilled(true);
        } else {
            pos.setFilled(false);
        }

        pos.setPositionId(2);
        pos.setMemberResourceId(2);
        pos.setName("Good Position");
        pos.setFilled(false);
        pos.setPublished(true);

        return pos;
    }

    /**
     * Finds all positions that match the criteria in the passed filter. Returns empty array if none
     * found.It is used just to return an empty array in order to test <c>TeamService#findFreeAgents(long)</c>
     * method.
     *
     * @param filter The filter criteria to match positions
     * @return An empty array.
     * @throws IllegalArgumentException If filter is null
     * @throws com.topcoder.management.team.TeamPersistenceException
     *                                  If any unexpected system error occurs
     */
    public TeamPosition[] findPositions(Filter filter) {
        return new TeamPosition[0];
    }

    /**
     * <p>
     * This is an implementation of Team.
     * </p>
     */
    private class TeamImpl implements Team {

        /**
         * <p>
         * Represents the TeamHeader.
         * </p>
         */
        private TeamHeader teamHeader = null;

        /**
         * <p>
         * Represents the TeamPositions.
         * </p>
         */
        private TeamPosition[] positions = null;

        /**
         * <p>
         * Gets the team header.
         * </p>
         *
         * @return the team header
         */
        public TeamHeader getTeamHeader() {
            return this.teamHeader;
        }

        /**
         * <p>
         * Sets the team header.
         * </p>
         *
         * @param teamHeader team header
         */
        public void setTeamHeader(TeamHeader teamHeader) {
            this.teamHeader = teamHeader;
        }

        /**
         * <p>
         * Gets the positions.
         * </p>
         *
         * @return array of team positions
         */
        public TeamPosition[] getPositions() {
            return this.positions;
        }

        /**
         * <p>
         * Sets the positions.
         * </p>
         *
         * @return the array of positions
         */
        public void setPositions(TeamPosition[] positions) {
            this.positions = positions;
        }

    }
}
