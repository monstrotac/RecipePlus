CREATE DATABASE recipePlusDatabase;

CREATE TABLE USER (
	id INT UNIQUE NOT NULL AUTO_INCREMENT,
	firstName VARCHAR(255) NOT NULL,
	lastName VARCHAR(255) NOT NULL,
	password VARCHAR(255) NOT NULL,
	email VARCHAR(255) NOT NULL UNIQUE,
	PRIMARY KEY (id)
);
	
CREATE TABLE RECIPE (
	id INT UNIQUE NOT NULL AUTO_INCREMENT,
	name VARCHAR(85) NOT NULL,
	description VARCHAR(255) NOT NULL,
	instruction TEXT NOT NULL
	PRIMARY KEY (id)
);

CREATE TABLE INGREDIENT (
	id INT UNIQUE NOT NULL AUTO_INCREMENT,
	name VARCHAR(255) NOT NULL,
	description VARCHAR(255) NOT NULL,
	averagePrice REAL,
	PRIMARY KEY (id)
);

CREATE TABLE USER_FAVORITE (
	userId INT NOT NULL,
	recipeId INT NOT NULL,
	PRIMARY KEY (userId, recipeId),
	FOREIGN KEY (recipeId) REFERENCES RECIPE(id),
	FOREIGN KEY (userId) REFERENCES USER(id)
);

CREATE TABLE USER_IMAGE (
	id INT NOT NULL UNIQUE AUTO_INCREMENT,
	userId INT NOT NULL UNIQUE,
	image BLOB NOT NULL,
	PRIMARY KEY (id),
	FOREIGN KEY (userId) REFERENCES USER(id)
);

CREATE TABLE RECIPE_IMAGE (
	id INT NOT NULL UNIQUE AUTO_INCREMENT,
	recipeId INT NOT NULL,
	image BLOB NOT NULL,
	PRIMARY KEY (id),
	FOREIGN KEY (recipeId) REFERENCES RECIPE(id)
);

CREATE TABLE RECIPE_THUMBNAIL (
	id INT NOT NULL UNIQUE AUTO_INCREMENT,
	recipeId INT NOT NULL UNIQUE,
	image BLOB NOT NULL,
	PRIMARY KEY (id),
	FOREIGN KEY (recipeId) REFERENCES RECIPE(id)
);

CREATE TABLE RECIPE_INGREDIENT (
	ingredientId INT NOT NULL,
	recipeId INT NOT NULL,
	PRIMARY KEY (ingredientId, recipeId),
	FOREIGN KEY (recipeId) REFERENCES RECIPE(id),
	FOREIGN KEY (ingredientId) REFERENCES INGREDIENT(id)
);