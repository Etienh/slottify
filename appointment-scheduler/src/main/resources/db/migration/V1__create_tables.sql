-- USERS
CREATE TABLE users (
    id UUID PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(255) NOT NULL UNIQUE,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    status BOOLEAN NOT NULL
);

-- PROJECT
CREATE TABLE project (
    id UUID PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    status BOOLEAN,
    user_shared_calendar BOOLEAN,
    item_shared_calendar BOOLEAN,
    time_slot INT,
    created_by VARCHAR(100) NOT NULL,
    created_at TIMESTAMP(6) NOT NULL,
    modified_by VARCHAR(100) NOT NULL,
    modified_at TIMESTAMP(6) NOT NULL
);

-- USER_PROJECT
CREATE TABLE user_project (
    id UUID PRIMARY KEY,
    user_id UUID REFERENCES users(id),
    project_id UUID REFERENCES project(id),
    access_type VARCHAR(50)
);

-- Item
CREATE TABLE item (
    id UUID PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    attributes JSON,
    time_slot INT,
    project_id UUID REFERENCES project(id),
    created_by VARCHAR(100) NOT NULL,
    created_at TIMESTAMP(6) NOT NULL,
    modified_by VARCHAR(100) NOT NULL,
    modified_at TIMESTAMP(6) NOT NULL
);

-- PRIORITY
CREATE TABLE priority (
    id UUID PRIMARY KEY,
    level INT NOT NULL,
    color VARCHAR(255),
    project_id UUID REFERENCES project(id),
    created_by VARCHAR(100) NOT NULL,
    created_at TIMESTAMP(6) NOT NULL,
    modified_by VARCHAR(100) NOT NULL,
    modified_at TIMESTAMP(6) NOT NULL
);

-- APPOINTMENT
CREATE TABLE appointment (
    id UUID PRIMARY KEY,
    start_date_time TIMESTAMP(6) NOT NULL,
    end_date_time TIMESTAMP(6) NOT NULL,
    description TEXT,
    phone_number VARCHAR(15),
    user_project_id UUID REFERENCES user_project(id),
    item_id UUID REFERENCES item(id),
    priority_id UUID REFERENCES priority(id),
    created_by VARCHAR(100) NOT NULL,
    created_at TIMESTAMP(6) NOT NULL,
    modified_by VARCHAR(100) NOT NULL,
    modified_at TIMESTAMP(6) NOT NULL
);

-- SHIFT
CREATE TABLE shift (
    id UUID PRIMARY KEY,
    start_hour TIME NOT NULL,
    end_hour TIME NOT NULL,
    type VARCHAR(30) NOT NULL,
    user_project_id UUID REFERENCES user_project(id),
    created_by VARCHAR(100) NOT NULL,
    created_at TIMESTAMP(6) NOT NULL,
    modified_by VARCHAR(100) NOT NULL,
    modified_at TIMESTAMP(6) NOT NULL
);
