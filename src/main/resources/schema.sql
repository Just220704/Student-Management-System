-- 1. Create the Students table with N-Admin composite unique constraint
CREATE TABLE IF NOT EXISTS students (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    registration_number VARCHAR(255) NOT NULL,
    admin_email VARCHAR(255) NOT NULL,
    name VARCHAR(255),
    email VARCHAR(255),
    course VARCHAR(255),
    password VARCHAR(255),
    role VARCHAR(50),
    -- This allows different admins to use the same Reg No
    UNIQUE KEY uk_reg_admin (registration_number, admin_email) 
);

-- 2. Create the Courses table with N-Admin composite unique constraint
CREATE TABLE IF NOT EXISTS courses (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    course_code VARCHAR(255) NOT NULL,
    admin_email VARCHAR(255) NOT NULL,
    course_name VARCHAR(255),
    intake INT,
    syllabus TEXT,
    -- This allows different admins to have the same Course Code
    UNIQUE KEY uk_course_admin (course_code, admin_email)
);