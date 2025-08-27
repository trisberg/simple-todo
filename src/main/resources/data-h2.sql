-- H2 Sample Data for Development
-- This inserts sample todos for development and testing

INSERT INTO todos (task, completed, created_at, updated_at) VALUES
    ('Learn Spring Boot and create a todo application', true, CURRENT_TIMESTAMP - 2, CURRENT_TIMESTAMP - 1),
    ('Set up PostgreSQL database for production', false, CURRENT_TIMESTAMP - 1, CURRENT_TIMESTAMP - 1),
    ('Implement user authentication and authorization', false, CURRENT_TIMESTAMP - 1, CURRENT_TIMESTAMP - 1),
    ('Add dark mode toggle functionality', true, CURRENT_TIMESTAMP - 0.5, CURRENT_TIMESTAMP - 0.25),
    ('Deploy application to cloud platform', false, CURRENT_TIMESTAMP - 0.25, CURRENT_TIMESTAMP - 0.25),
    ('Write comprehensive documentation', false, CURRENT_TIMESTAMP - 0.125, CURRENT_TIMESTAMP - 0.125),
    ('Optimize database queries and performance', false, CURRENT_TIMESTAMP - 0.05, CURRENT_TIMESTAMP - 0.05),
    ('Add email notifications for completed tasks', false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
