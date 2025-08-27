-- Insert sample todo data
-- This migration adds some initial sample todos for demonstration

INSERT INTO todos (task, completed, created_at, updated_at) VALUES
    ('Learn Spring Boot and create a todo application', true, CURRENT_TIMESTAMP - INTERVAL '2 days', CURRENT_TIMESTAMP - INTERVAL '1 day'),
    ('Set up PostgreSQL database for production', false, CURRENT_TIMESTAMP - INTERVAL '1 day', CURRENT_TIMESTAMP - INTERVAL '1 day'),
    ('Implement user authentication and authorization', false, CURRENT_TIMESTAMP - INTERVAL '1 day', CURRENT_TIMESTAMP - INTERVAL '1 day'),
    ('Add dark mode toggle functionality', true, CURRENT_TIMESTAMP - INTERVAL '12 hours', CURRENT_TIMESTAMP - INTERVAL '6 hours'),
    ('Deploy application to cloud platform', false, CURRENT_TIMESTAMP - INTERVAL '6 hours', CURRENT_TIMESTAMP - INTERVAL '6 hours'),
    ('Write comprehensive documentation', false, CURRENT_TIMESTAMP - INTERVAL '3 hours', CURRENT_TIMESTAMP - INTERVAL '3 hours'),
    ('Optimize database queries and performance', false, CURRENT_TIMESTAMP - INTERVAL '1 hour', CURRENT_TIMESTAMP - INTERVAL '1 hour'),
    ('Add email notifications for completed tasks', false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Update the statistics to show some completed and pending items
-- This demonstrates the filtering and statistics functionality
