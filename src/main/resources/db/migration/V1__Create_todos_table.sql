-- Create todos table
-- This migration creates the initial table structure for storing todo items

CREATE TABLE todos (
    id BIGSERIAL PRIMARY KEY,
    task VARCHAR(255) NOT NULL,
    completed BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Create an index on the created_at column for efficient ordering
CREATE INDEX idx_todos_created_at ON todos(created_at DESC);

-- Create an index on the completed column for filtering
CREATE INDEX idx_todos_completed ON todos(completed);

-- Create a partial index for pending todos (most common query)
CREATE INDEX idx_todos_pending ON todos(created_at DESC) WHERE completed = FALSE;

-- Add comments for documentation
COMMENT ON TABLE todos IS 'Table storing todo list items';
COMMENT ON COLUMN todos.id IS 'Unique identifier for each todo item';
COMMENT ON COLUMN todos.task IS 'Description of the task to be completed';
COMMENT ON COLUMN todos.completed IS 'Flag indicating whether the task is completed';
COMMENT ON COLUMN todos.created_at IS 'Timestamp when the todo was created';
COMMENT ON COLUMN todos.updated_at IS 'Timestamp when the todo was last modified';
