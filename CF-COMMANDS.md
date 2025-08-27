# Cloud Foundry Commands Quick Reference

## 🚀 Deployment Commands

### Quick Deploy
```bash
# Development environment
./cf-deploy.sh dev

# Production environment  
./cf-deploy.sh prod

# Verify deployment readiness
./cf-verify.sh
```

### Manual Deploy
```bash
# Build application
./mvnw clean package

# Deploy to CF
cf push simple-todo-dev -f manifest.yml
cf push simple-todo-prod -f manifest.yml
```

## 🔧 Application Management

### Basic Operations
```bash
# View applications
cf apps

# View specific app details
cf app simple-todo-prod

# Start/Stop/Restart
cf start simple-todo-prod
cf stop simple-todo-prod
cf restart simple-todo-prod

# Restage (rebuild)
cf restage simple-todo-prod
```

### Scaling
```bash
# Scale instances
cf scale simple-todo-prod -i 3

# Scale memory
cf scale simple-todo-prod -m 2G

# Scale disk
cf scale simple-todo-prod -k 1G
```

## 📊 Monitoring & Debugging

### Logs
```bash
# Recent logs
cf logs simple-todo-prod --recent

# Follow live logs
cf logs simple-todo-prod

# Follow logs for specific component
cf logs simple-todo-prod | grep "ERROR"
```

### Application Info
```bash
# Environment variables
cf env simple-todo-prod

# Application events
cf events simple-todo-prod

# Application stats
cf app simple-todo-prod
```

### SSH Access
```bash
# SSH into app container
cf ssh simple-todo-prod

# SSH with specific command
cf ssh simple-todo-prod -c "ps aux"

# SSH to specific instance
cf ssh simple-todo-prod -i 1
```

## 🗄️ Service Management

### PostgreSQL Service
```bash
# Create service
cf create-service postgres shared-postgres simple-todo-postgres

# View services
cf services

# View service details
cf service simple-todo-postgres

# Bind service to app
cf bind-service simple-todo-prod simple-todo-postgres

# Unbind service
cf unbind-service simple-todo-prod simple-todo-postgres

# Delete service
cf delete-service simple-todo-postgres
```

### Service Keys (for external access)
```bash
# Create service key
cf create-service-key simple-todo-postgres dev-access

# View service key
cf service-key simple-todo-postgres dev-access

# Delete service key
cf delete-service-key simple-todo-postgres dev-access
```

## 🌍 Multi-Environment Management

### Space Management
```bash
# List spaces
cf spaces

# Target specific space
cf target -s development
cf target -s staging
cf target -s production

# Create new space
cf create-space staging
```

### Organization Management
```bash
# List orgs
cf orgs

# Target specific org
cf target -o my-org

# View current target
cf target
```

## 🔒 Security & Access

### User Management
```bash
# Set space role
cf set-space-role user@example.com my-org development SpaceDeveloper

# Unset space role
cf unset-space-role user@example.com my-org development SpaceDeveloper

# View space users
cf space-users my-org development
```

### Security Groups
```bash
# View security groups
cf security-groups

# Bind security group
cf bind-security-group my-sg my-org development
```

## 📈 Health & Monitoring

### Health Checks
```bash
# View app health
curl https://simple-todo-prod.cfapps.io/actuator/health

# View detailed health (if enabled)
curl https://simple-todo-prod.cfapps.io/actuator/health -H "Authorization: Bearer TOKEN"
```

### Metrics
```bash
# View app metrics
curl https://simple-todo-prod.cfapps.io/actuator/metrics

# View specific metric
curl https://simple-todo-prod.cfapps.io/actuator/metrics/jvm.memory.used
```

## 🔄 Route Management

### Domain & Route Operations
```bash
# List routes
cf routes

# Map additional route
cf map-route simple-todo-prod cfapps.io --hostname my-custom-todo

# Unmap route
cf unmap-route simple-todo-prod cfapps.io --hostname simple-todo-prod

# Delete route
cf delete-route cfapps.io --hostname old-todo-app
```

## 🚨 Troubleshooting

### Common Issues
```bash
# App won't start - check logs
cf logs simple-todo-prod --recent | grep ERROR

# Memory issues - check usage
cf app simple-todo-prod

# Database connection issues
cf env simple-todo-prod | grep VCAP_SERVICES

# Health check failures
curl https://simple-todo-prod.cfapps.io/actuator/health
```

### Emergency Procedures
```bash
# Emergency scale down
cf scale simple-todo-prod -i 1

# Emergency restart
cf restart simple-todo-prod

# Emergency stop
cf stop simple-todo-prod

# Rollback (redeploy previous version)
cf push simple-todo-prod -f manifest.yml --strategy rolling
```

## 📋 Pre-Deployment Checklist

- [ ] Application built: `./mvnw clean package`
- [ ] CF CLI installed and logged in
- [ ] Target space set: `cf target`
- [ ] PostgreSQL service created (prod): `cf create-service postgres shared-postgres simple-todo-postgres`
- [ ] Routes available
- [ ] Environment variables configured
- [ ] Health endpoints accessible

## 🆘 Emergency Contacts

- **Platform Team**: platform@company.com
- **Database Team**: database@company.com  
- **Security Team**: security@company.com

## 📚 Additional Resources

- [Cloud Foundry CLI Reference](https://docs.cloudfoundry.org/cf-cli/)
- [Cloud Foundry Developer Guide](https://docs.cloudfoundry.org/devguide/)
- [Spring Boot on Cloud Foundry](https://docs.spring.io/spring-boot/docs/current/reference/html/cloud-deployment.html#cloud-deployment.cloud-foundry)
