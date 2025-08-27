#!/bin/bash

# Cloud Foundry Deployment Script for Simple Todo Application
# Usage: ./cf-deploy.sh [dev|prod]

set -e

ENVIRONMENT=${1:-dev}
APP_NAME="simple-todo-${ENVIRONMENT}"

echo "🚀 Deploying Simple Todo Application to Cloud Foundry (${ENVIRONMENT} environment)"

# Check if logged into CF
if ! cf target >/dev/null 2>&1; then
    echo "❌ Not logged into Cloud Foundry. Please run 'cf login' first."
    exit 1
fi

echo "📋 Current CF target:"
cf target

# Build the application
echo "🔨 Building application..."
./mvnw clean package -DskipTests

# Check if JAR file exists
if [ ! -f "target/simple-todo-0.0.1-SNAPSHOT.jar" ]; then
    echo "❌ JAR file not found. Build may have failed."
    exit 1
fi

# Deploy based on environment
if [ "$ENVIRONMENT" = "prod" ]; then
    echo "🏭 Deploying to PRODUCTION environment..."
    
    # Check if PostgreSQL service exists, create if not
    if ! cf service simple-todo-postgres >/dev/null 2>&1; then
        echo "📊 Creating PostgreSQL service..."
        cf create-service postgres shared-postgres simple-todo-postgres
        echo "⏳ Waiting for service to be ready..."
        sleep 30
    fi
    
    # Deploy production app
    cf push simple-todo-prod -f manifest.yml
    
    echo "🔗 Production app routes:"
    cf apps | grep simple-todo-prod
    
elif [ "$ENVIRONMENT" = "dev" ]; then
    echo "🔧 Deploying to DEVELOPMENT environment..."
    
    # Deploy development app (uses H2, no external services needed)
    cf push simple-todo-dev -f manifest.yml
    
    echo "🔗 Development app routes:"
    cf apps | grep simple-todo-dev
    
else
    echo "❌ Invalid environment. Use 'dev' or 'prod'"
    exit 1
fi

echo "✅ Deployment completed successfully!"
echo ""
echo "📱 Application URLs:"
if [ "$ENVIRONMENT" = "dev" ]; then
    echo "   🔧 Development: https://simple-todo-dev.cfapps.io"
    echo "   💚 Health Check: https://simple-todo-dev.cfapps.io/actuator/health"
else
    echo "   🏭 Production: https://simple-todo.cfapps.io"
    echo "   💚 Health Check: https://simple-todo.cfapps.io/actuator/health"
fi
echo ""
echo "🔍 Useful commands:"
echo "   cf logs ${APP_NAME} --recent    # View recent logs"
echo "   cf ssh ${APP_NAME}              # SSH into app container"
echo "   cf env ${APP_NAME}              # View environment variables"
echo "   cf scale ${APP_NAME} -i 3       # Scale to 3 instances"
echo "   cf restage ${APP_NAME}          # Restage application"
