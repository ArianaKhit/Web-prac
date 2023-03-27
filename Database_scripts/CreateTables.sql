CREATE TABLE Coach (
    coachID     SERIAL PRIMARY KEY,
    name        TEXT,
    sport       TEXT,
    birthDate   TIMESTAMPTZ,
    country     TEXT
);

CREATE TABLE Sportsman (
    sportsmanID SERIAL PRIMARY KEY,
    name        TEXT,
    sport       TEXT,
    birthDate   TIMESTAMPTZ,
    country     TEXT
);

CREATE TABLE Team (
   teamID       SERIAL PRIMARY KEY,
   teamName     TEXT,
   sport        TEXT,
   country      TEXT
);

CREATE TABLE SportEvent (
    eventID     SERIAL PRIMARY KEY,
    sport       TEXT,
    title       TEXT,
    tournament  TEXT,
    description TEXT,
    venue       TEXT,
    date        TIMESTAMPTZ,
    seats       JSONB,
    results     JSONB
);

CREATE TABLE TeamCoaches (
     coachID     INT,
     teamID      INT,
     dates       JSONB,
     PRIMARY KEY (coachID, teamID),
     FOREIGN KEY(coachID) REFERENCES Coach(coachID),
     FOREIGN KEY(teamID) REFERENCES Team(teamID)
);

CREATE TABLE SportsmanCoaches (
    coachID     INT,
    sportsmanID INT,
    dates       JSONB,
    PRIMARY KEY (coachID, sportsmanID),
    FOREIGN KEY(coachID) REFERENCES Coach(coachID),
    FOREIGN KEY(sportsmanID) REFERENCES Sportsman(sportsmanID)
);

CREATE TABLE TeamSportsmans (
    teamID      INT,
    sportsmanID INT,
    dates       JSONB,
    current     BOOLEAN,
    PRIMARY KEY (teamID,sportsmanID),
    FOREIGN KEY(teamID) REFERENCES Team(teamID),
    FOREIGN KEY(sportsmanID) REFERENCES Sportsman(sportsmanID)
);


CREATE TABLE EventSportsmans (
    eventID     INT,
    sportsmanID INT,
    PRIMARY KEY (eventID, sportsmanID),
    FOREIGN KEY(eventID) REFERENCES SportEvent(eventID),
    FOREIGN KEY(sportsmanID) REFERENCES Sportsman(sportsmanID)
);

CREATE TABLE EventTeams (
    eventID     INT,
    teamID      INT,
    PRIMARY KEY (eventID,teamID),
    FOREIGN KEY(eventID) REFERENCES SportEvent(eventID),
    FOREIGN KEY(teamID) REFERENCES Team(teamID)
);
