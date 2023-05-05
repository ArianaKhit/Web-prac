CREATE TABLE Coach (
    coachID     SERIAL PRIMARY KEY,
    name        TEXT,
    sport       TEXT,
    birthDate   DATE,
    country     TEXT,
    description TEXT
);

CREATE TABLE Sportsman (
    sportsmanID SERIAL PRIMARY KEY,
    name        TEXT,
    sport       TEXT,
    birthDate   DATE,
    country     TEXT,
    description TEXT
);

CREATE TABLE Team (
   teamID       SERIAL PRIMARY KEY,
   teamName     TEXT,
   sport        TEXT,
   country      TEXT,
   description  TEXT
);

CREATE TABLE SportEvent (
    eventID     SERIAL PRIMARY KEY,
    title       TEXT,
    sport       TEXT,
    tournament  TEXT,
    description TEXT,
    venue       TEXT,
    date        DATE,
    seats       JSON,
    results     JSON
);

CREATE TABLE TeamCoaches (
     ID          SERIAL PRIMARY KEY,
     coachID     INT,
     teamID      INT,
     dates       JSON,
     FOREIGN KEY(coachID) REFERENCES Coach(coachID),
     FOREIGN KEY(teamID) REFERENCES Team(teamID),
     UNIQUE (coachID, teamID)
);

CREATE TABLE SportsmanCoaches (
    ID          SERIAL PRIMARY KEY,
    coachID     INT,
    sportsmanID INT,
    dates       JSON,
    FOREIGN KEY(coachID) REFERENCES Coach(coachID),
    FOREIGN KEY(sportsmanID) REFERENCES Sportsman(sportsmanID),
    UNIQUE (coachID, sportsmanID)
);

CREATE TABLE TeamSportsmans (
    ID          SERIAL PRIMARY KEY,
    teamID      INT,
    sportsmanID INT,
    dates       JSON,
    current     BOOLEAN,
    FOREIGN KEY(teamID) REFERENCES Team(teamID),
    FOREIGN KEY(sportsmanID) REFERENCES Sportsman(sportsmanID),
    UNIQUE (teamID, sportsmanID)
);


CREATE TABLE EventSportsmans (
    ID          SERIAL PRIMARY KEY,
    eventID     INT,
    sportsmanID INT,
    FOREIGN KEY(eventID) REFERENCES SportEvent(eventID),
    FOREIGN KEY(sportsmanID) REFERENCES Sportsman(sportsmanID),
    UNIQUE (eventID, sportsmanID)
);

CREATE TABLE EventTeams (
    ID          SERIAL PRIMARY KEY,
    eventID     INT,
    teamID      INT,
    FOREIGN KEY(eventID) REFERENCES SportEvent(eventID),
    FOREIGN KEY(teamID) REFERENCES Team(teamID),
    UNIQUE (eventID, teamID)
);
