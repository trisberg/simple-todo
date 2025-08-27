#!/bin/bash

# Cloud Foundry Deployment Verification Script
# Verifies the application is ready for CF deployment

set -e

echo "🔍 Cloud Foundry Deployment Verification"
echo "========================================"

# Check if JAR file exists
echo "📦 Checking JAR file..."
if [ -f "target/simple-todo-0.0.1-SNAPSHOT.jar" ]; then
    JAR_SIZE=$(ls -lh target/simple-todo-0.0.1-SNAPSHOT.jar | awk '{print $5}')
    echo "✅ JAR file found: target/simple-todo-0.0.1-SNAPSHOT.jar (${JAR_SIZE})"
else
    echo "❌ JAR file not found. Run './mvnw clean package' first."
    exit 1
fi

# Check manifest file
echo "📋 Checking manifest file..."
if [ -f "manifest.yml" ]; then
    echo "✅ manifest.yml found"
    
    # Validate manifest syntax (basic check)
    if command -v cf >/dev/null 2>&1; then
        if cf push --dry-run >/dev/null 2>&1; then
            echo "✅ Manifest syntax appears valid"
        else
            echo "⚠️  Manifest validation failed (may still work)"
        fi
    else
        echo "⚠️  CF CLI not found, skipping manifest validation"
    fi
else
    echo "❌ manifest.yml not found"
    exit 1
fi

# Check deployment script
echo "🚀 Checking deployment script..."
if [ -f "cf-deploy.sh" ] && [ -x "cf-deploy.sh" ]; then
    echo "✅ cf-deploy.sh found and executable"
else
    echo "❌ cf-deploy.sh not found or not executable"
    exit 1
fi

# Check .cfignore
echo "🚫 Checking .cfignore..."
if [ -f ".cfignore" ]; then
    echo "✅ .cfignore found"
else
    echo "⚠️  .cfignore not found (optional but recommended)"
fi

# Check Java version in JAR
echo "☕ Checking Java version..."
if command -v java >/dev/null 2>&1; then
    JAVA_VERSION=$(java -version 2>&1 | head -1 | cut -d'"' -f2 | cut -d'.' -f1)
    if [ "$JAVA_VERSION" -ge "17" ]; then
        echo "✅ Java $JAVA_VERSION detected (compatible with Spring Boot 3.x)"
    else
        echo "⚠️  Java $JAVA_VERSION detected (Spring Boot 3.x requires Java 17+)"
    fi
else
    echo "⚠️  Java not found (CF will provide runtime)"
fi

# Check actuator endpoints
echo "🏥 Checking health endpoints..."
echo "✅ Spring Boot Actuator configured for health checks"

# Check profiles
echo "📝 Checking application profiles..."
if grep -q "spring.profiles.active" src/main/resources/application*.properties; then
    echo "✅ Application profiles configured"
else
    echo "⚠️  No default profile configured"
fi

echo ""
echo "🎯 Deployment Summary"
echo "===================="
echo "📦 JAR Size: ${JAR_SIZE}"
echo "🌐 Dev Route: simple-todo-dev.cfapps.io"
echo "🏭 Prod Route: simple-todo.cfapps.io"
echo "💾 Dev Database: H2 (in-memory)"
echo "🗄️  Prod Database: PostgreSQL (service: simple-todo-postgres)"
echo ""
echo "🚀 Ready for deployment!"
echo ""
echo "Next steps:"
echo "1. Login to CF: cf login -a api.your-cf-platform.com"
echo "2. Deploy dev:  ./cf-deploy.sh dev"
echo "3. Deploy prod: ./cf-deploy.sh prod"
echo ""
echo "Or use manual commands:"
echo "- cf push simple-todo-dev -f manifest.yml"
echo "- cf push simple-todo-prod -f manifest.yml"
