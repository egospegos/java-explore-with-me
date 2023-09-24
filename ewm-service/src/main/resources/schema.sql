DROP TABLE IF EXISTS requests;
DROP TABLE IF EXISTS events;
DROP TABLE IF EXISTS compilations;
DROP TABLE IF EXISTS locations;
DROP TABLE IF EXISTS categories;
DROP TABLE IF EXISTS users;


CREATE TABLE IF NOT EXISTS users (
  id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  name VARCHAR(255) NOT NULL,
  email VARCHAR(512) NOT NULL,
  UNIQUE (email)
);

CREATE TABLE IF NOT EXISTS categories (
  id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  name VARCHAR(255) NOT NULL,
  UNIQUE (name)
);

CREATE TABLE IF NOT EXISTS locations (
  id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  lat FLOAT,
  lon FLOAT
);

CREATE TABLE IF NOT EXISTS compilations (
  id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  pinned BOOLEAN,
  title varchar(50)
);

CREATE TABLE IF NOT EXISTS events (
  id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  annotation VARCHAR(2000) NOT NULL,
  category_id BIGINT, --category
  compilation_id BIGINT, --compilation
  confirmed_requests BIGINT,
  created_on varchar(50),
  description VARCHAR(7000),
  event_date varchar(50),
  initiator_id BIGINT, --initiator
  location_id BIGINT, --location
  paid BOOLEAN NOT NULL,
  participant_limit BIGINT,
  published_on varchar(50),
  request_moderation BOOLEAN,
  state varchar(50),
  title VARCHAR(512) NOT NULL,
  views BIGINT,

  CONSTRAINT fk_events_to_users FOREIGN KEY(initiator_id) REFERENCES users(id),
  CONSTRAINT fk_events_to_categories FOREIGN KEY(category_id) REFERENCES categories(id),
  CONSTRAINT fk_events_to_compilations FOREIGN KEY(compilation_id) REFERENCES compilations(id),
  CONSTRAINT fk_events_to_locations FOREIGN KEY(location_id) REFERENCES locations(id)

);

CREATE TABLE IF NOT EXISTS requests (
  id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  event_id BIGINT, --events
  requester_id BIGINT, --users
  created timestamp,
  status varchar(50),

  CONSTRAINT fk_requests_to_users FOREIGN KEY(requester_id) REFERENCES users(id),
  CONSTRAINT fk_requests_to_events FOREIGN KEY(event_id) REFERENCES events(id)
);