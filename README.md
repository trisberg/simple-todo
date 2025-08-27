# Simple Todo List - Spring Boot Application

A beautiful and feature-rich todo list application built with Spring Boot, Thymeleaf, and modern CSS. This application provides both a web interface and REST API for managing your tasks.

## Features

### 🎯 Core Features
- ✅ Create, read, update, and delete todos
- ✅ Mark todos as completed/pending
- ✅ Search todos by content
- ✅ Filter todos by status (All, Pending, Completed)
- ✅ View todo statistics
- ✅ Beautiful, responsive UI with modern design
- ✅ Full REST API support

### 🎨 UI Features
- Modern gradient background
- Responsive design (mobile-friendly)
- Beautiful cards and animations
- Font Awesome icons
- Success/error flash messages
- Real-time todo statistics

### 🔧 Technical Features
- Spring Boot 3.5.5
- Java 21
- H2 in-memory database
- JPA/Hibernate for data persistence
- Thymeleaf templating engine
- Maven build system
- Comprehensive validation
- RESTful API endpoints

## Quick Start

### Prerequisites
- Java 21 or higher
- Maven 3.6+ (or use the Maven wrapper included)
- PostgreSQL 12+ (for production) or use H2 (for development)

### Database Setup

#### Development (H2 - Default)
The application runs with H2 in-memory database by default for easy development:

```bash
./mvnw spring-boot:run
```

**Access H2 Console:**
- URL: http://localhost:8080/h2-console
- JDBC URL: `jdbc:h2:mem:tododb`
- Username: `sa`
- Password: (leave empty)

#### Production (PostgreSQL)

1. **Install PostgreSQL:**
   ```bash
   # macOS with Homebrew
   brew install postgresql
   brew services start postgresql
   
   # Ubuntu/Debian
   sudo apt-get install postgresql postgresql-contrib
   sudo systemctl start postgresql
   
   # Windows - Download from https://www.postgresql.org/download/
   ```

2. **Create Database:**
   ```bash
   # Connect to PostgreSQL
   psql -U postgres
   
   # Create database and user
   CREATE DATABASE simpletodo;
   CREATE USER todouser WITH ENCRYPTED PASSWORD 'todopass';
   GRANT ALL PRIVILEGES ON DATABASE simpletodo TO todouser;
   \q
   ```

3. **Run with PostgreSQL:**
   ```bash
   # Set environment variables
   export DATABASE_URL=jdbc:postgresql://localhost:5432/simpletodo
   export DATABASE_USERNAME=todouser
   export DATABASE_PASSWORD=todopass
   
   # Run with production profile
   ./mvnw spring-boot:run -Dspring.profiles.active=prod
   ```

   Or use application properties:
   ```bash
   ./mvnw spring-boot:run -Dspring.profiles.active=prod \
     -Ddatabase.url=jdbc:postgresql://localhost:5432/simpletodo \
     -Ddatabase.username=todouser \
     -Ddatabase.password=todopass
   ```

#### Docker Setup (Recommended for Production)

1. **Start PostgreSQL with Docker:**
   ```bash
   # Start PostgreSQL and pgAdmin
   docker-compose up -d postgres
   
   # Or start both PostgreSQL and pgAdmin
   docker-compose up -d
   ```

2. **Access Services:**
   - **PostgreSQL**: localhost:5432
   - **pgAdmin**: http://localhost:5050 (admin@simpletodo.com / admin)

3. **Run Application:**
   ```bash
   ./mvnw spring-boot:run -Dspring.profiles.active=prod
   ```

4. **Stop Services:**
   ```bash
   docker-compose down
   ```

### Running the Application

1. **Clone or navigate to the project directory:**
   ```bash
   cd /Users/risbergt/workspace/cursor/simple-todo
   ```

2. **Development Mode (H2):**
   ```bash
   ./mvnw spring-boot:run
   ```

3. **Production Mode (PostgreSQL):**
   ```bash
   ./mvnw spring-boot:run -Dspring.profiles.active=prod
   ```

4. **Access the application:**
   - **Web Interface:** http://localhost:8080
   - **H2 Console (dev only):** http://localhost:8080/h2-console

## API Endpoints

### REST API
The application provides a complete REST API at `/api/todos`:

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/todos` | Get all todos |
| GET | `/api/todos/pending` | Get pending todos |
| GET | `/api/todos/completed` | Get completed todos |
| GET | `/api/todos/{id}` | Get todo by ID |
| POST | `/api/todos` | Create new todo |
| PUT | `/api/todos/{id}` | Update todo |
| PATCH | `/api/todos/{id}/toggle` | Toggle completion status |
| PATCH | `/api/todos/{id}/complete` | Mark as completed |
| DELETE | `/api/todos/{id}` | Delete todo |
| GET | `/api/todos/search?q={query}` | Search todos |
| GET | `/api/todos/stats` | Get todo statistics |
| DELETE | `/api/todos/completed` | Delete all completed todos |

### Example API Usage

**Create a new todo:**
```bash
curl -X POST http://localhost:8080/api/todos \
  -H "Content-Type: application/json" \
  -d '{"task": "Learn Spring Boot"}'
```

**Get all todos:**
```bash
curl http://localhost:8080/api/todos
```

**Toggle completion status:**
```bash
curl -X PATCH http://localhost:8080/api/todos/1/toggle
```

## Project Structure

```
src/
├── main/
│   ├── java/com/example/simpletodo/
│   │   ├── SimpleTodoApplication.java      # Main application class
│   │   ├── entity/
│   │   │   └── Todo.java                   # Todo entity with JPA annotations
│   │   ├── repository/
│   │   │   └── TodoRepository.java         # JPA repository interface
│   │   ├── service/
│   │   │   └── TodoService.java            # Business logic layer
│   │   └── controller/
│   │       ├── TodoRestController.java     # REST API endpoints
│   │       └── TodoWebController.java      # Web UI controllers
│   └── resources/
│       ├── application.properties          # Application configuration
│       ├── templates/                      # Thymeleaf templates
│       │   ├── index.html                  # Main todo list page
│       │   └── edit.html                   # Edit todo page
│       └── static/css/
│           └── style.css                   # Modern CSS styling
└── test/                                   # Test directory (ready for your tests)
```

## Database Management

### Database Profiles

The application supports two database profiles:

- **Development (`dev`)**: Uses H2 in-memory database with auto-generated schema
- **Production (`prod`)**: Uses PostgreSQL with Flyway migrations

### Database Schema

The application uses the following optimized table structure:

```sql
CREATE TABLE todos (
    id BIGSERIAL PRIMARY KEY,
    task VARCHAR(255) NOT NULL,
    completed BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Optimized indexes for performance
CREATE INDEX idx_todos_created_at ON todos(created_at DESC);
CREATE INDEX idx_todos_completed ON todos(completed);
CREATE INDEX idx_todos_pending ON todos(created_at DESC) WHERE completed = FALSE;
```

### Database Migrations (Flyway)

The application uses Flyway for database version control in production:

- **V1__Create_todos_table.sql**: Creates the initial table structure with indexes
- **V2__Insert_sample_data.sql**: Adds sample todo data for demonstration

**Migration Commands:**
```bash
# Run migrations manually (if needed)
./mvnw flyway:migrate -Dspring.profiles.active=prod

# Check migration status
./mvnw flyway:info -Dspring.profiles.active=prod

# Validate migrations
./mvnw flyway:validate -Dspring.profiles.active=prod
```

### Performance Features

- **Connection Pooling**: HikariCP with optimized settings
- **Batch Processing**: Hibernate batch operations for better performance
- **Optimized Indexes**: Strategic indexes for common query patterns
- **Query Optimization**: Efficient JPA queries with proper fetch strategies

## ☁️ Cloud Foundry Deployment

The application is ready for deployment to Cloud Foundry platforms with comprehensive configuration for both development and production environments.

### 📋 Prerequisites

- **Cloud Foundry CLI**: Install from [https://docs.cloudfoundry.org/cf-cli/](https://docs.cloudfoundry.org/cf-cli/)
- **Cloud Foundry Account**: Access to a CF platform (e.g., Pivotal Web Services, IBM Cloud, SAP BTP)
- **Built Application**: Run `./mvnw clean package` to create the JAR file

### 🚀 Quick Deployment

#### Development Environment (H2 Database)
```bash
# Login to Cloud Foundry
cf login -a api.run.pivotal.io

# Deploy development version
./cf-deploy.sh dev
```

#### Production Environment (PostgreSQL)
```bash
# Login to Cloud Foundry  
cf login -a api.run.pivotal.io

# Deploy production version (creates PostgreSQL service automatically)
./cf-deploy.sh prod
```

### 📄 Manifest Configuration

The `manifest.yml` file includes two application configurations:

#### Development App (`simple-todo-dev`)
- **Memory**: 1GB
- **Instances**: 1
- **Database**: H2 in-memory (no external services)
- **Profile**: `dev`
- **Route**: `simple-todo-dev.cfapps.io`

#### Production App (`simple-todo-prod`)
- **Memory**: 2GB  
- **Instances**: 2 (load-balanced)
- **Database**: PostgreSQL service (`simple-todo-postgres`)
- **Profile**: `prod`
- **Route**: `simple-todo.cfapps.io`

### 🔧 Manual Deployment Steps

If you prefer manual deployment:

1. **Build the application:**
   ```bash
   ./mvnw clean package
   ```

2. **Create PostgreSQL service (production only):**
   ```bash
   cf create-service postgres shared-postgres simple-todo-postgres
   ```

3. **Deploy the application:**
   ```bash
   # Development
   cf push simple-todo-dev -f manifest.yml
   
   # Production  
   cf push simple-todo-prod -f manifest.yml
   ```

### 🏥 Health Monitoring

The application includes Spring Boot Actuator for Cloud Foundry health checks:

- **Health Endpoint**: `/actuator/health`
- **Liveness Probe**: Automatic container restart if unhealthy
- **Readiness Probe**: Traffic routing control
- **Metrics**: Available at `/actuator/metrics`

### 🔗 Service Binding

For production deployments, the PostgreSQL service is automatically bound and provides:

- **Database URL**: Automatically configured via `VCAP_SERVICES`
- **Connection Pooling**: Optimized for cloud environments  
- **Flyway Migrations**: Automatic schema setup and updates
- **High Availability**: Multiple database instances

### 📊 Scaling and Management

```bash
# Scale instances
cf scale simple-todo-prod -i 5

# View application status
cf app simple-todo-prod

# View logs
cf logs simple-todo-prod --recent
cf logs simple-todo-prod  # Follow live logs

# SSH into container
cf ssh simple-todo-prod

# View environment variables
cf env simple-todo-prod

# Restart application
cf restart simple-todo-prod

# Restage (rebuild) application
cf restage simple-todo-prod
```

### 🌍 Multi-Environment Setup

The configuration supports multiple Cloud Foundry spaces:

```bash
# Target different spaces
cf target -s development
cf push simple-todo-dev -f manifest.yml

cf target -s staging  
cf push simple-todo-staging -f manifest.yml

cf target -s production
cf push simple-todo-prod -f manifest.yml
```

### 🔒 Security Considerations

- **HTTPS**: Automatic SSL/TLS termination by CF router
- **Service Binding**: Secure credential injection via `VCAP_SERVICES`
- **Network Isolation**: Container-to-container networking
- **Secrets Management**: Use CF user-provided services for sensitive data

### 📈 Monitoring and Observability

- **Application Metrics**: Available via CF metrics
- **Custom Metrics**: Exposed through Spring Actuator
- **Logging**: Centralized via CF logging service
- **Tracing**: Ready for integration with APM tools

## Configuration

The application is configured via `application.properties`:

- **Server Port:** 8080
- **Database:** H2 in-memory (data resets on restart)
- **JPA:** DDL auto-generation enabled
- **Thymeleaf:** Template caching disabled for development
- **Logging:** Debug level enabled for development

## Development

### Running in Development Mode
The application is configured for development with:
- Hot reload enabled for Thymeleaf templates
- SQL logging enabled
- H2 console accessible for database inspection
- Detailed logging for debugging

### Building for Production
```bash
mvn clean package
java -jar target/simple-todo-0.0.1-SNAPSHOT.jar
```

## Customization

### Styling
- Modify `/src/main/resources/static/css/style.css` for custom styling
- The design uses CSS custom properties for easy theme customization
- Responsive breakpoints are included for mobile devices

### Features
- Add new fields to the `Todo` entity
- Extend the `TodoService` for additional business logic
- Create new API endpoints in the controllers
- Add new pages by creating Thymeleaf templates

## Screenshots

### Main Todo List
- Beautiful gradient background
- Modern card-based design
- Filter buttons (All, Pending, Completed)
- Search functionality
- Real-time statistics

### Features Demonstrated
- Add new todos with validation
- Toggle completion status with smooth animations
- Edit todos with a dedicated page
- Delete individual todos or all completed ones
- Responsive design that works on all devices

## Contributing

Feel free to enhance this application by:
1. Adding user authentication
2. Implementing todo categories/tags
3. Adding due dates and reminders
4. Creating a mobile app using the REST API
5. Adding todo sharing functionality

## License

This project is open source and available under the MIT License.
